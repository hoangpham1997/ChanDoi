package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCPaymentDetailVendor.
 */
@Entity
@Table(name = "mcpaymentdetailvendor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCPaymentDetailVendor implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcpaymentid")
    private UUID mCPaymentID;

    @Column(name = "ppinvoiceid")
    private UUID pPInvoiceID;

    @Column(name = "vouchertypeid")
    private Integer voucherTypeID;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @NotNull
    @Column(name = "payableamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal payableAmount;

    @NotNull
    @Column(name = "payableamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal payableAmountOriginal;

    @NotNull
    @Column(name = "remainingamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal remainingAmount;

    @NotNull
    @Column(name = "remainingamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal remainingAmountOriginal;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "amountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal amountOriginal;

    @NotNull
    @Column(name = "discountamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "discountamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmountOriginal;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @Size(max = 25)
    @Column(name = "discountaccount", length = 25)
    private String discountAccount;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "refvoucherexchangerate", precision = 10, scale = 2)
    private BigDecimal refVoucherExchangeRate;

    @Column(name = "lastexchangerate", precision = 10, scale = 2)
    private BigDecimal lastExchangeRate;

    @Column(name = "differamount", precision = 10, scale = 2)
    private BigDecimal differAmount;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate date;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noFBook;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noMBook;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

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

    public MCPaymentDetailVendor mCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
        return this;
    }

    public void setmCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
    }

    public UUID getpPInvoiceID() {
        return pPInvoiceID;
    }

    public MCPaymentDetailVendor pPInvoiceID(UUID pPInvoiceID) {
        this.pPInvoiceID = pPInvoiceID;
        return this;
    }

    public void setpPInvoiceID(UUID pPInvoiceID) {
        this.pPInvoiceID = pPInvoiceID;
    }

    public Integer getVoucherTypeID() {
        return voucherTypeID;
    }

    public MCPaymentDetailVendor voucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
        return this;
    }

    public void setVoucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MCPaymentDetailVendor debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public MCPaymentDetailVendor dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public MCPaymentDetailVendor payableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
        return this;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public BigDecimal getPayableAmountOriginal() {
        return payableAmountOriginal;
    }

    public MCPaymentDetailVendor payableAmountOriginal(BigDecimal payableAmountOriginal) {
        this.payableAmountOriginal = payableAmountOriginal;
        return this;
    }

    public void setPayableAmountOriginal(BigDecimal payableAmountOriginal) {
        this.payableAmountOriginal = payableAmountOriginal;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public MCPaymentDetailVendor remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getRemainingAmountOriginal() {
        return remainingAmountOriginal;
    }

    public MCPaymentDetailVendor remainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
        return this;
    }

    public void setRemainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MCPaymentDetailVendor amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MCPaymentDetailVendor amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public MCPaymentDetailVendor discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public MCPaymentDetailVendor discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public MCPaymentDetailVendor discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public MCPaymentDetailVendor discountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
        return this;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public MCPaymentDetailVendor accountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public MCPaymentDetailVendor employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getRefVoucherExchangeRate() {
        return refVoucherExchangeRate;
    }

    public MCPaymentDetailVendor refVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
        return this;
    }

    public void setRefVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
    }

    public BigDecimal getLastExchangeRate() {
        return lastExchangeRate;
    }

    public MCPaymentDetailVendor lastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
        return this;
    }

    public void setLastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public MCPaymentDetailVendor differAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
        return this;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCPaymentDetailVendor orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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
        MCPaymentDetailVendor mCPaymentDetailVendor = (MCPaymentDetailVendor) o;
        if (mCPaymentDetailVendor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPaymentDetailVendor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPaymentDetailVendor{" +
            "id=" + getId() +
            ", mCPaymentID=" + getmCPaymentID() +
            ", pPInvoiceID=" + getpPInvoiceID() +
            ", voucherTypeID=" + getVoucherTypeID() +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", payableAmount=" + getPayableAmount() +
            ", payableAmountOriginal=" + getPayableAmountOriginal() +
            ", remainingAmount=" + getRemainingAmount() +
            ", remainingAmountOriginal=" + getRemainingAmountOriginal() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAccount='" + getDiscountAccount() + "'" +
            ", accountingObjectID=" + getAccountingObjectID() +
            ", employeeID=" + getEmployeeID() +
            ", refVoucherExchangeRate=" + getRefVoucherExchangeRate() +
            ", lastExchangeRate=" + getLastExchangeRate() +
            ", differAmount=" + getDifferAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
