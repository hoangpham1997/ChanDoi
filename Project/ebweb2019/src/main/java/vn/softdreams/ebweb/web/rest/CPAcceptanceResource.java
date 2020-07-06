package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPAcceptance;
import vn.softdreams.ebweb.service.CPAcceptanceService;
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
 * REST controller for managing CPAcceptance.
 */
@RestController
@RequestMapping("/api")
public class CPAcceptanceResource {

    private final Logger log = LoggerFactory.getLogger(CPAcceptanceResource.class);

    private static final String ENTITY_NAME = "cPAcceptance";

    private final CPAcceptanceService cPAcceptanceService;

    public CPAcceptanceResource(CPAcceptanceService cPAcceptanceService) {
        this.cPAcceptanceService = cPAcceptanceService;
    }

    /**
     * POST  /cp-acceptances : Create a new cPAcceptance.
     *
     * @param cPAcceptance the cPAcceptance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPAcceptance, or with status 400 (Bad Request) if the cPAcceptance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-acceptances")
    @Timed
    public ResponseEntity<CPAcceptance> createCPAcceptance(@RequestBody CPAcceptance cPAcceptance) throws URISyntaxException {
        log.debug("REST request to save CPAcceptance : {}", cPAcceptance);
        if (cPAcceptance.getId() != null) {
            throw new BadRequestAlertException("A new cPAcceptance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPAcceptance result = cPAcceptanceService.save(cPAcceptance);
        return ResponseEntity.created(new URI("/api/cp-acceptances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-acceptances : Updates an existing cPAcceptance.
     *
     * @param cPAcceptance the cPAcceptance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAcceptance,
     * or with status 400 (Bad Request) if the cPAcceptance is not valid,
     * or with status 500 (Internal Server Error) if the cPAcceptance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-acceptances")
    @Timed
    public ResponseEntity<CPAcceptance> updateCPAcceptance(@RequestBody CPAcceptance cPAcceptance) throws URISyntaxException {
        log.debug("REST request to update CPAcceptance : {}", cPAcceptance);
        if (cPAcceptance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPAcceptance result = cPAcceptanceService.save(cPAcceptance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAcceptance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-acceptances : get all the cPAcceptances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAcceptances in body
     */
    @GetMapping("/cp-acceptances")
    @Timed
    public ResponseEntity<List<CPAcceptance>> getAllCPAcceptances(Pageable pageable) {
        log.debug("REST request to get a page of CPAcceptances");
        Page<CPAcceptance> page = cPAcceptanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-acceptances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-acceptances/:id : get the "id" cPAcceptance.
     *
     * @param id the id of the cPAcceptance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAcceptance, or with status 404 (Not Found)
     */
    @GetMapping("/cp-acceptances/{id}")
    @Timed
    public ResponseEntity<CPAcceptance> getCPAcceptance(@PathVariable Long id) {
        log.debug("REST request to get CPAcceptance : {}", id);
        Optional<CPAcceptance> cPAcceptance = cPAcceptanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAcceptance);
    }

    /**
     * DELETE  /cp-acceptances/:id : delete the "id" cPAcceptance.
     *
     * @param id the id of the cPAcceptance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-acceptances/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAcceptance(@PathVariable Long id) {
        log.debug("REST request to delete CPAcceptance : {}", id);
        cPAcceptanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
