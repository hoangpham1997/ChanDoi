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
 * A MaterialGoodsSpecialTaxGroup.
 */
@Entity
@Table(name = "materialgoodsspecialtaxgroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialGoodsSpecialTaxGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "materialgoodsspecialtaxgroupcode")
    private String materialGoodsSpecialTaxGroupCode;

    @Size(max = 512)
    @Column(name = "materialgoodsspecialtaxgroupname")
    private String materialGoodsSpecialTaxGroupName;

    @Column(name = "taxrate", precision = 10, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "unitid")
    private UUID unitID;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "parentid")
    private UUID parentID;

    @Column(name = "isparentnode")
    private Boolean isParentNode;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "issecurity")
    private Boolean isSecurity;

    @Column(name = "companyid")
    private UUID companyID;

//    @ManyToOne    @JsonIgnoreProperties("")
//    private MaterialGoodsSpecialTaxGroup parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialGoodsSpecialTaxGroupCode() {
        return materialGoodsSpecialTaxGroupCode;
    }

    public MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroupCode(String materialGoodsSpecialTaxGroupCode) {
        this.materialGoodsSpecialTaxGroupCode = materialGoodsSpecialTaxGroupCode;
        return this;
    }

    public void setMaterialGoodsSpecialTaxGroupCode(String materialGoodsSpecialTaxGroupCode) {
        this.materialGoodsSpecialTaxGroupCode = materialGoodsSpecialTaxGroupCode;
    }

    public String getMaterialGoodsSpecialTaxGroupName() {
        return materialGoodsSpecialTaxGroupName;
    }

    public MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroupName(String materialGoodsSpecialTaxGroupName) {
        this.materialGoodsSpecialTaxGroupName = materialGoodsSpecialTaxGroupName;
        return this;
    }

    public void setMaterialGoodsSpecialTaxGroupName(String materialGoodsSpecialTaxGroupName) {
        this.materialGoodsSpecialTaxGroupName = materialGoodsSpecialTaxGroupName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public MaterialGoodsSpecialTaxGroup taxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }


    public String getOrderFixCode() {
        return orderFixCode;
    }

    public MaterialGoodsSpecialTaxGroup orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public UUID getParentID() {
        return parentID;
    }

    public MaterialGoodsSpecialTaxGroup parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public MaterialGoodsSpecialTaxGroup isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public Integer getGrade() {
        return grade;
    }

    public MaterialGoodsSpecialTaxGroup grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public MaterialGoodsSpecialTaxGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public MaterialGoodsSpecialTaxGroup isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getGoodsSpecialTaxGroup() {
        return materialGoodsSpecialTaxGroupCode;
    }


    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }


//    public MaterialGoodsSpecialTaxGroup getParent() {
//        return parent;
//    }
//
//    public MaterialGoodsSpecialTaxGroup parent(MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
//        this.parent = materialGoodsSpecialTaxGroup;
//        return this;
//    }
//
//    public void setParent(MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup) {
//        this.parent = materialGoodsSpecialTaxGroup;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialGoodsSpecialTaxGroup materialGoodsSpecialTaxGroup = (MaterialGoodsSpecialTaxGroup) o;
        if (materialGoodsSpecialTaxGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsSpecialTaxGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsSpecialTaxGroup{" +
            "id=" + getId() +
            ", materialGoodsSpecialTaxGroupCode='" + getMaterialGoodsSpecialTaxGroupCode() + "'" +
            ", materialGoodsSpecialTaxGroupName='" + getMaterialGoodsSpecialTaxGroupName() + "'" +
            ", taxRate=" + getTaxRate() +
            ", unitID='" + getUnitID() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
