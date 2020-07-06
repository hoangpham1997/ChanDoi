package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAllocationDetails;
import vn.softdreams.ebweb.service.TIAllocationDetailsService;
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
 * REST controller for managing TIAllocationDetails.
 */
@RestController
@RequestMapping("/api")
public class TIAllocationDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIAllocationDetailsResource.class);

    private static final String ENTITY_NAME = "tIAllocationDetails";

    private final TIAllocationDetailsService tIAllocationDetailsService;

    public TIAllocationDetailsResource(TIAllocationDetailsService tIAllocationDetailsService) {
        this.tIAllocationDetailsService = tIAllocationDetailsService;
    }

    /**
     * POST  /t-i-allocation-details : Create a new tIAllocationDetails.
     *
     * @param tIAllocationDetails the tIAllocationDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAllocationDetails, or with status 400 (Bad Request) if the tIAllocationDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-allocation-details")
    @Timed
    public ResponseEntity<TIAllocationDetails> createTIAllocationDetails(@Valid @RequestBody TIAllocationDetails tIAllocationDetails) throws URISyntaxException {
        log.debug("REST request to save TIAllocationDetails : {}", tIAllocationDetails);
        if (tIAllocationDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIAllocationDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAllocationDetails result = tIAllocationDetailsService.save(tIAllocationDetails);
        return ResponseEntity.created(new URI("/api/t-i-allocation-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-allocation-details : Updates an existing tIAllocationDetails.
     *
     * @param tIAllocationDetails the tIAllocationDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAllocationDetails,
     * or with status 400 (Bad Request) if the tIAllocationDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIAllocationDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-allocation-details")
    @Timed
    public ResponseEntity<TIAllocationDetails> updateTIAllocationDetails(@Valid @RequestBody TIAllocationDetails tIAllocationDetails) throws URISyntaxException {
        log.debug("REST request to update TIAllocationDetails : {}", tIAllocationDetails);
        if (tIAllocationDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAllocationDetails result = tIAllocationDetailsService.save(tIAllocationDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAllocationDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-allocation-details : get all the tIAllocationDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAllocationDetails in body
     */
    @GetMapping("/t-i-allocation-details")
    @Timed
    public ResponseEntity<List<TIAllocationDetails>> getAllTIAllocationDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIAllocationDetails");
        Page<TIAllocationDetails> page = tIAllocationDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-allocation-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-allocation-details/:id : get the "id" tIAllocationDetails.
     *
     * @param id the id of the tIAllocationDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAllocationDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-allocation-details/{id}")
    @Timed
    public ResponseEntity<TIAllocationDetails> getTIAllocationDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIAllocationDetails : {}", id);
        Optional<TIAllocationDetails> tIAllocationDetails = tIAllocationDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAllocationDetails);
    }

    /**
     * DELETE  /t-i-allocation-details/:id : delete the "id" tIAllocationDetails.
     *
     * @param id the id of the tIAllocationDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-allocation-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAllocationDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIAllocationDetails : {}", id);
        tIAllocationDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
