package vn.softdreams.ebweb.security;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;
    private final OrganizationUnitRepository organizationUnitRepository;

    public DomainUserDetailsService(UserRepository userRepository, OrganizationUnitRepository organizationUnitRepository) {
        this.userRepository = userRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    @Override
    @Transactional
    public EbUserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        boolean isLogin2 = login.contains("|||");
        if (isLogin2) {
            UUID uuid = UUID.fromString(login.substring(login.lastIndexOf("|") + 1));
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(uuid);
            UUID orgID;
            if (organizationUnit.getUnitType() == 1) {
                orgID = organizationUnit.getParentID();
            } else {
                orgID = organizationUnit.getId();
            }
            String loginReal = login.substring(0, login.indexOf("|"));
            return userRepository.findOneWithAuthoritiesByLoginAndOrganizationUnitsId(loginReal, uuid)
                .map(user -> createSpringSecurityUser(lowercaseLogin, user, uuid, orgID))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
        }

        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user, null,null))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private EbUserDetails createSpringSecurityUser(String lowercaseLogin, User user, UUID org, UUID orgGetData) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
//        user.getOrganizationUnits().forEach(organizationUnit -> {
//            organizationUnit.getGroups().forEach(ebGroup -> {
//                List<GrantedAuthority> collect = ebGroup.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getCode()))
//                    .collect(Collectors.toList());
//                grantedAuthorities.addAll(collect);
//            });
//
//        });

        return new EbUserDetails(user.getLogin(),
            user.getPassword(),
            grantedAuthorities,
            org,
            org == null, orgGetData);
    }
}
