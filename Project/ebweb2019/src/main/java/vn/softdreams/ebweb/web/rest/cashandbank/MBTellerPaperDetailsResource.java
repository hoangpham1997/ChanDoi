package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBTellerPaperDetails;
import vn.softdreams.ebweb.service.MBTellerPaperDetailsService;
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
 * REST controller for managing MBTellerPaperDetails.
 */
@RestController
@RequestMapping("/api")
public class MBTellerPaperDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailsResource.class);

    private static final String ENTITY_NAME = "mBTellerPaperDetails";

    private final MBTellerPaperDetailsService mBTellerPaperDetailsService;

    public MBTellerPaperDetailsResource(MBTellerPaperDetailsService mBTellerPaperDetailsService) {
        this.mBTellerPaperDetailsService = mBTellerPaperDetailsService;
    }

    /**
     * POST  /mb-teller-paper-details : Create a new mBTellerPaperDetails.
     *
     * @param mBTellerPaperDetails the mBTellerPaperDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBTellerPaperDetails, or with status 400 (Bad Request) if the mBTellerPaperDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-teller-paper-details")
    @Timed
    public ResponseEntity<MBTellerPaperDetails> createMBTellerPaperDetails(@Valid @RequestBody MBTellerPaperDetails mBTellerPaperDetails)
        throws URISyntaxException {
        log.debug("REST request to save MBTellerPaperDetails : {}", mBTellerPaperDetails);
        if (mBTellerPaperDetails.getId() != null) {
            throw new BadRequestAlertException("A new mBTellerPaperDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBTellerPaperDetails result = mBTellerPaperDetailsService.save(mBTellerPaperDetails);
        return ResponseEntity.created(new URI("/api/mb-teller-paper-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-teller-paper-details : Updates an existing mBTellerPaperDetails.
     *
     * @param mBTellerPaperDetails the mBTellerPaperDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBTellerPaperDetails,
     * or with status 400 (Bad Request) if the mBTellerPaperDetails is not valid,
     * or with status 500 (Internal Server Error) if the mBTellerPaperDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-teller-paper-details")
    @Timed
    public ResponseEntity<MBTellerPaperDetails> updateMBTellerPaperDetails(@Valid @RequestBody MBTellerPaperDetails mBTellerPaperDetails) throws URISyntaxException {
        log.debug("REST request to update MBTellerPaperDetails : {}", mBTellerPaperDetails);
        if (mBTellerPaperDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBTellerPaperDetails result = mBTellerPaperDetailsService.save(mBTellerPaperDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBTellerPaperDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-teller-paper-details : get all the mBTellerPaperDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBTellerPaperDetails in body
     */
    @GetMapping("/mb-teller-paper-details")
    @Timed
    public ResponseEntity<List<MBTellerPaperDetails>> getAllMBTellerPaperDetails(Pageable pageable) {
        log.debug("REST request to get a page of MBTellerPaperDetails");
        Page<MBTellerPaperDetails> page = mBTellerPaperDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-paper-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-teller-paper-details/:id : get the "id" mBTellerPaperDetails.
     *
     * @param id the id of the mBTellerPaperDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaperDetails, or with status 404 (Not Found)
     */
    @GetMapping("/mb-teller-paper-details/{id}")
    @Timed
    public ResponseEntity<MBTellerPaperDetails> getMBTellerPaperDetails(@PathVariable UUID id) {
        log.debug("REST request to get MBTellerPaperDetails : {}", id);
        Optional<MBTellerPaperDetails> mBTellerPaperDetails = mBTellerPaperDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBTellerPaperDetails);
    }

    /**
     * DELETE  /mb-teller-paper-details/:id : delete the "id" mBTellerPaperDetails.
     *
     * @param id the id of the mBTellerPaperDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-teller-paper-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBTellerPaperDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MBTellerPaperDetails : {}", id);
        mBTellerPaperDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
