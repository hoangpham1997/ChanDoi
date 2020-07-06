package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.UUID;

public class DestructionInvoiceMIV {
    private UUID inv_InvoiceAuth_id;
    private String sovb;
    private String ngayvb;
    private String ghi_chu;

    public UUID getInv_InvoiceAuth_id() {
        return inv_InvoiceAuth_id;
    }

    public void setInv_InvoiceAuth_id(UUID inv_InvoiceAuth_id) {
        this.inv_InvoiceAuth_id = inv_InvoiceAuth_id;
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
}
