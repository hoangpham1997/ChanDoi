package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCPaymentDetailInsurance.
 */
@Entity
@Table(name = "mcpaymentdetailinsurance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCPaymentDetailInsurance implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcpaymentid")
    private UUID mCPaymentID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @NotNull
    @Column(name = "accumamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal accumAmount;

    @NotNull
    @Column(name = "payamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal payAmount;

    @NotNull
    @Column(name = "remainningamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal remainningAmount;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "budgetitemid")
    private BudgetItem budgetItem;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "costsetid")
    private CostSet costSet;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "departmentid")
    private OrganizationUnit organizationUnit;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticsCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getmCPaymentID() {
        return mCPaymentID;
    }

    public MCPaymentDetailInsurance mCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
        return this;
    }

    public void setmCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
    }

    public String getDescription() {
        return description;
    }

    public MCPaymentDetailInsurance description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MCPaymentDetailInsurance debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MCPaymentDetailInsurance creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAccumAmount() {
        return accumAmount;
    }

    public MCPaymentDetailInsurance accumAmount(BigDecimal accumAmount) {
        this.accumAmount = accumAmount;
        return this;
    }

    public void setAccumAmount(BigDecimal accumAmount) {
        this.accumAmount = accumAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public MCPaymentDetailInsurance payAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
        return this;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getRemainningAmount() {
        return remainningAmount;
    }

    public MCPaymentDetailInsurance remainningAmount(BigDecimal remainningAmount) {
        this.remainningAmount = remainningAmount;
        return this;
    }

    public void setRemainningAmount(BigDecimal remainningAmount) {
        this.remainningAmount = remainningAmount;
    }

    public UUID getContractID() {
        return contractID;
    }

    public MCPaymentDetailInsurance contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCPaymentDetailInsurance orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public BudgetItem getBudgetItem() {
        return budgetItem;
    }

    public MCPaymentDetailInsurance budgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
        return this;
    }

    public void setBudgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
    }

    public CostSet getCostSet() {
        return costSet;
    }

    public MCPaymentDetailInsurance costSet(CostSet costSet) {
        this.costSet = costSet;
        return this;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCPaymentDetailInsurance accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public MCPaymentDetailInsurance organizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
        return this;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public ExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public MCPaymentDetailInsurance expenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
        return this;
    }

    public void setExpenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    public StatisticsCode getStatisticsCode() {
        return statisticsCode;
    }

    public MCPaymentDetailInsurance statisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
        return this;
    }

    public void setStatisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
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
        MCPaymentDetailInsurance mCPaymentDetailInsurance = (MCPaymentDetailInsurance) o;
        if (mCPaymentDetailInsurance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPaymentDetailInsurance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPaymentDetailInsurance{" +
            "id=" + getId() +
            ", mCPaymentID=" + getmCPaymentID() +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", accumAmount=" + getAccumAmount() +
            ", payAmount=" + getPayAmount() +
            ", remainningAmount=" + getRemainningAmount() +
            ", contractID=" + getContractID() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
