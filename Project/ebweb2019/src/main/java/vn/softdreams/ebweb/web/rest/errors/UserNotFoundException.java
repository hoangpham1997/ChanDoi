package vn.softdreams.ebweb.web.rest.errors;

public class UserNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User not found", "user", "User not found");
    }
}
