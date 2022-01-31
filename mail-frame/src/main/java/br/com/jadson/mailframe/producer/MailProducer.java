/*
 *
 * mail-frame
 * MailProducer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.name}")
    private String queue;

    public void send(String message) {
        rabbitTemplate.convertAndSend(queue, message);
    }

}


