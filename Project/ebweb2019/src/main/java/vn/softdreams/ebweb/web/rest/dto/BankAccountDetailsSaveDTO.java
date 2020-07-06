package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.domain.Unit;

public class BankAccountDetailsSaveDTO {
    private BankAccountDetails bankAccountDetails;

    private int status;

    public BankAccountDetailsSaveDTO() {
    }

    public BankAccountDetailsSaveDTO(BankAccountDetails bankAccountDetails, int status) {
        this.bankAccountDetails = bankAccountDetails;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BankAccountDetails getBankAccountDetails() {
        return bankAccountDetails;
    }

    public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }
}
