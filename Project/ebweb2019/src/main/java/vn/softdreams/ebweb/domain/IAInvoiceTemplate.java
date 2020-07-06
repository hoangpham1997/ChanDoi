package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A IAInvoiceTemplate.
 */
@Entity
@Table(name = "iainvoicetemplate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IAInvoiceTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 512)
    @Column(name = "invoicetempname", length = 512)
    private String invoiceTemplateName;

    @Size(max = 512)
    @Column(name = "invoicetemppath", length = 512)
    private String invoiceTemplatePath;

    @Column(name = "invoicetype")
    private Integer invoiceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceTemplateName() {
        return invoiceTemplateName;
    }

    public IAInvoiceTemplate invoiceTemplateName(String invoiceTemplateName) {
        this.invoiceTemplateName = invoiceTemplateName;
        return this;
    }

    public void setInvoiceTemplateName(String invoiceTemplateName) {
        this.invoiceTemplateName = invoiceTemplateName;
    }

    public String getInvoiceTemplatePath() {
        return invoiceTemplatePath;
    }

    public IAInvoiceTemplate invoiceTemplatePath(String invoiceTemplatePath) {
        this.invoiceTemplatePath = invoiceTemplatePath;
        return this;
    }

    public void setInvoiceTemplatePath(String invoiceTemplatePath) {
        this.invoiceTemplatePath = invoiceTemplatePath;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public IAInvoiceTemplate invoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
        return this;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
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
        IAInvoiceTemplate iAInvoiceTemplate = (IAInvoiceTemplate) o;
        if (iAInvoiceTemplate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iAInvoiceTemplate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IAInvoiceTemplate{" +
            "id=" + getId() +
            ", invoiceTemplateName='" + getInvoiceTemplateName() + "'" +
            ", invoiceTemplatePath='" + getInvoiceTemplatePath() + "'" +
            ", invoiceType=" + getInvoiceType() +
            "}";
    }
}
