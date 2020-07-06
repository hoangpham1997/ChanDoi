package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAdjustmentDetails;
import vn.softdreams.ebweb.service.TIAdjustmentDetailsService;
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
 * REST controller for managing TIAdjustmentDetails.
 */
@RestController
@RequestMapping("/api")
public class TIAdjustmentDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIAdjustmentDetailsResource.class);

    private static final String ENTITY_NAME = "tIAdjustmentDetails";

    private final TIAdjustmentDetailsService tIAdjustmentDetailsService;

    public TIAdjustmentDetailsResource(TIAdjustmentDetailsService tIAdjustmentDetailsService) {
        this.tIAdjustmentDetailsService = tIAdjustmentDetailsService;
    }

    /**
     * POST  /t-i-adjustment-details : Create a new tIAdjustmentDetails.
     *
     * @param tIAdjustmentDetails the tIAdjustmentDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAdjustmentDetails, or with status 400 (Bad Request) if the tIAdjustmentDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-adjustment-details")
    @Timed
    public ResponseEntity<TIAdjustmentDetails> createTIAdjustmentDetails(@Valid @RequestBody TIAdjustmentDetails tIAdjustmentDetails) throws URISyntaxException {
        log.debug("REST request to save TIAdjustmentDetails : {}", tIAdjustmentDetails);
        if (tIAdjustmentDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIAdjustmentDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAdjustmentDetails result = tIAdjustmentDetailsService.save(tIAdjustmentDetails);
        return ResponseEntity.created(new URI("/api/t-i-adjustment-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-adjustment-details : Updates an existing tIAdjustmentDetails.
     *
     * @param tIAdjustmentDetails the tIAdjustmentDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAdjustmentDetails,
     * or with status 400 (Bad Request) if the tIAdjustmentDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIAdjustmentDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-adjustment-details")
    @Timed
    public ResponseEntity<TIAdjustmentDetails> updateTIAdjustmentDetails(@Valid @RequestBody TIAdjustmentDetails tIAdjustmentDetails) throws URISyntaxException {
        log.debug("REST request to update TIAdjustmentDetails : {}", tIAdjustmentDetails);
        if (tIAdjustmentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAdjustmentDetails result = tIAdjustmentDetailsService.save(tIAdjustmentDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAdjustmentDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-adjustment-details : get all the tIAdjustmentDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAdjustmentDetails in body
     */
    @GetMapping("/t-i-adjustment-details")
    @Timed
    public ResponseEntity<List<TIAdjustmentDetails>> getAllTIAdjustmentDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIAdjustmentDetails");
        Page<TIAdjustmentDetails> page = tIAdjustmentDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-adjustment-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-adjustment-details/:id : get the "id" tIAdjustmentDetails.
     *
     * @param id the id of the tIAdjustmentDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAdjustmentDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-adjustment-details/{id}")
    @Timed
    public ResponseEntity<TIAdjustmentDetails> getTIAdjustmentDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIAdjustmentDetails : {}", id);
        Optional<TIAdjustmentDetails> tIAdjustmentDetails = tIAdjustmentDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAdjustmentDetails);
    }

    /**
     * DELETE  /t-i-adjustment-details/:id : delete the "id" tIAdjustmentDetails.
     *
     * @param id the id of the tIAdjustmentDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-adjustment-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAdjustmentDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIAdjustmentDetails : {}", id);
        tIAdjustmentDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
