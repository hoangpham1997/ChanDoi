package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.AccountGroup;
import vn.softdreams.ebweb.service.AccountGroupService;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;
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
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing AccountGroup.
 */
@RestController
@RequestMapping("/api")
public class AccountGroupResource {

    private final Logger log = LoggerFactory.getLogger(AccountGroupResource.class);

    private static final String ENTITY_NAME = "accountGroup";

    private final AccountGroupService accountGroupService;

    public AccountGroupResource(AccountGroupService accountGroupService) {
        this.accountGroupService = accountGroupService;
    }

    /**
     * POST  /account-groups : Create a new accountGroup.
     *
     * @param accountGroup the accountGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountGroup, or with status 400 (Bad Request) if the accountGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-groups")
    @Timed
    public ResponseEntity<AccountGroup> createAccountGroup(@Valid @RequestBody AccountGroup accountGroup) throws URISyntaxException {
        log.debug("REST request to save AccountGroup : {}", accountGroup);
        if (accountGroup.getId() != null) {
            throw new BadRequestAlertException("A new accountGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountGroup result = accountGroupService.save(accountGroup);
        return ResponseEntity.created(new URI("/api/account-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-groups : Updates an existing accountGroup.
     *
     * @param accountGroup the accountGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountGroup,
     * or with status 400 (Bad Request) if the accountGroup is not valid,
     * or with status 500 (Internal Server Error) if the accountGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-groups")
    @Timed
    public ResponseEntity<AccountGroup> updateAccountGroup(@Valid @RequestBody AccountGroup accountGroup) throws URISyntaxException {
        log.debug("REST request to update AccountGroup : {}", accountGroup);
        if (accountGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountGroup result = accountGroupService.save(accountGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-groups : get all the accountGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountGroups in body
     */
    @GetMapping("/account-groups")
    @Timed
    public ResponseEntity<List<AccountGroup>> getAllAccountGroups(Pageable pageable) {
        log.debug("REST request to get a page of AccountGroups");
        Page<AccountGroup> page = accountGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-groups/:id : get the "id" accountGroup.
     *
     * @param id the id of the accountGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountGroup, or with status 404 (Not Found)
     */
    @GetMapping("/account-groups/{id}")
    @Timed
    public ResponseEntity<AccountGroup> getAccountGroup(@PathVariable String id) {
        log.debug("REST request to get AccountGroup : {}", id);
        Optional<AccountGroup> accountGroup = accountGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountGroup);
    }

    /**
     * DELETE  /account-groups/:id : delete the "id" accountGroup.
     *
     * @param id the id of the accountGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountGroup(@PathVariable String id) {
        log.debug("REST request to delete AccountGroup : {}", id);
        accountGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/account-groups/find-all-account-groups-active-companyid")
    @Timed
    public ResponseEntity<List<AccountGroup>> getAllAccountGroupActiveCompanyID() {
        log.debug("REST request to get a page of Account Group");
        List<AccountGroup> page = accountGroupService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
