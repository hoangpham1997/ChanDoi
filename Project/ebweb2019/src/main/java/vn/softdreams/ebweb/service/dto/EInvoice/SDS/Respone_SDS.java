package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlElement;

public class Respone_SDS {

    public HttpStatus HttpStatusCode;
    public Integer Status;
    public String Message;
    public ResponseData Data;
    public byte[] RawBytes;
    public String FileName;
    public String html;
    private String invoiceNo; // cho MIV

    public Respone_SDS() {
        Data = new ResponseData();
    }

    public HttpStatus getHttpStatusCode() {
        return HttpStatusCode;
    }

    public void setHttpStatusCode(HttpStatus httpStatusCode) {
        HttpStatusCode = httpStatusCode;
    }

    @XmlElement(name = "Status")
    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    @XmlElement(name = "Message")
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @XmlElement(name = "Data")
    public ResponseData getData() {
        return Data;
    }

    public void setData(ResponseData data) {
        Data = data;
    }

    public byte[] getRawBytes() {
        return RawBytes;
    }

    public void setRawBytes(byte[] rawBytes) {
        RawBytes = rawBytes;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
