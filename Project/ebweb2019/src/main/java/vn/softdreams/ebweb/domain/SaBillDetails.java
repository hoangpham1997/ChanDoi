package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDetailDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A SaBillDetails.
 */
@Entity
@Table(name = "sabilldetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SaBillCreatedDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaBillCreatedDetailDTO.class,
                columns = {
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "saBillID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class)
                }
            )
        }
    )
})
public class SaBillDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sabillid")
    private UUID saBillId;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal = BigDecimal.ZERO;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity = BigDecimal.ZERO;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice = BigDecimal.ZERO;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate = BigDecimal.ZERO;

    @Column(name = "formula")
    private String formula;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal = BigDecimal.ZERO;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate = BigDecimal.ZERO;

    @Column(name = "discountamount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "discountamountoriginal", precision = 10, scale = 2)
    private BigDecimal discountAmountOriginal = BigDecimal.ZERO;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vatRate = BigDecimal.ZERO;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vatAmountOriginal = BigDecimal.ZERO;

    @Column(name = "lotno")
    private String lotNo;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "ispromotion")
    private Boolean isPromotion;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @ManyToOne
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGoods;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "mainunitid")
    private UUID mainUnitID;

    @Column(name = "careergroupid")
    private UUID careerGroupID;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Unit unit;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Unit mainUnit;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private UUID saInvoiceDetailID;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private UUID saReturnDetailID;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private UUID ppDiscountReturnDetailID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaBillId() {
        return saBillId;
    }

    public void setSaBillId(UUID saBillId) {
        this.saBillId = saBillId;
    }

    public String getDescription() {
        return description;
    }

    public SaBillDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public SaBillDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public SaBillDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SaBillDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SaBillDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public SaBillDetails unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public SaBillDetails mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public SaBillDetails mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public SaBillDetails mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public SaBillDetails Formula(String Formula) {
        this.formula = Formula;
        return this;
    }

    public void setFormula(String Formula) {
        this.formula = Formula;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SaBillDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public SaBillDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public SaBillDetails discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public SaBillDetails discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public SaBillDetails discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public SaBillDetails vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public SaBillDetails vatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public SaBillDetails vatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
        return this;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public String getLotNo() {
        return lotNo;
    }

    public SaBillDetails lotNo(String lotNo) {
        this.lotNo = lotNo;
        return this;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public SaBillDetails expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean isIsPromotion() {
        return isPromotion;
    }

    public SaBillDetails isPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
        return this;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public SaBillDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public UUID getSaInvoiceDetailID() {
        return saInvoiceDetailID;
    }

    public void setSaInvoiceDetailID(UUID saInvoiceDetailID) {
        this.saInvoiceDetailID = saInvoiceDetailID;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(Unit mainUnit) {
        this.mainUnit = mainUnit;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
    }

    public UUID getSaReturnDetailID() {
        return saReturnDetailID;
    }

    public void setSaReturnDetailID(UUID saReturnDetailID) {
        this.saReturnDetailID = saReturnDetailID;
    }

    public UUID getPpDiscountReturnDetailID() {
        return ppDiscountReturnDetailID;
    }

    public void setPpDiscountReturnDetailID(UUID ppDiscountReturnDetailID) {
        this.ppDiscountReturnDetailID = ppDiscountReturnDetailID;
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
        SaBillDetails saBillDetails = (SaBillDetails) o;
        if (saBillDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saBillDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaBillDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", Formula='" + getFormula() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", lotNo='" + getLotNo() + "'" +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", isPromotion='" + isIsPromotion() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
