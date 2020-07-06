package vn.softdreams.ebweb.service.Utils.RestfullAPI_CRM;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.DigestData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestCRM {
    private String companyTaxCode;
    private boolean status;
    private String hash;

    public RequestCRM() {
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
