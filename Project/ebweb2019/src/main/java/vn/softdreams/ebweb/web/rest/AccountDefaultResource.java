package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.undertow.security.idm.Account;
import vn.softdreams.ebweb.domain.AccountDefault;
import vn.softdreams.ebweb.service.AccountDefaultService;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;
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
 * REST controller for managing AccountDefault.
 */
@RestController
@RequestMapping("/api")
public class AccountDefaultResource {

    private final Logger log = LoggerFactory.getLogger(AccountDefaultResource.class);

    private static final String ENTITY_NAME = "accountDefault";

    private final AccountDefaultService accountDefaultService;

    public AccountDefaultResource(AccountDefaultService accountDefaultService) {
        this.accountDefaultService = accountDefaultService;
    }

    /**
     * POST  /account-defaults : Create a new accountDefault.
     *
     * @param accountDefault the accountDefault to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountDefault, or with status 400 (Bad Request) if the accountDefault has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-defaults")
    @Timed
    public ResponseEntity<AccountDefault> createAccountDefault(@Valid @RequestBody AccountDefault accountDefault) throws URISyntaxException {
        log.debug("REST request to save AccountDefault : {}", accountDefault);
        if (accountDefault.getId() != null) {
            throw new BadRequestAlertException("A new accountDefault cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountDefault result = accountDefaultService.save(accountDefault);
        return ResponseEntity.created(new URI("/api/account-defaults/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-defaults : Updates an existing accountDefault.
     *
     * @param accountDefault the accountDefault to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountDefault,
     * or with status 400 (Bad Request) if the accountDefault is not valid,
     * or with status 500 (Internal Server Error) if the accountDefault couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-defaults")
    @Timed
    public ResponseEntity<AccountDefault> updateAccountDefault(@Valid @RequestBody AccountDefault accountDefault) throws URISyntaxException {
        log.debug("REST request to update AccountDefault : {}", accountDefault);
        if (accountDefault.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountDefault result = accountDefaultService.save(accountDefault);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountDefault.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-defaults : get all the accountDefaults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountDefaults in body
     */
    @GetMapping("/account-defaults")
    @Timed
    public ResponseEntity<List<AccountDefault>> getAllAccountDefaults(Pageable pageable) {
        log.debug("REST request to get a page of AccountDefaults");
        Page<AccountDefault> page = accountDefaultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-defaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-defaults : get all the accountDefaults.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountDefaults in body
     */
    @GetMapping("/account-defaults/getAllAccountDefaults")
    @Timed
    public ResponseEntity<List<AccountDefault>> getAllAccountDefaults() {
        log.debug("REST request to get a page of AccountDefaults");
        Page<AccountDefault> page = accountDefaultService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-defaults/getAllAccountDefaults");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-defaults/:id : get the "id" accountDefault.
     *
     * @param id the id of the accountDefault to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountDefault, or with status 404 (Not Found)
     */
    @GetMapping("/account-defaults/{id}")
    @Timed
    public ResponseEntity<AccountDefault> getAccountDefault(@PathVariable UUID id) {
        log.debug("REST request to get AccountDefault : {}", id);
        Optional<AccountDefault> accountDefault = accountDefaultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountDefault);
    }

    /**
     * DELETE  /account-defaults/:id : delete the "id" accountDefault.
     *
     * @param id the id of the accountDefault to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-defaults/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountDefault(@PathVariable UUID id) {
        log.debug("REST request to delete AccountDefault : {}", id);
        accountDefaultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/account-defaults/find-all-account-defaults-by-companyid")
    @Timed
    public ResponseEntity<List<AccountDefaultDTO>> getAllAccountDefaultDTOCompanyID() {
        log.debug("REST request to get a page of Account Defaults");
        List<AccountDefaultDTO> page = accountDefaultService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-defaults/find-by-type-id")
    @Timed
    public ResponseEntity<List<AccountDefault>> getAccountDefaultByTypeID(@RequestParam Integer typeID) {
        log.debug("REST request to get AccountDefault : {}", typeID);
        List<AccountDefault> accountDefault = accountDefaultService.findByTypeID(typeID);
        return new ResponseEntity<>(accountDefault, HttpStatus.OK);
    }

    @PutMapping("/account-defaults/save-all")
    @Timed
    public ResponseEntity<AccountDefault> saveAll(@RequestBody List<AccountDefault> accountDefaults) throws URISyntaxException {
        log.debug("REST request to update accountDefaults : {}", accountDefaults);
        accountDefaultService.saveAll(accountDefaults);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
