package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FixedAssetDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A FixedAsset.
 */
@Entity
@Table(name = "fixedasset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "FixedAssetActiveDTO",
        classes = {
            @ConstructorResult(
                targetClass = FixedAssetDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "fixedAssetCode", type = String.class),
                    @ColumnResult(name = "fixedAssetName", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "originalPrice", type = BigDecimal.class)
                }
            )
        }),
    @SqlResultSetMapping(
        name = "FixedAssetLedgerDTO",
        classes = {
            @ConstructorResult(
                targetClass = FixedAssetDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "fixedAssetCode", type = String.class),
                    @ColumnResult(name = "fixedAssetName", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "originalPrice", type = BigDecimal.class),
                    @ColumnResult(name = "usedTime", type = BigDecimal.class),
                    @ColumnResult(name = "depreciationRate", type = BigDecimal.class),
                    @ColumnResult(name = "purchasePrice", type = BigDecimal.class),
                    @ColumnResult(name = "depreciationAccount", type = String.class),
                    @ColumnResult(name = "originalPriceAccount", type = String.class),
                    @ColumnResult(name = "monthDepreciationRate", type = BigDecimal.class),
                    @ColumnResult(name = "monthPeriodDepreciationAmount", type = BigDecimal.class)
                }
            )
        })
})
public class FixedAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "fixedassetcode", length = 25, nullable = false)
    private String fixedAssetCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "fixedassetname", length = 512, nullable = false)
    private String fixedAssetName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "productionyear")
    private Integer productionYear;

    @Size(max = 100)
    @Column(name = "madein", length = 100)
    private String madeIn;

    @Size(max = 100)
    @Column(name = "serialnumber", length = 100)
    private String serialNumber;

    @Column(name="depreciationaccount")
    private String depreciationAccount;

    @Column(name="originalpriceaccount")
    private String originalPriceAccount;

    @Column(name="expenditureaccount")
    private String expenditureAccount;

    @Size(max = 512)
    @Column(name = "accountingObjectName", length = 512)
    private String accountingObjectName;

    @Column(name="warranty")
    private String warranty;

    @Size(max = 20)
    @Column(name = "guaranteecodition", length = 20)
    private String guaranteeCodition;

    @NotNull
    @Column(name = "issecondhand", nullable = false)
    private Boolean isSecondHand;

    @Column(name = "currentstate")
    private Integer currentState;

    @Size(max = 50)
    @Column(name = "deliveryrecordno", length = 50)
    private String deliveryRecordNo;

    @Column(name = "deliveryrecorddate")
    private LocalDate deliveryRecordDate;

    @Column(name = "purchaseddate")
    private LocalDate purchasedDate;

    @Column(name = "incrementdate")
    private LocalDate incrementDate;

    @Column(name = "depreciationdate")
    private LocalDate depreciationDate;

    @Column(name = "useddate")
    private LocalDate usedDate;

    @Column(name = "purchaseprice", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "originalprice", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "depreciationmethod")
    private Integer depreciationMethod;

    @Column(name = "usedtime", precision = 10, scale = 2)
    private BigDecimal usedTime;

    @Column(name = "displaymonthyear")
    private Boolean displayMonthYear;

    @Column(name = "perioddepreciationamount", precision = 10, scale = 2)
    private BigDecimal periodDepreciationAmount;

    @Column(name = "depreciationrate", precision = 10, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "monthdepreciationrate", precision = 10, scale = 2)
    private BigDecimal monthDepreciationRate;

    @Column(name = "monthperioddepreciationamount", precision = 10, scale = 2)
    private BigDecimal monthPeriodDepreciationAmount;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="fixedassetcategoryID")
    private FixedAssetCategory fixedAssetCategoryID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="branchID")
    private OrganizationUnit branchID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="departmentid")
    private OrganizationUnit organizationUnitID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="accountingobjectID")
    private AccountingObject accountingObjectID;


    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fixedassetid")
    private Set<FixedAssetAccessories> fixedAssetAccessories = new HashSet<>();

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fixedassetid")
    private Set<FixedAssetAllocation> fixedAssetAllocation = new HashSet<>();

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fixedassetid")
    private Set<FixedAssetDetails> fixedAssetDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFixedAssetCode() {
        return fixedAssetCode;
    }

    public FixedAsset fixedAssetCode(String fixedAssetCode) {
        this.fixedAssetCode = fixedAssetCode;
        return this;
    }

    public void setFixedAssetCode(String fixedAssetCode) {
        this.fixedAssetCode = fixedAssetCode;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public FixedAsset fixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
        return this;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public String getDescription() {
        return description;
    }

    public FixedAsset description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public FixedAsset productionYear(Integer productionYear) {
        this.productionYear = productionYear;
        return this;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public FixedAsset madeIn(String madeIn) {
        this.madeIn = madeIn;
        return this;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public FixedAsset serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public FixedAsset accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Boolean getSecondHand() {
        return isSecondHand;
    }

    public void setSecondHand(Boolean secondHand) {
        isSecondHand = secondHand;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getDisplayMonthYear() {
        return displayMonthYear;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }


    public String getGuaranteeCodition() {
        return guaranteeCodition;
    }

    public FixedAsset guaranteeCodition(String guaranteeCodition) {
        this.guaranteeCodition = guaranteeCodition;
        return this;
    }

    public void setGuaranteeCodition(String guaranteeCodition) {
        this.guaranteeCodition = guaranteeCodition;
    }

    public Boolean isIsSecondHand() {
        return isSecondHand;
    }

    public FixedAsset isSecondHand(Boolean isSecondHand) {
        this.isSecondHand = isSecondHand;
        return this;
    }

    public void setIsSecondHand(Boolean isSecondHand) {
        this.isSecondHand = isSecondHand;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public FixedAsset currentState(Integer currentState) {
        this.currentState = currentState;
        return this;
    }

    public void setCurrentState(Integer currentState) {
        this.currentState = currentState;
    }

    public String getDeliveryRecordNo() {
        return deliveryRecordNo;
    }

    public FixedAsset deliveryRecordNo(String deliveryRecordNo) {
        this.deliveryRecordNo = deliveryRecordNo;
        return this;
    }

    public void setDeliveryRecordNo(String deliveryRecordNo) {
        this.deliveryRecordNo = deliveryRecordNo;
    }

    public LocalDate getDeliveryRecordDate() {
        return deliveryRecordDate;
    }

    public FixedAsset deliveryRecordDate(LocalDate deliveryRecordDate) {
        this.deliveryRecordDate = deliveryRecordDate;
        return this;
    }

    public void setDeliveryRecordDate(LocalDate deliveryRecordDate) {
        this.deliveryRecordDate = deliveryRecordDate;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public FixedAsset purchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
        return this;
    }

    public void setPurchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public LocalDate getIncrementDate() {
        return incrementDate;
    }

    public FixedAsset incrementDate(LocalDate incrementDate) {
        this.incrementDate = incrementDate;
        return this;
    }

    public void setIncrementDate(LocalDate incrementDate) {
        this.incrementDate = incrementDate;
    }

    public LocalDate getDepreciationDate() {
        return depreciationDate;
    }

    public FixedAsset depreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
        return this;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public LocalDate getUsedDate() {
        return usedDate;
    }

    public FixedAsset usedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
        return this;
    }

    public void setUsedDate(LocalDate usedDate) {
        this.usedDate = usedDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public FixedAsset purchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public FixedAsset originalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
        return this;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getDepreciationMethod() {
        return depreciationMethod;
    }

    public FixedAsset depreciationMethod(Integer depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
        return this;
    }

    public void setDepreciationMethod(Integer depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public BigDecimal getUsedTime() {
        return usedTime;
    }

    public FixedAsset usedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
        return this;
    }

    public void setUsedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
    }

    public Boolean isDisplayMonthYear() {
        return displayMonthYear;
    }

    public FixedAsset displayMonthYear(Boolean displayMonthYear) {
        this.displayMonthYear = displayMonthYear;
        return this;
    }

    public void setDisplayMonthYear(Boolean displayMonthYear) {
        this.displayMonthYear = displayMonthYear;
    }

    public BigDecimal getPeriodDepreciationAmount() {
        return periodDepreciationAmount;
    }

    public FixedAsset periodDepreciationAmount(BigDecimal periodDepreciationAmount) {
        this.periodDepreciationAmount = periodDepreciationAmount;
        return this;
    }

    public void setPeriodDepreciationAmount(BigDecimal periodDepreciationAmount) {
        this.periodDepreciationAmount = periodDepreciationAmount;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public FixedAsset depreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
        return this;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getMonthDepreciationRate() {
        return monthDepreciationRate;
    }

    public FixedAsset monthDepreciationRate(BigDecimal monthDepreciationRate) {
        this.monthDepreciationRate = monthDepreciationRate;
        return this;
    }

    public void setMonthDepreciationRate(BigDecimal monthDepreciationRate) {
        this.monthDepreciationRate = monthDepreciationRate;
    }

    public BigDecimal getMonthPeriodDepreciationAmount() {
        return monthPeriodDepreciationAmount;
    }

    public FixedAsset monthPeriodDepreciationAmount(BigDecimal monthPeriodDepreciationAmount) {
        this.monthPeriodDepreciationAmount = monthPeriodDepreciationAmount;
        return this;
    }

    public void setMonthPeriodDepreciationAmount(BigDecimal monthPeriodDepreciationAmount) {
        this.monthPeriodDepreciationAmount = monthPeriodDepreciationAmount;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public FixedAsset isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public FixedAssetCategory getFixedAssetCategoryID() {
        return fixedAssetCategoryID;
    }

    public FixedAsset fixedAssetCategoryID(FixedAssetCategory fixedAssetCategory) {
        this.fixedAssetCategoryID = fixedAssetCategory;
        return this;
    }

    public void setFixedAssetCategoryID(FixedAssetCategory fixedAssetCategory) {
        this.fixedAssetCategoryID = fixedAssetCategory;
    }

    public OrganizationUnit getBranchID() {
        return branchID;
    }

    public FixedAsset branchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
        return this;
    }

    public void setBranchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
    }

    public OrganizationUnit getOrganizationUnitID() {
        return organizationUnitID;
    }

    public FixedAsset organizationUnitID(OrganizationUnit organizationUnit) {
        this.organizationUnitID = organizationUnit;
        return this;
    }

    public void setOrganizationUnitID(OrganizationUnit organizationUnit) {
        this.organizationUnitID = organizationUnit;
    }

    public AccountingObject getAccountingObjectID() {
        return accountingObjectID;
    }

    public FixedAsset accountingObjectID(AccountingObject accountingObject) {
        this.accountingObjectID = accountingObject;
        return this;
    }

    public void setAccountingObjectID(AccountingObject accountingObject) {
        this.accountingObjectID = accountingObject;
    }

    public String getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public String getExpenditureAccount() {
        return expenditureAccount;
    }

    public void setExpenditureAccount(String expenditureAccount) {
        this.expenditureAccount = expenditureAccount;
    }

    public String getOriginalPriceAccount() {
        return originalPriceAccount;
    }

    public void setOriginalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
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
        FixedAsset fixedAsset = (FixedAsset) o;
        if (fixedAsset.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fixedAsset.getId());
    }

    public void setFixedAssetAccessories(Set<FixedAssetAccessories> fixedAssetAccessories) {
        this.fixedAssetAccessories = fixedAssetAccessories;
    }

    public Set<FixedAssetAccessories> getFixedAssetAccessories() {
        return fixedAssetAccessories;
    }

    public void setFixedAssetAllocation(Set<FixedAssetAllocation> fixedAssetAllocation) {
        this.fixedAssetAllocation = fixedAssetAllocation;
    }

    public Set<FixedAssetAllocation> getFixedAssetAllocation() {
        return fixedAssetAllocation;
    }

    public void setFixedAssetDetails(Set<FixedAssetDetails> fixedAssetDetails) {
        this.fixedAssetDetails = fixedAssetDetails;
    }

    public Set<FixedAssetDetails> getFixedAssetDetails() {
        return fixedAssetDetails;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixedAsset{" +
            "id=" + getId() +
            ", fixedAssetCode='" + getFixedAssetCode() + "'" +
            ", fixedAssetName='" + getFixedAssetName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productionYear=" + getProductionYear() +
            ", madeIn='" + getMadeIn() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", warranty='" + getWarranty() + "'" +
            ", guaranteeCodition='" + getGuaranteeCodition() + "'" +
            ", isSecondHand='" + isIsSecondHand() + "'" +
            ", currentState=" + getCurrentState() +
            ", deliveryRecordNo='" + getDeliveryRecordNo() + "'" +
            ", deliveryRecordDate='" + getDeliveryRecordDate() + "'" +
            ", purchasedDate='" + getPurchasedDate() + "'" +
            ", incrementDate='" + getIncrementDate() + "'" +
            ", depreciationDate='" + getDepreciationDate() + "'" +
            ", usedDate='" + getUsedDate() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", originalPrice=" + getOriginalPrice() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", usedTime=" + getUsedTime() +
            ", displayMonthYear='" + isDisplayMonthYear() + "'" +
            ", periodDepreciationAmount=" + getPeriodDepreciationAmount() +
            ", depreciationRate=" + getDepreciationRate() +
            ", monthDepreciationRate=" + getMonthDepreciationRate() +
            ", monthPeriodDepreciationAmount=" + getMonthPeriodDepreciationAmount() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
