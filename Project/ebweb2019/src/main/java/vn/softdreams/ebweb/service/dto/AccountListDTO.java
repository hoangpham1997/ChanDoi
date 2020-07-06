package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountListDTO {
    private UUID id;

    private UUID companyID;

    private UUID branchId;

    private Integer accountingType;

    private String accountNumber;

    private String parentAccountNumber;

    private String accountName;

    private String accountNameGlobal;

    private String description;

    private UUID parentAccountID;

    private Boolean isParentNode;

    private Integer grade;

    private Integer accountGroupKind;

    private String detailType;

    private Boolean isActive;

    private Integer detailByAccountObject;

    private Boolean isForeignCurrency;

    private OPAccountDTO opAccountDTO;

    private BigDecimal amountOriginal;

    private BigDecimal creditAmountOriginal;

    private BigDecimal debitAmountOriginal;

    private List<OPAccountDTO> opAccountDTOList = new ArrayList<>();

    private List<AccountListDTO> children;

    public List<AccountListDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AccountListDTO> children) {
        this.children = children;
    }

    public void addOPAccountDTO(OPAccountDTO opAccountDTO) {
        opAccountDTOList.add(opAccountDTO);
    }

    public AccountListDTO(UUID id, String accountNumber, String accountName, String accountNameGlobal, String detailType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountNameGlobal = accountNameGlobal;
        this.detailType = detailType;
    }

    public AccountListDTO(UUID id, String accountNumber, Integer accountGroupKind , String detailType,
                          String parentAccountNumber, Boolean isForeignCurrency) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountGroupKind = accountGroupKind;
        this.detailType = detailType;
        this.parentAccountNumber = parentAccountNumber;
        this.isForeignCurrency = isForeignCurrency;
    }

    public AccountListDTO(UUID id, UUID companyID, UUID branchId, Integer accountingType, String accountNumber,
                          String accountName, String accountNameGlobal, String description, UUID parentAccountID,
                          Boolean isParentNode, Integer grade, Integer accountGroupKind,
                          String detailType, Boolean isActive, Integer detailByAccountObject, Boolean isForeignCurrency,
                          UUID idOPAccount, Integer typeId, LocalDate postedDate,
                          Integer typeLedger, String currencyId,
                          BigDecimal exchangeRate, BigDecimal debitAmount, BigDecimal debitAmountOriginal,
                          BigDecimal creditAmount, BigDecimal creditAmountOriginal, UUID accountingObjectId,
                          String accountingObjectName, String accountingObjectCode, UUID bankAccountDetailId,
                          String bankAccount, UUID contractId, String noBookContract, UUID costSetId,
                          String costSetCode, UUID expenseItemId, String expenseItemCode, UUID departmentId,
                          String organizationUnitCode, UUID statisticsCodeId, String statisticsCode,
                          UUID budgetItemId, String budgetItemCode, Integer orderPriorityOPA, BigDecimal amountOriginal) {
        this.id = id;
        this.companyID = companyID;
        this.branchId = branchId;
        this.accountingType = accountingType;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountNameGlobal = accountNameGlobal;
        this.description = description;
        this.parentAccountID = parentAccountID;
        this.isParentNode = isParentNode;
        this.grade = grade;
        this.accountGroupKind = accountGroupKind;
        this.detailType = detailType;
        this.isActive = isActive;
        this.detailByAccountObject = detailByAccountObject;
        this.isForeignCurrency = isForeignCurrency;
        if (idOPAccount != null) {
            this.opAccountDTO = new OPAccountDTO(idOPAccount,companyID, typeId, postedDate,
                    typeLedger, accountNumber, accountName, currencyId,
                    exchangeRate, debitAmount, debitAmountOriginal,
                    creditAmount, creditAmountOriginal, accountingObjectId,
                    accountingObjectName, accountingObjectCode, bankAccountDetailId,
                    bankAccount, contractId, noBookContract, costSetId,
                    costSetCode, expenseItemId, expenseItemCode, departmentId,
                    organizationUnitCode, statisticsCodeId, statisticsCode,
                    budgetItemId, budgetItemCode, orderPriorityOPA);
        }
        this.amountOriginal = amountOriginal;
    }

    public String getParentAccountNumber() {
        return parentAccountNumber;
    }

    public void setParentAccountNumber(String parentAccountNumber) {
        this.parentAccountNumber = parentAccountNumber;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal == null ? BigDecimal.ZERO : amountOriginal;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public List<OPAccountDTO> getOpAccountDTOList() {
        return opAccountDTOList;
    }

    public void setOpAccountDTOList(List<OPAccountDTO> opAccountDTOList) {
        this.opAccountDTOList = opAccountDTOList;
    }

    public AccountListDTO(UUID id, UUID companyID, UUID branchId, Integer accountingType, String accountNumber,
                          String accountName, String accountNameGlobal, String description, UUID parentAccountID,
                          Boolean isParentNode, Integer grade, Integer accountGroupKind,
                          String detailType, Boolean isActive, Integer detailByAccountObject, Boolean isForeignCurrency) {
        this.id = id;
        this.companyID = companyID;
        this.branchId = branchId;
        this.accountingType = accountingType;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountNameGlobal = accountNameGlobal;
        this.description = description;
        this.parentAccountID = parentAccountID;
        this.isParentNode = isParentNode;
        this.grade = grade;
        this.accountGroupKind = accountGroupKind;
        this.detailType = detailType;
        this.isActive = isActive;
        this.detailByAccountObject = detailByAccountObject;
        this.isForeignCurrency = isForeignCurrency;
    }

    public OPAccountDTO getOpAccountDTO() {
        return opAccountDTO;
    }

    public void setOpAccountDTO(OPAccountDTO opAccountDTO) {
        this.opAccountDTO = opAccountDTO;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameGlobal() {
        return accountNameGlobal;
    }

    public void setAccountNameGlobal(String accountNameGlobal) {
        this.accountNameGlobal = accountNameGlobal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentAccountID() {
        return parentAccountID;
    }

    public void setParentAccountID(UUID parentAccountID) {
        this.parentAccountID = parentAccountID;
    }

    public Boolean getParentNode() {
        return isParentNode == null ? false : isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getAccountGroupKind() {
        return accountGroupKind;
    }

    public void setAccountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getDetailByAccountObject() {
        return detailByAccountObject;
    }

    public void setDetailByAccountObject(Integer detailByAccountObject) {
        this.detailByAccountObject = detailByAccountObject;
    }

    public Boolean getIsForeignCurrency() {
        return isForeignCurrency;
    }

    public void setIsForeignCurrency(Boolean foreignCurrency) {
        isForeignCurrency = foreignCurrency;
    }
}
