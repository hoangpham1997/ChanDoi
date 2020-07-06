package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.GOtherVoucher;

public class GOtherVoucherSaveDTO {
    private GOtherVoucher gOtherVoucher;

    private int status;

    private String msg;

    public GOtherVoucherSaveDTO() {
    }

    public GOtherVoucherSaveDTO(GOtherVoucher gOtherVoucher, int status) {
        this.gOtherVoucher = gOtherVoucher;
        this.status = status;
    }

    public GOtherVoucher getgOtherVoucher() {
        return gOtherVoucher;
    }

    public void setgOtherVoucher(GOtherVoucher gOtherVoucher) {
        this.gOtherVoucher = gOtherVoucher;
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
}
