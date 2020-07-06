package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A StatisticsCode.
 */
@Entity
@Table(name = "statisticscode")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StatisticsCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column(name = "companyid")
    private UUID companyID;

    @Size(max = 25)
    @Column(name = "statisticscode", length = 25)
    private String statisticsCode;

    @Size(max = 512)
    @Column(name = "statisticscodename", length = 512)
    private String statisticsCodeName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
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

    @Column(name = "issecurity")
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public StatisticsCode statisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
        return this;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getStatisticsCodeName() {
        return statisticsCodeName;
    }

    public StatisticsCode statisticsCodeName(String statisticsCodeName) {
        this.statisticsCodeName = statisticsCodeName;
        return this;
    }

    public void setStatisticsCodeName(String statisticsCodeName) {
        this.statisticsCodeName = statisticsCodeName;
    }

    public String getDescription() {
        return description;
    }

    public StatisticsCode description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentID() {
        return parentID;
    }

    public StatisticsCode parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public StatisticsCode isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public StatisticsCode orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public StatisticsCode grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public StatisticsCode isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public StatisticsCode isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

//    public Boolean getActive() {
//        return isActive;
//    }
//
//    public Boolean getParentNode() {
//        return isParentNode;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

//    public Boolean getSecurity() {
//        return isSecurity;
//    }
//
//    public void setSecurity(Boolean security) {
//        isSecurity = security;
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
        StatisticsCode statisticsCode = (StatisticsCode) o;
        if (statisticsCode.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statisticsCode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatisticsCode{" +
            "id=" + getId() +
            ", statisticsCode='" + getStatisticsCode() + "'" +
            ", statisticsCodeName='" + getStatisticsCodeName() + "'" +
            ", description='" + getDescription() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
