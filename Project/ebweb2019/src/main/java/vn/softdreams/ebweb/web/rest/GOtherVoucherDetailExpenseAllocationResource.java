package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import vn.softdreams.ebweb.service.GOtherVoucherDetailExpenseAllocationService;
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
 * REST controller for managing GOtherVoucherDetailExpenseAllocation.
 */
@RestController
@RequestMapping("/api")
public class GOtherVoucherDetailExpenseAllocationResource {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailExpenseAllocationResource.class);

    private static final String ENTITY_NAME = "gOtherVoucherDetailExpenseAllocation";

    private final GOtherVoucherDetailExpenseAllocationService gOtherVoucherDetailExpenseAllocationService;

    public GOtherVoucherDetailExpenseAllocationResource(GOtherVoucherDetailExpenseAllocationService gOtherVoucherDetailExpenseAllocationService) {
        this.gOtherVoucherDetailExpenseAllocationService = gOtherVoucherDetailExpenseAllocationService;
    }

    /**
     * POST  /g-other-voucher-detail-expense-allocations : Create a new gOtherVoucherDetailExpenseAllocation.
     *
     * @param gOtherVoucherDetailExpenseAllocation the gOtherVoucherDetailExpenseAllocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gOtherVoucherDetailExpenseAllocation, or with status 400 (Bad Request) if the gOtherVoucherDetailExpenseAllocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/g-other-voucher-detail-expense-allocations")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpenseAllocation> createGOtherVoucherDetailExpenseAllocation(@Valid @RequestBody GOtherVoucherDetailExpenseAllocation gOtherVoucherDetailExpenseAllocation) throws URISyntaxException {
        log.debug("REST request to save GOtherVoucherDetailExpenseAllocation : {}", gOtherVoucherDetailExpenseAllocation);
        if (gOtherVoucherDetailExpenseAllocation.getId() != null) {
            throw new BadRequestAlertException("A new gOtherVoucherDetailExpenseAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherDetailExpenseAllocation result = gOtherVoucherDetailExpenseAllocationService.save(gOtherVoucherDetailExpenseAllocation);
        return ResponseEntity.created(new URI("/api/g-other-voucher-detail-expense-allocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /g-other-voucher-detail-expense-allocations : Updates an existing gOtherVoucherDetailExpenseAllocation.
     *
     * @param gOtherVoucherDetailExpenseAllocation the gOtherVoucherDetailExpenseAllocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gOtherVoucherDetailExpenseAllocation,
     * or with status 400 (Bad Request) if the gOtherVoucherDetailExpenseAllocation is not valid,
     * or with status 500 (Internal Server Error) if the gOtherVoucherDetailExpenseAllocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/g-other-voucher-detail-expense-allocations")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpenseAllocation> updateGOtherVoucherDetailExpenseAllocation(@Valid @RequestBody GOtherVoucherDetailExpenseAllocation gOtherVoucherDetailExpenseAllocation) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucherDetailExpenseAllocation : {}", gOtherVoucherDetailExpenseAllocation);
        if (gOtherVoucherDetailExpenseAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GOtherVoucherDetailExpenseAllocation result = gOtherVoucherDetailExpenseAllocationService.save(gOtherVoucherDetailExpenseAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gOtherVoucherDetailExpenseAllocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /g-other-voucher-detail-expense-allocations : get all the gOtherVoucherDetailExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gOtherVoucherDetailExpenseAllocations in body
     */
    @GetMapping("/g-other-voucher-detail-expense-allocations")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailExpenseAllocation>> getAllGOtherVoucherDetailExpenseAllocations(Pageable pageable) {
        log.debug("REST request to get a page of GOtherVoucherDetailExpenseAllocations");
        Page<GOtherVoucherDetailExpenseAllocation> page = gOtherVoucherDetailExpenseAllocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-voucher-detail-expense-allocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /g-other-voucher-detail-expense-allocations/:id : get the "id" gOtherVoucherDetailExpenseAllocation.
     *
     * @param id the id of the gOtherVoucherDetailExpenseAllocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gOtherVoucherDetailExpenseAllocation, or with status 404 (Not Found)
     */
    @GetMapping("/g-other-voucher-detail-expense-allocations/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpenseAllocation> getGOtherVoucherDetailExpenseAllocation(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucherDetailExpenseAllocation : {}", id);
        Optional<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocation = gOtherVoucherDetailExpenseAllocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gOtherVoucherDetailExpenseAllocation);
    }

    /**
     * DELETE  /g-other-voucher-detail-expense-allocations/:id : delete the "id" gOtherVoucherDetailExpenseAllocation.
     *
     * @param id the id of the gOtherVoucherDetailExpenseAllocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/g-other-voucher-detail-expense-allocations/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucherDetailExpenseAllocation(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucherDetailExpenseAllocation : {}", id);
        gOtherVoucherDetailExpenseAllocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
