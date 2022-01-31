/*
 *
 * mail-frame
 * MailConsumer
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * The consumer listens for messages sent to the mail queue
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@Component
public class MailConsumer {

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {

        try{ Thread.sleep(10000);} catch (Exception e){}

        System.out.println("Message " + fileBody);
    }
}

