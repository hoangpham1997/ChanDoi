package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoChiTietBanHangDTO {
    private LocalDate ngayCTu;
    private String ngayCTuString;
    private LocalDate ngayHachToan;
    private String ngayHachToanString;
    private String soHieu;
    private LocalDate ngayHoaDon;
    private String ngayHoaDonString;
    private String soHoaDon;
    private String dienGiai;
    private String tkDoiUng;
    private String dvt;
    private BigDecimal soLuong;
    private String soLuongString;
    private BigDecimal donGia;
    private String donGiaString;
    private BigDecimal thanhTien;
    private String thanhTienString;
    private BigDecimal thue;
    private String thueString;
    private BigDecimal khac;
    private String khacString;
    private String materialGoodsName;
    private String materialGoodsCode;
    private UUID materialGoodsID;
    private UUID refID;
    private Integer typeID;
    private BigDecimal giaVon;
    private String giaVonString;
    private BigDecimal tongSoLuong;
    private String tongSoLuongString;
    private BigDecimal tongThanhTien;
    private String tongThanhTienString;
    private BigDecimal tongThue;
    private String tongThueString;
    private BigDecimal tongKhac;
    private String tongKhacString;
    private BigDecimal doanhThuThuan;
    private String doanhThuThuanString;
    private BigDecimal laiGop;
    private String laiGopString;
    public String linkRef;

    public SoChiTietBanHangDTO() {
    }

    public SoChiTietBanHangDTO(String materialGoodsName, String materialGoodsCode) {
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsCode = materialGoodsCode;
    }

    public SoChiTietBanHangDTO(LocalDate ngayCTu, LocalDate ngayHachToan, String soHieu, LocalDate ngayHoaDon, String soHoaDon, String dienGiai, String tkDoiUng, String dvt, BigDecimal soLuong, BigDecimal donGia, BigDecimal thanhTien, BigDecimal thue, BigDecimal khac, String materialGoodsName, String materialGoodsCode, UUID materialGoodsID, UUID refID, Integer typeID, BigDecimal giaVon, BigDecimal tongSoLuong, BigDecimal tongThanhTien, BigDecimal tongThue, BigDecimal tongKhac, BigDecimal doanhThuThuan, BigDecimal laiGop) {
        this.ngayCTu = ngayCTu;
        this.ngayHachToan = ngayHachToan;
        this.soHieu = soHieu;
        this.ngayHoaDon = ngayHoaDon;
        this.soHoaDon = soHoaDon;
        this.dienGiai = dienGiai;
        this.tkDoiUng = tkDoiUng;
        this.dvt = dvt;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.thue = thue;
        this.khac = khac;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsID = materialGoodsID;
        this.refID = refID;
        this.typeID = typeID;
        this.giaVon = giaVon;
        this.tongSoLuong = tongSoLuong;
        this.tongThanhTien = tongThanhTien;
        this.tongThue = tongThue;
        this.tongKhac = tongKhac;
        this.doanhThuThuan = doanhThuThuan;
        this.laiGop = laiGop;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public BigDecimal getDoanhThuThuan() {
        return doanhThuThuan;
    }

    public void setDoanhThuThuan(BigDecimal doanhThuThuan) {
        this.doanhThuThuan = doanhThuThuan;
    }

    public BigDecimal getLaiGop() {
        return laiGop;
    }

    public void setLaiGop(BigDecimal laiGop) {
        this.laiGop = laiGop;
    }

    public String getDoanhThuThuanString() {
        return doanhThuThuanString;
    }

    public void setDoanhThuThuanString(String doanhThuThuanString) {
        this.doanhThuThuanString = doanhThuThuanString;
    }

    public String getLaiGopString() {
        return laiGopString;
    }

    public void setLaiGopString(String laiGopString) {
        this.laiGopString = laiGopString;
    }

    public String getTongSoLuongString() {
        return tongSoLuongString;
    }

    public void setTongSoLuongString(String tongSoLuongString) {
        this.tongSoLuongString = tongSoLuongString;
    }

    public String getTongThanhTienString() {
        return tongThanhTienString;
    }

    public void setTongThanhTienString(String tongThanhTienString) {
        this.tongThanhTienString = tongThanhTienString;
    }

    public String getTongThueString() {
        return tongThueString;
    }

    public void setTongThueString(String tongThueString) {
        this.tongThueString = tongThueString;
    }

    public String getTongKhacString() {
        return tongKhacString;
    }

    public void setTongKhacString(String tongKhacString) {
        this.tongKhacString = tongKhacString;
    }

    public BigDecimal getTongSoLuong() {
        return tongSoLuong;
    }

    public void setTongSoLuong(BigDecimal tongSoLuong) {
        this.tongSoLuong = tongSoLuong;
    }

    public BigDecimal getTongThanhTien() {
        return tongThanhTien;
    }

    public void setTongThanhTien(BigDecimal tongThanhTien) {
        this.tongThanhTien = tongThanhTien;
    }

    public BigDecimal getTongThue() {
        return tongThue;
    }

    public void setTongThue(BigDecimal tongThue) {
        this.tongThue = tongThue;
    }

    public BigDecimal getTongKhac() {
        return tongKhac;
    }

    public void setTongKhac(BigDecimal tongKhac) {
        this.tongKhac = tongKhac;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public BigDecimal getGiaVon() {
        return giaVon;
    }

    public void setGiaVon(BigDecimal giaVon) {
        this.giaVon = giaVon;
    }

    public String getGiaVonString() {
        return giaVonString;
    }

    public void setGiaVonString(String giaVonString) {
        this.giaVonString = giaVonString;
    }

    public LocalDate getNgayCTu() {
        return ngayCTu;
    }

    public void setNgayCTu(LocalDate ngayCTu) {
        this.ngayCTu = ngayCTu;
    }

    public LocalDate getNgayHachToan() {
        return ngayHachToan;
    }

    public void setNgayHachToan(LocalDate ngayHachToan) {
        this.ngayHachToan = ngayHachToan;
    }

    public String getSoHieu() {
        return soHieu;
    }

    public void setSoHieu(String soHieu) {
        this.soHieu = soHieu;
    }

    public LocalDate getNgayHoaDon() {
        return ngayHoaDon;
    }

    public void setNgayHoaDon(LocalDate ngayHoaDon) {
        this.ngayHoaDon = ngayHoaDon;
    }

    public String getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(String soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public String getDienGiai() {
        return dienGiai;
    }

    public void setDienGiai(String dienGiai) {
        this.dienGiai = dienGiai;
    }

    public String getTkDoiUng() {
        return tkDoiUng;
    }

    public void setTkDoiUng(String tkDoiUng) {
        this.tkDoiUng = tkDoiUng;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public BigDecimal getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(BigDecimal soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }

    public BigDecimal getThue() {
        return thue;
    }

    public void setThue(BigDecimal thue) {
        this.thue = thue;
    }

    public BigDecimal getKhac() {
        return khac;
    }

    public void setKhac(BigDecimal khac) {
        this.khac = khac;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
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

    public String getNgayCTuString() {
        return ngayCTuString;
    }

    public void setNgayCTuString(String ngayCTuString) {
        this.ngayCTuString = ngayCTuString;
    }

    public String getNgayHachToanString() {
        return ngayHachToanString;
    }

    public void setNgayHachToanString(String ngayHachToanString) {
        this.ngayHachToanString = ngayHachToanString;
    }

    public String getNgayHoaDonString() {
        return ngayHoaDonString;
    }

    public void setNgayHoaDonString(String ngayHoaDonString) {
        this.ngayHoaDonString = ngayHoaDonString;
    }

    public String getSoLuongString() {
        return soLuongString;
    }

    public void setSoLuongString(String soLuongString) {
        this.soLuongString = soLuongString;
    }

    public String getDonGiaString() {
        return donGiaString;
    }

    public void setDonGiaString(String donGiaString) {
        this.donGiaString = donGiaString;
    }

    public String getThanhTienString() {
        return thanhTienString;
    }

    public void setThanhTienString(String thanhTienString) {
        this.thanhTienString = thanhTienString;
    }

    public String getThueString() {
        return thueString;
    }

    public void setThueString(String thueString) {
        this.thueString = thueString;
    }

    public String getKhacString() {
        return khacString;
    }

    public void setKhacString(String khacString) {
        this.khacString = khacString;
    }
}
