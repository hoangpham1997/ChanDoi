package vn.softdreams.ebweb.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.UserRepository;
import vn.softdreams.ebweb.security.EbUPAuthenticationToken;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.security.jwt.JWTFilter;
import vn.softdreams.ebweb.security.jwt.TokenProvider;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.web.rest.dto.ChangeSessionDTO;
import vn.softdreams.ebweb.web.rest.dto.UserSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.InvalidPasswordException;
import vn.softdreams.ebweb.web.rest.errors.UserNotFoundException;
import vn.softdreams.ebweb.web.rest.vm.LoginVM;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private OrganizationUnitRepository organizationUnitRepository;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager,
                             UserRepository userRepository, PasswordEncoder passwordEncoder,
                             UserService userService, OrganizationUnitRepository organizationUnitRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<Object> authorize(@Valid @RequestBody LoginVM loginVM) {
        Boolean isAdmin = loginVM.getAdmin() != null ? loginVM.getAdmin() : false;
        if (loginVM.getOrg() != null || isAdmin) {
            EbUPAuthenticationToken authenticationToken = null;
            if (loginVM.getOrg() != null) {
                loginVM.setUsername(loginVM.getUsername() + "|||" + loginVM.getOrg().toString());
                authenticationToken = new EbUPAuthenticationToken(loginVM.getUsername(), loginVM.getPassword(), loginVM.getOrg());
            } else {
                authenticationToken =
                    new EbUPAuthenticationToken(loginVM.getUsername(), loginVM.getPassword(), true);
            }

            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            UUID orgID = null;
            if (loginVM.getOrg() != null && Boolean.FALSE.equals(isAdmin)) {
                OrganizationUnit organizationUnit = organizationUnitRepository.findByID(loginVM.getOrg());
                if (organizationUnit.getUnitType() == 1) {
                    orgID = organizationUnit.getParentID();
                } else {
                    orgID = organizationUnit.getId();
                }
            }
            String jwt = tokenProvider.createToken(authentication, rememberMe, loginVM.getOrg(), orgID);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } else {
            ChangeSessionDTO changeSessionDTO = userService.preLogin(loginVM);
            return new ResponseEntity<>(changeSessionDTO, HttpStatus.OK);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
