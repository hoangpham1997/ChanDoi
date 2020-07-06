package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A SAQuoteDetails.
 */
@Entity
@Table(name = "saquotedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SAQuoteDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "saquoteid")
    private UUID sAQuoteID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @NotNull
    @Column(name = "unitprice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "unitpriceoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @NotNull
    @Column(name = "mainunitprice", precision = 10, scale = 2, nullable = false)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Size(max = 512)
    @Column(name = "vatdescription", length = 512)
    private String vATDescription;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "amountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal amountOriginal;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @NotNull
    @Column(name = "discountamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "discountamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmountOriginal;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @NotNull
    @Column(name = "vatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmount;

    @NotNull
    @Column(name = "vatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal vATAmountOriginal;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne
    @JsonIgnoreProperties
    @JoinColumn(name = "unitid")
    private Unit unit;

    @ManyToOne
    @JsonIgnoreProperties
    @JoinColumn(name = "mainunitid")
    private Unit mainUnit;

    @ManyToOne(optional = false)    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGoods;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<UnitConvertRateDTO> units;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getsAQuoteID() {
        return sAQuoteID;
    }

    public SAQuoteDetails sAQuoteID(UUID sAQuoteID) {
        this.sAQuoteID = sAQuoteID;
        return this;
    }

    public void setsAQuoteID(UUID sAQuoteID) {
        this.sAQuoteID = sAQuoteID;
    }

    public String getDescription() {
        return description;
    }

    public SAQuoteDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SAQuoteDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SAQuoteDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public SAQuoteDetails unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public SAQuoteDetails mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public SAQuoteDetails mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public SAQuoteDetails mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public SAQuoteDetails formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SAQuoteDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public SAQuoteDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public SAQuoteDetails discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public SAQuoteDetails discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public SAQuoteDetails discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public SAQuoteDetails vATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
        return this;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public SAQuoteDetails vATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
        return this;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public SAQuoteDetails vATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
        return this;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public SAQuoteDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public SAQuoteDetails materialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
        return this;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
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
        SAQuoteDetails sAQuoteDetails = (SAQuoteDetails) o;
        if (sAQuoteDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAQuoteDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SAQuoteDetails{" +
            "id=" + getId() +
            ", sAQuoteID=" + getsAQuoteID() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", vATRate=" + getvATRate() +
            ", vATAmount=" + getvATAmount() +
            ", vATAmountOriginal=" + getvATAmountOriginal() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }

    public Unit getUnit() {
        return unit;
    }
    public SAQuoteDetails unit(Unit unit) {
        this.unit = unit;
        return this;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getMainUnit() {
        return mainUnit;
    }
    public SAQuoteDetails mainUnit(Unit mainUnit) {
        this.mainUnit = mainUnit;
        return this;
    }
    public void setMainUnit(Unit mainUnit) {
        this.mainUnit = mainUnit;
    }

    public String getvATDescription() {
        return vATDescription;
    }

    public void setvATDescription(String vATDescription) {
        this.vATDescription = vATDescription;
    }

    public List<UnitConvertRateDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitConvertRateDTO> units) {
        this.units = units;
    }
}
