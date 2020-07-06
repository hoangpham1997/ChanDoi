package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.service.SAOrderDetailsService;
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
 * REST controller for managing SAOrderDetails.
 */
@RestController
@RequestMapping("/api")
public class SAOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SAOrderDetailsResource.class);

    private static final String ENTITY_NAME = "sAOrderDetails";

    private final SAOrderDetailsService sAOrderDetailsService;

    public SAOrderDetailsResource(SAOrderDetailsService sAOrderDetailsService) {
        this.sAOrderDetailsService = sAOrderDetailsService;
    }

    /**
     * POST  /s-a-order-details : Create a new sAOrderDetails.
     *
     * @param sAOrderDetails the sAOrderDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAOrderDetails, or with status 400 (Bad Request) if the sAOrderDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/s-a-order-details")
    @Timed
    public ResponseEntity<SAOrderDetails> createSAOrderDetails(@Valid @RequestBody SAOrderDetails sAOrderDetails) throws URISyntaxException {
        log.debug("REST request to save SAOrderDetails : {}", sAOrderDetails);
        if (sAOrderDetails.getId() != null) {
            throw new BadRequestAlertException("A new sAOrderDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SAOrderDetails result = sAOrderDetailsService.save(sAOrderDetails);
        return ResponseEntity.created(new URI("/api/s-a-order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /s-a-order-details : Updates an existing sAOrderDetails.
     *
     * @param sAOrderDetails the sAOrderDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAOrderDetails,
     * or with status 400 (Bad Request) if the sAOrderDetails is not valid,
     * or with status 500 (Internal Server Error) if the sAOrderDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/s-a-order-details")
    @Timed
    public ResponseEntity<SAOrderDetails> updateSAOrderDetails(@Valid @RequestBody SAOrderDetails sAOrderDetails) throws URISyntaxException {
        log.debug("REST request to update SAOrderDetails : {}", sAOrderDetails);
        if (sAOrderDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SAOrderDetails result = sAOrderDetailsService.save(sAOrderDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sAOrderDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /s-a-order-details : get all the sAOrderDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAOrderDetails in body
     */
    @GetMapping("/s-a-order-details")
    @Timed
    public ResponseEntity<List<SAOrderDetails>> getAllSAOrderDetails(Pageable pageable) {
        log.debug("REST request to get a page of SAOrderDetails");
        Page<SAOrderDetails> page = sAOrderDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/s-a-order-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /s-a-order-details/:id : get the "id" sAOrderDetails.
     *
     * @param id the id of the sAOrderDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAOrderDetails, or with status 404 (Not Found)
     */
    @GetMapping("/s-a-order-details/{id}")
    @Timed
    public ResponseEntity<SAOrderDetails> getSAOrderDetails(@PathVariable UUID id) {
        log.debug("REST request to get SAOrderDetails : {}", id);
        Optional<SAOrderDetails> sAOrderDetails = sAOrderDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAOrderDetails);
    }

    /**
     * DELETE  /s-a-order-details/:id : delete the "id" sAOrderDetails.
     *
     * @param id the id of the sAOrderDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/s-a-order-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAOrderDetails(@PathVariable UUID id) {
        log.debug("REST request to delete SAOrderDetails : {}", id);
        sAOrderDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/s-a-orders/details")
    @Timed
    public ResponseEntity<List<SAOrderDetails>> findAllDetailsById(@RequestParam List<UUID> id) {
        log.debug("REST request to get SaOrder : {}", id);
        List<SAOrderDetails> details = sAOrderDetailsService.findAllDetailsById(id);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
