package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.SAOrder;

public class SAOrderSaveDTO {
    private SAOrder sAOrder;
    private int status;

    public SAOrderSaveDTO() {
    }

    public SAOrderSaveDTO(SAOrder sAOrder, int status) {
        this.sAOrder = sAOrder;
        this.status = status;
    }

    public SAOrder getsAOrder() {
        return sAOrder;
    }

    public void setsAOrder(SAOrder sAOrder) {
        this.sAOrder = sAOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
