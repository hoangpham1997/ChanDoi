package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.ArrayList;
import java.util.List;

public class Details_MIV {
    private String tab_id;
    private List<DataDetails_MIV> data = new ArrayList<>();

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public List<DataDetails_MIV> getData() {
        return data;
    }

    public void setData(List<DataDetails_MIV> data) {
        this.data = data;
    }
}
