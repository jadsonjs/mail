/*
 *
 * mail-frame
 * MailController
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.controller;

import br.com.jadson.mailframe.client.dto.MailDto;
import br.com.jadson.mailframe.converters.MailConverter;
import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.producer.MailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * A controller to receive the mail data.
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@RestController
public class MailController {

    @Autowired
    MailProducer producer;

    @Autowired
    MailConverter converter;

    /**
     * Send mail asynchronously. Put the mail data to queue and return ok to client.
     * @return
     */
    @PostMapping("/send")
    public ResponseEntity<MailDto> send(@Valid @RequestBody MailDto dto) {
        try {
            Mail mail = converter.toModel(dto);
            mail = producer.sendToQueue(mail);
            return new ResponseEntity<>(converter.toDto(mail), HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

