package vn.softdreams.ebweb.service.dto;

public class AccountForAccountDefaultDTO {

    private String accountNumber;
    private String accountName;

    public AccountForAccountDefaultDTO() {
    }

    public AccountForAccountDefaultDTO(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
