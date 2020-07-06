package vn.softdreams.ebweb.web.rest.dto;

public class CRMUserStatusDTO {
    private String status;
    private String email;
    private String hash;

    public CRMUserStatusDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
