package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.service.CPUncompleteDetailsService;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;
import vn.softdreams.ebweb.web.rest.dto.EvaluateDTO;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CPUncompleteDetails.
 */
@RestController
@RequestMapping("/api")
public class CPUncompleteDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CPUncompleteDetailsResource.class);

    private static final String ENTITY_NAME = "cPUncompleteDetails";

    private final CPUncompleteDetailsService cPUncompleteDetailsService;

    public CPUncompleteDetailsResource(CPUncompleteDetailsService cPUncompleteDetailsService) {
        this.cPUncompleteDetailsService = cPUncompleteDetailsService;
    }

    /**
     * POST  /cp-uncomplete-details : Create a new cPUncompleteDetails.
     *
     * @param cPUncompleteDetails the cPUncompleteDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPUncompleteDetails, or with status 400 (Bad Request) if the cPUncompleteDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-uncomplete-details")
    @Timed
    public ResponseEntity<CPUncompleteDetails> createCPUncompleteDetails(@RequestBody CPUncompleteDetails cPUncompleteDetails) throws URISyntaxException {
        log.debug("REST request to save CPUncompleteDetails : {}", cPUncompleteDetails);
        if (cPUncompleteDetails.getId() != null) {
            throw new BadRequestAlertException("A new cPUncompleteDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPUncompleteDetails result = cPUncompleteDetailsService.save(cPUncompleteDetails);
        return ResponseEntity.created(new URI("/api/cp-uncomplete-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-uncomplete-details : Updates an existing cPUncompleteDetails.
     *
     * @param cPUncompleteDetails the cPUncompleteDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPUncompleteDetails,
     * or with status 400 (Bad Request) if the cPUncompleteDetails is not valid,
     * or with status 500 (Internal Server Error) if the cPUncompleteDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-uncomplete-details")
    @Timed
    public ResponseEntity<CPUncompleteDetails> updateCPUncompleteDetails(@RequestBody CPUncompleteDetails cPUncompleteDetails) throws URISyntaxException {
        log.debug("REST request to update CPUncompleteDetails : {}", cPUncompleteDetails);
        if (cPUncompleteDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPUncompleteDetails result = cPUncompleteDetailsService.save(cPUncompleteDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPUncompleteDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-uncomplete-details : get all the cPUncompleteDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPUncompleteDetails in body
     */
    @GetMapping("/cp-uncomplete-details")
    @Timed
    public ResponseEntity<List<CPUncompleteDetails>> getAllCPUncompleteDetails(Pageable pageable) {
        log.debug("REST request to get a page of CPUncompleteDetails");
        Page<CPUncompleteDetails> page = cPUncompleteDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-uncomplete-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-uncomplete-details : get all the cPUncompleteDetails.
     *
     * @param cPPeriodID the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPUncompleteDetails in body
     */
    @GetMapping("/cp-uncomplete-details/get-all-by-cPPeriodID/{cPPeriodID}")
    @Timed
    public ResponseEntity<List<CPUncompleteDetails>> getAllCPUncompleteDetailsByCPPeriodID(@PathVariable UUID cPPeriodID) {
        log.debug("REST request to get a page of CPUncompleteDetails");
        List<CPUncompleteDetails> page = cPUncompleteDetailsService.findAllByCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /cp-uncomplete-details/:id : get the "id" cPUncompleteDetails.
     *
     * @param id the id of the cPUncompleteDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPUncompleteDetails, or with status 404 (Not Found)
     */
    @GetMapping("/cp-uncomplete-details/{id}")
    @Timed
    public ResponseEntity<CPUncompleteDetails> getCPUncompleteDetails(@PathVariable UUID id) {
        log.debug("REST request to get CPUncompleteDetails : {}", id);
        Optional<CPUncompleteDetails> cPUncompleteDetails = cPUncompleteDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPUncompleteDetails);
    }

    /**
     * DELETE  /cp-uncomplete-details/:id : delete the "id" cPUncompleteDetails.
     *
     * @param id the id of the cPUncompleteDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-uncomplete-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPUncompleteDetails(@PathVariable UUID id) {
        log.debug("REST request to delete CPUncompleteDetails : {}", id);
        cPUncompleteDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/cp-uncomplete-details/evaluate")
    @Timed
    public ResponseEntity<List<CPUncompleteDetails>> evaluate(@RequestBody EvaluateDTO evaluateDTO) {
        log.debug("REST request to get a page of CPProductQuantum");
        List<CPUncompleteDetails> page = cPUncompleteDetailsService.evaluate(evaluateDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
