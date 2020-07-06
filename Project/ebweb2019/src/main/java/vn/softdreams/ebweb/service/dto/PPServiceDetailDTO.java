package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PPServiceDetailDTO {
    private UUID id;
    private UUID materialGoodsId;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String debitAccount;
    private String creditAccount;
    private UUID postedObjectId;
    private String postedObjectCode;
    private String postedObjectName;
    private UUID unitId;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private String discountAccount;
    private String ppOrderNo;
    private UUID ppOrderId;
    private UUID ppOrderDetailId;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amountOriginal;
    private BigDecimal discountAmountOriginal;
    private String vatDescription;
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private String vatAccount;
    private String deductionDebitAccount;
    private String invoiceTemplate;
    private String invoiceSeries;
    private String invoiceNo;
    private String invoiceDate;
    private UUID goodsServicePurchaseId;
    private String goodsServicePurchaseName;
    private Boolean isForeignCurrency;
    private BigDecimal vatAmountOriginal;
    private UUID expenseItemId;
    private String expenseItemCode;
    private UUID costSetId;
    private String costSetCode;
    private UUID emContractId;
    private String emContractCode;
    private UUID budgetItemId;
    private String budgetItemCode;
    private UUID departmentId;
    private String departmentCode;
    private UUID statisticsId;
    private String statisticsCode;
    private Integer orderPriority;

    public PPServiceDetailDTO(UUID id, UUID materialGoodsId, String materialGoodsCode, String materialGoodsName,
                              String debitAccount, String creditAccount, UUID postedObjectId,
                              String postedObjectCode, String postedObjectName, UUID unitId, String unitName,
                              BigDecimal quantity, BigDecimal unitPrice, BigDecimal amount, BigDecimal discountRate,
                              BigDecimal discountAmount, String discountAccount, UUID ppOrderId, UUID ppOrderDetailId, String ppOrderNo,
                              BigDecimal unitPriceOriginal, BigDecimal amountOriginal,
                              BigDecimal discountAmountOriginal, String vatDescription, BigDecimal vatRate,
                              BigDecimal vatAmount, String vatAccount, String deductionDebitAccount,
                              String invoiceTemplate, String invoiceSeries, String invoiceNo, String invoiceDate,
                              UUID goodsServicePurchaseId, String goodsServicePurchaseName, Boolean isForeignCurrency,
                              BigDecimal vatAmountOriginal, UUID expenseItemId, String expenseItemCode,
                              UUID costSetId, String costSetCode, UUID emContractId, String emContractCode,
                              UUID budgetItemId, String budgetItemCode, UUID departmentId, String departmentCode,
                              UUID statisticsId, String statisticsCode, Integer orderPriority) {
        this.id = id;
        this.materialGoodsId = materialGoodsId;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.postedObjectId = postedObjectId;
        this.postedObjectCode = postedObjectCode;
        this.postedObjectName = postedObjectName;
        this.unitId = unitId;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAccount = discountAccount;
        this.ppOrderNo = ppOrderNo;
        this.ppOrderId = ppOrderId;
        this.ppOrderDetailId = ppOrderDetailId;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amountOriginal = amountOriginal;
        this.discountAmountOriginal = discountAmountOriginal;
        this.vatDescription = vatDescription;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAccount = vatAccount;
        this.deductionDebitAccount = deductionDebitAccount;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.goodsServicePurchaseId = goodsServicePurchaseId;
        this.goodsServicePurchaseName = goodsServicePurchaseName;
        this.isForeignCurrency = isForeignCurrency;
        this.vatAmountOriginal = vatAmountOriginal;
        this.expenseItemId = expenseItemId;
        this.expenseItemCode = expenseItemCode;
        this.costSetId = costSetId;
        this.costSetCode = costSetCode;
        this.emContractId = emContractId;
        this.emContractCode = emContractCode;
        this.budgetItemId = budgetItemId;
        this.budgetItemCode = budgetItemCode;
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.statisticsId = statisticsId;
        this.statisticsCode = statisticsCode;
        this.orderPriority = orderPriority;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getPpOrderId() {
        return ppOrderId;
    }

    public void setPpOrderId(UUID ppOrderId) {
        this.ppOrderId = ppOrderId;
    }

    public UUID getPpOrderDetailId() {
        return ppOrderDetailId;
    }

    public void setPpOrderDetailId(UUID ppOrderDetailId) {
        this.ppOrderDetailId = ppOrderDetailId;
    }

    public PPServiceDetailDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPostedObjectId() {
        return postedObjectId;
    }

    public void setPostedObjectId(UUID postedObjectId) {
        this.postedObjectId = postedObjectId;
    }

    public UUID getEmContractId() {
        return emContractId;
    }

    public void setEmContractId(UUID emContractId) {
        this.emContractId = emContractId;
    }


    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
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

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }


    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(UUID statisticsId) {
        this.statisticsId = statisticsId;
    }

    public UUID getGoodsServicePurchaseId() {
        return goodsServicePurchaseId;
    }

    public void setGoodsServicePurchaseId(UUID goodsServicePurchaseId) {
        this.goodsServicePurchaseId = goodsServicePurchaseId;
    }

    public UUID getMaterialGoodsId() {
        return materialGoodsId;
    }

    public void setMaterialGoodsId(UUID materialGoodsId) {
        this.materialGoodsId = materialGoodsId;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
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

    public String getPostedObjectCode() {
        return postedObjectCode;
    }

    public void setPostedObjectCode(String postedObjectCode) {
        this.postedObjectCode = postedObjectCode;
    }

    public String getPostedObjectName() {
        return postedObjectName;
    }

    public void setPostedObjectName(String postedObjectName) {
        this.postedObjectName = postedObjectName;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPpOrderNo() {
        return ppOrderNo;
    }

    public void setPpOrderNo(String ppOrderNo) {
        this.ppOrderNo = ppOrderNo;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
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

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getGoodsServicePurchaseName() {
        return goodsServicePurchaseName;
    }

    public void setGoodsServicePurchaseName(String goodsServicePurchaseName) {
        this.goodsServicePurchaseName = goodsServicePurchaseName;
    }

    public Boolean getForeignCurrency() {
        return isForeignCurrency;
    }

    public void setForeignCurrency(Boolean foreignCurrency) {
        isForeignCurrency = foreignCurrency;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
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

    public String getEmContractCode() {
        return emContractCode;
    }

    public void setEmContractCode(String emContractCode) {
        this.emContractCode = emContractCode;
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
}
