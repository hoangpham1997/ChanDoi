package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.SAQuote;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.service.SAQuoteService;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteViewDTO;
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
 * REST controller for managing SAQuote.
 */
@RestController
@RequestMapping("/api")
public class SAQuoteResource {

    private final Logger log = LoggerFactory.getLogger(SAQuoteResource.class);

    private static final String ENTITY_NAME = "sAQuote";

    private final SAQuoteService sAQuoteService;

    public SAQuoteResource(SAQuoteService sAQuoteService) {
        this.sAQuoteService = sAQuoteService;
    }

    /**
     * POST  /sa-quotes : Create a new sAQuote.
     *
     * @param sAQuote the sAQuote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAQuote, or with status 400 (Bad Request) if the sAQuote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-quotes")
    @Timed
    public ResponseEntity<SAQuoteSaveDTO> createSAQuote(@Valid @RequestBody SAQuote sAQuote) throws URISyntaxException {
        log.debug("REST request to save SAQuote : {}", sAQuote);
        SAQuoteSaveDTO result = sAQuoteService.save(sAQuote, true);
        if (result.getsAQuote().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/sa-quotes/" + result.getsAQuote().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getsAQuote().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /sa-quotes : Updates an existing sAQuote.
     *
     * @param sAQuote the sAQuote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAQuote,
     * or with status 400 (Bad Request) if the sAQuote is not valid,
     * or with status 500 (Internal Server Error) if the sAQuote couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sa-quotes")
    @Timed
    public ResponseEntity<SAQuoteSaveDTO> updateSAQuote(@Valid @RequestBody SAQuote sAQuote) throws URISyntaxException {
        log.debug("REST request to update SAQuote : {}", sAQuote);
        SAQuoteSaveDTO result = sAQuoteService.save(sAQuote, false);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getsAQuote().getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-quotes : get all the sAQuotes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAQuotes in body
     */
    @GetMapping("/sa-quotes")
    @Timed
    public ResponseEntity<List<SAQuote>> getAllSAQuotes(Pageable pageable) {
        log.debug("REST request to get a page of SAQuotes");
        Page<SAQuote> page = sAQuoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-quotes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-quotes/:id : get the "id" sAQuote.
     *
     * @param id the id of the sAQuote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAQuote, or with status 404 (Not Found)
     */
    @GetMapping("/sa-quotes/{id}")
    @Timed
    public ResponseEntity<SAQuote> getSAQuote(@PathVariable UUID id) {
        log.debug("REST request to get SAQuote : {}", id);
        Optional<SAQuote> sAQuote = sAQuoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAQuote);
    }

    @GetMapping("/sa-quotes/find-one")
    @Timed
    public ResponseEntity<SAQuoteViewDTO> findOneByID(@RequestParam UUID id) {
        log.debug("REST request to get SAQuote : {}", id);
        SAQuoteViewDTO sAQuote = sAQuoteService.findOneByID(id);
        return new ResponseEntity<>(sAQuote, HttpStatus.OK);
    }

    /**
     * DELETE  /sa-quotes/:id : delete the "id" sAQuote.
     *
     * @param id the id of the sAQuote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-quotes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAQuote(@PathVariable UUID id) {
        log.debug("REST request to delete SAQuote : {}", id);
        sAQuoteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /sa-quotes : get all the sAQuotes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAQuotes in body
     */
    @GetMapping("/sa-quotes/get-all-data")
    @Timed
    public ResponseEntity<List<SAQuoteDTO>> getAllData(Pageable pageable) {
        log.debug("REST request to get a page of SAQuotes");
        Page<SAQuoteDTO> page = sAQuoteService.findAllData(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-quotes/get-all-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @return
     * @author anmt
     */
    @GetMapping("/sa-quotes/search-all")
    @Timed
    public ResponseEntity<List<SAQuote>> findAll(Pageable pageable,
                                                 @RequestParam(required = false) String searchVoucher
    ) {
        log.debug("REST request to get a page of sa-quotes");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<SAQuote> page = sAQuoteService.findAll(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-quotes/search-all");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-quotes/: get the "id" sa-quotes.
     *
     * @param searchVoucher str sort of the mBTellerPaper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaper, or with status 404 (Not Found)
     * @author mran
     */
    @GetMapping("/sa-quotes/findByRowNum")
    @Timed
    public ResponseEntity<SAQuote> getSAQuoteByRowNum(@RequestParam(required = false) String searchVoucher,
                                                      @RequestParam(required = false) Number rowNum
    ) {
        log.debug("REST request to get a page of sa-quotes");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SAQuote sAQuote = sAQuoteService.findOneByRowNum(searchVoucher1, rowNum);
        return new ResponseEntity<>(sAQuote, HttpStatus.OK);
    }

    @GetMapping("/sa-quotes/getIndexRow")
    @Timed
    public ResponseEntity<List<Number>> getIndexRow(@RequestParam(required = false) UUID id,
                                                    @RequestParam(required = false) String searchVoucher
    ) {
        log.debug("REST request to get a page of sa-quotes");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Number> lstIndex = sAQuoteService.getIndexRow(id, searchVoucher1);
        return new ResponseEntity<>(lstIndex, HttpStatus.OK);
    }

    @GetMapping("/sa-quotes/ViewSAQuoteDTO")
    @Timed
    public ResponseEntity<List<ViewSAQuoteDTO>> getViewSAQuoteDTOPopup(Pageable pageable,
                                                                       @RequestParam(required = false) UUID accountingObjectID,
                                                                       @RequestParam(required = false) String fromDate,
                                                                       @RequestParam(required = false) String toDate,
                                                                       @RequestParam(required = false) String currencyID
    ) {
        log.debug("REST request to get a page of SAInvoicePopupDTO");
        Page<ViewSAQuoteDTO> page = sAQuoteService.getViewSAQuoteDTOPopup(pageable, accountingObjectID, fromDate, toDate, currencyID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoices/popup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/sa-quotes/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = sAQuoteService.exportPdf(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-quotes/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String searchVoucher) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] export = sAQuoteService.exportExcel(searchVoucher1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/sa-quotes/check-relate-voucher")
    @Timed
    public ResponseEntity<Boolean> checkRelateVoucher(@RequestParam(required = true) UUID id) {
        log.debug("REST request to checkRelateVoucher SAOrder ");
        Boolean check = sAQuoteService.checkRelateVoucher(id);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @PostMapping("/sa-quotes/delete-ref/{id}")
    @Timed
    public ResponseEntity<Boolean> deleteRefSAInvoiceAndRSInwardOutward(@PathVariable UUID id) {
        log.debug("REST request to sendMail EInvoice : {}", id);
        Boolean check = sAQuoteService.deleteRefSAInvoiceAndRSInwardOutward(id);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @PostMapping("/sa-quotes/delete-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedger(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = sAQuoteService.deleteList(uuids);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }
}
