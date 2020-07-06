package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A IaPublishInvoice.
 */
@Entity
@Table(name = "iapublishinvoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IaPublishInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyId;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "no")
    private String no;

    @Column(name = "typeid")
    private Integer typeId;

    @Column(name = "receiptedtaxoffical")
    private String receiptedTaxOffical;

    @Column(name = "representationinlaw")
    private String representationInLaw;

    @Column(name = "status")
    private Integer status;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "iapublishinvoiceid", nullable = false)
    private Set<IaPublishInvoiceDetails> iaPublishInvoiceDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Set<IaPublishInvoiceDetails> getIaPublishInvoiceDetails() {
        return iaPublishInvoiceDetails;
    }

    public void setIaPublishInvoiceDetails(Set<IaPublishInvoiceDetails> iaPublishInvoiceDetails) {
        this.iaPublishInvoiceDetails = iaPublishInvoiceDetails;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public LocalDate getDate() {
        return date;
    }

    public IaPublishInvoice date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public IaPublishInvoice no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public IaPublishInvoice typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getReceiptedTaxOffical() {
        return receiptedTaxOffical;
    }

    public IaPublishInvoice receiptedTaxOffical(String receiptedTaxOffical) {
        this.receiptedTaxOffical = receiptedTaxOffical;
        return this;
    }

    public void setReceiptedTaxOffical(String receiptedTaxOffical) {
        this.receiptedTaxOffical = receiptedTaxOffical;
    }

    public String getRepresentationInLaw() {
        return representationInLaw;
    }

    public IaPublishInvoice representationInLaw(String representationInLaw) {
        this.representationInLaw = representationInLaw;
        return this;
    }

    public void setRepresentationInLaw(String representationInLaw) {
        this.representationInLaw = representationInLaw;
    }

    public Integer getStatus() {
        return status;
    }

    public IaPublishInvoice status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        IaPublishInvoice iaPublishInvoice = (IaPublishInvoice) o;
        if (iaPublishInvoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iaPublishInvoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IaPublishInvoice{" +
            "id=" + getId() +
            ", companyId='" + getCompanyId() + "'" +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", typeId=" + getTypeId() +
            ", receiptedTaxOffical='" + getReceiptedTaxOffical() + "'" +
            ", representationInLaw='" + getRepresentationInLaw() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
