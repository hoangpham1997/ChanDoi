package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.UUID;

public class DataResponeAdjust {
    private UUID inv_InvoiceAuth_id;
    private String inv_invoiceNumber;

    public UUID getInv_InvoiceAuth_id() {
        return inv_InvoiceAuth_id;
    }

    public void setInv_InvoiceAuth_id(UUID inv_InvoiceAuth_id) {
        this.inv_InvoiceAuth_id = inv_InvoiceAuth_id;
    }

    public String getInv_invoiceNumber() {
        return inv_invoiceNumber;
    }

    public void setInv_invoiceNumber(String inv_invoiceNumber) {
        this.inv_invoiceNumber = inv_invoiceNumber;
    }
}
