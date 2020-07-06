package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBCreditCardDetails;
import vn.softdreams.ebweb.service.MBCreditCardDetailsService;
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
 * REST controller for managing MBCreditCardDetails.
 */
@RestController
@RequestMapping("/api")
public class MBCreditCardDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailsResource.class);

    private static final String ENTITY_NAME = "mBCreditCardDetails";

    private final MBCreditCardDetailsService mBCreditCardDetailsService;

    public MBCreditCardDetailsResource(MBCreditCardDetailsService mBCreditCardDetailsService) {
        this.mBCreditCardDetailsService = mBCreditCardDetailsService;
    }

    /**
     * POST  /mb-credit-card-details : Create a new mBCreditCardDetails.
     *
     * @param mBCreditCardDetails the mBCreditCardDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBCreditCardDetails, or with status 400 (Bad Request) if the mBCreditCardDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-credit-card-details")
    @Timed
    public ResponseEntity<MBCreditCardDetails> createMBCreditCardDetails(@Valid @RequestBody MBCreditCardDetails mBCreditCardDetails) throws URISyntaxException {
        log.debug("REST request to save MBCreditCardDetails : {}", mBCreditCardDetails);
        if (mBCreditCardDetails.getId() != null) {
            throw new BadRequestAlertException("A new mBCreditCardDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBCreditCardDetails result = mBCreditCardDetailsService.save(mBCreditCardDetails);
        return ResponseEntity.created(new URI("/api/mb-credit-card-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-credit-card-details : Updates an existing mBCreditCardDetails.
     *
     * @param mBCreditCardDetails the mBCreditCardDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBCreditCardDetails,
     * or with status 400 (Bad Request) if the mBCreditCardDetails is not valid,
     * or with status 500 (Internal Server Error) if the mBCreditCardDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-credit-card-details")
    @Timed
    public ResponseEntity<MBCreditCardDetails> updateMBCreditCardDetails(@Valid @RequestBody MBCreditCardDetails mBCreditCardDetails) throws URISyntaxException {
        log.debug("REST request to update MBCreditCardDetails : {}", mBCreditCardDetails);
        if (mBCreditCardDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBCreditCardDetails result = mBCreditCardDetailsService.save(mBCreditCardDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBCreditCardDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-credit-card-details : get all the mBCreditCardDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBCreditCardDetails in body
     */
    @GetMapping("/mb-credit-card-details")
    @Timed
    public ResponseEntity<List<MBCreditCardDetails>> getAllMBCreditCardDetails(Pageable pageable) {
        log.debug("REST request to get a page of MBCreditCardDetails");
        Page<MBCreditCardDetails> page = mBCreditCardDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-credit-card-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-credit-card-details/:id : get the "id" mBCreditCardDetails.
     *
     * @param id the id of the mBCreditCardDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBCreditCardDetails, or with status 404 (Not Found)
     */
    @GetMapping("/mb-credit-card-details/{id}")
    @Timed
    public ResponseEntity<MBCreditCardDetails> getMBCreditCardDetails(@PathVariable UUID id) {
        log.debug("REST request to get MBCreditCardDetails : {}", id);
        Optional<MBCreditCardDetails> mBCreditCardDetails = mBCreditCardDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBCreditCardDetails);
    }

    /**
     * DELETE  /mb-credit-card-details/:id : delete the "id" mBCreditCardDetails.
     *
     * @param id the id of the mBCreditCardDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-credit-card-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBCreditCardDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MBCreditCardDetails : {}", id);
        mBCreditCardDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
