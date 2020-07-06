package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.service.dto.UserDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * A user.
 */
@Entity
@Table(name = "mail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mail extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MailId")
    @SequenceGenerator(name = "MailId", sequenceName = "MAIL_SEQ", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "tomail")
    private String toMail;

    @Column(name = "plainbody")
    private String plainBody;

    @Column(name = "cc")
    private String cc;

    @Column(name = "bcc")
    private String bcc;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(id, mail.id) &&
            Objects.equals(title, mail.title) &&
            Objects.equals(content, mail.content) &&
            Objects.equals(toMail, mail.toMail) &&
            Objects.equals(plainBody, mail.plainBody) &&
            Objects.equals(cc, mail.cc) &&
            Objects.equals(bcc, mail.bcc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, toMail, plainBody, cc, bcc);
    }

    public Mail(String title, String content, String toMail, String plainBody, String cc, String bcc) {
        this.title = title;
        this.content = content;
        this.toMail = toMail;
        this.plainBody = plainBody;
        this.cc = cc;
        this.bcc = bcc;
    }

    public Mail() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getPlainBody() {
        return plainBody;
    }

    public void setPlainBody(String plainBody) {
        this.plainBody = plainBody;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
}
