package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A GOtherVoucherDetailTax.
 */
@Entity
@Table(name = "gothervoucherdetailtax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GOtherVoucherDetailTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "gothervoucherid")
    private UUID gOtherVoucherID;

    @Column(name = "description")
    private String description;

    @Column(name = "vataccount")
    private String vATAccount;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vATAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vATAmountOriginal;

    @Column(name = "pretaxamount", precision = 10, scale = 2)
    private BigDecimal pretaxAmount;

    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal pretaxAmountOriginal;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

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

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public GOtherVoucherDetailTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public GOtherVoucherDetailTax vATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
        return this;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public GOtherVoucherDetailTax vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public GOtherVoucherDetailTax vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public GOtherVoucherDetailTax vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public GOtherVoucherDetailTax pretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
        return this;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public GOtherVoucherDetailTax pretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
        return this;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public GOtherVoucherDetailTax invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public GOtherVoucherDetailTax invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public GOtherVoucherDetailTax invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public GOtherVoucherDetailTax invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public GOtherVoucherDetailTax orderPriority(Integer orderPriority) {
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
        GOtherVoucherDetailTax gOtherVoucherDetailTax = (GOtherVoucherDetailTax) o;
        if (gOtherVoucherDetailTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gOtherVoucherDetailTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GOtherVoucherDetailTax{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", vATAccount='" + getvATAccount() + "'" +
            ", vATRate=" + getvATRate() +
            ", vATAmount=" + getvATAmount() +
            ", vATAmountOriginal=" + getvATAmountOriginal() +
            ", pretaxAmount=" + getPretaxAmount() +
            ", pretaxAmountOriginal=" + getPretaxAmountOriginal() +
            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", goodsServicePurchaseID='" + getGoodsServicePurchaseID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
