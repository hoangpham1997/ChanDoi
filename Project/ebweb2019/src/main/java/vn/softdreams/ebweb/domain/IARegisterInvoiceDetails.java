package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A IARegisterInvoiceDetails.
 */
@Entity
@Table(name = "iaregisterinvoicedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IARegisterInvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "iaregisterinvoiceid", insertable = false, updatable = false)
    private UUID iaRegisterInvoiceID;

    @Column(name = "iareportid")
    private UUID iaReportID;

    @Column(name = "purpose")
    private String purpose;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIaRegisterInvoiceID() {
        return iaRegisterInvoiceID;
    }

    public void setIaRegisterInvoiceID(UUID iaRegisterInvoiceID) {
        this.iaRegisterInvoiceID = iaRegisterInvoiceID;
    }

    public UUID getIaReportID() {
        return iaReportID;
    }

    public void setIaReportID(UUID iaReportID) {
        this.iaReportID = iaReportID;
    }

    public String getPurpose() {
        return purpose;
    }

    public IARegisterInvoiceDetails purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
        IARegisterInvoiceDetails iARegisterInvoiceDetails = (IARegisterInvoiceDetails) o;
        if (iARegisterInvoiceDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iARegisterInvoiceDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IARegisterInvoiceDetails{" +
            "id=" + getId() +
//            ", iaRegisterInvoiceID='" + getIaRegisterInvoiceID() + "'" +
            ", iaReportID='" + getIaReportID() + "'" +
            ", purpose='" + getPurpose() + "'" +
            "}";
    }
}
