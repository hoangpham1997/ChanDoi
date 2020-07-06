package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

public class GetListInvoiceWithDate {
    public String supplierTaxCode;
    public String fromDate;
    public String toDate;

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }

    public void setSupplierTaxCode(String supplierTaxCode) {
        this.supplierTaxCode = supplierTaxCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
