package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A Toolledger.
 */
@Entity
@Table(name = "fixedassetledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FixedAssetLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "referenceid", nullable = false)
    private UUID referenceID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Size(max = 25)
    @Column(name = "fixedassetcode", length = 25)
    private String fixedAssetCode;

    @Size(max = 255)
    @Column(name = "fixedassetname", length = 255)
    private String fixedAssetName;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "usedtime")
    private BigDecimal usedTime;

    @Column(name = "depreciationrate")
    private BigDecimal depreciationRate;

    @Column(name = "depreciationamount")
    private BigDecimal depreciationAmount;

    @Size(max = 25)
    @Column(name = "depreciationaccount", length = 25)
    private String depreciationAccount;

    @Column(name = "originalprice")
    private BigDecimal originalPrice;

    @Size(max = 25)
    @Column(name = "originalpriceaccount", length = 25)
    private String originalPriceAccount;

    @Column(name = "usedtimeremain")
    private BigDecimal usedTimeRemain;

    @Column(name = "differusedtime")
    private BigDecimal differUsedTime;

    @Column(name = "monthdepreciationrate")
    private BigDecimal monthDepreciationRate;

    @Column(name = "monthperioddepreciationamount")
    private BigDecimal monthPeriodDepreciationAmount;

    @Column(name = "acdepreciationamount")
    private BigDecimal acDepreciationAmount;

    @Column(name = "remainingamount")
    private BigDecimal remainingAmount;

    @Column(name = "differOrgPrice")
    private BigDecimal differOrgPrice;

    @Column(name = "differacdepreciationamount")
    private BigDecimal differAcDepreciationAmount;

    @Column(name = "differmonthlydepreciationamount")
    private BigDecimal differMonthlyDepreciationAmount;

    @Column(name = "differremainingamount")
    private BigDecimal differRemainingAmount;

    @Column(name = "differdepreciationamount")
    private BigDecimal differDepreciationAmount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public String getFixedAssetCode() {
        return fixedAssetCode;
    }

    public void setFixedAssetCode(String fixedAssetCode) {
        this.fixedAssetCode = fixedAssetCode;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public BigDecimal getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public String getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOriginalPriceAccount() {
        return originalPriceAccount;
    }

    public void setOriginalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
    }

    public BigDecimal getUsedTimeRemain() {
        return usedTimeRemain;
    }

    public void setUsedTimeRemain(BigDecimal usedTimeRemain) {
        this.usedTimeRemain = usedTimeRemain;
    }

    public BigDecimal getDifferUsedTime() {
        return differUsedTime;
    }

    public void setDifferUsedTime(BigDecimal differUsedTime) {
        this.differUsedTime = differUsedTime;
    }

    public BigDecimal getMonthDepreciationRate() {
        return monthDepreciationRate;
    }

    public void setMonthDepreciationRate(BigDecimal monthDepreciationRate) {
        this.monthDepreciationRate = monthDepreciationRate;
    }

    public BigDecimal getMonthPeriodDepreciationAmount() {
        return monthPeriodDepreciationAmount;
    }

    public void setMonthPeriodDepreciationAmount(BigDecimal monthPeriodDepreciationAmount) {
        this.monthPeriodDepreciationAmount = monthPeriodDepreciationAmount;
    }

    public BigDecimal getAcDepreciationAmount() {
        return acDepreciationAmount;
    }

    public void setAcDepreciationAmount(BigDecimal acDepreciationAmount) {
        this.acDepreciationAmount = acDepreciationAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getDifferOrgPrice() {
        return differOrgPrice;
    }

    public void setDifferOrgPrice(BigDecimal differOrgPrice) {
        this.differOrgPrice = differOrgPrice;
    }

    public BigDecimal getDifferAcDepreciationAmount() {
        return differAcDepreciationAmount;
    }

    public void setDifferAcDepreciationAmount(BigDecimal differAcDepreciationAmount) {
        this.differAcDepreciationAmount = differAcDepreciationAmount;
    }

    public BigDecimal getDifferMonthlyDepreciationAmount() {
        return differMonthlyDepreciationAmount;
    }

    public void setDifferMonthlyDepreciationAmount(BigDecimal differMonthlyDepreciationAmount) {
        this.differMonthlyDepreciationAmount = differMonthlyDepreciationAmount;
    }

    public BigDecimal getDifferRemainingAmount() {
        return differRemainingAmount;
    }

    public void setDifferRemainingAmount(BigDecimal differRemainingAmount) {
        this.differRemainingAmount = differRemainingAmount;
    }

    public BigDecimal getDifferDepreciationAmount() {
        return differDepreciationAmount;
    }

    public void setDifferDepreciationAmount(BigDecimal differDepreciationAmount) {
        this.differDepreciationAmount = differDepreciationAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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
        FixedAssetLedger toolledger = (FixedAssetLedger) o;
        if (toolledger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), toolledger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixedAssetLedger{" +
            "id=" + id +
            ", companyID=" + companyID +
            ", branchID=" + branchID +
            ", referenceID=" + referenceID +
            ", typeLedger=" + typeLedger +
            ", noFBook='" + noFBook + '\'' +
            ", noMBook='" + noMBook + '\'' +
            ", typeID=" + typeID +
            ", date=" + date +
            ", postedDate=" + postedDate +
            ", reason='" + reason + '\'' +
            ", description='" + description + '\'' +
            ", fixedAssetID=" + fixedAssetID +
            ", fixedAssetCode='" + fixedAssetCode + '\'' +
            ", fixedAssetName='" + fixedAssetName + '\'' +
            ", departmentID=" + departmentID +
            ", usedTime=" + usedTime +
            ", depreciationRate=" + depreciationRate +
            ", depreciationAmount=" + depreciationAmount +
            ", depreciationAccount='" + depreciationAccount + '\'' +
            ", originalPrice=" + originalPrice +
            ", originalPriceAccount='" + originalPriceAccount + '\'' +
            ", usedTimeRemain=" + usedTimeRemain +
            ", differUsedTime=" + differUsedTime +
            ", monthDepreciationRate=" + monthDepreciationRate +
            ", monthPeriodDepreciationAmount=" + monthPeriodDepreciationAmount +
            ", acDepreciationAmount=" + acDepreciationAmount +
            ", remainingAmount=" + remainingAmount +
            ", differOrgPrice=" + differOrgPrice +
            ", differAcDepreciationAmount=" + differAcDepreciationAmount +
            ", differMonthlyDepreciationAmount=" + differMonthlyDepreciationAmount +
            ", differRemainingAmount=" + differRemainingAmount +
            ", differDepreciationAmount=" + differDepreciationAmount +
            ", orderPriority=" + orderPriority +
            '}';
    }
}
