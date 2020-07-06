package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A MBTellerPaperDetails.
 */
@Entity
@Table(name = "mbtellerpaperdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBTellerPaperDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String amountOriginalString;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String amountString;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFb;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFb;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFb;

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountfb", length = 25)
    private String cashOutDifferAccountFb;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMb;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMb;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMb;

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountmb", length = 25)
    private String cashOutDifferAccountMb;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID eMContractID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "mbtellerpaperid")
    private UUID mBTellerPaperId;

    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;

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
    //    @ManyToOne(optional = false)    @NotNull
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "mbtellerpaperid")
//    private MBTellerPaper mbtellerPaper;

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

    public MBTellerPaperDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MBTellerPaperDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


//    public MBTellerPaper getMbtellerPaper() {
//        return mbtellerPaper;
//    }
//
//    public void setMbtellerPaper(MBTellerPaper mbtellerPaper) {
//        this.mbtellerPaper = mbtellerPaper;
//    }
//    public MBTellerPaperDetails mBTellerPaper(MBTellerPaper mBTellerPaper) {
//        this.mbtellerPaper = mBTellerPaper;
//        return this;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MBTellerPaperDetails mBTellerPaperDetails = (MBTellerPaperDetails) o;
        if (mBTellerPaperDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBTellerPaperDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBTellerPaperDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
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

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getCashOutExchangeRateFb() {
        return cashOutExchangeRateFb;
    }

    public void setCashOutExchangeRateFb(BigDecimal cashOutExchangeRateFb) {
        this.cashOutExchangeRateFb = cashOutExchangeRateFb;
    }

    public BigDecimal getCashOutAmountFb() {
        return cashOutAmountFb;
    }

    public void setCashOutAmountFb(BigDecimal cashOutAmountFb) {
        this.cashOutAmountFb = cashOutAmountFb;
    }

    public BigDecimal getCashOutDifferAmountFb() {
        return cashOutDifferAmountFb;
    }

    public void setCashOutDifferAmountFb(BigDecimal cashOutDifferAmountFb) {
        this.cashOutDifferAmountFb = cashOutDifferAmountFb;
    }

    public String getCashOutDifferAccountFb() {
        return cashOutDifferAccountFb;
    }

    public void setCashOutDifferAccountFb(String cashOutDifferAccountFb) {
        this.cashOutDifferAccountFb = cashOutDifferAccountFb;
    }

    public BigDecimal getCashOutExchangeRateMb() {
        return cashOutExchangeRateMb;
    }

    public void setCashOutExchangeRateMb(BigDecimal cashOutExchangeRateMb) {
        this.cashOutExchangeRateMb = cashOutExchangeRateMb;
    }

    public BigDecimal getCashOutAmountMb() {
        return cashOutAmountMb;
    }

    public void setCashOutAmountMb(BigDecimal cashOutAmountMb) {
        this.cashOutAmountMb = cashOutAmountMb;
    }

    public BigDecimal getCashOutDifferAmountMb() {
        return cashOutDifferAmountMb;
    }

    public void setCashOutDifferAmountMb(BigDecimal cashOutDifferAmountMb) {
        this.cashOutDifferAmountMb = cashOutDifferAmountMb;
    }

    public String getCashOutDifferAccountMb() {
        return cashOutDifferAccountMb;
    }

    public void setCashOutDifferAccountMb(String cashOutDifferAccountMb) {
        this.cashOutDifferAccountMb = cashOutDifferAccountMb;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getmBTellerPaperId() {
        return mBTellerPaperId;
    }

    public void setmBTellerPaperId(UUID mBTellerPaperId) {
        this.mBTellerPaperId = mBTellerPaperId;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
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

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID geteMContractID() {
        return eMContractID;
    }

    public void seteMContractID(UUID eMContractID) {
        this.eMContractID = eMContractID;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }
}
