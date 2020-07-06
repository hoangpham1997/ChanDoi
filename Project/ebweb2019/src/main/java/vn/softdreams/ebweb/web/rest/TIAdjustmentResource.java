package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIAdjustment;
import vn.softdreams.ebweb.service.TIAdjustmentService;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailAllConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailAllDTO;
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
 * REST controller for managing TIAdjustment.
 */
@RestController
@RequestMapping("/api")
public class TIAdjustmentResource {

    private final Logger log = LoggerFactory.getLogger(TIAdjustmentResource.class);

    private static final String ENTITY_NAME = "tIAdjustment";

    private final TIAdjustmentService tIAdjustmentService;

    public TIAdjustmentResource(TIAdjustmentService tIAdjustmentService) {
        this.tIAdjustmentService = tIAdjustmentService;
    }

    /**
     * POST  /t-i-adjustments : Create a new tIAdjustment.
     *
     * @param tIAdjustment the tIAdjustment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAdjustment, or with status 400 (Bad Request) if the tIAdjustment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-adjustments")
    @Timed
    public ResponseEntity<TIAdjustment> createTIAdjustment(@Valid @RequestBody TIAdjustment tIAdjustment) throws URISyntaxException {
        log.debug("REST request to save TIAdjustment : {}", tIAdjustment);
        if (tIAdjustment.getId() != null) {
            throw new BadRequestAlertException("A new tIAdjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIAdjustment result = tIAdjustmentService.save(tIAdjustment);
        return ResponseEntity.created(new URI("/api/t-i-adjustments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-adjustments : Updates an existing tIAdjustment.
     *
     * @param tIAdjustment the tIAdjustment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAdjustment,
     * or with status 400 (Bad Request) if the tIAdjustment is not valid,
     * or with status 500 (Internal Server Error) if the tIAdjustment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-adjustments")
    @Timed
    public ResponseEntity<TIAdjustment> updateTIAdjustment(@Valid @RequestBody TIAdjustment tIAdjustment) throws URISyntaxException {
        log.debug("REST request to update TIAdjustment : {}", tIAdjustment);
        if (tIAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIAdjustment result = tIAdjustmentService.save(tIAdjustment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAdjustment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-adjustments : get all the tIAdjustments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAdjustments in body
     */
    @GetMapping("/t-i-adjustments")
    @Timed
    public ResponseEntity<List<TIAdjustment>> getAllTIAdjustments(Pageable pageable) {
        log.debug("REST request to get a page of TIAdjustments");
        Page<TIAdjustment> page = tIAdjustmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-adjustments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-adjustments/:id : get the "id" tIAdjustment.
     *
     * @param id the id of the tIAdjustment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAdjustment, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-adjustments/{id}")
    @Timed
    public ResponseEntity<TIAdjustment> getTIAdjustment(@PathVariable UUID id) {
        log.debug("REST request to get TIAdjustment : {}", id);
        Optional<TIAdjustment> tIAdjustment = tIAdjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAdjustment);
    }

    /**
     * DELETE  /t-i-adjustments/:id : delete the "id" tIAdjustment.
     *
     * @param id the id of the tIAdjustment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-adjustments/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAdjustment(@PathVariable UUID id) {
        log.debug("REST request to delete TIAdjustment : {}", id);
        tIAdjustmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/t-i-adjustments/load-all")
    @Timed
    public ResponseEntity<List<TITransferAndTIAdjustmentConvertDTO>> getAllTIAdjustmentsSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TITransferAndTIAdjustmentConvertDTO> page = tIAdjustmentService.getAllTIAdjustmentsSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/t-i-adjustments/details/{id}")
    @Timed
    public ResponseEntity<TIAdjustmentDetailAllConvertDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get TIAudit : {}", id);
        TIAdjustmentDetailAllConvertDTO tIAudit = tIAdjustmentService.findDetailsByID(id);
        return new ResponseEntity<>(tIAudit, HttpStatus.OK);
    }

}
