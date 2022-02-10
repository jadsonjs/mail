package br.com.jadson.mailframe.client;

import br.com.jadson.mailframe.client.dtos.AttachmentDto;
import br.com.jadson.mailframe.client.dtos.MailDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * Class to builder a Mail Dto Object.
 * https://www.vogella.com/tutorials/DesignPatternBuilder/article.html
 */
public class MailDtoBuilder {


    private String from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String replyTo;
    private String subject;
    private String text;
    private Map<String, byte[]> attachments = new HashMap<>();
    private String application;

    /**
     * Minimum constructor
     *
     * Build an anonymous email, the "form" field will be replaced by "noReply"
     *
     * @param to
     * @param subject
     * @param text
     * @param application
     */
    public MailDtoBuilder(String to, String subject, String text, String application) {
        this(null, Arrays.asList(new String[]{to}), null, null, null, subject, text, application);
    }

    /**
     * Minimum constructor
     *
     * Build an anonymous email, the "form" field will be replaced by "noReply"
     *
     * @param to
     * @param subject
     * @param text
     * @param application
     */
    public MailDtoBuilder(List<String> to, String subject, String text, String application) {
        this(null, to, null, null, null, subject, text, application);
    }

    /**
     *
     * @param from
     * @param to
     * @param subject
     * @param text
     * @param application
     */
    public MailDtoBuilder(String from, String to, String subject, String text, String application) {
        this(from, Arrays.asList(new String[]{to}), null, null, null, subject, text, application);
    }

    /**
     *
     * @param from
     * @param to
     * @param subject
     * @param text
     * @param application
     */
    public MailDtoBuilder(String from, List<String> to, String subject, String text, String application) {
        this(from, to, null, null, null, subject, text, application);
    }

    /**
     * Complete constructor
     *
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param replyTo
     * @param subject
     * @param text
     * @param application
     */
    public MailDtoBuilder(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo, String subject, String text, String application) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.replyTo = replyTo;
        this.subject = subject;
        this.text = text;
        this.application = application;
    }

    public MailDtoBuilder setFrom(String s) {
        from = s;
        return this;
    }

    public MailDtoBuilder addTo(String s) {
        if(to == null)
            to = new ArrayList<>();
        to.add(s);
        return this;
    }


    public MailDtoBuilder addCc(String cc) {
        if(cc == null || cc.trim().isBlank())
            throw new IllegalArgumentException("bcc: should have at least one mail");

        if(this.cc == null)
            this.cc = new ArrayList<>();
        this.cc.add(cc);
        return this;
    }

    public MailDtoBuilder addBcc(String bcc) {
        if(bcc == null || bcc.trim().isBlank())
            throw new IllegalArgumentException("bcc: should have at least one mail");

        if(this.bcc == null)
            this.bcc = new ArrayList<>();
        this.bcc.add(bcc);
        return this;
    }

    public MailDtoBuilder setCc(List<String> cc) {
        if(cc == null || cc.size() == 0)
            throw new IllegalArgumentException("cc: should have at least one mail");
        this.cc = cc;
        return this;
    }

    public MailDtoBuilder setBcc(List<String> bcc) {
        if(bcc == null || bcc.size() == 0)
            throw new IllegalArgumentException("bcc: should have at least one mail");
        this.bcc = bcc;
        return this;
    }

    public MailDtoBuilder addFile(String fileName, byte[] file) {
        if(this.attachments == null){
            this.attachments = new HashMap<>();
        }else {
            if (this.attachments.containsKey(fileName))
                throw new IllegalArgumentException("Attached files can not have same name");
        }
        this.attachments.put(fileName, file);
        return this;
    }

    public MailDtoBuilder setReplyTO(String s) {
        replyTo = s;
        return this;
    }

    public MailDtoBuilder addFile(String fileName, File file) {
        try {
            this.addFile(fileName, Files.readAllBytes(file.toPath()) );
        } catch (IOException e) {
            throw  new IllegalArgumentException(e.getMessage());
        }
        return this;
    }

    public MailDtoBuilder addFile(String fileName, InputStream ins) {
        try {
            this.addFile(fileName, ins.readAllBytes() );
        } catch (IOException e) {
            throw  new IllegalArgumentException(e.getMessage());
        }
        return this;
    }

    public MailDto build() {
        MailDto dto = new MailDto(from, to, subject, text, application);

        if(cc != null && cc.size() > 0)
            dto.setCc(cc);

        if(bcc != null && bcc.size() > 0)
            dto.setBcc(bcc);

        if(replyTo != null && ! replyTo.isBlank())
            dto.setReplyTo(replyTo);

        // attachments
        if(attachments.size() > 0){
            List<AttachmentDto> atts = new ArrayList<>();
            for (String key: attachments.keySet()) {
                atts.add(new AttachmentDto(key, Base64.getEncoder().encodeToString(attachments.get(key))));
            }
            dto.setAttachments(atts);
        }

        return dto;
    }


}
