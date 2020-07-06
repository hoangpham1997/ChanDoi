package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.EbGroupRepository;
import vn.softdreams.ebweb.security.AuthoritiesConstants;
import vn.softdreams.ebweb.service.EbGroupService;
import vn.softdreams.ebweb.service.dto.OrgGroupDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.EbGroupSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.errors.EmailAlreadyUsedException;
import vn.softdreams.ebweb.web.rest.errors.LoginAlreadyUsedException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EbGroupResource {
    private static final String ENTITY_NAME = "ebGroups";
    private final Logger log = LoggerFactory.getLogger(EbGroupResource.class);

    private final EbGroupService ebGroupService;

    private final EbGroupRepository ebGroupRepository;

    public EbGroupResource(EbGroupService ebGroupService, EbGroupRepository ebGroupRepository) {
        this.ebGroupService = ebGroupService;
        this.ebGroupRepository = ebGroupRepository;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param ebGroup the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/ebGroups")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbGroupSaveDTO> createUser(@Valid @RequestBody EbGroup ebGroup) throws URISyntaxException {
        log.debug("REST request to save User : {}", ebGroup);
        if (ebGroup.getId() != null) {
            throw new BadRequestAlertException("A new ebGroup cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        }
        EbGroupSaveDTO result = ebGroupService.save(ebGroup);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/ebGroups/for-admin-total-package")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbGroupSaveDTO> createEbGroupForAdminTotalPackage(@Valid @RequestBody OrgGroupDTO orgGroupDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", orgGroupDTO);
//        if (ebGroup.getId() != null) {
//            throw new BadRequestAlertException("A new ebGroup cannot already have an ID", "userManagement", "idexists");
//            // Lowercase the user login before comparing with database
//        }
        EbGroupSaveDTO result = ebGroupService.saveOrgGroup(orgGroupDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param ebGroup the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/ebGroups")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbGroupSaveDTO> updateUser(@Valid @RequestBody EbGroup ebGroup) {
        log.debug("REST request to update User : {}", ebGroup);
        EbGroupSaveDTO result = ebGroupService.save(ebGroup);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param ebGroup the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/ebGroups/update-authorities")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbGroup> updateAuthorities(@RequestBody EbGroup ebGroup) {
        log.debug("REST request to update User : {}", ebGroup);
        EbGroup result = ebGroupService.saveAuthorities(ebGroup);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/ebGroups")
    @Timed
    public ResponseEntity<List<EbGroup>> getAllEbGroups(Pageable pageable) {
        Page<EbGroup> page = ebGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ebGroups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param id the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/ebGroups/{id}")
    @Timed
    public ResponseEntity<EbGroup> getEbGroup(@PathVariable UUID id) {
        log.debug("REST request to get User : {}", id);
        Optional<EbGroup> ebGroup = ebGroupService.findOneById(id);
        return ResponseUtil.wrapOrNotFound(ebGroup);
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param id the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ebGroups/{id}")
    @Timed
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.debug("REST request to delete User: {}", id);
        ebGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET /users : get all users.
     *
     * @param orgID the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/ebGroups/get-all-by-orgId")
    @Timed
    public ResponseEntity<List<EbGroup>> getAllEbGroupsByOrgId(@RequestParam UUID orgID ) {
        List<EbGroup> page = ebGroupService.findAllByOrgId(orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
