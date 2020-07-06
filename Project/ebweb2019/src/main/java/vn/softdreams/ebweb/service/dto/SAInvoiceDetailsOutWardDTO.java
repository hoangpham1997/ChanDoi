package vn.softdreams.ebweb.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SAInvoiceDetailsOutWardDTO {
    private UUID id;
    private UUID sAInvoiceId;
    private UUID materialGoodsID;
    private Boolean isPromotion;
    private UUID repositoryID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private UUID unitID;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private UUID mainUnitID;
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
    private String vatAccount;
    private String repositoryAccount;
    private String costAccount;
    private BigDecimal owPrice;
    private BigDecimal owAmount;
    private LocalDate expiryDate;
    private String lotNo;
    private UUID accountingObjectID;
    private UUID departmentID;
    private UUID expenseItemID;
    private UUID budgetItemID;
    private UUID costSetID;
    private UUID contractID;
    private UUID statisticsCodeID;
    private String vatDescription;
    private String deductionDebitAccount;
    private String noBookSaInvoice;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getsAInvoiceId() {
        return sAInvoiceId;
    }

    public void setsAInvoiceId(UUID sAInvoiceId) {
        this.sAInvoiceId = sAInvoiceId;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public String getNoBookSaInvoice() {
        return noBookSaInvoice;
    }

    public void setNoBookSaInvoice(String noBookSaInvoice) {
        this.noBookSaInvoice = noBookSaInvoice;
    }

    public SAInvoiceDetailsOutWardDTO(UUID id, UUID sAInvoiceId, UUID materialGoodsID, Boolean isPromotion, UUID repositoryID, String description, String debitAccount, String creditAccount, UUID unitID, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, UUID mainUnitID, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, BigDecimal amount, BigDecimal amountOriginal, BigDecimal discountRate, BigDecimal discountAmount, BigDecimal discountAmountOriginal, String discountAccount, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, String vatAccount, String repositoryAccount, String costAccount, BigDecimal owPrice, BigDecimal owAmount, LocalDate expiryDate, String lotNo, UUID accountingObjectID, UUID departmentID, UUID expenseItemID, UUID budgetItemID, UUID costSetID, UUID contractID, UUID statisticsCodeID, String vatDescription, String deductionDebitAccount, String noBookSaInvoice) {
        this.id = id;
        this.sAInvoiceId = sAInvoiceId;
        this.materialGoodsID = materialGoodsID;
        this.isPromotion = isPromotion;
        this.repositoryID = repositoryID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unitID = unitID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainUnitID = mainUnitID;
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
        this.vatAccount = vatAccount;
        this.repositoryAccount = repositoryAccount;
        this.costAccount = costAccount;
        this.owPrice = owPrice;
        this.owAmount = owAmount;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.accountingObjectID = accountingObjectID;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticsCodeID = statisticsCodeID;
        this.vatDescription = vatDescription;
        this.deductionDebitAccount = deductionDebitAccount;
        this.noBookSaInvoice = noBookSaInvoice;
    }

    public SAInvoiceDetailsOutWardDTO() {
    }
}
