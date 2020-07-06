package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.MCAudit;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.service.SaBillService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.FileSizeLimitExceededException;
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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing SaBill.
 */
@RestController
@RequestMapping("/api")
public class SaBillResource {

    private final Logger log = LoggerFactory.getLogger(SaBillResource.class);

    private static final String ENTITY_NAME = "saBill";

    private final SaBillService saBillService;

    public SaBillResource(SaBillService saBillService) {
        this.saBillService = saBillService;
    }

    /**
     * POST  /sa-bills : Create a new saBill.
     *
     * @param saBill the saBill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saBill, or with status 400 (Bad Request) if the saBill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-bills")
    @Timed
    public ResponseEntity<SaBillSaveDTO> createSaBill(@RequestBody SaBillSaveDTO saBill) throws URISyntaxException {
        log.debug("REST request to save SaBill : {}", saBill);
        SaBillSaveDTO result = saBillService.save(saBill);
        return ResponseEntity.created(new URI("/api/sa-bills/" + result.getSaBill().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getSaBill().getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-bills : get all the saBills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saBills in body
     */
    @GetMapping("/sa-bills")
    @Timed
    public ResponseEntity<List<SaBillDTO>> getAllSaBillDTOs(Pageable pageable,
                                                            @RequestParam(required = false) UUID accountingObjectID,
                                                            @RequestParam(required = false) String invoiceTemplate,
                                                            @RequestParam(required = false) String fromInvoiceDate,
                                                            @RequestParam(required = false) String toInvoiceDate,
                                                            @RequestParam(required = false) String invoiceSeries,
                                                            @RequestParam(required = false) String invoiceNo,
                                                            @RequestParam(required = false) String freeText) {
        log.debug("REST request to get a page of SaBills");
        Page<SaBillDTO> page = saBillService.getAllSaBillDTOs(pageable, accountingObjectID, invoiceTemplate,
            fromInvoiceDate, toInvoiceDate, invoiceSeries, invoiceNo, freeText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/sa-bills/index")
    @Timed
    public ResponseEntity<SaBillViewDTO> getNextSaBillDTOs(Pageable pageable,
                                                           @RequestParam(required = false) UUID accountingObjectID,
                                                           @RequestParam(required = false) String invoiceTemplate,
                                                           @RequestParam(required = false) String fromInvoiceDate,
                                                           @RequestParam(required = false) String toInvoiceDate,
                                                           @RequestParam(required = false) String invoiceSeries,
                                                           @RequestParam(required = false) String invoiceNo,
                                                           @RequestParam(required = false) String freeText,
                                                           @RequestParam(required = false) UUID id,
                                                           @RequestParam(required = false) Integer rowIndex) {
        log.debug("REST request to get a page of SaBills");
        SaBillViewDTO saBill = saBillService.getNextSaBillDTOs(pageable, accountingObjectID, invoiceTemplate,
            fromInvoiceDate, toInvoiceDate, invoiceSeries, invoiceNo, freeText, rowIndex, id);
        return new ResponseEntity<>(saBill, HttpStatus.OK);
    }

    /**
     * GET  /sa-bills/:id : get the "id" saBill.
     *
     * @param id the id of the saBill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saBill, or with status 404 (Not Found)
     */
    @GetMapping("/sa-bills/{id}")
    @Timed
    public ResponseEntity<SaBillViewDTO> getSaBill(@PathVariable UUID id) {
        log.debug("REST request to get SaBill : {}", id);
        SaBillViewDTO saBill = saBillService.findOne(id);
        return new ResponseEntity<>(saBill, HttpStatus.OK);
    }

    @GetMapping("/sa-bills/find-one")
    @Timed
    public ResponseEntity<SaBillViewDTO> findOneByID(@RequestParam UUID id) {
        log.debug("REST request to get SaBill : {}", id);
        SaBillViewDTO saBill = saBillService.findOneByID(id);
        return new ResponseEntity<>(saBill, HttpStatus.OK);
    }
    /**
     * DELETE  /sa-bills/:id : delete the "id" saBill.
     *
     * @param id the id of the saBill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-bills/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaBill(@PathVariable UUID id) {
        log.debug("REST request to delete SaBill : {}", id);
        saBillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/sa-bills/search-data")
    @Timed
    public ResponseEntity<SaBillSearchDTO> getAllSearchData() {
        log.debug("REST request to get a page of SaBills");
        SaBillSearchDTO dto = saBillService.getAllSearchData();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/sa-bills/search-all")
    @Timed
    public ResponseEntity<List<SaBill>> getAll() {
        log.debug("REST request to get a page of SaBills");
        Page<SaBill> page = saBillService.getAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * lấy ra danh sách hóa đơn đã tạo không qua các bảng PPDisCountReturnDetail/SAInvoiceDetail/SAReturnDetail
     * @param pageable
     * @return
     */
    @GetMapping("/sa-bills/search-sabill-created")
    @Timed
    public ResponseEntity<List<SaBillCreatedDTO>> saBillCreated(Pageable pageable,
                                                                @RequestParam(required = false) UUID accountingObjectID,
                                                                @RequestParam(required = false) String formDate,
                                                                @RequestParam(required = false) String toDate,
                                                                @RequestParam(required = false) String currencyCode
                                                                ) {
        log.debug("REST request to get a page of SaBills");
        Page<SaBillCreatedDTO> page = saBillService.saBillCreated(pageable, accountingObjectID, formDate, toDate, currencyCode);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-sabill-created");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * lấy ra danh sách hóa đơn detail theo mảng id truyền lên
     * @return
     */
    @GetMapping("/sa-bills//search-sabill-created/detail")
    @Timed
    public ResponseEntity<List<SaBillCreatedDetailDTO>> saBillCreatedDetail(@RequestParam List<UUID> sabillIdList) {
        log.debug("REST request to get a page of SaBills");
        List<SaBillCreatedDetailDTO> page = saBillService.saBillCreatedDetail(sabillIdList);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-sabill-created");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @Author hieugie
     *
     * @param file
     * @return
     * @throws IOException
     * @throws FileSizeLimitExceededException
     * @throws URISyntaxException
     */
    @PostMapping(value = "/sa-bills/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> upload(@RequestParam(required = false) MultipartFile file)
        throws IOException, FileSizeLimitExceededException, URISyntaxException {
        UploadInvoiceDTO uploadInvoiceDTO = saBillService.upload(file);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        headers.add("isError", uploadInvoiceDTO.getError() ? "1" : "0");
        headers.add("message", uploadInvoiceDTO.getMessage());
        return new ResponseEntity<>(uploadInvoiceDTO.getExcelFile(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/sa-bills/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/invoice.xlsx");
        byte[] fileContent = Files.readAllBytes(reportFile.toPath());

        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-bills/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) UUID accountingObjectID,
                                              @RequestParam(required = false) String invoiceTemplate,
                                              @RequestParam(required = false) String fromInvoiceDate,
                                              @RequestParam(required = false) String toInvoiceDate,
                                              @RequestParam(required = false) String invoiceSeries,
                                              @RequestParam(required = false) String invoiceNo,
                                              @RequestParam(required = false) String freeText) throws IOException {
        byte[] export = saBillService.exportExcel(accountingObjectID, invoiceTemplate,
            fromInvoiceDate, toInvoiceDate, invoiceSeries, invoiceNo, freeText);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    @GetMapping(value = "/sa-bills/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) UUID accountingObjectID,
                                            @RequestParam(required = false) String invoiceTemplate,
                                            @RequestParam(required = false) String fromInvoiceDate,
                                            @RequestParam(required = false) String toInvoiceDate,
                                            @RequestParam(required = false) String invoiceSeries,
                                            @RequestParam(required = false) String invoiceNo,
                                            @RequestParam(required = false) String freeText) throws IOException {
        byte[] export = saBillService.exportPdf(accountingObjectID, invoiceTemplate,
            fromInvoiceDate, toInvoiceDate, invoiceSeries, invoiceNo, freeText);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);

    }

    /**
     * Kiểm tra xem hóa đơn có được tham chiếu không
     * @param sABillID
     * @return
     */
    @GetMapping("/sa-bills/check-relate-voucher")
    @Timed
    public ResponseEntity<Boolean> checkRelateVoucher(@RequestParam(required = false) UUID sABillID) {
        log.debug("REST request to get a page of SaBills");
        Boolean check = saBillService.checkRelateVoucher(sABillID);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @PostMapping("/sa-bills/multi-delete-sa-bills")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteSABill(@Valid @RequestBody List<SaBill> saBills) {
        log.debug("REST request to closeBook : {}", saBills);
        HandlingResultDTO responeCloseBookDTO = saBillService.multiDelete(saBills);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
