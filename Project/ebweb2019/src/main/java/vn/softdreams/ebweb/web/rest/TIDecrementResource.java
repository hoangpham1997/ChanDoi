package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.TIAudit;
import vn.softdreams.ebweb.domain.TIDecrement;
import vn.softdreams.ebweb.service.TIDecrementService;
import vn.softdreams.ebweb.service.dto.*;
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
 * REST controller for managing TIDecrement.
 */
@RestController
@RequestMapping("/api")
public class TIDecrementResource {

    private final Logger log = LoggerFactory.getLogger(TIDecrementResource.class);

    private static final String ENTITY_NAME = "tIDecrement";

    private final TIDecrementService tIDecrementService;

    public TIDecrementResource(TIDecrementService tIDecrementService) {
        this.tIDecrementService = tIDecrementService;
    }

    /**
     * POST  /t-i-decrements : Create a new tIDecrement.
     *
     * @param tIDecrement the tIDecrement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIDecrement, or with status 400 (Bad Request) if the tIDecrement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-decrements")
    @Timed
    public ResponseEntity<TIDecrement> createTIDecrement(@Valid @RequestBody TIDecrement tIDecrement) throws URISyntaxException {
        log.debug("REST request to save TIDecrement : {}", tIDecrement);
        if (tIDecrement.getId() != null) {
            throw new BadRequestAlertException("A new tIDecrement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIDecrement result = tIDecrementService.save(tIDecrement);
        return ResponseEntity.created(new URI("/api/t-i-decrements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-decrements : Updates an existing tIDecrement.
     *
     * @param tIDecrement the tIDecrement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIDecrement,
     * or with status 400 (Bad Request) if the tIDecrement is not valid,
     * or with status 500 (Internal Server Error) if the tIDecrement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-decrements")
    @Timed
    public ResponseEntity<TIDecrement> updateTIDecrement(@Valid @RequestBody TIDecrement tIDecrement) throws URISyntaxException {
        log.debug("REST request to update TIDecrement : {}", tIDecrement);
        if (tIDecrement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIDecrement result = tIDecrementService.save(tIDecrement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIDecrement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-decrements : get all the tIDecrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIDecrements in body
     */
    @GetMapping("/t-i-decrements")
    @Timed
    public ResponseEntity<List<TIDecrement>> getAllTIDecrements(Pageable pageable) {
        log.debug("REST request to get a page of TIDecrements");
        Page<TIDecrement> page = tIDecrementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-decrements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-decrements/:id : get the "id" tIDecrement.
     *
     * @param id the id of the tIDecrement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIDecrement, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-decrements/{id}")
    @Timed
    public ResponseEntity<TIDecrement> getTIDecrement(@PathVariable UUID id) {
        log.debug("REST request to get TIDecrement : {}", id);
        Optional<TIDecrement> tIDecrement = tIDecrementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIDecrement);
    }

    /**
     * DELETE  /t-i-decrements/:id : delete the "id" tIDecrement.
     *
     * @param id the id of the tIDecrement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-decrements/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIDecrement(@PathVariable UUID id) {
        log.debug("REST request to delete TIDecrement : {}", id);
        tIDecrementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/t-i-decrements/load-all")
    @Timed
    public ResponseEntity<List<TIDecrementConvertDTO>> getAllTIDecrementSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TIDecrementConvertDTO> page = tIDecrementService.getAllTIDecrementSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/t-i-decrements/details/{id}")
    @Timed
    public ResponseEntity<TIDecrementAllDetailsConvertDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get TIIncrement : {}", id);
        TIDecrementAllDetailsConvertDTO tIIncrement = tIDecrementService.findDetailsByID(id);
        return new ResponseEntity<>(tIIncrement, HttpStatus.OK);
    }

    /**
     * @param tiDecrements
     * xóa nhiều dòng dữ liệu màn ghi giảm
     * @return
     */
    @PostMapping("/t-i-decrements/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteTIAudit(@RequestBody List<TIDecrement> tiDecrements) {
        log.debug("REST request to closeBook : {}", tiDecrements);
        HandlingResultDTO rsCloseBookDTO = tIDecrementService.multiDelete(tiDecrements);
        return new ResponseEntity<>(rsCloseBookDTO, HttpStatus.OK);
    }

    /**
     * @param tiDecrements
     * bỏ ghi sổ nhiều dòng dữ liệu màn ghi giảm
     * @return
     */
    @PostMapping("/t-i-decrements/multi-un-record")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecord(@Valid @RequestBody List<TIDecrement> tiDecrements) {
        log.debug("REST request to closeBook : {}", tiDecrements);
        HandlingResultDTO responeCloseBookDTO = tIDecrementService.multiUnRecord(tiDecrements);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
