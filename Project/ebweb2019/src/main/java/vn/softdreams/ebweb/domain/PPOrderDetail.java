package vn.softdreams.ebweb.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PPOrderDetail.
 */
@Entity
@Table(name = "pporderdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PPOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "pporderid", insertable = false, updatable = false)
    private UUID pPOrderID;

    @ManyToOne
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGood;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "quantityreceipt", precision = 10, scale = 2)
    private BigDecimal quantityReceipt;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

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
    private BigDecimal vatRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vatAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vatAmountOriginal;

    @ManyToOne
    @JoinColumn(name = "mainunitid")
    private Unit mainUnit;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Column(name = "formula")
    private String formula;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PPOrderDetail description(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public PPOrderDetail quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getQuantityReceipt() {
        return quantityReceipt;
    }

    public void setQuantityReceipt(BigDecimal quantityReceipt) {
        this.quantityReceipt = quantityReceipt;
    }

    public PPOrderDetail quantityReceipt(BigDecimal quantityReceipt) {
        this.quantityReceipt = quantityReceipt;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public PPOrderDetail unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public PPOrderDetail unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public PPOrderDetail discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public PPOrderDetail discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public PPOrderDetail discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public PPOrderDetail vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public PPOrderDetail vatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public PPOrderDetail vatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
        return this;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public PPOrderDetail mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public PPOrderDetail mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public PPOrderDetail mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public PPOrderDetail formula(String formula) {
        this.formula = formula;
        return this;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public PPOrderDetail orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MaterialGoods getMaterialGood() {
        return materialGood;
    }

    public void setMaterialGood(MaterialGoods materialGoods) {
        this.materialGood = materialGoods;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public UUID getpPOrderID() {
        return pPOrderID;
    }

    public void setpPOrderID(UUID pPOrderID) {
        this.pPOrderID = pPOrderID;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
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
        PPOrderDetail pporderdetail = (PPOrderDetail) o;
        if (pporderdetail.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pporderdetail.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PPOrderDetail{" +
            "id=" + getId() +
//            ", ppOrderId='" + getPpOrderID() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", quantityReceipt=" + getQuantityReceipt() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
