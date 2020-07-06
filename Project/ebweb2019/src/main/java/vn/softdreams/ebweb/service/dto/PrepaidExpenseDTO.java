package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PrepaidExpense;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;

import java.util.List;
import java.util.UUID;

public class PrepaidExpenseDTO {
   private PrepaidExpense prepaidExpense;
   private List<PrepaidExpenseVoucher> prepaidExpenseVoucher;

    public PrepaidExpenseDTO(PrepaidExpense prepaidExpense, List<PrepaidExpenseVoucher> prepaidExpenseVoucher) {
        this.prepaidExpense = prepaidExpense;
        this.prepaidExpenseVoucher = prepaidExpenseVoucher;
    }

    public PrepaidExpenseDTO() {
    }

    public PrepaidExpense getPrepaidExpense() {
        return prepaidExpense;
    }

    public void setPrepaidExpense(PrepaidExpense prepaidExpense) {
        this.prepaidExpense = prepaidExpense;
    }

    public List<PrepaidExpenseVoucher> getPrepaidExpenseVoucher() {
        return prepaidExpenseVoucher;
    }

    public void setPrepaidExpenseVoucher(List<PrepaidExpenseVoucher> prepaidExpenseVoucher) {
        this.prepaidExpenseVoucher = prepaidExpenseVoucher;
    }
}
