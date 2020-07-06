package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCReceiptDetailCustomer;
import vn.softdreams.ebweb.service.MCReceiptDetailCustomerService;
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
 * REST controller for managing MCReceiptDetailCustomer.
 */
@RestController
@RequestMapping("/api")
public class MCReceiptDetailCustomerResource {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailCustomerResource.class);

    private static final String ENTITY_NAME = "mCReceiptDetailCustomer";

    private final MCReceiptDetailCustomerService mCReceiptDetailCustomerService;

    public MCReceiptDetailCustomerResource(MCReceiptDetailCustomerService mCReceiptDetailCustomerService) {
        this.mCReceiptDetailCustomerService = mCReceiptDetailCustomerService;
    }

    /**
     * POST  /m-c-receipt-detail-customers : Create a new mCReceiptDetailCustomer.
     *
     * @param mCReceiptDetailCustomer the mCReceiptDetailCustomer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCReceiptDetailCustomer, or with status 400 (Bad Request) if the mCReceiptDetailCustomer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-receipt-detail-customers")
    @Timed
    public ResponseEntity<MCReceiptDetailCustomer> createMCReceiptDetailCustomer(@Valid @RequestBody MCReceiptDetailCustomer mCReceiptDetailCustomer) throws URISyntaxException {
        log.debug("REST request to save MCReceiptDetailCustomer : {}", mCReceiptDetailCustomer);
        if (mCReceiptDetailCustomer.getId() != null) {
            throw new BadRequestAlertException("A new mCReceiptDetailCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCReceiptDetailCustomer result = mCReceiptDetailCustomerService.save(mCReceiptDetailCustomer);
        return ResponseEntity.created(new URI("/api/m-c-receipt-detail-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-receipt-detail-customers : Updates an existing mCReceiptDetailCustomer.
     *
     * @param mCReceiptDetailCustomer the mCReceiptDetailCustomer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCReceiptDetailCustomer,
     * or with status 400 (Bad Request) if the mCReceiptDetailCustomer is not valid,
     * or with status 500 (Internal Server Error) if the mCReceiptDetailCustomer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-receipt-detail-customers")
    @Timed
    public ResponseEntity<MCReceiptDetailCustomer> updateMCReceiptDetailCustomer(@Valid @RequestBody MCReceiptDetailCustomer mCReceiptDetailCustomer) throws URISyntaxException {
        log.debug("REST request to update MCReceiptDetailCustomer : {}", mCReceiptDetailCustomer);
        if (mCReceiptDetailCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCReceiptDetailCustomer result = mCReceiptDetailCustomerService.save(mCReceiptDetailCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCReceiptDetailCustomer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-receipt-detail-customers : get all the mCReceiptDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCReceiptDetailCustomers in body
     */
    @GetMapping("/m-c-receipt-detail-customers")
    @Timed
    public ResponseEntity<List<MCReceiptDetailCustomer>> getAllMCReceiptDetailCustomers(Pageable pageable) {
        log.debug("REST request to get a page of MCReceiptDetailCustomers");
        Page<MCReceiptDetailCustomer> page = mCReceiptDetailCustomerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-receipt-detail-customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-receipt-detail-customers/:id : get the "id" mCReceiptDetailCustomer.
     *
     * @param id the id of the mCReceiptDetailCustomer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCReceiptDetailCustomer, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-receipt-detail-customers/{id}")
    @Timed
    public ResponseEntity<MCReceiptDetailCustomer> getMCReceiptDetailCustomer(@PathVariable UUID id) {
        log.debug("REST request to get MCReceiptDetailCustomer : {}", id);
        Optional<MCReceiptDetailCustomer> mCReceiptDetailCustomer = mCReceiptDetailCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCReceiptDetailCustomer);
    }

    /**
     * DELETE  /m-c-receipt-detail-customers/:id : delete the "id" mCReceiptDetailCustomer.
     *
     * @param id the id of the mCReceiptDetailCustomer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-receipt-detail-customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCReceiptDetailCustomer(@PathVariable UUID id) {
        log.debug("REST request to delete MCReceiptDetailCustomer : {}", id);
        mCReceiptDetailCustomerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
