package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewVoucherNoDetailDTO {
    private UUID id;
    private UUID refID;
    private UUID refParentID;
    private String creditAccount;
    private String debitAccount;
    private UUID bankAccountDetailID;
    private String bankAccount;
    private String bankName;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal vATRate;
    private String vATAccount;
    private String deductionDebitAccount;
    private BigDecimal vATAmount;
    private BigDecimal vATAmountOriginal;
    private BigDecimal discountRate;
    private String discountAccount;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal exportTaxAmount;
    private String exportTaxAmountAccount;
    private String exportTaxAccountCorresponding;
    private String description;
    private UUID accountingObjectID;
    private String accountingObjectCode;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private UUID debitAccountingObjectID;
    private String debitAccountingObjectCode;
    private String debitAccountingObjectName;
    private String debitAccountingObjectAddress;
    private UUID creditAccountingObjectID;
    private String creditAccountingObjectCode;
    private String creditAccountingObjectName;
    private String creditAccountingObjectAddress;
    private UUID employeeID;
    private String employeeCode;
    private String employeeName;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private Integer MaterialGoodsType;
    private UUID repositoryID;
    private String repositoryCode;
    private String repositoryName;
    private UUID unitID;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private UUID mainUnitID;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private UUID departmentID;
    private UUID expenseItemID;
    private UUID budgetItemID;
    private UUID costSetID;
    private UUID contractID;
    private UUID statisticsCodeID;
    private Integer orderPriority;
    private String refTable;
    private LocalDate expiryDate;
    private String lotNo;
    private UUID confrontID;
    private UUID confrontDetailID;
    private String repositoryAccount;
    private String costAccount;
    private String vatDescription;
    private String invoiceNo;
    private BigDecimal oWPrice;
    private BigDecimal oWAmount;
    private UUID ppOrderDetailId;
    private BigDecimal importTaxAmount;
    private BigDecimal importTaxAmountOriginal;
    private String importTaxAccount;
    private BigDecimal specialConsumeTaxAmount;
    private BigDecimal specialConsumeTaxAmountOriginal;
    private String specialConsumeTaxAccount;
    private BigDecimal ppOrderDetailQuantity;
    private Boolean isPromotion;
    private BigDecimal inwardAmount;
    private UUID fromRepositoryID;
    private UUID toRepositoryID;

    public ViewVoucherNoDetailDTO(UUID id, UUID refID, UUID refParentID, String creditAccount, String debitAccount,
                                  UUID bankAccountDetailID, String bankAccount, String bankName, BigDecimal amount,
                                  BigDecimal amountOriginal, BigDecimal vATRate, String vATAccount,
                                  String deductionDebitAccount, BigDecimal vATAmount, BigDecimal vATAmountOriginal,
                                  BigDecimal discountRate, String discountAccount, BigDecimal discountAmount,
                                  BigDecimal discountAmountOriginal, BigDecimal exportTaxAmount,
                                  String exportTaxAmountAccount, String exportTaxAccountCorresponding,
                                  String description, UUID accountingObjectID, String accountingObjectCode,
                                  String accountingObjectName, String accountingObjectAddress,
                                  UUID debitAccountingObjectID, String debitAccountingObjectCode,
                                  String debitAccountingObjectName, String debitAccountingObjectAddress,
                                  UUID creditAccountingObjectID, String creditAccountingObjectCode,
                                  String creditAccountingObjectName, String creditAccountingObjectAddress,
                                  UUID employeeID, String employeeCode, String employeeName, UUID materialGoodsID,
                                  String materialGoodsCode, String materialGoodsName, Integer materialGoodsType, UUID repositoryID,
                                  String repositoryCode, String repositoryName, UUID unitID, BigDecimal quantity,
                                  BigDecimal unitPrice, BigDecimal unitPriceOriginal, UUID mainUnitID,
                                  BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate,
                                  String formula, UUID departmentID, UUID expenseItemID, UUID budgetItemID,
                                  UUID costSetID, UUID contractID, UUID statisticsCodeID, Integer orderPriority,
                                  String refTable, LocalDate expiryDate, String lotNo, UUID confrontID,
                                  UUID confrontDetailID, String repositoryAccount, String costAccount,
                                  String vatDescription,
                                  String invoiceNo, BigDecimal oWPrice, BigDecimal oWAmount,
                                  UUID ppOrderDetailId, BigDecimal importTaxAmount, BigDecimal importTaxAmountOriginal,
                                  String importTaxAccount, BigDecimal specialConsumeTaxAmount,
                                  BigDecimal specialConsumeTaxAmountOriginal, String specialConsumeTaxAccount,
                                  BigDecimal ppOrderDetailQuantity, Boolean isPromotion, BigDecimal inwardAmount,
                                  UUID fromRepositoryID, UUID toRepositoryID) {
        this.id = id;
        this.refID = refID;
        this.refParentID = refParentID;
        this.creditAccount = creditAccount;
        MaterialGoodsType = materialGoodsType;
        this.ppOrderDetailId = ppOrderDetailId;
        this.importTaxAmount = importTaxAmount;
        this.importTaxAmountOriginal = importTaxAmountOriginal;
        this.importTaxAccount = importTaxAccount;
        this.specialConsumeTaxAmount = specialConsumeTaxAmount;
        this.specialConsumeTaxAmountOriginal = specialConsumeTaxAmountOriginal;
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
        this.ppOrderDetailQuantity = ppOrderDetailQuantity;
        this.debitAccount = debitAccount;
        this.bankAccountDetailID = bankAccountDetailID;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.vATRate = vATRate;
        this.vATAccount = vATAccount;
        this.deductionDebitAccount = deductionDebitAccount;
        this.vATAmount = vATAmount;
        this.vATAmountOriginal = vATAmountOriginal;
        this.discountRate = discountRate;
        this.discountAccount = discountAccount;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.exportTaxAmount = exportTaxAmount;
        this.exportTaxAmountAccount = exportTaxAmountAccount;
        this.exportTaxAccountCorresponding = exportTaxAccountCorresponding;
        this.description = description;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.debitAccountingObjectID = debitAccountingObjectID;
        this.debitAccountingObjectCode = debitAccountingObjectCode;
        this.debitAccountingObjectName = debitAccountingObjectName;
        this.debitAccountingObjectAddress = debitAccountingObjectAddress;
        this.creditAccountingObjectID = creditAccountingObjectID;
        this.creditAccountingObjectCode = creditAccountingObjectCode;
        this.creditAccountingObjectName = creditAccountingObjectName;
        this.creditAccountingObjectAddress = creditAccountingObjectAddress;
        this.employeeID = employeeID;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.repositoryName = repositoryName;
        this.unitID = unitID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainUnitID = mainUnitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticsCodeID = statisticsCodeID;
        this.orderPriority = orderPriority;
        this.refTable = refTable;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.confrontID = confrontID;
        this.confrontDetailID = confrontDetailID;
        this.repositoryAccount = repositoryAccount;
        this.costAccount = costAccount;
        this.vatDescription = vatDescription;
        this.invoiceNo = invoiceNo;
        this.oWPrice = oWPrice;
        this.oWAmount = oWAmount;
        this.isPromotion = isPromotion;
        this.inwardAmount = inwardAmount;
        this.fromRepositoryID = fromRepositoryID;
        this.toRepositoryID = toRepositoryID;
    }

    public UUID getFromRepositoryID() {
        return fromRepositoryID;
    }

    public void setFromRepositoryID(UUID fromRepositoryID) {
        this.fromRepositoryID = fromRepositoryID;
    }

    public UUID getToRepositoryID() {
        return toRepositoryID;
    }

    public void setToRepositoryID(UUID toRepositoryID) {
        this.toRepositoryID = toRepositoryID;
    }

    public BigDecimal getInwardAmount() {
        return inwardAmount;
    }

    public void setInwardAmount(BigDecimal inwardAmount) {
        this.inwardAmount = inwardAmount;
    }

    public BigDecimal getSpecialConsumeTaxAmount() {
        return specialConsumeTaxAmount;
    }

    public void setSpecialConsumeTaxAmount(BigDecimal specialConsumeTaxAmount) {
        this.specialConsumeTaxAmount = specialConsumeTaxAmount;
    }

    public BigDecimal getSpecialConsumeTaxAmountOriginal() {
        return specialConsumeTaxAmountOriginal;
    }

    public void setSpecialConsumeTaxAmountOriginal(BigDecimal specialConsumeTaxAmountOriginal) {
        this.specialConsumeTaxAmountOriginal = specialConsumeTaxAmountOriginal;
    }

    public String getSpecialConsumeTaxAccount() {
        return specialConsumeTaxAccount;
    }

    public void setSpecialConsumeTaxAccount(String specialConsumeTaxAccount) {
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
    }

    public BigDecimal getImportTaxAmount() {
        return importTaxAmount;
    }

    public void setImportTaxAmount(BigDecimal importTaxAmount) {
        this.importTaxAmount = importTaxAmount;
    }

    public BigDecimal getImportTaxAmountOriginal() {
        return importTaxAmountOriginal;
    }

    public void setImportTaxAmountOriginal(BigDecimal importTaxAmountOriginal) {
        this.importTaxAmountOriginal = importTaxAmountOriginal;
    }

    public String getImportTaxAccount() {
        return importTaxAccount;
    }

    public void setImportTaxAccount(String importTaxAccount) {
        this.importTaxAccount = importTaxAccount;
    }

    public UUID getPpOrderDetailId() {
        return ppOrderDetailId;
    }

    public void setPpOrderDetailId(UUID ppOrderDetailId) {
        this.ppOrderDetailId = ppOrderDetailId;
    }

    public BigDecimal getPpOrderDetailQuantity() {
        return ppOrderDetailQuantity;
    }

    public void setPpOrderDetailQuantity(BigDecimal ppOrderDetailQuantity) {
        this.ppOrderDetailQuantity = ppOrderDetailQuantity;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public UUID getDebitAccountingObjectID() {
        return debitAccountingObjectID;
    }

    public void setDebitAccountingObjectID(UUID debitAccountingObjectID) {
        this.debitAccountingObjectID = debitAccountingObjectID;
    }

    public String getDebitAccountingObjectCode() {
        return debitAccountingObjectCode;
    }

    public void setDebitAccountingObjectCode(String debitAccountingObjectCode) {
        this.debitAccountingObjectCode = debitAccountingObjectCode;
    }

    public String getDebitAccountingObjectName() {
        return debitAccountingObjectName;
    }

    public void setDebitAccountingObjectName(String debitAccountingObjectName) {
        this.debitAccountingObjectName = debitAccountingObjectName;
    }

    public String getDebitAccountingObjectAddress() {
        return debitAccountingObjectAddress;
    }

    public void setDebitAccountingObjectAddress(String debitAccountingObjectAddress) {
        this.debitAccountingObjectAddress = debitAccountingObjectAddress;
    }

    public UUID getCreditAccountingObjectID() {
        return creditAccountingObjectID;
    }

    public void setCreditAccountingObjectID(UUID creditAccountingObjectID) {
        this.creditAccountingObjectID = creditAccountingObjectID;
    }

    public String getCreditAccountingObjectCode() {
        return creditAccountingObjectCode;
    }

    public void setCreditAccountingObjectCode(String creditAccountingObjectCode) {
        this.creditAccountingObjectCode = creditAccountingObjectCode;
    }

    public String getCreditAccountingObjectName() {
        return creditAccountingObjectName;
    }

    public void setCreditAccountingObjectName(String creditAccountingObjectName) {
        this.creditAccountingObjectName = creditAccountingObjectName;
    }

    public String getCreditAccountingObjectAddress() {
        return creditAccountingObjectAddress;
    }

    public void setCreditAccountingObjectAddress(String creditAccountingObjectAddress) {
        this.creditAccountingObjectAddress = creditAccountingObjectAddress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public UUID getRefParentID() {
        return refParentID;
    }

    public void setRefParentID(UUID refParentID) {
        this.refParentID = refParentID;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAmount() {
        return amount;
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

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String mormula) {
        this.formula = mormula;
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

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public BigDecimal getExportTaxAmount() {
        return exportTaxAmount;
    }

    public void setExportTaxAmount(BigDecimal exportTaxAmount) {
        this.exportTaxAmount = exportTaxAmount;
    }

    public String getExportTaxAmountAccount() {
        return exportTaxAmountAccount;
    }

    public void setExportTaxAmountAccount(String exportTaxAmountAccount) {
        this.exportTaxAmountAccount = exportTaxAmountAccount;
    }

    public String getExportTaxAccountCorresponding() {
        return exportTaxAccountCorresponding;
    }

    public void setExportTaxAccountCorresponding(String exportTaxAccountCorresponding) {
        this.exportTaxAccountCorresponding = exportTaxAccountCorresponding;
    }

    public String getRepositoryAccount() {
        return repositoryAccount;
    }

    public void setRepositoryAccount(String repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public BigDecimal getoWPrice() {
        return oWPrice;
    }

    public void setoWPrice(BigDecimal oWPrice) {
        this.oWPrice = oWPrice;
    }

    public BigDecimal getoWAmount() {
        return oWAmount;
    }

    public void setoWAmount(BigDecimal oWAmount) {
        this.oWAmount = oWAmount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public UUID getConfrontID() {
        return confrontID;
    }

    public void setConfrontID(UUID confrontID) {
        this.confrontID = confrontID;
    }

    public UUID getConfrontDetailID() {
        return confrontDetailID;
    }

    public void setConfrontDetailID(UUID confrontDetailID) {
        this.confrontDetailID = confrontDetailID;
    }

    public Boolean isIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public Integer getMaterialGoodsType() {
        return MaterialGoodsType;
    }

    public void setMaterialGoodsType(Integer materialGoodsType) {
        MaterialGoodsType = materialGoodsType;
    }
}
