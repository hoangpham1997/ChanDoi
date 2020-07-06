package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataInvoice_MIV {
    private String inv_invoiceSeries;
    private String inv_invoiceNumber;
    private String inv_invoiceIssuedDate;
    private String inv_currencyCode;
    private BigDecimal inv_exchangeRate;
    private String inv_buyerDisplayName;
    private String ma_dt;
    private String inv_buyerLegalName;
    private String inv_buyerTaxCode;
    private String inv_buyerAddressLine;
    private String inv_buyerEmail;
    private String inv_buyerBankAccount;
    private String inv_buyerBankName;
    private String inv_paymentMethodName;
    private String inv_sellerBankAccount;
    private String inv_sellerBankName;
    private String mau_hd;
    private List<Details_MIV> details = new ArrayList<>();
    private BigDecimal inv_TotalAmountWithoutVat;
    private BigDecimal inv_discountAmount;
    private BigDecimal inv_vatAmount;
    private BigDecimal inv_TotalAmount;

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

    public String getInv_invoiceIssuedDate() {
        return inv_invoiceIssuedDate;
    }

    public void setInv_invoiceIssuedDate(String inv_invoiceIssuedDate) {
        this.inv_invoiceIssuedDate = inv_invoiceIssuedDate;
    }

    public String getInv_currencyCode() {
        return inv_currencyCode;
    }

    public void setInv_currencyCode(String inv_currencyCode) {
        this.inv_currencyCode = inv_currencyCode;
    }

    public BigDecimal getInv_exchangeRate() {
        return inv_exchangeRate;
    }

    public void setInv_exchangeRate(BigDecimal inv_exchangeRate) {
        this.inv_exchangeRate = inv_exchangeRate;
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

    public String getInv_buyerTaxCode() {
        return inv_buyerTaxCode;
    }

    public void setInv_buyerTaxCode(String inv_buyerTaxCode) {
        this.inv_buyerTaxCode = inv_buyerTaxCode;
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

    public String getInv_paymentMethodName() {
        return inv_paymentMethodName;
    }

    public void setInv_paymentMethodName(String inv_paymentMethodName) {
        this.inv_paymentMethodName = inv_paymentMethodName;
    }

    public String getInv_sellerBankAccount() {
        return inv_sellerBankAccount;
    }

    public void setInv_sellerBankAccount(String inv_sellerBankAccount) {
        this.inv_sellerBankAccount = inv_sellerBankAccount;
    }

    public String getInv_sellerBankName() {
        return inv_sellerBankName;
    }

    public void setInv_sellerBankName(String inv_sellerBankName) {
        this.inv_sellerBankName = inv_sellerBankName;
    }

    public String getMau_hd() {
        return mau_hd;
    }

    public void setMau_hd(String mau_hd) {
        this.mau_hd = mau_hd;
    }

    public List<Details_MIV> getDetails() {
        return details;
    }

    public void setDetails(List<Details_MIV> details) {
        this.details = details;
    }

    public BigDecimal getInv_TotalAmountWithoutVat() {
        return inv_TotalAmountWithoutVat;
    }

    public void setInv_TotalAmountWithoutVat(BigDecimal inv_TotalAmountWithoutVat) {
        this.inv_TotalAmountWithoutVat = inv_TotalAmountWithoutVat;
    }

    public BigDecimal getInv_discountAmount() {
        return inv_discountAmount;
    }

    public void setInv_discountAmount(BigDecimal inv_discountAmount) {
        this.inv_discountAmount = inv_discountAmount;
    }

    public BigDecimal getInv_vatAmount() {
        return inv_vatAmount;
    }

    public void setInv_vatAmount(BigDecimal inv_vatAmount) {
        this.inv_vatAmount = inv_vatAmount;
    }

    public BigDecimal getInv_TotalAmount() {
        return inv_TotalAmount;
    }

    public void setInv_TotalAmount(BigDecimal inv_TotalAmount) {
        this.inv_TotalAmount = inv_TotalAmount;
    }
}
