package vn.softdreams.ebweb.service.dto.cashandbank;

import vn.softdreams.ebweb.domain.MBCreditCard;

public class MBCreditCardSaveDTO {
    private MBCreditCard mbCreditCard;

    private int status;

    private String msg;

    public MBCreditCardSaveDTO() {
    }

    public MBCreditCardSaveDTO(MBCreditCard mbCreditCard, int status) {
        this.mbCreditCard = mbCreditCard;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MBCreditCard getMbCreditCard() {
        return mbCreditCard;
    }

    public void setMbCreditCard(MBCreditCard mbCreditCard) {
        this.mbCreditCard = mbCreditCard;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
