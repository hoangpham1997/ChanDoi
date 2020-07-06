package vn.softdreams.ebweb.service.dto;

import org.springframework.security.core.parameters.P;
import vn.softdreams.ebweb.domain.PPInvoice;
import vn.softdreams.ebweb.domain.RSInwardOutward;

public class PPInvoiceSaveDTO {
    private PPInvoice ppInvoice;
    private RSInwardOutward rsInwardOutward;

    public PPInvoiceSaveDTO() {
    }

    public PPInvoice getPpInvoice() {
        return ppInvoice;
    }

    public void setPpInvoice(PPInvoice ppInvoice) {
        this.ppInvoice = ppInvoice;
    }

    public RSInwardOutward getRsInwardOutward() {
        return rsInwardOutward;
    }

    public void setRsInwardOutward(RSInwardOutward rsInwardOutward) {
        this.rsInwardOutward = rsInwardOutward;
    }
}
