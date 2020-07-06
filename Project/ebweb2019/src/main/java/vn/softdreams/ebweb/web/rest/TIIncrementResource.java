package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.poi.ss.formula.functions.T;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.service.TIIncrementService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
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
 * REST controller for managing TIIncrement.
 */
@RestController
@RequestMapping("/api")
public class TIIncrementResource {

    private final Logger log = LoggerFactory.getLogger(TIIncrementResource.class);

    private static final String ENTITY_NAME = "tIIncrement";

    private final TIIncrementService tIIncrementService;

    public TIIncrementResource(TIIncrementService tIIncrementService) {
        this.tIIncrementService = tIIncrementService;
    }

    /**
     * POST  /t-i-increments : Create a new tIIncrement.
     *
     * @param tIIncrement the tIIncrement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIIncrement, or with status 400 (Bad Request) if the tIIncrement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-increments")
    @Timed
    public ResponseEntity<TIIncrement> createTIIncrement(@Valid @RequestBody TIIncrement tIIncrement) throws URISyntaxException {
        log.debug("REST request to save TIIncrement : {}", tIIncrement);
        if (tIIncrement.getId() != null) {
            throw new BadRequestAlertException("A new tIIncrement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIIncrement result = tIIncrementService.save(tIIncrement);
        return ResponseEntity.created(new URI("/api/t-i-increments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-increments : Updates an existing tIIncrement.
     *
     * @param tIIncrement the tIIncrement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIIncrement,
     * or with status 400 (Bad Request) if the tIIncrement is not valid,
     * or with status 500 (Internal Server Error) if the tIIncrement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-increments")
    @Timed
    public ResponseEntity<TIIncrement> updateTIIncrement(@Valid @RequestBody TIIncrement tIIncrement) throws URISyntaxException {
        log.debug("REST request to update TIIncrement : {}", tIIncrement);
        if (tIIncrement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIIncrement result = tIIncrementService.save(tIIncrement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIIncrement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-increments : get all the tIIncrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIIncrements in body
     */
    @GetMapping("/t-i-increments/load-all")
    @Timed
    public ResponseEntity<List<TIIncrementConvertDTO>> getAllTIIncrementsSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TIIncrementConvertDTO> page = tIIncrementService.getAllTIIncrementsSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
 /**
     * GET  /t-i-increments : get all the tIIncrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIIncrements in body
     */
    @GetMapping("/t-i-increments/find-by-row-num")
    @Timed
    public ResponseEntity<Optional<TIIncrement>> findByRowNum(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch, Integer rowNum) {
        log.debug("REST request to get a page of TIIncrements");
        Optional<TIIncrement> page = tIIncrementService.findByRowNum(pageable, fromDate, toDate, keySearch, rowNum);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/t-i-increments")
    @Timed
    public ResponseEntity<List<TIIncrement>> getAllTIIncrements(Pageable pageable) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TIIncrement> page = tIIncrementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/t-i-increments/get-tool")
    @Timed
    public ResponseEntity<List<ToolsConvertDTO>> getAllToolsByActive() {
        log.debug("REST request to get a page of TIIncrements");
        List<ToolsConvertDTO> page = tIIncrementService.getAllToolsByActive();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    //    load tool màn ghi giảm
    @GetMapping("/t-i-increments/get-tool-by-decrement")
    @Timed
    public ResponseEntity<List<ToolsConvertDTO>> getToolsActiveByTIDecrement(@RequestParam String date) {
        log.debug("REST request to get a page of TIIncrements");
        List<ToolsConvertDTO> page = tIIncrementService.getToolsActiveByTIDecrement(date);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
//    load tool màn ghi tăng
    @GetMapping("/t-i-increments/get-tool-increments")
    @Timed
    public ResponseEntity<List<ToolsConvertDTO>> getToolsActiveByIncrements(@RequestParam String date) {
        log.debug("REST request to get a page of TIIncrements");
        List<ToolsConvertDTO> page = tIIncrementService.getToolsActiveByIncrements(date);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /t-i-increments/:id : get the "id" tIIncrement.
     *
     * @param id the id of the tIIncrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIIncrement, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-increments/{id}")
    @Timed
    public ResponseEntity<TIIncrement> getTIIncrement(@PathVariable UUID id) {
        log.debug("REST request to get TIIncrement : {}", id);
        Optional<TIIncrement> tIIncrement = tIIncrementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIIncrement);
    }
    /**
     * GET  /t-i-increments/:id : get the "id" tIIncrement.
     *
     * @param id the id of the tIIncrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIIncrement, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-increments/organization-unit-active-by-tools-id")
    @Timed
    public ResponseEntity<List<OrganizationUnitCustomDTO>> getOrganizationUnitByToolsIDTIDecrement(@RequestParam UUID id, @RequestParam String date) {
        log.debug("REST request to get TIIncrement : {}", id);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = tIIncrementService.getOrganizationUnitByToolsIDTIDecrement(id, date);
        return new ResponseEntity<>(organizationUnitCustomDTOS, HttpStatus.OK);
    }
    /**
     * GET  /t-i-increments/:id : get the "id" tIIncrement.
     *
     * @param id the id of the tIIncrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIIncrement, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-increments/organization-unit-active-by-tools-id-ti")
    @Timed
    public ResponseEntity<List<OrganizationUnitCustomDTO>> getOrganizationUnitByToolsIDTIIncrement(@RequestParam UUID id, @RequestParam String date) {
        log.debug("REST request to get TIIncrement : {}", id);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = tIIncrementService.getOrganizationUnitByToolsIDTIIncrement(id, date);
        return new ResponseEntity<>(organizationUnitCustomDTOS, HttpStatus.OK);
    }

    @GetMapping("/t-i-increments/details/{id}")
    @Timed
    public ResponseEntity<TIIncrementAllDetailsConvertDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get TIIncrement : {}", id);
        TIIncrementAllDetailsConvertDTO tIIncrement = tIIncrementService.findDetailsByID(id);
        return new ResponseEntity<>(tIIncrement, HttpStatus.OK);
    }

    /**
     * DELETE  /t-i-increments/:id : delete the "id" tIIncrement.
     *
     * @param id the id of the tIIncrement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-increments/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIIncrement(@PathVariable UUID id) {
        log.debug("REST request to delete TIIncrement : {}", id);
        tIIncrementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/t-i-increments/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteTIIncrements(@RequestBody List<TIIncrement> tiIncrements) {
        log.debug("REST request to closeBook : {}", tiIncrements);
        HandlingResultDTO rsCloseBookDTO = tIIncrementService.multiDelete(tiIncrements);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }/**
     * record  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/t-i-increments/multi-unrecord")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnRecordTIIncrements(@RequestBody List<TIIncrement> tiIncrements) {
        log.debug("REST request to closeBook : {}", tiIncrements);
        HandlingResultDTO rsCloseBookDTO = tIIncrementService.multiUnRecord(tiIncrements);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }
}
