package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCPayment;

public class MCPaymentSaveDTO {
    private MCPayment mCPayment;
    private int status;
    private String msg;

    public MCPaymentSaveDTO() {
    }

    public MCPaymentSaveDTO(MCPayment mCPayment, int status) {
        this.mCPayment = mCPayment;
        this.status = status;
    }

    public MCPayment getmCPayment() {
        return mCPayment;
    }

    public void setmCPayment(MCPayment mCPayment) {
        this.mCPayment = mCPayment;
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
