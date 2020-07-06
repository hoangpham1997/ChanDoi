package vn.softdreams.ebweb.web.rest.dto;

public class ResponseEInvoiceDTO {
    private Integer status;
    private Byte[] rawData;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Byte[] getRawData() {
        return rawData;
    }

    public void setRawData(Byte[] rawData) {
        this.rawData = rawData;
    }
}
