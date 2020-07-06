package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.math.BigDecimal;

public class ItemInfo {
    private int lineNumber;
    private String itemCode;
    private String itemName;
    private String unitName;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal itemTotalAmountWithoutTax;
    private int taxPercentage;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private BigDecimal itemDiscount;
    private BigDecimal adjustmentTaxAmount;
    private boolean isIncreaseItem;
    private String itemNote;
    private String batchNo;
    private String expDate;
    private int selection;
    //        [JsonIgnore]
    private Boolean IsPromotion;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public BigDecimal getItemTotalAmountWithoutTax() {
        return itemTotalAmountWithoutTax;
    }

    public void setItemTotalAmountWithoutTax(BigDecimal itemTotalAmountWithoutTax) {
        this.itemTotalAmountWithoutTax = itemTotalAmountWithoutTax;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(BigDecimal itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public BigDecimal getAdjustmentTaxAmount() {
        return adjustmentTaxAmount;
    }

    public void setAdjustmentTaxAmount(BigDecimal adjustmentTaxAmount) {
        this.adjustmentTaxAmount = adjustmentTaxAmount;
    }

    public boolean getIsIncreaseItem() {
        return isIncreaseItem;
    }

    public void setIsIncreaseItem(boolean isIncreaseItem) {
        this.isIncreaseItem = isIncreaseItem;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public Boolean getPromotion() {
        return IsPromotion;
    }

    public void setPromotion(Boolean promotion) {
        IsPromotion = promotion;
    }
}
