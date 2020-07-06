package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.math.BigDecimal;
import java.util.UUID;

public class InvoiceInfo {
    private UUID transactionUuid;
    private String templateCode;
    private String invoiceSeries;
    private String invoiceIssuedDate;
    private String invoiceType;
    private String currencyCode;
    private String adjustmentType;
    private boolean paymentStatus;
    private boolean cusGetInvoiceRight;
    private String buyerIdNo;
    private String buyerIdType;
    private String invoiceNote;
    private String adjustmentInvoiceType;
    private String originalInvoiceId;
    private String originalInvoiceIssueDate;
    private String additionalReferenceDesc;
    private String additionalReferenceDate;
    private String certificateSerial;
    private String userName;
    private BigDecimal exchangeRate;


    public UUID getTransactionUuid() {
        return transactionUuid;
    }

    public void setTransactionUuid(UUID transactionUuid) {
        this.transactionUuid = transactionUuid;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceIssuedDate() {
        return invoiceIssuedDate;
    }

    public void setInvoiceIssuedDate(String invoiceIssuedDate) {
        this.invoiceIssuedDate = invoiceIssuedDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean getCusGetInvoiceRight() {
        return cusGetInvoiceRight;
    }

    public void setCusGetInvoiceRight(boolean cusGetInvoiceRight) {
        this.cusGetInvoiceRight = cusGetInvoiceRight;
    }

    public String getBuyerIdNo() {
        return buyerIdNo;
    }

    public void setBuyerIdNo(String buyerIdNo) {
        this.buyerIdNo = buyerIdNo;
    }

    public String getBuyerIdType() {
        return buyerIdType;
    }

    public void setBuyerIdType(String buyerIdType) {
        this.buyerIdType = buyerIdType;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public String getAdjustmentInvoiceType() {
        return adjustmentInvoiceType;
    }

    public void setAdjustmentInvoiceType(String adjustmentInvoiceType) {
        this.adjustmentInvoiceType = adjustmentInvoiceType;
    }

    public String getOriginalInvoiceId() {
        return originalInvoiceId;
    }

    public void setOriginalInvoiceId(String originalInvoiceId) {
        this.originalInvoiceId = originalInvoiceId;
    }

    public String getOriginalInvoiceIssueDate() {
        return originalInvoiceIssueDate;
    }

    public void setOriginalInvoiceIssueDate(String originalInvoiceIssueDate) {
        this.originalInvoiceIssueDate = originalInvoiceIssueDate;
    }

    public String getAdditionalReferenceDesc() {
        return additionalReferenceDesc;
    }

    public void setAdditionalReferenceDesc(String additionalReferenceDesc) {
        this.additionalReferenceDesc = additionalReferenceDesc;
    }

    public String getAdditionalReferenceDate() {
        return additionalReferenceDate;
    }

    public void setAdditionalReferenceDate(String additionalReferenceDate) {
        this.additionalReferenceDate = additionalReferenceDate;
    }

    public String getCertificateSerial() {
        return certificateSerial;
    }

    public void setCertificateSerial(String certificateSerial) {
        this.certificateSerial = certificateSerial;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public boolean isCusGetInvoiceRight() {
        return cusGetInvoiceRight;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
