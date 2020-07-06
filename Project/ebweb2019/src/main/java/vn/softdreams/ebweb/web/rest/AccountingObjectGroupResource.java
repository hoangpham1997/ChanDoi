package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.checkerframework.checker.units.qual.Time;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.AccountingObjectGroup;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.AccountingObjectGroupService;
import vn.softdreams.ebweb.service.Utils.Utils;
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
 * REST controller for managing AccountingObjectGroup.
 */
@RestController
@RequestMapping("/api")
public class AccountingObjectGroupResource {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectGroupResource.class);

    private static final String ENTITY_NAME = "accountingObjectGroup";

    private final AccountingObjectGroupService accountingObjectGroupService;

    public AccountingObjectGroupResource(AccountingObjectGroupService accountingObjectGroupService) {
        this.accountingObjectGroupService = accountingObjectGroupService;
    }

    /**
     * POST  /accounting-object-groups : Create a new accountingObjectGroup.
     *
     * @param accountingObjectGroup the accountingObjectGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingObjectGroup, or with status 400 (Bad Request) if the accountingObjectGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-object-groups")
    @Timed
    public ResponseEntity<AccountingObjectGroup> createAccountingObjectGroup(@Valid @RequestBody AccountingObjectGroup accountingObjectGroup) throws URISyntaxException {
        log.debug("REST request to save AccountingObjectGroup : {}", accountingObjectGroup);
        if (accountingObjectGroup.getId() != null) {
            throw new BadRequestAlertException("A new accountingObjectGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountingObjectGroup result = accountingObjectGroupService.save(accountingObjectGroup);
        return ResponseEntity.created(new URI("/api/accounting-object-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounting-object-groups : Updates an existing accountingObjectGroup.
     *
     * @param accountingObjectGroup the accountingObjectGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingObjectGroup,
     * or with status 400 (Bad Request) if the accountingObjectGroup is not valid,
     * or with status 500 (Internal Server Error) if the accountingObjectGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-object-groups")
    @Timed
    public ResponseEntity<AccountingObjectGroup> updateAccountingObjectGroup(@Valid @RequestBody AccountingObjectGroup accountingObjectGroup) throws URISyntaxException {
        log.debug("REST request to update AccountingObjectGroup : {}", accountingObjectGroup);
        if (accountingObjectGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountingObjectGroup result = accountingObjectGroupService.save(accountingObjectGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingObjectGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-object-groups : get all the accountingObjectGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjectGroups in body
     */
    @GetMapping("/accounting-object-groups")
    @Timed
    public ResponseEntity<List<AccountingObjectGroup>> getAllAccountingObjectGroups(Pageable pageable) {
        log.debug("REST request to get a page of AccountingObjectGroups");
        Page<AccountingObjectGroup> page = accountingObjectGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-object-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-object-groups/:id : get the "id" accountingObjectGroup.
     *
     * @param id the id of the accountingObjectGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingObjectGroup, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-object-groups/{id}")
    @Timed
    public ResponseEntity<AccountingObjectGroup> getAccountingObjectGroup(@PathVariable UUID id) {
        log.debug("REST request to get AccountingObjectGroup : {}", id);
        Optional<AccountingObjectGroup> accountingObjectGroup = accountingObjectGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingObjectGroup);
    }

    /**
     * DELETE  /accounting-object-groups/:id : delete the "id" accountingObjectGroup.
     *
     * @param id the id of the accountingObjectGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-object-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingObjectGroup(@PathVariable UUID id) {
        log.debug("REST request to delete AccountingObjectGroup : {}", id);
        accountingObjectGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/accounting-object-groups/search-all")
    @Timed
    public ResponseEntity<List<AccountingObjectGroup>> findAll(Pageable pageable,
                                                               @RequestParam(required = false) String accountingobjectgroupcode) {
        log.debug("REST request to get a page of Units");
        Page<AccountingObjectGroup> page = accountingObjectGroupService.findAll(pageable, accountingobjectgroupcode);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-object-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/accounting-object-groups/find-all-accounting-object-group-by-companyid")
    @Timed
    public ResponseEntity<List<AccountingObjectGroup>> getAllAccountingObjectGroup(@RequestParam(required = false) Boolean similarBranch,
                                                                                   @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of AccountList");
        List<AccountingObjectGroup> page = accountingObjectGroupService.findAllAccountingObjectGroup(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }



    @GetMapping("/accounting-object-groups/load-all-accounting-object-group-by-companyid")
    @Time
    public ResponseEntity<List<AccountingObjectGroup>> getLoadAllAccountingObjectGroup() {
        log.debug("REST request to get a page of AccountList");
        List<AccountingObjectGroup> page = accountingObjectGroupService.loadAllAccountingObjectGroup();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-object-groups/getCbxAccountingObjectGroup/{id}")
    @Time
    public ResponseEntity<List<AccountingObjectGroup>> getCbxAccountingObjectGroup (@PathVariable UUID id) {
        log.debug("REST request to get a combobox list of AccountingObjectGroup", id);
        List<AccountingObjectGroup> accountingObjectGroups = accountingObjectGroupService.getCbxAccountingObjectGroup(id);
        return new ResponseEntity<>(accountingObjectGroups , HttpStatus.OK);
    }


    @GetMapping("/accounting-object-groups/find-all-accounting-object-group-by-company-id-similar-branch")
    @Timed
    public ResponseEntity<List<AccountingObjectGroup>> getAllAccountingObjectGroupSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                                                                @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of AccountList");
        List<AccountingObjectGroup> page = accountingObjectGroupService.getAllAccountingObjectGroupSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
