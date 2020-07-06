package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.UUID;

public class AdjustInformation_MIV {
    private UUID inv_InvoiceAuth_id;
    private String inv_invoiceIssuedDate;
    private String sovb;
    private String ngayvb;
    private String ghi_chu;
    private String inv_buyerDisplayName;
    private String ma_dt;
    private String inv_buyerLegalName;
    private String inv_buyerAddressLine;
    private String inv_buyerEmail;
    private String inv_buyerBankAccount;
    private String inv_buyerBankName;

    public UUID getInv_InvoiceAuth_id() {
        return inv_InvoiceAuth_id;
    }

    public void setInv_InvoiceAuth_id(UUID inv_InvoiceAuth_id) {
        this.inv_InvoiceAuth_id = inv_InvoiceAuth_id;
    }

    public String getInv_invoiceIssuedDate() {
        return inv_invoiceIssuedDate;
    }

    public void setInv_invoiceIssuedDate(String inv_invoiceIssuedDate) {
        this.inv_invoiceIssuedDate = inv_invoiceIssuedDate;
    }

    public String getSovb() {
        return sovb;
    }

    public void setSovb(String sovb) {
        this.sovb = sovb;
    }

    public String getNgayvb() {
        return ngayvb;
    }

    public void setNgayvb(String ngayvb) {
        this.ngayvb = ngayvb;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public String getInv_buyerDisplayName() {
        return inv_buyerDisplayName;
    }

    public void setInv_buyerDisplayName(String inv_buyerDisplayName) {
        this.inv_buyerDisplayName = inv_buyerDisplayName;
    }

    public String getMa_dt() {
        return ma_dt;
    }

    public void setMa_dt(String ma_dt) {
        this.ma_dt = ma_dt;
    }

    public String getInv_buyerLegalName() {
        return inv_buyerLegalName;
    }

    public void setInv_buyerLegalName(String inv_buyerLegalName) {
        this.inv_buyerLegalName = inv_buyerLegalName;
    }

    public String getInv_buyerAddressLine() {
        return inv_buyerAddressLine;
    }

    public void setInv_buyerAddressLine(String inv_buyerAddressLine) {
        this.inv_buyerAddressLine = inv_buyerAddressLine;
    }

    public String getInv_buyerEmail() {
        return inv_buyerEmail;
    }

    public void setInv_buyerEmail(String inv_buyerEmail) {
        this.inv_buyerEmail = inv_buyerEmail;
    }

    public String getInv_buyerBankAccount() {
        return inv_buyerBankAccount;
    }

    public void setInv_buyerBankAccount(String inv_buyerBankAccount) {
        this.inv_buyerBankAccount = inv_buyerBankAccount;
    }

    public String getInv_buyerBankName() {
        return inv_buyerBankName;
    }

    public void setInv_buyerBankName(String inv_buyerBankName) {
        this.inv_buyerBankName = inv_buyerBankName;
    }
}
