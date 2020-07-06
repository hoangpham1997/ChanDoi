package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.FixedAssetDetails;
import vn.softdreams.ebweb.service.FixedAssetDetailsService;
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
 * REST controller for managing FixedAssetDetails.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetDetailsResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetDetailsResource.class);

    private static final String ENTITY_NAME = "fixedAssetDetails";

    private final FixedAssetDetailsService fixedAssetDetailsService;

    public FixedAssetDetailsResource(FixedAssetDetailsService fixedAssetDetailsService) {
        this.fixedAssetDetailsService = fixedAssetDetailsService;
    }

    /**
     * POST  /fixed-asset-details : Create a new fixedAssetDetails.
     *
     * @param fixedAssetDetails the fixedAssetDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedAssetDetails, or with status 400 (Bad Request) if the fixedAssetDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fixed-asset-details")
    @Timed
    public ResponseEntity<FixedAssetDetails> createFixedAssetDetails(@Valid @RequestBody FixedAssetDetails fixedAssetDetails) throws URISyntaxException {
        log.debug("REST request to save FixedAssetDetails : {}", fixedAssetDetails);
        if (fixedAssetDetails.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetDetails result = fixedAssetDetailsService.save(fixedAssetDetails);
        return ResponseEntity.created(new URI("/api/fixed-asset-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-asset-details : Updates an existing fixedAssetDetails.
     *
     * @param fixedAssetDetails the fixedAssetDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedAssetDetails,
     * or with status 400 (Bad Request) if the fixedAssetDetails is not valid,
     * or with status 500 (Internal Server Error) if the fixedAssetDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fixed-asset-details")
    @Timed
    public ResponseEntity<FixedAssetDetails> updateFixedAssetDetails(@Valid @RequestBody FixedAssetDetails fixedAssetDetails) throws URISyntaxException {
        log.debug("REST request to update FixedAssetDetails : {}", fixedAssetDetails);
        if (fixedAssetDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAssetDetails result = fixedAssetDetailsService.save(fixedAssetDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fixedAssetDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-asset-details : get all the fixedAssetDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedAssetDetails in body
     */
    @GetMapping("/fixed-asset-details")
    @Timed
    public ResponseEntity<List<FixedAssetDetails>> getAllFixedAssetDetails(Pageable pageable) {
        log.debug("REST request to get a page of FixedAssetDetails");
        Page<FixedAssetDetails> page = fixedAssetDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-asset-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-asset-details/:id : get the "id" fixedAssetDetails.
     *
     * @param id the id of the fixedAssetDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedAssetDetails, or with status 404 (Not Found)
     */
    @GetMapping("/fixed-asset-details/{id}")
    @Timed
    public ResponseEntity<FixedAssetDetails> getFixedAssetDetails(@PathVariable UUID id) {
        log.debug("REST request to get FixedAssetDetails : {}", id);
        Optional<FixedAssetDetails> fixedAssetDetails = fixedAssetDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetDetails);
    }

    /**
     * DELETE  /fixed-asset-details/:id : delete the "id" fixedAssetDetails.
     *
     * @param id the id of the fixedAssetDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fixed-asset-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteFixedAssetDetails(@PathVariable UUID id) {
        log.debug("REST request to delete FixedAssetDetails : {}", id);
        fixedAssetDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
