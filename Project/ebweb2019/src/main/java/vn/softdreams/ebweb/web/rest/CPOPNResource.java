package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.service.CPOPNService;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;
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
 * REST controller for managing CPOPN.
 */
@RestController
@RequestMapping("/api")
public class CPOPNResource {

    private final Logger log = LoggerFactory.getLogger(CPOPNResource.class);

    private static final String ENTITY_NAME = "cPOPN";

    private final CPOPNService cPOPNService;

    public CPOPNResource(CPOPNService cPOPNService) {
        this.cPOPNService = cPOPNService;
    }

    /**
     * POST  /c-popns : Create a new cPOPN.
     *
     * @param cPOPN the cPOPN to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPOPN, or with status 400 (Bad Request) if the cPOPN has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/c-popns")
    @Timed
    public ResponseEntity<CPOPN> createCPOPN(@RequestBody CPOPN cPOPN) throws URISyntaxException {
        log.debug("REST request to save CPOPN : {}", cPOPN);
        if (cPOPN.getId() != null) {
            throw new BadRequestAlertException("A new cPOPN cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPOPN result = cPOPNService.save(cPOPN);
        return ResponseEntity.created(new URI("/api/c-popns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /c-popns : Updates an existing cPOPN.
     *
     * @param cPOPN the cPOPN to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPOPN,
     * or with status 400 (Bad Request) if the cPOPN is not valid,
     * or with status 500 (Internal Server Error) if the cPOPN couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/c-popns")
    @Timed
    public ResponseEntity<CPOPN> updateCPOPN(@RequestBody CPOPN cPOPN) throws URISyntaxException {
        log.debug("REST request to update CPOPN : {}", cPOPN);
        if (cPOPN.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPOPN result = cPOPNService.save(cPOPN);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPOPN.getId().toString()))
            .body(result);
    }

    /**
     * GET  /c-popns : get all the cPOPNS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPOPNS in body
     */
    @GetMapping("/c-popns")
    @Timed
    public ResponseEntity<List<CPOPN>> getAllCPOPNS(Pageable pageable) {
        log.debug("REST request to get a page of CPOPNS");
        Page<CPOPN> page = cPOPNService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-popns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /c-popns/:id : get the "id" cPOPN.
     *
     * @param id the id of the cPOPN to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPOPN, or with status 404 (Not Found)
     */
    @GetMapping("/c-popns/{id}")
    @Timed
    public ResponseEntity<CPOPN> getCPOPN(@PathVariable UUID id) {
        log.debug("REST request to get CPOPN : {}", id);
        Optional<CPOPN> cPOPN = cPOPNService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPOPN);
    }

    /**
     * DELETE  /c-popns/:id : delete the "id" cPOPN.
     *
     * @param id the id of the cPOPN to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-popns/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPOPN(@PathVariable UUID id) {
        log.debug("REST request to delete CPOPN : {}", id);
        cPOPNService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/c-popns/find-all-c-popns-active-companyid")
    @Timed
    public ResponseEntity<List<CPOPNSDTO>> getAllCPOPNsActiveCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of CPOPNS");
        Page<CPOPNSDTO> page = cPOPNService.findAllActive(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-popns/find-all-c-popns-active-companyid");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @PutMapping("/c-popns/save-all")
    @Timed
    public ResponseEntity<CPOPN> saveAll(@RequestBody List<CPOPN> cpProductQuantum) throws URISyntaxException {
        log.debug("REST request to update CPProductQuantum : {}", cpProductQuantum);
        cPOPNService.saveAll(cpProductQuantum);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
