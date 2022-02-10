package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.dtos.MailDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MailDtoBuilderTest {


    @Test
    void minimumCreationTest() {

        MailDto dto = new MailDtoBuilder("xxxxx@gmail.com",
                "Email Test", "<p> This is an email with limit</p>", "mail-client").build();

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("xxxxx@gmail.com", dto.getTo().get(0));
        Assertions.assertEquals("Email Test", dto.getSubject());
        Assertions.assertEquals("<p> This is an email with limit</p>", dto.getText());
        Assertions.assertEquals("mail-client", dto.getApplication());

    }

    @Test
    void minimumCreationWithFromTest() {

        MailDto dto = new MailDtoBuilder("yyyyy@gmail.com", "xxxxx@gmail.com",
                "Email Test", "<p> This is an email with limit</p>", "mail-client").build();

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("yyyyy@gmail.com", dto.getFrom());
        Assertions.assertEquals("xxxxx@gmail.com", dto.getTo().get(0));
        Assertions.assertEquals("Email Test", dto.getSubject());
        Assertions.assertEquals("<p> This is an email with limit</p>", dto.getText());
        Assertions.assertEquals("mail-client", dto.getApplication());

    }

    @Test
    void replyToTest() {

        MailDto dto = new MailDtoBuilder("yyyyy@gmail.com", "xxxxx@gmail.com",
                "Email Test", "<p> This is an email with limit</p>", "mail-client")
                .setReplyTO("reply@gmail.com")
                .build();

        Assertions.assertNotNull(dto);
        Assertions.assertEquals("yyyyy@gmail.com", dto.getFrom());
        Assertions.assertEquals("xxxxx@gmail.com", dto.getTo().get(0));
        Assertions.assertEquals("Email Test", dto.getSubject());
        Assertions.assertEquals("<p> This is an email with limit</p>", dto.getText());
        Assertions.assertEquals("mail-client", dto.getApplication());

        Assertions.assertEquals("reply@gmail.com", dto.getReplyTo());

    }


    @Test
    void addCcTest() {

        MailDto dto = new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                        .addCc("wwwwww@gmail.com")
                        .addCc("zzzzz@gmail.com")
                        .build();

        Assertions.assertEquals(2, dto.getCc().size());
        Assertions.assertEquals("wwwwww@gmail.com", dto.getCc().get(0));
        Assertions.assertEquals("zzzzz@gmail.com", dto.getCc().get(1));

    }

    @Test
    void addBccTest() {

        MailDto dto = new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                .addBcc("wwwwww@gmail.com")
                .addBcc("zzzzz@gmail.com")
                .build();

        Assertions.assertEquals(2, dto.getBcc().size());
        Assertions.assertEquals("wwwwww@gmail.com", dto.getBcc().get(0));
        Assertions.assertEquals("zzzzz@gmail.com", dto.getBcc().get(1));

    }

    @Test
    void setCcToNullTest() {

        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                        .setCc(null)
                        .build());

        assertTrue(thrown.getMessage().contains("cc: should have at least one mail"));

    }

    @Test
    void setBccToNullTest() {

        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                        .setBcc(null)
                        .build());

        assertTrue(thrown.getMessage().contains("bcc: should have at least one mail"));

    }

    @Test
    void addFileTest() throws IOException {

        MailDto dto = new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                .addFile("file1", File.createTempFile("teste1", "txt"))
                .addFile("file2", File.createTempFile("teste2", "txt"))
                .build();

        Assertions.assertEquals(2, dto.getAttachments().size());
        Assertions.assertTrue(dto.getAttachments().get(0).getName().startsWith("file"));

    }

    @Test
    void addFileSameNameTest() {

        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> new MailDtoBuilder("xxxxx@gmail.com", "Email Test", "<p> This is an email with limit</p>", "mail-client")
                        .addFile("file1.txt", File.createTempFile("teste1", "txt"))
                        .addFile("file1.txt", File.createTempFile("teste2", "txt"))
                        .build());

        assertTrue(thrown.getMessage().contains("Attached files can not have same name"));

    }
}