package vn.softdreams.ebweb.service.dto;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPInvoiceDetailDTO {
    private UUID id;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String repositoryCode;
    private String debitAccount;
    private String creditAccount;
    private String accountingObjectCode;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPriceOriginal;
    private BigDecimal unitPrice;
    private String mainUnitName;
    private BigDecimal mainConvertRate;
    private String formula;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal amountOriginal;
    private BigDecimal amount;
    private BigDecimal discountRate;
    private BigDecimal discountAmountOriginal;
    private BigDecimal discountAmount;
    private BigDecimal freightAmount;
    private BigDecimal importTaxExpenseAmount;
    private BigDecimal inwardAmount;
    private String lotNo;
    private LocalDate expiryDate;
    private UUID ppOrderDetailId;
    private String vatDescription;
    private BigDecimal customUnitPrice;
    private BigDecimal importTaxRate;
    private BigDecimal importTaxAmount;
    private String importTaxAccount;
    private BigDecimal specialConsumeTaxRate;
    private BigDecimal specialConsumeTaxAmount;
    private String specialConsumeTaxAccount;
    private BigDecimal vatRate;
    private BigDecimal vatAmountOriginal;
    private BigDecimal vatAmount;
    private String vatAccount;
    private String deductionDebitAccount;
    private String invoiceTemplate;
    private String invoiceSeries;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String goodsServicePurchaseCode;
    private String expenseItemCode;
    private String costSetCode;
    private String noMBook;
    private String budgetItemCode;
    private String organizationUnitCode;
    private String statisticsCode;
    private String ppOrderNo;
    private UUID unitID;
    private String amountOriginalToString;
    private String amountToString;
    private String unitPriceOriginalToString;
    private String amountString;
    private String description;
    private String amountOriginalString;
    private String quantityToString;
    private String repositoryName;

    public PPInvoiceDetailDTO() {
    }

    public PPInvoiceDetailDTO(UUID id, String materialGoodsCode, String materialGoodsName, String repositoryCode, String debitAccount, String creditAccount, String accountingObjectCode, String unitName, BigDecimal quantity, BigDecimal unitPriceOriginal, BigDecimal unitPrice, String mainUnitName, BigDecimal mainConvertRate, String formula, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal amountOriginal, BigDecimal amount, BigDecimal discountRate, BigDecimal discountAmountOriginal, BigDecimal discountAmount, BigDecimal freightAmount, BigDecimal importTaxExpenseAmount, BigDecimal inwardAmount, String lotNo, LocalDate expiryDate, UUID ppOrderDetailId, String vatDescription, BigDecimal customUnitPrice, BigDecimal importTaxRate, BigDecimal importTaxAmount, String importTaxAccount, BigDecimal specialConsumeTaxRate, BigDecimal specialConsumeTaxAmount, String specialConsumeTaxAccount, BigDecimal vatRate, BigDecimal vatAmountOriginal, BigDecimal vatAmount, String vatAccount, String deductionDebitAccount, String invoiceTemplate, String invoiceSeries, String invoiceNo, LocalDate invoiceDate, String goodsServicePurchaseCode, String expenseItemCode, String costSetCode, String noMBook, String budgetItemCode, String organizationUnitCode, String statisticsCode, String ppOrderNo, String repositoryName) {
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.repositoryCode = repositoryCode;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.accountingObjectCode = accountingObjectCode;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPriceOriginal = unitPriceOriginal;
        this.unitPrice = unitPrice;
        this.mainUnitName = mainUnitName;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.amountOriginal = amountOriginal;
        this.amount = amount;
        this.discountRate = discountRate;
        this.discountAmountOriginal = discountAmountOriginal;
        this.discountAmount = discountAmount;
        this.freightAmount = freightAmount;
        this.importTaxExpenseAmount = importTaxExpenseAmount;
        this.inwardAmount = inwardAmount;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.ppOrderDetailId = ppOrderDetailId;
        this.vatDescription = vatDescription;
        this.customUnitPrice = customUnitPrice;
        this.importTaxRate = importTaxRate;
        this.importTaxAmount = importTaxAmount;
        this.importTaxAccount = importTaxAccount;
        this.specialConsumeTaxRate = specialConsumeTaxRate;
        this.specialConsumeTaxAmount = specialConsumeTaxAmount;
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
        this.vatRate = vatRate;
        this.vatAmountOriginal = vatAmountOriginal;
        this.vatAmount = vatAmount;
        this.vatAccount = vatAccount;
        this.deductionDebitAccount = deductionDebitAccount;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
        this.expenseItemCode = expenseItemCode;
        this.costSetCode = costSetCode;
        this.noMBook = noMBook;
        this.budgetItemCode = budgetItemCode;
        this.organizationUnitCode = organizationUnitCode;
        this.statisticsCode = statisticsCode;
        this.ppOrderNo = ppOrderNo;
        this.description = materialGoodsName;
        this.repositoryName = repositoryName;
    }

    public String getUnitPriceOriginalToString() {
        return unitPriceOriginalToString;
    }

    public void setUnitPriceOriginalToString(String unitPriceOriginalToString) {
        this.unitPriceOriginalToString = unitPriceOriginalToString;
    }

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
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

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
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

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getImportTaxExpenseAmount() {
        return importTaxExpenseAmount;
    }

    public void setImportTaxExpenseAmount(BigDecimal importTaxExpenseAmount) {
        this.importTaxExpenseAmount = importTaxExpenseAmount;
    }

    public BigDecimal getInwardAmount() {
        return inwardAmount;
    }

    public void setInwardAmount(BigDecimal inwardAmount) {
        this.inwardAmount = inwardAmount;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UUID getPpOrderDetailId() {
        return ppOrderDetailId;
    }

    public void setPpOrderDetailId(UUID ppOrderDetailId) {
        this.ppOrderDetailId = ppOrderDetailId;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public BigDecimal getCustomUnitPrice() {
        return customUnitPrice;
    }

    public void setCustomUnitPrice(BigDecimal customUnitPrice) {
        this.customUnitPrice = customUnitPrice;
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

    public String getSpecialConsumeTaxAccount() {
        return specialConsumeTaxAccount;
    }

    public void setSpecialConsumeTaxAccount(String specialConsumeTaxAccount) {
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
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

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getGoodsServicePurchaseCode() {
        return goodsServicePurchaseCode;
    }

    public void setGoodsServicePurchaseCode(String goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getPpOrderNo() {
        return ppOrderNo;
    }

    public void setPpOrderNo(String ppOrderNo) {
        this.ppOrderNo = ppOrderNo;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantityToString() {
        return quantityToString;
    }

    public void setQuantityToString(String quantityToString) {
        this.quantityToString = quantityToString;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public String toString() {
        return "PPInvoiceDetailDTO{" +
            "id=" + id +
            ", materialGoodsCode='" + materialGoodsCode + '\'' +
            ", materialGoodsName='" + materialGoodsName + '\'' +
            ", repositoryCode='" + repositoryCode + '\'' +
            ", debitAccount='" + debitAccount + '\'' +
            ", creditAccount='" + creditAccount + '\'' +
            ", accountingObjectCode='" + accountingObjectCode + '\'' +
            ", unitName='" + unitName + '\'' +
            ", quantity=" + quantity +
            ", unitPriceOriginal=" + unitPriceOriginal +
            ", unitPrice=" + unitPrice +
            ", mainUnitName='" + mainUnitName + '\'' +
            ", mainConvertRate=" + mainConvertRate +
            ", formula='" + formula + '\'' +
            ", mainQuantity=" + mainQuantity +
            ", mainUnitPrice=" + mainUnitPrice +
            ", amountOriginal=" + amountOriginal +
            ", amount=" + amount +
            ", discountRate=" + discountRate +
            ", discountAmountOriginal=" + discountAmountOriginal +
            ", discountAmount=" + discountAmount +
            ", freightAmount=" + freightAmount +
            ", importTaxExpenseAmount=" + importTaxExpenseAmount +
            ", inwardAmount=" + inwardAmount +
            ", lotNo='" + lotNo + '\'' +
            ", expiryDate=" + expiryDate +
            ", ppOrderDetailId=" + ppOrderDetailId +
            ", vatDescription='" + vatDescription + '\'' +
            ", customUnitPrice=" + customUnitPrice +
            ", importTaxRate=" + importTaxRate +
            ", importTaxAmount=" + importTaxAmount +
            ", importTaxAccount='" + importTaxAccount + '\'' +
            ", specialConsumeTaxRate=" + specialConsumeTaxRate +
            ", specialConsumeTaxAmount=" + specialConsumeTaxAmount +
            ", specialConsumeTaxAccount='" + specialConsumeTaxAccount + '\'' +
            ", vatRate=" + vatRate +
            ", vatAmountOriginal=" + vatAmountOriginal +
            ", vatAmount=" + vatAmount +
            ", vatAccount='" + vatAccount + '\'' +
            ", deductionDebitAccount='" + deductionDebitAccount + '\'' +
            ", invoiceTemplate='" + invoiceTemplate + '\'' +
            ", invoiceSeries='" + invoiceSeries + '\'' +
            ", invoiceNo='" + invoiceNo + '\'' +
            ", invoiceDate=" + invoiceDate +
            ", goodsServicePurchaseCode='" + goodsServicePurchaseCode + '\'' +
            ", expenseItemCode='" + expenseItemCode + '\'' +
            ", costSetCode='" + costSetCode + '\'' +
            ", noMBook='" + noMBook + '\'' +
            ", budgetItemCode='" + budgetItemCode + '\'' +
            ", organizationUnitCode='" + organizationUnitCode + '\'' +
            ", statisticsCode='" + statisticsCode + '\'' +
            '}';
    }
}
