/*
 *
 * mail-frame
 * MailProducer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.producer;

import br.com.jadson.mailframe.exceptions.MailValidationException;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Write messages to the rabbitMQ queue
 *
 * @author Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class MailProducer {

    @Autowired
    MailRepository mailRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${mail.limit.diary}")
    private Integer limitDiary;



    /**
     * Send the mail to rabbitMQ and save it on database to be processed later
     * @param mail
     * @return
     */
    public Mail sendToQueue(Mail mail) {

        try {

            mail.validate();

            if(mailRepository.countSavedEmails(MailStatus.SENT, MailStatus.PROCESSING, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) > limitDiary){
                throw new MailValidationException("Dialy Limit Exceeded: "+limitDiary);
            }

            mail.setStatus(MailStatus.PROCESSING);
            mail = mailRepository.save(mail);

            rabbitTemplate.convertAndSend(queue, mail);

            return mail;

        } catch (Exception e){
            // e.printStackTrace();
            mailRepository.delete(mail);
            throw new MailValidationException(e.getMessage());
        }
    }

}


