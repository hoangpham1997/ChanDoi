package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A OrganizationUnit.
 */
@Entity
@Table(name = "eborganizationunit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "OrganizationUnitCustomDTO",
        classes = {
            @ConstructorResult(
                targetClass = OrganizationUnitCustomDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "organizationUnitCode", type = String.class),
                    @ColumnResult(name = "organizationUnitName", type = String.class),
                    @ColumnResult(name = "unitType", type = Integer.class),
                    @ColumnResult(name = "accType", type = Integer.class),
                    @ColumnResult(name = "parentID", type = UUID.class),
                }
            )
        }
    ), @SqlResultSetMapping(
        name = "OrganizationUnitCustomTIDTO",
        classes = {
            @ConstructorResult(
                targetClass = OrganizationUnitCustomDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "organizationUnitCode", type = String.class),
                    @ColumnResult(name = "organizationUnitName", type = String.class),
                    @ColumnResult(name = "unitType", type = Integer.class),
                    @ColumnResult(name = "accType", type = Integer.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "quantityRest", type = BigDecimal.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                }
            )
        }
    ), @SqlResultSetMapping(
        name = "OrganizationUnitCustomTIDTO2",
        classes = {
            @ConstructorResult(
                targetClass = OrganizationUnitCustomDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "organizationUnitCode", type = String.class),
                    @ColumnResult(name = "organizationUnitName", type = String.class),
                    @ColumnResult(name = "unitType", type = Integer.class),
                    @ColumnResult(name = "accType", type = Integer.class),
                    @ColumnResult(name = "quantityRest", type = BigDecimal.class),
                }
            )
        }
    )
})
public class OrganizationUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "organizationunitcode", nullable = false)
    private String organizationUnitCode;

    @Column(name = "organizationunitname", nullable = false)
    private String organizationUnitName;

    @Column(name = "accountingtype", nullable = false)
    private Integer accountingType;

    @Column(name = "unittype", nullable = false)
    private Integer unitType;

    @Column(name = "organizationunitename")
    private String organizationUnitEName;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "businessregistrationnumber")
    private String businessRegistrationNumber;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "issuedate")
    private LocalDate issueDate;

    @Column(name = "issueby")
    private String issueBy;

    @Column(name = "acctype")
    private Integer accType;

    @Column(name = "isprivatevat")
    private Boolean isPrivateVAT;

    @Column(name = "costaccount")
    private String costAccount;

    @Column(name = "financialyear")
    private Integer financialYear;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fromdate")
    private LocalDate fromDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "todate")
    private LocalDate toDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "startdate")
    private LocalDate startDate;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "taxcalculationmethod")
    private Integer taxCalculationMethod;

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseID;

    @Column(name = "careergroupid")
    private UUID careerGroupID;

    @Column(name = "orderfixcode")
    private String orderFixCode;


    @Column(name = "parentid")
    private UUID parentID;

    @Column(name = "isparentnode", nullable = false)
    private Boolean isParentNode;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @Column(name = "userid", nullable = false)
    private Long userID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID packageID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer status;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean isTotalPackage;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean isHaveOrg;

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private OrganizationUnitOptionReport organizationUnitOptionReport;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "eborganizationunitgroup",
        joinColumns = {@JoinColumn(name = "orgid", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "groupid", referencedColumnName = "id")})
    private Set<EbGroup> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public OrganizationUnit branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public OrganizationUnit organizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
        return this;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public OrganizationUnit organizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
        return this;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public OrganizationUnit unitType(Integer unitType) {
        this.unitType = unitType;
        return this;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getAddress() {
        return address;
    }

    public OrganizationUnit address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public OrganizationUnit businessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
        return this;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public OrganizationUnit issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public OrganizationUnit issueBy(String issueBy) {
        this.issueBy = issueBy;
        return this;
    }

    public OrganizationUnitOptionReport getOrganizationUnitOptionReport() {
        return organizationUnitOptionReport;
    }

    public void setOrganizationUnitOptionReport(OrganizationUnitOptionReport organizationUnitOptionReport) {
        this.organizationUnitOptionReport = organizationUnitOptionReport;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public OrganizationUnit costAccount(String costAccount) {
        this.costAccount = costAccount;
        return this;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public OrganizationUnit orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public UUID getParentID() {
        return parentID;
    }

    public OrganizationUnit parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public OrganizationUnit isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public Integer getGrade() {
        return grade;
    }

    public OrganizationUnit grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public String getOrganizationUnitEName() {
        return organizationUnitEName;
    }

    public void setOrganizationUnitEName(String organizationUnitEName) {
        this.organizationUnitEName = organizationUnitEName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String gettaxCode() {
        return taxCode;
    }

    public void settaxCode(String taxCode) {
        this.taxCode = taxCode;
    }


    public Boolean getParentNode() {
        return isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(Integer financialYear) {
        this.financialYear = financialYear;
    }

    public Integer getTaxCalculationMethod() {
        return taxCalculationMethod;
    }

    public void setTaxCalculationMethod(Integer taxCalculationMethod) {
        this.taxCalculationMethod = taxCalculationMethod;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public Set<EbGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<EbGroup> groups) {
        this.groups = groups;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Boolean isIsTotalPackage() {
        return isTotalPackage;
    }

    public void setIsTotalPackage(Boolean isTotalPackage) {
        this.isTotalPackage = isTotalPackage;
    }

    public Boolean getIsHaveOrg() {
        return isHaveOrg;
    }

    public void setIsHaveOrg(Boolean haveOrg) {
        isHaveOrg = haveOrg;
    }

    public Boolean getIsPrivateVAT() {
        return isPrivateVAT;
    }

    public void setIsPrivateVAT(Boolean privateVAT) {
        isPrivateVAT = privateVAT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrganizationUnit organizationUnit = (OrganizationUnit) o;
        if (organizationUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationUnit{" +
            "id=" + getId() +
            ", branchID='" + getBranchID() + "'" +
            ", organizationUnitCode='" + getOrganizationUnitCode() + "'" +
            ", organizationUnitName='" + getOrganizationUnitName() + "'" +
            ", unitType=" + getUnitType() +
            ", address='" + getAddress() + "'" +
            ", businessRegistrationNumber='" + getBusinessRegistrationNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", issueBy='" + getIssueBy() + "'" +
            ", accType='" + getAccType() + "'" +
            ", costAccount='" + getCostAccount() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", grade=" + getGrade() +
            "}";
    }
}
