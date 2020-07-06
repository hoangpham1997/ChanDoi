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
 * A MBDepositDetailTax.
 */
@Entity
@Table(name = "mbdepositdetailtax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBDepositDetailTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mbdepositid")
    private UUID mBDepositID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

//    @NotNull
    @Column(name = "vatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmount;

//    @NotNull
    @Column(name = "vatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmountOriginal;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @Size(max = 25)
    @Column(name = "vataccount", length = 25)
    private String vATAccount;

//    @NotNull
    @Column(name = "pretaxamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmount;

//    @NotNull
    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2, nullable = false)
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

    @Column(name = "orderpriority")
    private Integer orderPriority;



    @Column(name="accountingobjectid")
    private UUID accountingObjectID;

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

    public MBDepositDetailTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public MBDepositDetailTax vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public MBDepositDetailTax vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public MBDepositDetailTax vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public MBDepositDetailTax vATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
        return this;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public MBDepositDetailTax pretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
        return this;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public MBDepositDetailTax pretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
        return this;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public MBDepositDetailTax invoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
        return this;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public MBDepositDetailTax invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public MBDepositDetailTax invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public MBDepositDetailTax invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public MBDepositDetailTax goodsServicePurchaseID(String goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        return this;
    }

    public void setGoodsServicePurchaseID(String goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MBDepositDetailTax orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getmBDepositID() {
        return mBDepositID;
    }

    public void setmBDepositID(UUID mBDepositID) {
        this.mBDepositID = mBDepositID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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
        MBDepositDetailTax mBDepositDetailTax = (MBDepositDetailTax) o;
        if (mBDepositDetailTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBDepositDetailTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBDepositDetailTax{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", vATAmount=" + getvATAmount() +
            ", vATAmountOriginal=" + getvATAmountOriginal() +
            ", vATRate=" + getvATRate() +
            ", vATAccount='" + getvATAccount() + "'" +
            ", pretaxAmount=" + getPretaxAmount() +
            ", pretaxAmountOriginal=" + getPretaxAmountOriginal() +
            ", invoiceType=" + getInvoiceType() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", goodsServicePurchaseID='" + getGoodsServicePurchaseID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
