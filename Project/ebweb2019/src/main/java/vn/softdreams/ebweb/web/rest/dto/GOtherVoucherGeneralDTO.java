package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.*;

import java.util.List;

public class GOtherVoucherGeneralDTO {
    private GOtherVoucher gOtherVoucher;
    private List<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses;
    private List<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations;
    private List<GOtherVoucherDetails> gOtherVoucherDetails;
    private List<RefVoucher> viewVouchers;

    public GOtherVoucherGeneralDTO() {
    }

    public GOtherVoucherGeneralDTO(GOtherVoucher gOtherVoucher, List<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses, List<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations, List<GOtherVoucherDetails> gOtherVoucherDetails, List<RefVoucher> viewVouchers) {
        this.gOtherVoucher = gOtherVoucher;
        this.gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenses;
        this.gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocations;
        this.gOtherVoucherDetails = gOtherVoucherDetails;
        this.viewVouchers = viewVouchers;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public GOtherVoucher getgOtherVoucher() {
        return gOtherVoucher;
    }

    public void setgOtherVoucher(GOtherVoucher gOtherVoucher) {
        this.gOtherVoucher = gOtherVoucher;
    }

    public List<GOtherVoucherDetailExpense> getgOtherVoucherDetailExpenses() {
        return gOtherVoucherDetailExpenses;
    }

    public void setgOtherVoucherDetailExpenses(List<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses) {
        this.gOtherVoucherDetailExpenses = gOtherVoucherDetailExpenses;
    }

    public List<GOtherVoucherDetailExpenseAllocation> getgOtherVoucherDetailExpenseAllocations() {
        return gOtherVoucherDetailExpenseAllocations;
    }

    public void setgOtherVoucherDetailExpenseAllocations(List<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations) {
        this.gOtherVoucherDetailExpenseAllocations = gOtherVoucherDetailExpenseAllocations;
    }

    public List<GOtherVoucherDetails> getgOtherVoucherDetails() {
        return gOtherVoucherDetails;
    }

    public void setgOtherVoucherDetails(List<GOtherVoucherDetails> gOtherVoucherDetails) {
        this.gOtherVoucherDetails = gOtherVoucherDetails;
    }
}
