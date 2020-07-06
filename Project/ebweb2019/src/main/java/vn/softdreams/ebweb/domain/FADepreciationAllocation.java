package vn.softdreams.ebweb.domain;

import org.apache.poi.hpsf.Decimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FADepreciationAllocation.
 */
@Entity
@Table(name = "fadecrementdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FADepreciationAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fadepreciationid", nullable = false)
    private UUID faDepreciationID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "description")
    private String description;

    @Column(name = "objectid")
    private UUID objectID;

    @Column(name = "objecttype")
    private int objectType;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "allocationamount")
    private BigDecimal allocationAmount;

    @Column(name = "costaccount")
    private String costAccount;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFaDepreciationID() {
        return faDepreciationID;
    }

    public void setFaDepreciationID(UUID faDepreciationID) {
        this.faDepreciationID = faDepreciationID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
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
        FADepreciationAllocation faDecrementDetails = (FADepreciationAllocation) o;
        if (faDecrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faDecrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
