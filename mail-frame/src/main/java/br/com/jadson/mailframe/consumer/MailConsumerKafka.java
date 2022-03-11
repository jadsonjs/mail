package br.com.jadson.mailframe.consumer;

import br.com.jadson.mailframe.models.Attachment;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailCounter;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import br.com.jadson.mailframe.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Consumer message come from kafka
 */
@Component
public class MailConsumerKafka {

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

    @KafkaListener(topics = "${order.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receive(Mail mail) {
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
