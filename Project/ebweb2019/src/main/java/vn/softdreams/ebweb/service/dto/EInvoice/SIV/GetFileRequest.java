package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.util.UUID;

public class GetFileRequest {
    private String invoiceNo;
    private String fileType;
    private String strIssueDate;
    private String additionalReferenceDesc;
    private String additionalReferenceDate;
    private String pattern;
    private String supplierTaxCode;
    private UUID transactionUuid;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getStrIssueDate() {
        return strIssueDate;
    }

    public void setStrIssueDate(String strIssueDate) {
        this.strIssueDate = strIssueDate;
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }

    public void setSupplierTaxCode(String supplierTaxCode) {
        this.supplierTaxCode = supplierTaxCode;
    }

    public UUID getTransactionUuid() {
        return transactionUuid;
    }

    public void setTransactionUuid(UUID transactionUuid) {
        this.transactionUuid = transactionUuid;
    }
}
