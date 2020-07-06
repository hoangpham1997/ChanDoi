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
 * A MBDepositDetails.
 */
@Entity
@Table(name = "mbdepositdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBDepositDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "mbdepositid")
    private UUID mBDepositID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

//    @Column(name = "contractid", length = 512)
//    private String contractID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "refvoucherexchangerate", precision = 10, scale = 2)
    private BigDecimal refVoucherExchangeRate;

    @Column(name = "lastexchangerate", precision = 10, scale = 2)
    private BigDecimal lastExchangeRate;

    @Column(name = "differamount", precision = 10, scale = 2)
    private BigDecimal differAmount;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

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

    public UUID getmBDepositID() {
        return mBDepositID;
    }

    public MBDepositDetails mBDepositID(UUID mBDepositID) {
        this.mBDepositID = mBDepositID;
        return this;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public void setmBDepositID(UUID mBDepositID) {
        this.mBDepositID = mBDepositID;
    }

    public String getDescription() {
        return description;
    }

    public MBDepositDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public MBDepositDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public MBDepositDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MBDepositDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public MBDepositDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }
    //    public EMContract getEMContract() {
//        return eMContract;
//    }
//
//    public void setEMContract(EMContract eMContract) {
//        this.eMContract = eMContract;
//    }


    public BigDecimal getRefVoucherExchangeRate() {
        return refVoucherExchangeRate;
    }

    public MBDepositDetails refVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
        return this;
    }

    public void setRefVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
    }

    public BigDecimal getLastExchangeRate() {
        return lastExchangeRate;
    }

    public MBDepositDetails lastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
        return this;
    }

    public void setLastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public MBDepositDetails differAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
        return this;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MBDepositDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
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
        MBDepositDetails mBDepositDetails = (MBDepositDetails) o;
        if (mBDepositDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBDepositDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBDepositDetails{" +
            "id=" + getId() +
            ", mBDepositID='" + getmBDepositID() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
//            ", costSetID='" + getCostSet() + "'" +
//            ", contractID='" + getContractID() + "'" +
//            ", accountingObjectID='" + getAccountingObject() + "'" +
//            ", statisticsCodeID='" + getStatisticsCode() + "'" +
//            ", organizationUnitID='" + getDepartmentID() + "'" +
//            ", expenseItemID='" + getExpenseItem() + "'" +
//            ", budgetItemID='" + getBudgetItem() + "'" +
            ", refVoucherExchangeRate=" + getRefVoucherExchangeRate() +
            ", lastExchangeRate=" + getLastExchangeRate() +
            ", differAmount=" + getDifferAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
