package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIIncrementDetails;
import vn.softdreams.ebweb.service.TIIncrementDetailsService;
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
 * REST controller for managing TIIncrementDetails.
 */
@RestController
@RequestMapping("/api")
public class TIIncrementDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIIncrementDetailsResource.class);

    private static final String ENTITY_NAME = "tIIncrementDetails";

    private final TIIncrementDetailsService tIIncrementDetailsService;

    public TIIncrementDetailsResource(TIIncrementDetailsService tIIncrementDetailsService) {
        this.tIIncrementDetailsService = tIIncrementDetailsService;
    }

    /**
     * POST  /t-i-increment-details : Create a new tIIncrementDetails.
     *
     * @param tIIncrementDetails the tIIncrementDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIIncrementDetails, or with status 400 (Bad Request) if the tIIncrementDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-increment-details")
    @Timed
    public ResponseEntity<TIIncrementDetails> createTIIncrementDetails(@Valid @RequestBody TIIncrementDetails tIIncrementDetails) throws URISyntaxException {
        log.debug("REST request to save TIIncrementDetails : {}", tIIncrementDetails);
        if (tIIncrementDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIIncrementDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIIncrementDetails result = tIIncrementDetailsService.save(tIIncrementDetails);
        return ResponseEntity.created(new URI("/api/t-i-increment-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-increment-details : Updates an existing tIIncrementDetails.
     *
     * @param tIIncrementDetails the tIIncrementDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIIncrementDetails,
     * or with status 400 (Bad Request) if the tIIncrementDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIIncrementDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-increment-details")
    @Timed
    public ResponseEntity<TIIncrementDetails> updateTIIncrementDetails(@Valid @RequestBody TIIncrementDetails tIIncrementDetails) throws URISyntaxException {
        log.debug("REST request to update TIIncrementDetails : {}", tIIncrementDetails);
        if (tIIncrementDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIIncrementDetails result = tIIncrementDetailsService.save(tIIncrementDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIIncrementDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-increment-details : get all the tIIncrementDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIIncrementDetails in body
     */
    @GetMapping("/t-i-increment-details")
    @Timed
    public ResponseEntity<List<TIIncrementDetails>> getAllTIIncrementDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIIncrementDetails");
        Page<TIIncrementDetails> page = tIIncrementDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increment-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-increment-details/:id : get the "id" tIIncrementDetails.
     *
     * @param id the id of the tIIncrementDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIIncrementDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-increment-details/{id}")
    @Timed
    public ResponseEntity<TIIncrementDetails> getTIIncrementDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIIncrementDetails : {}", id);
        Optional<TIIncrementDetails> tIIncrementDetails = tIIncrementDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIIncrementDetails);
    }

    /**
     * DELETE  /t-i-increment-details/:id : delete the "id" tIIncrementDetails.
     *
     * @param id the id of the tIIncrementDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-increment-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIIncrementDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIIncrementDetails : {}", id);
        tIIncrementDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
