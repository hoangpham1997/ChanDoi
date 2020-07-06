package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAcceptanceDetails;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.service.CPAcceptanceDetailsService;
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
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CPAcceptanceDetails.
 */
@RestController
@RequestMapping("/api")
public class CPAcceptanceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CPAcceptanceDetailsResource.class);

    private static final String ENTITY_NAME = "cPAcceptanceDetails";

    private final CPAcceptanceDetailsService cPAcceptanceDetailsService;

    public CPAcceptanceDetailsResource(CPAcceptanceDetailsService cPAcceptanceDetailsService) {
        this.cPAcceptanceDetailsService = cPAcceptanceDetailsService;
    }

    /**
     * POST  /cp-acceptance-details : Create a new cPAcceptanceDetails.
     *
     * @param cPAcceptanceDetails the cPAcceptanceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPAcceptanceDetails, or with status 400 (Bad Request) if the cPAcceptanceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-acceptance-details")
    @Timed
    public ResponseEntity<CPAcceptanceDetails> createCPAcceptanceDetails(@RequestBody CPAcceptanceDetails cPAcceptanceDetails) throws URISyntaxException {
        log.debug("REST request to save CPAcceptanceDetails : {}", cPAcceptanceDetails);
        if (cPAcceptanceDetails.getId() != null) {
            throw new BadRequestAlertException("A new cPAcceptanceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPAcceptanceDetails result = cPAcceptanceDetailsService.save(cPAcceptanceDetails);
        return ResponseEntity.created(new URI("/api/cp-acceptance-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-acceptance-details : Updates an existing cPAcceptanceDetails.
     *
     * @param cPAcceptanceDetails the cPAcceptanceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAcceptanceDetails,
     * or with status 400 (Bad Request) if the cPAcceptanceDetails is not valid,
     * or with status 500 (Internal Server Error) if the cPAcceptanceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-acceptance-details")
    @Timed
    public ResponseEntity<CPAcceptanceDetails> updateCPAcceptanceDetails(@RequestBody CPAcceptanceDetails cPAcceptanceDetails) throws URISyntaxException {
        log.debug("REST request to update CPAcceptanceDetails : {}", cPAcceptanceDetails);
        if (cPAcceptanceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPAcceptanceDetails result = cPAcceptanceDetailsService.save(cPAcceptanceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAcceptanceDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-acceptance-details : get all the cPAcceptanceDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAcceptanceDetails in body
     */
    @GetMapping("/cp-acceptance-details")
    @Timed
    public ResponseEntity<List<CPAcceptanceDetails>> getAllCPAcceptanceDetails(Pageable pageable) {
        log.debug("REST request to get a page of CPAcceptanceDetails");
        Page<CPAcceptanceDetails> page = cPAcceptanceDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-acceptance-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-acceptance-details/:id : get the "id" cPAcceptanceDetails.
     *
     * @param id the id of the cPAcceptanceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAcceptanceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/cp-acceptance-details/{id}")
    @Timed
    public ResponseEntity<CPAcceptanceDetails> getCPAcceptanceDetails(@PathVariable UUID id) {
        log.debug("REST request to get CPAcceptanceDetails : {}", id);
        Optional<CPAcceptanceDetails> cPAcceptanceDetails = cPAcceptanceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAcceptanceDetails);
    }

    /**
     * DELETE  /cp-acceptance-details/:id : delete the "id" cPAcceptanceDetails.
     *
     * @param id the id of the cPAcceptanceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-acceptance-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAcceptanceDetails(@PathVariable UUID id) {
        log.debug("REST request to delete CPAcceptanceDetails : {}", id);
        cPAcceptanceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/cp-acceptance-details/evaluate")
    @Timed
    public ResponseEntity<List<CPAcceptanceDetails>> evaluate(@RequestBody EvaluateDTO evaluateDTO) {
        log.debug("REST request to get a page of CPProductQuantum");
        List<CPAcceptanceDetails> page = cPAcceptanceDetailsService.evaluate(evaluateDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
