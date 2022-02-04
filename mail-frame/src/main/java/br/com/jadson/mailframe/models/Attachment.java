package br.com.jadson.mailframe.models;

import br.com.jadson.mailframe.exceptions.MailValidationException;
import lombok.Data;

@Data
public class Attachment {

    /** Name of file */
    private String name;

    /***
     * Bytes of file
     */
    private byte[] content;

    public void validate() {
        if(name == null || name.isBlank())
            throw new MailValidationException("\"Attachment Name\" field should not be blank");

        if(content == null || content.length == 0)
            throw new MailValidationException("\"Attachment Content\" field should not be blank");
    }
}
