package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;
import vn.softdreams.ebweb.service.PrepaidExpenseAllocationService;
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
 * REST controller for managing PrepaidExpenseAllocation.
 */
@RestController
@RequestMapping("/api")
public class PrepaidExpenseAllocationResource {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseAllocationResource.class);

    private static final String ENTITY_NAME = "prepaidExpenseAllocation";

    private final PrepaidExpenseAllocationService prepaidExpenseAllocationService;

    public PrepaidExpenseAllocationResource(PrepaidExpenseAllocationService prepaidExpenseAllocationService) {
        this.prepaidExpenseAllocationService = prepaidExpenseAllocationService;
    }

    /**
     * POST  /prepaid-expense-allocations : Create a new prepaidExpenseAllocation.
     *
     * @param prepaidExpenseAllocation the prepaidExpenseAllocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prepaidExpenseAllocation, or with status 400 (Bad Request) if the prepaidExpenseAllocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prepaid-expense-allocations")
    @Timed
    public ResponseEntity<PrepaidExpenseAllocation> createPrepaidExpenseAllocation(@Valid @RequestBody PrepaidExpenseAllocation prepaidExpenseAllocation) throws URISyntaxException {
        log.debug("REST request to save PrepaidExpenseAllocation : {}", prepaidExpenseAllocation);
        if (prepaidExpenseAllocation.getId() != null) {
            throw new BadRequestAlertException("A new prepaidExpenseAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaidExpenseAllocation result = prepaidExpenseAllocationService.save(prepaidExpenseAllocation);
        return ResponseEntity.created(new URI("/api/prepaid-expense-allocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prepaid-expense-allocations : Updates an existing prepaidExpenseAllocation.
     *
     * @param prepaidExpenseAllocation the prepaidExpenseAllocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prepaidExpenseAllocation,
     * or with status 400 (Bad Request) if the prepaidExpenseAllocation is not valid,
     * or with status 500 (Internal Server Error) if the prepaidExpenseAllocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prepaid-expense-allocations")
    @Timed
    public ResponseEntity<PrepaidExpenseAllocation> updatePrepaidExpenseAllocation(@Valid @RequestBody PrepaidExpenseAllocation prepaidExpenseAllocation) throws URISyntaxException {
        log.debug("REST request to update PrepaidExpenseAllocation : {}", prepaidExpenseAllocation);
        if (prepaidExpenseAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrepaidExpenseAllocation result = prepaidExpenseAllocationService.save(prepaidExpenseAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prepaidExpenseAllocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prepaid-expense-allocations : get all the prepaidExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prepaidExpenseAllocations in body
     */
    @GetMapping("/prepaid-expense-allocations")
    @Timed
    public ResponseEntity<List<PrepaidExpenseAllocation>> getAllPrepaidExpenseAllocations(Pageable pageable) {
        log.debug("REST request to get a page of PrepaidExpenseAllocations");
        Page<PrepaidExpenseAllocation> page = prepaidExpenseAllocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expense-allocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prepaid-expense-allocations/:id : get the "id" prepaidExpenseAllocation.
     *
     * @param id the id of the prepaidExpenseAllocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prepaidExpenseAllocation, or with status 404 (Not Found)
     */
    @GetMapping("/prepaid-expense-allocations/{id}")
    @Timed
    public ResponseEntity<PrepaidExpenseAllocation> getPrepaidExpenseAllocation(@PathVariable UUID id) {
        log.debug("REST request to get PrepaidExpenseAllocation : {}", id);
        Optional<PrepaidExpenseAllocation> prepaidExpenseAllocation = prepaidExpenseAllocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaidExpenseAllocation);
    }

    /**
     * DELETE  /prepaid-expense-allocations/:id : delete the "id" prepaidExpenseAllocation.
     *
     * @param id the id of the prepaidExpenseAllocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prepaid-expense-allocations/{id}")
    @Timed
    public ResponseEntity<Void> deletePrepaidExpenseAllocation(@PathVariable UUID id) {
        log.debug("REST request to delete PrepaidExpenseAllocation : {}", id);
        prepaidExpenseAllocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
