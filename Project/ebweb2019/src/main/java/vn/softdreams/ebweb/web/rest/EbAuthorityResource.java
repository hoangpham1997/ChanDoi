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
import vn.softdreams.ebweb.domain.EbAuthority;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.repository.EbAuthorityRepository;
import vn.softdreams.ebweb.repository.EbGroupRepository;
import vn.softdreams.ebweb.security.AuthoritiesConstants;
import vn.softdreams.ebweb.service.EbAuthorityService;
import vn.softdreams.ebweb.service.EbGroupService;
import vn.softdreams.ebweb.web.rest.dto.AuthorityTreeDTO;
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
public class EbAuthorityResource {
    private static final String ENTITY_NAME = "ebAuthorities";
    private final Logger log = LoggerFactory.getLogger(EbAuthorityResource.class);

    private final EbAuthorityService ebAuthorityService;

    private final EbAuthorityRepository ebAuthorityRepository;

    public EbAuthorityResource(EbAuthorityService ebAuthorityService, EbAuthorityRepository ebAuthorityRepository) {
        this.ebAuthorityService = ebAuthorityService;
        this.ebAuthorityRepository = ebAuthorityRepository;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param ebAuthority the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/ebAuthorities")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbAuthority> createEbAuthority(@Valid @RequestBody EbAuthority ebAuthority) throws URISyntaxException {
        log.debug("REST request to save User : {}", ebAuthority);
        if (ebAuthority.getId() != null) {
            throw new BadRequestAlertException("A new ebGroup cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        }
        EbAuthority result = ebAuthorityService.save(ebAuthority);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param ebAuthority the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/ebAuthorities")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<EbAuthority> updateEbAuthority(@Valid @RequestBody EbAuthority ebAuthority) {
        log.debug("REST request to update User : {}", ebAuthority);
        EbAuthority result = ebAuthorityService.save(ebAuthority);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/ebAuthorities")
    @Timed
    public ResponseEntity<List<EbAuthority>> getAllEbAuthorities(Pageable pageable) {
        Page<EbAuthority> page = ebAuthorityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ebAuthorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /users : get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/ebAuthorities/getAll")
    @Timed
    public ResponseEntity<List<EbAuthority>> getAllListEbAuthorities() {
        List<EbAuthority> page = ebAuthorityService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET /users : get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/ebAuthorities/tree/get-all")
    @Timed
    public ResponseEntity<List<AuthorityTreeDTO>> getAllTreeEbAuthorities() {
        List<AuthorityTreeDTO> page = ebAuthorityService.findAllAuthorityTree();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param id the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/ebAuthorities/{id}")
    @Timed
    public ResponseEntity<EbAuthority> getEbAuthority(@PathVariable UUID id) {
        log.debug("REST request to get User : {}", id);
        Optional<EbAuthority> ebAuthority = ebAuthorityService.findOneById(id);
        return ResponseUtil.wrapOrNotFound(ebAuthority);
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param id the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ebAuthorities/{id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteEbAuthority(@PathVariable UUID id) {
        log.debug("REST request to delete User: {}", id);
        ebAuthorityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
