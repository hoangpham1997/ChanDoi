package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.service.MCPaymentDetailsService;
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
 * REST controller for managing MCPaymentDetails.
 */
@RestController
@RequestMapping("/api")
public class MCPaymentDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailsResource.class);

    private static final String ENTITY_NAME = "mCPaymentDetails";

    private final MCPaymentDetailsService mCPaymentDetailsService;

    public MCPaymentDetailsResource(MCPaymentDetailsService mCPaymentDetailsService) {
        this.mCPaymentDetailsService = mCPaymentDetailsService;
    }

    /**
     * POST  /m-c-payment-details : Create a new mCPaymentDetails.
     *
     * @param mCPaymentDetails the mCPaymentDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCPaymentDetails, or with status 400 (Bad Request) if the mCPaymentDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-payment-details")
    @Timed
    public ResponseEntity<MCPaymentDetails> createMCPaymentDetails(@Valid @RequestBody MCPaymentDetails mCPaymentDetails) throws URISyntaxException {
        log.debug("REST request to save MCPaymentDetails : {}", mCPaymentDetails);
        if (mCPaymentDetails.getId() != null) {
            throw new BadRequestAlertException("A new mCPaymentDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCPaymentDetails result = mCPaymentDetailsService.save(mCPaymentDetails);
        return ResponseEntity.created(new URI("/api/m-c-payment-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-payment-details : Updates an existing mCPaymentDetails.
     *
     * @param mCPaymentDetails the mCPaymentDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCPaymentDetails,
     * or with status 400 (Bad Request) if the mCPaymentDetails is not valid,
     * or with status 500 (Internal Server Error) if the mCPaymentDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-payment-details")
    @Timed
    public ResponseEntity<MCPaymentDetails> updateMCPaymentDetails(@Valid @RequestBody MCPaymentDetails mCPaymentDetails) throws URISyntaxException {
        log.debug("REST request to update MCPaymentDetails : {}", mCPaymentDetails);
        if (mCPaymentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCPaymentDetails result = mCPaymentDetailsService.save(mCPaymentDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCPaymentDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-payment-details : get all the mCPaymentDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCPaymentDetails in body
     */
    @GetMapping("/m-c-payment-details")
    @Timed
    public ResponseEntity<List<MCPaymentDetails>> getAllMCPaymentDetails(Pageable pageable) {
        log.debug("REST request to get a page of MCPaymentDetails");
        Page<MCPaymentDetails> page = mCPaymentDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-payment-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-payment-details/:id : get the "id" mCPaymentDetails.
     *
     * @param id the id of the mCPaymentDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCPaymentDetails, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-payment-details/{id}")
    @Timed
    public ResponseEntity<MCPaymentDetails> getMCPaymentDetails(@PathVariable UUID id) {
        log.debug("REST request to get MCPaymentDetails : {}", id);
        Optional<MCPaymentDetails> mCPaymentDetails = mCPaymentDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCPaymentDetails);
    }

    /**
     * DELETE  /m-c-payment-details/:id : delete the "id" mCPaymentDetails.
     *
     * @param id the id of the mCPaymentDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-payment-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCPaymentDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MCPaymentDetails : {}", id);
        mCPaymentDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
