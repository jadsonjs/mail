package br.com.jadson.mailframe.repositories;

import br.com.jadson.mailframe.models.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MailRepository extends JpaRepository<Mail, UUID> {

}
