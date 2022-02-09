package br.com.jadson.mailframe.client.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class MailDto {

    private String id;

    @NotBlank
    @Email(message = "field is not a valid Email")
    private String from;

    @NotEmpty(message = "field must have at least one Email")
    private List<String> to;

    private List<String> cc;
    private List<String> bcc;

    @Email(message = "field is not a valid Email")
    private String replyTo;

    @NotBlank(message = "field must be filled")
    private String subject;
    @NotBlank(message = "field must be filled")
    private String text;

    /***
     * Base 64 files to be sent in the Email
     */
    private List<AttachmentDto> attachments;

    @NotBlank(message = "field should not be blank")
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
