package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.Record;
import vn.softdreams.ebweb.domain.SaReturn;
import vn.softdreams.ebweb.service.SaReturnDetailsService;
import vn.softdreams.ebweb.service.SaReturnService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing SaReturn.
 */
@RestController
@RequestMapping("/api")
public class SaReturnResource {

    private final Logger log = LoggerFactory.getLogger(SaReturnResource.class);

    private static final String ENTITY_NAME = "saReturn";

    private final SaReturnService saReturnService;

    private final SaReturnDetailsService saReturnDetailsService;

    public SaReturnResource(SaReturnService saReturnService, SaReturnDetailsService saReturnDetailsService) {
        this.saReturnService = saReturnService;
        this.saReturnDetailsService = saReturnDetailsService;
    }

    /**
     * POST  /sa-returns : Create a new saReturn.
     *
     * @param saReturn the saReturn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saReturn, or with status 400 (Bad Request) if the saReturn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-returns")
    @Timed
    public ResponseEntity<SaReturnSaveDTO> createSaReturn(@RequestBody SaReturnSaveDTO saReturn) throws URISyntaxException, InvocationTargetException, IllegalAccessException {
        log.debug("REST request to save SaReturn : {}", saReturn);
        SaReturnSaveDTO result = saReturnService.save(saReturn);
        return ResponseEntity.created(new URI("/api/sa-returns/" + result.getSaReturn().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getSaReturn().getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-returns : get all the saReturns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saReturns in body
     */
    @GetMapping("/sa-returns")
    @Timed
    public ResponseEntity<List<SaReturnDTO>> getAllSaBillDTOs(Pageable pageable,
                                                          @RequestParam(required = false) UUID accountingObjectID,
                                                          @RequestParam(required = false) String currencyID,
                                                          @RequestParam(required = false) String fromDate,
                                                          @RequestParam(required = false) String toDate,
                                                          @RequestParam(required = false) Boolean recorded,
                                                          @RequestParam(required = false) String freeText,
                                                          @RequestParam(defaultValue = "330") Integer typeID) {
        log.debug("REST request to get a page of SaReturns");
        Page<SaReturnDTO> page = saReturnService.getAllSaReturnDTOs(pageable, accountingObjectID, currencyID,
            fromDate, toDate, recorded, freeText, typeID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-returns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-returns/:id : get the "id" saReturn.
     *
     * @param id the id of the saReturn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saReturn, or with status 404 (Not Found)
     */
    @GetMapping("/sa-returns/{id}")
    @Timed
    public ResponseEntity<SaReturnViewDTO> getSaReturn(@PathVariable UUID id) {
        log.debug("REST request to get SaReturn : {}", id);
        SaReturnViewDTO saReturn = saReturnService.findOne(id);
        return new ResponseEntity<>(saReturn, HttpStatus.OK);
    }

    /**
     * DELETE  /sa-returns/:id : delete the "id" saReturn.
     *
     * @param id the id of the saReturn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-returns/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaReturn(@PathVariable UUID id) {
        log.debug("REST request to delete SaReturn : {}", id);
        saReturnService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/sa-returns/unrecord")
    @Timed
    public ResponseEntity<Record> unrecord(@RequestBody Record record) {
        log.debug("REST request to delete SaReturn : {}", record);
        record = saReturnService.unrecord(record);
        return ResponseEntity.status(HttpStatus.OK)
            .headers(HeaderUtil.createEntityUnrecordedAlert(ENTITY_NAME, record.getMsg()))
            .body(record);
    }

    @GetMapping("/sa-returns/index")
    @Timed
    public ResponseEntity<SaReturnViewDTO> getNextSaReturnDTOs(Pageable pageable,
                                                               @RequestParam(required = false) UUID accountingObjectID,
                                                               @RequestParam(required = false) String currencyID,
                                                               @RequestParam(required = false) String fromDate,
                                                               @RequestParam(required = false) String toDate,
                                                               @RequestParam(required = false) Boolean recorded,
                                                               @RequestParam(required = false) String freeText,
                                                               @RequestParam(required = false) Integer rowIndex,
                                                               @RequestParam(required = false) UUID id,
                                                               @RequestParam(defaultValue = "330") Integer typeID) {
        log.debug("REST request to get a page of SaBills");
        SaReturnViewDTO saBill = saReturnService.getNextSaReturnDTOs(pageable, accountingObjectID, currencyID,
            fromDate, toDate, recorded, freeText, rowIndex, typeID, id);
        return new ResponseEntity<>(saBill, HttpStatus.OK);
    }

    @GetMapping("/sa-returns/search-data")
    @Timed
    public ResponseEntity<SaReturnSearchDTO> getAllSearchData() {
        log.debug("REST request to get a page of SaBills");
        SaReturnSearchDTO dto = saReturnService.getAllSearchData();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/sa-returns/export/excel")
    @Timed
    public ResponseEntity<byte[]> export(@RequestParam(required = false) UUID accountingObjectID,
                                         @RequestParam(required = false) String currencyID,
                                         @RequestParam(required = false) String fromDate,
                                         @RequestParam(required = false) String toDate,
                                         @RequestParam(required = false) Boolean recorded,
                                         @RequestParam(required = false) String freeText,
                                         @RequestParam(defaultValue = "330") Integer typeID) throws IOException {
        byte[] export = saReturnService.export(accountingObjectID, currencyID, fromDate, toDate, recorded, freeText, typeID);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-returns/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) UUID accountingObjectID,
                                         @RequestParam(required = false) String currencyID,
                                         @RequestParam(required = false) String fromDate,
                                         @RequestParam(required = false) String toDate,
                                         @RequestParam(required = false) Boolean recorded,
                                         @RequestParam(required = false) String freeText,
                                         @RequestParam(defaultValue = "330") Integer typeID) throws IOException {
        byte[] export = saReturnService.exportPdf(accountingObjectID, currencyID, fromDate, toDate, recorded, freeText, typeID);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }
    /***
     *
     * @param accountingObject
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping("/sa-returns/search-all-dto")
    @Timed
    public ResponseEntity<Page<SaReturnRSInwardDTO>> searchAllOrderDTO(Pageable pageable,
                                                                       @RequestParam(required = false) UUID accountingObject,
                                                                       @RequestParam(required = false) String fromDate,
                                                                       @RequestParam(required = false) String toDate) {
        log.debug("REST request to getAll SaReturnRSInwardDTO");
        Page<SaReturnRSInwardDTO> page = saReturnService.findAllSaReturnDTO(pageable, accountingObject, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/sa-return/details")
    @Timed
    public ResponseEntity<List<SaReturnDetailsRSInwardDTO>> findAllDetailsById(@RequestParam List<UUID> id) {
        log.debug("REST request to get SaReturn : {}", id);
        List<SaReturnDetailsRSInwardDTO> details = saReturnDetailsService.findAllDetailsById(id);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/sa-returns/check-relate-voucher")
    @Timed
    public ResponseEntity<Boolean> checkRelateVoucher(@RequestParam(required = false) UUID saReturnID) {
        Boolean check = saReturnService.checkRelateVoucher(saReturnID);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @PostMapping("/sa-returns/multi-delete-sa-returns")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteSAReturn(@Valid @RequestBody List<SaReturn> saReturns) {
        log.debug("REST request to closeBook : {}", saReturns);
        HandlingResultDTO responeCloseBookDTO = saReturnService.multiDelete(saReturns);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/sa-returns/multi-unrecord-sa-returns")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnrecordSAReturn(@Valid @RequestBody List<SaReturn> saReturns) {
        log.debug("REST request to closeBook : {}", saReturns);
        HandlingResultDTO responeCloseBookDTO = saReturnService.multiUnrecord(saReturns);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

}
