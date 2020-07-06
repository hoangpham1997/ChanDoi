package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A MaterialGoodsCategory.
 */
@Entity
@Table(name = "materialgoodscategory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialGoodsCategory implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Size(max = 25)
    @Column(name = "materialgoodscategorycode", length = 25)
    private String materialGoodsCategoryCode;

    @Size(max = 512)
    @Column(name = "materialgoodscategoryname", length = 512)
    private String materialGoodsCategoryName;

    @Column(name = "parentid")
    private UUID parentID;

    @Column(name = "isparentnode")
    private Boolean isParentNode;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "issecurity")
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialGoodsCategory() {
        return materialGoodsCategoryCode;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public MaterialGoodsCategory companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public MaterialGoodsCategory branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public String getMaterialGoodsCategoryCode() {
        return materialGoodsCategoryCode;
    }

    public MaterialGoodsCategory materialGoodsCategoryCode(String materialGoodsCategoryCode) {
        this.materialGoodsCategoryCode = materialGoodsCategoryCode;
        return this;
    }

    public void setMaterialGoodsCategoryCode(String materialGoodsCategoryCode) {
        this.materialGoodsCategoryCode = materialGoodsCategoryCode;
    }

    public String getMaterialGoodsCategoryName() {
        return materialGoodsCategoryName;
    }

    public MaterialGoodsCategory materialGoodsCategoryName(String materialGoodsCategoryName) {
        this.materialGoodsCategoryName = materialGoodsCategoryName;
        return this;
    }

    public void setMaterialGoodsCategoryName(String materialGoodsCategoryName) {
        this.materialGoodsCategoryName = materialGoodsCategoryName;
    }

    public UUID getParentID() {
        return parentID;
    }

    public MaterialGoodsCategory parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public MaterialGoodsCategory isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public MaterialGoodsCategory orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public MaterialGoodsCategory grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public MaterialGoodsCategory isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
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
        MaterialGoodsCategory materialGoodsCategory = (MaterialGoodsCategory) o;
        if (materialGoodsCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsCategory{" +
            "id=" + getId() +
            ", companyID=" + getCompanyID() +
            ", branchID=" + getBranchID() +
            ", materialGoodsCategoryCode='" + getMaterialGoodsCategoryCode() + "'" +
            ", materialGoodsCategoryName='" + getMaterialGoodsCategoryName() + "'" +
            ", parentID=" + getParentID() +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
