package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.User;

public class UserSaveDTO {
    private User user;

    private int status;

    public UserSaveDTO() {
    }

    public UserSaveDTO(User user, int status) {
        this.user = user;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
