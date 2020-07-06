package vn.softdreams.ebweb.web.rest.dto;

public class CRMEbPackageDTO {
    private String packageCode;
    private String description;
    private String limitedVoucher;
    private String limitedUser;
    private String limitedCompany;
    private String expireTime;
    private String comType;
    private String hash;

    public CRMEbPackageDTO() {
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLimitedVoucher() {
        return limitedVoucher;
    }

    public void setLimitedVoucher(String limitedVoucher) {
        this.limitedVoucher = limitedVoucher;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLimitedUser() {
        return limitedUser;
    }

    public void setLimitedUser(String limitedUser) {
        this.limitedUser = limitedUser;
    }

    public String getLimitedCompany() {
        return limitedCompany;
    }

    public void setLimitedCompany(String limitedCompany) {
        this.limitedCompany = limitedCompany;
    }

    public String getComType() {
        return comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }
}
