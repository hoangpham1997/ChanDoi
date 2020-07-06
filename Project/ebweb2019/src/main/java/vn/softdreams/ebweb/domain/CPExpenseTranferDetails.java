package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPExpenseTranferDetails.
 */
@Entity
@Table(name = "cpexpensetranferdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CPExpenseTranferDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpexpensetranferid")
    private UUID cPExpenseTranferID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amountoriginal")
    private BigDecimal amountOriginal;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public CPExpenseTranferDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public CPExpenseTranferDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public CPExpenseTranferDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CPExpenseTranferDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public CPExpenseTranferDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getcPExpenseTranferID() {
        return cPExpenseTranferID;
    }

    public void setcPExpenseTranferID(UUID cPExpenseTranferID) {
        this.cPExpenseTranferID = cPExpenseTranferID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
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
        CPExpenseTranferDetails cPExpenseTranferDetails = (CPExpenseTranferDetails) o;
        if (cPExpenseTranferDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPExpenseTranferDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPExpenseTranferDetails{" +
            "id=" + getId() +
            ", cPExpenseTranferID='" + getcPExpenseTranferID() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal='" + getAmountOriginal() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", statisticsCodeID='" + getStatisticsCodeID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
