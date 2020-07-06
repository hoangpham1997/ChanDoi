package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

public class DestructionInvoice {
    private String supplierTaxCode;
    private String invoiceNo;
    private String strIssueDate;
    private String templateCode;
    private String additionalReferenceDesc;
    private String additionalReferenceDate;

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }

    public void setSupplierTaxCode(String supplierTaxCode) {
        this.supplierTaxCode = supplierTaxCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
