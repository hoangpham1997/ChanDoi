package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.AccountingObject;

public class ResultDTO {

    private String message;

    private Object result;

    private Boolean status;

    private Boolean isUpdate;

    private AccountingObjectDTO accountingObjectDTO;

    public ResultDTO() {
    }

    public ResultDTO(String message) {
        this.message = message;
    }

    public Boolean getUpdate() {
        return isUpdate;
    }

    public void setUpdate(Boolean update) {
        isUpdate = update;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AccountingObjectDTO getAccountingObjectDTO() {
        return accountingObjectDTO;
    }

    public void setAccountingObjectDTO(AccountingObjectDTO accountingObjectDTO) {
        this.accountingObjectDTO = accountingObjectDTO;
    }
}
