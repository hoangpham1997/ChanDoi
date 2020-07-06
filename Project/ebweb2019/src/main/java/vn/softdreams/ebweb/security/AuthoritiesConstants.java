package vn.softdreams.ebweb.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String ROLE_MGT = "ROLE_MGT";

    public static final String ROLE_REPORT = "ROLE_REPORT";

    public static final String ROLE_PERMISSION = "ROLE_PERMISSION";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
