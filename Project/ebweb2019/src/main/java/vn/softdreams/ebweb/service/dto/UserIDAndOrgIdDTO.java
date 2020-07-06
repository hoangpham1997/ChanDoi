package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class UserIDAndOrgIdDTO {
    private UUID orgID;
    private Long userID;

    public UUID getOrgID() {
        return orgID;
    }

    public void setOrgID(UUID orgID) {
        this.orgID = orgID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
