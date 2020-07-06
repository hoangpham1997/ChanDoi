package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PSSalaryTaxInsuranceRegulation;
import vn.softdreams.ebweb.service.PSSalaryTaxInsuranceRegulationService;
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
 * REST controller for managing PSSalaryTaxInsuranceRegulation.
 */
@RestController
@RequestMapping("/api")
public class PSSalaryTaxInsuranceRegulationResource {

    private final Logger log = LoggerFactory.getLogger(PSSalaryTaxInsuranceRegulationResource.class);

    private static final String ENTITY_NAME = "pSSalaryTaxInsuranceRegulation";

    private final PSSalaryTaxInsuranceRegulationService pSSalaryTaxInsuranceRegulationService;

    public PSSalaryTaxInsuranceRegulationResource(PSSalaryTaxInsuranceRegulationService pSSalaryTaxInsuranceRegulationService) {
        this.pSSalaryTaxInsuranceRegulationService = pSSalaryTaxInsuranceRegulationService;
    }

    /**
     * POST  /ps-salary-tax-insurance-regulations : Create a new pSSalaryTaxInsuranceRegulation.
     *
     * @param pSSalaryTaxInsuranceRegulation the pSSalaryTaxInsuranceRegulation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pSSalaryTaxInsuranceRegulation, or with status 400 (Bad Request) if the pSSalaryTaxInsuranceRegulation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ps-salary-tax-insurance-regulations")
    @Timed
    public ResponseEntity<PSSalaryTaxInsuranceRegulation> createPSSalaryTaxInsuranceRegulation(@RequestBody PSSalaryTaxInsuranceRegulation pSSalaryTaxInsuranceRegulation) throws URISyntaxException {
        log.debug("REST request to save PSSalaryTaxInsuranceRegulation : {}", pSSalaryTaxInsuranceRegulation);
        if (pSSalaryTaxInsuranceRegulation.getId() != null) {
            throw new BadRequestAlertException("A new pSSalaryTaxInsuranceRegulation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PSSalaryTaxInsuranceRegulation result = pSSalaryTaxInsuranceRegulationService.save(pSSalaryTaxInsuranceRegulation);
        return ResponseEntity.created(new URI("/api/ps-salary-tax-insurance-regulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ps-salary-tax-insurance-regulations : Updates an existing pSSalaryTaxInsuranceRegulation.
     *
     * @param pSSalaryTaxInsuranceRegulation the pSSalaryTaxInsuranceRegulation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pSSalaryTaxInsuranceRegulation,
     * or with status 400 (Bad Request) if the pSSalaryTaxInsuranceRegulation is not valid,
     * or with status 500 (Internal Server Error) if the pSSalaryTaxInsuranceRegulation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ps-salary-tax-insurance-regulations")
    @Timed
    public ResponseEntity<PSSalaryTaxInsuranceRegulation> updatePSSalaryTaxInsuranceRegulation(@RequestBody PSSalaryTaxInsuranceRegulation pSSalaryTaxInsuranceRegulation) throws URISyntaxException {
        log.debug("REST request to update PSSalaryTaxInsuranceRegulation : {}", pSSalaryTaxInsuranceRegulation);
        if (pSSalaryTaxInsuranceRegulation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PSSalaryTaxInsuranceRegulation result = pSSalaryTaxInsuranceRegulationService.save(pSSalaryTaxInsuranceRegulation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pSSalaryTaxInsuranceRegulation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ps-salary-tax-insurance-regulations : get all the pSSalaryTaxInsuranceRegulations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pSSalaryTaxInsuranceRegulations in body
     */
    @GetMapping("/ps-salary-tax-insurance-regulations")
    @Timed
    public ResponseEntity<List<PSSalaryTaxInsuranceRegulation>> getAllPSSalaryTaxInsuranceRegulations(Pageable pageable) {
        log.debug("REST request to get a page of PSSalaryTaxInsuranceRegulations");
        Page<PSSalaryTaxInsuranceRegulation> page = pSSalaryTaxInsuranceRegulationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ps-salary-tax-insurance-regulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ps-salary-tax-insurance-regulations/:id : get the "id" pSSalaryTaxInsuranceRegulation.
     *
     * @param id the id of the pSSalaryTaxInsuranceRegulation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pSSalaryTaxInsuranceRegulation, or with status 404 (Not Found)
     */
    @GetMapping("/ps-salary-tax-insurance-regulations/{id}")
    @Timed
    public ResponseEntity<PSSalaryTaxInsuranceRegulation> getPSSalaryTaxInsuranceRegulation(@PathVariable UUID id) {
        log.debug("REST request to get PSSalaryTaxInsuranceRegulation : {}", id);
        Optional<PSSalaryTaxInsuranceRegulation> pSSalaryTaxInsuranceRegulation = pSSalaryTaxInsuranceRegulationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pSSalaryTaxInsuranceRegulation);
    }

    /**
     * DELETE  /ps-salary-tax-insurance-regulations/:id : delete the "id" pSSalaryTaxInsuranceRegulation.
     *
     * @param id the id of the pSSalaryTaxInsuranceRegulation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ps-salary-tax-insurance-regulations/{id}")
    @Timed
    public ResponseEntity<Void> deletePSSalaryTaxInsuranceRegulation(@PathVariable UUID id) {
        log.debug("REST request to delete PSSalaryTaxInsuranceRegulation : {}", id);
        pSSalaryTaxInsuranceRegulationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
