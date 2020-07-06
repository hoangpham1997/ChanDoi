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
 * A PrepaidExpenseAllocation.
 */
@Entity
@Table(name = "prepaidexpenseallocation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrepaidExpenseAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "prepaidexpenseid")
    private UUID prepaidExpenseID;

    @Column(name = "allocationobjectid")
    private UUID allocationObjectID;

    @Column(name = "allocationobjecttype")
    private Integer allocationObjectType;

    @Size(max = 512)
    @Column(name = "allocationobjectname", length = 512)
    private String allocationObjectName;

    @Column(name = "allocationrate", precision = 10, scale = 2)
    private BigDecimal allocationRate;

    @Size(max = 25)
    @Column(name = "costaccount", length = 25)
    private String costAccount;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Integer getAllocationObjectType() {
        return allocationObjectType;
    }

    public PrepaidExpenseAllocation allocationObjectType(Integer allocationObjectType) {
        this.allocationObjectType = allocationObjectType;
        return this;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public void setAllocationObjectType(Integer allocationObjectType) {
        this.allocationObjectType = allocationObjectType;
    }

    public String getAllocationObjectName() {
        return allocationObjectName;
    }

    public PrepaidExpenseAllocation allocationObjectName(String allocationObjectName) {
        this.allocationObjectName = allocationObjectName;
        return this;
    }

    public void setAllocationObjectName(String allocationObjectName) {
        this.allocationObjectName = allocationObjectName;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public PrepaidExpenseAllocation allocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
        return this;
    }

    public void setAllocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public PrepaidExpenseAllocation costAccount(String costAccount) {
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

//    public UUID getPrepaidExpenseID() {
//        return prepaidExpenseID;
//    }
//
//    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
//        this.prepaidExpenseID = prepaidExpenseID;
//    }

    public UUID getAllocationObjectID() {
        return allocationObjectID;
    }

    public void setAllocationObjectID(UUID allocationObjectID) {
        this.allocationObjectID = allocationObjectID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        PrepaidExpenseAllocation prepaidExpenseAllocation = (PrepaidExpenseAllocation) o;
//        if (prepaidExpenseAllocation.getId() == null || getId() == null) {
//            return false;
//        }
//        return Objects.equals(getId(), prepaidExpenseAllocation.getId());
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hashCode(getId());
//    }

    @Override
    public String toString() {
        return "PrepaidExpenseAllocation{" +
            "id=" + getId() +
//            ", prepaidExpenseID='" + getPrepaidExpenseID() + "'" +
            ", allocationObjectID='" + getAllocationObjectID() + "'" +
            ", allocationObjectType=" + getAllocationObjectType() +
            ", allocationObjectName='" + getAllocationObjectName() + "'" +
            ", allocationRate=" + getAllocationRate() +
            ", costAccount='" + getCostAccount() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            "}";
    }
}
