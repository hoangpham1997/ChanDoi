package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCPaymentDetailSalary.
 */
@Entity
@Table(name = "mcpaymentdetailsalary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCPaymentDetailSalary implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcpaymentid")
    private UUID mCPaymentID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "accumamount", precision = 10, scale = 2)
    private BigDecimal accumAmount;

    @Column(name = "accumamountoriginal", precision = 10, scale = 2)
    private BigDecimal accumAmountOriginal;

    @Column(name = "currentmonthamount", precision = 10, scale = 2)
    private BigDecimal currentMonthAmount;

    @Column(name = "currentmonthamountoriginal", precision = 10, scale = 2)
    private BigDecimal currentMonthAmountOriginal;

    @Column(name = "payamount", precision = 10, scale = 2)
    private BigDecimal payAmount;

    @Column(name = "payamountoriginal", precision = 10, scale = 2)
    private BigDecimal payAmountOriginal;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "departmentid")
    private OrganizationUnit organizationUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getmCPaymentID() {
        return mCPaymentID;
    }

    public MCPaymentDetailSalary mCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
        return this;
    }

    public void setmCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public MCPaymentDetailSalary employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getDescription() {
        return description;
    }

    public MCPaymentDetailSalary description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAccumAmount() {
        return accumAmount;
    }

    public MCPaymentDetailSalary accumAmount(BigDecimal accumAmount) {
        this.accumAmount = accumAmount;
        return this;
    }

    public void setAccumAmount(BigDecimal accumAmount) {
        this.accumAmount = accumAmount;
    }

    public BigDecimal getAccumAmountOriginal() {
        return accumAmountOriginal;
    }

    public MCPaymentDetailSalary accumAmountOriginal(BigDecimal accumAmountOriginal) {
        this.accumAmountOriginal = accumAmountOriginal;
        return this;
    }

    public void setAccumAmountOriginal(BigDecimal accumAmountOriginal) {
        this.accumAmountOriginal = accumAmountOriginal;
    }

    public BigDecimal getCurrentMonthAmount() {
        return currentMonthAmount;
    }

    public MCPaymentDetailSalary currentMonthAmount(BigDecimal currentMonthAmount) {
        this.currentMonthAmount = currentMonthAmount;
        return this;
    }

    public void setCurrentMonthAmount(BigDecimal currentMonthAmount) {
        this.currentMonthAmount = currentMonthAmount;
    }

    public BigDecimal getCurrentMonthAmountOriginal() {
        return currentMonthAmountOriginal;
    }

    public MCPaymentDetailSalary currentMonthAmountOriginal(BigDecimal currentMonthAmountOriginal) {
        this.currentMonthAmountOriginal = currentMonthAmountOriginal;
        return this;
    }

    public void setCurrentMonthAmountOriginal(BigDecimal currentMonthAmountOriginal) {
        this.currentMonthAmountOriginal = currentMonthAmountOriginal;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public MCPaymentDetailSalary payAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
        return this;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmountOriginal() {
        return payAmountOriginal;
    }

    public MCPaymentDetailSalary payAmountOriginal(BigDecimal payAmountOriginal) {
        this.payAmountOriginal = payAmountOriginal;
        return this;
    }

    public void setPayAmountOriginal(BigDecimal payAmountOriginal) {
        this.payAmountOriginal = payAmountOriginal;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCPaymentDetailSalary orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public MCPaymentDetailSalary organizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
        return this;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
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
        MCPaymentDetailSalary mCPaymentDetailSalary = (MCPaymentDetailSalary) o;
        if (mCPaymentDetailSalary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPaymentDetailSalary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPaymentDetailSalary{" +
            "id=" + getId() +
            ", mCPaymentID=" + getmCPaymentID() +
            ", employeeID=" + getEmployeeID() +
            ", description='" + getDescription() + "'" +
            ", accumAmount=" + getAccumAmount() +
            ", accumAmountOriginal=" + getAccumAmountOriginal() +
            ", currentMonthAmount=" + getCurrentMonthAmount() +
            ", currentMonthAmountOriginal=" + getCurrentMonthAmountOriginal() +
            ", payAmount=" + getPayAmount() +
            ", payAmountOriginal=" + getPayAmountOriginal() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
