/*
 *
 * mail-frame
 * MailConsumer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.consumer;

import br.com.jadson.mailframe.models.Attachment;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailCounter;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import br.com.jadson.mailframe.util.MailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * The consumer listens for messages sent to the mail queue.
 *
 * Receive mail data from RabbitMQ Queue and sent it
 *
 * https://stackoverflow.com/questions/120107/guidelines-for-email-newsletter-service
 *
 *  - Choose a clean hosting/smtp server. IP addresses of spamming SMTP servers are often black-listed by other ISPs.
 *
 *  - Send a simple introductory email to every subscriber, asking them to add your sender address to their safe list.
 *
 *  - Be very prudent in sending to only those people who are actually expecting it. You wouldn't want pattern
 *  recognizers of spam filters learning the smell of your content.
 *
 *  - If you don't know your smtp servers in advance, its a good practice to provide configuration options in your
 *  application for controlling batch sizes and delay between batches. Some servers don't like large batches or
 *  continuous activity.
 *
 * @author Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class MailConsumer {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailUtil mailUtil;

    @Value("${mail.noreply.address}")
    String noReply;

    @Autowired
    MailCounter mailCounter;
    

//    /**
//     * Exemple using SimpleMailMessage class
//     */
//    @RabbitListener(queues = {"${queue.name}"})
//    public void receive(@Payload Mail mail) {
//
//        mail.setSendDate(LocalDateTime.now());
//
//        try{
//
//            SimpleMailMessage message = new SimpleMailMessage();
//
//            message.setFrom(mail.getFrom());
//            message.setTo(mail.getToAsArray());
//            if(mail.getCc() != null && mail.getCc().length() > 0 )
//                message.setCc(mail.getCcAsArray());
//            if(mail.getBcc() != null && mail.getCc().length() > 0 )
//                message.setBcc(mail.getCcAsArray());
//            boolean isNoReply = false;
//            if(mail.getReplyTo() == null || mail.getReplyTo().trim().isEmpty()) {
//                message.setReplyTo(noReply);
//                isNoReply = true;
//            }else{
//                message.setReplyTo(mail.getReplyTo());
//                isNoReply = false;
//            }
//            message.setSubject( ( "["+mail.getApplicationName()+"] "+mail.getSubject() ) );
//            message.setText(mail.getText());
//
//            mailSender.send(message);
//            mail.setStatus(MailStatus.SENT);
//        } catch (Exception e){
//            mail.setStatus(MailStatus.ERROR);
//        } finally {
//            mailRepository.save(mail);
//        }
//    }


    /***
     * send HTML emails
     * @param mail
     */
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void receive(@Payload Mail mail) {

        mail.setSendDate(LocalDateTime.now());
        try{

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if(mail.getFrom() == null || mail.getFrom().trim().isEmpty()) {
                mail.setFrom(noReply);
            }

            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getToAsArray());

            if(mail.getCc() != null && mail.getCc().length() > 0 )
                helper.setCc(mail.getCcAsArray());
            if(mail.getBcc() != null && mail.getCc().length() > 0 )
                helper.setBcc(mail.getCcAsArray());

            boolean isNoReply = false;
            if(mail.getReplyTo() == null || mail.getReplyTo().trim().isEmpty()) {
                mail.setReplyTo(noReply);
                isNoReply = true;
            }else{
                isNoReply = false;
            }

            helper.setReplyTo(mail.getReplyTo());

            helper.setSubject( ( "["+mail.getApplicationName()+"] "+mail.getSubject() ) );


            helper.setText(
                    mailUtil.formatEmailText(
                        mail.getSubject(), mail.getText(), mail.getApplicationName(), mail.getId(), isNoReply),
                    true);


            if(mail.getAttachments() != null) {
                for (Attachment att : mail.getAttachments()) {
                    helper.addAttachment(att.getName(), new ByteArrayResource(att.getContent()));
                }
            }

            mailSender.send(message);

            mail.setStatus(MailStatus.SENT);

        } catch (Exception e){
            mail.setStatus(MailStatus.ERROR);
            mail.setError(e.getMessage() == null || e.getMessage().length() < 256 ? e.getMessage() : e.getMessage().substring(0, 256) );
        } finally {
            mailRepository.save(mail);
            delay();
        }
    }

    /**
     * This is a technique to avoid send span
     *
     * *** Some mail servers don’t accept large sending batches or continuous activity. ***
     * *** options in your application for controlling batch sizes and delay between batches ***
     */
    private void delay() {

        // batch size of 100 mails, so sleep 5 minutes
        if(mailCounter.getCounter() % 100 == 0) {
            try {Thread.sleep(5 * 60 * 1000);} catch (Exception e) { }
        }else{
            SecureRandom r = new SecureRandom();
            float delay = r.nextFloat();
            try{ Thread.sleep( (int) (delay * 1000)  ); } catch (Exception e){}
        }

        mailCounter.incrementCounter();
    }


}

