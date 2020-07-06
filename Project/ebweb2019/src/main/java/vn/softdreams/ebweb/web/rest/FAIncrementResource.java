package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.service.FAIncrementService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing FAIncrement.
 */
@RestController
@RequestMapping("/api")
public class FAIncrementResource {

    private final Logger log = LoggerFactory.getLogger(FAIncrementResource.class);

    private static final String ENTITY_NAME = "faIncrement";

    private final FAIncrementService faIncrementService;

    public FAIncrementResource(FAIncrementService faIncrementService) {
        this.faIncrementService = faIncrementService;
    }

    /**
     * POST  /fa-increments : Create a new faIncrement.
     *
     * @param faIncrement the faIncrement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new faIncrement, or with status 400 (Bad Request) if the faIncrement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fa-increments")
    @Timed
    public ResponseEntity<FAIncrement> createFAIncrement(@Valid @RequestBody FAIncrement faIncrement) throws URISyntaxException {
        log.debug("REST request to save FAIncrement : {}", faIncrement);
        if (faIncrement.getId() != null) {
            throw new BadRequestAlertException("A new faIncrement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FAIncrement result = faIncrementService.save(faIncrement);
        return ResponseEntity.created(new URI("/api/fa-increments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fa-increments : Updates an existing faIncrement.
     *
     * @param faIncrement the faIncrement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated faIncrement,
     * or with status 400 (Bad Request) if the faIncrement is not valid,
     * or with status 500 (Internal Server Error) if the faIncrement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fa-increments")
    @Timed
    public ResponseEntity<FAIncrement> updateFAIncrement(@Valid @RequestBody FAIncrement faIncrement) throws URISyntaxException {
        log.debug("REST request to update FAIncrement : {}", faIncrement);
        if (faIncrement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FAIncrement result = faIncrementService.save(faIncrement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, faIncrement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fa-increments : get all the faIncrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of faIncrements in body
     */
    @GetMapping("/fa-increments/load-all")
    @Timed
    public ResponseEntity<List<FAIncrementConvertDTO>> getAllFAIncrementsSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of FAIncrements");
        Page<FAIncrementConvertDTO> page = faIncrementService.getAllFAIncrementsSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fa-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
 /**
     * GET  /fa-increments : get all the faIncrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of faIncrements in body
     */
    @GetMapping("/fa-increments/find-by-row-num")
    @Timed
    public ResponseEntity<Optional<FAIncrement>> findByRowNum(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch, Integer rowNum) {
        log.debug("REST request to get a page of FAIncrements");
        Optional<FAIncrement> page = faIncrementService.findByRowNum(pageable, fromDate, toDate, keySearch, rowNum);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fa-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/fa-increments")
    @Timed
    public ResponseEntity<List<FAIncrement>> getAllFAIncrements(Pageable pageable) {
        log.debug("REST request to get a page of FAIncrements");
        Page<FAIncrement> page = faIncrementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fa-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/fa-increments/get-tscd")
    @Timed
    public ResponseEntity<List<FixedAssetDTO>> getAllFixedAssetByActive() {
        log.debug("REST request to get a page of FAIncrements");
        List<FixedAssetDTO> page = faIncrementService.getAllFixedAssetByActive();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fa-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /fa-increments/:id : get the "id" faIncrement.
     *
     * @param id the id of the faIncrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the faIncrement, or with status 404 (Not Found)
     */
    @GetMapping("/fa-increments/{id}")
    @Timed
    public ResponseEntity<FAIncrement> getFAIncrement(@PathVariable UUID id) {
        log.debug("REST request to get FAIncrement : {}", id);
        Optional<FAIncrement> faIncrement = faIncrementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faIncrement);
    }

    @GetMapping("/fa-increments/details/{id}")
    @Timed
    public ResponseEntity<FAIncrementAllDetailsConvertDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get FAIncrement : {}", id);
        FAIncrementAllDetailsConvertDTO faIncrement = faIncrementService.findDetailsByID(id);
        return new ResponseEntity<>(faIncrement, HttpStatus.OK);
    }

    /**
     * DELETE  /fa-increments/:id : delete the "id" faIncrement.
     *
     * @param id the id of the faIncrement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fa-increments/{id}")
    @Timed
    public ResponseEntity<Void> deleteFAIncrement(@PathVariable UUID id) {
        log.debug("REST request to delete FAIncrement : {}", id);
        faIncrementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/fa-increments/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteFAIncrements(@RequestBody List<FAIncrement> tiIncrements) {
        log.debug("REST request to closeBook : {}", tiIncrements);
        HandlingResultDTO rsCloseBookDTO = faIncrementService.multiDelete(tiIncrements);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }/**
     * record  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/fa-increments/multi-unrecord")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnRecordFAIncrements(@RequestBody List<FAIncrement> tiIncrements) {
        log.debug("REST request to closeBook : {}", tiIncrements);
        HandlingResultDTO rsCloseBookDTO = faIncrementService.multiUnRecord(tiIncrements);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }

    @GetMapping("/fa-increments/g-t/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String keySearch
    ) {
        byte[] export = faIncrementService.exportPdf(fromDate, toDate, keySearch);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping("/fa-increments/g-t/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String keySearch
    ) {
        byte[] export = faIncrementService.exportExcel(fromDate, toDate, keySearch);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }
}
