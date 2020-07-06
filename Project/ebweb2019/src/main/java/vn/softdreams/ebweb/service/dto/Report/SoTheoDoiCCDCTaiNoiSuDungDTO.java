package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheoDoiCCDCTaiNoiSuDungDTO {
    private UUID iDPhongBan; // id phong ban
    private String maPhongBan; // ma phong ban
    private String tenPhongBan; // ten pb
    private String soChungTuGhiTang; // soChungTuGhiTang
    private LocalDate ngayChungTuGhiTang; // NgayChungTuGhiTang
    private String toolsName;
    private String unitName;
    private Integer incrementQuantity;
    private BigDecimal unitPrice;
    private BigDecimal incrementAmount;
    private String soChungTuGhiGiam;
    private LocalDate ngayChungTuGhiGiam;
    private String reason;
    private Integer decrementQuantity;
    private BigDecimal decrementAmount;

    private String ngayChungTuGhiTangString;
    private String incrementQuantityString;
    private String unitPriceString;
    private String incrementAmountString;
    private String ngayChungTuGhiGiamString;
    private String decrementQuantityString;
    private String decrementAmountString;

    public SoTheoDoiCCDCTaiNoiSuDungDTO() {
    }

    public SoTheoDoiCCDCTaiNoiSuDungDTO(UUID iDPhongBan, String maPhongBan, String tenPhongBan, String soChungTuGhiTang, LocalDate ngayChungTuGhiTang, String toolsName, String unitName, Integer incrementQuantity, BigDecimal unitPrice, BigDecimal incrementAmount, String soChungTuGhiGiam, LocalDate ngayChungTuGhiGiam, String reason, Integer decrementQuantity, BigDecimal decrementAmount) {
        this.iDPhongBan = iDPhongBan;
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.soChungTuGhiTang = soChungTuGhiTang;
        this.ngayChungTuGhiTang = ngayChungTuGhiTang;
        this.toolsName = toolsName;
        this.unitName = unitName;
        this.incrementQuantity = incrementQuantity;
        this.unitPrice = unitPrice;
        this.incrementAmount = incrementAmount;
        this.soChungTuGhiGiam = soChungTuGhiGiam;
        this.ngayChungTuGhiGiam = ngayChungTuGhiGiam;
        this.reason = reason;
        this.decrementQuantity = decrementQuantity;
        this.decrementAmount = decrementAmount;
    }

    public String getNgayChungTuGhiTangString() {
        return ngayChungTuGhiTangString;
    }

    public void setNgayChungTuGhiTangString(String ngayChungTuGhiTangString) {
        this.ngayChungTuGhiTangString = ngayChungTuGhiTangString;
    }

    public String getIncrementQuantityString() {
        return incrementQuantityString;
    }

    public void setIncrementQuantityString(String incrementQuantityString) {
        this.incrementQuantityString = incrementQuantityString;
    }

    public String getUnitPriceString() {
        return unitPriceString;
    }

    public void setUnitPriceString(String unitPriceString) {
        this.unitPriceString = unitPriceString;
    }

    public String getIncrementAmountString() {
        return incrementAmountString;
    }

    public void setIncrementAmountString(String incrementAmountString) {
        this.incrementAmountString = incrementAmountString;
    }

    public String getNgayChungTuGhiGiamString() {
        return ngayChungTuGhiGiamString;
    }

    public void setNgayChungTuGhiGiamString(String ngayChungTuGhiGiamString) {
        this.ngayChungTuGhiGiamString = ngayChungTuGhiGiamString;
    }

    public String getDecrementQuantityString() {
        return decrementQuantityString;
    }

    public void setDecrementQuantityString(String decrementQuantityString) {
        this.decrementQuantityString = decrementQuantityString;
    }

    public String getDecrementAmountString() {
        return decrementAmountString;
    }

    public void setDecrementAmountString(String decrementAmountString) {
        this.decrementAmountString = decrementAmountString;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getSoChungTuGhiTang() {
        return soChungTuGhiTang;
    }

    public void setSoChungTuGhiTang(String soChungTuGhiTang) {
        this.soChungTuGhiTang = soChungTuGhiTang;
    }

    public LocalDate getNgayChungTuGhiTang() {
        return ngayChungTuGhiTang;
    }

    public void setNgayChungTuGhiTang(LocalDate ngayChungTuGhiTang) {
        this.ngayChungTuGhiTang = ngayChungTuGhiTang;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
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

    public BigDecimal getIncrementAmount() {
        return incrementAmount;
    }

    public void setIncrementAmount(BigDecimal incrementAmount) {
        this.incrementAmount = incrementAmount;
    }

    public String getSoChungTuGhiGiam() {
        return soChungTuGhiGiam;
    }

    public void setSoChungTuGhiGiam(String soChungTuGhiGiam) {
        this.soChungTuGhiGiam = soChungTuGhiGiam;
    }

    public LocalDate getNgayChungTuGhiGiam() {
        return ngayChungTuGhiGiam;
    }

    public void setNgayChungTuGhiGiam(LocalDate ngayChungTuGhiGiam) {
        this.ngayChungTuGhiGiam = ngayChungTuGhiGiam;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getDecrementAmount() {
        return decrementAmount;
    }

    public void setDecrementAmount(BigDecimal decrementAmount) {
        this.decrementAmount = decrementAmount;
    }

    public Integer getIncrementQuantity() {
        return incrementQuantity;
    }

    public void setIncrementQuantity(Integer incrementQuantity) {
        this.incrementQuantity = incrementQuantity;
    }

    public Integer getDecrementQuantity() {
        return decrementQuantity;
    }

    public void setDecrementQuantity(Integer decrementQuantity) {
        this.decrementQuantity = decrementQuantity;
    }

    public UUID getiDPhongBan() {
        return iDPhongBan;
    }

    public void setiDPhongBan(UUID iDPhongBan) {
        this.iDPhongBan = iDPhongBan;
    }
}
