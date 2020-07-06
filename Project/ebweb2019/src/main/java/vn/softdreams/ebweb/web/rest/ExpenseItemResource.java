package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.ExpenseItem;
import vn.softdreams.ebweb.service.ExpenseItemService;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.dto.ExpenseItemConvertDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
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
 * REST controller for managing ExpenseItem.
 */
@RestController
@RequestMapping("/api")
public class ExpenseItemResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseItemResource.class);

    private static final String ENTITY_NAME = "expenseItem";

    private final ExpenseItemService expenseItemService;

    public ExpenseItemResource(ExpenseItemService expenseItemService) {
        this.expenseItemService = expenseItemService;
    }

    /**
     * POST  /expense-items : Create a new expenseItem.
     *
     * @param expenseItem the expenseItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expenseItem, or with status 400 (Bad Request) if the expenseItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/expense-items")
    @Timed
    public ResponseEntity<ExpenseItem> createExpenseItem(@Valid @RequestBody ExpenseItem expenseItem) throws URISyntaxException {
        log.debug("REST request to save ExpenseItem : {}", expenseItem);
        if (expenseItem.getId() != null) {
            throw new BadRequestAlertException("A new expenseItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseItem result = expenseItemService.save(expenseItem);
        return ResponseEntity.created(new URI("/api/expenseItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expense-items : Updates an existing expenseItem.
     *
     * @param expenseItem the expenseItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expenseItem,
     * or with status 400 (Bad Request) if the expenseItem is not valid,
     * or with status 500 (Internal Server Error) if the expenseItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/expense-items")
    @Timed
    public ResponseEntity<ExpenseItem> updateExpenseItem(@Valid @RequestBody ExpenseItem expenseItem) throws URISyntaxException {
        log.debug("REST request to update ExpenseItem : {}", expenseItem);
        if (expenseItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpenseItem result = expenseItemService.save(expenseItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, expenseItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expense-items : get all the expenseItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of expenseItems in body
     */
    @GetMapping("/expense-items")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getAllExpenseItems(Pageable pageable) {
        log.debug("REST request to get a page of ExpenseItems");
        Page<ExpenseItem> page = expenseItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expense-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expense-items : get all the expenseItems.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of expenseItems in body
     */
    @GetMapping("/expense-items/getAllExpenseItems")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getAllExpenseItems() {
        log.debug("REST request to get a page of ExpenseItems");
        Page<ExpenseItem> page = expenseItemService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expenseItems/getAllExpenseItems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expense-items/:id : get the "id" expenseItem.
     *
     * @param id the id of the expenseItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expenseItem, or with status 404 (Not Found)
     */
    @GetMapping("/expense-items/{id}")
    @Timed
    public ResponseEntity<ExpenseItem> getExpenseItem(@PathVariable UUID id) {
        log.debug("REST request to get ExpenseItem : {}", id);
        Optional<ExpenseItem> expenseItem = expenseItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseItem);
    }

    /**
     * DELETE  /expense-items/:id : delete the "id" expenseItem.
     *
     * @param id the id of the expenseItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/expense-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteExpenseItem(@PathVariable UUID id) {
        log.debug("REST request to delete ExpenseItem : {}", id);
        expenseItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/expense-items/expense-item-active")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getExpenseItemActive() {
        log.debug("REST request to get a page of ExpenseItems");
        Page<ExpenseItem> page = expenseItemService.getExpenseItemActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expense-items/getAllExpenseItems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/expense-items/find-all-expense-item-active-companyid")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getAllExepenseItemActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/expense-items/find-all-expense-item-active-companyid-and-is-dependent")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getExpenseItemsAndIsDependent(@RequestParam (required = false) Boolean isDependent, @RequestParam (required = false) UUID orgID) {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.getExpenseItemsAndIsDependent(isDependent, orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/expense-items/find-all-expense-item-by-companyid")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getExpenseItemsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.getExpenseItemsByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/expense-items/getexpense-companyid")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getAllByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/expense-items/expense-companyid")
    @Timed
    public ResponseEntity<List<ExpenseItem>> findAllByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/expense-items/find-all-expense-item-by-company-id-similar-branch")
    @Timed
    public ResponseEntity<List<ExpenseItem>> getExpenseItemSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                                         @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.getExpenseItemSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/expense-items/expense-findallAndcompanyid")
    @Timed
    public ResponseEntity<List<ExpenseItem>> findAllByAndCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.findAllByAndCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/expense-items/multi-delete-expenseItem")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDelete(@RequestBody List<ExpenseItem> expenseItems) throws URISyntaxException {
        log.debug("REST request to delete multi ExpenseItem : {}", expenseItems);
        HandlingResultDTO handlingResultDTO = expenseItemService.deleteMulti(expenseItems);
        return new ResponseEntity<>(handlingResultDTO, HttpStatus.OK);
    }

    @GetMapping("/expense-items/find-all-expense-item-active-by-companyID")
    @Timed
    public ResponseEntity<List<ExpenseItem>> findAllActiveByCompanyID(@RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of Accounts");
        List<ExpenseItem> page = expenseItemService.findAllActiveByCompanyID(companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
