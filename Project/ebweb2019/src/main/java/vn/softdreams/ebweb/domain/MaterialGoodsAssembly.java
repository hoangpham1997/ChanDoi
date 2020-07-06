package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A MaterialGoodsAssembly.
 */
@Entity
@Table(name = "materialgoodsassembly")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialGoodsAssembly implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 512)
    @Column(name = "materialassemblydescription", length = 512)
    private String materialAssemblyDescription;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "materialassemblyid")
    private UUID materialAssemblyID;

//    @ManyToOne
//    @JoinColumn(name = "materialgoodsid")
//    private MaterialGoods materialGoods;

    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialAssemblyDescription() {
        return materialAssemblyDescription;
    }

    public MaterialGoodsAssembly materialAssemblyDescription(String materialAssemblyDescription) {
        this.materialAssemblyDescription = materialAssemblyDescription;
        return this;
    }

    public void setMaterialAssemblyDescription(String materialAssemblyDescription) {
        this.materialAssemblyDescription = materialAssemblyDescription;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public MaterialGoodsAssembly quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public MaterialGoodsAssembly unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MaterialGoodsAssembly totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getMaterialAssemblyID() {
        return materialAssemblyID;
    }

    public void setMaterialAssemblyID(UUID materialAssemblyID) {
        this.materialAssemblyID = materialAssemblyID;
    }
    //    public MaterialGoods getMaterialGoods() {
//        return materialGoods;
//    }
//
//    public MaterialGoodsAssembly materialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//        return this;
//    }
//
//    public void setMaterialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//    }


    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public Unit getUnit() {
        return unit;
    }

    public MaterialGoodsAssembly unit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
        MaterialGoodsAssembly materialGoodsAssembly = (MaterialGoodsAssembly) o;
        if (materialGoodsAssembly.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsAssembly.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsAssembly{" +
            "id=" + getId() +
            ", materialAssemblyDescription='" + getMaterialAssemblyDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
