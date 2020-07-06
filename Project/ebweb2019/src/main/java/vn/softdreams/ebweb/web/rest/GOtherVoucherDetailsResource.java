package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.service.GOtherVoucherDetailsService;
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
import java.util.UUID;

/**
 * REST controller for managing GOtherVoucherDetails.
 */
@RestController
@RequestMapping("/api")
public class GOtherVoucherDetailsResource {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailsResource.class);

    private static final String ENTITY_NAME = "gOtherVoucherDetails";

    private final GOtherVoucherDetailsService gOtherVoucherDetailsService;

    public GOtherVoucherDetailsResource(GOtherVoucherDetailsService gOtherVoucherDetailsService) {
        this.gOtherVoucherDetailsService = gOtherVoucherDetailsService;
    }

    /**
     * POST  /g-other-voucher-details : Create a new gOtherVoucherDetails.
     *
     * @param gOtherVoucherDetails the gOtherVoucherDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gOtherVoucherDetails, or with status 400 (Bad Request) if the gOtherVoucherDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/g-other-voucher-details")
    @Timed
    public ResponseEntity<GOtherVoucherDetails> createGOtherVoucherDetails(@RequestBody GOtherVoucherDetails gOtherVoucherDetails) throws URISyntaxException {
        log.debug("REST request to save GOtherVoucherDetails : {}", gOtherVoucherDetails);
        if (gOtherVoucherDetails.getId() != null) {
            throw new BadRequestAlertException("A new gOtherVoucherDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherDetails result = gOtherVoucherDetailsService.save(gOtherVoucherDetails);
        return ResponseEntity.created(new URI("/api/g-other-voucher-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /g-other-voucher-details : Updates an existing gOtherVoucherDetails.
     *
     * @param gOtherVoucherDetails the gOtherVoucherDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gOtherVoucherDetails,
     * or with status 400 (Bad Request) if the gOtherVoucherDetails is not valid,
     * or with status 500 (Internal Server Error) if the gOtherVoucherDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/g-other-voucher-details")
    @Timed
    public ResponseEntity<GOtherVoucherDetails> updateGOtherVoucherDetails(@RequestBody GOtherVoucherDetails gOtherVoucherDetails) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucherDetails : {}", gOtherVoucherDetails);
        if (gOtherVoucherDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GOtherVoucherDetails result = gOtherVoucherDetailsService.save(gOtherVoucherDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gOtherVoucherDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /g-other-voucher-details : get all the gOtherVoucherDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gOtherVoucherDetails in body
     */
    @GetMapping("/g-other-voucher-details")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetails>> getAllGOtherVoucherDetails(Pageable pageable) {
        log.debug("REST request to get a page of GOtherVoucherDetails");
        Page<GOtherVoucherDetails> page = gOtherVoucherDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-voucher-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /g-other-voucher-details/:id : get the "id" gOtherVoucherDetails.
     *
     * @param id the id of the gOtherVoucherDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gOtherVoucherDetails, or with status 404 (Not Found)
     */
    @GetMapping("/g-other-voucher-details/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherDetails> getGOtherVoucherDetails(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucherDetails : {}", id);
        Optional<GOtherVoucherDetails> gOtherVoucherDetails = gOtherVoucherDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gOtherVoucherDetails);
    }

    /**
     * DELETE  /g-other-voucher-details/:id : delete the "id" gOtherVoucherDetails.
     *
     * @param id the id of the gOtherVoucherDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/g-other-voucher-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucherDetails(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucherDetails : {}", id);
        gOtherVoucherDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
