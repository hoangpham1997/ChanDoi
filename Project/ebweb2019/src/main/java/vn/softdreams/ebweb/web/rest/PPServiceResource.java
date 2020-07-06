package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.PPServiceService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PPServiceResource {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumResource.class);

    private static final String ENTITY_NAME = "ppService";

    private final PPServiceService ppServiceService;

    public PPServiceResource(PPServiceService ppServiceService) {
        this.ppServiceService = ppServiceService;
    }

    /***
     * @author jsp
     * @param pageable
     * @param noBookType 0: Sổ tài chính 1: Sổ quản trị
     * @return
     */
    @GetMapping("ppService/find-all")
    @Timed
    public ResponseEntity<Page<PPServiceDTO>> findAllPPService(Pageable pageable,
                                                               @RequestParam(required = false) Integer noBookType,
                                                               @RequestParam(required = false) Integer receiptType,
                                                               @RequestParam(required = false) String toDate,
                                                               @RequestParam(required = false) String fromDate,
                                                               @RequestParam(required = false) Integer hasRecord,
                                                               @RequestParam(required = false) String currencyID,
                                                               @RequestParam(required = false) UUID accountingObjectID,
                                                               @RequestParam(required = false) String freeSearch,
                                                               @RequestParam(required = false) UUID currentPPService) {
        log.debug("REST request to get a page of PPServiceDTO");
        Page<PPServiceDTO> page = ppServiceService.findAllPPService(pageable, receiptType, toDate, fromDate, hasRecord,
                currencyID, accountingObjectID, noBookType, freeSearch, currentPPService);
        BigDecimal totalResultAmountOriginal = ppServiceService.countTotalResultAmountOriginal(receiptType, toDate, fromDate, hasRecord,
                currencyID, accountingObjectID, noBookType, freeSearch);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        headers.add("total-result-amount-original", totalResultAmountOriginal != null ? totalResultAmountOriginal.toString() : "0");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("ppService/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(
                                                        @RequestParam(required = false) Integer noBookType,
                                                        @RequestParam(required = false) Integer receiptType,
                                                        @RequestParam(required = false) String toDate,
                                                        @RequestParam(required = false) String fromDate,
                                                        @RequestParam(required = false) Integer hasRecord,
                                                        @RequestParam(required = false) String currencyID,
                                                        @RequestParam(required = false) UUID accountingObjectID,
                                                        @RequestParam(required = false) String freeSearch) {
        byte[] export = ppServiceService.exportPdf(receiptType, toDate, fromDate, hasRecord,
                currencyID, accountingObjectID, noBookType, freeSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("ppService/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(
                                              @RequestParam(required = false) Integer noBookType,
                                              @RequestParam(required = false) Integer receiptType,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) Integer hasRecord,
                                              @RequestParam(required = false) String currencyID,
                                              @RequestParam(required = false) UUID accountingObjectID,
                                              @RequestParam(required = false) String freeSearch) {
        log.debug("REST request to get a page of PPServiceDTO");
        byte[] export = ppServiceService.exportExcel(receiptType, toDate, fromDate, hasRecord,
                currencyID, accountingObjectID, noBookType, freeSearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("ppService/had-reference")
    @Timed
    public ResponseEntity<UpdateDataDTO> checkHasReference(@RequestParam UUID ppServiceId) {
        log.debug("REST request to get a PPServiceDTO by ppServiceId");
        UpdateDataDTO updateDataDTO = ppServiceService.checkHadReference(ppServiceId);
        HttpHeaders headers = HeaderUtil.updateData(updateDataDTO);
        return new ResponseEntity<>(updateDataDTO, headers, HttpStatus.OK);
    }

    @GetMapping("ppService/find-by-location")
    @Timed
    public ResponseEntity<UpdateDataDTO> findPPServiceDTOById(@RequestParam(required = false) UUID ppServiceId,
                                                             @RequestParam(required = false) Integer action,
                                                             @RequestParam(required = false) Integer noBookType,
                                                             @RequestParam(required = false) Integer receiptType,
                                                             @RequestParam(required = false) String toDate,
                                                             @RequestParam(required = false) String fromDate,
                                                             @RequestParam(required = false) Integer hasRecord,
                                                             @RequestParam(required = false) String currencyID,
                                                             @RequestParam(required = false) UUID accountingObjectID,
                                                             @RequestParam(required = false) String freeSearch) {
        log.debug("REST request to get a PPServiceDTO by ppServiceId");
        UpdateDataDTO updateDataDTO = ppServiceService.getPPServiceDTOByLocation(ppServiceId, action, receiptType, toDate, fromDate, hasRecord,
                currencyID, accountingObjectID, noBookType, freeSearch);
        HttpHeaders headers = HeaderUtil.updateData(updateDataDTO);
        return new ResponseEntity<>(updateDataDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("ppService/delete-by-id")
    @Timed
    public ResponseEntity<UpdateDataDTO> deletePPServiceDTOById(@RequestParam UUID id) {
        log.debug("REST request to get a PPServiceDTO by ppServiceId");
        UpdateDataDTO updateDataDTO = ppServiceService.deletePPServiceById(id);
        HttpHeaders headers = HeaderUtil.updateData(updateDataDTO);
        return new ResponseEntity<>(updateDataDTO, headers, HttpStatus.OK);
    }
    @PostMapping("ppService/delete-in-id")
    @Timed
    public ResponseEntity<UpdateDataDTO> deletePPServiceDTOInId(@RequestBody List<UUID> ids) {
        log.debug("REST request to get a PPServiceDTO in ppServiceId");
        UpdateDataDTO updateDataDTO = ppServiceService.deletePPServiceInId(ids);
        HttpHeaders headers = HeaderUtil.updateData(updateDataDTO);
        return new ResponseEntity<>(updateDataDTO, headers, HttpStatus.OK);
    }

    @PostMapping("ppService")
    @Timed
    public ResponseEntity<UpdateDataDTO> createPPService(@RequestBody PPServiceDTO ppServiceDTO) throws URISyntaxException {
        log.debug("REST request to save PPService : {}", ppServiceDTO);
        UpdateDataDTO updateDataDTO = ppServiceService.savePPService(ppServiceDTO);
        HttpHeaders headers = HeaderUtil.updateData(updateDataDTO);
        return new ResponseEntity<>(updateDataDTO, headers, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("ppService/find-all-receive-bill-pPService")
    @Timed
    public ResponseEntity<List<ReceiveBillSearchDTO>> findAllReceiveBillSearchDTO(Pageable pageable, String searchVoucher) {
        log.debug("REST request to get a page of PPInvoice");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucher searchVoucher1 = null;
        try {
            searchVoucher1 = objectMapper.readValue(searchVoucher, SearchVoucher.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<ReceiveBillSearchDTO> page = ppServiceService.findAllReceiveBillSearchDTO(pageable, searchVoucher1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/ppService/update-receive-bill")
    @Timed
    public ResponseEntity<Void> updatePPService(@Valid @RequestBody List<UUID> listPPInvoice) {
        ppServiceService.saveReceiveBill(listPPInvoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ppservice/search-cost-vouchers")
    @Timed
    public ResponseEntity<List<PPServiceCostVoucherDTO>> findCostVouchers(Pageable pageable,
                                                                          @RequestParam(required = false) UUID accountingObject,
                                                                          @RequestParam(required = false) String fromDate,
                                                                          @RequestParam(required = false) String toDate,
                                                                          @RequestParam(required = false) UUID ppInvoiceId,
                                                                          @RequestParam(required = false) Boolean isHaiQuan) {
        log.debug("REST request to getAll PP Order DTO");
        Page<PPServiceCostVoucherDTO> page = ppServiceService.findCostVouchers(pageable, accountingObject, fromDate, toDate, ppInvoiceId, isHaiQuan);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/ppservice/search-cost-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
