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
 * A TIAllocationPost.
 */
@Entity
@Table(name = "tiallocationpost")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TIAllocationPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiallocationid")
    private UUID tiAllocationID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "statisticcodeid")
    private UUID statisticCodeID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getDescription() {
        return description;
    }

    public TIAllocationPost description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public TIAllocationPost debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public TIAllocationPost creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TIAllocationPost amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
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

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIAllocationPost orderPriority(Integer orderPriority) {
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
        TIAllocationPost tIAllocationPost = (TIAllocationPost) o;
        if (tIAllocationPost.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAllocationPost.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAllocationPost{" +
            "id=" + getId() +
            ", tiAllocationID='" + getTiAllocationID() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", amount=" + getAmount() +
            ", departmentID='" + getDepartmentID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", statisticCodeID='" + getStatisticCodeID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
