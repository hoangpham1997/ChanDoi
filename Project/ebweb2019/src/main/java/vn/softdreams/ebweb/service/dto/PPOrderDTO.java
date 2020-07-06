package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PPOrderDTO {
    UUID id;
    String orderDate;
    String orderNumber;
    String productCode;
    String productName;
    BigDecimal quantityReceipt;
    UUID ppOrderId;
    Integer priority;
    UUID accountingObjectID;
    private String materialGoodsCode;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal vATRate;
    private BigDecimal vATAmount;
    private BigDecimal vATAmountOriginal;
    private BigDecimal quantityDelivery;
    private Number total;
    private String quantityString;
    private String unitPriceString;
    private String amountString;
    private String amountOriginalString;
    private String discountRateString;
    private String discountAmountString;
    private String discountAmountOriginalString;
    private String vATRateString;
    private String vATAmountString;
    private String vATAmountOriginalString;
    private String unitName;
    private Integer orderPriority;
    private String reason;
    private Integer typeGroupID;

    public PPOrderDTO(UUID id, String orderDate, String orderNumber, String productCode,
                      String productName, BigDecimal quantityReceipt, UUID ppOrderId, Integer priority, UUID accountingObjectID, String reason, Integer typeGroupID) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.productName = productName;
        this.quantityReceipt = quantityReceipt;
        this.ppOrderId = ppOrderId;
        this.priority = priority;
        this.accountingObjectID = accountingObjectID;
        this.reason = reason;
        this.typeGroupID = typeGroupID;
    }

    public PPOrderDTO() {
    }

    public PPOrderDTO(
            String materialGoodsCode,
            String description,
            BigDecimal quantity,
            String unitName,
            BigDecimal unitPrice,
            BigDecimal unitPriceOriginal,
            BigDecimal amount,
            BigDecimal amountOriginal,
            BigDecimal discountRate,
            BigDecimal discountAmount,
            BigDecimal discountAmountOriginal,
            BigDecimal vATRate,
            BigDecimal vATAmount,
            BigDecimal vATAmountOriginal,
            Integer orderPriority) {
        this.materialGoodsCode = materialGoodsCode;
        this.description = description;
        this.quantity = quantity;
        this.unitName = unitName;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.vATRate = vATRate;
        this.vATAmount = vATAmount;
        this.vATAmountOriginal = vATAmountOriginal;
        this.orderPriority = orderPriority;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getQuantityDelivery() {
        return quantityDelivery;
    }

    public void setQuantityDelivery(BigDecimal quantityDelivery) {
        this.quantityDelivery = quantityDelivery;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public PPOrderDTO(UUID id, BigDecimal quantityReceipt) {
        this.id = id;
        this.quantityReceipt = quantityReceipt;
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

    public String getvATRateString() {
        return vATRateString;
    }

    public void setvATRateString(String vATRateString) {
        this.vATRateString = vATRateString;
    }

    public String getvATAmountString() {
        return vATAmountString;
    }

    public void setvATAmountString(String vATAmountString) {
        this.vATAmountString = vATAmountString;
    }

    public String getvATAmountOriginalString() {
        return vATAmountOriginalString;
    }

    public void setvATAmountOriginalString(String vATAmountOriginalString) {
        this.vATAmountOriginalString = vATAmountOriginalString;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public UUID getPpOrderId() {
        return ppOrderId;
    }

    public void setPpOrderId(UUID ppOrderId) {
        this.ppOrderId = ppOrderId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantityReceipt() {
        return quantityReceipt;
    }

    public void setQuantityReceipt(BigDecimal quantityReceipt) {
        this.quantityReceipt = quantityReceipt;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }
}
