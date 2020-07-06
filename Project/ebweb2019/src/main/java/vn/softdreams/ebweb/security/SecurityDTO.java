package vn.softdreams.ebweb.security;

import java.util.UUID;

public class SecurityDTO {
    private String login;
    private UUID org;
    private UUID orgGetData;

    public SecurityDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UUID getOrg() {
        return org;
    }

    public void setOrg(UUID org) {
        this.org = org;
    }

    public UUID getOrgGetData() {
        return orgGetData;
    }

    public void setOrgGetData(UUID orgGetData) {
        this.orgGetData = orgGetData;
    }

    public SecurityDTO(String login, UUID org, UUID orgGetData) {
        this.login = login;
        this.org = org;
        this.orgGetData = orgGetData;
    }

    public SecurityDTO(String login) {
        this.login = login;
    }
}
