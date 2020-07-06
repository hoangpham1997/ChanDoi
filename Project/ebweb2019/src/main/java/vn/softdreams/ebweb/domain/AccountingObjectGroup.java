package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountingObjectGroup.
 */
@Entity
@Table(name = "accountingobjectgroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountingObjectGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Size(max = 25)
    @Column(name = "accountingobjectgroupcode")
    private String accountingObjectGroupCode;

    @Size(max = 512)
    @Column(name = "accountingobjectgroupname")
    private String accountingObjectGroupName;

    @Column(name = "parentid")
    private UUID parentId;

    @Column(name = "isparentnode")
    private Boolean isParentNode;

    @Column(name = "orderfixcode")
    private String orderFixCode;

    @Column(name = "grade")
    private Integer grade;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

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

    public Integer getGrade() {
        return grade;
    }

    public AccountingObjectGroup grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getAccountingObjectGroup() {
        return accountingObjectGroupCode;
    }

    public String getDescription() {
        return description;
    }

    public AccountingObjectGroup description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        AccountingObjectGroup accountingObjectGroup = (AccountingObjectGroup) o;
        if (accountingObjectGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountingObjectGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountingObjectGroup{" +
            "id=" + getId() +
            "}";
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public AccountingObjectGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getParentNode() {
        return isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }


    public AccountingObjectGroup parentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentId = parentID;
    }


    public String getAccountingObjectGroupCode() {
        return accountingObjectGroupCode;
    }

    public void setAccountingObjectGroupCode(String accountingObjectGroupCode) {
        this.accountingObjectGroupCode = accountingObjectGroupCode;
    }

    public String getAccountingObjectGroupName() {
        return accountingObjectGroupName;
    }

    public void setAccountingObjectGroupName(String accountingObjectGroupName) {
        this.accountingObjectGroupName = accountingObjectGroupName;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Boolean getIsParentNode() {
        return isParentNode;
    }

    public AccountingObjectGroup isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsSecurity() {
        return isSecurity;
    }

    public AccountingObjectGroup isIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean security) {
        isSecurity = security;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }
}
