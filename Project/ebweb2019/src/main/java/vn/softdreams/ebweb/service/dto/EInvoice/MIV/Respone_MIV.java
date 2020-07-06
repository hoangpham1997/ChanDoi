package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

public class Respone_MIV {
    private String windowid;
    private String error;
    private String token;
    private Boolean ok;
    private DetailsRespone_MIV data = new DetailsRespone_MIV();
    private byte[] dataByteArr;

    public String getWindowid() {
        return windowid;
    }

    public void setWindowid(String windowid) {
        this.windowid = windowid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public DetailsRespone_MIV getData() {
        return data;
    }

    public void setData(DetailsRespone_MIV data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getDataByteArr() {
        return dataByteArr;
    }

    public void setDataByteArr(byte[] dataByteArr) {
        this.dataByteArr = dataByteArr;
    }
}
