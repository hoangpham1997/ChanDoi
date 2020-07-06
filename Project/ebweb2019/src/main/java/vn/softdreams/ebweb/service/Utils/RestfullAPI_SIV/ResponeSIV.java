package vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV;

import vn.softdreams.ebweb.service.dto.EInvoice.SIV.ResponeCreateListInvoice;
import vn.softdreams.ebweb.service.dto.EInvoice.SIV.Result;

import java.util.List;

public class ResponeSIV {
    private Integer status;
    private String message;
    private String errorCode;
    private String description;
    private String fileToBytes;
    private String fileName;
    private Boolean paymentStatus;
    private Result result;
    private List<ResponeCreateListInvoice> createInvoiceOutputs;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileToBytes() {
        return fileToBytes;
    }

    public void setFileToBytes(String fileToBytes) {
        this.fileToBytes = fileToBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<ResponeCreateListInvoice> getCreateInvoiceOutputs() {
        return createInvoiceOutputs;
    }

    public void setCreateInvoiceOutputs(List<ResponeCreateListInvoice> createInvoiceOutputs) {
        this.createInvoiceOutputs = createInvoiceOutputs;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
