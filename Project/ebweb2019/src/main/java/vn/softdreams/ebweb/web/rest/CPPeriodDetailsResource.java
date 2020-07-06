package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPPeriodDetails;
import vn.softdreams.ebweb.service.CPPeriodDetailsService;
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
 * REST controller for managing CPPeriodDetails.
 */
@RestController
@RequestMapping("/api")
public class CPPeriodDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CPPeriodDetailsResource.class);

    private static final String ENTITY_NAME = "cPPeriodDetails";

    private final CPPeriodDetailsService cPPeriodDetailsService;

    public CPPeriodDetailsResource(CPPeriodDetailsService cPPeriodDetailsService) {
        this.cPPeriodDetailsService = cPPeriodDetailsService;
    }

    /**
     * POST  /cp-period-details : Create a new cPPeriodDetails.
     *
     * @param cPPeriodDetails the cPPeriodDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPPeriodDetails, or with status 400 (Bad Request) if the cPPeriodDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-period-details")
    @Timed
    public ResponseEntity<CPPeriodDetails> createCPPeriodDetails(@RequestBody CPPeriodDetails cPPeriodDetails) throws URISyntaxException {
        log.debug("REST request to save CPPeriodDetails : {}", cPPeriodDetails);
        if (cPPeriodDetails.getId() != null) {
            throw new BadRequestAlertException("A new cPPeriodDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPPeriodDetails result = cPPeriodDetailsService.save(cPPeriodDetails);
        return ResponseEntity.created(new URI("/api/cp-period-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-period-details : Updates an existing cPPeriodDetails.
     *
     * @param cPPeriodDetails the cPPeriodDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPPeriodDetails,
     * or with status 400 (Bad Request) if the cPPeriodDetails is not valid,
     * or with status 500 (Internal Server Error) if the cPPeriodDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-period-details")
    @Timed
    public ResponseEntity<CPPeriodDetails> updateCPPeriodDetails(@RequestBody CPPeriodDetails cPPeriodDetails) throws URISyntaxException {
        log.debug("REST request to update CPPeriodDetails : {}", cPPeriodDetails);
        if (cPPeriodDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPPeriodDetails result = cPPeriodDetailsService.save(cPPeriodDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPPeriodDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-period-details : get all the cPPeriodDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPPeriodDetails in body
     */
    @GetMapping("/cp-period-details")
    @Timed
    public ResponseEntity<List<CPPeriodDetails>> getAllCPPeriodDetails(Pageable pageable) {
        log.debug("REST request to get a page of CPPeriodDetails");
        Page<CPPeriodDetails> page = cPPeriodDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-period-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-period-details/:id : get the "id" cPPeriodDetails.
     *
     * @param id the id of the cPPeriodDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPPeriodDetails, or with status 404 (Not Found)
     */
    @GetMapping("/cp-period-details/{id}")
    @Timed
    public ResponseEntity<CPPeriodDetails> getCPPeriodDetails(@PathVariable UUID id) {
        log.debug("REST request to get CPPeriodDetails : {}", id);
        Optional<CPPeriodDetails> cPPeriodDetails = cPPeriodDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPPeriodDetails);
    }

    /**
     * DELETE  /cp-period-details/:id : delete the "id" cPPeriodDetails.
     *
     * @param id the id of the cPPeriodDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-period-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPPeriodDetails(@PathVariable UUID id) {
        log.debug("REST request to delete CPPeriodDetails : {}", id);
        cPPeriodDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
