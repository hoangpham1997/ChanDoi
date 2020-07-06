package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PPInvoiceConvertDTO {
    private UUID ppInvoiceDetailID;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeGroupID;
    private String noMBook;
//    private String noFBook;
    private String materialGoodsCode;
    private Integer typeLedger;
    private UUID refID2;
    private String reason;
    private UUID ppInvoiceID;
    private UUID materialGoodsID;
    private UUID repositoryID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private UUID unitID;
    private BigDecimal quantity;
    private BigDecimal remainingAmount;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal inwardAmount;
    private BigDecimal inwardAmountOriginal;
    private BigDecimal freightAmount;
    private BigDecimal freightAmountOriginal;
    private BigDecimal importTaxExpenseAmount;
    private BigDecimal importTaxExpenseAmountOriginal;
    private LocalDate expiryDate;
    private String lotNo;
    private BigDecimal customUnitPrice;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private String vatAccount;
    private String deductionDebitAccount;
    private UUID mainUnitID;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private BigDecimal importTaxRate;
    private BigDecimal importTaxAmount;
    private BigDecimal importTaxAmountOriginal;
    private String importTaxAccount;
    private BigDecimal specialConsumeTaxRate;
    private BigDecimal specialConsumeTaxAmount;
    private BigDecimal specialConsumeTaxAmountOriginal;
    private String specialConsumeTaxAccount;
    private String invoiceType;
    private LocalDate invoiceDate;
    private String invoiceNo;
    private String invoiceSeries;
    private UUID goodsServicePurchaseID;
    private UUID accountingObjectID;
    private UUID budgetItemID;
    private UUID costSetID;
    private UUID contractID;
    private UUID statisticCodeID;
    private UUID departmentId;
    private UUID expenseItemID;
    private UUID ppOrderID;
    private UUID ppOrderDetailID;
    private BigDecimal cashOutExchangeRateFB;
    private BigDecimal cashOutAmountFB;
    private BigDecimal cashOutDifferAmountFB;
    private String cashOutDifferAccountFB;
    private BigDecimal cashOutExchangeRateMB;
    private BigDecimal cashOutAmountMB;
    private BigDecimal cashOutDifferAmountMB;
    private String cashOutDifferAccountMB;
    private BigDecimal cashOutVATAmountFB;
    private BigDecimal cashOutDifferVATAmountFB;
    private BigDecimal cashOutVATAmountMB;
    private BigDecimal cashOutDifferVATAmountMB;
    private Integer orderPriority;
    private String vatDescription;
    private String invoiceTemplate;
    private PPInvoiceDTO ppinvoice;
    private List<UnitConvertRateDTO> units;

    public PPInvoiceConvertDTO() {
    }

    public PPInvoiceConvertDTO(UUID ppInvoiceDetailID, LocalDate date, LocalDate postedDate, Integer typeGroupID, String noMBook, String materialGoodsCode, Integer typeLedger, UUID refID2, String reason, UUID ppInvoiceID, UUID materialGoodsID, UUID repositoryID, String description, String debitAccount, String creditAccount, UUID unitID, BigDecimal quantity, BigDecimal remainingAmount, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal amountOriginal, BigDecimal discountRate, BigDecimal discountAmount, BigDecimal discountAmountOriginal, BigDecimal inwardAmount, BigDecimal inwardAmountOriginal, BigDecimal freightAmount, BigDecimal freightAmountOriginal, BigDecimal importTaxExpenseAmount, BigDecimal importTaxExpenseAmountOriginal, LocalDate expiryDate, String lotNo, BigDecimal customUnitPrice, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, String vatAccount, String deductionDebitAccount, UUID mainUnitID, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, BigDecimal importTaxRate, BigDecimal importTaxAmount, BigDecimal importTaxAmountOriginal, String importTaxAccount, BigDecimal specialConsumeTaxRate, BigDecimal specialConsumeTaxAmount, BigDecimal specialConsumeTaxAmountOriginal, String specialConsumeTaxAccount, String invoiceType, LocalDate invoiceDate, String invoiceNo, String invoiceSeries, UUID goodsServicePurchaseID, UUID accountingObjectID, UUID budgetItemID, UUID costSetID, UUID contractID, UUID statisticCodeID, UUID departmentId, UUID expenseItemID, UUID ppOrderID, UUID ppOrderDetailID, BigDecimal cashOutExchangeRateFB, BigDecimal cashOutAmountFB, BigDecimal cashOutDifferAmountFB, String cashOutDifferAccountFB, BigDecimal cashOutExchangeRateMB, BigDecimal cashOutAmountMB, BigDecimal cashOutDifferAmountMB, String cashOutDifferAccountMB, BigDecimal cashOutVATAmountFB, BigDecimal cashOutDifferVATAmountFB, BigDecimal cashOutVATAmountMB, BigDecimal cashOutDifferVATAmountMB, Integer orderPriority, String vatDescription, String invoiceTemplate) {
        this.ppInvoiceDetailID = ppInvoiceDetailID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeGroupID = typeGroupID;
        this.reason = reason;
        this.noMBook = noMBook;
        this.materialGoodsCode = materialGoodsCode;
        this.typeLedger = typeLedger;
        this.ppInvoiceID = ppInvoiceID;
        this.materialGoodsID = materialGoodsID;
        this.repositoryID = repositoryID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unitID = unitID;
        this.refID2 = refID2;
        this.quantity = quantity;
        this.remainingAmount = remainingAmount;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.inwardAmount = inwardAmount;
        this.inwardAmountOriginal = inwardAmountOriginal;
        this.freightAmount = freightAmount;
        this.freightAmountOriginal = freightAmountOriginal;
        this.importTaxExpenseAmount = importTaxExpenseAmount;
        this.importTaxExpenseAmountOriginal = importTaxExpenseAmountOriginal;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.customUnitPrice = customUnitPrice;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.vatAccount = vatAccount;
        this.deductionDebitAccount = deductionDebitAccount;
        this.mainUnitID = mainUnitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.importTaxRate = importTaxRate;
        this.importTaxAmount = importTaxAmount;
        this.importTaxAmountOriginal = importTaxAmountOriginal;
        this.importTaxAccount = importTaxAccount;
        this.specialConsumeTaxRate = specialConsumeTaxRate;
        this.specialConsumeTaxAmount = specialConsumeTaxAmount;
        this.specialConsumeTaxAmountOriginal = specialConsumeTaxAmountOriginal;
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
        this.invoiceType = invoiceType;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.invoiceSeries = invoiceSeries;
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        this.accountingObjectID = accountingObjectID;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticCodeID = statisticCodeID;
        this.departmentId = departmentId;
        this.expenseItemID = expenseItemID;
        this.ppOrderID = ppOrderID;
        this.ppOrderDetailID = ppOrderDetailID;
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        this.cashOutAmountFB = cashOutAmountFB;
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        this.cashOutAmountMB = cashOutAmountMB;
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        this.cashOutVATAmountFB = cashOutVATAmountFB;
        this.cashOutDifferVATAmountFB = cashOutDifferVATAmountFB;
        this.cashOutVATAmountMB = cashOutVATAmountMB;
        this.cashOutDifferVATAmountMB = cashOutDifferVATAmountMB;
        this.orderPriority = orderPriority;
        this.vatDescription = vatDescription;
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public UUID getPpInvoiceDetailID() {
        return ppInvoiceDetailID;
    }

    public void setPpInvoiceDetailID(UUID ppInvoiceDetailID) {
        this.ppInvoiceDetailID = ppInvoiceDetailID;
    }

    public PPInvoiceDTO getPpinvoice() {
        return ppinvoice;
    }

    public void setPpinvoice(PPInvoiceDTO ppinvoice) {
        this.ppinvoice = ppinvoice;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

//    public String getNoFBook() {
//        return noFBook;
//    }
//
//    public void setNoFBook(String noFBook) {
//        this.noFBook = noFBook;
//    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public UUID getPpInvoiceID() {
        return ppInvoiceID;
    }

    public void setPpInvoiceID(UUID ppInvoiceID) {
        this.ppInvoiceID = ppInvoiceID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
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

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
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

    public BigDecimal getInwardAmount() {
        return inwardAmount;
    }

    public void setInwardAmount(BigDecimal inwardAmount) {
        this.inwardAmount = inwardAmount;
    }

    public BigDecimal getInwardAmountOriginal() {
        return inwardAmountOriginal;
    }

    public void setInwardAmountOriginal(BigDecimal inwardAmountOriginal) {
        this.inwardAmountOriginal = inwardAmountOriginal;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getFreightAmountOriginal() {
        return freightAmountOriginal;
    }

    public void setFreightAmountOriginal(BigDecimal freightAmountOriginal) {
        this.freightAmountOriginal = freightAmountOriginal;
    }

    public BigDecimal getImportTaxExpenseAmount() {
        return importTaxExpenseAmount;
    }

    public void setImportTaxExpenseAmount(BigDecimal importTaxExpenseAmount) {
        this.importTaxExpenseAmount = importTaxExpenseAmount;
    }

    public BigDecimal getImportTaxExpenseAmountOriginal() {
        return importTaxExpenseAmountOriginal;
    }

    public void setImportTaxExpenseAmountOriginal(BigDecimal importTaxExpenseAmountOriginal) {
        this.importTaxExpenseAmountOriginal = importTaxExpenseAmountOriginal;
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

    public BigDecimal getCustomUnitPrice() {
        return customUnitPrice;
    }

    public void setCustomUnitPrice(BigDecimal customUnitPrice) {
        this.customUnitPrice = customUnitPrice;
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

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
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

    public BigDecimal getImportTaxRate() {
        return importTaxRate;
    }

    public void setImportTaxRate(BigDecimal importTaxRate) {
        this.importTaxRate = importTaxRate;
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

    public BigDecimal getSpecialConsumeTaxRate() {
        return specialConsumeTaxRate;
    }

    public void setSpecialConsumeTaxRate(BigDecimal specialConsumeTaxRate) {
        this.specialConsumeTaxRate = specialConsumeTaxRate;
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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getPpOrderID() {
        return ppOrderID;
    }

    public void setPpOrderID(UUID ppOrderID) {
        this.ppOrderID = ppOrderID;
    }

    public UUID getPpOrderDetailID() {
        return ppOrderDetailID;
    }

    public void setPpOrderDetailID(UUID ppOrderDetailID) {
        this.ppOrderDetailID = ppOrderDetailID;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public BigDecimal getCashOutVATAmountFB() {
        return cashOutVATAmountFB;
    }

    public void setCashOutVATAmountFB(BigDecimal cashOutVATAmountFB) {
        this.cashOutVATAmountFB = cashOutVATAmountFB;
    }

    public BigDecimal getCashOutDifferVATAmountFB() {
        return cashOutDifferVATAmountFB;
    }

    public void setCashOutDifferVATAmountFB(BigDecimal cashOutDifferVATAmountFB) {
        this.cashOutDifferVATAmountFB = cashOutDifferVATAmountFB;
    }

    public BigDecimal getCashOutVATAmountMB() {
        return cashOutVATAmountMB;
    }

    public void setCashOutVATAmountMB(BigDecimal cashOutVATAmountMB) {
        this.cashOutVATAmountMB = cashOutVATAmountMB;
    }

    public BigDecimal getCashOutDifferVATAmountMB() {
        return cashOutDifferVATAmountMB;
    }

    public void setCashOutDifferVATAmountMB(BigDecimal cashOutDifferVATAmountMB) {
        this.cashOutDifferVATAmountMB = cashOutDifferVATAmountMB;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public List<UnitConvertRateDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitConvertRateDTO> units) {
        this.units = units;
    }
}
