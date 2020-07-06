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
 * A MBInternalTransferTax.
 */
@Entity
@Table(name = "mbinternaltransfertax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBInternalTransferTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "vatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal vatAmount;

    @NotNull
    @Column(name = "vatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal vatAmountOriginal;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vataccount")
    private String vatAccount;

    @NotNull
    @Column(name = "pretaxamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmount;

    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal pretaxAmountOriginal;

    @Column(name = "invoicetype")
    private Integer invoiceType;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    @Size(max = 25)
    @Column(name = "invoiceseries", length = 25)
    private String invoiceSeries;

    @Column(name = "goodsservicepurchaseid")
    private String goodsServicePurchaseID;

    @Column(name = "accountingobjectid")
    private String accountingObjectID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @ManyToOne(optional = false)    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name = "mbinternaltransferID")
    private MBInternalTransfer mBInternalTransfer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public MBInternalTransferTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public MBInternalTransferTax vatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public MBInternalTransferTax vatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
        return this;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public MBInternalTransferTax vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getVatAccount() {
        return vatAccount;
    }

    public MBInternalTransferTax vatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
        return this;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public MBInternalTransferTax pretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
        return this;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public MBInternalTransferTax pretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
        return this;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public MBInternalTransferTax invoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
        return this;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public MBInternalTransferTax invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public MBInternalTransferTax invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public MBInternalTransferTax invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public MBInternalTransferTax goodsServicePurchaseID(String goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        return this;
    }

    public void setGoodsServicePurchaseID(String goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public String getAccountingObjectID() {
        return accountingObjectID;
    }

    public MBInternalTransferTax accountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MBInternalTransferTax orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public MBInternalTransfer getMBInternalTransfer() {
        return mBInternalTransfer;
    }

    public MBInternalTransferTax mBInternalTransfer(MBInternalTransfer mBInternalTransfer) {
        this.mBInternalTransfer = mBInternalTransfer;
        return this;
    }

    public void setMBInternalTransfer(MBInternalTransfer mBInternalTransfer) {
        this.mBInternalTransfer = mBInternalTransfer;
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
        MBInternalTransferTax mBInternalTransferTax = (MBInternalTransferTax) o;
        if (mBInternalTransferTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBInternalTransferTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBInternalTransferTax{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", vatAmount=" + getVatAmount() +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", vatRate=" + getVatRate() +
            ", vatAccount='" + getVatAccount() + "'" +
            ", pretaxAmount=" + getPretaxAmount() +
            ", pretaxAmountOriginal=" + getPretaxAmountOriginal() +
            ", invoiceType=" + getInvoiceType() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", goodsServicePurchaseID='" + getGoodsServicePurchaseID() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
