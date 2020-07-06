package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.util.UUID;

public class SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO {
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID expenseItemID;
    private String expenseItemCode;
    private String expenseItemName;
    private BigDecimal soDauky;
    private BigDecimal soPhatSinh;
    private BigDecimal luyKeCuoiKy;
    private String soDauKyString;
    private String soPhatSinhString;
    private String luyKeCuoiKyString;
    private String totalsoDauKy;
    private String totalSoPhatSinh;
    private String totalLuyKeCuoiKy;
    private String linkRef;

    public SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO() {
    }

    public SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO(UUID costSetID, String costSetCode, String costSetName, UUID expenseItemID, String expenseItemCode, String expenseItemName, BigDecimal soDauky, BigDecimal soPhatSinh, BigDecimal luyKeCuoiKy) {
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.expenseItemName = expenseItemName;
        this.soDauky = soDauky;
        this.soPhatSinh = soPhatSinh;
        this.luyKeCuoiKy = luyKeCuoiKy;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
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

    public BigDecimal getSoDauky() {
        return soDauky;
    }

    public void setSoDauky(BigDecimal soDauky) {
        this.soDauky = soDauky;
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

    public String getTotalsoDauKy() {
        return totalsoDauKy;
    }

    public void setTotalsoDauKy(String totalsoDauKy) {
        this.totalsoDauKy = totalsoDauKy;
    }

    public String getTotalSoPhatSinh() {
        return totalSoPhatSinh;
    }

    public void setTotalSoPhatSinh(String totalSoPhatSinh) {
        this.totalSoPhatSinh = totalSoPhatSinh;
    }

    public String getTotalLuyKeCuoiKy() {
        return totalLuyKeCuoiKy;
    }

    public void setTotalLuyKeCuoiKy(String totalLuyKeCuoiKy) {
        this.totalLuyKeCuoiKy = totalLuyKeCuoiKy;
    }
}
