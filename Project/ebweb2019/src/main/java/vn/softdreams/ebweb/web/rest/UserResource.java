package vn.softdreams.ebweb.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.EbUserPackageRepository;
import vn.softdreams.ebweb.repository.UserRepository;
import vn.softdreams.ebweb.security.jwt.JWTFilter;
import vn.softdreams.ebweb.service.EbUserPackageService;
import vn.softdreams.ebweb.service.MailService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.EbUserPackageDTO;
import vn.softdreams.ebweb.service.dto.OrgTreeUserDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserIDAndOrgIdDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateSessionDTO;
import vn.softdreams.ebweb.web.rest.dto.UserSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.UserSearchDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.errors.EmailAlreadyUsedException;
import vn.softdreams.ebweb.web.rest.errors.LoginAlreadyUsedException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and EbAuthority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private static final String ENTITY_NAME = "ebPackage";

    private final EbUserPackageService ebUserPackageService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService, EbUserPackageService ebUserPackageService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.ebUserPackageService = ebUserPackageService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserSaveDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else {
            UserSaveDTO result = userService.createNewUser(userDTO);
//            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + result.getUser().getLogin()))
                .headers(HeaderUtil.createAlert("userManagement.created", result.getUser().getLogin()))
                .body(result);
        }
    }

    @PostMapping("/users/user-org")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Boolean createUserOrg(@RequestBody UserIDAndOrgIdDTO userIDAndOrgIdDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userIDAndOrgIdDTO);
        return userService.createUserOrg(userIDAndOrgIdDTO);
    }

    @GetMapping("/p/users/reset-password")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> resetPassword(@RequestParam(required = false) String email) throws URISyntaxException {
        log.debug("REST request to save User : {}", email);
        UserDTO userDTO = userService.resetPassword(email);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/p/users/reset-password-expired")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void resetPasswordExpired(@RequestParam(required = false) String login) throws URISyntaxException {
        log.debug("REST request to save User : {}", login);
        userService.resetPasswordExpired(login);
    }

    @PostMapping("/users/create-admin")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserSaveDTO> createUserAdmin(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else {
            UserSaveDTO result = userService.createNewUserAdmin(userDTO);
//            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + result.getUser().getLogin()))
                .headers(HeaderUtil.createAlert("userManagement.created", result.getUser().getLogin()))
                .body(result);
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
//        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new EmailAlreadyUsedException();
//        }
//        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new LoginAlreadyUsedException();
//        }
        Boolean updatedUser = userService.updateEbUser(userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param orgTreeUserDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users/updateForEbGroupOrg")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Boolean> updateUserForEbGroupOrg(@RequestBody OrgTreeUserDTO orgTreeUserDTO) {
        log.debug("REST request to update User : {}", orgTreeUserDTO);
        Boolean result = userService.updateForEbGroupOrg(orgTreeUserDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users/updateInfo")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateInfoUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
//        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new EmailAlreadyUsedException();
//        }
//        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new LoginAlreadyUsedException();
//        }
        Optional<UserDTO> updatedUser = userService.updateInfoUser(userDTO);
        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    @PutMapping("/users/updateInfoAdmin")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateInfoAdmin(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
//        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new EmailAlreadyUsedException();
//        }
//        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
//            throw new LoginAlreadyUsedException();
//        }
        Optional<UserDTO> updatedUser = userService.updateInfoAdmin(userDTO);
        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/user-search")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable,
                                                     @RequestParam(required = false) String userSearch) {
        ObjectMapper mapper = new ObjectMapper();
        UserSearchDTO userSearchDTO = new UserSearchDTO();
        try {
            userSearchDTO = mapper.readValue(userSearch, UserSearchDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Page<UserDTO> page = userService.getUserSearch(pageable, userSearchDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/user-client")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsersClient(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsersClient(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/list-user/{orgId}")
    @Timed
    public ResponseEntity<List<UserDTO>> getListUsers(@PathVariable UUID orgId) {
        List<UserDTO> listUser = userService.getListUser(orgId);
        return new ResponseEntity<>(listUser, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * @return a string list of the all of the roles
     * @author anmt
     */
    @GetMapping("/users/ebGroups")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<EbGroup> getEbGroups() {
        return userService.getEbGroups();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        Optional<UserDTO> userDTO = userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    @GetMapping("/users/org-name")
    @Timed
    public ResponseEntity<EbUserOrganizationUnit> getOrgByUser(Long userId) {
        log.debug("REST request to get User : {}", userId);
        EbUserOrganizationUnit ebUserOrganizationUnit = userService.getOrgByUser(userId);
        return new ResponseEntity<>(ebUserOrganizationUnit, HttpStatus.OK);
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }

    @DeleteMapping("/users/user-admin/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUserAdmin(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUserAdmin(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }

    @PutMapping(path = "/users/change-session")
    @Timed
    public ResponseEntity<Object> updateSession(@RequestBody UpdateSessionDTO updateSessionDTO) {
        String jwt = userService.updateSession(updateSessionDTO.getCurrentBook(), updateSessionDTO.getOrg());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/users/active-package")
    @Timed
    public ResponseEntity<Boolean> updateEbPackage(@RequestBody EbUserPackageDTO ebUserPackageDTO) throws URISyntaxException {
        log.debug("REST request to update EbPackage : {}", ebUserPackageDTO);
        if (ebUserPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Boolean result = ebUserPackageService.activePackageEbUser(ebUserPackageDTO);
        return ResponseEntity.ok()
            .headers (HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ebUserPackageDTO.getId().toString()))
            .body(result);
    }

    @PostMapping(path = "/users/active-package-no-send-crm")
    @Timed
    public ResponseEntity<Boolean> updateEbPackageNoSendCrm(@RequestBody EbUserPackageDTO ebUserPackageDTO) throws URISyntaxException {
        log.debug("REST request to update EbPackage : {}", ebUserPackageDTO);
        if (ebUserPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Boolean result = ebUserPackageService.activePackageEbUserNoSendCrm(ebUserPackageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ebUserPackageDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/users/currentBookOfUser")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public int getCurrentBookOfUser() {
        return userService.getCurrentBookOfUser();
    }
}
