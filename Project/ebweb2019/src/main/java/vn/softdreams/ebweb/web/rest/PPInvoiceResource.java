package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.PPInvoice;
import vn.softdreams.ebweb.service.PPInvoiceService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
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
 * REST controller for managing PPInvoice.
 */
@RestController
@RequestMapping("/api")
public class PPInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(PPInvoiceResource.class);

    private static final String ENTITY_NAME = "pPInvoice";

    private final PPInvoiceService pPInvoiceService;

    public PPInvoiceResource(PPInvoiceService pPInvoiceService) {
        this.pPInvoiceService = pPInvoiceService;
    }

    /**
     * POST  /pp-invoices : Create a new pPInvoice.
     *
     * @param pPInvoice the pPInvoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pPInvoice, or with status 400 (Bad Request) if the pPInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pp-invoices")
    @Timed
    public ResponseEntity<PPInvoice> createPPInvoice(@Valid @RequestBody PPInvoice pPInvoice) throws URISyntaxException {
        log.debug("REST request to save PPInvoice : {}", pPInvoice);
        if (pPInvoice.getId() != null) {
            throw new BadRequestAlertException("A new pPInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPInvoice result = pPInvoiceService.save(pPInvoice);
        return ResponseEntity.created(new URI("/api/pp-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pp-invoices : Updates an existing pPInvoice.
     *
     * @param pPInvoice the pPInvoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoice,
     * or with status 400 (Bad Request) if the pPInvoice is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoices")
    @Timed
    public ResponseEntity<PPInvoice> updatePPInvoice(@Valid @RequestBody PPInvoice pPInvoice) throws URISyntaxException {
        log.debug("REST request to update PPInvoice : {}", pPInvoice);
        if (pPInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPInvoice result = pPInvoiceService.save(pPInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pPInvoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pp-invoices : get all the pPInvoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pPInvoices in body
     */
    @GetMapping("/pp-invoices")
    @Timed
    public ResponseEntity<List<PPInvoice>> getAllPPInvoices(Pageable pageable) {
        log.debug("REST request to get a page of PPInvoices");
        Page<PPInvoice> page = pPInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pp-invoices/:id : get the "id" pPInvoice.
     *
     * @param id the id of the pPInvoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPInvoice, or with status 404 (Not Found)
     */
    @GetMapping("/pp-invoices/{id}")
    @Timed
    public ResponseEntity<PPInvoice> getPPInvoice(@PathVariable UUID id) {
        log.debug("REST request to get PPInvoice : {}", id);
        Optional<PPInvoice> pPInvoice = pPInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPInvoice);
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("pp-invoices/find-all-receive-bill-ppInvoice")
    @Timed
    public ResponseEntity<List<ReceiveBillSearchDTO>> findAllPPInvoiceReceiveBill(Pageable pageable, String searchVoucher, String formality) {
        log.debug("REST request to get a page of ReceiveBillSearchDTO");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<ReceiveBillSearchDTO> page = pPInvoiceService.findAllPPInvoiceReceiveBill(pageable, searchVoucher1, formality);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/find-all-receive-bill-ppInvoice");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("pp-invoices/find-all-receive-bill")
    @Timed
    public ResponseEntity<List<ReceiveBillSearchDTO>> findAllReceiveBill(Pageable pageable, String searchVoucher) {
        log.debug("REST request to get a page of ReceiveBillSearchDTO");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<ReceiveBillSearchDTO> page = pPInvoiceService.findAllReceiveBill(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/find-all-receive-bill-ppInvoice");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /pp-invoices/:id : delete the "id" pPInvoice.
     *
     * @param id the id of the pPInvoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pp-invoices/{id}")
    @Timed
    public ResponseEntity<Void> deletePPInvoice(@PathVariable UUID id) {
        log.debug("REST request to delete PPInvoice : {}", id);
        pPInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * PUT  /pp-invoices : Updates an existing pPInvoice.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoice,
     * or with status 400 (Bad Request) if the pPInvoice is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoices/update-receive-bill")
    @Timed
    public ResponseEntity<Void> updatePPInvoice(@Valid @RequestBody List<UUID> listPPInvoice) {
        pPInvoiceService.saveReceiveBill(listPPInvoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pp-invoices/rsi-save")
    @Timed
    public ResponseEntity<UpdateDataDTO> savePPInvoiceRSI(@Valid @RequestBody PPInvoiceDTO pPInvoice) throws URISyntaxException {
        UpdateDataDTO result = pPInvoiceService.savePPInvoice(pPInvoice, true);
        return ResponseEntity.created(new URI("/api/pp-invoices/rsi-save"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.toString()))
            .body(result);
    }

    @PostMapping("/pp-invoices/save")
    @Timed
    public ResponseEntity<UpdateDataDTO> savePPInvoice(@Valid @RequestBody PPInvoiceDTO pPInvoice) throws URISyntaxException {
        UpdateDataDTO result = pPInvoiceService.savePPInvoice(pPInvoice, false);
        return ResponseEntity.created(new URI("/api/pp-invoices/save"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.toString()))
            .body(result);
    }

    /**
     * @param pageable
     * @param accountingObject
     * @param currencyId
     * @param fromDate
     * @param toDate
     * @param status
     * @param keySearch
     * @param employee
     * @return
     * @author congnd
     */
    @GetMapping("/pp-invoices/search")
    @Timed
    public ResponseEntity<List<PPInvoiceSearchDTO>> searchPPInvoice(Pageable pageable,
                                                                    @RequestParam(required = false) UUID accountingObject,
                                                                    @RequestParam(required = false) String currencyId,
                                                                    @RequestParam(required = false) String fromDate,
                                                                    @RequestParam(required = false) String toDate,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) String keySearch,
                                                                    @RequestParam(required = false) UUID employee,
                                                                    @RequestParam Boolean isRSI) {
        log.debug("REST request to get a page of PPDiscountReturns");
        Page<PPInvoiceSearchDTO> page = pPInvoiceService.searchPPInvoice(pageable, accountingObject, currencyId, fromDate, toDate, status, keySearch, employee, isRSI);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoices/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * lấy chi tiết màn sửa
     *
     * @param id
     * @return
     * @author congnd
     */
    @GetMapping("/pp-invoices/get-by-id")
    @Timed
    public ResponseEntity<PPInvoiceDTO> getPPInvoiceById(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        PPInvoiceDTO ppInvoiceDTO = pPInvoiceService.getPPInvoiceById(id);
        return new ResponseEntity<>(ppInvoiceDTO, HttpStatus.OK);
    }

    @GetMapping("/pp-invoices/get-pp-invoice-by-id")
    @Timed
    public ResponseEntity<PPInvoiceDTO> getPPInvoiceNotDetailById(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        PPInvoiceDTO ppInvoiceDTO = pPInvoiceService.getPPInvoiceNotDetailById(id);
        return new ResponseEntity<>(ppInvoiceDTO, HttpStatus.OK);
    }

    /**
     * lấy chi tiết ppinvoiceDetail màn danh sách
     *
     * @param id
     * @return
     * @author congnd
     */
    @GetMapping("/pp-invoices/get-detail-by-ppinvoice-id")
    @Timed
    public ResponseEntity<List<PPInvoiceDetailDTO>> getPPInvoiceDetailByIdPPInvoice(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        List<PPInvoiceDetailDTO> ppInvoiceDetailDTOS = pPInvoiceService.getPPInvoiceDetailByIdPPInvoice(id);
        return new ResponseEntity<>(ppInvoiceDetailDTOS, HttpStatus.OK);
    }

    /**
     * lấy chi tiết ppinvoiceDetail màn danh sách dùng bên màn phiếu chi
     * @Author Hautv
     * @param paymentVoucherID
     * @return
     * @author congnd
     */
    @GetMapping("/pp-invoices/get-detail-by-paymentVoucher-id")
    @Timed
    public ResponseEntity<List<PPInvoiceDetailDTO>> getPPInvoiceDetailByPaymentVoucherID(@RequestParam UUID paymentVoucherID) {
        log.debug("REST request to get a page of PPDiscountReturns");
        List<PPInvoiceDetailDTO> ppInvoiceDetailDTOS = pPInvoiceService.getPPInvoiceDetailByPaymentVoucherID(paymentVoucherID);
        return new ResponseEntity<>(ppInvoiceDetailDTOS, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param pageable
     * @param accountingObject
     * @param status
     * @param currency
     * @param employee
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @param rowNum
     * @param isRSI
     * @return
     */
    @GetMapping("/pp-invoices/index")
    @Timed
    public ResponseEntity<PPInvoiceDTO> findByRowNum(Pageable pageable,
                                                     @RequestParam(required = false) UUID accountingObject,
                                                     @RequestParam(required = false) Integer status,
                                                     @RequestParam(required = false) String currency,
                                                     @RequestParam(required = false) UUID employee,
                                                     @RequestParam(required = false) String fromDate,
                                                     @RequestParam(required = false) String toDate,
                                                     @RequestParam(required = false) String searchValue,
                                                     @RequestParam(required = false) Integer rowNum,
                                                     @RequestParam Boolean isRSI
    ) {
        log.debug("REST request to get a page of findByRowNum");
        PPInvoiceDTO ppInvoice = pPInvoiceService.findIdByRowNum(pageable, accountingObject, status, currency, employee, fromDate, toDate, searchValue, rowNum, isRSI);
        return new ResponseEntity<>(ppInvoice, HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     * @author congnd
     */
    @GetMapping("/pp-invoices/delete-by-id")
    @Timed
    public ResponseEntity<ResultDTO> deleteById(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = pPInvoiceService.deleteById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     * GET  /pp-invoices/:id : get the "id" pPInvoice.
     *
     * @param id the id of the pPInvoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPInvoice, or with status 404 (Not Found)
     */
    @GetMapping("/pp-invoices/findPPInvoiceByPaymentVoucherId")
    @Timed
    public ResponseEntity<PPInvoice> findPPInvoiceByPaymentVoucherId(@RequestParam UUID id) {
        log.debug("REST request to get a page of PPDiscountReturns");
        PPInvoice ppInvoice = pPInvoiceService.getPPInvoiceByPaymentVoucherId(id);
        return new ResponseEntity<>(ppInvoice, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param accountingObject
     * @param status
     * @param currency
     * @param employee
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @param isRSI
     * @return
     */
    @GetMapping("/pp-invoices/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) UUID accountingObject,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) String currency,
                                            @RequestParam(required = false) UUID employee,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) String searchValue,
                                            @RequestParam Boolean isRSI
    ) {
        byte[] export = pPInvoiceService.exportPdf(accountingObject, status, currency, employee, fromDate, toDate, searchValue, isRSI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param accountingObject
     * @param status
     * @param currency
     * @param employee
     * @param fromDate
     * @param toDate
     * @param searchValue
     * @param isRSI
     * @return
     */
    @GetMapping("/pp-invoices/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) UUID accountingObject,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) String currency,
                                              @RequestParam(required = false) UUID employee,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String searchValue,
                                              @RequestParam Boolean isRSI
    ) {
        byte[] export = pPInvoiceService.exportExcel(accountingObject, status, currency, employee, fromDate, toDate, searchValue, isRSI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/pp-invoices/ViewPPInvoiceDTO")
    @Timed
    public ResponseEntity<List<ViewPPInvoiceDTO>> getViewRSOutwardDTOPopup(Pageable pageable,
                                                                           @RequestParam(required = false) UUID accountingObjectID,
                                                                           @RequestParam(required = false) String fromDate,
                                                                           @RequestParam(required = false) String toDate,
                                                                           @RequestParam(required = false) String currencyID
    ) {
        log.debug("REST request to get a page of ViewPPInvoiceDTO");
        Page<ViewPPInvoiceDTO> page = pPInvoiceService.getViewPPInvoiceDTOPopup(pageable, accountingObjectID, fromDate, toDate, currencyID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoices/popup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * check chứng từ có được tham chiếu bởi chứng từ khác hay không,git st trước khi bỏ ghi sổ
     * @author congnd
     * @param id
     * @return
     */
    @GetMapping("/pp-invoice/check-un-record")
    @Timed
    public ResponseEntity<ResultDTO> checkUnRecord(@RequestParam UUID id)
    {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = pPInvoiceService.checkUnRecord(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     * check chứng từ mua hàng qua kho hay khong qua kho
     * @author congnd
     * @param id
     * @return
     */
    @GetMapping("/pp-invoice/check-via-stock")
    @Timed
    public ResponseEntity<ResultDTO> checkViaStock(@RequestParam UUID id)
    {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = pPInvoiceService.checkViaStock(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     * @author congnd
     * đếm số liên kết đến đơn mua hàng từ các màn khác
     */
    @GetMapping("/pp-invoice/check-play-vendor")
    @Timed
    public ResponseEntity<ResultDTO> checkPayVendor(@RequestParam UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO resultDTO = pPInvoiceService.checkReferencesCount(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param ppInvoiceDeleteListDTO
     * @return
     */
    @PostMapping("/pp-invoices/multi-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteSAReturn(@RequestBody PPInvoiceDeleteListDTO ppInvoiceDeleteListDTO) {
        log.debug("REST request to closeBook : {}", ppInvoiceDeleteListDTO);
        HandlingResultDTO responeCloseBookDTO = pPInvoiceService.multiDelete(ppInvoiceDeleteListDTO.getPpInvoices(), Boolean.TRUE.equals(ppInvoiceDeleteListDTO.getKho()));
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param ppInvoiceDeleteListDTO
     * @return
     */
    @PostMapping("/pp-invoices/multi-unrecord")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnrecordSAReturn(@RequestBody PPInvoiceDeleteListDTO ppInvoiceDeleteListDTO) {
        log.debug("REST request to closeBook : {}", ppInvoiceDeleteListDTO);
        HandlingResultDTO responeCloseBookDTO = pPInvoiceService.multiUnRecord(ppInvoiceDeleteListDTO.getPpInvoices(), Boolean.TRUE.equals(ppInvoiceDeleteListDTO.getKho()));
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
