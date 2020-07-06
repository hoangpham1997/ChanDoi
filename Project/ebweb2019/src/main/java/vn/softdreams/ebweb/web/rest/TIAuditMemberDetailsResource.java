package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAuditMemberDetails;
import vn.softdreams.ebweb.service.TIAuditMemberDetailsService;
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
 * REST controller for managing TIAuditMemberDetails.
 */
@RestController
@RequestMapping("/api")
public class TIAuditMemberDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TIAuditMemberDetailsResource.class);

    private static final String ENTITY_NAME = "tIAuditMemberDetails";

    private final TIAuditMemberDetailsService tIAuditMemberDetailsService;

    public TIAuditMemberDetailsResource(TIAuditMemberDetailsService tIAuditMemberDetailsService) {
        this.tIAuditMemberDetailsService = tIAuditMemberDetailsService;
    }

    /**
     * POST  /t-i-audit-member-details : Create a new tIAuditMemberDetails.
     *
     * @param tIAuditMemberDetails the tIAuditMemberDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAuditMemberDetails, or with status 400 (Bad Request) if the tIAuditMemberDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-audit-member-details")
    @Timed
    public ResponseEntity<TIAuditMemberDetails> createTIAuditMemberDetails(@Valid @RequestBody TIAuditMemberDetails tIAuditMemberDetails) throws URISyntaxException {
        log.debug("REST request to save TIAuditMemberDetails : {}", tIAuditMemberDetails);
        if (tIAuditMemberDetails.getId() != null) {
            throw new BadRequestAlertException("A new tIAuditMemberDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAuditMemberDetails result = tIAuditMemberDetailsService.save(tIAuditMemberDetails);
        return ResponseEntity.created(new URI("/api/t-i-audit-member-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-audit-member-details : Updates an existing tIAuditMemberDetails.
     *
     * @param tIAuditMemberDetails the tIAuditMemberDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAuditMemberDetails,
     * or with status 400 (Bad Request) if the tIAuditMemberDetails is not valid,
     * or with status 500 (Internal Server Error) if the tIAuditMemberDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-audit-member-details")
    @Timed
    public ResponseEntity<TIAuditMemberDetails> updateTIAuditMemberDetails(@Valid @RequestBody TIAuditMemberDetails tIAuditMemberDetails) throws URISyntaxException {
        log.debug("REST request to update TIAuditMemberDetails : {}", tIAuditMemberDetails);
        if (tIAuditMemberDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAuditMemberDetails result = tIAuditMemberDetailsService.save(tIAuditMemberDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAuditMemberDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-audit-member-details : get all the tIAuditMemberDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAuditMemberDetails in body
     */
    @GetMapping("/t-i-audit-member-details")
    @Timed
    public ResponseEntity<List<TIAuditMemberDetails>> getAllTIAuditMemberDetails(Pageable pageable) {
        log.debug("REST request to get a page of TIAuditMemberDetails");
        Page<TIAuditMemberDetails> page = tIAuditMemberDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-audit-member-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-audit-member-details/:id : get the "id" tIAuditMemberDetails.
     *
     * @param id the id of the tIAuditMemberDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAuditMemberDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-audit-member-details/{id}")
    @Timed
    public ResponseEntity<TIAuditMemberDetails> getTIAuditMemberDetails(@PathVariable UUID id) {
        log.debug("REST request to get TIAuditMemberDetails : {}", id);
        Optional<TIAuditMemberDetails> tIAuditMemberDetails = tIAuditMemberDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAuditMemberDetails);
    }

    /**
     * DELETE  /t-i-audit-member-details/:id : delete the "id" tIAuditMemberDetails.
     *
     * @param id the id of the tIAuditMemberDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-audit-member-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAuditMemberDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TIAuditMemberDetails : {}", id);
        tIAuditMemberDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
