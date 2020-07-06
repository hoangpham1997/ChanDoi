package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIDecrementDetails;
import vn.softdreams.ebweb.service.TIDecrementDetailsService;
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
 * REST controller for managing TIDecrementDetails.
 */
@RestController
@RequestMapping("/api")
public class TIDecrementDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIDecrementDetailsResource.class);

    private static final String ENTITY_NAME = "tIDecrementDetails";

    private final TIDecrementDetailsService tIDecrementDetailsService;

    public TIDecrementDetailsResource(TIDecrementDetailsService tIDecrementDetailsService) {
        this.tIDecrementDetailsService = tIDecrementDetailsService;
    }

    /**
     * POST  /t-i-decrement-details : Create a new tIDecrementDetails.
     *
     * @param tIDecrementDetails the tIDecrementDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIDecrementDetails, or with status 400 (Bad Request) if the tIDecrementDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-decrement-details")
    @Timed
    public ResponseEntity<TIDecrementDetails> createTIDecrementDetails(@Valid @RequestBody TIDecrementDetails tIDecrementDetails) throws URISyntaxException {
        log.debug("REST request to save TIDecrementDetails : {}", tIDecrementDetails);
        if (tIDecrementDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIDecrementDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIDecrementDetails result = tIDecrementDetailsService.save(tIDecrementDetails);
        return ResponseEntity.created(new URI("/api/t-i-decrement-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-decrement-details : Updates an existing tIDecrementDetails.
     *
     * @param tIDecrementDetails the tIDecrementDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIDecrementDetails,
     * or with status 400 (Bad Request) if the tIDecrementDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIDecrementDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-decrement-details")
    @Timed
    public ResponseEntity<TIDecrementDetails> updateTIDecrementDetails(@Valid @RequestBody TIDecrementDetails tIDecrementDetails) throws URISyntaxException {
        log.debug("REST request to update TIDecrementDetails : {}", tIDecrementDetails);
        if (tIDecrementDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIDecrementDetails result = tIDecrementDetailsService.save(tIDecrementDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIDecrementDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-decrement-details : get all the tIDecrementDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIDecrementDetails in body
     */
    @GetMapping("/t-i-decrement-details")
    @Timed
    public ResponseEntity<List<TIDecrementDetails>> getAllTIDecrementDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIDecrementDetails");
        Page<TIDecrementDetails> page = tIDecrementDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-decrement-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-decrement-details/:id : get the "id" tIDecrementDetails.
     *
     * @param id the id of the tIDecrementDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIDecrementDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-decrement-details/{id}")
    @Timed
    public ResponseEntity<TIDecrementDetails> getTIDecrementDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIDecrementDetails : {}", id);
        Optional<TIDecrementDetails> tIDecrementDetails = tIDecrementDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIDecrementDetails);
    }

    /**
     * DELETE  /t-i-decrement-details/:id : delete the "id" tIDecrementDetails.
     *
     * @param id the id of the tIDecrementDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-decrement-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIDecrementDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIDecrementDetails : {}", id);
        tIDecrementDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
