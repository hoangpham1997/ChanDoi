package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.CPExpenseTranfer;
import vn.softdreams.ebweb.domain.GOtherVoucher;

public class CPExpenseTranferSaveDTO {
    private CPExpenseTranfer cpExpenseTranfer;

    private int status;

    private String msg;

    public CPExpenseTranferSaveDTO() {
    }

    public CPExpenseTranferSaveDTO(CPExpenseTranfer cpExpenseTranfer, int status) {
        this.cpExpenseTranfer= cpExpenseTranfer;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CPExpenseTranfer getCpExpenseTranfer() {
        return cpExpenseTranfer;
    }

    public void setCpExpenseTranfer(CPExpenseTranfer cpExpenseTranfer) {
        this.cpExpenseTranfer = cpExpenseTranfer;
    }
}
