package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.math.BigDecimal;

public class DataDetails_MIV {
    private String stt_rec0;
    private String inv_itemCode;
    private String inv_itemName;
    private String inv_unitCode;
    private String inv_unitName;
    private BigDecimal inv_unitPrice;
    private BigDecimal inv_quantity;
    private BigDecimal inv_TotalAmountWithoutVat;
    private BigDecimal inv_vatAmount;
    private BigDecimal inv_TotalAmount;

    private Boolean inv_promotion;
    private BigDecimal inv_discountPercentage;
    private BigDecimal inv_discountAmount;
    private String ma_thue;

    public String getStt_rec0() {
        return stt_rec0;
    }

    public void setStt_rec0(String stt_rec0) {
        this.stt_rec0 = stt_rec0;
    }

    public String getInv_itemCode() {
        return inv_itemCode;
    }

    public void setInv_itemCode(String inv_itemCode) {
        this.inv_itemCode = inv_itemCode;
    }

    public String getInv_itemName() {
        return inv_itemName;
    }

    public void setInv_itemName(String inv_itemName) {
        this.inv_itemName = inv_itemName;
    }

    public String getInv_unitCode() {
        return inv_unitCode;
    }

    public void setInv_unitCode(String inv_unitCode) {
        this.inv_unitCode = inv_unitCode;
    }

    public String getInv_unitName() {
        return inv_unitName;
    }

    public void setInv_unitName(String inv_unitName) {
        this.inv_unitName = inv_unitName;
    }

    public BigDecimal getInv_unitPrice() {
        return inv_unitPrice;
    }

    public void setInv_unitPrice(BigDecimal inv_unitPrice) {
        this.inv_unitPrice = inv_unitPrice;
    }

    public BigDecimal getInv_quantity() {
        return inv_quantity;
    }

    public void setInv_quantity(BigDecimal inv_quantity) {
        this.inv_quantity = inv_quantity;
    }

    public BigDecimal getInv_TotalAmountWithoutVat() {
        return inv_TotalAmountWithoutVat;
    }

    public void setInv_TotalAmountWithoutVat(BigDecimal inv_TotalAmountWithoutVat) {
        this.inv_TotalAmountWithoutVat = inv_TotalAmountWithoutVat;
    }

    public BigDecimal getInv_vatAmount() {
        return inv_vatAmount;
    }

    public void setInv_vatAmount(BigDecimal inv_vatAmount) {
        this.inv_vatAmount = inv_vatAmount;
    }

    public BigDecimal getInv_TotalAmount() {
        return inv_TotalAmount;
    }

    public void setInv_TotalAmount(BigDecimal inv_TotalAmount) {
        this.inv_TotalAmount = inv_TotalAmount;
    }

    public Boolean getInv_promotion() {
        return inv_promotion;
    }

    public void setInv_promotion(Boolean inv_promotion) {
        this.inv_promotion = inv_promotion;
    }

    public BigDecimal getInv_discountPercentage() {
        return inv_discountPercentage;
    }

    public void setInv_discountPercentage(BigDecimal inv_discountPercentage) {
        this.inv_discountPercentage = inv_discountPercentage;
    }

    public BigDecimal getInv_discountAmount() {
        return inv_discountAmount;
    }

    public void setInv_discountAmount(BigDecimal inv_discountAmount) {
        this.inv_discountAmount = inv_discountAmount;
    }

    public String getMa_thue() {
        return ma_thue;
    }

    public void setMa_thue(String ma_thue) {
        this.ma_thue = ma_thue;
    }
}
