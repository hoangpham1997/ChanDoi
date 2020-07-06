package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class GOtherVoucherDetailDataKcDTO {
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private Integer fromAccountData;
    private String fromAccount;
    private Boolean isDebit;

    public GOtherVoucherDetailDataKcDTO() {
    }

    public GOtherVoucherDetailDataKcDTO(String description, String debitAccount, String creditAccount, BigDecimal amount, Integer fromAccountData, String fromAccount, Boolean isDebit) {
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.fromAccountData = fromAccountData;
        this.fromAccount = fromAccount;
        this.isDebit = isDebit;
    }

    public GOtherVoucherDetailDataKcDTO(String description, String debitAccount, String creditAccount, BigDecimal amount, Integer fromAccountData) {
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.fromAccountData = fromAccountData;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getFromAccountData() {
        return fromAccountData;
    }

    public void setFromAccountData(Integer fromAccountData) {
        this.fromAccountData = fromAccountData;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Boolean getDebit() {
        return isDebit;
    }

    public void setDebit(Boolean debit) {
        isDebit = debit;
    }
}
