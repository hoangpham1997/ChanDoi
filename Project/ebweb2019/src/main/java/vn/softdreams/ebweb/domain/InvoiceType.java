package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A InvoiceType.
 */
@Entity
@Table(name = "invoicetype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "invoicetypecode")
    private String invoiceTypeCode;

    @Column(name = "invoicetypename")
    private String invoiceTypeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceTypeCode() {
        return invoiceTypeCode;
    }

    public InvoiceType invoiceTypeCode(String invoiceTypeCode) {
        this.invoiceTypeCode = invoiceTypeCode;
        return this;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
        this.invoiceTypeCode = invoiceTypeCode;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public InvoiceType invoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
        return this;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
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
        InvoiceType invoiceType = (InvoiceType) o;
        if (invoiceType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceType{" +
            "id=" + getId() +
            ", invoiceTypeCode='" + getInvoiceTypeCode() + "'" +
            ", invoiceTypeName='" + getInvoiceTypeName() + "'" +
            "}";
    }
}
