package vn.softdreams.ebweb.service.dto.crm;

import org.springframework.http.HttpStatus;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.ResponseData;

import javax.xml.bind.annotation.XmlElement;

public class ResponeCRM {

    public int status;
    public String message;

    public ResponeCRM() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
