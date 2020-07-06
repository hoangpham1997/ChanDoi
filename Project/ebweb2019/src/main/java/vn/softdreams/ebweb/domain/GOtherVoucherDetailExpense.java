package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseAllocationViewDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseViewDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A GOtherVoucherDetailExpense.
 */
@Entity
@Table(name = "gOthervoucherdetailexpense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailExpenseViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailExpenseViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "gothervoucherid", type = UUID.class),
                    @ColumnResult(name = "prepaidexpenseid", type = UUID.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingamount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationamount", type = BigDecimal.class),
                    @ColumnResult(name = "orderpriority", type = Integer.class),
                    @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                    @ColumnResult(name = "prepaidExpenseName", type = String.class),
                }
            )
        }
    ),
})
public class GOtherVoucherDetailExpense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "gothervoucherid")
    private UUID gOtherVoucherID;

    @Column(name = "prepaidexpenseid")
    private UUID prepaidExpenseID;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "remainingamount", precision = 10, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public GOtherVoucherDetailExpense amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public GOtherVoucherDetailExpense remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public GOtherVoucherDetailExpense allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public GOtherVoucherDetailExpense orderPriority(Integer orderPriority) {
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
        GOtherVoucherDetailExpense gOtherVoucherDetailExpense = (GOtherVoucherDetailExpense) o;
        if (gOtherVoucherDetailExpense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gOtherVoucherDetailExpense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GOtherVoucherDetailExpense{" +
            "id=" + getId() +
            ", gOtherVoucherID='" + getgOtherVoucherID() + "'" +
            ", prepaidExpenseID='" + getPrepaidExpenseID() + "'" +
            ", amount=" + getAmount() +
            ", remainingAmount=" + getRemainingAmount() +
            ", allocationAmount=" + getAllocationAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
