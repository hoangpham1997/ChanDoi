package vn.softdreams.ebweb.service.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPDiscountReturnDetailsReportConvertDTO {
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
    private String repositoryCode;
    private String materialGoodsCode;
    private String unitCode;
    private String mainUnitCode;
    private String goodsServicePurchaseCode;
    private String accountingObjectCode;
    private String costSetCode;
    private String contractNoMBook;
    private String contractNoFBook;
    private String statisticsCodeCode;
    private String expenseItemCode;
    private String budgetItemCode;
    private String departmentCode;
    private String ppInVoiceNoBook;
    private String ppInVoiceDate;
    private String amountToString;
    private String amountOriginalToString;
    private String unitPriceOriginalToString;
    private String unitName;
    private String materialGoodsName;
    private String repositoryName;
    private String quantityToString;

    public PPDiscountReturnDetailsReportConvertDTO() {
    }

    public PPDiscountReturnDetailsReportConvertDTO(String description, String debitAccount, String creditAccount, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal amountOriginal, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, String expiryDate, String lotNo, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal vatAmountOriginal, BigDecimal vatAccount, Boolean isMatch, String matchDate, Integer orderPriority, String vatDescription, String deductionDebitAccount, Boolean isPromotion, String repositoryCode, String materialGoodsCode, String unitCode, String mainUnitCode, String goodsServicePurchaseCode, String accountingObjectCode, String costSetCode, String contractNoMBook, String contractNoFBook, String statisticsCodeCode, String expenseItemCode, String budgetItemCode, String departmentCode, String ppInVoiceNoBook, String ppInVoiceDate, String unitName, String materialGoodsName, String repositoryName) {
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
        this.repositoryCode = repositoryCode;
        this.materialGoodsCode = materialGoodsCode;
        this.unitCode = unitCode;
        this.mainUnitCode = mainUnitCode;
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
        this.accountingObjectCode = accountingObjectCode;
        this.costSetCode = costSetCode;
        this.contractNoMBook = contractNoMBook;
        this.contractNoFBook = contractNoFBook;
        this.statisticsCodeCode = statisticsCodeCode;
        this.expenseItemCode = expenseItemCode;
        this.budgetItemCode = budgetItemCode;
        this.departmentCode = departmentCode;
        this.ppInVoiceNoBook = ppInVoiceNoBook;
        this.ppInVoiceDate = ppInVoiceDate;
        this.unitName = unitName;
        this.materialGoodsName = materialGoodsName;
        this.repositoryName = repositoryName;
    }
    public String getQuantityToString() {
        return quantityToString;
    }

    public void setQuantityToString(String quantityToString) {
        this.quantityToString = quantityToString;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getMainUnitCode() {
        return mainUnitCode;
    }

    public void setMainUnitCode(String mainUnitCode) {
        this.mainUnitCode = mainUnitCode;
    }

    public String getGoodsServicePurchaseCode() {
        return goodsServicePurchaseCode;
    }

    public void setGoodsServicePurchaseCode(String goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
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

    public String getStatisticsCodeCode() {
        return statisticsCodeCode;
    }

    public void setStatisticsCodeCode(String statisticsCodeCode) {
        this.statisticsCodeCode = statisticsCodeCode;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
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

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getUnitPriceOriginalToString() {
        return unitPriceOriginalToString;
    }

    public void setUnitPriceOriginalToString(String unitPriceOriginalToString) {
        this.unitPriceOriginalToString = unitPriceOriginalToString;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
