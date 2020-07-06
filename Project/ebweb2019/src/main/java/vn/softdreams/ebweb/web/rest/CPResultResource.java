package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPResult;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import vn.softdreams.ebweb.service.CPResultService;
import vn.softdreams.ebweb.web.rest.dto.CalculateCostDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CPResult.
 */
@RestController
@RequestMapping("/api")
public class CPResultResource {

    private final Logger log = LoggerFactory.getLogger(CPResultResource.class);

    private static final String ENTITY_NAME = "cPResult";

    private final CPResultService cPResultService;

    public CPResultResource(CPResultService cPResultService) {
        this.cPResultService = cPResultService;
    }

    /**
     * POST  /cp-results : Create a new cPResult.
     *
     * @param cPResult the cPResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPResult, or with status 400 (Bad Request) if the cPResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-results")
    @Timed
    public ResponseEntity<CPResult> createCPResult(@Valid @RequestBody CPResult cPResult) throws URISyntaxException {
        log.debug("REST request to save CPResult : {}", cPResult);
        if (cPResult.getId() != null) {
            throw new BadRequestAlertException("A new cPResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPResult result = cPResultService.save(cPResult);
        return ResponseEntity.created(new URI("/api/cp-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-results : Updates an existing cPResult.
     *
     * @param cPResult the cPResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPResult,
     * or with status 400 (Bad Request) if the cPResult is not valid,
     * or with status 500 (Internal Server Error) if the cPResult couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-results")
    @Timed
    public ResponseEntity<CPResult> updateCPResult(@Valid @RequestBody CPResult cPResult) throws URISyntaxException {
        log.debug("REST request to update CPResult : {}", cPResult);
        if (cPResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPResult result = cPResultService.save(cPResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-results : get all the cPResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPResults in body
     */
    @GetMapping("/cp-results")
    @Timed
    public ResponseEntity<List<CPResult>> getAllCPResults(Pageable pageable) {
        log.debug("REST request to get a page of CPResults");
        Page<CPResult> page = cPResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-results/:id : get the "id" cPResult.
     *
     * @param id the id of the cPResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPResult, or with status 404 (Not Found)
     */
    @GetMapping("/cp-results/{id}")
    @Timed
    public ResponseEntity<CPResult> getCPResult(@PathVariable UUID id) {
        log.debug("REST request to get CPResult : {}", id);
        Optional<CPResult> cPResult = cPResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPResult);
    }

    /**
     * DELETE  /cp-results/:id : delete the "id" cPResult.
     *
     * @param id the id of the cPResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPResult(@PathVariable UUID id) {
        log.debug("REST request to delete CPResult : {}", id);
        cPResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/cp-results/calculateCost")
    @Timed
    public ResponseEntity<List<CPResult>> calculateCost(@RequestBody CalculateCostDTO calculateCostDTO) {
        log.debug("REST request to get a page of CPProductQuantum");
        List<CPResult> page = cPResultService.calculateCost(calculateCostDTO);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
