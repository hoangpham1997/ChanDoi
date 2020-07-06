package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPExpenseTranferDetails;
import vn.softdreams.ebweb.service.CPExpenseTranferDetailsService;
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

/**
 * REST controller for managing CPExpenseTranferDetails.
 */
@RestController
@RequestMapping("/api")
public class CPExpenseTranferDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CPExpenseTranferDetailsResource.class);

    private static final String ENTITY_NAME = "cPExpenseTranferDetails";

    private final CPExpenseTranferDetailsService cPExpenseTranferDetailsService;

    public CPExpenseTranferDetailsResource(CPExpenseTranferDetailsService cPExpenseTranferDetailsService) {
        this.cPExpenseTranferDetailsService = cPExpenseTranferDetailsService;
    }

    /**
     * POST  /c-p-expense-tranfer-details : Create a new cPExpenseTranferDetails.
     *
     * @param cPExpenseTranferDetails the cPExpenseTranferDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPExpenseTranferDetails, or with status 400 (Bad Request) if the cPExpenseTranferDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/c-p-expense-tranfer-details")
    @Timed
    public ResponseEntity<CPExpenseTranferDetails> createCPExpenseTranferDetails(@RequestBody CPExpenseTranferDetails cPExpenseTranferDetails) throws URISyntaxException {
        log.debug("REST request to save CPExpenseTranferDetails : {}", cPExpenseTranferDetails);
        if (cPExpenseTranferDetails.getId() != null) {
            throw new BadRequestAlertException("A new cPExpenseTranferDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPExpenseTranferDetails result = cPExpenseTranferDetailsService.save(cPExpenseTranferDetails);
        return ResponseEntity.created(new URI("/api/c-p-expense-tranfer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /c-p-expense-tranfer-details : Updates an existing cPExpenseTranferDetails.
     *
     * @param cPExpenseTranferDetails the cPExpenseTranferDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPExpenseTranferDetails,
     * or with status 400 (Bad Request) if the cPExpenseTranferDetails is not valid,
     * or with status 500 (Internal Server Error) if the cPExpenseTranferDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/c-p-expense-tranfer-details")
    @Timed
    public ResponseEntity<CPExpenseTranferDetails> updateCPExpenseTranferDetails(@RequestBody CPExpenseTranferDetails cPExpenseTranferDetails) throws URISyntaxException {
        log.debug("REST request to update CPExpenseTranferDetails : {}", cPExpenseTranferDetails);
        if (cPExpenseTranferDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPExpenseTranferDetails result = cPExpenseTranferDetailsService.save(cPExpenseTranferDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPExpenseTranferDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /c-p-expense-tranfer-details : get all the cPExpenseTranferDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPExpenseTranferDetails in body
     */
    @GetMapping("/c-p-expense-tranfer-details")
    @Timed
    public ResponseEntity<List<CPExpenseTranferDetails>> getAllCPExpenseTranferDetails(Pageable pageable) {
        log.debug("REST request to get a page of CPExpenseTranferDetails");
        Page<CPExpenseTranferDetails> page = cPExpenseTranferDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-expense-tranfer-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /c-p-expense-tranfer-details/:id : get the "id" cPExpenseTranferDetails.
     *
     * @param id the id of the cPExpenseTranferDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPExpenseTranferDetails, or with status 404 (Not Found)
     */
    @GetMapping("/c-p-expense-tranfer-details/{id}")
    @Timed
    public ResponseEntity<CPExpenseTranferDetails> getCPExpenseTranferDetails(@PathVariable Long id) {
        log.debug("REST request to get CPExpenseTranferDetails : {}", id);
        Optional<CPExpenseTranferDetails> cPExpenseTranferDetails = cPExpenseTranferDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPExpenseTranferDetails);
    }

    /**
     * DELETE  /c-p-expense-tranfer-details/:id : delete the "id" cPExpenseTranferDetails.
     *
     * @param id the id of the cPExpenseTranferDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-p-expense-tranfer-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPExpenseTranferDetails(@PathVariable Long id) {
        log.debug("REST request to delete CPExpenseTranferDetails : {}", id);
        cPExpenseTranferDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
