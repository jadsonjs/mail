package br.com.jadson.mailframe.models;

import br.com.jadson.mailframe.converters.StringConverter;
import br.com.jadson.mailframe.exceptions.MailValidationException;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Entity
@Table(name = "MAIL")
public class Mail implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(name = "from_")
    private String from;

    /** A list of email addresses separanted by ";" */
    @Column(name = "to_", columnDefinition = "TEXT")
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

    /** The application that send this email */
    private String applicationName;

    private LocalDateTime sendDate;

    @Column(length = 60)
    @Enumerated(EnumType.STRING)
    private MailStatus status;

    /**
     * When something goes wrong, save the error
     */
    @Column
    private String error;

    public String[] getToAsArray() {
        return new StringConverter().stringToList(to).toArray(new String[0]);
    }

    public String[] getCcAsArray() {
        return new StringConverter().stringToList(cc).toArray(new String[0]);
    }

    public String[] getBccAsArray() {
        return new StringConverter().stringToList(bcc).toArray(new String[0]);
    }

    public void validate() {

        if(from == null) {
            throw new MailValidationException("from: field should not be null");
        }else
            validateEmailsList("from", from);

        if(to == null) {
            throw new MailValidationException("to: field should not be null");
        }else
            validateEmailsList("to", to);

        if(cc != null)
            validateEmailsList("cc", cc);

         if(bcc != null)
             validateEmailsList("bcc", bcc);

         if(replyTo != null)
             validateEmailAddress("replyTo", replyTo);


        if(applicationName == null || applicationName.isBlank())
            throw new MailValidationException("application: field should not be blank");

        if(subject == null || subject.isBlank())
            throw new MailValidationException("subject: field should not be blank");

        if(text == null || text.isBlank())
            throw new MailValidationException("text: field should not be blank");


        if(attachments != null)
            for (Attachment att :  attachments){
                att.validate();
            }


    }

    private void validateEmailsList(String name, String value) {
        List<String> emails = new StringConverter().stringToList(value);
        if(emails.size() > 100)
            throw new MailValidationException(name+": field should have a maximum of 100 emails");
        for (String e : emails){
            validateEmailAddress(name, e);
        }
    }


    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static Matcher matcher;


    public void validateEmailAddress(final String name, final String email) {
        matcher = pattern.matcher(email);
        if( ! matcher.matches() )
            throw new MailValidationException(name+": "+"\""+email+"\" is not valid Email");

    }

}
