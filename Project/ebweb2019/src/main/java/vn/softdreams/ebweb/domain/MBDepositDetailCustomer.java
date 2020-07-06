package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A MBDepositDetailCustomer.
 */
@Entity
@Table(name = "mbdepositdetailcustomer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBDepositDetailCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "saleinvoiceid")
    private UUID saleInvoiceID;

    @Column(name = "vouchertypeid")
    private Integer voucherTypeID;

    @NotNull
    @Column(name = "receipableamount", precision = 10, scale = 2)
    private BigDecimal receipableAmount;

    @NotNull
    @Column(name = "receipableamountoriginal", precision = 10, scale = 2)
    private BigDecimal receipableAmountOriginal;

    @NotNull
    @Column(name = "remainingamount", precision = 10, scale = 2)
    private BigDecimal remainingAmount;

    @NotNull
    @Column(name = "remainingamountoriginal", precision = 10, scale = 2)
    private BigDecimal remainingAmountOriginal;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "amountoriginal", precision = 10)
    private BigDecimal amountOriginal;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @NotNull
    @Column(name = "discountamount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "discountamountoriginal", precision = 10, scale = 2)
    private BigDecimal discountAmountOriginal;

    @Size(max = 25)
    @Column(name = "discountaccount", length = 25)
    private String discountAccount;

    @Column(name = "refvoucherexchangerate", precision = 10, scale = 2)
    private BigDecimal refVoucherExchangeRate;

    @Column(name = "lastexchangerate", precision = 10, scale = 2)
    private BigDecimal lastExchangeRate;

    @Column(name = "differamount", precision = 10, scale = 2)
    private BigDecimal differAmount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "mbdepositid")
    private UUID mBDepositID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectID")
    private AccountingObject accountingObject;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noFBook;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noMBook;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String paymentClauseCode;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate date;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate dueDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MBDepositDetailCustomer creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public UUID getSaleInvoiceID() {
        return saleInvoiceID;
    }

    public void setSaleInvoiceID(UUID saleInvoiceID) {
        this.saleInvoiceID = saleInvoiceID;
    }

    public Integer getVoucherTypeID() {
        return voucherTypeID;
    }

    public MBDepositDetailCustomer voucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
        return this;
    }

    public void setVoucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
    }

    public BigDecimal getReceipableAmount() {
        return receipableAmount;
    }

    public MBDepositDetailCustomer receipableAmount(BigDecimal receipableAmount) {
        this.receipableAmount = receipableAmount;
        return this;
    }

    public void setReceipableAmount(BigDecimal receipableAmount) {
        this.receipableAmount = receipableAmount;
    }

    public BigDecimal getReceipableAmountOriginal() {
        return receipableAmountOriginal;
    }

    public MBDepositDetailCustomer receipableAmountOriginal(BigDecimal receipableAmountOriginal) {
        this.receipableAmountOriginal = receipableAmountOriginal;
        return this;
    }

    public void setReceipableAmountOriginal(BigDecimal receipableAmountOriginal) {
        this.receipableAmountOriginal = receipableAmountOriginal;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public MBDepositDetailCustomer remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getRemainingAmountOriginal() {
        return remainingAmountOriginal;
    }

    public MBDepositDetailCustomer remainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
        return this;
    }

    public void setRemainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MBDepositDetailCustomer amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MBDepositDetailCustomer amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public MBDepositDetailCustomer discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public MBDepositDetailCustomer discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public MBDepositDetailCustomer discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public MBDepositDetailCustomer discountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
        return this;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public BigDecimal getRefVoucherExchangeRate() {
        return refVoucherExchangeRate;
    }

    public MBDepositDetailCustomer refVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
        return this;
    }

    public void setRefVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
    }

    public BigDecimal getLastExchangeRate() {
        return lastExchangeRate;
    }

    public MBDepositDetailCustomer lastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
        return this;
    }

    public void setLastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public MBDepositDetailCustomer differAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
        return this;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MBDepositDetailCustomer orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
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

    public String getPaymentClauseCode() {
        return paymentClauseCode;
    }

    public void setPaymentClauseCode(String paymentClauseCode) {
        this.paymentClauseCode = paymentClauseCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getmBDepositID() {
        return mBDepositID;
    }

    public void setmBDepositID(UUID mBDepositID) {
        this.mBDepositID = mBDepositID;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MBDepositDetailCustomer accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
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
        MBDepositDetailCustomer mBDepositDetailCustomer = (MBDepositDetailCustomer) o;
        if (mBDepositDetailCustomer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBDepositDetailCustomer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBDepositDetailCustomer{" +
            "id=" + getId() +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", saleInvoiceID='" + getSaleInvoiceID() + "'" +
            ", voucherTypeID=" + getVoucherTypeID() +
            ", receipableAmount=" + getReceipableAmount() +
            ", receipableAmountOriginal=" + getReceipableAmountOriginal() +
            ", remainingAmount=" + getRemainingAmount() +
            ", remainingAmountOriginal=" + getRemainingAmountOriginal() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", discountAccount='" + getDiscountAccount() + "'" +
            ", refVoucherExchangeRate=" + getRefVoucherExchangeRate() +
            ", lastExchangeRate=" + getLastExchangeRate() +
            ", differAmount=" + getDifferAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
