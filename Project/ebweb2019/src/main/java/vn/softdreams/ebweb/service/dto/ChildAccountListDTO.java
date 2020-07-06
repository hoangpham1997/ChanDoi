package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.AccountList;

import java.util.List;

public class ChildAccountListDTO {
    private AccountList accountList;
    private ChildAccountListDTO childAccountList;

    public ChildAccountListDTO() {
    }

    public ChildAccountListDTO(AccountList accountList, ChildAccountListDTO childAccountList) {
        this.accountList = accountList;
        this.childAccountList = childAccountList;
    }

    public AccountList getAccountList() {
        return accountList;
    }

    public void setAccountList(AccountList accountList) {
        this.accountList = accountList;
    }

    public ChildAccountListDTO getChildAccountList() {
        return childAccountList;
    }

    public void setChildAccountList(ChildAccountListDTO childAccountList) {
        this.childAccountList = childAccountList;
    }
}
