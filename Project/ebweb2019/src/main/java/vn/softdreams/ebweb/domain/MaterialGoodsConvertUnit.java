package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A MaterialGoodsConvertUnit.
 */
@Entity
@Table(name = "materialgoodsconvertunit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MaterialGoodsConvertUnitDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialGoodsConvertUnitDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "orderNumber", type = Integer.class),
                    @ColumnResult(name = "convertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "fixedSalePrice", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice1", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice2", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice3", type = BigDecimal.class)
                }
            )
        }
    )
})

public class MaterialGoodsConvertUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "ordernumber")
    private Integer orderNumber;

    @Column(name = "convertrate", precision = 10, scale = 2)
    private BigDecimal convertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "fixedSalePrice", precision = 18, scale = 4)
    private BigDecimal fixedSalePrice;

    @Column(name = "salePrice1", precision = 18, scale = 4)
    private BigDecimal salePrice1;

    @Column(name = "salePrice2", precision = 18, scale = 4)
    private BigDecimal salePrice2;

    @Column(name = "salePrice3", precision = 18, scale = 4)
    private BigDecimal salePrice3;

//    @ManyToOne
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "materialgoodsid")
//    private MaterialGoods materialGoods;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public MaterialGoodsConvertUnit orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public MaterialGoodsConvertUnit convertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
        return this;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public String getFormula() {
        return formula;
    }

    public MaterialGoodsConvertUnit formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDescription() {
        return description;
    }

    public MaterialGoodsConvertUnit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
//    public MaterialGoods getMaterialGoods() {
//        return materialGoods;
//    }
//
//    public MaterialGoodsConvertUnit materialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//        return this;
//    }
//
//    public void setMaterialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public BigDecimal getFixedSalePrice() {
        return fixedSalePrice;
    }

    public MaterialGoodsConvertUnit fixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
        return this;
    }

    public void setFixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
    }

    public BigDecimal getSalePrice1() {
        return salePrice1;
    }

    public MaterialGoodsConvertUnit salePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
        return this;
    }

    public void setSalePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
    }

    public BigDecimal getSalePrice2() {
        return salePrice2;
    }

    public MaterialGoodsConvertUnit salePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
        return this;
    }

    public void setSalePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
    }

    public BigDecimal getSalePrice3() {
        return salePrice3;
    }

    public MaterialGoodsConvertUnit SalePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
        return this;
    }

    public void setSalePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialGoodsConvertUnit materialGoodsConvertUnit = (MaterialGoodsConvertUnit) o;
        if (materialGoodsConvertUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsConvertUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsConvertUnit{" +
            "id=" + getId() +
            ", orderNumber=" + getOrderNumber() +
            ", convertRate=" + getConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", description='" + getDescription() + "'" +
            ", fixedSalePrice='" + getFixedSalePrice() + "'" +
            ", salePrice1='" + getSalePrice1() + "'" +
            ", salePrice2='" + getSalePrice2() + "'" +
            ", salePrice3='" + getSalePrice3() + "'" +
            "}";
    }
}
