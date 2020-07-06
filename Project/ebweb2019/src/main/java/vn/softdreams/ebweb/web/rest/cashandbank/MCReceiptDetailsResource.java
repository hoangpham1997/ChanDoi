package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCReceiptDetails;
import vn.softdreams.ebweb.service.MCReceiptDetailsService;
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
 * REST controller for managing MCReceiptDetails.
 */
@RestController
@RequestMapping("/api")
public class MCReceiptDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailsResource.class);

    private static final String ENTITY_NAME = "mCReceiptDetails";

    private final MCReceiptDetailsService mCReceiptDetailsService;

    public MCReceiptDetailsResource(MCReceiptDetailsService mCReceiptDetailsService) {
        this.mCReceiptDetailsService = mCReceiptDetailsService;
    }

    /**
     * POST  /m-c-receipt-details : Create a new mCReceiptDetails.
     *
     * @param mCReceiptDetails the mCReceiptDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCReceiptDetails, or with status 400 (Bad Request) if the mCReceiptDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-receipt-details")
    @Timed
    public ResponseEntity<MCReceiptDetails> createMCReceiptDetails(@Valid @RequestBody MCReceiptDetails mCReceiptDetails) throws URISyntaxException {
        log.debug("REST request to save MCReceiptDetails : {}", mCReceiptDetails);
        if (mCReceiptDetails.getId() != null) {
            throw new BadRequestAlertException("A new mCReceiptDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCReceiptDetails result = mCReceiptDetailsService.save(mCReceiptDetails);
        return ResponseEntity.created(new URI("/api/m-c-receipt-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-receipt-details : Updates an existing mCReceiptDetails.
     *
     * @param mCReceiptDetails the mCReceiptDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCReceiptDetails,
     * or with status 400 (Bad Request) if the mCReceiptDetails is not valid,
     * or with status 500 (Internal Server Error) if the mCReceiptDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-receipt-details")
    @Timed
    public ResponseEntity<MCReceiptDetails> updateMCReceiptDetails(@Valid @RequestBody MCReceiptDetails mCReceiptDetails) throws URISyntaxException {
        log.debug("REST request to update MCReceiptDetails : {}", mCReceiptDetails);
        if (mCReceiptDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCReceiptDetails result = mCReceiptDetailsService.save(mCReceiptDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCReceiptDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-receipt-details : get all the mCReceiptDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCReceiptDetails in body
     */
    @GetMapping("/m-c-receipt-details")
    @Timed
    public ResponseEntity<List<MCReceiptDetails>> getAllMCReceiptDetails(Pageable pageable) {
        log.debug("REST request to get a page of MCReceiptDetails");
        Page<MCReceiptDetails> page = mCReceiptDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-receipt-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-receipt-details/:id : get the "id" mCReceiptDetails.
     *
     * @param id the id of the mCReceiptDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCReceiptDetails, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-receipt-details/{id}")
    @Timed
    public ResponseEntity<MCReceiptDetails> getMCReceiptDetails(@PathVariable UUID id) {
        log.debug("REST request to get MCReceiptDetails : {}", id);
        Optional<MCReceiptDetails> mCReceiptDetails = mCReceiptDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCReceiptDetails);
    }

    /**
     * DELETE  /m-c-receipt-details/:id : delete the "id" mCReceiptDetails.
     *
     * @param id the id of the mCReceiptDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-receipt-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCReceiptDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MCReceiptDetails : {}", id);
        mCReceiptDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
