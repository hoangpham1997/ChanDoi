package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A MCReceiptDetailTax.
 */
@Entity
@Table(name = "mcreceiptdetailtax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCReceiptDetailTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcreceiptid")
    private UUID mCReceiptID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "vatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmount;

    @NotNull
    @Column(name = "vatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmountOriginal;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @Size(max = 25)
    @Column(name = "vataccount", length = 25)
    private String vATAccount;

    @Column(name = "pretaxamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmount;

    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmountOriginal;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    @Size(max = 25)
    @Column(name = "invoiceseries", length = 25)
    private String invoiceSeries;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getmCReceiptID() {
        return mCReceiptID;
    }

    public void setmCReceiptID(UUID mCReceiptID) {
        this.mCReceiptID = mCReceiptID;
    }

    public String getDescription() {
        return description;
    }

    public MCReceiptDetailTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public MCReceiptDetailTax vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public MCReceiptDetailTax vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public MCReceiptDetailTax vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public MCReceiptDetailTax vATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
        return this;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public MCReceiptDetailTax pretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
        return this;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public MCReceiptDetailTax pretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
        return this;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public MCReceiptDetailTax invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public MCReceiptDetailTax invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public MCReceiptDetailTax invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public MCReceiptDetailTax invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCReceiptDetailTax orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

//    public MCReceipt getMCReceipt() {
//        return mCReceipt;
//    }
//
//    public MCReceiptDetailTax mCReceipt(MCReceipt mCReceipt) {
//        this.mCReceipt = mCReceipt;
//        return this;
//    }
//
//    public void setMCReceipt(MCReceipt mCReceipt) {
//        this.mCReceipt = mCReceipt;
//    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCReceiptDetailTax accountingObject(AccountingObject accountingObject) {
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
        MCReceiptDetailTax mCReceiptDetailTax = (MCReceiptDetailTax) o;
        if (mCReceiptDetailTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCReceiptDetailTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCReceiptDetailTax{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", vATAmount=" + getvATAmount() +
            ", vATAmountOriginal=" + getvATAmountOriginal() +
            ", vATRate=" + getvATRate() +
            ", vATAccount='" + getvATAccount() + "'" +
            ", pretaxAmount=" + getPretaxAmount() +
            ", pretaxAmountOriginal=" + getPretaxAmountOriginal() +
            ", invoiceTemplate=" + getInvoiceTemplate() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
