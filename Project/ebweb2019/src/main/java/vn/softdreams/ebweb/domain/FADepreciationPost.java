package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FADepreciationPost.
 */
@Entity
@Table(name = "fadepreciationpost")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FADepreciationPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "fadepreciationid")
    private UUID faDepreciationID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
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

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
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
        FADepreciationPost faDecrementDetailsPost = (FADepreciationPost) o;
        if (faDecrementDetailsPost.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faDecrementDetailsPost.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
