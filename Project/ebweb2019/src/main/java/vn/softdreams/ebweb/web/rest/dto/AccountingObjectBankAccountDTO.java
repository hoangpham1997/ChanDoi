package vn.softdreams.ebweb.web.rest.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.UUID;

public class AccountingObjectBankAccountDTO {
    private UUID id;
    private String bankAccount;
    private String bankName;
    private String bankBranchName;
    private String accountHolderName;
    private Integer orderPriority;
    private UUID accountingObjectId;

    public AccountingObjectBankAccountDTO(UUID id, String bankAccount, String bankName, String bankBranchName, String accountHolderName, Integer orderPriority, UUID accountingObjectId) {
        this.id = id;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.bankBranchName = bankBranchName;
        this.accountHolderName = accountHolderName;
        this.orderPriority = orderPriority;
        this.accountingObjectId = accountingObjectId;
    }

    public AccountingObjectBankAccountDTO() {
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

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }
}
