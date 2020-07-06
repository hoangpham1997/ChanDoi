package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.domain.RefVoucher;

import java.util.List;

public class PPDiscountReturnDTO {
    private PPOrder ppOrder;
    private List<RefVoucher> viewVouchers;

    public PPDiscountReturnDTO() {
    }

    public PPDiscountReturnDTO(PPOrder ppOrder, List<RefVoucher> viewVouchers) {
        this.ppOrder = ppOrder;
        this.viewVouchers = viewVouchers;
    }

    public PPOrder getPpOrder() {
        return ppOrder;
    }

    public void setPpOrder(PPOrder ppOrder) {
        this.ppOrder = ppOrder;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
