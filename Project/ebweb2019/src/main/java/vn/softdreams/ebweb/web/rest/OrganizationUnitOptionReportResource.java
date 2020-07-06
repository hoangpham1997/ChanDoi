package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.OrganizationUnitOptionReport;
import vn.softdreams.ebweb.service.OrganizationUnitOptionReportService;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing OrganizationUnitOptionReport.
 */
@RestController
@RequestMapping("/api")
public class OrganizationUnitOptionReportResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUnitOptionReportResource.class);

    private static final String ENTITY_NAME = "organizationUnitOptionReport";

    private final OrganizationUnitOptionReportService organizationUnitOptionReportService;

    public OrganizationUnitOptionReportResource(OrganizationUnitOptionReportService organizationUnitOptionReportService) {
        this.organizationUnitOptionReportService = organizationUnitOptionReportService;
    }

    /**
     * POST  /organization-unit-option-reports : Create a new organizationUnitOptionReport.
     *
     * @param organizationUnitOptionReport the organizationUnitOptionReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizationUnitOptionReport, or with status 400 (Bad Request) if the organizationUnitOptionReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organization-unit-option-reports")
    @Timed
    public ResponseEntity<OrganizationUnitOptionReport> createOrganizationUnitOptionReport(@Valid @RequestBody OrganizationUnitOptionReport organizationUnitOptionReport) throws URISyntaxException {
        log.debug("REST request to save OrganizationUnitOptionReport : {}", organizationUnitOptionReport);
        if (organizationUnitOptionReport.getId() != null) {
            throw new BadRequestAlertException("A new organizationUnitOptionReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationUnitOptionReport result = organizationUnitOptionReportService.save(organizationUnitOptionReport);
        return ResponseEntity.created(new URI("/api/organization-unit-option-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organization-unit-option-reports : Updates an existing organizationUnitOptionReport.
     *
     * @param organizationUnitOptionReport the organizationUnitOptionReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationUnitOptionReport,
     * or with status 400 (Bad Request) if the organizationUnitOptionReport is not valid,
     * or with status 500 (Internal Server Error) if the organizationUnitOptionReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organization-unit-option-reports")
    @Timed
    public ResponseEntity<OrganizationUnitOptionReport> updateOrganizationUnitOptionReport(@Valid @RequestBody OrganizationUnitOptionReport organizationUnitOptionReport) throws URISyntaxException {
        log.debug("REST request to update OrganizationUnitOptionReport : {}", organizationUnitOptionReport);
        if (organizationUnitOptionReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationUnitOptionReport result = organizationUnitOptionReportService.save(organizationUnitOptionReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationUnitOptionReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organization-unit-option-reports : get all the organizationUnitOptionReports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationUnitOptionReports in body
     */
    @GetMapping("/organization-unit-option-reports")
    @Timed
    public ResponseEntity<List<OrganizationUnitOptionReport>> getAllOrganizationUnitOptionReports(Pageable pageable) {
        log.debug("REST request to get a page of OrganizationUnitOptionReports");
        Page<OrganizationUnitOptionReport> page = organizationUnitOptionReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-unit-option-reports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organization-unit-option-reports/:id : get the "id" organizationUnitOptionReport.
     *
     * @param id the id of the organizationUnitOptionReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationUnitOptionReport, or with status 404 (Not Found)
     */
    @GetMapping("/organization-unit-option-reports/{id}")
    @Timed
    public ResponseEntity<OrganizationUnitOptionReport> getOrganizationUnitOptionReport(@PathVariable UUID id) {
        log.debug("REST request to get OrganizationUnitOptionReport : {}", id);
        Optional<OrganizationUnitOptionReport> organizationUnitOptionReport = organizationUnitOptionReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationUnitOptionReport);
    }

    /**
     * Lấy entity theo company ID
     */
    @GetMapping("/organization-unit-option-reports/company-id")
    @Timed
    public ResponseEntity<OrganizationUnitOptionReport> findByCompanyID() {
        Optional<OrganizationUnitOptionReport> organizationUnitOptionReport = organizationUnitOptionReportService.findOneByCompanyID();
        return ResponseUtil.wrapOrNotFound(organizationUnitOptionReport);
    }

    /**
     * DELETE  /organization-unit-option-reports/:id : delete the "id" organizationUnitOptionReport.
     *
     * @param id the id of the organizationUnitOptionReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organization-unit-option-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganizationUnitOptionReport(@PathVariable UUID id) {
        log.debug("REST request to delete OrganizationUnitOptionReport : {}", id);
        organizationUnitOptionReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/organization-unit-option-reports/find-by-organization-unit")
    @Timed
    public ResponseEntity<OrganizationUnitOptionReport> findByOrganizationUnitID(@RequestParam(required = false) UUID orgID

    ) {
        OrganizationUnitOptionReport organizationUnitOptionReport = organizationUnitOptionReportService.findByOrganizationUnitID(orgID);
        return new ResponseEntity<>(organizationUnitOptionReport, HttpStatus.OK);
    }
}
