package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import org.checkerframework.checker.units.qual.A;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name = "Invoices")
public class InvoicesSDS_DTO {
    private List<InvSDS_DTO> inv;

    @XmlElement(name = "Inv")
    public List<InvSDS_DTO> getInv() {
        return inv;
    }

    public void setInv(List<InvSDS_DTO> inv) {
        this.inv = inv;
    }
}
