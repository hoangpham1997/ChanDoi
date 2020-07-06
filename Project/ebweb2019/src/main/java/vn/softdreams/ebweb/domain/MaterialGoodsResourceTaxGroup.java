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
 * A MaterialGoodsResourceTaxGroup.
 */
@Entity
@Table(name = "materialgoodsresourcetaxgroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialGoodsResourceTaxGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "materialgoodsresourcetaxgroupcode", length = 25, nullable = false)
    private String materialGoodsResourceTaxGroupCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "material_goods_resource_tax_group_name", length = 512, nullable = false)
    private String materialGoodsResourceTaxGroupName;

    @Column(name = "unitid")
    private String unitID;

    @Column(name = "taxrate", precision = 10, scale = 2)
    private BigDecimal taxRate;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "parentid")
    private String parentID;

    @NotNull
    @Column(name = "isparentnode", nullable = false)
    private Boolean isParentNode;

    @NotNull
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    @ManyToOne    @JsonIgnoreProperties("")
    private Unit son;

    @ManyToOne    @JsonIgnoreProperties("")
    private MaterialGoodsResourceTaxGroup parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialGoodsResourceTaxGroupCode() {
        return materialGoodsResourceTaxGroupCode;
    }

    public MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroupCode(String materialGoodsResourceTaxGroupCode) {
        this.materialGoodsResourceTaxGroupCode = materialGoodsResourceTaxGroupCode;
        return this;
    }

    public void setMaterialGoodsResourceTaxGroupCode(String materialGoodsResourceTaxGroupCode) {
        this.materialGoodsResourceTaxGroupCode = materialGoodsResourceTaxGroupCode;
    }

    public String getMaterialGoodsResourceTaxGroupName() {
        return materialGoodsResourceTaxGroupName;
    }

    public MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroupName(String materialGoodsResourceTaxGroupName) {
        this.materialGoodsResourceTaxGroupName = materialGoodsResourceTaxGroupName;
        return this;
    }

    public void setMaterialGoodsResourceTaxGroupName(String materialGoodsResourceTaxGroupName) {
        this.materialGoodsResourceTaxGroupName = materialGoodsResourceTaxGroupName;
    }

    public String getUnitID() {
        return unitID;
    }

    public MaterialGoodsResourceTaxGroup unitID(String unitID) {
        this.unitID = unitID;
        return this;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public MaterialGoodsResourceTaxGroup taxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public MaterialGoodsResourceTaxGroup orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public String getParentID() {
        return parentID;
    }

    public MaterialGoodsResourceTaxGroup parentID(String parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public MaterialGoodsResourceTaxGroup isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public Integer getGrade() {
        return grade;
    }

    public MaterialGoodsResourceTaxGroup grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public MaterialGoodsResourceTaxGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public MaterialGoodsResourceTaxGroup isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }

    public Unit getSon() {
        return son;
    }

    public MaterialGoodsResourceTaxGroup son(Unit unit) {
        this.son = unit;
        return this;
    }

    public void setSon(Unit unit) {
        this.son = unit;
    }

    public MaterialGoodsResourceTaxGroup getParent() {
        return parent;
    }

    public MaterialGoodsResourceTaxGroup parent(MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup) {
        this.parent = materialGoodsResourceTaxGroup;
        return this;
    }

    public void setParent(MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup) {
        this.parent = materialGoodsResourceTaxGroup;
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
        MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup = (MaterialGoodsResourceTaxGroup) o;
        if (materialGoodsResourceTaxGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsResourceTaxGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsResourceTaxGroup{" +
            "id=" + getId() +
            ", materialGoodsResourceTaxGroupCode='" + getMaterialGoodsResourceTaxGroupCode() + "'" +
            ", materialGoodsResourceTaxGroupName='" + getMaterialGoodsResourceTaxGroupName() + "'" +
            ", unitID='" + getUnitID() + "'" +
            ", taxRate=" + getTaxRate() +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
