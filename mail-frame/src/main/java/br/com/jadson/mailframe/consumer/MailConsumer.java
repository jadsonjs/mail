/*
 *
 * mail-frame
 * MailConsumer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.consumer;

import br.com.jadson.mailframe.dtos.MailDto;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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

        System.out.println("Consuming mail....");

        delay();


        mail.setSendDate(LocalDateTime.now());
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail.getFrom());
            message.setTo(mail.getTo());
            if(mail.getCc() != null)
                message.setCc(mail.getCc());
            if(mail.getBcc() != null)
                message.setBcc(mail.getBcc());

            if(mail.getReplyTo() != null || mail.getReplyTo().trim().isEmpty())
            message.setReplyTo(noReply);

            message.setSubject(mail.getSubject());
            message.setText(mail.getText());

            mailSender.send(message);

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

