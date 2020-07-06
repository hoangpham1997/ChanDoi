package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Record;

import java.util.List;

public class RequestRecordListDTO {
    private List<Record> records;
    private int typeIDMain;
    private Boolean isKho;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public int getTypeIDMain() {
        return typeIDMain;
    }

    public void setTypeIDMain(int typeIDMain) {
        this.typeIDMain = typeIDMain;
    }

    public Boolean getKho() {
        return isKho;
    }

    public void setKho(Boolean kho) {
        isKho = kho;
    }
}
