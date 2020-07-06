package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoNhatKyMuaHangDTO {
    private LocalDate ngayGhiSo;
    private String ngayGhiSoString;
    private String soCTu;
    private LocalDate ngayCTu;
    private String ngayCTuString;
    private String dienGiai;
    private BigDecimal hangHoa;
    private String hangHoaString;
    private BigDecimal phaiTraNguoiBan;
    private String phaiTraNguoiBanString;
    private BigDecimal nguyenVatLieu;
    private String nguyenVatLieuString;
    private BigDecimal soTien;
    private String soTienString;
    private String account;
    public UUID referenceID;    // id chung tu
    public Integer typeID;    // loai chung tu
    public String linkRef;

    public SoNhatKyMuaHangDTO() {
    }

    public SoNhatKyMuaHangDTO(LocalDate ngayGhiSo, String soCTu, LocalDate ngayCTu, String dienGiai, BigDecimal hangHoa, BigDecimal phaiTraNguoiBan, BigDecimal nguyenVatLieu, BigDecimal soTien, String account, UUID referenceID, Integer typeID) {
        this.ngayGhiSo = ngayGhiSo;
        this.soCTu = soCTu;
        this.ngayCTu = ngayCTu;
        this.dienGiai = dienGiai;
        this.hangHoa = hangHoa;
        this.phaiTraNguoiBan = phaiTraNguoiBan;
        this.nguyenVatLieu = nguyenVatLieu;
        this.soTien = soTien;
        this.account = account;
        this.referenceID = referenceID;
        this.typeID = typeID;
    }

    public LocalDate getNgayGhiSo() {
        return ngayGhiSo;
    }

    public void setNgayGhiSo(LocalDate ngayGhiSo) {
        this.ngayGhiSo = ngayGhiSo;
    }

    public String getSoCTu() {
        return soCTu;
    }

    public void setSoCTu(String soCTu) {
        this.soCTu = soCTu;
    }

    public LocalDate getNgayCTu() {
        return ngayCTu;
    }

    public void setNgayCTu(LocalDate ngayCTu) {
        this.ngayCTu = ngayCTu;
    }

    public String getDienGiai() {
        return dienGiai;
    }

    public void setDienGiai(String dienGiai) {
        this.dienGiai = dienGiai;
    }

    public BigDecimal getHangHoa() {
        return hangHoa;
    }

    public void setHangHoa(BigDecimal hangHoa) {
        this.hangHoa = hangHoa;
    }

    public BigDecimal getPhaiTraNguoiBan() {
        return phaiTraNguoiBan;
    }

    public void setPhaiTraNguoiBan(BigDecimal phaiTraNguoiBan) {
        this.phaiTraNguoiBan = phaiTraNguoiBan;
    }

    public BigDecimal getNguyenVatLieu() {
        return nguyenVatLieu;
    }

    public void setNguyenVatLieu(BigDecimal nguyenVatLieu) {
        this.nguyenVatLieu = nguyenVatLieu;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNgayGhiSoString() {
        return ngayGhiSoString;
    }

    public void setNgayGhiSoString(String ngayGhiSoString) {
        this.ngayGhiSoString = ngayGhiSoString;
    }

    public String getNgayCTuString() {
        return ngayCTuString;
    }

    public void setNgayCTuString(String ngayCTuString) {
        this.ngayCTuString = ngayCTuString;
    }

    public String getHangHoaString() {
        return hangHoaString;
    }

    public void setHangHoaString(String hangHoaString) {
        this.hangHoaString = hangHoaString;
    }

    public String getPhaiTraNguoiBanString() {
        return phaiTraNguoiBanString;
    }

    public void setPhaiTraNguoiBanString(String phaiTraNguoiBanString) {
        this.phaiTraNguoiBanString = phaiTraNguoiBanString;
    }

    public String getNguyenVatLieuString() {
        return nguyenVatLieuString;
    }

    public void setNguyenVatLieuString(String nguyenVatLieuString) {
        this.nguyenVatLieuString = nguyenVatLieuString;
    }

    public String getSoTienString() {
        return soTienString;
    }

    public void setSoTienString(String soTienString) {
        this.soTienString = soTienString;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}
