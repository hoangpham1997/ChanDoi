package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;
import vn.softdreams.ebweb.service.CPAllocationGeneralExpenseService;
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
 * REST controller for managing CPAllocationGeneralExpense.
 */
@RestController
@RequestMapping("/api")
public class CPAllocationGeneralExpenseResource {

    private final Logger log = LoggerFactory.getLogger(CPAllocationGeneralExpenseResource.class);

    private static final String ENTITY_NAME = "cPAllocationGeneralExpense";

    private final CPAllocationGeneralExpenseService cPAllocationGeneralExpenseService;

    public CPAllocationGeneralExpenseResource(CPAllocationGeneralExpenseService cPAllocationGeneralExpenseService) {
        this.cPAllocationGeneralExpenseService = cPAllocationGeneralExpenseService;
    }

    /**
     * POST  /cp-allocation-general-expenses : Create a new cPAllocationGeneralExpense.
     *
     * @param cPAllocationGeneralExpense the cPAllocationGeneralExpense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPAllocationGeneralExpense, or with status 400 (Bad Request) if the cPAllocationGeneralExpense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-allocation-general-expenses")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpense> createCPAllocationGeneralExpense(@Valid @RequestBody CPAllocationGeneralExpense cPAllocationGeneralExpense) throws URISyntaxException {
        log.debug("REST request to save CPAllocationGeneralExpense : {}", cPAllocationGeneralExpense);
        if (cPAllocationGeneralExpense.getId() != null) {
            throw new BadRequestAlertException("A new cPAllocationGeneralExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPAllocationGeneralExpense result = cPAllocationGeneralExpenseService.save(cPAllocationGeneralExpense);
        return ResponseEntity.created(new URI("/api/cp-allocation-general-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-allocation-general-expenses : Updates an existing cPAllocationGeneralExpense.
     *
     * @param cPAllocationGeneralExpense the cPAllocationGeneralExpense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAllocationGeneralExpense,
     * or with status 400 (Bad Request) if the cPAllocationGeneralExpense is not valid,
     * or with status 500 (Internal Server Error) if the cPAllocationGeneralExpense couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-allocation-general-expenses")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpense> updateCPAllocationGeneralExpense(@Valid @RequestBody CPAllocationGeneralExpense cPAllocationGeneralExpense) throws URISyntaxException {
        log.debug("REST request to update CPAllocationGeneralExpense : {}", cPAllocationGeneralExpense);
        if (cPAllocationGeneralExpense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPAllocationGeneralExpense result = cPAllocationGeneralExpenseService.save(cPAllocationGeneralExpense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAllocationGeneralExpense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-allocation-general-expenses : get all the cPAllocationGeneralExpenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAllocationGeneralExpenses in body
     */
    @GetMapping("/cp-allocation-general-expenses")
    @Timed
    public ResponseEntity<List<CPAllocationGeneralExpense>> getAllCPAllocationGeneralExpenses(Pageable pageable) {
        log.debug("REST request to get a page of CPAllocationGeneralExpenses");
        Page<CPAllocationGeneralExpense> page = cPAllocationGeneralExpenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-allocation-general-expenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-allocation-general-expenses/:id : get the "id" cPAllocationGeneralExpense.
     *
     * @param id the id of the cPAllocationGeneralExpense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAllocationGeneralExpense, or with status 404 (Not Found)
     */
    @GetMapping("/cp-allocation-general-expenses/{id}")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpense> getCPAllocationGeneralExpense(@PathVariable UUID id) {
        log.debug("REST request to get CPAllocationGeneralExpense : {}", id);
        Optional<CPAllocationGeneralExpense> cPAllocationGeneralExpense = cPAllocationGeneralExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAllocationGeneralExpense);
    }

    /**
     * DELETE  /cp-allocation-general-expenses/:id : delete the "id" cPAllocationGeneralExpense.
     *
     * @param id the id of the cPAllocationGeneralExpense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-allocation-general-expenses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAllocationGeneralExpense(@PathVariable UUID id) {
        log.debug("REST request to delete CPAllocationGeneralExpense : {}", id);
        cPAllocationGeneralExpenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
