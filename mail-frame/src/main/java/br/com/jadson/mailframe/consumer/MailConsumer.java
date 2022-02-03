/*
 *
 * mail-frame
 * MailConsumer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.consumer;

import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * The consumer listens for messages sent to the mail queue
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@Component
public class MailConsumer {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.noreply}")
    private String noReply;

    /**
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
     * @param mail
     */
    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload Mail mail) {

        mail.setSendDate(LocalDateTime.now());
        try{

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo().toArray(new String[0]));

            if(mail.getCc() != null && mail.getCc().size() > 0 )
                helper.setCc(mail.getCc().toArray(new String[0]));
            if(mail.getBcc() != null && mail.getCc().size() > 0 )
                helper.setBcc(mail.getBcc().toArray(new String[0]));

            if(mail.getReplyTo() != null || mail.getReplyTo().trim().isEmpty())
                helper.setReplyTo(noReply);
            else
                helper.setReplyTo(mail.getReplyTo());

            helper.setSubject(mail.getSubject());
            helper.setText("<html><body><br><br><br><br><br><br><br>"+mail.getText()+"</body></html>");

            File file = new File("/Users/jadson/Desktop/test1.txt");
            byte[] fileContent = Files.readAllBytes(file.toPath());

            File file2 = new File("/Users/jadson/Desktop/test2.txt");
            byte[] fileContent2 = Files.readAllBytes(file.toPath());

            File file3 = new File("/Users/jadson/Desktop/test3.txt");
            byte[] fileContent3 = Files.readAllBytes(file.toPath());

            helper.addAttachment("Test1.txt", new ByteArrayResource(fileContent));
            helper.addAttachment("Test2.txt", new ByteArrayResource(fileContent2));
            helper.addAttachment("Test3.txt", new ByteArrayResource(fileContent3));

            mail.setStatus(MailStatus.SENT);
        } catch (Exception e){
            mail.setStatus(MailStatus.ERROR);
        } finally {
            mailRepository.save(mail);
        }
    }

    /**
     * Delay each mail send between 1s and 30s.
     * This is a technique to avoid send span
     */
    private void delay() {
        SecureRandom r = new SecureRandom();
        int low = 1;
        int high = 30;
        int result = r.nextInt(high-low) + low;
        try{ Thread.sleep(result * 1000);} catch (Exception e){}
    }
}

