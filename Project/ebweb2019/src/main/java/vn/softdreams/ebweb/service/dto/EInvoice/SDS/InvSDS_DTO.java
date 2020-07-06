package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvSDS_DTO {
    private EInvoiceSDS_DTO invoice;

    public InvSDS_DTO() {
        invoice = new EInvoiceSDS_DTO();
    }

    @XmlElement(name = "Invoice")
    public EInvoiceSDS_DTO getInvoice() {
        return invoice;
    }

    public void setInvoice(EInvoiceSDS_DTO invoice) {
        this.invoice = invoice;
    }
}
