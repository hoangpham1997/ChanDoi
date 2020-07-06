package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TheoDoiMaThongKeTheoKhoanMucChiPhiDTO {
    private UUID statisticsCodeID;
    private String statisticsCode;
    private String statisticsCodeName;
    private UUID expenseItemID;
    private String expenseItemCode;
    private String expenseItemName;
    private BigDecimal soDauKy;
    private BigDecimal soPhatSinh;
    private BigDecimal luyKeCuoiKy;
    private BigDecimal tongSoDauKy;
    private BigDecimal tongSoPhatSinh;
    private BigDecimal tongLuyKeCuoiKy;
    private String soDauKyString;
    private String soPhatSinhString;
    private String luyKeCuoiKyString;
    private String tongSoDauKyString;
    private String tongSoPhatSinhString;
    private String tongLuyKeCuoiKyString;

    public TheoDoiMaThongKeTheoKhoanMucChiPhiDTO() {
    }

    public TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(String statisticsCode, String statisticsCodeName) {
        this.statisticsCode = statisticsCode;
        this.statisticsCodeName = statisticsCodeName;
    }

    public TheoDoiMaThongKeTheoKhoanMucChiPhiDTO(UUID statisticsCodeID, String statisticsCode, String statisticsCodeName, UUID expenseItemID,
                                                 String expenseItemCode, String expenseItemName, BigDecimal soDauKy,
                                                 BigDecimal soPhatSinh, BigDecimal luyKeCuoiKy, BigDecimal tongSoDauKy, BigDecimal tongSoPhatSinh, BigDecimal tongLuyKeCuoiKy) {
        this.statisticsCodeID = statisticsCodeID;
        this.statisticsCode = statisticsCode;
        this.statisticsCodeName = statisticsCodeName;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.expenseItemName = expenseItemName;
        this.soDauKy = soDauKy;
        this.soPhatSinh = soPhatSinh;
        this.luyKeCuoiKy = luyKeCuoiKy;
        this.tongSoDauKy = tongSoDauKy;
        this.tongSoPhatSinh = tongSoPhatSinh;
        this.tongLuyKeCuoiKy = tongLuyKeCuoiKy;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getStatisticsCodeName() {
        return statisticsCodeName;
    }

    public void setStatisticsCodeName(String statisticsCodeName) {
        this.statisticsCodeName = statisticsCodeName;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getExpenseItemName() {
        return expenseItemName;
    }

    public void setExpenseItemName(String expenseItemName) {
        this.expenseItemName = expenseItemName;
    }

    public BigDecimal getSoDauKy() {
        return soDauKy;
    }

    public void setSoDauKy(BigDecimal soDauKy) {
        this.soDauKy = soDauKy;
    }

    public BigDecimal getSoPhatSinh() {
        return soPhatSinh;
    }

    public void setSoPhatSinh(BigDecimal soPhatSinh) {
        this.soPhatSinh = soPhatSinh;
    }

    public BigDecimal getLuyKeCuoiKy() {
        return luyKeCuoiKy;
    }

    public void setLuyKeCuoiKy(BigDecimal luyKeCuoiKy) {
        this.luyKeCuoiKy = luyKeCuoiKy;
    }

    public BigDecimal getTongSoDauKy() {
        return tongSoDauKy;
    }

    public void setTongSoDauKy(BigDecimal tongSoDauKy) {
        this.tongSoDauKy = tongSoDauKy;
    }

    public BigDecimal getTongSoPhatSinh() {
        return tongSoPhatSinh;
    }

    public void setTongSoPhatSinh(BigDecimal tongSoPhatSinh) {
        this.tongSoPhatSinh = tongSoPhatSinh;
    }

    public BigDecimal getTongLuyKeCuoiKy() {
        return tongLuyKeCuoiKy;
    }

    public void setTongLuyKeCuoiKy(BigDecimal tongLuyKeCuoiKy) {
        this.tongLuyKeCuoiKy = tongLuyKeCuoiKy;
    }

    public String getSoDauKyString() {
        return soDauKyString;
    }

    public void setSoDauKyString(String soDauKyString) {
        this.soDauKyString = soDauKyString;
    }

    public String getSoPhatSinhString() {
        return soPhatSinhString;
    }

    public void setSoPhatSinhString(String soPhatSinhString) {
        this.soPhatSinhString = soPhatSinhString;
    }

    public String getLuyKeCuoiKyString() {
        return luyKeCuoiKyString;
    }

    public void setLuyKeCuoiKyString(String luyKeCuoiKyString) {
        this.luyKeCuoiKyString = luyKeCuoiKyString;
    }

    public String getTongSoDauKyString() {
        return tongSoDauKyString;
    }

    public void setTongSoDauKyString(String tongSoDauKyString) {
        this.tongSoDauKyString = tongSoDauKyString;
    }

    public String getTongSoPhatSinhString() {
        return tongSoPhatSinhString;
    }

    public void setTongSoPhatSinhString(String tongSoPhatSinhString) {
        this.tongSoPhatSinhString = tongSoPhatSinhString;
    }

    public String getTongLuyKeCuoiKyString() {
        return tongLuyKeCuoiKyString;
    }

    public void setTongLuyKeCuoiKyString(String tongLuyKeCuoiKyString) {
        this.tongLuyKeCuoiKyString = tongLuyKeCuoiKyString;
    }
}
