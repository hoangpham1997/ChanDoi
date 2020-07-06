package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAuditDetails;
import vn.softdreams.ebweb.service.TIAuditDetailsService;
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
 * REST controller for managing TIAuditDetails.
 */
@RestController
@RequestMapping("/api")
public class TIAuditDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIAuditDetailsResource.class);

    private static final String ENTITY_NAME = "tIAuditDetails";

    private final TIAuditDetailsService tIAuditDetailsService;

    public TIAuditDetailsResource(TIAuditDetailsService tIAuditDetailsService) {
        this.tIAuditDetailsService = tIAuditDetailsService;
    }

    /**
     * POST  /t-i-audit-details : Create a new tIAuditDetails.
     *
     * @param tIAuditDetails the tIAuditDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAuditDetails, or with status 400 (Bad Request) if the tIAuditDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-audit-details")
    @Timed
    public ResponseEntity<TIAuditDetails> createTIAuditDetails(@Valid @RequestBody TIAuditDetails tIAuditDetails) throws URISyntaxException {
        log.debug("REST request to save TIAuditDetails : {}", tIAuditDetails);
        if (tIAuditDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIAuditDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAuditDetails result = tIAuditDetailsService.save(tIAuditDetails);
        return ResponseEntity.created(new URI("/api/t-i-audit-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-audit-details : Updates an existing tIAuditDetails.
     *
     * @param tIAuditDetails the tIAuditDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAuditDetails,
     * or with status 400 (Bad Request) if the tIAuditDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIAuditDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-audit-details")
    @Timed
    public ResponseEntity<TIAuditDetails> updateTIAuditDetails(@Valid @RequestBody TIAuditDetails tIAuditDetails) throws URISyntaxException {
        log.debug("REST request to update TIAuditDetails : {}", tIAuditDetails);
        if (tIAuditDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAuditDetails result = tIAuditDetailsService.save(tIAuditDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAuditDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-audit-details : get all the tIAuditDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAuditDetails in body
     */
    @GetMapping("/t-i-audit-details")
    @Timed
    public ResponseEntity<List<TIAuditDetails>> getAllTIAuditDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIAuditDetails");
        Page<TIAuditDetails> page = tIAuditDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-audit-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-audit-details/:id : get the "id" tIAuditDetails.
     *
     * @param id the id of the tIAuditDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAuditDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-audit-details/{id}")
    @Timed
    public ResponseEntity<TIAuditDetails> getTIAuditDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIAuditDetails : {}", id);
        Optional<TIAuditDetails> tIAuditDetails = tIAuditDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAuditDetails);
    }

    /**
     * DELETE  /t-i-audit-details/:id : delete the "id" tIAuditDetails.
     *
     * @param id the id of the tIAuditDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-audit-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAuditDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIAuditDetails : {}", id);
        tIAuditDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
