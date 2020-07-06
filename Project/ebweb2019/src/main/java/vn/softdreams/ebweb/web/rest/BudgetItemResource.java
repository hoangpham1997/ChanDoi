package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.BudgetItem;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.service.BudgetItemService;
import vn.softdreams.ebweb.service.dto.BudgetItemConvertDTO;
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
 * REST controller for managing BudgetItem.
 */
@RestController
@RequestMapping("/api")
public class BudgetItemResource {

    private final Logger log = LoggerFactory.getLogger(BudgetItemResource.class);

    private static final String ENTITY_NAME = "budgetItem";

    private final BudgetItemService budgetItemService;

    public BudgetItemResource(BudgetItemService budgetItemService) {
        this.budgetItemService = budgetItemService;
    }

    /**
     * POST  /budget-items : Create a new budgetItem.
     *
     * @param budgetItem the budgetItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new budgetItem, or with status 400 (Bad Request) if the budgetItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/budget-items")
    @Timed
    public ResponseEntity<BudgetItem> createBudgetItem(@Valid @RequestBody BudgetItem budgetItem) throws URISyntaxException {
        log.debug("REST request to save BudgetItem : {}", budgetItem);
        if (budgetItem.getId() != null) {
            throw new BadRequestAlertException("A new budgetItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BudgetItem result = budgetItemService.save(budgetItem);
        return ResponseEntity.created(new URI("/api/budget-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budget-items : Updates an existing budgetItem.
     *
     * @param budgetItem the budgetItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated budgetItem,
     * or with status 400 (Bad Request) if the budgetItem is not valid,
     * or with status 500 (Internal Server Error) if the budgetItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/budget-items")
    @Timed
    public ResponseEntity<BudgetItem> updateBudgetItem(@Valid @RequestBody BudgetItem budgetItem) throws URISyntaxException {
        log.debug("REST request to update BudgetItem : {}", budgetItem);
        if (budgetItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BudgetItem result = budgetItemService.save(budgetItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, budgetItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budget-items : get all the budgetItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of budgetItems in body
     */
    @GetMapping("/budget-items")
    @Timed
    public ResponseEntity<List<BudgetItem>> getAllBudgetItems(Pageable pageable) {
        log.debug("REST request to get a page of BudgetItems");
        List<BudgetItem> page = budgetItemService.findAllBudgetItem();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/budget-items");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /budget-items/:id : get the "id" budgetItem.
     *
     * @param id the id of the budgetItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the budgetItem, or with status 404 (Not Found)
     */
    @GetMapping("/budget-items/{id}")
    @Timed
    public ResponseEntity<BudgetItem> getBudgetItem(@PathVariable UUID id) {
        log.debug("REST request to get BudgetItem : {}", id);
        Optional<BudgetItem> budgetItem = budgetItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetItem);
    }

    /**
     * DELETE  /budget-items/:id : delete the "id" budgetItem.
     *
     * @param id the id of the budgetItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/budget-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteBudgetItem(@PathVariable UUID id) {
        log.debug("REST request to delete BudgetItem : {}", id);
        budgetItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/budget-items//active")
    @Timed
    public ResponseEntity<List<BudgetItemConvertDTO>> getAllBudgetItemsActive() {
        log.debug("REST request to get a page of BudgetItems");
        Page<BudgetItemConvertDTO> page = budgetItemService.getAllBudgetItemsActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/budget-items/active");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/budget-items/find-all-budget-items-active-companyid")
    @Timed
    public ResponseEntity<List<BudgetItem>> getAllBudgetItemActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<BudgetItem> page = budgetItemService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/budget-items/find-all-budget-items-by-companyid")
    @Timed
    public ResponseEntity<List<BudgetItem>> getBudgetItemsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<BudgetItem> page = budgetItemService.getBudgetItemsByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/budget-items/multi-delete-budgetItem")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDelete(@RequestBody List<BudgetItem> budgetItems) throws URISyntaxException {
        log.debug("REST request to delete multi BudgetItem : {}", budgetItems);
        HandlingResultDTO handlingResultDTO = budgetItemService.deleteMulti(budgetItems);
        return new ResponseEntity<>(handlingResultDTO, HttpStatus.OK);
    }
}
