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
 * A MCReceiptDetailCustomer.
 */
@Entity
@Table(name = "mcreceiptdetailcustomer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCReceiptDetailCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @NotNull
    @Column(name = "saleinvoiceid", nullable = false)
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
    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @NotNull
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

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "refvoucherexchangerate", precision = 10, scale = 2)
    private BigDecimal refVoucherExchangeRate;

    @Column(name = "lastexchangerate", precision = 10, scale = 2)
    private BigDecimal lastExchangeRate;

    @Column(name = "differamount", precision = 10, scale = 2)
    private BigDecimal differAmount;

    @Column(name = "mcreceiptid")
    private UUID mCReceiptID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
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
    private LocalDate duDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDuDate() {
        return duDate;
    }

    public void setDuDate(LocalDate duDate) {
        this.duDate = duDate;
    }

    public String getPaymentClauseCode() {
        return paymentClauseCode;
    }

    public void setPaymentClauseCode(String paymentClauseCode) {
        this.paymentClauseCode = paymentClauseCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MCReceiptDetailCustomer creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public UUID getSaleInvoiceID() {
        return saleInvoiceID;
    }

    public MCReceiptDetailCustomer saleInvoiceID(UUID saleInvoiceID) {
        this.saleInvoiceID = saleInvoiceID;
        return this;
    }

    public void setSaleInvoiceID(UUID saleInvoiceID) {
        this.saleInvoiceID = saleInvoiceID;
    }

    public Integer getVoucherTypeID() {
        return voucherTypeID;
    }

    public MCReceiptDetailCustomer voucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
        return this;
    }

    public void setVoucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
    }

    public BigDecimal getReceipableAmount() {
        return receipableAmount;
    }

    public MCReceiptDetailCustomer receipableAmount(BigDecimal receipableAmount) {
        this.receipableAmount = receipableAmount;
        return this;
    }

    public void setReceipableAmount(BigDecimal receipableAmount) {
        this.receipableAmount = receipableAmount;
    }

    public BigDecimal getReceipableAmountOriginal() {
        return receipableAmountOriginal;
    }

    public MCReceiptDetailCustomer receipableAmountOriginal(BigDecimal receipableAmountOriginal) {
        this.receipableAmountOriginal = receipableAmountOriginal;
        return this;
    }

    public void setReceipableAmountOriginal(BigDecimal receipableAmountOriginal) {
        this.receipableAmountOriginal = receipableAmountOriginal;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public MCReceiptDetailCustomer remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getRemainingAmountOriginal() {
        return remainingAmountOriginal;
    }

    public MCReceiptDetailCustomer remainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
        return this;
    }

    public void setRemainingAmountOriginal(BigDecimal remainingAmountOriginal) {
        this.remainingAmountOriginal = remainingAmountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MCReceiptDetailCustomer amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MCReceiptDetailCustomer amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public MCReceiptDetailCustomer discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public MCReceiptDetailCustomer discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public MCReceiptDetailCustomer discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public MCReceiptDetailCustomer discountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
        return this;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public MCReceiptDetailCustomer employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCReceiptDetailCustomer orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public BigDecimal getRefVoucherExchangeRate() {
        return refVoucherExchangeRate;
    }

    public MCReceiptDetailCustomer refVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
        return this;
    }

    public void setRefVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
    }

    public BigDecimal getLastExchangeRate() {
        return lastExchangeRate;
    }

    public MCReceiptDetailCustomer lastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
        return this;
    }

    public void setLastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public MCReceiptDetailCustomer differAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
        return this;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public UUID getmCReceiptID() {
        return mCReceiptID;
    }

    public void setmCReceiptID(UUID mCReceiptID) {
        this.mCReceiptID = mCReceiptID;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCReceiptDetailCustomer accountingObject(AccountingObject accountingObject) {
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
        MCReceiptDetailCustomer mCReceiptDetailCustomer = (MCReceiptDetailCustomer) o;
        if (mCReceiptDetailCustomer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCReceiptDetailCustomer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCReceiptDetailCustomer{" +
            "id=" + getId() +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", saleInvoiceID=" + getSaleInvoiceID() +
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
            ", employeeID=" + getEmployeeID() +
            ", orderPriority=" + getOrderPriority() +
            ", refVoucherExchangeRate=" + getRefVoucherExchangeRate() +
            ", lastExchangeRate=" + getLastExchangeRate() +
            ", differAmount=" + getDifferAmount() +
            "}";
    }
}
