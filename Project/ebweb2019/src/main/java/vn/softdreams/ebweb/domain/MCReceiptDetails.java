package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.*;
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
 * A MCReceiptDetails.
 */
@Entity
@Table(name = "mcreceiptdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCReceiptDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mcreceiptid")
    private UUID mCReceiptID;

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

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountfb", length = 25)
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountmb", length = 25)
    private String cashOutDifferAccountMB;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;


    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "bankaccountdetailid")
    private BankAccountDetails bankAccountDetails;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticsCode;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "departmentid")
    private OrganizationUnit organizationUnit;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "costsetid")
    private CostSet costSet;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "budgetitemid")
    private BudgetItem budgetItem;

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

    public UUID getmCReceiptID() {
        return mCReceiptID;
    }

    public void setmCReceiptID(UUID mCReceiptID) {
        this.mCReceiptID = mCReceiptID;
    }

    public String getDescription() {
        return description;
    }

    public MCReceiptDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MCReceiptDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MCReceiptDetails creditAccount(String creaditAccount) {
        this.creditAccount = creaditAccount;
        return this;
    }

    public void setCreaditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MCReceiptDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MCReceiptDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public UUID getContractID() {
        return contractID;
    }

    public MCReceiptDetails contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public MCReceiptDetails cashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        return this;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public MCReceiptDetails cashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
        return this;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public MCReceiptDetails cashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        return this;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public MCReceiptDetails cashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        return this;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public MCReceiptDetails cashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        return this;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public MCReceiptDetails cashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
        return this;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public MCReceiptDetails cashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        return this;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public MCReceiptDetails cashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        return this;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public MCReceiptDetails isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public MCReceiptDetails matchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
        return this;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCReceiptDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

//    public MCReceipt getMCReceipt() {
//        return mCReceipt;
//    }
//
//    public MCReceiptDetails mCReceipt(MCReceipt mCReceipt) {
//        this.mCReceipt = mCReceipt;
//        return this;
//    }
//
//    public void setMCReceipt(MCReceipt mCReceipt) {
//        this.mCReceipt = mCReceipt;
//    }

    public BankAccountDetails getBankAccountDetails() {
        return bankAccountDetails;
    }

    public MCReceiptDetails bankAccountDetails(BankAccountDetails bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
        return this;
    }

    public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCReceiptDetails accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public StatisticsCode getStatisticsCode() {
        return statisticsCode;
    }

    public MCReceiptDetails statisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
        return this;
    }

    public void setStatisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public MCReceiptDetails organizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
        return this;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public ExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public MCReceiptDetails expenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
        return this;
    }

    public void setExpenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    public CostSet getCostSet() {
        return costSet;
    }

    public MCReceiptDetails costSet(CostSet costSet) {
        this.costSet = costSet;
        return this;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public BudgetItem getBudgetItem() {
        return budgetItem;
    }

    public MCReceiptDetails budgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
        return this;
    }

    public void setBudgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MCReceiptDetails mCReceiptDetails = (MCReceiptDetails) o;
        if (mCReceiptDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCReceiptDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCReceiptDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creaditAccount='" + getCreditAccount() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", contractID=" + getContractID() +
            ", cashOutExchangeRateFB=" + getCashOutExchangeRateFB() +
            ", cashOutAmountFB=" + getCashOutAmountFB() +
            ", cashOutDifferAmountFB=" + getCashOutDifferAmountFB() +
            ", cashOutDifferAccountFB='" + getCashOutDifferAccountFB() + "'" +
            ", cashOutExchangeRateMB=" + getCashOutExchangeRateMB() +
            ", cashOutAmountMB=" + getCashOutAmountMB() +
            ", cashOutDifferAmountMB=" + getCashOutDifferAmountMB() +
            ", cashOutDifferAccountMB='" + getCashOutDifferAccountMB() + "'" +
            ", isMatch='" + isIsMatch() + "'" +
            ", matchDate='" + getMatchDate() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
