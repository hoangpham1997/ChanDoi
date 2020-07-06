package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

public class Result {
    private String supplierTaxCode;
    private String invoiceNo;
    private String transactionID;
    private String reservationCode;
    private String hashString;

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

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }
}
