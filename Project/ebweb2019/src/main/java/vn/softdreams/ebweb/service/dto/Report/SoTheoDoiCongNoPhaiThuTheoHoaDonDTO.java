package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheoDoiCongNoPhaiThuTheoHoaDonDTO {
    private UUID sAInvoiceID;
    private Integer refType;
    private UUID refID;
    private LocalDate invoiceDate;
    private String invoiceNo;
    private String accountingObjectName;
    private BigDecimal giaTriHoaDon;
    private BigDecimal traLai;
    private BigDecimal giamGia;
    private BigDecimal chietKhauTT_GiamTruKhac;
    private BigDecimal soDaThu;
    private BigDecimal soConPhaiThu;
    public String linkRef;

    private String invoiceDateString;
    private String giaTriHoaDonString;
    private String traLaiString;
    private String giamGiaString;
    private String chietKhauTT_GiamTruKhacString;
    private String soDaThuString;
    private String soConPhaiThuString;

    public SoTheoDoiCongNoPhaiThuTheoHoaDonDTO() {
    }

    public SoTheoDoiCongNoPhaiThuTheoHoaDonDTO(UUID sAInvoiceID, Integer refType, UUID refID, LocalDate invoiceDate, String invoiceNo, String accountingObjectName, BigDecimal giaTriHoaDon, BigDecimal traLai, BigDecimal giamGia, BigDecimal chietKhauTT_GiamTruKhac, BigDecimal soDaThu, BigDecimal soConPhaiThu) {
        this.sAInvoiceID = sAInvoiceID;
        this.refType = refType;
        this.refID = refID;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.accountingObjectName = accountingObjectName;
        this.giaTriHoaDon = giaTriHoaDon;
        this.traLai = traLai;
        this.giamGia = giamGia;
        this.chietKhauTT_GiamTruKhac = chietKhauTT_GiamTruKhac;
        this.soDaThu = soDaThu;
        this.soConPhaiThu = soConPhaiThu;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public UUID getsAInvoiceID() {
        return sAInvoiceID;
    }

    public void setsAInvoiceID(UUID sAInvoiceID) {
        this.sAInvoiceID = sAInvoiceID;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public BigDecimal getGiaTriHoaDon() {
        return giaTriHoaDon;
    }

    public void setGiaTriHoaDon(BigDecimal giaTriHoaDon) {
        this.giaTriHoaDon = giaTriHoaDon;
    }

    public BigDecimal getTraLai() {
        return traLai;
    }

    public void setTraLai(BigDecimal traLai) {
        this.traLai = traLai;
    }

    public BigDecimal getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(BigDecimal giamGia) {
        this.giamGia = giamGia;
    }

    public BigDecimal getChietKhauTT_GiamTruKhac() {
        return chietKhauTT_GiamTruKhac;
    }

    public void setChietKhauTT_GiamTruKhac(BigDecimal chietKhauTT_GiamTruKhac) {
        this.chietKhauTT_GiamTruKhac = chietKhauTT_GiamTruKhac;
    }

    public BigDecimal getSoDaThu() {
        return soDaThu;
    }

    public void setSoDaThu(BigDecimal soDaThu) {
        this.soDaThu = soDaThu;
    }

    public BigDecimal getSoConPhaiThu() {
        return soConPhaiThu;
    }

    public void setSoConPhaiThu(BigDecimal soConPhaiThu) {
        this.soConPhaiThu = soConPhaiThu;
    }

    public String getGiaTriHoaDonString() {
        return giaTriHoaDonString;
    }

    public void setGiaTriHoaDonString(String giaTriHoaDonString) {
        this.giaTriHoaDonString = giaTriHoaDonString;
    }

    public String getTraLaiString() {
        return traLaiString;
    }

    public void setTraLaiString(String traLaiString) {
        this.traLaiString = traLaiString;
    }

    public String getGiamGiaString() {
        return giamGiaString;
    }

    public void setGiamGiaString(String giamGiaString) {
        this.giamGiaString = giamGiaString;
    }

    public String getChietKhauTT_GiamTruKhacString() {
        return chietKhauTT_GiamTruKhacString;
    }

    public void setChietKhauTT_GiamTruKhacString(String chietKhauTT_GiamTruKhacString) {
        this.chietKhauTT_GiamTruKhacString = chietKhauTT_GiamTruKhacString;
    }

    public String getSoDaThuString() {
        return soDaThuString;
    }

    public void setSoDaThuString(String soDaThuString) {
        this.soDaThuString = soDaThuString;
    }

    public String getSoConPhaiThuString() {
        return soConPhaiThuString;
    }

    public void setSoConPhaiThuString(String soConPhaiThuString) {
        this.soConPhaiThuString = soConPhaiThuString;
    }

    public String getInvoiceDateString() {
        return invoiceDateString;
    }

    public void setInvoiceDateString(String invoiceDateString) {
        this.invoiceDateString = invoiceDateString;
    }
}
