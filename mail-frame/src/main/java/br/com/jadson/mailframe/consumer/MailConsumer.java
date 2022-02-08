/*
 *
 * mail-frame
 * MailConsumer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.consumer;

import br.com.jadson.mailframe.models.Attachment;
import br.com.jadson.mailframe.models.Mail;
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
 * On the technical side:
 * if you can choose your SMTP server, be sure it is a “clean” SMTP server.
 * IP addresses of spamming SMTP servers are often blacklisted by other providers.
 * If you don’t know your SMTP servers in advance, it’s a good practice to provide configuration
 * options in your application for controlling batch sizes and delay between batches.
 * Some mail servers don’t accept large sending batches or continuous activity.
 *
 * Use email authentication methods, such as SPF, and DKIM to prove that your emails and your domain name belong together.
 * The nice side-effect is you help in preventing that your email domain is spoofed. Also check your reverse DNS
 * to make sure the IP address of your mail server points to the domain name that you use for sending mail.
 *
 * https://stackoverflow.com/questions/13833098/how-to-sign-javamail-with-dkim
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@Component
public class MailConsumer {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailUtil mailUtil;

    @Value("${mail.noreply}")
    String noReply;
    

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
    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload Mail mail) {

        delay();

        mail.setSendDate(LocalDateTime.now());
        try{

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getToAsArray());

            if(mail.getCc() != null && mail.getCc().length() > 0 )
                helper.setCc(mail.getCcAsArray());
            if(mail.getBcc() != null && mail.getCc().length() > 0 )
                helper.setBcc(mail.getCcAsArray());

            boolean isNoReply = false;
            if(mail.getReplyTo() == null || mail.getReplyTo().trim().isEmpty()) {
                helper.setReplyTo(noReply);
                isNoReply = true;
            }else{
                helper.setReplyTo(mail.getReplyTo());
                isNoReply = false;
            }

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
            mail.setError(e.getMessage().length() < 256 ? e.getMessage() : e.getMessage().substring(0, 256) );
        } finally {
            mailRepository.save(mail);
        }
    }

    /**
     * This is a technique to avoid send span
     *
     * *** Some mail servers don’t accept large sending batches or continuous activity. ***
     * *** options in your application for controlling batch sizes and delay between batches ***
     */
    private void delay() {
        SecureRandom r = new SecureRandom();
        int low = 1;
        int high = 10;
        int delaytime = r.nextInt(high-low) + low;
        System.out.println("delay :"+delaytime+" seconds");
        try{ Thread.sleep(delaytime * 1000);} catch (Exception e){}

        int QTY_SENT_MAIL_LAST_HOUR = 10;

        if(QTY_SENT_MAIL_LAST_HOUR > 100)
            try{ Thread.sleep(5 * 60 * 1000);} catch (Exception e){}
    }


}

