package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;
import vn.softdreams.ebweb.service.CPAllocationGeneralExpenseDetailsService;
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
 * REST controller for managing CPAllocationGeneralExpenseDetails.
 */
@RestController
@RequestMapping("/api")
public class CPAllocationGeneralExpenseDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CPAllocationGeneralExpenseDetailsResource.class);

    private static final String ENTITY_NAME = "cPAllocationGeneralExpenseDetails";

    private final CPAllocationGeneralExpenseDetailsService cPAllocationGeneralExpenseDetailsService;

    public CPAllocationGeneralExpenseDetailsResource(CPAllocationGeneralExpenseDetailsService cPAllocationGeneralExpenseDetailsService) {
        this.cPAllocationGeneralExpenseDetailsService = cPAllocationGeneralExpenseDetailsService;
    }

    /**
     * POST  /cp-allocation-general-expense-details : Create a new cPAllocationGeneralExpenseDetails.
     *
     * @param cPAllocationGeneralExpenseDetails the cPAllocationGeneralExpenseDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPAllocationGeneralExpenseDetails, or with status 400 (Bad Request) if the cPAllocationGeneralExpenseDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-allocation-general-expense-details")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpenseDetails> createCPAllocationGeneralExpenseDetails(@Valid @RequestBody CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseDetails) throws URISyntaxException {
        log.debug("REST request to save CPAllocationGeneralExpenseDetails : {}", cPAllocationGeneralExpenseDetails);
        if (cPAllocationGeneralExpenseDetails.getId() != null) {
            throw new BadRequestAlertException("A new cPAllocationGeneralExpenseDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPAllocationGeneralExpenseDetails result = cPAllocationGeneralExpenseDetailsService.save(cPAllocationGeneralExpenseDetails);
        return ResponseEntity.created(new URI("/api/cp-allocation-general-expense-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-allocation-general-expense-details : Updates an existing cPAllocationGeneralExpenseDetails.
     *
     * @param cPAllocationGeneralExpenseDetails the cPAllocationGeneralExpenseDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAllocationGeneralExpenseDetails,
     * or with status 400 (Bad Request) if the cPAllocationGeneralExpenseDetails is not valid,
     * or with status 500 (Internal Server Error) if the cPAllocationGeneralExpenseDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-allocation-general-expense-details")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpenseDetails> updateCPAllocationGeneralExpenseDetails(@Valid @RequestBody CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseDetails) throws URISyntaxException {
        log.debug("REST request to update CPAllocationGeneralExpenseDetails : {}", cPAllocationGeneralExpenseDetails);
        if (cPAllocationGeneralExpenseDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPAllocationGeneralExpenseDetails result = cPAllocationGeneralExpenseDetailsService.save(cPAllocationGeneralExpenseDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAllocationGeneralExpenseDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-allocation-general-expense-details : get all the cPAllocationGeneralExpenseDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAllocationGeneralExpenseDetails in body
     */
    @GetMapping("/cp-allocation-general-expense-details")
    @Timed
    public ResponseEntity<List<CPAllocationGeneralExpenseDetails>> getAllCPAllocationGeneralExpenseDetails(Pageable pageable) {
        log.debug("REST request to get a page of CPAllocationGeneralExpenseDetails");
        Page<CPAllocationGeneralExpenseDetails> page = cPAllocationGeneralExpenseDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-allocation-general-expense-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-allocation-general-expense-details : get all the cPAllocationGeneralExpenseDetails.
     *
     * @param cPPeriodID the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAllocationGeneralExpenseDetails in body
     */
    @GetMapping("/cp-allocation-general-expense-details/get-all-by-cPPeriodID/{cPPeriodID}")
    @Timed
    public ResponseEntity<List<CPAllocationGeneralExpenseDetails>> getAllCPAllocationGeneralExpenseDetailsByCPPeriodID
    (@PathVariable UUID cPPeriodID) {
        log.debug("REST request to get a page of CPAllocationGeneralExpenseDetails");
        List<CPAllocationGeneralExpenseDetails> page = cPAllocationGeneralExpenseDetailsService.findAllByCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /cp-allocation-general-expense-details/:id : get the "id" cPAllocationGeneralExpenseDetails.
     *
     * @param id the id of the cPAllocationGeneralExpenseDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAllocationGeneralExpenseDetails, or with status 404 (Not Found)
     */
    @GetMapping("/cp-allocation-general-expense-details/{id}")
    @Timed
    public ResponseEntity<CPAllocationGeneralExpenseDetails> getCPAllocationGeneralExpenseDetails(@PathVariable UUID id) {
        log.debug("REST request to get CPAllocationGeneralExpenseDetails : {}", id);
        Optional<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails = cPAllocationGeneralExpenseDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAllocationGeneralExpenseDetails);
    }

    /**
     * DELETE  /cp-allocation-general-expense-details/:id : delete the "id" cPAllocationGeneralExpenseDetails.
     *
     * @param id the id of the cPAllocationGeneralExpenseDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-allocation-general-expense-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAllocationGeneralExpenseDetails(@PathVariable UUID id) {
        log.debug("REST request to delete CPAllocationGeneralExpenseDetails : {}", id);
        cPAllocationGeneralExpenseDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
