package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Data {
    private UUID inv_InvoiceAuth_id;
    private String inv_invoiceNumber;
    private String trang_thai;

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

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }
}
