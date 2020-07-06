package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MBTellerPaper;

public class MBTellerPaperSaveDTO {
    private MBTellerPaper mBTellerPaper;

    private int status;

    private String msg;

    public MBTellerPaperSaveDTO() {
    }

    public MBTellerPaperSaveDTO(MBTellerPaper mBTellerPaper, int status) {
        this.mBTellerPaper = mBTellerPaper;
        this.status = status;
    }


    public MBTellerPaper getmBTellerPaper() {
        return mBTellerPaper;
    }

    public void setmBTellerPaper(MBTellerPaper mBTellerPaper) {
        this.mBTellerPaper = mBTellerPaper;
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
