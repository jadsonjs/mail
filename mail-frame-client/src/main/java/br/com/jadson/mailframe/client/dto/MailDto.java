package br.com.jadson.mailframe.client.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
public class MailDto {

    private String id;

    @NotBlank
    @Email(message = "\"From\" field is not a valid Email")
    private String from;

    @NotEmpty(message = "The \"to\" field must have at least one Email")
    private List<String> to;

    private List<String> cc;
    private List<String> bcc;

    @Email(message = "\"ReplyTo\" field is not a valid Email")
    private String replyTo;

    @NotBlank(message = "\"Subject\" field must be filled")
    private String subject;
    @NotBlank(message = "\"Text\" field must be filled")
    private String text;

    /***
     * Base 64 files to be sent in the Email
     */
    private List<AttachmentDto> attachments;

    @NotBlank(message = "\"Application Name\" field should not be blank")
    private String application;

    public MailDto() {
    }

    public MailDto(String from, List<String> to, String subject, String text, String application) {
        setFrom(from);
        setTo(to);
        setSubject(subject);
        setText(text);
        setApplication(application);
    }



}
