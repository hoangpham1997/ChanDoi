package vn.softdreams.ebweb.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;
import java.util.UUID;

/**
 * @author hieug
 */
public class EbUPAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // ~ Instance fields
    // ================================================================================================

    private UUID org;
    private Boolean isAdmin;

    // ~ Constructors
    // ===================================================================================================

    public EbUPAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        setAuthenticated(false);
    }

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     */
    public EbUPAuthenticationToken(Object principal, Object credentials, UUID org) {
        super(principal, credentials);
        this.org = org;
        setAuthenticated(false);
    }

    public EbUPAuthenticationToken(Object principal, Object credentials, boolean isAdmin) {
        super(principal, credentials);
        this.isAdmin = isAdmin;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public EbUPAuthenticationToken(Object principal, Object credentials,
                                   Collection<? extends GrantedAuthority> authorities,
                                   UUID org) {
        super(principal, credentials, authorities);
        this.org = org;
        super.setAuthenticated(true); // must use super, as we override
    }

    // ~ Methods
    // ========================================================================================================

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    public UUID getOrg() {
        return org;
    }

    public void setOrg(UUID org) {
        this.org = org;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
