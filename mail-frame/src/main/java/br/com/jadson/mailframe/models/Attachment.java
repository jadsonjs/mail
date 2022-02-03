package br.com.jadson.mailframe.models;

import lombok.Data;

@Data
public class Attachment {

    /** Name of file */
    private String name;

    /***
     * Bytes of file
     */
    private byte[] content;
}
