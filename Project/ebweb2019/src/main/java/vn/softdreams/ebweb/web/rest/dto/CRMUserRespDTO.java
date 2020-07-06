package vn.softdreams.ebweb.web.rest.dto;

import java.util.UUID;

public class CRMUserRespDTO {
    private int systemCode;
    private String companyTaxCode;
    private boolean status;

    public CRMUserRespDTO() {
    }

    public int getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(int systemCode) {
        this.systemCode = systemCode;
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
}
