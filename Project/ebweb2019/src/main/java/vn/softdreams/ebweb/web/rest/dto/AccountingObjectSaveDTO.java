package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.AccountingObject;

public class AccountingObjectSaveDTO {
    private AccountingObject accountingObject;

    private int status;

    public AccountingObjectSaveDTO() {
    }

    public AccountingObjectSaveDTO(AccountingObject accountingObject, int status) {
        this.accountingObject = accountingObject;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }
}
