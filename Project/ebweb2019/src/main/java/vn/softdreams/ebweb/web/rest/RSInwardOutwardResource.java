package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.RSInwardOutwardService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.ViewRSOutwardDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;
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
 * REST controller for managing RSInwardOutward.
 */
@RestController
@RequestMapping("/api")
public class RSInwardOutwardResource {

    private final Logger log = LoggerFactory.getLogger(RSInwardOutwardResource.class);

    private static final String ENTITY_NAME = "rSInwardOutward";

    private final RSInwardOutwardService rSInwardOutwardService;

    @Autowired
    private UtilsRepository utilsRepository;

    public RSInwardOutwardResource(RSInwardOutwardService rSInwardOutwardService) {
        this.rSInwardOutwardService = rSInwardOutwardService;
    }

    /**
     * POST  /rs-inward-outwards : Create a new rSInwardOutward.
     *
     * @param rSInwardOutward the rSInwardOutward to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rSInwardOutward, or with status 400 (Bad Request) if the rSInwardOutward has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rs-inward-outwards")
    @Timed
    public ResponseEntity<RSInwardOutward> createRSInwardOutward(@Valid @RequestBody RSInwardOutwardSaveDTO rSInwardOutward) throws URISyntaxException {
        log.debug("REST request to save RSInwardOutward : {}", rSInwardOutward);
        if (rSInwardOutward.getRsInwardOutward().getId() != null) {
            throw new BadRequestAlertException("A new rSInwardOutward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RSInwardOutwardSaveDTO result = rSInwardOutwardService.save(rSInwardOutward);
        return ResponseEntity.created(new URI("/api/rs-inward-outwards/" + result.getRsInwardOutward().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getRsInwardOutward().getId().toString()))
            .body(result.getRsInwardOutward());
    }

    /**
     * PUT  /rs-inward-outwards : Updates an existing rSInwardOutward.
     *
     * @param rSInwardOutward the rSInwardOutward to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rSInwardOutward,
     * or with status 400 (Bad Request) if the rSInwardOutward is not valid,
     * or with status 500 (Internal Server Error) if the rSInwardOutward couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rs-inward-outwards")
    @Timed
    public ResponseEntity<RSInwardOutward> updateRSInwardOutward(@Valid @RequestBody RSInwardOutwardSaveDTO rSInwardOutward) throws URISyntaxException {
        log.debug("REST request to update RSInwardOutward : {}", rSInwardOutward);
        if (rSInwardOutward.getRsInwardOutward().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RSInwardOutwardSaveDTO result = rSInwardOutwardService.save(rSInwardOutward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rSInwardOutward.getRsInwardOutward().getId().toString()))
            .body(result.getRsInwardOutward());
    }

    /**
     * GET  /rs-inward-outwards : get all the rSInwardOutwards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rSInwardOutwards in body
     */
    @GetMapping("/rs-inward-outwards")
    @Timed
    public ResponseEntity<List<RSInwardOutward>> getAllRSInwardOutwards(Pageable pageable) {
        log.debug("REST request to get a page of RSInwardOutwards");
        Page<RSInwardOutward> page = rSInwardOutwardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rs-inward-outwards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rs-inward-outwards/:id : get the "id" rSInwardOutward.
     *
     * @param id the id of the rSInwardOutward to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rSInwardOutward, or with status 404 (Not Found)
     */
    @GetMapping("/rs-inward-outwards/{id}")
    @Timed
    public ResponseEntity<RSInwardOutward> getRSInwardOutward(@PathVariable UUID id) {
        log.debug("REST request to get RSInwardOutward : {}", id);
        Optional<RSInwardOutward> rSInwardOutward = rSInwardOutwardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rSInwardOutward);
    }

    /**
     * DELETE  /rs-inward-outwards/:id : delete the "id" rSInwardOutward.
     *
     * @param id the id of the rSInwardOutward to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rs-inward-outwards/{id}")
    @Timed
    public ResponseEntity<Void> deleteRSInwardOutward(@PathVariable UUID id) {
        log.debug("REST request to delete RSInwardOutward : {}", id);
        rSInwardOutwardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/rs-inward-outwards/search-all")
    @Timed
    public ResponseEntity<List<RSInwardOutwardSearchDTO>> searchAll(Pageable pageable,
                                                                    @RequestParam(required = false) UUID accountingObject,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) Integer noType,
                                                                    @RequestParam(required = false) String fromDate,
                                                                    @RequestParam(required = false) String toDate,
                                                                    @RequestParam(required = false) String searchValue
    ) {
        log.debug("REST request to get a page of RSInwardOutward");
        Page<RSInwardOutwardSearchDTO> page = rSInwardOutwardService.searchAll(pageable, accountingObject, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nhap-kho");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) UUID accountingObject,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) Integer noType,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSInwardOutwardService.exportPdf(accountingObject, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) UUID accountingObject,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) Integer noType,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSInwardOutwardService.exportExcel(accountingObject, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/exportOutWard/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdfOutWard(@RequestParam(required = false) UUID accountingObject,
                                            @RequestParam(required = false) UUID accountingObjectEmployee,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) Integer noType,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSInwardOutwardService.exportPdfOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/exportOutWard/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcelOutWard(@RequestParam(required = false) UUID accountingObject,
                                              @RequestParam(required = false) UUID accountingObjectEmployee,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) Integer noType,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String searchValue
    ) {
        byte[] export = rSInwardOutwardService.exportExcelOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/search-all-out-ward")
    @Timed
    public ResponseEntity<List<RSInwardOutwardSearchDTO>> searchAllOutWard(Pageable pageable,
                                                                           @RequestParam(required = false) UUID accountingObject,
                                                                           @RequestParam(required = false) UUID accountingObjectEmployee,
                                                                           @RequestParam(required = false) Integer status,
                                                                           @RequestParam(required = false) Integer noType,
                                                                           @RequestParam(required = false) String fromDate,
                                                                           @RequestParam(required = false) String toDate,
                                                                           @RequestParam(required = false) String searchValue
    ) {
        log.debug("REST request to get a page of RSInwardOutward");
        Page<RSInwardOutwardSearchDTO> page = rSInwardOutwardService.searchAllOutWard(pageable, accountingObject, accountingObjectEmployee, status, noType, fromDate, toDate, searchValue);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/xuat-kho");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/in-ward-details/{id}/{typeID}")
    @Timed
    public ResponseEntity<List<RSInwardOutWardDetails>> getDetailsById(@PathVariable UUID id, @PathVariable Integer typeID) {
        log.debug("REST request to get a page of RSInwardOutWardDetails");
        List<RSInwardOutWardDetails> details = rSInwardOutwardService.getDetailsById(id, typeID);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/out-ward-details/{id}/{typeID}")
    @Timed
    public ResponseEntity<List<RSInwardOutWardDetails>> getDetailsOutWardById(@PathVariable UUID id, @PathVariable Integer typeID) {
        log.debug("REST request to get a page of RSInwardOutWardDetails");
        List<RSInwardOutWardDetails> details = rSInwardOutwardService.getDetailsOutWardById(id, typeID);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/reference-table/{id}/{typeID}")
    @Timed
    public ResponseEntity<UUID> getReferenceIDByRSInwardID(@PathVariable UUID id, @PathVariable Integer typeID) {
        log.debug("REST request to get a page of UUID {}  {}", id, typeID);
        UUID uuid = rSInwardOutwardService.getReferenceIDByRSInwardID(id, typeID);
        return new ResponseEntity<>(uuid, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/index/reference-tables")
    @Timed
    public ResponseEntity<RSInwardOutwardSearchDTO> findReferenceTablesID(@RequestParam(required = false) UUID accountingObject,
                                                                          @RequestParam(required = false) UUID accountingObjectEmployee,
                                                                          @RequestParam(required = false) Integer status,
                                                                          @RequestParam(required = false) Integer noType,
                                                                          @RequestParam(required = false) String fromDate,
                                                                          @RequestParam(required = false) String toDate,
                                                                          @RequestParam(required = false) String searchValue,
                                                                          @RequestParam(required = false) Integer rowNum
    ) {
        RSInwardOutwardSearchDTO rsInwardOutward = rSInwardOutwardService.findReferenceTablesID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, rowNum);
        return new ResponseEntity<>(rsInwardOutward, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/index/reference-table")
    @Timed
    public ResponseEntity<RSInwardOutwardSearchDTO> findReferenceTableID(@RequestParam(required = false) UUID accountingObject,
                                                                         @RequestParam(required = false) Integer status,
                                                                         @RequestParam(required = false) Integer noType,
                                                                         @RequestParam(required = false) String fromDate,
                                                                         @RequestParam(required = false) String toDate,
                                                                         @RequestParam(required = false) String searchValue,
                                                                         @RequestParam(required = false) Integer rowNum
    ) {
        RSInwardOutwardSearchDTO rsInwardOutward = rSInwardOutwardService.findReferenceTableID(accountingObject, status, fromDate, toDate, noType, searchValue, rowNum);
        return new ResponseEntity<>(rsInwardOutward, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/row-num")
    @Timed
    public ResponseEntity<Integer> findRowNumByID(@RequestParam(required = false) UUID accountingObject,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam(required = false) Integer noType,
                                                  @RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  @RequestParam(required = false) String searchValue,
                                                  @RequestParam(required = false) UUID id
    ) {
        log.debug("REST request to get a page of findByRowNum");
        Integer rowNum = rSInwardOutwardService.findRowNumByID(accountingObject, status, fromDate, toDate, noType, searchValue, id);
        return new ResponseEntity<>(rowNum, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/row-num-out-ward")
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
        Integer rowNum = rSInwardOutwardService.findRowNumOutWardByID(accountingObject, accountingObjectEmployee, status, fromDate, toDate, noType, searchValue, id);
        return new ResponseEntity<>(rowNum, HttpStatus.OK);
    }

    @GetMapping("/rs-inward-outwards/ViewRSOutWardDTO")
    @Timed
    public ResponseEntity<List<ViewRSOutwardDTO>> getViewRSOutwardDTOPopup(Pageable pageable,
                                                                           @RequestParam(required = false) UUID accountingObjectID,
                                                                           @RequestParam(required = false) String fromDate,
                                                                           @RequestParam(required = false) String toDate,
                                                                           @RequestParam(required = false) String currencyID
    ) {
        log.debug("REST request to get a page of ViewRSOutWardDTO");
        Page<ViewRSOutwardDTO> page = rSInwardOutwardService.getViewRSOutwardDTOPopup(pageable, accountingObjectID, fromDate, toDate, currencyID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rs-inward-outwards/popup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * @author dungvm
     *
     * Sau khi bỏ ghi sổ, xóa id của các trường liên kết
     *
     * @param ids id của những rsinwardoutwarddetail lập từ màn khác
     */
    @GetMapping("/rs-inward-outwards/detail/un-record")
    @Timed
    public void unRecordDetails(@RequestParam("ids") List<UUID> ids) {
        log.debug("REST request to get RSInwardOutWardDetails : {}", ids);
        rSInwardOutwardService.unRecordDetails(ids);
    }

    @PostMapping("/rs-inward-outwards/multi-delete-rsInwardOutward")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeleteRSInwardOutward(@Valid @RequestBody List<RSInwardOutward> rsInwardOutward) {
        log.debug("REST request to closeBook : {}", rsInwardOutward);
        HandlingResultDTO responeCloseBookDTO = rSInwardOutwardService.multiDelete(rsInwardOutward);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/rs-inward-outwards/multi-unrecord-rs-inward-outward-returns")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiUnRecordRSInwardOutward(@Valid @RequestBody List<RSInwardOutward> rsInwardOutward) {
        log.debug("REST request to closeBook : {}", rsInwardOutward);
        HandlingResultDTO responeCloseBookDTO = rSInwardOutwardService.multiUnrecord(rsInwardOutward);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
