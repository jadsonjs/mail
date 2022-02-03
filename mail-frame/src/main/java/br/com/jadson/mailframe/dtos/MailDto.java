package br.com.jadson.mailframe.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class MailDto {

    private String id;

    @NotBlank
    @Email(message = "\"From\" field is not a valid Email")
    private String from;

    @NotBlank(message = "The \"to\" field must have at least one Email")
    @Max(value = 100, message = "The \"to\" field should not be greater than 100 Email addresses")
    private List<String> to;


    @Max(value = 100, message = "The \"cc\" field should not be greater than 100 Email addresses")
    private List<String> cc;
    @Max(value = 100, message = "The \"bcc\" field should not be greater than 100 Email addresses")
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
    @Max(value = 10, message = "The \"attachments\" field should not be greater than 10 files")
    private List<AttachmentDto> attachments;
}
