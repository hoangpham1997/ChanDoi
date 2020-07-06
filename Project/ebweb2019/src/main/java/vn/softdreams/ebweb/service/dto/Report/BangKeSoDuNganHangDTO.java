package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BangKeSoDuNganHangDTO {
    private Integer rowNum;
    private UUID BankAccountDetailID;
    private String TaiKhoanNganHang;
    private String TenNganHang;
    private String ChiNhanh;
    private BigDecimal SoDuDauKy;
    private String SoDuDauKyToString;
    private BigDecimal PhatSinhNo;
    private String PhatSinhNoToString;
    private BigDecimal PhatSinhCo;
    private String PhatSinhCoToString;
    private BigDecimal SoDuCuoiKy;
    private String SoDuCuoiKyToString;

    public BangKeSoDuNganHangDTO(Integer rowNum, UUID bankAccountDetailID, String taiKhoanNganHang, String tenNganHang, String chiNhanh, BigDecimal soDuDauKy, BigDecimal phatSinhNo, BigDecimal phatSinhCo, BigDecimal soDuCuoiKy) {
        this.rowNum = rowNum;
        BankAccountDetailID = bankAccountDetailID;
        TaiKhoanNganHang = taiKhoanNganHang;
        TenNganHang = tenNganHang;
        ChiNhanh = chiNhanh;
        SoDuDauKy = soDuDauKy;
        PhatSinhNo = phatSinhNo;
        PhatSinhCo = phatSinhCo;
        SoDuCuoiKy = soDuCuoiKy;
    }

    public BangKeSoDuNganHangDTO() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public UUID getBankAccountDetailID() {
        return BankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        BankAccountDetailID = bankAccountDetailID;
    }

    public String getTaiKhoanNganHang() {
        return TaiKhoanNganHang;
    }

    public void setTaiKhoanNganHang(String taiKhoanNganHang) {
        TaiKhoanNganHang = taiKhoanNganHang;
    }

    public String getTenNganHang() {
        return TenNganHang;
    }

    public void setTenNganHang(String tenNganHang) {
        TenNganHang = tenNganHang;
    }

    public String getChiNhanh() {
        return ChiNhanh;
    }

    public void setChiNhanh(String chiNhanh) {
        ChiNhanh = chiNhanh;
    }

    public BigDecimal getSoDuDauKy() {
        return SoDuDauKy;
    }

    public void setSoDuDauKy(BigDecimal soDuDauKy) {
        SoDuDauKy = soDuDauKy;
    }

    public BigDecimal getPhatSinhNo() {
        return PhatSinhNo;
    }

    public void setPhatSinhNo(BigDecimal phatSinhNo) {
        PhatSinhNo = phatSinhNo;
    }

    public BigDecimal getPhatSinhCo() {
        return PhatSinhCo;
    }

    public void setPhatSinhCo(BigDecimal phatSinhCo) {
        PhatSinhCo = phatSinhCo;
    }

    public BigDecimal getSoDuCuoiKy() {
        return SoDuCuoiKy;
    }

    public void setSoDuCuoiKy(BigDecimal soDuCuoiKy) {
        SoDuCuoiKy = soDuCuoiKy;
    }

    public String getSoDuDauKyToString() {
        return SoDuDauKyToString;
    }

    public void setSoDuDauKyToString(String soDuDauKyToString) {
        SoDuDauKyToString = soDuDauKyToString;
    }

    public String getPhatSinhNoToString() {
        return PhatSinhNoToString;
    }

    public void setPhatSinhNoToString(String phatSinhNoToString) {
        PhatSinhNoToString = phatSinhNoToString;
    }

    public String getPhatSinhCoToString() {
        return PhatSinhCoToString;
    }

    public void setPhatSinhCoToString(String phatSinhCoToString) {
        PhatSinhCoToString = phatSinhCoToString;
    }

    public String getSoDuCuoiKyToString() {
        return SoDuCuoiKyToString;
    }

    public void setSoDuCuoiKyToString(String soDuCuoiKyToString) {
        SoDuCuoiKyToString = soDuCuoiKyToString;
    }
}

