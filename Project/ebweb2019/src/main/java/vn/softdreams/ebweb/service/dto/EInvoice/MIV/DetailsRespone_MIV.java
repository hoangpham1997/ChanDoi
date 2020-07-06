package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.UUID;

public class DetailsRespone_MIV {
    private UUID inv_InvoiceAuth_id;
    private String inv_invoiceType;
    private UUID inv_InvoiceCode_id;

    private String inv_invoiceSeries;
    private String inv_invoiceNumber;
    private String inv_invoiceName;
    private String inv_invoiceIssuedDate;
    private String inv_submittedDate;

    private String inv_contractNumber;
    private String inv_contractDate;
    private String inv_currencyCode;
    private String inv_exchangeRate;
    private String inv_invoiceNote;
    private Integer inv_adjustmentType;
    private String ten_trang_thai;
    private String id;
    private Integer trang_thai_hd;
    private String trang_thai;

    public UUID getInv_InvoiceAuth_id() {
        return inv_InvoiceAuth_id;
    }

    public void setInv_InvoiceAuth_id(UUID inv_InvoiceAuth_id) {
        this.inv_InvoiceAuth_id = inv_InvoiceAuth_id;
    }

    public String getInv_invoiceType() {
        return inv_invoiceType;
    }

    public void setInv_invoiceType(String inv_invoiceType) {
        this.inv_invoiceType = inv_invoiceType;
    }

    public UUID getInv_InvoiceCode_id() {
        return inv_InvoiceCode_id;
    }

    public void setInv_InvoiceCode_id(UUID inv_InvoiceCode_id) {
        this.inv_InvoiceCode_id = inv_InvoiceCode_id;
    }

    public String getInv_invoiceSeries() {
        return inv_invoiceSeries;
    }

    public void setInv_invoiceSeries(String inv_invoiceSeries) {
        this.inv_invoiceSeries = inv_invoiceSeries;
    }

    public String getInv_invoiceNumber() {
        return inv_invoiceNumber;
    }

    public void setInv_invoiceNumber(String inv_invoiceNumber) {
        this.inv_invoiceNumber = inv_invoiceNumber;
    }

    public String getInv_invoiceName() {
        return inv_invoiceName;
    }

    public void setInv_invoiceName(String inv_invoiceName) {
        this.inv_invoiceName = inv_invoiceName;
    }

    public String getInv_invoiceIssuedDate() {
        return inv_invoiceIssuedDate;
    }

    public void setInv_invoiceIssuedDate(String inv_invoiceIssuedDate) {
        this.inv_invoiceIssuedDate = inv_invoiceIssuedDate;
    }

    public String getInv_submittedDate() {
        return inv_submittedDate;
    }

    public void setInv_submittedDate(String inv_submittedDate) {
        this.inv_submittedDate = inv_submittedDate;
    }

    public String getInv_contractNumber() {
        return inv_contractNumber;
    }

    public void setInv_contractNumber(String inv_contractNumber) {
        this.inv_contractNumber = inv_contractNumber;
    }

    public String getInv_contractDate() {
        return inv_contractDate;
    }

    public void setInv_contractDate(String inv_contractDate) {
        this.inv_contractDate = inv_contractDate;
    }

    public String getInv_currencyCode() {
        return inv_currencyCode;
    }

    public void setInv_currencyCode(String inv_currencyCode) {
        this.inv_currencyCode = inv_currencyCode;
    }

    public String getInv_exchangeRate() {
        return inv_exchangeRate;
    }

    public void setInv_exchangeRate(String inv_exchangeRate) {
        this.inv_exchangeRate = inv_exchangeRate;
    }

    public String getInv_invoiceNote() {
        return inv_invoiceNote;
    }

    public void setInv_invoiceNote(String inv_invoiceNote) {
        this.inv_invoiceNote = inv_invoiceNote;
    }

    public Integer getInv_adjustmentType() {
        return inv_adjustmentType;
    }

    public void setInv_adjustmentType(Integer inv_adjustmentType) {
        this.inv_adjustmentType = inv_adjustmentType;
    }

    public String getTen_trang_thai() {
        return ten_trang_thai;
    }

    public void setTen_trang_thai(String ten_trang_thai) {
        this.ten_trang_thai = ten_trang_thai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTrang_thai_hd() {
        return trang_thai_hd;
    }

    public void setTrang_thai_hd(Integer trang_thai_hd) {
        this.trang_thai_hd = trang_thai_hd;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }
}
