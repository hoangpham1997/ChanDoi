package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.domain.ViewVoucher;

import java.util.List;
import java.util.UUID;

public class SaBillSaveDTO {
    private SaBill saBill;
    private List<RefVoucher> viewVouchers;
    private UUID saInvoiceDetailID;

    public SaBillSaveDTO() {
    }

    public SaBillSaveDTO(SaBill saBill, List<RefVoucher> viewVouchers) {
        this.saBill = saBill;
        this.viewVouchers = viewVouchers;
    }

    public SaBill getSaBill() {
        return saBill;
    }

    public void setSaBill(SaBill saBill) {
        this.saBill = saBill;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public UUID getSaInvoiceDetailID() {
        return saInvoiceDetailID;
    }

    public void setSaInvoiceDetailID(UUID saInvoiceDetailID) {
        this.saInvoiceDetailID = saInvoiceDetailID;
    }
}
