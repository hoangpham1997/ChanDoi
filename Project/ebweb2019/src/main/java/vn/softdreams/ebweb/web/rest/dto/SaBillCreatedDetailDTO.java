package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SaBillCreatedDetailDTO {
    private String invoiceNo;
    private LocalDate invoiceDate;
    private Integer invoiceForm;
    private Integer typeGroupID;
    private String invoiceTemplate;
    private UUID id;
    private UUID saBillID;
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
    private BigDecimal vatRate;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private Integer orderPriority;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private Boolean isPromotion;
    private List<UnitConvertRateDTO> units;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String contactName;
    private String companyTaxCode;

    public SaBillCreatedDetailDTO() {
    }

    public SaBillCreatedDetailDTO(String invoiceNo, LocalDate invoiceDate, Integer invoiceForm, Integer typeGroupID, String invoiceTemplate,
                                  UUID id, UUID saBillID, UUID materialGoodsID, String description, String debitAccount,
                                  String creditAccount, UUID unitID, BigDecimal quantity, BigDecimal unitPrice,
                                  BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal amountOriginal,
                                  UUID mainUnitID, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate,
                                  String formula, LocalDate expiryDate, String lotNo, BigDecimal vatRate, BigDecimal vatAmount,
                                  BigDecimal vatAmountOriginal, Integer orderPriority, BigDecimal discountRate,
                                  BigDecimal discountAmount, BigDecimal discountAmountOriginal, Boolean isPromotion,
                                  UUID accountingObjectID, String accountingObjectName, String contactName,
                                  String companyTaxCode) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.invoiceForm = invoiceForm;
        this.typeGroupID = typeGroupID;
        this.invoiceTemplate = invoiceTemplate;
        this.id = id;
        this.saBillID = saBillID;
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
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.orderPriority = orderPriority;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.isPromotion = isPromotion;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.contactName = contactName;
        this.companyTaxCode = companyTaxCode;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
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

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaBillID() {
        return saBillID;
    }

    public void setSaBillID(UUID saBillID) {
        this.saBillID = saBillID;
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
    }

    public List<UnitConvertRateDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitConvertRateDTO> units) {
        this.units = units;
    }

    public Boolean getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }
}
