package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SaReturnDetailsRSInwardDTO {
    private UUID id;
    private UUID saReturnID;
    private Boolean isPromotion;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private String discountAccount;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private BigDecimal owPrice;
    private BigDecimal owAmount;
    private String costAccount;
    private String repositoryAccount;
    private LocalDate expiryDate;
    private String lotNo;
    private UUID departmentID;
    private UUID expenseItemID;
    private UUID budgetItemID;
    private UUID costSetID;
    private UUID contractID;
    private UUID statisticsCodeID;
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
    private UUID materialGoodsID;
    private UUID unitID;
    private UUID repositoryID;
    private UUID mainUnitID;
    private UUID accountingObjectID;
    private String vatAccount;
    private String deductionDebitAccount;
    private UUID saBillID;
    private UUID saBillDetailID;
    private UUID saInvoiceID;
    private UUID saInvoiceDetailID;
    private UUID careerGroupID;
    private String bookSaReturn;

    public SaReturnDetailsRSInwardDTO(UUID id, UUID saReturnID, Boolean isPromotion, String description, String debitAccount, String creditAccount, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, BigDecimal amount, BigDecimal amountOriginal, BigDecimal discountRate, BigDecimal discountAmount, BigDecimal discountAmountOriginal, String discountAccount, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, BigDecimal owPrice, BigDecimal owAmount, String costAccount, String repositoryAccount, LocalDate expiryDate, String lotNo, UUID departmentID, UUID expenseItemID, UUID budgetItemID, UUID costSetID, UUID contractID, UUID statisticsCodeID, BigDecimal cashOutExchangeRateFB, BigDecimal cashOutAmountFB, BigDecimal cashOutDifferAmountFB, String cashOutDifferAccountFB, BigDecimal cashOutExchangeRateMB, BigDecimal cashOutAmountMB, BigDecimal cashOutDifferAmountMB, String cashOutDifferAccountMB, BigDecimal cashOutVATAmountFB, BigDecimal cashOutDifferVATAmountFB, BigDecimal cashOutVATAmountMB, BigDecimal cashOutDifferVATAmountMB, Integer orderPriority, String vatDescription, UUID materialGoodsID, UUID unitID, UUID repositoryID, UUID mainUnitID, UUID accountingObjectID, String vatAccount, String deductionDebitAccount, UUID saBillID, UUID saBillDetailID, UUID saInvoiceID, UUID saInvoiceDetailID, UUID careerGroupID, String bookSaReturn) {
        this.id = id;
        this.saReturnID = saReturnID;
        this.isPromotion = isPromotion;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.discountAccount = discountAccount;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.owPrice = owPrice;
        this.owAmount = owAmount;
        this.costAccount = costAccount;
        this.repositoryAccount = repositoryAccount;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticsCodeID = statisticsCodeID;
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
        this.materialGoodsID = materialGoodsID;
        this.unitID = unitID;
        this.repositoryID = repositoryID;
        this.mainUnitID = mainUnitID;
        this.accountingObjectID = accountingObjectID;
        this.vatAccount = vatAccount;
        this.deductionDebitAccount = deductionDebitAccount;
        this.saBillID = saBillID;
        this.saBillDetailID = saBillDetailID;
        this.saInvoiceID = saInvoiceID;
        this.saInvoiceDetailID = saInvoiceDetailID;
        this.careerGroupID = careerGroupID;
        this.bookSaReturn = bookSaReturn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaReturnID() {
        return saReturnID;
    }

    public void setSaReturnID(UUID saReturnID) {
        this.saReturnID = saReturnID;
    }

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
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

    public String getDiscountAccount() {
        return discountAccount;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
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

    public BigDecimal getOwPrice() {
        return owPrice;
    }

    public void setOwPrice(BigDecimal owPrice) {
        this.owPrice = owPrice;
    }

    public BigDecimal getOwAmount() {
        return owAmount;
    }

    public void setOwAmount(BigDecimal owAmount) {
        this.owAmount = owAmount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public String getRepositoryAccount() {
        return repositoryAccount;
    }

    public void setRepositoryAccount(String repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
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

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public UUID getSaInvoiceID() {
        return saInvoiceID;
    }

    public void setSaInvoiceID(UUID saInvoiceID) {
        this.saInvoiceID = saInvoiceID;
    }

    public UUID getSaInvoiceDetailID() {
        return saInvoiceDetailID;
    }

    public void setSaInvoiceDetailID(UUID saInvoiceDetailID) {
        this.saInvoiceDetailID = saInvoiceDetailID;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
    }

    public String getBookSaReturn() {
        return bookSaReturn;
    }

    public void setBookSaReturn(String bookSaReturn) {
        this.bookSaReturn = bookSaReturn;
    }
}
