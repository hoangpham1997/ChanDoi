package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A PaymentClause.
 */
@Entity
@Table(name = "paymentclause")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentClause implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Size(max = 25)
    @Column(name = "paymentclausecode", length = 25, nullable = false)
    private String paymentClauseCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "paymentclausename", length = 512, nullable = false)
    private String paymentClauseName;

    @Column(name = "duedate", nullable = false)
    private Integer dueDate;

    @Column(name = "discountdate", nullable = false)
    private Integer discountDate;

    @Column(name = "discountpercent", precision = 10, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "overdueinterestpercent", precision = 10, scale = 2)
    private BigDecimal overDueInterestPercent;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        PaymentClause paymentClause = (PaymentClause) o;
        if (paymentClause.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentClause.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentClause{" +
            "id=" + getId() +
            "}";
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public String getPaymentClauseCode() {
        return paymentClauseCode;
    }

    public void setPaymentClauseCode(String paymentClauseCode) {
        this.paymentClauseCode = paymentClauseCode;
    }

    public String getPaymentClauseName() {
        return paymentClauseName;
    }

    public void setPaymentClauseName(String paymentClauseName) {
        this.paymentClauseName = paymentClauseName;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDiscountDate() {
        return discountDate;
    }

    public void setDiscountDate(Integer discountDate) {
        this.discountDate = discountDate;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getOverDueInterestPercent() {
        return overDueInterestPercent;
    }

    public void setOverDueInterestPercent(BigDecimal overDueInterestPercent) {
        this.overDueInterestPercent = overDueInterestPercent;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PaymentClause isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public PaymentClause isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }
}
