package vn.softdreams.ebweb.web.rest.dto;

public class CRMUserResetDTO {
    private String email;
    private String hash;

    public CRMUserResetDTO() {
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
