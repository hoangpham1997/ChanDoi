package vn.softdreams.ebweb.web.rest.dto;

public class TIAllocationPostDTO {
    private String description;

    private String debitAccount;

    private String creditAccount;

    private String amount;

    public TIAllocationPostDTO() {
    }

    public TIAllocationPostDTO(String description, String debitAccount, String creditAccount, String amount) {
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
