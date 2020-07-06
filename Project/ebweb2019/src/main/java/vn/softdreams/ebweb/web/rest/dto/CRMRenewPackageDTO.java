package vn.softdreams.ebweb.web.rest.dto;

public class CRMRenewPackageDTO {
    private String email;
    private String servicePackage;
    private String renewEndDate;
    private String hash;

    public CRMRenewPackageDTO() {
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getRenewEndDate() {
        return renewEndDate;
    }

    public void setRenewEndDate(String renewEndDate) {
        this.renewEndDate = renewEndDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
