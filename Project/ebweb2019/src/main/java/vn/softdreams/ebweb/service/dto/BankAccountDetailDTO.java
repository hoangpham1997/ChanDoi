package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class BankAccountDetailDTO {
    private UUID id;
    private String bankAccount;
    private String bankName;

    public BankAccountDetailDTO(UUID id, String bankAccount, String bankName) {
        this.id = id;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
