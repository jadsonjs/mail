package br.com.jadson.mailframe.dtos;

import lombok.Data;

@Data
public class AttachmentDto {

    /** Name of file */
    private String name;

    /***
     * Bytes of file in Base64 encode
     * https://www.baeldung.com/java-base64-encode-and-decode
     */
    private String content;

}
