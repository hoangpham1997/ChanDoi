package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SAQuoteDetails;
import vn.softdreams.ebweb.service.SAQuoteDetailsService;
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
 * REST controller for managing SAQuoteDetails.
 */
@RestController
@RequestMapping("/api")
public class SAQuoteDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SAQuoteDetailsResource.class);

    private static final String ENTITY_NAME = "sAQuoteDetails";

    private final SAQuoteDetailsService sAQuoteDetailsService;

    public SAQuoteDetailsResource(SAQuoteDetailsService sAQuoteDetailsService) {
        this.sAQuoteDetailsService = sAQuoteDetailsService;
    }

    /**
     * POST  /sa-quote-details : Create a new sAQuoteDetails.
     *
     * @param sAQuoteDetails the sAQuoteDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAQuoteDetails, or with status 400 (Bad Request) if the sAQuoteDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-quote-details")
    @Timed
    public ResponseEntity<SAQuoteDetails> createSAQuoteDetails(@Valid @RequestBody SAQuoteDetails sAQuoteDetails) throws URISyntaxException {
        log.debug("REST request to save SAQuoteDetails : {}", sAQuoteDetails);
        if (sAQuoteDetails.getId() != null) {
            throw new BadRequestAlertException("A new sAQuoteDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SAQuoteDetails result = sAQuoteDetailsService.save(sAQuoteDetails);
        return ResponseEntity.created(new URI("/api/sa-quote-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sa-quote-details : Updates an existing sAQuoteDetails.
     *
     * @param sAQuoteDetails the sAQuoteDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAQuoteDetails,
     * or with status 400 (Bad Request) if the sAQuoteDetails is not valid,
     * or with status 500 (Internal Server Error) if the sAQuoteDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sa-quote-details")
    @Timed
    public ResponseEntity<SAQuoteDetails> updateSAQuoteDetails(@Valid @RequestBody SAQuoteDetails sAQuoteDetails) throws URISyntaxException {
        log.debug("REST request to update SAQuoteDetails : {}", sAQuoteDetails);
        if (sAQuoteDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SAQuoteDetails result = sAQuoteDetailsService.save(sAQuoteDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sAQuoteDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-quote-details : get all the sAQuoteDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAQuoteDetails in body
     */
    @GetMapping("/sa-quote-details")
    @Timed
    public ResponseEntity<List<SAQuoteDetails>> getAllSAQuoteDetails(Pageable pageable) {
        log.debug("REST request to get a page of SAQuoteDetails");
        Page<SAQuoteDetails> page = sAQuoteDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-quote-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-quote-details/:id : get the "id" sAQuoteDetails.
     *
     * @param id the id of the sAQuoteDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAQuoteDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sa-quote-details/{id}")
    @Timed
    public ResponseEntity<SAQuoteDetails> getSAQuoteDetails(@PathVariable UUID id) {
        log.debug("REST request to get SAQuoteDetails : {}", id);
        Optional<SAQuoteDetails> sAQuoteDetails = sAQuoteDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAQuoteDetails);
    }

    /**
     * DELETE  /sa-quote-details/:id : delete the "id" sAQuoteDetails.
     *
     * @param id the id of the sAQuoteDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-quote-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAQuoteDetails(@PathVariable UUID id) {
        log.debug("REST request to delete SAQuoteDetails : {}", id);
        sAQuoteDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
