package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoChiTietMuaHangDTO {
    private String maKH;
    private String tenKH;
    private LocalDate ngayHachToan;
    private String ngayHachToanString;
    private LocalDate ngayCTu;
    private String ngayCTuString;
    private String soCTu;
    private String soHoaDon;
    private LocalDate ngayHoaDon;
    private String ngayHoaDonString;
    private String mahang;
    private String tenhang;
    private String dvt;
    private BigDecimal soLuongMua;
    private String soLuongMuaString;
    private BigDecimal donGia;
    private String donGiaString;
    private BigDecimal giaTriMua;
    private String giaTriMuaString;
    private BigDecimal chietKhau;
    private String chietKhauString;
    private BigDecimal soLuongTraLai;
    private String soLuongTraLaiString;
    private BigDecimal giaTriTraLai;
    private String giaTriTraLaiString;
    private BigDecimal giaTriGiamGia;
    private String giaTriGiamGiaString;
    private UUID refID;
    private Integer typeID;
    private String reason;
    public String linkRef;

    public SoChiTietMuaHangDTO() {
    }

    public SoChiTietMuaHangDTO(String maKH, String tenKH, LocalDate ngayHachToan, LocalDate ngayCTu, String soCTu, String soHoaDon, LocalDate ngayHoaDon, String mahang, String tenhang, String dvt, BigDecimal soLuongMua, BigDecimal donGia, BigDecimal giaTriMua, BigDecimal chietKhau, BigDecimal soLuongTraLai, BigDecimal giaTriTraLai, BigDecimal giaTriGiamGia, UUID refID, Integer typeID) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.ngayHachToan = ngayHachToan;
        this.ngayCTu = ngayCTu;
        this.soCTu = soCTu;
        this.soHoaDon = soHoaDon;
        this.ngayHoaDon = ngayHoaDon;
        this.mahang = mahang;
        this.tenhang = tenhang;
        this.dvt = dvt;
        this.soLuongMua = soLuongMua;
        this.donGia = donGia;
        this.giaTriMua = giaTriMua;
        this.chietKhau = chietKhau;
        this.soLuongTraLai = soLuongTraLai;
        this.giaTriTraLai = giaTriTraLai;
        this.giaTriGiamGia = giaTriGiamGia;
        this.refID = refID;
        this.typeID = typeID;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public LocalDate getNgayHachToan() {
        return ngayHachToan;
    }

    public void setNgayHachToan(LocalDate ngayHachToan) {
        this.ngayHachToan = ngayHachToan;
    }

    public LocalDate getNgayCTu() {
        return ngayCTu;
    }

    public void setNgayCTu(LocalDate ngayCTu) {
        this.ngayCTu = ngayCTu;
    }

    public String getSoCTu() {
        return soCTu;
    }

    public void setSoCTu(String soCTu) {
        this.soCTu = soCTu;
    }

    public String getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(String soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public LocalDate getNgayHoaDon() {
        return ngayHoaDon;
    }

    public void setNgayHoaDon(LocalDate ngayHoaDon) {
        this.ngayHoaDon = ngayHoaDon;
    }

    public String getMahang() {
        return mahang;
    }

    public void setMahang(String mahang) {
        this.mahang = mahang;
    }

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public BigDecimal getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(BigDecimal soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getGiaTriMua() {
        return giaTriMua;
    }

    public void setGiaTriMua(BigDecimal giaTriMua) {
        this.giaTriMua = giaTriMua;
    }

    public BigDecimal getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(BigDecimal chietKhau) {
        this.chietKhau = chietKhau;
    }

    public BigDecimal getSoLuongTraLai() {
        return soLuongTraLai;
    }

    public void setSoLuongTraLai(BigDecimal soLuongTraLai) {
        this.soLuongTraLai = soLuongTraLai;
    }

    public BigDecimal getGiaTriTraLai() {
        return giaTriTraLai;
    }

    public void setGiaTriTraLai(BigDecimal giaTriTraLai) {
        this.giaTriTraLai = giaTriTraLai;
    }

    public BigDecimal getGiaTriGiamGia() {
        return giaTriGiamGia;
    }

    public void setGiaTriGiamGia(BigDecimal giaTriGiamGia) {
        this.giaTriGiamGia = giaTriGiamGia;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getNgayHachToanString() {
        return ngayHachToanString;
    }

    public void setNgayHachToanString(String ngayHachToanString) {
        this.ngayHachToanString = ngayHachToanString;
    }

    public String getNgayCTuString() {
        return ngayCTuString;
    }

    public void setNgayCTuString(String ngayCTuString) {
        this.ngayCTuString = ngayCTuString;
    }

    public String getNgayHoaDonString() {
        return ngayHoaDonString;
    }

    public void setNgayHoaDonString(String ngayHoaDonString) {
        this.ngayHoaDonString = ngayHoaDonString;
    }

    public String getSoLuongMuaString() {
        return soLuongMuaString;
    }

    public void setSoLuongMuaString(String soLuongMuaString) {
        this.soLuongMuaString = soLuongMuaString;
    }

    public String getDonGiaString() {
        return donGiaString;
    }

    public void setDonGiaString(String donGiaString) {
        this.donGiaString = donGiaString;
    }

    public String getGiaTriMuaString() {
        return giaTriMuaString;
    }

    public void setGiaTriMuaString(String giaTriMuaString) {
        this.giaTriMuaString = giaTriMuaString;
    }

    public String getChietKhauString() {
        return chietKhauString;
    }

    public void setChietKhauString(String chietKhauString) {
        this.chietKhauString = chietKhauString;
    }

    public String getSoLuongTraLaiString() {
        return soLuongTraLaiString;
    }

    public void setSoLuongTraLaiString(String soLuongTraLaiString) {
        this.soLuongTraLaiString = soLuongTraLaiString;
    }

    public String getGiaTriTraLaiString() {
        return giaTriTraLaiString;
    }

    public void setGiaTriTraLaiString(String giaTriTraLaiString) {
        this.giaTriTraLaiString = giaTriTraLaiString;
    }

    public String getGiaTriGiamGiaString() {
        return giaTriGiamGiaString;
    }

    public void setGiaTriGiamGiaString(String giaTriGiamGiaString) {
        this.giaTriGiamGiaString = giaTriGiamGiaString;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
