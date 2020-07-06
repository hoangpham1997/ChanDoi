package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.Bank;

public class BankSaveDTO {
    private Bank bank;

    private int status;

    public BankSaveDTO() {
    }

    public BankSaveDTO(Bank bank, int status) {
        this.bank = bank;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Bank getBank() {
        return bank;
    }

    public void getBank(Bank bank) {
        this.bank = bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}
