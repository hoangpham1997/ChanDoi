package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.SaBill;

import java.util.List;

public class SaBillViewDTO {
    private SaBill saBill;
    private List<RefVoucherDTO> viewVouchers;
    private Integer totalRow;

    public SaBillViewDTO() {
    }

    public SaBillViewDTO(SaBill saBill, List<RefVoucherDTO> viewVouchers) {
        this.saBill = saBill;
        this.viewVouchers = viewVouchers;
    }

    public SaBill getSaBill() {
        return saBill;
    }

    public void setSaBill(SaBill saBill) {
        this.saBill = saBill;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }
}
