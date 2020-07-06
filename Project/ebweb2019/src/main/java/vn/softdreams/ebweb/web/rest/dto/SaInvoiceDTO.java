package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.SAInvoice;


public class SaInvoiceDTO {
    private SAInvoice saInvoice;

    private int status;

    public SaInvoiceDTO() {
    }

    public SaInvoiceDTO(SAInvoice saInvoice, int status) {
        this.saInvoice = saInvoice;
        this.status = status;
    }

    public SAInvoice getSaInvoice() {
        return saInvoice;
    }

    public void setSaInvoice(SAInvoice saInvoice) {
        this.saInvoice = saInvoice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
