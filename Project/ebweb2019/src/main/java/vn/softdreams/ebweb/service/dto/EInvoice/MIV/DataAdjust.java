package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.math.BigDecimal;

public class DataAdjust {
    private String inv_itemCode;
    private BigDecimal inv_quantity;
    private BigDecimal inv_unitPrice;
    private String ma_thue;
    private BigDecimal nv_discountPercentage;
    private BigDecimal inv_discountAmount;
    private String tieu_thuc;

    public String getInv_itemCode() {
        return inv_itemCode;
    }

    public void setInv_itemCode(String inv_itemCode) {
        this.inv_itemCode = inv_itemCode;
    }

    public BigDecimal getInv_quantity() {
        return inv_quantity;
    }

    public void setInv_quantity(BigDecimal inv_quantity) {
        this.inv_quantity = inv_quantity;
    }

    public BigDecimal getInv_unitPrice() {
        return inv_unitPrice;
    }

    public void setInv_unitPrice(BigDecimal inv_unitPrice) {
        this.inv_unitPrice = inv_unitPrice;
    }

    public String getMa_thue() {
        return ma_thue;
    }

    public void setMa_thue(String ma_thue) {
        this.ma_thue = ma_thue;
    }

    public BigDecimal getNv_discountPercentage() {
        return nv_discountPercentage;
    }

    public void setNv_discountPercentage(BigDecimal nv_discountPercentage) {
        this.nv_discountPercentage = nv_discountPercentage;
    }

    public BigDecimal getInv_discountAmount() {
        return inv_discountAmount;
    }

    public void setInv_discountAmount(BigDecimal inv_discountAmount) {
        this.inv_discountAmount = inv_discountAmount;
    }

    public String getTieu_thuc() {
        return tieu_thuc;
    }

    public void setTieu_thuc(String tieu_thuc) {
        this.tieu_thuc = tieu_thuc;
    }
}
