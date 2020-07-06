package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;

public class UserSearchDTO {
    private String login;
    private String fullName;
    private String mobilePhone;
    private OrganizationUnit organizationUnit;
    private Integer status;

    public UserSearchDTO() {}

    public UserSearchDTO(String login, String fullName, String mobilePhone, OrganizationUnit organizationUnit, Integer status) {
        this.login = login;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.organizationUnit = organizationUnit;
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
