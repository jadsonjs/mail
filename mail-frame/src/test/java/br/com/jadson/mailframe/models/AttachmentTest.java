package br.com.jadson.mailframe.models;

import br.com.jadson.mailframe.client.MailDtoBuilder;
import br.com.jadson.mailframe.exceptions.MailValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentTest {

    @Test
    void validateEmptyName() {

        Attachment a = new Attachment();
        a.setContent("teste1".getBytes());

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> a.validate());

        assertTrue(thrown.getMessage().contains("Attachment Name: field should not be blank"));
    }

    @Test
    void validateNullContent() {

        Attachment a = new Attachment();
        a.setName("teste1.txt");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> a.validate());

        assertTrue(thrown.getMessage().contains("Attachment Content: field should not be blank"));
    }
}