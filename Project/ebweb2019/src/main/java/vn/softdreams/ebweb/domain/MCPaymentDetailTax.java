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
 * A MCPaymentDetailTax.
 */
@Entity
@Table(name = "mcpaymentdetailtax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCPaymentDetailTax implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcpaymentid")
    private UUID mCPaymentID;

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

    @Column(name = "pretaxamount", precision = 10, scale = 2)
    private BigDecimal pretaxAmount;

    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2)
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

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "taxcode")
    private String taxCode;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getmCPaymentID() {
        return mCPaymentID;
    }

    public MCPaymentDetailTax mCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
        return this;
    }

    public void setmCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
    }

    public String getDescription() {
        return description;
    }

    public MCPaymentDetailTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public MCPaymentDetailTax vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public MCPaymentDetailTax vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public MCPaymentDetailTax vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public MCPaymentDetailTax vATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
        return this;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public MCPaymentDetailTax pretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
        return this;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public MCPaymentDetailTax pretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
        return this;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public MCPaymentDetailTax invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public MCPaymentDetailTax invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public MCPaymentDetailTax invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public MCPaymentDetailTax invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public MCPaymentDetailTax goodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        return this;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCPaymentDetailTax orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCPaymentDetailTax accountingObject(AccountingObject accountingObject) {
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
        MCPaymentDetailTax mCPaymentDetailTax = (MCPaymentDetailTax) o;
        if (mCPaymentDetailTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPaymentDetailTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPaymentDetailTax{" +
            "id=" + getId() +
            ", mCPaymentID=" + getmCPaymentID() +
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
            ", goodsServicePurchaseID=" + getGoodsServicePurchaseID() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
