/*
 *
 * mail-frame
 * MailController
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.controller;

import br.com.jadson.mailframe.client.dtos.MailDto;
import br.com.jadson.mailframe.converters.MailConverter;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.producer.MailProducer;
import br.com.jadson.mailframe.producer.MailProducerKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * A controller to receive the mail data.
 *
 * @author Jadson Santos - jadsonjs@gmail.com
 */
@RestController
public class MailController {

    @Autowired
    MailProducer producer;

    @Autowired
    MailConverter converter;

    @Autowired
    MailProducerKafka producer2;

    @GetMapping("/up")
    public ResponseEntity<String> up() {
        return new ResponseEntity<>("UP", HttpStatus.OK);
    }

    /**
     * Send mail asynchronously. Put the mail data to queue and return ok to client.
     * @return
     */
    @PostMapping("/send/mq")
    public ResponseEntity<MailDto> send(@Valid @RequestBody MailDto dto) {
        Mail mail = converter.toModel(dto);
        mail = producer.sendToQueue(mail);
        return new ResponseEntity<>(converter.toDto(mail), HttpStatus.CREATED);
    }

    /**
     * Send mail asynchronously. Put the mail data to queue and return ok to client.
     * @return
     */
    @PostMapping("/send/kafka")
    public ResponseEntity<MailDto> sendKafka(@Valid @RequestBody MailDto dto) {
        Mail mail = converter.toModel(dto);
        mail = producer2.send(mail);
        return new ResponseEntity<>(converter.toDto(mail), HttpStatus.CREATED);
    }
}

