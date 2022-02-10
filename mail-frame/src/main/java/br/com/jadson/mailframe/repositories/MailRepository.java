package br.com.jadson.mailframe.repositories;

import br.com.jadson.mailframe.models.Mail;
import br.com.jadson.mailframe.models.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MailRepository extends JpaRepository<Mail, UUID> {

    @Query(value = "SELECT COUNT(*) FROM Mail m WHERE m.status in ( :SENT, :PROCESSING ) " +
            "AND m.sendDate > :TODAY ")
    long countSavedEmails(@Param("SENT") MailStatus sent, @Param("PROCESSING") MailStatus processing, @Param("TODAY") LocalDateTime today);
}
