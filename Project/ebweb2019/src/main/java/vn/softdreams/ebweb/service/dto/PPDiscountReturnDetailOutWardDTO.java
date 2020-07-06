package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPDiscountReturnDetailOutWardDTO {
    private UUID id;
    private UUID ppDiscountReturnID;
    private UUID ppInvoiceID;
    private UUID ppInvoiceDetailID;
    private UUID repositoryID;
    private UUID materialGoodsID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private UUID unitID;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private UUID mainUnitID;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private LocalDate expiryDate;
    private String lotNo;
    private Boolean isPromotion;
    private UUID goodsServicePurchaseID;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private String vatAccount;
    private UUID accountingObjectID;
    private UUID saBillID;
    private UUID saBillDetailID;
    private UUID costSetID;
    private UUID contractID;
    private UUID statisticsCodeID;
    private UUID departmentID;
    private UUID expenseItemID;
    private UUID budgetItemID;
    private Boolean isMatch;
    private LocalDate matchDate;
    private Integer orderPriority;
    private String deductionDebitAccount;
    private String vatDescription;
    private String noBookPPDiscountReturn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPpDiscountReturnID() {
        return ppDiscountReturnID;
    }

    public void setPpDiscountReturnID(UUID ppDiscountReturnID) {
        this.ppDiscountReturnID = ppDiscountReturnID;
    }

    public UUID getPpInvoiceID() {
        return ppInvoiceID;
    }

    public void setPpInvoiceID(UUID ppInvoiceID) {
        this.ppInvoiceID = ppInvoiceID;
    }

    public UUID getPpInvoiceDetailID() {
        return ppInvoiceDetailID;
    }

    public void setPpInvoiceDetailID(UUID ppInvoiceDetailID) {
        this.ppInvoiceDetailID = ppInvoiceDetailID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public void setFormula(String formula) {
        this.formula = formula;
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

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
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

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getSaBillID() {
        return saBillID;
    }

    public void setSaBillID(UUID saBillID) {
        this.saBillID = saBillID;
    }

    public UUID getSaBillDetailID() {
        return saBillDetailID;
    }

    public void setSaBillDetailID(UUID saBillDetailID) {
        this.saBillDetailID = saBillDetailID;
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

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public String getNoBookPPDiscountReturn() {
        return noBookPPDiscountReturn;
    }

    public void setNoBookPPDiscountReturn(String noBookPPDiscountReturn) {
        this.noBookPPDiscountReturn = noBookPPDiscountReturn;
    }

    public PPDiscountReturnDetailOutWardDTO(UUID id, UUID ppDiscountReturnID, UUID ppInvoiceID, UUID ppInvoiceDetailID, UUID repositoryID, UUID materialGoodsID, String description, String debitAccount, String creditAccount, UUID unitID, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal amountOriginal, UUID mainUnitID, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, LocalDate expiryDate, String lotNo, Boolean isPromotion, UUID goodsServicePurchaseID, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, String vatAccount, UUID accountingObjectID, UUID saBillID, UUID saBillDetailID, UUID costSetID, UUID contractID, UUID statisticsCodeID, UUID departmentID, UUID expenseItemID, UUID budgetItemID, Boolean isMatch, LocalDate matchDate, Integer orderPriority, String deductionDebitAccount, String vatDescription, String noBookPPDiscountReturn) {
        this.id = id;
        this.ppDiscountReturnID = ppDiscountReturnID;
        this.ppInvoiceID = ppInvoiceID;
        this.ppInvoiceDetailID = ppInvoiceDetailID;
        this.repositoryID = repositoryID;
        this.materialGoodsID = materialGoodsID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unitID = unitID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.mainUnitID = mainUnitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.isPromotion = isPromotion;
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.vatAccount = vatAccount;
        this.accountingObjectID = accountingObjectID;
        this.saBillID = saBillID;
        this.saBillDetailID = saBillDetailID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticsCodeID = statisticsCodeID;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.budgetItemID = budgetItemID;
        this.isMatch = isMatch;
        this.matchDate = matchDate;
        this.orderPriority = orderPriority;
        this.deductionDebitAccount = deductionDebitAccount;
        this.vatDescription = vatDescription;
        this.noBookPPDiscountReturn = noBookPPDiscountReturn;
    }

    public PPDiscountReturnDetailOutWardDTO() {
    }
}
