/*
 *
 * mail-frame
 * MailController
 * @since 31/01/2022
 */
package br.com.jadson.mailframe.controller;

import br.com.jadson.mailframe.producer.MailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller to receive the mail data.
 *
 * @author Jadson Santos - jadson.santos@ufrn.br
 */
@RestController
public class MailController {

    @Autowired
    MailProducer producer;

    @PostMapping("/send")
    public ResponseEntity<String> send() {
        producer.send("{\n" +
                "   \"from\":\"jadson.santos@xxxx.com\",\n" +
                "   \"to\":\"jadson@xxxx.com\",\n" +
                "   \"body\":\"This is a message!\",\n" +
                "}");
        return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
    }
}

