package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

import java.util.ArrayList;
import java.util.List;

public class DataStatus_MIV {
    public List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
