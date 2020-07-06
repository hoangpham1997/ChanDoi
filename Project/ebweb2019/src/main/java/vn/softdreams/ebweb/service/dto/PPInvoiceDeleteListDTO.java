package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PPInvoice;

import java.util.List;

public class PPInvoiceDeleteListDTO {
    private List<PPInvoice> ppInvoices;
    private Boolean isKho;

    public PPInvoiceDeleteListDTO() {
    }

    public List<PPInvoice> getPpInvoices() {
        return ppInvoices;
    }

    public void setPpInvoices(List<PPInvoice> ppInvoices) {
        this.ppInvoices = ppInvoices;
    }

    public Boolean getKho() {
        return isKho;
    }

    public void setKho(Boolean kho) {
        isKho = kho;
    }
}
