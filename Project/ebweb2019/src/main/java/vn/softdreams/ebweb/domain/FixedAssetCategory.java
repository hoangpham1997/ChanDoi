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
 * A FixedAssetCategory.
 */
@Entity
@Table(name = "fixedassetcategory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FixedAssetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "fixedassetcategorycode", length = 25, nullable = false)
    private String fixedAssetCategoryCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "fixedassetcategoryname", length = 512, nullable = false)
    private String fixedAssetCategoryName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "isparentnode", nullable = false)
    private Boolean isParentNode;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "usedtime", precision = 10, scale = 2)
    private BigDecimal usedTime;

    @Column(name = "depreciationrate", precision = 10, scale = 2)
    private BigDecimal depreciationRate;

    @Size(max = 25)
    @Column(name = "originalpriceaccount", length = 25)
    private String originalPriceAccount;

    @Size(max = 25)
    @Column(name = "depreciationaccount", length = 25)
    private String depreciationAccount;

    @Size(max = 25)
    @Column(name = "expenditureaccount", length = 25)
    private String expenditureAccount;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @Column(name = "parentid", nullable = false)
    private String parentID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFixedAssetCategoryCode() {
        return fixedAssetCategoryCode;
    }

    public FixedAssetCategory fixedAssetCategoryCode(String fixedAssetCategoryCode) {
        this.fixedAssetCategoryCode = fixedAssetCategoryCode;
        return this;
    }

    public void setFixedAssetCategoryCode(String fixedAssetCategoryCode) {
        this.fixedAssetCategoryCode = fixedAssetCategoryCode;
    }

    public String getFixedAssetCategoryName() {
        return fixedAssetCategoryName;
    }

    public FixedAssetCategory fixedAssetCategoryName(String fixedAssetCategoryName) {
        this.fixedAssetCategoryName = fixedAssetCategoryName;
        return this;
    }

    public void setFixedAssetCategoryName(String fixedAssetCategoryName) {
        this.fixedAssetCategoryName = fixedAssetCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public FixedAssetCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public FixedAssetCategory isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public FixedAssetCategory orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public FixedAssetCategory grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public BigDecimal getUsedTime() {
        return usedTime;
    }

    public FixedAssetCategory usedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
        return this;
    }

    public void setUsedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public FixedAssetCategory depreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
        return this;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getOriginalPriceAccount() {
        return originalPriceAccount;
    }

    public FixedAssetCategory originalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
        return this;
    }

    public void setOriginalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
    }

    public String getDepreciationAccount() {
        return depreciationAccount;
    }

    public FixedAssetCategory depreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
        return this;
    }

    public void setDepreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public String getExpenditureAccount() {
        return expenditureAccount;
    }

    public FixedAssetCategory expenditureAccount(String expenditureAccount) {
        this.expenditureAccount = expenditureAccount;
        return this;
    }

    public void setExpenditureAccount(String expenditureAccount) {
        this.expenditureAccount = expenditureAccount;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public FixedAssetCategory isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
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
        FixedAssetCategory fixedAssetCategory = (FixedAssetCategory) o;
        if (fixedAssetCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fixedAssetCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixedAssetCategory{" +
            "id=" + getId() +
            ", fixedAssetCategoryCode='" + getFixedAssetCategoryCode() + "'" +
            ", fixedAssetCategoryName='" + getFixedAssetCategoryName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", usedTime=" + getUsedTime() +
            ", depreciationRate=" + getDepreciationRate() +
            ", originalPriceAccount='" + getOriginalPriceAccount() + "'" +
            ", depreciationAccount='" + getDepreciationAccount() + "'" +
            ", expenditureAccount='" + getExpenditureAccount() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
