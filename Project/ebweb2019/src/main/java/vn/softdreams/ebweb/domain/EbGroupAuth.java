package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A EbGroupAuth.
 */
@Entity
@Table(name = "ebgroupauth")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbGroupAuth extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name = "authorityid")
    private EbAuthority ebAuthority;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name = "groupid")
    private EbGroup ebGroup;

    public EbGroupAuth(EbAuthority ebAuthority, EbGroup groupz) {

    }

    public EbGroupAuth() {

    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EbAuthority getEbAuthority() {
        return ebAuthority;
    }

    public void setEbAuthority(EbAuthority ebAuthority) {
        this.ebAuthority = ebAuthority;
    }

    public EbGroup getEbGroup() {
        return ebGroup;
    }

    public EbGroupAuth groups(EbGroup ebGroup) {
        this.ebGroup = ebGroup;
        return this;
    }

    public void setEbGroup(EbGroup ebGroup) {
        this.ebGroup = ebGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EbGroupAuth ebGroupAuth = (EbGroupAuth) o;
        if (ebGroupAuth.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ebGroupAuth.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EbGroupAuth{" +
            "id=" + getId() +
            ", ebAuthority=" + getEbAuthority() +
            "}";
    }
}
