package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;

import java.util.List;

public class GOtherVoucherGeneralViewDTO {
    private GOtherVoucher gOtherVoucher;
    private List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenses;
    private List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDetailExpenseAllocations;
    private List<GOtherVoucherDetailDTO> gOtherVoucherDetails;
    private List<RefVoucherDTO> refVoucherDTOS;

    public GOtherVoucherGeneralViewDTO() {
    }

    public GOtherVoucherGeneralViewDTO(GOtherVoucher gOtherVoucher, List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenses, List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDetailExpenseAllocations, List<GOtherVoucherDetailDTO> gOtherVoucherDetails, List<RefVoucherDTO> refVoucherDTOS) {
        this.gOtherVoucher = gOtherVoucher;
        this.gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenses;
        this.gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocations;
        this.gOtherVoucherDetails = gOtherVoucherDetails;
        this.refVoucherDTOS = refVoucherDTOS;
    }

    public List<RefVoucherDTO> getRefVoucherDTOS() {
        return refVoucherDTOS;
    }

    public void setRefVoucherDTOS(List<RefVoucherDTO> refVoucherDTOS) {
        this.refVoucherDTOS = refVoucherDTOS;
    }

    public GOtherVoucher getgOtherVoucher() {
        return gOtherVoucher;
    }

    public void setgOtherVoucher(GOtherVoucher gOtherVoucher) {
        this.gOtherVoucher = gOtherVoucher;
    }

    public List<GOtherVoucherDetailExpenseViewDTO> getgOtherVoucherDetailExpenses() {
        return gOtherVoucherDetailExpenses;
    }

    public void setgOtherVoucherDetailExpenses(List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenses) {
        this.gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenses;
    }

    public List<GOtherVoucherDetailExpenseAllocationViewDTO> getgOtherVoucherDetailExpenseAllocations() {
        return gOtherVoucherDetailExpenseAllocations;
    }

    public void setgOtherVoucherDetailExpenseAllocations(List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDetailExpenseAllocations) {
        this.gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocations;
    }

    public List<GOtherVoucherDetailDTO> getgOtherVoucherDetails() {
        return gOtherVoucherDetails;
    }

    public void setgOtherVoucherDetails(List<GOtherVoucherDetailDTO> gOtherVoucherDetails) {
        this.gOtherVoucherDetails = gOtherVoucherDetails;
    }
}
