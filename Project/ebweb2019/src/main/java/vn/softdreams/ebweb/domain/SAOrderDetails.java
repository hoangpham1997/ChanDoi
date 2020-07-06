package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A SAOrderDetails.
 */
@Entity
@Table(name = "saorderdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SAOrderDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "saorderid")
    private UUID sAOrderID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "quantitydelivery", precision = 10, scale = 2)
    private BigDecimal quantityDelivery;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discountamount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discountamountoriginal", precision = 10, scale = 2)
    private BigDecimal discountAmountOriginal;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vATAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vATAmountOriginal;

    @Size(max = 512)
    @Column(name = "vatdescription", length = 512)
    private String vATDescription;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGoods;

    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "mainunitid")
    private Unit mainUnit;

    @Column(name = "saquoteid")
    private UUID sAQuoteID;

    @Column(name = "saquotedetailid")
    private UUID sAQuoteDetailID;

    @Transient
    private String amountOriginalToString;
    @Transient
    private String amountToString;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getsAOrderID() {
        return sAOrderID;
    }

    public SAOrderDetails sAOrderID(UUID sAOrderID) {
        this.sAOrderID = sAOrderID;
        return this;
    }

    public void setsAOrderID(UUID sAOrderID) {
        this.sAOrderID = sAOrderID;
    }

    public String getDescription() {
        return description;
    }

    public SAOrderDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SAOrderDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityDelivery() {
        return quantityDelivery;
    }

    public SAOrderDetails quantityDelivery(BigDecimal quantityDelivery) {
        this.quantityDelivery = quantityDelivery;
        return this;
    }

    public void setQuantityDelivery(BigDecimal quantityDelivery) {
        this.quantityDelivery = quantityDelivery;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SAOrderDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public SAOrderDetails unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public SAOrderDetails mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public SAOrderDetails mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public SAOrderDetails mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public SAOrderDetails formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SAOrderDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public SAOrderDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public SAOrderDetails discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public SAOrderDetails discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public SAOrderDetails discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public SAOrderDetails vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public SAOrderDetails vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public SAOrderDetails vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public String getvATDescription() {
        return vATDescription;
    }

    public SAOrderDetails vATDescription(String vATDescription) {
        this.vATDescription = vATDescription;
        return this;
    }

    public void setvATDescription(String vATDescription) {
        this.vATDescription = vATDescription;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public SAOrderDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
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

    public UUID getsAQuoteID() {
        return sAQuoteID;
    }

    public void setsAQuoteID(UUID sAQuoteID) {
        this.sAQuoteID = sAQuoteID;
    }

    public UUID getsAQuoteDetailID() {
        return sAQuoteDetailID;
    }

    public void setsAQuoteDetailID(UUID sAQuoteDetailID) {
        this.sAQuoteDetailID = sAQuoteDetailID;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
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
        SAOrderDetails sAOrderDetails = (SAOrderDetails) o;
        if (sAOrderDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAOrderDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

//    @Override
//    public String toString() {
//        return "SAOrderDetails{" +
//            "id=" + getId() +
//            ", sAOrderID=" + getsAOrderID() +
//            ", materialGoodsID=" + getMaterialGoods().getId() +
//            ", description='" + getDescription() + "'" +
//            ", unitID=" + getUnit().getId() +
//            ", quantity=" + getQuantity() +
//            ", quantityDelivery=" + getQuantityDelivery() +
//            ", unitPrice=" + getUnitPrice() +
//            ", unitPriceOriginal=" + getUnitPriceOriginal() +
//            ", mainUnitID=" + getMainUnit().getId() +
//            ", mainQuantity=" + getMainQuantity() +
//            ", mainUnitPrice=" + getMainUnitPrice() +
//            ", mainConvertRate=" + getMainConvertRate() +
//            ", formula='" + getFormula() + "'" +
//            ", amount=" + getAmount() +
//            ", amountOriginal=" + getAmountOriginal() +
//            ", discountRate=" + getDiscountRate() +
//            ", discountAmount=" + getDiscountAmount() +
//            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
//            ", vATRate=" + getvATRate() +
//            ", vATAmount=" + getvATAmount() +
//            ", vATAmountOriginal=" + getvATAmountOriginal() +
//            ", vATDescription='" + getvATDescription() + "'" +
//            ", orderPriority=" + getOrderPriority() +
//            "}";
//    }
}
