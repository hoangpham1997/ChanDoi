package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

public class ConvertInvoice {
    private String supplierTaxCode;
    private String invoiceNo;
    private String strIssueDate;
    private String exchangeUser;

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

    public String getExchangeUser() {
        return exchangeUser;
    }

    public void setExchangeUser(String exchangeUser) {
        this.exchangeUser = exchangeUser;
    }
}
