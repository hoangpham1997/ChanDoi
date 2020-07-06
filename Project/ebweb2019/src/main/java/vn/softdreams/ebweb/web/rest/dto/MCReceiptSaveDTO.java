package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCReceipt;

public class MCReceiptSaveDTO {
    private MCReceipt mCReceipt;
    private String msg;
    private int status;

    public MCReceiptSaveDTO() {
    }

    public MCReceiptSaveDTO(MCReceipt mCReceipt, int status) {
        this.mCReceipt = mCReceipt;
        this.status = status;
    }

    public MCReceipt getmCReceipt() {
        return mCReceipt;
    }

    public void setmCReceipt(MCReceipt mCReceipt) {
        this.mCReceipt = mCReceipt;
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
