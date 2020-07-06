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
 * A MBTellerPaperDetailTax.
 */
@Entity
@Table(name = "mbtellerpaperdetailtax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBTellerPaperDetailTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    @NotNull
    @Column(name = "pretaxamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmount;

    @NotNull
    @Column(name = "pretaxamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal pretaxAmountOriginal;

    @Column(name = "invoicetemplate", length = 25)
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
    private UUID goodsServicePurchaseId;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "mbtellerpaperid")
    private UUID mBTellerPaperId;

//    @ManyToOne(optional = false)    @NotNull
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "mbtellerpaperid")
//    private MBTellerPaper mbtellerPaper;

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

    public String getDescription() {
        return description;
    }

    public MBTellerPaperDetailTax description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        MBTellerPaperDetailTax mBTellerPaperDetailTax = (MBTellerPaperDetailTax) o;
        if (mBTellerPaperDetailTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBTellerPaperDetailTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBTellerPaperDetailTax{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }

    public BigDecimal getPretaxAmount() {
        return pretaxAmount;
    }

    public void setPretaxAmount(BigDecimal pretaxAmount) {
        this.pretaxAmount = pretaxAmount;
    }

    public BigDecimal getPretaxAmountOriginal() {
        return pretaxAmountOriginal;
    }

    public void setPretaxAmountOriginal(BigDecimal pretaxAmountOriginal) {
        this.pretaxAmountOriginal = pretaxAmountOriginal;
    }

    public UUID getmBTellerPaperId() {
        return mBTellerPaperId;
    }

    public void setmBTellerPaperId(UUID mBTellerPaperId) {
        this.mBTellerPaperId = mBTellerPaperId;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getGoodsServicePurchaseId() {
        return goodsServicePurchaseId;
    }

    public void setGoodsServicePurchaseId(UUID goodsServicePurchaseId) {
        this.goodsServicePurchaseId = goodsServicePurchaseId;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }
}
