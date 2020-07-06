package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.MutipleRecord;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.service.SAInvoiceService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnOutWardDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing SAInvoice.
 */
@RestController
@RequestMapping("/api")
public class SAInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(SAInvoiceResource.class);

    private static final String ENTITY_NAME = "sAInvoice";

    private final SAInvoiceService sAInvoiceService;

    public SAInvoiceResource(SAInvoiceService sAInvoiceService) {
        this.sAInvoiceService = sAInvoiceService;
    }

    /**
     * POST  /sa-invoices : Create a new sAInvoice.
     *
     * @param sAInvoice the sAInvoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAInvoice, or with status 400 (Bad Request) if the sAInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-invoices")
    @Timed
    public ResponseEntity<SaInvoiceDTO> createSAInvoice(@RequestBody SAInvoice sAInvoice) throws URISyntaxException, InvocationTargetException, IllegalAccessException {
        log.debug("REST request to save SAInvoice : {}", sAInvoice);
        if (sAInvoice.getId() != null) {
            throw new BadRequestAlertException("A new sAInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaInvoiceDTO result = new SaInvoiceDTO();
        result = sAInvoiceService.saveDTO(sAInvoice);
        if (result.getSaInvoice().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/sa-invoices/" + result.getSaInvoice().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getSaInvoice().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /sa-invoices : Updates an existing sAInvoice.
     *
     * @param sAInvoice the sAInvoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAInvoice,
     * or with status 400 (Bad Request) if the sAInvoice is not valid,
     * or with status 500 (Internal Server Error) if the sAInvoice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sa-invoices")
    @Timed
    public ResponseEntity<SaInvoiceDTO> updateSAInvoice(@RequestBody SAInvoice sAInvoice) throws InvocationTargetException, IllegalAccessException {
        log.debug("REST request to update SAInvoice : {}", sAInvoice);
        if (sAInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaInvoiceDTO result = new SaInvoiceDTO();
        result = sAInvoiceService.saveDTO(sAInvoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sAInvoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-invoices : get all the sAInvoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAInvoices in body
     */
    @GetMapping("/sa-invoices")
    @Timed
    public ResponseEntity<List<SAInvoice>> getAllSAInvoices(Pageable pageable) {
        log.debug("REST request to get a page of SAInvoices");
        Page<SAInvoice> page = sAInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-invoices/:id : get the "id" sAInvoice.
     *
     * @param id the id of the sAInvoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAInvoice, or with status 404 (Not Found)
     */
    @GetMapping("/sa-invoices/{id}")
    @Timed
    public ResponseEntity<SAInvoice> getSAInvoice(@PathVariable UUID id) {
        log.debug("REST request to get SAInvoice : {}", id);
        Optional<SAInvoice> sAInvoice = sAInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAInvoice);
    }

    /**
     * DELETE  /sa-invoices/:id : delete the "id" sAInvoice.
     *
     * @param id the id of the sAInvoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAInvoice(@PathVariable UUID id) {
        log.debug("REST request to delete SAInvoice : {}", id);
        sAInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/sa-invoice-objects-search")
    @Timed
    public ResponseEntity<List<SAInvoiceViewDTO>> searchSAInvoice(Pageable pageable,
                                                             @RequestParam(required = false) UUID accountingObjectID,
                                                             @RequestParam(required = false) String currencyID,
                                                             @RequestParam(required = false) String fromDate,
                                                             @RequestParam(required = false) String toDate,
                                                             @RequestParam(required = false) Boolean status,
                                                             @RequestParam(required = false) String keySearch,
                                                           @RequestParam(required = false) Integer typeId)
    {
        log.debug("REST request to get a page of SAInvoices");
        Page<SAInvoiceViewDTO> page = sAInvoiceService.searchSAInvoice(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, typeId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/index")
    @Timed
    public ResponseEntity<SAInvoice> findByRowNum(@RequestParam(required = false) UUID accountingObjectID,
                                                  @RequestParam(required = false) String currencyID,
                                                  @RequestParam(required = false) String fromDate,
                                                  @RequestParam(required = false) String toDate,
                                                  @RequestParam(required = false) Boolean status,
                                                  @RequestParam(required = false) String keySearch,
                                                  @RequestParam(required = false) Integer rowNum,
                                                  @RequestParam(required = false) Integer typeId
    ) {
        log.debug("REST request to get a page of findByRowNum");
        SAInvoice ppDiscountReturn = sAInvoiceService.findByRowNum(accountingObjectID, currencyID, fromDate,
            toDate, status, keySearch, rowNum, typeId);
        return new ResponseEntity<>(ppDiscountReturn, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/ref-voucher/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> refVouchersBySAInvoiceID(@PathVariable UUID id) {
        log.debug("REST request to get a page of refVouchers");
        List<RefVoucherDTO> refVoucherDTOList = sAInvoiceService.refVouchersBySAOrderID(id);
        return new ResponseEntity<>(refVoucherDTOList, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/ref-voucher-by-mcreceipt-id/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> refVouchersByMCReceiptID(@PathVariable UUID id) {
        log.debug("REST request to get a page of refVouchers");
        List<RefVoucherDTO> refVoucherDTOList = sAInvoiceService.refVouchersByMCReceiptID(id);
        return new ResponseEntity<>(refVoucherDTOList, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/ref-voucher-by-mb-deposit-id/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> refVouchersByMBDepositID(@PathVariable UUID id) {
        log.debug("REST request to get a page of refVouchers");
        List<RefVoucherDTO> refVoucherDTOList = sAInvoiceService.refVouchersByMBDepositID(id);
        return new ResponseEntity<>(refVoucherDTOList, HttpStatus.OK);
    }

    @PostMapping("/sa-invoices/detail")
    @Timed
    public ResponseEntity<List<SAInvoiceDetailPopupDTO>> getSaInvoiceDetail(@RequestBody List<SAInvoiceDetailPopupDTO> saInvoiceDTO) {
        log.debug("REST request to get a page of SAInvoices");
        List<SAInvoiceDetailPopupDTO> list = sAInvoiceService.getSaInvoiceDetail(saInvoiceDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * @Author hieugie
     *
     * Lấy dữ liệu cho popup chứng từ bàn hàng cho form xuất hóa đơn, hàng bán trả lại, hàng giảm giá
     *
     * @param pageable
     * @param accountingObjectID
     * @param fromDate
     * @param toDate
     * @param typeID 350 : xuất hóa đơn, 330 hàng bán trả lại
     * @return
     */
    @GetMapping("/sa-invoices/popup")
    @Timed
    public ResponseEntity<List<SAInvoicePopupDTO>> gSaInvoiceSaBillPopupDTOs(Pageable pageable,
                                                                     @RequestParam(required = false) UUID accountingObjectID,
                                                                     @RequestParam(required = false) String fromDate,
                                                                     @RequestParam(required = false) String toDate,
                                                                     @RequestParam(required = false) Integer typeID,
                                                                     @RequestParam(required = false) String currencyID,
                                                                     @RequestParam(required = false) UUID objectID,
                                                                     @RequestParam(required = false) Integer createForm) {
        log.debug("REST request to get a page of SAInvoicePopupDTO");
        Page<SAInvoicePopupDTO> page = sAInvoiceService.gSaInvoiceSaBillPopupDTOs(pageable, accountingObjectID, fromDate,
            toDate, typeID, currencyID, objectID, createForm);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoices/popup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/sa-invoices/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/sainvoice.xlsx");
        byte[] fileContent = Files.readAllBytes(reportFile.toPath());

        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-invoices/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(Pageable pageable, @RequestParam(required = false) UUID accountingObjectID,
                                              @RequestParam(required = false) String currencyID,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) Boolean status,
                                              @RequestParam(required = false) String keySearch,
                                              @RequestParam(required = false) Integer typeId) throws IOException {
        byte[] export = sAInvoiceService.exportExcel(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, typeId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-invoices/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(Pageable pageable, @RequestParam(required = false) UUID accountingObjectID,
                                            @RequestParam(required = false) String currencyID,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate,
                                            @RequestParam(required = false) Boolean status,
                                            @RequestParam(required = false) String keySearch,
                                            @RequestParam(required = false) Integer typeId) throws IOException {
        byte[] export = sAInvoiceService.exportPdf(pageable, accountingObjectID, currencyID, fromDate, toDate, status, keySearch, typeId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    /**
     * Kiểm tra xem hóa đơn có được tham chiếu không
     * @param sAInvoice
     * @return
     */
    @GetMapping("/sa-invoices/check-relate-voucher")
    @Timed
    public ResponseEntity<Integer> checkRelateVoucher(@RequestParam(required = false) UUID sAInvoice, @RequestParam(required = false) Boolean isCheckKPXK) {
        log.debug("REST request to get a page of sAInvoices");
        Integer check = sAInvoiceService.checkRelateVoucher(sAInvoice, isCheckKPXK);
        return new ResponseEntity<Integer>(check, HttpStatus.OK);
    }

    /***
     *
     * @param accountingObject
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping("/sa-invoice/search-all-dto")
    @Timed
    public ResponseEntity<Page<SAInvoiceDTO>> searchAllOrderDTO(Pageable pageable,
                                                                @RequestParam(required = false) UUID accountingObject,
                                                                @RequestParam(required = false) String fromDate,
                                                                @RequestParam(required = false) String toDate) {
        log.debug("REST request to getAll SAInvoiceOutWardDTO");
        Page<SAInvoiceDTO> page = sAInvoiceService.findAllSAInvoiceDTO(pageable, accountingObject, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/get-sainvoiceid")
    @Timed
    public ResponseEntity<List<UUID>> getSAInvoiceBySABillID(@RequestParam(required = false) UUID saBillID) {
        List<UUID> listSAInvoiceID = sAInvoiceService.getSAInvoiceBySABillID(saBillID);
        return new ResponseEntity<>(listSAInvoiceID, HttpStatus.OK);
    }

    @PostMapping("/sa-invoices/multi-delete-sa-invoices")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteSAInvoice(@Valid @RequestBody List<SAInvoice> saInvoices) {
        log.debug("REST request to closeBook : {}", saInvoices);
        HandlingResultDTO responeCloseBookDTO = sAInvoiceService.multiDelete(saInvoices);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @PostMapping("/sa-invoices/multi-unrecord-sa-invoices")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnrecordSAInvoice(@Valid @RequestBody List<SAInvoice> saInvoices) {
        log.debug("REST request to closeBook : {}", saInvoices);
        HandlingResultDTO responeCloseBookDTO = sAInvoiceService.multiUnrecord(saInvoices);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

    @GetMapping("/sa-invoices/isBanHangChuaThuTien")
    @Timed
    public ResponseEntity<Boolean> isBanHangChuaThuTien(@RequestParam(required = false) UUID refID) {
        Boolean result = sAInvoiceService.isBanHangChuaThuTien(refID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
