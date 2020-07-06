package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPDiscountReturnDetailConvertDTO {
    private String pPDiscountReturnID;
    private String pPInvoiceID;
    private String pPInvoiceDetailID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private String expiryDate;
    private String lotNo;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private BigDecimal vatAccount;
    private Boolean isMatch;
    private String matchDate;
    private Integer orderPriority;
    private String vatDescription;
    private String deductionDebitAccount;
    private Boolean isPromotion;
    private UUID repositoryID;
    private String repositoryCode;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private UUID unitID;
    private String UnitName;
    private UUID mainUnitID;
    private String mainUnitName;
    private UUID goodsServicePurchaseID;
    private String goodsServicePurchaseCode;
    private UUID accountingObjectID;
    private String accountingObjectCode;
    private UUID costSetID;
    private String costSetCode;
    private String contractNoMBook;
    private String contractNoFBook;
    private UUID statisticsCodeID;
    private String statisticsCode;
    private UUID departmentID;
    private String departmentCode;
    private String expenseItemID;
    private String expenseItemCode;
    private UUID budgetItemID;
    private String budgetItemCode;
    private String ppInVoiceNoBook;
    private String ppInVoiceDate;

    public PPDiscountReturnDetailConvertDTO(String pPDiscountReturnID, String pPInvoiceID, String pPInvoiceDetailID, String description, String debitAccount, String creditAccount, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal amountOriginal, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, String expiryDate, String lotNo, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, BigDecimal vatAccount, Boolean isMatch, String matchDate, Integer orderPriority, String vatDescription, String deductionDebitAccount, Boolean isPromotion, UUID repositoryID, String repositoryCode, UUID materialGoodsID, String materialGoodsCode, UUID unitID, String unitName, UUID mainUnitID, String mainUnitName, UUID goodsServicePurchaseID, String goodsServicePurchaseCode, UUID accountingObjectID, String accountingObjectCode, UUID costSetID, String costSetCode, String contractNoMBook, String contractNoFBook, UUID statisticsCodeID, String statisticsCode, UUID departmentID, String departmentCode, String expenseItemID, String expenseItemCode, UUID budgetItemID, String budgetItemCode, String ppInVoiceNoBook, String ppInVoiceDate) {
        this.pPDiscountReturnID = pPDiscountReturnID;
        this.pPInvoiceID = pPInvoiceID;
        this.pPInvoiceDetailID = pPInvoiceDetailID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.vatAccount = vatAccount;
        this.isMatch = isMatch;
        this.matchDate = matchDate;
        this.orderPriority = orderPriority;
        this.vatDescription = vatDescription;
        this.deductionDebitAccount = deductionDebitAccount;
        this.isPromotion = isPromotion;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.unitID = unitID;
        UnitName = unitName;
        this.mainUnitID = mainUnitID;
        this.mainUnitName = mainUnitName;
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.contractNoMBook = contractNoMBook;
        this.contractNoFBook = contractNoFBook;
        this.statisticsCodeID = statisticsCodeID;
        this.statisticsCode = statisticsCode;
        this.departmentID = departmentID;
        this.departmentCode = departmentCode;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.budgetItemID = budgetItemID;
        this.budgetItemCode = budgetItemCode;
        this.ppInVoiceNoBook = ppInVoiceNoBook;
        this.ppInVoiceDate = ppInVoiceDate;
    }

    public String getpPDiscountReturnID() {
        return pPDiscountReturnID;
    }

    public void setpPDiscountReturnID(String pPDiscountReturnID) {
        this.pPDiscountReturnID = pPDiscountReturnID;
    }

    public String getpPInvoiceID() {
        return pPInvoiceID;
    }

    public void setpPInvoiceID(String pPInvoiceID) {
        this.pPInvoiceID = pPInvoiceID;
    }

    public String getpPInvoiceDetailID() {
        return pPInvoiceDetailID;
    }

    public void setpPInvoiceDetailID(String pPInvoiceDetailID) {
        this.pPInvoiceDetailID = pPInvoiceDetailID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public BigDecimal getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(BigDecimal vatAccount) {
        this.vatAccount = vatAccount;
    }

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
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

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public String getGoodsServicePurchaseCode() {
        return goodsServicePurchaseCode;
    }

    public void setGoodsServicePurchaseCode(String goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
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

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getContractNoMBook() {
        return contractNoMBook;
    }

    public void setContractNoMBook(String contractNoMBook) {
        this.contractNoMBook = contractNoMBook;
    }

    public String getContractNoFBook() {
        return contractNoFBook;
    }

    public void setContractNoFBook(String contractNoFBook) {
        this.contractNoFBook = contractNoFBook;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(String expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getPpInVoiceNoBook() {
        return ppInVoiceNoBook;
    }

    public void setPpInVoiceNoBook(String ppInVoiceNoBook) {
        this.ppInVoiceNoBook = ppInVoiceNoBook;
    }

    public String getPpInVoiceDate() {
        return ppInVoiceDate;
    }

    public void setPpInVoiceDate(String ppInVoiceDate) {
        this.ppInVoiceDate = ppInVoiceDate;
    }
}
