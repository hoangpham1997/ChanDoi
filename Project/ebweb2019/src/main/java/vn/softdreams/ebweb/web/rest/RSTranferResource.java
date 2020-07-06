package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.domain.RSTransfer;
import vn.softdreams.ebweb.domain.RSTransferDetail;
import vn.softdreams.ebweb.service.RSTranferService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;
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
 * REST controller for managing RSTranfer.
 */
@RestController
@RequestMapping("/api")
public class RSTranferResource {

    private final Logger log = LoggerFactory.getLogger(RSTranferResource.class);

    private static final String ENTITY_NAME = "rSTransfer";

    private final RSTranferService rSTranferService;

    public RSTranferResource(RSTranferService rSTranferService) {
        this.rSTranferService = rSTranferService;
    }

    /**
     * POST  /rs-tranfers : Create a new rSTranfer.
     *
     * @param rSTransfer the rSTranfer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rSTranfer, or with status 400 (Bad Request) if the rSTranfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rs-tranfers")
    @Timed
    public ResponseEntity<RSTransfer> createRSTransfer(@RequestBody RSTransferSaveDTO rSTransfer) throws URISyntaxException {
        log.debug("REST request to save RSTranfer : {}", rSTransfer);
        if (rSTransfer.getRsTransfer().getId() != null) {
            throw new BadRequestAlertException("A new rSTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RSTransferSaveDTO result = rSTranferService.save(rSTransfer);
        return ResponseEntity.created(new URI("/api/rs-transfers/" + result.getRsTransfer().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getRsTransfer().getId().toString()))
            .body(result.getRsTransfer());
    }

    /**
     * PUT  /rs-tranfers : Updates an existing rSTranfer.
     *
     * @param rSTransfer the rSTranfer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rSTranfer,
     * or with status 400 (Bad Request) if the rSTranfer is not valid,
     * or with status 500 (Internal Server Error) if the rSTranfer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rs-tranfers")
    @Timed
    public ResponseEntity<RSTransfer> updateRSTransfer(@RequestBody RSTransferSaveDTO rSTransfer) throws URISyntaxException {
        log.debug("REST request to update RSTransfer : {}", rSTransfer);
        if (rSTransfer.getRsTransfer().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RSTransferSaveDTO result = rSTranferService.save(rSTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rSTransfer.getRsTransfer().getId().toString()))
            .body(result.getRsTransfer());
    }

    /**
     * GET  /rs-tranfers : get all the rSTranfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rSTranfers in body
     */
    @GetMapping("/rs-tranfers")
    @Timed
    public ResponseEntity<List<RSTransfer>> getAllRSTransfers(Pageable pageable) {
        log.debug("REST request to get a page of RSTranfers");
        Page<RSTransfer> page = rSTranferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rs-tranfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rs-tranfers/:id : get the "id" rSTranfer.
     *
     * @param id the id of the rSTranfer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rSTranfer, or with status 404 (Not Found)
     */
    @GetMapping("/rs-tranfers/{id}")
    @Timed
    public ResponseEntity<RSTransfer> getRSTransfer(@PathVariable UUID id) {
        log.debug("REST request to get RSTranfer : {}", id);
        Optional<RSTransfer> rSTransfer = rSTranferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rSTransfer);
    }

    /**
     * DELETE  /rs-tranfers/:id : delete the "id" rSTranfer.
     *
     * @param id the id of the rSTranfer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rs-tranfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteRSTransfer(@PathVariable UUID id) {
        log.debug("REST request to delete RSTransfer : {}", id);
        rSTranferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/rs-tranfers/search-all-transfer")
    @Timed
    public ResponseEntity<List<RSTransferSearchDTO>> searchAllTransfer(Pageable pageable,
                                                                       @RequestParam(required = false) UUID accountingObject,
                                                                       @RequestParam(required = false) UUID accountingObjectEmployee,
                                                                       @RequestParam(required = false) Integer status,
                                                                       @RequestParam(required = false) Integer noType,
                                                                       @RequestParam(required = false) String fromDate,
                                                                       @RequestParam(required = false) String toDate,
                                                                       @RequestParam(required = false) String searchValue
    ) {
        log.debug("REST request to get a page of RSTransfer");
        Page<RSTransferSearchDTO> page = rSTranferService.searchAllTransfer(pageable, accountingObject, accountingObjectEmployee, status, noType, fromDate, toDate, searchValue);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chuyen-kho");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/search-transfer")
    @Timed
    public ResponseEntity<List<RSTransferSearchDTO>> searchAll(Pageable pageable,
                                                               @RequestParam(required = false) UUID accountingObject,
                                                               @RequestParam(required = false) UUID repository,
                                                               @RequestParam(required = false) UUID accountingObjectEmployee,
                                                               @RequestParam(required = false) Integer status,
                                                               @RequestParam(required = false) Integer noType,
                                                               @RequestParam(required = false) String fromDate,
                                                               @RequestParam(required = false) String toDate,
                                                               @RequestParam(required = false) String searchValue
    ) {
        log.debug("REST request to get a page of RSTransfer");
        Page<RSTransferSearchDTO> page = rSTranferService.searchAll(pageable, accountingObject, repository, accountingObjectEmployee, status, noType, fromDate, toDate, searchValue);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chuyen-kho");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/transfer-details/{id}/{typeID}")
    @Timed
    public ResponseEntity<List<RSTransferDetailsDTO>> getDetailsTransferById(@PathVariable UUID id, @PathVariable Integer typeID) {
        log.debug("REST request to get a page of RSTransferDetails");
        List<RSTransferDetailsDTO> details = rSTranferService.getDetailsTransferById(id, typeID);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/index/reference-tables")
    @Timed
    public ResponseEntity<RSTransferSearchDTO> findReferenceTablesID(@RequestParam(required = false) UUID accountingObject,
                                                                     @RequestParam(required = false) UUID accountingObjectEmployee,
                                                                     @RequestParam(required = false) Integer status,
                                                                     @RequestParam(required = false) Integer noType,
                                                                     @RequestParam(required = false) String fromDate,
                                                                     @RequestParam(required = false) String toDate,
                                                                     @RequestParam(required = false) String searchValue,
                                                                     @RequestParam(required = false) Integer rowNum
    ) {
        RSTransferSearchDTO rsTransfer = rSTranferService.findReferenceTablesID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, rowNum);
        return new ResponseEntity<>(rsTransfer, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/row-num")
    @Timed
    public ResponseEntity<Integer> findRowNumOutWardByID(@RequestParam(required = false) UUID accountingObject,
                                                         @RequestParam(required = false) UUID accountingObjectEmployee,
                                                         @RequestParam(required = false) Integer status,
                                                         @RequestParam(required = false) Integer noType,
                                                         @RequestParam(required = false) String fromDate,
                                                         @RequestParam(required = false) String toDate,
                                                         @RequestParam(required = false) String searchValue,
                                                         @RequestParam(required = false) UUID id
    ) {
        log.debug("REST request to get a page of findByRowNum");
        Integer rowNum = rSTranferService.findRowNumID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, id);
        return new ResponseEntity<>(rowNum, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) UUID accountingObject,
                                            @RequestParam(required = false) UUID accountingObjectEmployee,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) Integer noType,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSTranferService.exportPdf(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/rs-tranfers/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) UUID accountingObject,
                                              @RequestParam(required = false) UUID accountingObjectEmployee,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) Integer noType,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSTranferService.exportExcel(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @PostMapping("/rs-tranfers/multi-delete-rsTransfer")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteRSTransfer(@Valid @RequestBody List<RSTransfer> rsTransfers) {
        log.debug("REST request to closeBook : {}", rsTransfers);
        HandlingResultDTO responeCloseBookDTO = rSTranferService.multiDelete(rsTransfers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/rs-tranfers/multi-unrecord-rs-transfer")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecordRSTransfer(@Valid @RequestBody List<RSTransfer> rsTransfers) {
        log.debug("REST request to closeBook : {}", rsTransfers);
        HandlingResultDTO responeCloseBookDTO = rSTranferService.multiUnrecord(rsTransfers);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
