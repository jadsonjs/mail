package br.com.jadson.mailframe.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "MAIL")
public class Mail implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(name = "_from")
    private String from;

    /** A list of email addresses separanted by ";" */
    @Column(name = "_to", columnDefinition = "TEXT")
    private String to;

    /** A list of email addresses separanted by ";" */
    @Column(columnDefinition = "TEXT")
    private String cc;

    /** A list of email addresses separanted by ";" */
    @Column(columnDefinition = "TEXT")
    private String bcc;

    private String replyTo;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Transient
    private List<Attachment> attachments;

    private LocalDateTime sendDate;

    @Column(length = 60)
    @Enumerated(EnumType.STRING)
    private MailStatus status;

    public List<String> getTo() {
        return stringToArray(to);
    }

    public Mail setTo(List<String> to) {
        return arrayToString(to);
    }

    public List<String> getCc() {
        return stringToArray(cc);
    }

    public Mail setCc(List<String> cc) {
        return arrayToString(cc);
    }

    public List<String> getBcc() {
        return stringToArray(bcc);
    }

    public Mail setBcc(List<String> bcc) {
        return arrayToString(bcc);
    }


    final String DELIMITER = ";";

    private List<String> stringToArray(String to) {
        if (to != null && to.length() > 0) {
            Arrays.asList(to.split(DELIMITER));
        }
        return Collections.emptyList();
    }

    private Mail arrayToString(List<String> to) {
        if(to != null && to.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            for (String t : to) {
                buffer.append(t+DELIMITER);
            }
            this.to = buffer.toString();
        }
        return this;
    }
}
