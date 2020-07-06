package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.SystemOption;
import vn.softdreams.ebweb.domain.User;

import java.util.List;

public class UserSystemOption {
    private User user;
    private List<SystemOption> systemOptions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SystemOption> getSystemOptions() {
        return systemOptions;
    }

    public void setSystemOptions(List<SystemOption> systemOptions) {
        this.systemOptions = systemOptions;
    }

    public UserSystemOption(User user, List<SystemOption> systemOptions) {
        this.user = user;
        this.systemOptions = systemOptions;
    }

    public UserSystemOption() {
    }
}
