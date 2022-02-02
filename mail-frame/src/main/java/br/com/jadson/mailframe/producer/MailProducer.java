/*
 *
 * mail-frame
 * MailProducer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.producer;

import br.com.jadson.mailframe.dtos.MailDto;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Write messages to the rabbitMQ queue
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@Component
public class MailProducer {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.name}")
    private String queue;

    /**
     * Send the mail to rabbitMQ and save it on database to be processed later
     * @param mail
     * @return
     */
    public Mail sendToQueue(Mail mail) {

        try {

            mail.setStatus(MailStatus.PROCESSING);
            mail = mailRepository.save(mail);

            rabbitTemplate.convertAndSend(queue, mail);

            return mail;

        } catch (Exception e){
            e.printStackTrace();
            mailRepository.delete(mail);
            throw new RuntimeException("Could not sent mail: "+e.getMessage());
        }
    }

}


