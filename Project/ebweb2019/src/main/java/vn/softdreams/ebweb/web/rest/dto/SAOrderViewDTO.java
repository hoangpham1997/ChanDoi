package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.SAOrder;

import java.util.List;

public class SAOrderViewDTO {
    private SAOrder sAOrder;
    private List<RefVoucherDTO> viewVouchers;

    public SAOrderViewDTO() {
    }

    public SAOrderViewDTO(SAOrder sAOrder, List<RefVoucherDTO> viewVouchers) {
        this.sAOrder = sAOrder;
        this.viewVouchers = viewVouchers;
    }

    public SAOrder getsAOrder() {
        return sAOrder;
    }

    public void setsAOrder(SAOrder sAOrder) {
        this.sAOrder = sAOrder;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
