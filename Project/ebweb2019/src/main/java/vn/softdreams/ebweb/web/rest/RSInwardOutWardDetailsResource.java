package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.service.RSInwardOutWardDetailsService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RSInwardOutWardDetails.
 */
@RestController
@RequestMapping("/api")
public class RSInwardOutWardDetailsResource {

    private final Logger log = LoggerFactory.getLogger(RSInwardOutWardDetailsResource.class);

    private static final String ENTITY_NAME = "rSInwardOutWardDetails";

    private final RSInwardOutWardDetailsService rSInwardOutWardDetailsService;

    public RSInwardOutWardDetailsResource(RSInwardOutWardDetailsService rSInwardOutWardDetailsService) {
        this.rSInwardOutWardDetailsService = rSInwardOutWardDetailsService;
    }

    /**
     * POST  /rs-inward-out-ward-details : Create a new rSInwardOutWardDetails.
     *
     * @param rSInwardOutWardDetails the rSInwardOutWardDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rSInwardOutWardDetails, or with status 400 (Bad Request) if the rSInwardOutWardDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rs-inward-out-ward-details")
    @Timed
    public ResponseEntity<RSInwardOutWardDetails> createRSInwardOutWardDetails(@RequestBody RSInwardOutWardDetails rSInwardOutWardDetails) throws URISyntaxException {
        log.debug("REST request to save RSInwardOutWardDetails : {}", rSInwardOutWardDetails);
        if (rSInwardOutWardDetails.getId() != null) {
            throw new BadRequestAlertException("A new rSInwardOutWardDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RSInwardOutWardDetails result = rSInwardOutWardDetailsService.save(rSInwardOutWardDetails);
        return ResponseEntity.created(new URI("/api/rs-inward-out-ward-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rs-inward-out-ward-details : Updates an existing rSInwardOutWardDetails.
     *
     * @param rSInwardOutWardDetails the rSInwardOutWardDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rSInwardOutWardDetails,
     * or with status 400 (Bad Request) if the rSInwardOutWardDetails is not valid,
     * or with status 500 (Internal Server Error) if the rSInwardOutWardDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rs-inward-out-ward-details")
    @Timed
    public ResponseEntity<RSInwardOutWardDetails> updateRSInwardOutWardDetails(@RequestBody RSInwardOutWardDetails rSInwardOutWardDetails) throws URISyntaxException {
        log.debug("REST request to update RSInwardOutWardDetails : {}", rSInwardOutWardDetails);
        if (rSInwardOutWardDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RSInwardOutWardDetails result = rSInwardOutWardDetailsService.save(rSInwardOutWardDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rSInwardOutWardDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rs-inward-out-ward-details : get all the rSInwardOutWardDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rSInwardOutWardDetails in body
     */
    @GetMapping("/rs-inward-out-ward-details")
    @Timed
    public ResponseEntity<List<RSInwardOutWardDetails>> getAllRSInwardOutWardDetails(Pageable pageable) {
        log.debug("REST request to get a page of RSInwardOutWardDetails");
        Page<RSInwardOutWardDetails> page = rSInwardOutWardDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rs-inward-out-ward-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rs-inward-out-ward-details/:id : get the "id" rSInwardOutWardDetails.
     *
     * @param id the id of the rSInwardOutWardDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rSInwardOutWardDetails, or with status 404 (Not Found)
     */
    @GetMapping("/rs-inward-out-ward-details/{id}")
    @Timed
    public ResponseEntity<RSInwardOutWardDetails> getRSInwardOutWardDetails(@PathVariable Long id) {
        log.debug("REST request to get RSInwardOutWardDetails : {}", id);
        Optional<RSInwardOutWardDetails> rSInwardOutWardDetails = rSInwardOutWardDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rSInwardOutWardDetails);
    }

    /**
     * DELETE  /rs-inward-out-ward-details/:id : delete the "id" rSInwardOutWardDetails.
     *
     * @param id the id of the rSInwardOutWardDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rs-inward-out-ward-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteRSInwardOutWardDetails(@PathVariable Long id) {
        log.debug("REST request to delete RSInwardOutWardDetails : {}", id);
        rSInwardOutWardDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
