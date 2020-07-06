package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import java.util.UUID;

public class KeyInvoiceNo {
    private UUID id;
    private String no;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
