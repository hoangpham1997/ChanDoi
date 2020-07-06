package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIAllocationAllocated.
 */
@Entity
@Table(name = "tiallocationallocated")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TIAllocationAllocated implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiallocationid")
    private UUID tiAllocationID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Column(name = "objectid")
    private UUID objectID;

    @Column(name = "objecttype")
    private Integer objectType;

    @Column(name = "totalallocationamount", precision = 10, scale = 2)
    private BigDecimal totalAllocationAmount;

    @Column(name = "rate", precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

    @Size(max = 25)
    @Column(name = "costaccount", length = 25)
    private String costAccount;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Integer getObjectType() {
        return objectType;
    }

    public TIAllocationAllocated objectType(Integer objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getTotalAllocationAmount() {
        return totalAllocationAmount;
    }

    public TIAllocationAllocated totalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
        return this;
    }

    public void setTotalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public TIAllocationAllocated rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public TIAllocationAllocated allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public TIAllocationAllocated costAccount(String costAccount) {
        this.costAccount = costAccount;
        return this;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAllocationID() {
        return tiAllocationID;
    }

    public void setTiAllocationID(UUID tiAllocationID) {
        this.tiAllocationID = tiAllocationID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIAllocationAllocated orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
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
        TIAllocationAllocated tIAllocationAllocated = (TIAllocationAllocated) o;
        if (tIAllocationAllocated.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAllocationAllocated.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAllocationAllocated{" +
            "id=" + getId() +
            ", tiAllocationID='" + getTiAllocationID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", objectID='" + getObjectID() + "'" +
            ", objectType=" + getObjectType() +
            ", totalAllocationAmount=" + getTotalAllocationAmount() +
            ", rate=" + getRate() +
            ", allocationAmount=" + getAllocationAmount() +
            ", costAccount='" + getCostAccount() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
