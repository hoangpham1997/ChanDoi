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
 * A MBInternalTransferDetails.
 */
@Entity
@Table(name = "mbinternaltransferdetails")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBInternalTransferDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "frombankaccountdetailid")
    private String fromBankAccountDetailID;

    @Column(name = "tobankaccountdetailid")
    private String toBankAccountDetailID;

    @Column(name = "frombranchid")
    private String fromBranchID;

    @Column(name = "tobranchid")
    private String toBranchID;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @Column(name = "employeeid")
    private String employeeID;

    @Column(name = "budgetitemid")
    private String budgetItemID;

    @Column(name = "costsetid")
    private String costSetID;

    @Column(name = "contractid")
    private String contractID;

    @Column(name = "statisticscodeid")
    private String statisticsCodeID;

    @Column(name = "organizationunitid")
    private String organizationUnitID;

    @Column(name = "expenseitemid")
    private String expenseItemID;

    @Column(name = "accountingobjectid")
    private String accountingObjectID;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFB;

    @Column(name = "cashoutdifferaccountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Column(name = "cashoutdifferaccountmb")
    private String cashOutDifferAccountMB;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @ManyToOne(optional = false)    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name = "mbinternaltransferID")
    private MBInternalTransfer mBInternalTransfer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MBInternalTransferDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MBInternalTransferDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getFromBankAccountDetailID() {
        return fromBankAccountDetailID;
    }

    public MBInternalTransferDetails fromBankAccountDetailID(String fromBankAccountDetailID) {
        this.fromBankAccountDetailID = fromBankAccountDetailID;
        return this;
    }

    public void setFromBankAccountDetailID(String fromBankAccountDetailID) {
        this.fromBankAccountDetailID = fromBankAccountDetailID;
    }

    public String getToBankAccountDetailID() {
        return toBankAccountDetailID;
    }

    public MBInternalTransferDetails toBankAccountDetailID(String toBankAccountDetailID) {
        this.toBankAccountDetailID = toBankAccountDetailID;
        return this;
    }

    public void setToBankAccountDetailID(String toBankAccountDetailID) {
        this.toBankAccountDetailID = toBankAccountDetailID;
    }

    public String getFromBranchID() {
        return fromBranchID;
    }

    public MBInternalTransferDetails fromBranchID(String fromBranchID) {
        this.fromBranchID = fromBranchID;
        return this;
    }

    public void setFromBranchID(String fromBranchID) {
        this.fromBranchID = fromBranchID;
    }

    public String getToBranchID() {
        return toBranchID;
    }

    public MBInternalTransferDetails toBranchID(String toBranchID) {
        this.toBranchID = toBranchID;
        return this;
    }

    public void setToBranchID(String toBranchID) {
        this.toBranchID = toBranchID;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public MBInternalTransferDetails currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public MBInternalTransferDetails exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MBInternalTransferDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MBInternalTransferDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public MBInternalTransferDetails employeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getBudgetItemID() {
        return budgetItemID;
    }

    public MBInternalTransferDetails budgetItemID(String budgetItemID) {
        this.budgetItemID = budgetItemID;
        return this;
    }

    public void setBudgetItemID(String budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public String getCostSetID() {
        return costSetID;
    }

    public MBInternalTransferDetails costSetID(String costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(String costSetID) {
        this.costSetID = costSetID;
    }

    public String getContractID() {
        return contractID;
    }

    public MBInternalTransferDetails contractID(String contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public MBInternalTransferDetails statisticsCodeID(String statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
        return this;
    }

    public void setStatisticsCodeID(String statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public String getOrganizationUnitID() {
        return organizationUnitID;
    }

    public MBInternalTransferDetails organizationUnitID(String organizationUnitID) {
        this.organizationUnitID = organizationUnitID;
        return this;
    }

    public void setOrganizationUnitID(String organizationUnitID) {
        this.organizationUnitID = organizationUnitID;
    }

    public String getExpenseItemID() {
        return expenseItemID;
    }

    public MBInternalTransferDetails expenseItemID(String expenseItemID) {
        this.expenseItemID = expenseItemID;
        return this;
    }

    public void setExpenseItemID(String expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getAccountingObjectID() {
        return accountingObjectID;
    }

    public MBInternalTransferDetails accountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public MBInternalTransferDetails cashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        return this;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public MBInternalTransferDetails cashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
        return this;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public MBInternalTransferDetails cashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        return this;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public BigDecimal getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public MBInternalTransferDetails cashOutDifferAccountFB(BigDecimal cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        return this;
    }

    public void setCashOutDifferAccountFB(BigDecimal cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public MBInternalTransferDetails cashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        return this;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public MBInternalTransferDetails cashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
        return this;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public MBInternalTransferDetails cashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        return this;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public MBInternalTransferDetails cashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        return this;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public MBInternalTransferDetails isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public MBInternalTransferDetails matchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
        return this;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MBInternalTransferDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public MBInternalTransfer getMBInternalTransfer() {
        return mBInternalTransfer;
    }

    public MBInternalTransferDetails mBInternalTransfer(MBInternalTransfer mBInternalTransfer) {
        this.mBInternalTransfer = mBInternalTransfer;
        return this;
    }

    public void setMBInternalTransfer(MBInternalTransfer mBInternalTransfer) {
        this.mBInternalTransfer = mBInternalTransfer;
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
        MBInternalTransferDetails mBInternalTransferDetails = (MBInternalTransferDetails) o;
        if (mBInternalTransferDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBInternalTransferDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBInternalTransferDetails{" +
            "id=" + getId() +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", fromBankAccountDetailID='" + getFromBankAccountDetailID() + "'" +
            ", toBankAccountDetailID='" + getToBankAccountDetailID() + "'" +
            ", fromBranchID='" + getFromBranchID() + "'" +
            ", toBranchID='" + getToBranchID() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", employeeID='" + getEmployeeID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", contractID='" + getContractID() + "'" +
            ", statisticsCodeID='" + getStatisticsCodeID() + "'" +
            ", organizationUnitID='" + getOrganizationUnitID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", cashOutExchangeRateFB=" + getCashOutExchangeRateFB() +
            ", cashOutAmountFB=" + getCashOutAmountFB() +
            ", cashOutDifferAmountFB=" + getCashOutDifferAmountFB() +
            ", cashOutDifferAccountFB=" + getCashOutDifferAccountFB() +
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
