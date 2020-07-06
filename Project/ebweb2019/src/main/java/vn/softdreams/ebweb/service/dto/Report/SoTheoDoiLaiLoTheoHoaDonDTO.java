package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheoDoiLaiLoTheoHoaDonDTO {
    private UUID sAInvoiceID;
    private Integer refType;
    private UUID refID;
    private LocalDate invoiceDate; // ngay hoa don
    private String invoiceNo;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String reason;
    private BigDecimal giaTriHHDV;
    private BigDecimal chietKhau;
    private BigDecimal giamGia;
    private BigDecimal traLai;
    private BigDecimal giaVon;
    private BigDecimal laiLo;

    private String invoiceDateString;
    private String giaTriHHDVString;
    private String chietKhauString;
    private String giamGiaString;
    private String traLaiString;
    private String giaVonString;
    private String laiLoString;
    public String linkRef;

    public SoTheoDoiLaiLoTheoHoaDonDTO() {
    }

    public SoTheoDoiLaiLoTheoHoaDonDTO(UUID sAInvoiceID, Integer refType, UUID refID, LocalDate invoiceDate, String invoiceNo, UUID accountingObjectID, String accountingObjectName, String reason, BigDecimal giaTriHHDV,
                                       BigDecimal chietKhau, BigDecimal giamGia, BigDecimal traLai, BigDecimal giaVon, BigDecimal laiLo) {
        this.sAInvoiceID = sAInvoiceID;
        this.refType = refType;
        this.refID = refID;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.reason = reason;
        this.giaTriHHDV = giaTriHHDV;
        this.chietKhau = chietKhau;
        this.giamGia = giamGia;
        this.traLai = traLai;
        this.giaVon = giaVon;
        this.laiLo = laiLo;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getGiaTriHHDV() {
        return giaTriHHDV;
    }

    public void setGiaTriHHDV(BigDecimal giaTriHHDV) {
        this.giaTriHHDV = giaTriHHDV;
    }

    public BigDecimal getTraLai() {
        return traLai;
    }

    public void setTraLai(BigDecimal traLai) {
        this.traLai = traLai;
    }

    public BigDecimal getGiaVon() {
        return giaVon;
    }

    public void setGiaVon(BigDecimal giaVon) {
        this.giaVon = giaVon;
    }

    public BigDecimal getLaiLo() {
        return laiLo;
    }

    public void setLaiLo(BigDecimal laiLo) {
        this.laiLo = laiLo;
    }

    public String getInvoiceDateString() {
        return invoiceDateString;
    }

    public void setInvoiceDateString(String invoiceDateString) {
        this.invoiceDateString = invoiceDateString;
    }

    public String getGiaTriHHDVString() {
        return giaTriHHDVString;
    }

    public void setGiaTriHHDVString(String giaTriHHDVString) {
        this.giaTriHHDVString = giaTriHHDVString;
    }

    public String getTraLaiString() {
        return traLaiString;
    }

    public void setTraLaiString(String traLaiString) {
        this.traLaiString = traLaiString;
    }

    public String getGiaVonString() {
        return giaVonString;
    }

    public void setGiaVonString(String giaVonString) {
        this.giaVonString = giaVonString;
    }

    public String getLaiLoString() {
        return laiLoString;
    }

    public void setLaiLoString(String laiLoString) {
        this.laiLoString = laiLoString;
    }

    public BigDecimal getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(BigDecimal chietKhau) {
        this.chietKhau = chietKhau;
    }

    public BigDecimal getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(BigDecimal giamGia) {
        this.giamGia = giamGia;
    }

    public String getChietKhauString() {
        return chietKhauString;
    }

    public void setChietKhauString(String chietKhauString) {
        this.chietKhauString = chietKhauString;
    }

    public String getGiamGiaString() {
        return giamGiaString;
    }

    public void setGiamGiaString(String giamGiaString) {
        this.giamGiaString = giamGiaString;
    }
}
