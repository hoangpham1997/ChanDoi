package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TITransfer;
import vn.softdreams.ebweb.service.TITransferService;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailsAllConvertDTO;
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
 * REST controller for managing TITransfer.
 */
@RestController
@RequestMapping("/api")
public class TITransferResource {

    private final Logger log = LoggerFactory.getLogger(TITransferResource.class);

    private static final String ENTITY_NAME = "tITransfer";

    private final TITransferService tITransferService;

    public TITransferResource(TITransferService tITransferService) {
        this.tITransferService = tITransferService;
    }

    /**
     * POST  /t-i-transfers : Create a new tITransfer.
     *
     * @param tITransfer the tITransfer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tITransfer, or with status 400 (Bad Request) if the tITransfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-transfers")
    @Timed
    public ResponseEntity<TITransfer> createTITransfer(@RequestBody TITransfer tITransfer) throws URISyntaxException {
        log.debug("REST request to save TITransfer : {}", tITransfer);
        if (tITransfer.getId() != null) {
            throw new BadRequestAlertException("A new tITransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TITransfer result = tITransferService.save(tITransfer);
        return ResponseEntity.created(new URI("/api/t-i-transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-transfers : Updates an existing tITransfer.
     *
     * @param tITransfer the tITransfer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tITransfer,
     * or with status 400 (Bad Request) if the tITransfer is not valid,
     * or with status 500 (Internal Server Error) if the tITransfer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-transfers")
    @Timed
    public ResponseEntity<TITransfer> updateTITransfer(@RequestBody TITransfer tITransfer) throws URISyntaxException {
        log.debug("REST request to update TITransfer : {}", tITransfer);
        if (tITransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TITransfer result = tITransferService.save(tITransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tITransfer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-transfers : get all the tITransfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tITransfers in body
     */
    @GetMapping("/t-i-transfers")
    @Timed
    public ResponseEntity<List<TITransfer>> getAllTITransfers(Pageable pageable) {
        log.debug("REST request to get a page of TITransfers");
        Page<TITransfer> page = tITransferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-transfers/:id : get the "id" tITransfer.
     *
     * @param id the id of the tITransfer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tITransfer, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-transfers/{id}")
    @Timed
    public ResponseEntity<TITransfer> getTITransfer(@PathVariable UUID id) {
        log.debug("REST request to get TITransfer : {}", id);
        Optional<TITransfer> tITransfer = tITransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tITransfer);
    }

    /**
     * DELETE  /t-i-transfers/:id : delete the "id" tITransfer.
     *
     * @param id the id of the tITransfer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTITransfer(@PathVariable UUID id) {
        log.debug("REST request to delete TITransfer : {}", id);
        tITransferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/t-i-transfers/get-tools-ti-transfer")
    @Timed
    public ResponseEntity<List<ToolsConvertDTO>> getToolsActiveByTITransfer() {
        log.debug("REST request to get a page of TIIncrements");
        List<ToolsConvertDTO> page = tITransferService.getToolsActiveByTITransfer();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/t-i-transfers/load-all")
    @Timed
    public ResponseEntity<List<TITransferAndTIAdjustmentConvertDTO>> getAllTITransferSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TITransferAndTIAdjustmentConvertDTO> page = tITransferService.getAllTITransferSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/t-i-transfers/details/{id}")
    @Timed
    public ResponseEntity<TITransferDetailsAllConvertDTO> findDetailsByID(@PathVariable UUID id) {
        log.debug("REST request to get TIAudit : {}", id);
        TITransferDetailsAllConvertDTO tiTransferDetailsAllConvertDTO = tITransferService.findDetailsByID(id);
        return new ResponseEntity<>(tiTransferDetailsAllConvertDTO, HttpStatus.OK);
    }

}
