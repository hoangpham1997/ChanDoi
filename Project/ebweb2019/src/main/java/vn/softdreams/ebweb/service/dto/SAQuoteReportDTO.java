package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;

public class SAQuoteReportDTO {
    private String materialGoodsName;
    private String description;
    private String unitName;
    private BigDecimal quantity;
    private String quantityString;
    private BigDecimal unitPriceOriginal;
    private BigDecimal unitPrice;
    private String unitPriceOriginalString;
    private BigDecimal amountOriginal;
    private String amountOriginalString;
    private BigDecimal discountRate;
    private BigDecimal discountAmountOriginal;
    private BigDecimal vATRate;
    private BigDecimal vATAmountOriginal;

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

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPriceOriginalString() {
        return unitPriceOriginalString;
    }

    public void setUnitPriceOriginalString(String unitPriceOriginalString) {
        this.unitPriceOriginalString = unitPriceOriginalString;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
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

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public SAQuoteReportDTO() {
    }

    public SAQuoteReportDTO(String materialGoodsName, String description, String unitName, BigDecimal quantity, String quantityString, BigDecimal unitPriceOriginal, BigDecimal unitPrice, String unitPriceString, BigDecimal amountOriginal, String amountOriginalString, BigDecimal discountRate, BigDecimal discountAmountOriginal,BigDecimal vATRate, BigDecimal vATAmountOriginal) {
        this.materialGoodsName = materialGoodsName;
        this.description = description;
        this.unitName = unitName;
        this.quantity = quantity;
        this.quantityString = quantityString;
        this.unitPriceOriginal = unitPriceOriginal;
        this.unitPrice = unitPrice;
        this.unitPriceOriginalString = unitPriceOriginalString;
        this.amountOriginal = amountOriginal;
        this.amountOriginalString = amountOriginalString;
        this.discountRate = discountRate;
        this.discountAmountOriginal = discountAmountOriginal;
        this.vATRate = vATRate;
        this.vATAmountOriginal = vATAmountOriginal;
    }
}

