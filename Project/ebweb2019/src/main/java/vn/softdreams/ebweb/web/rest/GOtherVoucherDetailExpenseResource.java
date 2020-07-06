package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;
import vn.softdreams.ebweb.service.GOtherVoucherDetailExpenseService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing GOtherVoucherDetailExpense.
 */
@RestController
@RequestMapping("/api")
public class GOtherVoucherDetailExpenseResource {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailExpenseResource.class);

    private static final String ENTITY_NAME = "gOtherVoucherDetailExpense";

    private final GOtherVoucherDetailExpenseService gOtherVoucherDetailExpenseService;

    public GOtherVoucherDetailExpenseResource(GOtherVoucherDetailExpenseService gOtherVoucherDetailExpenseService) {
        this.gOtherVoucherDetailExpenseService = gOtherVoucherDetailExpenseService;
    }

    /**
     * POST  /g-other-voucher-detail-expenses : Create a new gOtherVoucherDetailExpense.
     *
     * @param gOtherVoucherDetailExpense the gOtherVoucherDetailExpense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gOtherVoucherDetailExpense, or with status 400 (Bad Request) if the gOtherVoucherDetailExpense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/g-other-voucher-detail-expenses")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpense> createGOtherVoucherDetailExpense(@RequestBody GOtherVoucherDetailExpense gOtherVoucherDetailExpense) throws URISyntaxException {
        log.debug("REST request to save GOtherVoucherDetailExpense : {}", gOtherVoucherDetailExpense);
        if (gOtherVoucherDetailExpense.getId() != null) {
            throw new BadRequestAlertException("A new gOtherVoucherDetailExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherDetailExpense result = gOtherVoucherDetailExpenseService.save(gOtherVoucherDetailExpense);
        return ResponseEntity.created(new URI("/api/g-other-voucher-detail-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /g-other-voucher-detail-expenses : Updates an existing gOtherVoucherDetailExpense.
     *
     * @param gOtherVoucherDetailExpense the gOtherVoucherDetailExpense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gOtherVoucherDetailExpense,
     * or with status 400 (Bad Request) if the gOtherVoucherDetailExpense is not valid,
     * or with status 500 (Internal Server Error) if the gOtherVoucherDetailExpense couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/g-other-voucher-detail-expenses")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpense> updateGOtherVoucherDetailExpense(@RequestBody GOtherVoucherDetailExpense gOtherVoucherDetailExpense) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucherDetailExpense : {}", gOtherVoucherDetailExpense);
        if (gOtherVoucherDetailExpense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GOtherVoucherDetailExpense result = gOtherVoucherDetailExpenseService.save(gOtherVoucherDetailExpense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gOtherVoucherDetailExpense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /g-other-voucher-detail-expenses : get all the gOtherVoucherDetailExpenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gOtherVoucherDetailExpenses in body
     */
    @GetMapping("/g-other-voucher-detail-expenses")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailExpense>> getAllGOtherVoucherDetailExpenses(Pageable pageable) {
        log.debug("REST request to get a page of GOtherVoucherDetailExpenses");
        Page<GOtherVoucherDetailExpense> page = gOtherVoucherDetailExpenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-voucher-detail-expenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /g-other-voucher-detail-expenses/:id : get the "id" gOtherVoucherDetailExpense.
     *
     * @param id the id of the gOtherVoucherDetailExpense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gOtherVoucherDetailExpense, or with status 404 (Not Found)
     */
    @GetMapping("/g-other-voucher-detail-expenses/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherDetailExpense> getGOtherVoucherDetailExpense(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucherDetailExpense : {}", id);
        Optional<GOtherVoucherDetailExpense> gOtherVoucherDetailExpense = gOtherVoucherDetailExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gOtherVoucherDetailExpense);
    }

    /**
     * DELETE  /g-other-voucher-detail-expenses/:id : delete the "id" gOtherVoucherDetailExpense.
     *
     * @param id the id of the gOtherVoucherDetailExpense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/g-other-voucher-detail-expenses/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucherDetailExpense(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucherDetailExpense : {}", id);
        gOtherVoucherDetailExpenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
