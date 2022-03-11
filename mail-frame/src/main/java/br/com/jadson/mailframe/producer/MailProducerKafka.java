package br.com.jadson.mailframe.producer;

import br.com.jadson.mailframe.exceptions.MailValidationException;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import br.com.jadson.mailframe.repositories.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

/**
 * Write messages to the apache kafka
 *
 * @author Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class MailProducerKafka {


    @Autowired
    MailRepository mailRepository;

    @Value("${order.topic}")
    private String orderTopic;

    @Value("${mail.limit.diary}")
    private Integer limitDiary;


    @Autowired
    KafkaTemplate kafkaTemplate;

    public Mail send(@RequestBody Mail mail) {

        try {

            mail.validate();

            if(mailRepository.countSavedEmails(MailStatus.SENT, MailStatus.PROCESSING, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) > limitDiary){
                throw new MailValidationException("Dialy Limit Exceeded: "+limitDiary);
            }

            mail.setStatus(MailStatus.PROCESSING);
            mail = mailRepository.save(mail);

            kafkaTemplate.send(orderTopic, mail.getId().toString(), mail);

            return mail;

        } catch (Exception e){
            // e.printStackTrace();
            mailRepository.delete(mail);
            throw new MailValidationException(e.getMessage());
        }
    }
}
