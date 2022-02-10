package br.com.jadson.mailframe.models;

import br.com.jadson.mailframe.exceptions.MailValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailTest {

    @Test
    void validateMailWithOutTo() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("");
        m.setSubject("Test of mail");
        m.setText(" this is the mail body ....");
        m.setApplicationName("mail-client-app");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("to: field should not be blank"));
    }

    @Test
    void validateMailWithOutSubject() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("");
        m.setText(" this is the mail body ....");
        m.setApplicationName("mail-client-app");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("subject: field should not be blank"));
    }

    @Test
    void validateMailWithOutText() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("teste");
        m.setText(null);
        m.setApplicationName("mail-client-app");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("text: field should not be blank"));
    }

    @Test
    void validateMailWithInvalidFrom() {
        Mail m = new Mail();
        m.setFrom("xxxx_gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("teste");
        m.setText(" this is the mail text ....");
        m.setApplicationName("mail-client-app");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("from: \"xxxx_gmail.com\" is not valid Email") );
    }


    @Test
    void validateMailWithInvalidTo() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@");
        m.setSubject("teste");
        m.setText(" this is the mail text ....");
        m.setApplicationName("mail-client-app");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("to: \"xxxx@\" is not valid Email") );
    }

    @Test
    void validateMailWithInvalidCc() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("teste");
        m.setText(" this is the mail text ....");
        m.setApplicationName("mail-client-app");

        m.setCc("asdfghjkl;abc@gmail.com;");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("cc: \"asdfghjkl\" is not valid Email") );
    }


    @Test
    void validateMailWithInvalidBcc() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("teste");
        m.setText(" this is the mail text ....");
        m.setApplicationName("mail-client-app");

        m.setBcc("asdfghjkl;abc@gmail.com;");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("bcc: \"asdfghjkl\" is not valid Email") );
    }

    @Test
    void validateMailWithOutApplicationName() {
        Mail m = new Mail();
        m.setFrom("xxxx@gmail.com");
        m.setTo("xxxx@gmail.com");
        m.setSubject("Test of mail");
        m.setText(" this is the mail body ....");

        MailValidationException thrown = Assertions.assertThrows(
                MailValidationException.class, () -> m.validate());

        assertTrue(thrown.getMessage().contains("application: field should not be blank"));
    }
}