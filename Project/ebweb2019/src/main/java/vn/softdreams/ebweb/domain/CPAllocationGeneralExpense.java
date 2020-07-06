package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAllocationExpenseDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAllocationGeneralExpense.
 */
@Entity
@Table(name = "cpallocationgeneralexpense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAllocationExpenseDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAllocationExpenseDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "totalCost", type = BigDecimal.class),
                    @ColumnResult(name = "unallocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationMethod", type = Integer.class),
                    @ColumnResult(name = "refDetailID", type = UUID.class),
                    @ColumnResult(name = "refID", type = UUID.class)
                }
            )
        }
    )
})
public class CPAllocationGeneralExpense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Size(max = 25)
    @Column(name = "accountnumber", length = 25)
    private String accountNumber;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "totalcost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "unallocatedamount", precision = 10, scale = 2)
    private BigDecimal unallocatedAmount;

    @Column(name = "allocatedrate", precision = 10, scale = 2)
    private BigDecimal allocatedRate;

    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "allocationmethod")
    private Integer allocationMethod;

    @Column(name = "refDetailID")
    private UUID refDetailID;

    @Column(name = "refID")
    private UUID refID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public CPAllocationGeneralExpense cPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
        return this;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public CPAllocationGeneralExpense accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public CPAllocationGeneralExpense expenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
        return this;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public CPAllocationGeneralExpense totalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getUnallocatedAmount() {
        return unallocatedAmount;
    }

    public CPAllocationGeneralExpense unallocatedAmount(BigDecimal unallocatedAmount) {
        this.unallocatedAmount = unallocatedAmount;
        return this;
    }

    public void setUnallocatedAmount(BigDecimal unallocatedAmount) {
        this.unallocatedAmount = unallocatedAmount;
    }

    public BigDecimal getAllocatedRate() {
        return allocatedRate;
    }

    public CPAllocationGeneralExpense allocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
        return this;
    }

    public void setAllocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public CPAllocationGeneralExpense allocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Integer getAllocationMethod() {
        return allocationMethod;
    }

    public CPAllocationGeneralExpense allocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
        return this;
    }

    public void setAllocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public UUID getRefDetailID() {
        return refDetailID;
    }

    public void setRefDetailID(UUID refDetailID) {
        this.refDetailID = refDetailID;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
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
        CPAllocationGeneralExpense cPAllocationGeneralExpense = (CPAllocationGeneralExpense) o;
        if (cPAllocationGeneralExpense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAllocationGeneralExpense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAllocationGeneralExpense{" +
            "id=" + getId() +
            ", cPPeriodID=" + getcPPeriodID() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", expenseItemID=" + getExpenseItemID() +
            ", totalCost=" + getTotalCost() +
            ", unallocatedAmount=" + getUnallocatedAmount() +
            ", allocatedRate=" + getAllocatedRate() +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", allocationMethod=" + getAllocationMethod() +
            "}";
    }
}
