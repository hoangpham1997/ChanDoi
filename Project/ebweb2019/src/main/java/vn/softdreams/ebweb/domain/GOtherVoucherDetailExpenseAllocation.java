package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseAllocationViewDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A GOtherVoucherDetailExpenseAllocation.
 */
@Entity
@Table(name = "gothervoucherdetailexpenseallocation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailExpenseAllocationViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailExpenseAllocationViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "gOtherVoucherID", type = UUID.class),
                    @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "allocationRate", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "costSetId", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = String.class),
                    @ColumnResult(name = "prepaidExpenseID", type = UUID.class),
                    @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                    @ColumnResult(name = "prepaidExpenseName", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                }
            )
        }
    ),
})
public class GOtherVoucherDetailExpenseAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "gothervoucherid")
    private UUID gOtherVoucherID;

    @Column(name = "prepaidexpenseid")
    private UUID prepaidExpenseID;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

    @Column(name = "objectid")
    private UUID objectID;

    @Column(name = "objecttype")
    private Integer objectType;

    @Column(name = "allocationrate", precision = 10, scale = 2)
    private BigDecimal allocationRate;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

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
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public GOtherVoucherDetailExpenseAllocation allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public GOtherVoucherDetailExpenseAllocation objectType(Integer objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public GOtherVoucherDetailExpenseAllocation allocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
        return this;
    }

    public void setAllocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public GOtherVoucherDetailExpenseAllocation amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public GOtherVoucherDetailExpenseAllocation costAccount(String costAccount) {
        this.costAccount = costAccount;
        return this;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public GOtherVoucherDetailExpenseAllocation orderPriority(Integer orderPriority) {
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
        GOtherVoucherDetailExpenseAllocation gOtherVoucherDetailExpenseAllocation = (GOtherVoucherDetailExpenseAllocation) o;
        if (gOtherVoucherDetailExpenseAllocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gOtherVoucherDetailExpenseAllocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GOtherVoucherDetailExpenseAllocation{" +
            "id=" + getId() +
            ", gOtherVoucherID='" + getgOtherVoucherID() + "'" +
            ", prepaidExpenseID='" + getPrepaidExpenseID() + "'" +
            ", allocationAmount=" + getAllocationAmount() +
            ", objectID='" + getObjectID() + "'" +
            ", objectType=" + getObjectType() +
            ", allocationRate=" + getAllocationRate() +
            ", amount=" + getAmount() +
            ", costAccount='" + getCostAccount() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
