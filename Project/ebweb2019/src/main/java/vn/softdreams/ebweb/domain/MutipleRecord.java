package vn.softdreams.ebweb.domain;

import java.util.List;
import java.util.UUID;

public class MutipleRecord {
    private List<MBDeposit> listID; /*Loại chứng từ*/
    private Integer statusRecorded;

    public Integer getStatusRecorded() {
        return statusRecorded;
    }

    public void setStatusRecorded(Integer statusRecorded) {
        this.statusRecorded = statusRecorded;
    }

    public List<MBDeposit> getListID() {
        return listID;
    }

    public void setListID(List<MBDeposit> listID) {
        this.listID = listID;
    }
}

