package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TITransferDetails;
import vn.softdreams.ebweb.service.TITransferDetailsService;
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
 * REST controller for managing TITransferDetails.
 */
@RestController
@RequestMapping("/api")
public class TITransferDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TITransferDetailsResource.class);

    private static final String ENTITY_NAME = "tITransferDetails";

    private final TITransferDetailsService tITransferDetailsService;

    public TITransferDetailsResource(TITransferDetailsService tITransferDetailsService) {
        this.tITransferDetailsService = tITransferDetailsService;
    }

    /**
     * POST  /t-i-transfer-details : Create a new tITransferDetails.
     *
     * @param tITransferDetails the tITransferDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tITransferDetails, or with status 400 (Bad Request) if the tITransferDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-transfer-details")
    @Timed
    public ResponseEntity<TITransferDetails> createTITransferDetails(@Valid @RequestBody TITransferDetails tITransferDetails) throws URISyntaxException {
        log.debug("REST request to save TITransferDetails : {}", tITransferDetails);
        if (tITransferDetails.getId() != null) {
            throw new BadRequestAlertException("A new tITransferDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TITransferDetails result = tITransferDetailsService.save(tITransferDetails);
        return ResponseEntity.created(new URI("/api/t-i-transfer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-transfer-details : Updates an existing tITransferDetails.
     *
     * @param tITransferDetails the tITransferDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tITransferDetails,
     * or with status 400 (Bad Request) if the tITransferDetails is not valid,
     * or with status 500 (Internal Server Error) if the tITransferDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-transfer-details")
    @Timed
    public ResponseEntity<TITransferDetails> updateTITransferDetails(@Valid @RequestBody TITransferDetails tITransferDetails) throws URISyntaxException {
        log.debug("REST request to update TITransferDetails : {}", tITransferDetails);
        if (tITransferDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TITransferDetails result = tITransferDetailsService.save(tITransferDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tITransferDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-transfer-details : get all the tITransferDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tITransferDetails in body
     */
    @GetMapping("/t-i-transfer-details")
    @Timed
    public ResponseEntity<List<TITransferDetails>> getAllTITransferDetails(Pageable pageable) {
        log.debug("REST request to get a page of TITransferDetails");
        Page<TITransferDetails> page = tITransferDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-transfer-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-transfer-details/:id : get the "id" tITransferDetails.
     *
     * @param id the id of the tITransferDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tITransferDetails, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-transfer-details/{id}")
    @Timed
    public ResponseEntity<TITransferDetails> getTITransferDetails(@PathVariable UUID id) {
        log.debug("REST request to get TITransferDetails : {}", id);
        Optional<TITransferDetails> tITransferDetails = tITransferDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tITransferDetails);
    }

    /**
     * DELETE  /t-i-transfer-details/:id : delete the "id" tITransferDetails.
     *
     * @param id the id of the tITransferDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-transfer-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTITransferDetails(@PathVariable UUID id) {
        log.debug("REST request to delete TITransferDetails : {}", id);
        tITransferDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
