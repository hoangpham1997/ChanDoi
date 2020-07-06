package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "opaccount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OPAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyID")
    private UUID companyId;

    @Column(name = "branchID")
    private UUID branchId;

    @Column(name = "typeID")
    @NotNull
    private Integer typeId;

    @Column(name = "postedDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate postedDate;

    @Column(name = "typeLedger")
    private Integer typeLedger;

    @Column(name = "accountNumber")
    @Size(max = 25)
    private String accountNumber;

    @Column(name = "currencyID")
    @Size(max = 3)
    private String currencyId;

    @Column(name = "exchangeRate")
    @Digits(integer=25, fraction=10)
    private BigDecimal exchangeRate;

    @Column(name = "debitAmount")
    private BigDecimal debitAmount;

    @Column(name = "debitAmountOriginal")
    private BigDecimal debitAmountOriginal;

    @Column(name = "creditAmount")
    private BigDecimal creditAmount;

    @Column(name = "creditAmountOriginal")
    private BigDecimal creditAmountOriginal;

    @Column(name = "accountingObjectID")
    private UUID accountingObjectId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private AccountingObject accountingObject;

    @Column(name = "bankAccountDetailID")
    private UUID bankAccountDetailId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BankAccountDetails bankAccountDetails;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer index;

    @Column(name = "contractID")
    private UUID contractId;

    @Column(name = "costSetID")
    private UUID costSetId;

    @Column(name = "expenseItemID")
    private UUID expenseItemId;

    @Column(name = "departmentID")
    private UUID departmentId;

    @Column(name = "statisticsCodeID")
    private UUID statisticsCodeId;

    @Column(name = "budgetItemID")
    private UUID budgetItemId;

    @Column(name = "orderPriority")
    private Integer orderPriority;

    public OPAccount() {
    }

    public OPAccount(UUID companyId, UUID branchId, Integer typeId, LocalDate postedDate, Integer typeLedger,
                     String accountNumber, String currencyId, BigDecimal exchangeRate, BigDecimal debitAmount,
                     BigDecimal debitAmountOriginal, BigDecimal creditAmount, BigDecimal creditAmountOriginal,
                     UUID accountingObjectId, UUID bankAccountDetailId, UUID contractId, UUID costSetId,
                     UUID expenseItemId, UUID departmentId, UUID statisticsCodeId, UUID budgetItemId, Integer orderPriority) {
        this.companyId = companyId;
        this.branchId = branchId;
        this.typeId = typeId;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.accountNumber = accountNumber;
        this.currencyId = currencyId;
        this.exchangeRate = exchangeRate;
        this.debitAmount = debitAmount;
        this.debitAmountOriginal = debitAmountOriginal;
        this.creditAmount = creditAmount;
        this.creditAmountOriginal = creditAmountOriginal;
        this.accountingObjectId = accountingObjectId;
        this.bankAccountDetailId = bankAccountDetailId;
        this.contractId = contractId;
        this.costSetId = costSetId;
        this.expenseItemId = expenseItemId;
        this.departmentId = departmentId;
        this.statisticsCodeId = statisticsCodeId;
        this.budgetItemId = budgetItemId;
        this.orderPriority = orderPriority;
    }

    public OPAccount(OPAccountDTO opAccountDTO, UUID companyId, LocalDate postedDate) {
        try {
            BeanUtils.copyProperties(this, opAccountDTO);
            this.companyId = companyId;
            if (this.id == null) {
                this.postedDate = postedDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BankAccountDetails getBankAccountDetails() {
        return bankAccountDetails;
    }

    public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
        if (bankAccountDetails != null) {
            this.bankAccountDetails = bankAccountDetails;
            this.bankAccountDetailId = bankAccountDetails.getId();
        }
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        if (accountingObject != null) {
            this.accountingObject = accountingObject;
            this.accountingObjectId = accountingObject.getId();
        }

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount != null ? debitAmount : BigDecimal.ZERO;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal != null ? debitAmountOriginal : BigDecimal.ZERO;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount != null ? creditAmount : BigDecimal.ZERO;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal != null ? creditAmountOriginal : BigDecimal.ZERO;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }

    public UUID getBankAccountDetailId() {
        return bankAccountDetailId;
    }

    public void setBankAccountDetailId(UUID bankAccountDetailId) {
        this.bankAccountDetailId = bankAccountDetailId;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getStatisticsCodeId() {
        return statisticsCodeId;
    }

    public void setStatisticsCodeId(UUID statisticsCodeId) {
        this.statisticsCodeId = statisticsCodeId;
    }

    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
