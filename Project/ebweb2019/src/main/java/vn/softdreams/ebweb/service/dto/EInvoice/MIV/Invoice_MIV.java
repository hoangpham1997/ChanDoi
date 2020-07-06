package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.ArrayList;
import java.util.List;

public class Invoice_MIV {
    private String windowid;
    private Integer editmode;
    private List<DataInvoice_MIV> data = new ArrayList<>();

    public String getWindowid() {
        return windowid;
    }

    public void setWindowid(String windowid) {
        this.windowid = windowid;
    }

    public Integer getEditmode() {
        return editmode;
    }

    public void setEditmode(Integer editmode) {
        this.editmode = editmode;
    }

    public List<DataInvoice_MIV> getData() {
        return data;
    }

    public void setData(List<DataInvoice_MIV> data) {
        this.data = data;
    }
}
