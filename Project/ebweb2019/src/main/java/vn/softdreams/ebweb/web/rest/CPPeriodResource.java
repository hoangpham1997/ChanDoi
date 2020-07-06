package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.util.ResourceUtils;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;
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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CPPeriod.
 */
@RestController
@RequestMapping("/api")
public class CPPeriodResource {

    private final Logger log = LoggerFactory.getLogger(CPPeriodResource.class);

    private static final String ENTITY_NAME = "cPPeriod";

    private final CPPeriodService cPPeriodService;

    public CPPeriodResource(CPPeriodService cPPeriodService) {
        this.cPPeriodService = cPPeriodService;
    }

    /**
     * POST  /cp-periods : Create a new cPPeriod.
     *
     * @param cPPeriod the cPPeriod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cPPeriod, or with status 400 (Bad Request) if the cPPeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cp-periods")
    @Timed
    public ResponseEntity<CPPeriod> createCPPeriod(@Valid @RequestBody CPPeriod cPPeriod) throws URISyntaxException {
        log.debug("REST request to save CPPeriod : {}", cPPeriod);
        if (cPPeriod.getId() != null) {
            throw new BadRequestAlertException("A new cPPeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CPPeriod result = cPPeriodService.save(cPPeriod);
        return ResponseEntity.created(new URI("/api/cp-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cp-periods : Updates an existing cPPeriod.
     *
     * @param cPPeriod the cPPeriod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cPPeriod,
     * or with status 400 (Bad Request) if the cPPeriod is not valid,
     * or with status 500 (Internal Server Error) if the cPPeriod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cp-periods")
    @Timed
    public ResponseEntity<CPPeriod> updateCPPeriod(@Valid @RequestBody CPPeriod cPPeriod) throws URISyntaxException {
        log.debug("REST request to update CPPeriod : {}", cPPeriod);
        if (cPPeriod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CPPeriod result = cPPeriodService.save(cPPeriod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cPPeriod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cp-periods : get all the cPPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPPeriods in body
     */
    @GetMapping("/cp-periods")
    @Timed
    public ResponseEntity<List<CPPeriod>> getAllCPPeriods(Pageable pageable) {
        log.debug("REST request to get a page of CPPeriods");
        Page<CPPeriod> page = cPPeriodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-periods : get all the cPPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cPPeriods in body
     */
    @GetMapping("/cp-periods/getAllByType")
    @Timed
    public ResponseEntity<List<CPPeriodDTO>> getAllCPPeriodsByType(Pageable pageable, @RequestParam Integer type) {
        log.debug("REST request to get a page of CPPeriods");
        Page<CPPeriodDTO> page = cPPeriodService.findAllByType(pageable, type);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cp-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cp-periods/:id : get the "id" cPPeriod.
     *
     * @param id the id of the cPPeriod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cPPeriod, or with status 404 (Not Found)
     */
    @GetMapping("/cp-periods/{id}")
    @Timed
    public ResponseEntity<CPPeriod> getCPPeriod(@PathVariable UUID id) {
        log.debug("REST request to get CPPeriod : {}", id);
        Optional<CPPeriod> cPPeriod = cPPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cPPeriod);
    }

    /**
     * DELETE  /cp-periods/:id : delete the "id" cPPeriod.
     *
     * @param id the id of the cPPeriod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cp-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteCPPeriod(@PathVariable UUID id) {
        log.debug("REST request to delete CPPeriod : {}", id);
        cPPeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/cp-periods/checkDelete")
    @Timed
    public ResponseEntity<Integer> checkDelete(@RequestParam UUID id) {
        log.debug("REST request to delete CPPeriod : {}", id);
        Integer value = cPPeriodService.checkDelete(id);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @GetMapping("/cp-periods/checkPeriod")
    @Timed
    public ResponseEntity<Boolean> checkPeriod(@RequestParam String fromDate, @RequestParam String toDate, @RequestParam List<UUID> costSetIDs, @RequestParam Integer type) {
        Boolean check = cPPeriodService.checkPeriod(fromDate, toDate, costSetIDs, type);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @GetMapping("/cp-periods/get-all-c-p-periods")
    @Timed
    public ResponseEntity<List<CPPeriod>> getAllCPPeriod() {
        log.debug("REST request to get a page of Bank");
        List<CPPeriod> page = cPPeriodService.getAllCPPeriod();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cp-periods/get-all-c-p-periods-for-report")
    @Timed
    public ResponseEntity<List<CPPeriod>> getAllCPPeriodForReport(@RequestParam (required = false) Boolean isDependent, @RequestParam (required = false) UUID orgID) {
        log.debug("REST request to get a page of Bank");
        List<CPPeriod> page = cPPeriodService.getAllCPPeriodForReport(isDependent, orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(value = "/cp-periods/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/giathanh.xlsx");
        byte[] fileContent = Files.readAllBytes(reportFile.toPath());

        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }

    @GetMapping(value = "/cp-periods/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(Pageable pageable,
                                              @RequestParam(required = false) Integer type) throws IOException {
        byte[] export = cPPeriodService.exportExcel(pageable, type);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/cp-periods/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(Pageable pageable,
                                            @RequestParam(required = false) Integer type) throws IOException {
        byte[] export = cPPeriodService.exportPdf(pageable, type);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/cp-periods/get-cpperiod-by-id")
    @Timed
    public ResponseEntity<CPPeriodDTO> getCPPeriodByID(@RequestParam UUID id) {
        CPPeriodDTO cpPeriod = cPPeriodService.findByID(id);
        return new ResponseEntity<>(cpPeriod, HttpStatus.OK);
    }

    @PostMapping("/cp-periods/accepted")
    @Timed
    public ResponseEntity<CPPeriod> accepted(@Valid @RequestBody CPPeriod cPPeriod) {
        CPPeriod cpPeriod = cPPeriodService.accepted(cPPeriod);
        return new ResponseEntity<>(cpPeriod, HttpStatus.OK);
    }

    @PostMapping("/cp-periods/multi-delete-cp-period")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteCPPeriod(@Valid @RequestBody List<CPPeriodDTO> cpPeriodDTOS) {
        log.debug("REST request to closeBook : {}", cpPeriodDTOS);
        HandlingResultDTO responeCloseBookDTO = cPPeriodService.multiDelete(cpPeriodDTOS);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

}
