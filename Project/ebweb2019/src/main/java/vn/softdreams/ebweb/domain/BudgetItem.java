package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A BudgetItem.
 */
@Entity
@Table(name = "budgetitem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BudgetItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Size(max = 25)
    @NotNull
    @Column(name = "budgetitemcode", length = 25)
    private String budgetItemCode;

    @Size(max = 512)
    @NotNull
    @Column(name = "budgetitemname", length = 512)
    private String budgetItemName;

    @Column(name = "budgetitemtype")
    @NotNull
    private Integer budgetItemType;

    @Column(name = "description")
    private String description;

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

//    @ManyToOne    @JsonIgnoreProperties("")
//    private BudgetItem parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public BudgetItem budgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
        return this;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getBudgetItemName() {
        return budgetItemName;
    }

    public BudgetItem budgetItemName(String budgetItemName) {
        this.budgetItemName = budgetItemName;
        return this;
    }

    public void setBudgetItemName(String budgetItemName) {
        this.budgetItemName = budgetItemName;
    }

    public Integer getBudgetItemType() {
        return budgetItemType;
    }

    public BudgetItem budgetItemType(Integer budgetItemType) {
        this.budgetItemType = budgetItemType;
        return this;
    }

    public void setBudgetItemType(Integer budgetItemType) {
        this.budgetItemType = budgetItemType;
    }

    public String getDescription() {
        return description;
    }

    public BudgetItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentID() {
        return parentID;
    }

    public BudgetItem parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public BudgetItem isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public BudgetItem orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public BudgetItem grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public BudgetItem isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Boolean getParentNode() {
        return isParentNode;
    }

//    public void setActive(Boolean active) {
//        isActive = active;
//    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

//    public Boolean getActive() {
//        return isActive;
//    }
    //    public BudgetItem getParent() {
//        return parent;
//    }
//
//    public BudgetItem parent(BudgetItem budgetItem) {
//        this.parent = budgetItem;
//        return this;
//    }
//
//    public void setParent(BudgetItem budgetItem) {
//        this.parent = budgetItem;
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
        BudgetItem budgetItem = (BudgetItem) o;
        if (budgetItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), budgetItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
            "id=" + getId() +
            ", budgetItemCode='" + getBudgetItemCode() + "'" +
            ", budgetItemName='" + getBudgetItemName() + "'" +
            ", budgetItemType=" + getBudgetItemType() +
            ", description='" + getDescription() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
