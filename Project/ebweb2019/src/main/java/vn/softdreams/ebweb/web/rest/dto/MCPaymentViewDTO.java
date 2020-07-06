package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCPayment;

import java.util.List;

public class MCPaymentViewDTO {
    private MCPayment mCPayment;
    private List<RefVoucherDTO> viewVouchers;

    public MCPaymentViewDTO() {
    }

    public MCPaymentViewDTO(MCPayment mCPayment, List<RefVoucherDTO> viewVouchers) {
        this.mCPayment = mCPayment;
        this.viewVouchers = viewVouchers;
    }

    public MCPayment getmCPayment() {
        return mCPayment;
    }

    public void setmCPayment(MCPayment mCPayment) {
        this.mCPayment = mCPayment;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
