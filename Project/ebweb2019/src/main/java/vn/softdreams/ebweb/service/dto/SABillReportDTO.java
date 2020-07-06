package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SABillReportDTO {
    private UUID id;

    private UUID SABillID;

    public UUID materialGoodsID;

    public String materialGoodsName;

    public String materialGoodsCode;

    private BigDecimal unitPrice;

    private UUID unitID;

    private BigDecimal unitPriceOriginal;

    private BigDecimal quantity;

    private BigDecimal amount;

    private BigDecimal amountOriginal;

    private BigDecimal vatRate;

    private BigDecimal vatAmount;

    private BigDecimal vatAmountOriginal;

    private BigDecimal discountRate;

    private BigDecimal discountAmount;

    private BigDecimal discountAmountOriginal;

    private String vatAmountString;

    private String vatAmountOriginalString;

    private String amountOriginalString;

    private String amountString;

    private String quantityString;

    private String unitPriceString;

    private String vatRateString;

    private String discountRateString;

    private String discountAmountString;

    private String discountAmountOriginalString;

    private String unitName;

    private BigDecimal totalAll;

    private String totalAllString;

    private BigDecimal totalAmount;

    private String totalAmountString;

    private BigDecimal totalVatAmountOriginal;

    private String totalVatAmountOriginalString;

    private BigDecimal totalDiscountAmountOriginal;

    private String totalDiscountAmountOriginalString;

    private String  AmountInWordString;

    private String description;

    private String totalQuantitiesString;

    private String listCommonNameInventory;

    private Boolean isPromotion;

    private Integer index;

    public SABillReportDTO() {
    }

    public SABillReportDTO(String materialGoodsName,
                           String materialGoodsCode,
                           BigDecimal unitPrice,
                           BigDecimal unitPriceOriginal,
                           BigDecimal quantity,
                           String unitName,
                           BigDecimal amount,
                           BigDecimal amountOriginal,
                           BigDecimal vatRate,
                           BigDecimal vatAmount,
                           BigDecimal vatAmountOriginal,
                           BigDecimal discountAmount,
                           BigDecimal discountAmountOriginal,
                           BigDecimal discountRate,
                           String description,
                           String listCommonNameInventory,
                           Boolean isPromotion) {
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsCode = materialGoodsCode;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.quantity = quantity;
        this.unitName = unitName;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.description = description;
        this.listCommonNameInventory = listCommonNameInventory;
        this.isPromotion = isPromotion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSABillID() {
        return SABillID;
    }

    public void setSABillID(UUID SABillID) {
        this.SABillID = SABillID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getmaterialGoodsName() {
        return materialGoodsName;
    }

    public void setmaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public String getVatAmountString() {
        return vatAmountString;
    }

    public void setVatAmountString(String vatAmountString) {
        this.vatAmountString = vatAmountString;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public String getUnitPriceString() {
        return unitPriceString;
    }

    public void setUnitPriceString(String unitPriceString) {
        this.unitPriceString = unitPriceString;
    }

    public String getVatRateString() {
        return vatRateString;
    }

    public void setVatRateString(String vatRateString) {
        this.vatRateString = vatRateString;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public String getVatAmountOriginalString() {
        return vatAmountOriginalString;
    }

    public void setVatAmountOriginalString(String vatAmountOriginalString) {
        this.vatAmountOriginalString = vatAmountOriginalString;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getDiscountRateString() {
        return discountRateString;
    }

    public void setDiscountRateString(String discountRateString) {
        this.discountRateString = discountRateString;
    }

    public String getDiscountAmountString() {
        return discountAmountString;
    }

    public void setDiscountAmountString(String discountAmountString) {
        this.discountAmountString = discountAmountString;
    }

    public String getDiscountAmountOriginalString() {
        return discountAmountOriginalString;
    }

    public void setDiscountAmountOriginalString(String discountAmountOriginalString) {
        this.discountAmountOriginalString = discountAmountOriginalString;
    }

    public BigDecimal getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(BigDecimal totalAll) {
        this.totalAll = totalAll;
    }

    public BigDecimal getTotalVatAmountOriginal() {
        return totalVatAmountOriginal;
    }

    public void setTotalVatAmountOriginal(BigDecimal totalVatAmountOriginal) {
        this.totalVatAmountOriginal = totalVatAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public String getTotalAllString() {
        return totalAllString;
    }

    public void setTotalAllString(String totalAllString) {
        this.totalAllString = totalAllString;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountString() {
        return totalAmountString;
    }

    public void setTotalAmountString(String totalAmountString) {
        this.totalAmountString = totalAmountString;
    }

    public String getTotalVatAmountOriginalString() {
        return totalVatAmountOriginalString;
    }

    public void setTotalVatAmountOriginalString(String totalVatAmountOriginalString) {
        this.totalVatAmountOriginalString = totalVatAmountOriginalString;
    }

    public String getTotalDiscountAmountOriginalString() {
        return totalDiscountAmountOriginalString;
    }

    public void setTotalDiscountAmountOriginalString(String totalDiscountAmountOriginalString) {
        this.totalDiscountAmountOriginalString = totalDiscountAmountOriginalString;
    }

    public String getAmountInWordString() {
        return AmountInWordString;
    }

    public void setAmountInWordString(String amountInWordString) {
        AmountInWordString = amountInWordString;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getTotalQuantitiesString() {
        return totalQuantitiesString;
    }

    public void setTotalQuantitiesString(String totalQuantitiesString) {
        this.totalQuantitiesString = totalQuantitiesString;
    }

    public String getListCommonNameInventory() {
        return listCommonNameInventory;
    }

    public void setListCommonNameInventory(String listCommonNameInventory) {
        this.listCommonNameInventory = listCommonNameInventory;
    }

    public Boolean isIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
