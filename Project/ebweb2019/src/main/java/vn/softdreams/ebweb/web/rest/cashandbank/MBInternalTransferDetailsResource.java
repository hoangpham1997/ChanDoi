package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBInternalTransferDetails;
import vn.softdreams.ebweb.service.MBInternalTransferDetailsService;
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
 * REST controller for managing MBInternalTransferDetails.
 */
@RestController
@RequestMapping("/api")
public class MBInternalTransferDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferDetailsResource.class);

    private static final String ENTITY_NAME = "mBInternalTransferDetails";

    private final MBInternalTransferDetailsService mBInternalTransferDetailsService;

    public MBInternalTransferDetailsResource(MBInternalTransferDetailsService mBInternalTransferDetailsService) {
        this.mBInternalTransferDetailsService = mBInternalTransferDetailsService;
    }

    /**
     * POST  /mb-internal-transfer-details : Create a new mBInternalTransferDetails.
     *
     * @param mBInternalTransferDetails the mBInternalTransferDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBInternalTransferDetails, or with status 400 (Bad Request) if the mBInternalTransferDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-internal-transfer-details")
    @Timed
    public ResponseEntity<MBInternalTransferDetails> createMBInternalTransferDetails(@Valid @RequestBody MBInternalTransferDetails mBInternalTransferDetails) throws URISyntaxException {
        log.debug("REST request to save MBInternalTransferDetails : {}", mBInternalTransferDetails);
        if (mBInternalTransferDetails.getId() != null) {
            throw new BadRequestAlertException("A new mBInternalTransferDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBInternalTransferDetails result = mBInternalTransferDetailsService.save(mBInternalTransferDetails);
        return ResponseEntity.created(new URI("/api/mb-internal-transfer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-internal-transfer-details : Updates an existing mBInternalTransferDetails.
     *
     * @param mBInternalTransferDetails the mBInternalTransferDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBInternalTransferDetails,
     * or with status 400 (Bad Request) if the mBInternalTransferDetails is not valid,
     * or with status 500 (Internal Server Error) if the mBInternalTransferDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-internal-transfer-details")
    @Timed
    public ResponseEntity<MBInternalTransferDetails> updateMBInternalTransferDetails(@Valid @RequestBody MBInternalTransferDetails mBInternalTransferDetails) throws URISyntaxException {
        log.debug("REST request to update MBInternalTransferDetails : {}", mBInternalTransferDetails);
        if (mBInternalTransferDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBInternalTransferDetails result = mBInternalTransferDetailsService.save(mBInternalTransferDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBInternalTransferDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-internal-transfer-details : get all the mBInternalTransferDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBInternalTransferDetails in body
     */
    @GetMapping("/mb-internal-transfer-details")
    @Timed
    public ResponseEntity<List<MBInternalTransferDetails>> getAllMBInternalTransferDetails(Pageable pageable) {
        log.debug("REST request to get a page of MBInternalTransferDetails");
        Page<MBInternalTransferDetails> page = mBInternalTransferDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-internal-transfer-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-internal-transfer-details/:id : get the "id" mBInternalTransferDetails.
     *
     * @param id the id of the mBInternalTransferDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBInternalTransferDetails, or with status 404 (Not Found)
     */
    @GetMapping("/mb-internal-transfer-details/{id}")
    @Timed
    public ResponseEntity<MBInternalTransferDetails> getMBInternalTransferDetails(@PathVariable UUID id) {
        log.debug("REST request to get MBInternalTransferDetails : {}", id);
        Optional<MBInternalTransferDetails> mBInternalTransferDetails = mBInternalTransferDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBInternalTransferDetails);
    }

    /**
     * DELETE  /mb-internal-transfer-details/:id : delete the "id" mBInternalTransferDetails.
     *
     * @param id the id of the mBInternalTransferDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-internal-transfer-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBInternalTransferDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MBInternalTransferDetails : {}", id);
        mBInternalTransferDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
