package br.com.jadson.mailframe.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Column(name = "_to")
    private String to;

    private String cc;

    private String bcc;

    private String replyTo = "noreply@gmail.com";

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime sendDate;

    @Enumerated(EnumType.STRING)
    private MailStatus status;
}
