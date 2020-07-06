package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCPaymentDetailVendor;
import vn.softdreams.ebweb.service.MCPaymentDetailVendorService;
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
 * REST controller for managing MCPaymentDetailVendor.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentDetailVendorResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailVendorResource.class);

    private static final String ENTITY_NAME = "mCPaymentDetailVendor";

    private final MCPaymentDetailVendorService mCPaymentDetailVendorService;

    public MCPaymentDetailVendorResource(MCPaymentDetailVendorService mCPaymentDetailVendorService) {
        this.mCPaymentDetailVendorService = mCPaymentDetailVendorService;
    }

    /**
     * POST  /m-c-payment-detail-vendors : Create a new mCPaymentDetailVendor.
     *
     * @param mCPaymentDetailVendor the mCPaymentDetailVendor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPaymentDetailVendor, or with status 400 (Bad Request) if the mCPaymentDetailVendor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payment-detail-vendors")
    @Timed
    public ResponseEntity<MCPaymentDetailVendor> createMCPaymentDetailVendor(@Valid @RequestBody MCPaymentDetailVendor mCPaymentDetailVendor) throws URISyntaxException {
        log.debug("REST request to save MCPaymentDetailVendor : {}", mCPaymentDetailVendor);
        if (mCPaymentDetailVendor.getId() != null) {
            throw new BadRequestAlertException("A new mCPaymentDetailVendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentDetailVendor result = mCPaymentDetailVendorService.save(mCPaymentDetailVendor);
        return ResponseEntity.created(new URI("/api/m-c-payment-detail-vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-payment-detail-vendors : Updates an existing mCPaymentDetailVendor.
     *
     * @param mCPaymentDetailVendor the mCPaymentDetailVendor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPaymentDetailVendor,
     * or with status 400 (Bad Request) if the mCPaymentDetailVendor is not valid,
     * or with status 500 (Internal Server Error) if the mCPaymentDetailVendor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payment-detail-vendors")
    @Timed
    public ResponseEntity<MCPaymentDetailVendor> updateMCPaymentDetailVendor(@Valid @RequestBody MCPaymentDetailVendor mCPaymentDetailVendor) throws URISyntaxException {
        log.debug("REST request to update MCPaymentDetailVendor : {}", mCPaymentDetailVendor);
        if (mCPaymentDetailVendor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentDetailVendor result = mCPaymentDetailVendorService.save(mCPaymentDetailVendor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPaymentDetailVendor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payment-detail-vendors : get all the mCPaymentDetailVendors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPaymentDetailVendors in body
     */
    @GetMapping("/m-c-payment-detail-vendors")
    @Timed
    public ResponseEntity<List<MCPaymentDetailVendor>> getAllMCPaymentDetailVendors(Pageable pageable) {
        log.debug("REST request to get a page of MCPaymentDetailVendors");
        Page<MCPaymentDetailVendor> page = mCPaymentDetailVendorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payment-detail-vendors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payment-detail-vendors/:id : get the "id" mCPaymentDetailVendor.
     *
     * @param id the id of the mCPaymentDetailVendor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPaymentDetailVendor, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payment-detail-vendors/{id}")
    @Timed
    public ResponseEntity<MCPaymentDetailVendor> getMCPaymentDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to get MCPaymentDetailVendor : {}", id);
        Optional<MCPaymentDetailVendor> mCPaymentDetailVendor = mCPaymentDetailVendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPaymentDetailVendor);
    }

    /**
     * DELETE  /m-c-payment-detail-vendors/:id : delete the "id" mCPaymentDetailVendor.
     *
     * @param id the id of the mCPaymentDetailVendor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payment-detail-vendors/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPaymentDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to delete MCPaymentDetailVendor : {}", id);
        mCPaymentDetailVendorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
