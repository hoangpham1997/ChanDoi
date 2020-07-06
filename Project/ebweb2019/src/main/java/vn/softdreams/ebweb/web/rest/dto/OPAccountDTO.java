package vn.softdreams.ebweb.web.rest.dto;


import org.apache.commons.beanutils.BeanUtils;
import vn.softdreams.ebweb.domain.OPAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class OPAccountDTO {

    private UUID id;

    private UUID companyId;

    private Integer typeId;

    private LocalDate postedDate;

    private Integer typeLedger;

    private String accountNumber;

    private String accountName;

    private String currencyId;

    private BigDecimal exchangeRate;

    private BigDecimal debitAmount;

    private BigDecimal debitAmountOriginal;

    private BigDecimal creditAmount;

    private BigDecimal creditAmountOriginal;

    private Integer accountGroupKind;

    private UUID accountingObjectId;

    private String accountingObjectName;

    private String accountingObjectCode;

    private UUID bankAccountDetailId;

    private String bankAccount;

    private UUID contractId;

    private String noBookContract;

    private UUID costSetId;

    private String costSetCode;

    private UUID expenseItemId;

    private String expenseItemCode;

    private UUID departmentId;

    private String organizationUnitCode;

    private UUID statisticsCodeId;

    private String statisticsCode;

    private UUID budgetItemId;

    private String budgetItemCode;

    private Integer orderPriority;

    public OPAccountDTO() {
    }

    public OPAccountDTO(OPAccount opAccount) {
        try {
            BeanUtils.copyProperties(this, opAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OPAccountDTO(UUID id, UUID companyId, Integer typeId, LocalDate postedDate,
                        Integer typeLedger, String accountNumber, String accountName, String currencyId,
                        BigDecimal exchangeRate, BigDecimal debitAmount, BigDecimal debitAmountOriginal,
                        BigDecimal creditAmount, BigDecimal creditAmountOriginal, UUID accountingObjectId,
                        String accountingObjectName, String accountingObjectCode, UUID bankAccountDetailId,
                        String bankAccount, UUID contractId, String noBookContract, UUID costSetId,
                        String costSetCode, UUID expenseItemId, String expenseItemCode, UUID departmentId,
                        String organizationUnitCode, UUID statisticsCodeId, String statisticsCode,
                        UUID budgetItemId, String budgetItemCode, Integer orderPriority) {
        this.id = id;
        this.companyId = companyId;
        this.typeId = typeId;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.currencyId = currencyId;
        this.exchangeRate = exchangeRate;
        this.debitAmount = debitAmount;
        this.debitAmountOriginal = debitAmountOriginal;
        this.creditAmount = creditAmount;
        this.creditAmountOriginal = creditAmountOriginal;
        this.accountingObjectId = accountingObjectId;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectCode = accountingObjectCode;
        this.bankAccountDetailId = bankAccountDetailId;
        this.bankAccount = bankAccount;
        this.contractId = contractId;
        this.noBookContract = noBookContract;
        this.costSetId = costSetId;
        this.costSetCode = costSetCode;
        this.expenseItemId = expenseItemId;
        this.expenseItemCode = expenseItemCode;
        this.departmentId = departmentId;
        this.organizationUnitCode = organizationUnitCode;
        this.statisticsCodeId = statisticsCodeId;
        this.statisticsCode = statisticsCode;
        this.budgetItemId = budgetItemId;
        this.budgetItemCode = budgetItemCode;
        this.orderPriority = orderPriority;
    }

    public Integer getAccountGroupKind() {
        return accountGroupKind;
    }

    public void setAccountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount == null ? BigDecimal.ZERO : debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal == null ? BigDecimal.ZERO : debitAmountOriginal;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount == null ? BigDecimal.ZERO : creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal == null ? BigDecimal.ZERO : creditAmountOriginal;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public UUID getBankAccountDetailId() {
        return bankAccountDetailId;
    }

    public void setBankAccountDetailId(UUID bankAccountDetailId) {
        this.bankAccountDetailId = bankAccountDetailId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public String getNoBookContract() {
        return noBookContract;
    }

    public void setNoBookContract(String noBookContract) {
        this.noBookContract = noBookContract;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public UUID getStatisticsCodeId() {
        return statisticsCodeId;
    }

    public void setStatisticsCodeId(UUID statisticsCodeId) {
        this.statisticsCodeId = statisticsCodeId;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }
}
