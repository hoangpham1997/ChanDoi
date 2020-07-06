package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.AccountDefault;
import vn.softdreams.ebweb.domain.Mail;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountDefault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
}
