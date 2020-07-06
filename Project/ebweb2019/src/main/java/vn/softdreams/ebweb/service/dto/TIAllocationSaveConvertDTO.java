package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.TIAllocation;
import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.domain.TIAllocationPost;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TIAllocationSaveConvertDTO {
    private TIAllocation tiAllocation;
    private List<RefVoucher> viewVouchers;

    public TIAllocationSaveConvertDTO() {
    }

    public TIAllocationSaveConvertDTO(TIAllocation tiAllocation, List<RefVoucher> viewVouchers) {
        this.tiAllocation = tiAllocation;
        this.viewVouchers = viewVouchers;
    }

    public TIAllocation getTiAllocation() {
        return tiAllocation;
    }

    public void setTiAllocation(TIAllocation tiAllocation) {
        this.tiAllocation = tiAllocation;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
