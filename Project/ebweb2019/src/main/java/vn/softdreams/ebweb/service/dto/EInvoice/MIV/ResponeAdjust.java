package vn.softdreams.ebweb.service.dto.EInvoice.MIV;

public class ResponeAdjust {
    private String error;
    private DataResponeAdjust ok = new DataResponeAdjust();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataResponeAdjust getOk() {
        return ok;
    }

    public void setOk(DataResponeAdjust ok) {
        this.ok = ok;
    }
}
