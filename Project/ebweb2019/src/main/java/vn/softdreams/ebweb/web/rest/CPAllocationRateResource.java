package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.service.CPAllocationRateService;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;
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
 * REST controller for managing CPAllocationRate.
 */
@RestController
@RequestMapping("/api")
public class CPAllocationRateResource {

    private final Logger log = LoggerFactory.getLogger(CPAllocationRateResource.class);

    private static final String ENTITY_NAME = "cPAllocationRate";

    private final CPAllocationRateService cPAllocationRateService;

    public CPAllocationRateResource(CPAllocationRateService cPAllocationRateService) {
        this.cPAllocationRateService = cPAllocationRateService;
    }

    /**
     * POST  /c-p-allocation-rates : Create a new cPAllocationRate.
     *
     * @param cPAllocationRate the cPAllocationRate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPAllocationRate, or with status 400 (Bad Request) if the cPAllocationRate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/c-p-allocation-rates")
    @Timed
    public ResponseEntity<CPAllocationRate> createCPAllocationRate(@RequestBody CPAllocationRate cPAllocationRate) throws URISyntaxException {
        log.debug("REST request to save CPAllocationRate : {}", cPAllocationRate);
        if (cPAllocationRate.getId() != null) {
            throw new BadRequestAlertException("A new cPAllocationRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPAllocationRate result = cPAllocationRateService.save(cPAllocationRate);
        return ResponseEntity.created(new URI("/api/c-p-allocation-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /c-p-allocation-rates : Updates an existing cPAllocationRate.
     *
     * @param cPAllocationRate the cPAllocationRate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPAllocationRate,
     * or with status 400 (Bad Request) if the cPAllocationRate is not valid,
     * or with status 500 (Internal Server Error) if the cPAllocationRate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/c-p-allocation-rates")
    @Timed
    public ResponseEntity<CPAllocationRate> updateCPAllocationRate(@RequestBody CPAllocationRate cPAllocationRate) throws URISyntaxException {
        log.debug("REST request to update CPAllocationRate : {}", cPAllocationRate);
        if (cPAllocationRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPAllocationRate result = cPAllocationRateService.save(cPAllocationRate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPAllocationRate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /c-p-allocation-rates : get all the cPAllocationRates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPAllocationRates in body
     */
    @GetMapping("/c-p-allocation-rates")
    @Timed
    public ResponseEntity<List<CPAllocationRate>> getAllCPAllocationRates(Pageable pageable) {
        log.debug("REST request to get a page of CPAllocationRates");
        Page<CPAllocationRate> page = cPAllocationRateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/c-p-allocation-rates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /c-p-allocation-rates/:id : get the "id" cPAllocationRate.
     *
     * @param id the id of the cPAllocationRate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPAllocationRate, or with status 404 (Not Found)
     */
    @GetMapping("/c-p-allocation-rates/{id}")
    @Timed
    public ResponseEntity<CPAllocationRate> getCPAllocationRate(@PathVariable UUID id) {
        log.debug("REST request to get CPAllocationRate : {}", id);
        Optional<CPAllocationRate> cPAllocationRate = cPAllocationRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPAllocationRate);
    }

    /**
     * DELETE  /c-p-allocation-rates/:id : delete the "id" cPAllocationRate.
     *
     * @param id the id of the cPAllocationRate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/c-p-allocation-rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPAllocationRate(@PathVariable UUID id) {
        log.debug("REST request to delete CPAllocationRate : {}", id);
        cPAllocationRateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/c-p-allocation-rates/find-all-by-c-p-period-id")
    @Timed
    public ResponseEntity<List<CPAllocationRate>> findAllByCPPeriodID(@RequestParam(required = false) UUID cPPeriodID) {
        log.debug("REST request to get a page of CPAllocationRate");
        List<CPAllocationRate> page = cPAllocationRateService.findAllByCPPeriodID(cPPeriodID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/c-p-allocation-rates/find-all-by-list-cost-set")
    @Timed
    public ResponseEntity<List<CPAllocationRateDTO>> findAllByListCostSets(@RequestParam(required = false) List<UUID> uuids, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate) {
        log.debug("REST request to get a page of CPAllocationRate");
        List<CPAllocationRateDTO> page = cPAllocationRateService.findAllByListCostSets(uuids, fromDate, toDate);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
