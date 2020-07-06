package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCPaymentDetails.
 */
@Entity
@Table(name = "mcpaymentdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCPaymentDetails implements Serializable {

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
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "amountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal amountOriginal;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFB;

    @Column(name = "cashoutdifferaccountfb", length = 25)
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Column(name = "cashoutdifferaccountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAccountMB;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "bankaccountdetailid")
    private BankAccountDetails bankAccountDetails;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "budgetitemid")
    private BudgetItem budgetItem;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "costsetid")
    private CostSet costSet;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticsCode;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "departmentid")
    private OrganizationUnit organizationUnit;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;

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

    public MCPaymentDetails mCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
        return this;
    }

    public void setmCPaymentID(UUID mCPaymentID) {
        this.mCPaymentID = mCPaymentID;
    }

    public String getDescription() {
        return description;
    }

    public MCPaymentDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MCPaymentDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MCPaymentDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MCPaymentDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MCPaymentDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public UUID getContractID() {
        return contractID;
    }

    public MCPaymentDetails contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public MCPaymentDetails cashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        return this;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public MCPaymentDetails cashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
        return this;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public MCPaymentDetails cashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        return this;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public MCPaymentDetails cashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        return this;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public MCPaymentDetails cashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        return this;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public MCPaymentDetails cashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
        return this;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public MCPaymentDetails cashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        return this;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public BigDecimal getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public MCPaymentDetails cashOutDifferAccountMB(BigDecimal cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        return this;
    }

    public void setCashOutDifferAccountMB(BigDecimal cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public MCPaymentDetails isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public MCPaymentDetails matchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
        return this;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCPaymentDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public BankAccountDetails getBankAccountDetails() {
        return bankAccountDetails;
    }

    public MCPaymentDetails bankAccountDetails(BankAccountDetails bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
        return this;
    }

    public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCPaymentDetails accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public BudgetItem getBudgetItem() {
        return budgetItem;
    }

    public MCPaymentDetails budgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
        return this;
    }

    public void setBudgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
    }

    public CostSet getCostSet() {
        return costSet;
    }

    public MCPaymentDetails costSet(CostSet costSet) {
        this.costSet = costSet;
        return this;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public StatisticsCode getStatisticsCode() {
        return statisticsCode;
    }

    public MCPaymentDetails statisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
        return this;
    }

    public void setStatisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public MCPaymentDetails organizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
        return this;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public ExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public MCPaymentDetails expenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
        return this;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public void setExpenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
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
        MCPaymentDetails mCPaymentDetails = (MCPaymentDetails) o;
        if (mCPaymentDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPaymentDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPaymentDetails{" +
            "id=" + getId() +
            ", mCPaymentID=" + getmCPaymentID() +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", contractID=" + getContractID() +
            ", cashOutExchangeRateFB=" + getCashOutExchangeRateFB() +
            ", cashOutAmountFB=" + getCashOutAmountFB() +
            ", cashOutDifferAmountFB=" + getCashOutDifferAmountFB() +
            ", cashOutDifferAccountFB=" + getCashOutDifferAccountFB() +
            ", cashOutExchangeRateMB=" + getCashOutExchangeRateMB() +
            ", cashOutAmountMB=" + getCashOutAmountMB() +
            ", cashOutDifferAmountMB=" + getCashOutDifferAmountMB() +
            ", cashOutDifferAccountMB=" + getCashOutDifferAccountMB() +
            ", isMatch='" + isIsMatch() + "'" +
            ", matchDate='" + getMatchDate() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
