package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ListEInoviceSDS_DTO {
    List<EInvoiceSDS_DTO> invoice;

    @XmlElement(name = "Invoice")
    public List<EInvoiceSDS_DTO> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<EInvoiceSDS_DTO> invoice) {
        this.invoice = invoice;
    }
}
