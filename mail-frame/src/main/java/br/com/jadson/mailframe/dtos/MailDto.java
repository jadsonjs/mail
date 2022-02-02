package br.com.jadson.mailframe.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
public class MailDto {

    private String id;

    @NotBlank
    @Email
    private String from;
    @NotBlank
    @Email
    private String to;

    @Email
    private String cc;
    @Email
    private String bcc;

    @Email
    private String replyTo;

    @NotBlank
    private String subject;
    @NotBlank
    private String text;
}
