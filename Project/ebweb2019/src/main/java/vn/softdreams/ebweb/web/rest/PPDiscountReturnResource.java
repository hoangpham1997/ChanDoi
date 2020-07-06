package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.PPDiscountReturn;
import vn.softdreams.ebweb.domain.SaReturn;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.PPDiscountReturnService;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PPDiscountReturn.
 */
@RestController
@RequestMapping("/api")
public class PPDiscountReturnResource {

    private final Logger log = LoggerFactory.getLogger(PPDiscountReturnResource.class);

    private static final String ENTITY_NAME = "pPDiscountReturn";

    private final PPDiscountReturnService pPDiscountReturnService;

    @Autowired
    UtilsRepository utilsRepository;

    public PPDiscountReturnResource(PPDiscountReturnService pPDiscountReturnService, UtilsService utilsService) {
        this.pPDiscountReturnService = pPDiscountReturnService;
    }

    /**
     * POST  /pp-discount-returns : Create a new pPDiscountReturn.
     * @return the ResponseEntity with status 201 (Created) and with body the new pPDiscountReturn, or with status 400 (Bad Request) if the pPDiscountReturn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pp-discount-returns")
    @Timed
    public ResponseEntity<DiscountAndBillAndRSIDTO> createPPDiscountReturn(@RequestBody DiscountAndBillAndRSIDTO discountAndBillAndRSIDTO) throws URISyntaxException {
        log.debug("REST request to save PPDiscountReturn : {}", discountAndBillAndRSIDTO);
        if (discountAndBillAndRSIDTO.getPpDiscountReturn().getId() != null) {
            throw new BadRequestAlertException("A new pPDiscountReturn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscountAndBillAndRSIDTO result = null;
//        try {

        result = pPDiscountReturnService.save(discountAndBillAndRSIDTO);
//            utilsRepository.updateGencode(result.getNoMBook(), 22, result.getTypeLedger() == null ? 2 : result.getTypeLedger());
//            utilsRepository.updateGencode(result.getNoMBook(), 41, result.getTypeLedger() == null ? 2 : result.getTypeLedger());
//            utilsRepository.updateGencode(result.getNoFBook(),result.getNoMBook(), 22, result.getTypeLedger() == null ? 2 : result.getTypeLedger());
//        } catch (Exception ex){
//        utilsRepository.updateGencode(result.getNoFBook(),result.getNoMBook(), 22, result.getTypeLedger() == null ? 2 : result.getTypeLedger());
//        utilsRepository.updateGencode(result.getNoFBook(),result.getNoMBook(), 41, result.getTypeLedger() == null ? 2 : result.getTypeLedger());//        } catch (Exception ex){
//            System.out.println("lỗi "+ ex);
//        }
        return ResponseEntity.created(new URI("/api/pp-discount-returns/" + result.getPpDiscountReturn().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getPpDiscountReturn().getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pp-discount-returns : Updates an existing pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPDiscountReturn,
     * or with status 400 (Bad Request) if the pPDiscountReturn is not valid,
     * or with status 500 (Internal Server Error) if the pPDiscountReturn couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-discount-returns")
    @Timed
    public ResponseEntity<DiscountAndBillAndRSIDTO> updatePPDiscountReturn(@Valid @RequestBody DiscountAndBillAndRSIDTO discountAndBillAndRSIDTO) throws URISyntaxException {
        log.debug("REST request to update PPDiscountReturn : {}", discountAndBillAndRSIDTO);
        if (discountAndBillAndRSIDTO.getPpDiscountReturn().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscountAndBillAndRSIDTO result = pPDiscountReturnService.save(discountAndBillAndRSIDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discountAndBillAndRSIDTO.getPpDiscountReturn().getId().toString()))
            .body(result);
    }

    /**
     * GET  /pp-discount-returns : get all the pPDiscountReturns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pPDiscountReturns in body
     */
    @GetMapping("/pp-discount-returns")
    @Timed
    public ResponseEntity<List<PPDiscountReturn>> getAllPPDiscountReturns(Pageable pageable) {
        log.debug("REST request to get a page of PPDiscountReturns");
        Page<PPDiscountReturn> page = pPDiscountReturnService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-discount-returns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pp-discount-returns/:id : get the "id" pPDiscountReturn.
     *
     * @param id the id of the pPDiscountReturn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPDiscountReturn, or with status 404 (Not Found)
     */
    @GetMapping("/pp-discount-returns/{id}")
    @Timed
    public ResponseEntity<PPDiscountReturnDTO> getPPDiscountReturn(@PathVariable UUID id) {
        log.debug("REST request to get PPDiscountReturn : {}", id);
        Optional<PPDiscountReturnDTO> pPDiscountReturn = pPDiscountReturnService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPDiscountReturn);
    }
    @GetMapping("/pp-discount-returns/by-id/{id}")
    @Timed
    public ResponseEntity<PPDiscountReturn> getPPDiscountReturnByID(@PathVariable UUID id) {
        log.debug("REST request to get PPDiscountReturn : {}", id);
        Optional<PPDiscountReturn> pPDiscountReturn = pPDiscountReturnService.getPPDiscountReturnByID(id);
        return ResponseUtil.wrapOrNotFound(pPDiscountReturn);
    }

    /**
     * DELETE  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/pp-discount-returns/multiple-delete")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteGOtherVoucher(@RequestBody List<UUID> listID) {
        log.debug("REST request to closeBook : {}", listID);
        HandlingResultDTO responeCloseBookDTO = pPDiscountReturnService.multiDelete(listID);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
    @GetMapping("/pp-discount-return-objects-search")
    @Timed
    public ResponseEntity<List<PPDiscountReturnSearch2DTO>> searchPPDiscountReturn(Pageable pageable,
                                                                                   @RequestParam(required = false) UUID accountingObjectID,
                                                                                   @RequestParam(required = false) String currencyID,
                                                                                   @RequestParam(required = false) String fromDate,
                                                                                   @RequestParam(required = false) String toDate,
                                                                                   @RequestParam(required = false) Boolean status,
                                                                                   @RequestParam(required = false) Boolean statusPurchase,
                                                                                   @RequestParam(required = false) String keySearch)
    {
        log.debug("REST request to get a page of PPDiscountReturns");
        Page<PPDiscountReturnSearch2DTO> page = pPDiscountReturnService.searchPPDiscountReturn(pageable, accountingObjectID, currencyID, fromDate, toDate, status, statusPurchase, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-discount-returns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/pp-discount-returns/index")
    @Timed
    public ResponseEntity<PPDiscountReturn> findByRowNum(Pageable pageable,
                                                         @RequestParam(required = false) UUID accountingObjectID,
                                                         @RequestParam(required = false) String currencyID,
                                                         @RequestParam(required = false) String fromDate,
                                                         @RequestParam(required = false) String toDate,
                                                         @RequestParam(required = false) Boolean status,
                                                         @RequestParam(required = false) Boolean statusPurchase,
                                                         @RequestParam(required = false) String keySearch,
                                                         @RequestParam(required = false) Integer rowNum
    ) {
        log.debug("REST request to get a page of findByRowNum");
        PPDiscountReturn ppDiscountReturn = pPDiscountReturnService.findByRowNum(pageable, accountingObjectID, currencyID, fromDate,
            toDate, status, statusPurchase, keySearch, rowNum);
        return new ResponseEntity<>(ppDiscountReturn, HttpStatus.OK);
    }

    @GetMapping("/pp-discount-returns/ref-voucher/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> refVouchersByPPOrderID(@PathVariable UUID id) {
        log.debug("REST request to get a page of refVouchersByPPOrderID");
        List<RefVoucherDTO> refVoucherDTOList = pPDiscountReturnService.refVouchersByPPOrderID(id);
        return new ResponseEntity<>(refVoucherDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/pp-discount-returns/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(Pageable pageable, @RequestParam(required = false) UUID accountingObjectID,
                                             @RequestParam(required = false) String currencyID,
                                             @RequestParam(required = false) String fromDate,
                                             @RequestParam(required = false) String toDate,
                                             @RequestParam(required = false) Boolean status,
                                             @RequestParam(required = false) Boolean statusPurchase,
                                             @RequestParam(required = false) String keySearch) throws IOException {
        byte[] export = pPDiscountReturnService.exportPdf(pageable, accountingObjectID, currencyID,
            fromDate, toDate, status, statusPurchase, keySearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/pp-discount-returns/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(Pageable pageable, @RequestParam(required = false) UUID accountingObjectID,
                                              @RequestParam(required = false) String currencyID,
                                              @RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) Boolean status,
                                              @RequestParam(required = false) Boolean statusPurchase,
                                              @RequestParam(required = false) String keySearch) throws IOException {
        byte[] export = pPDiscountReturnService.exportExcel(pageable, accountingObjectID, currencyID,
            fromDate, toDate, status, statusPurchase, keySearch);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
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
    @GetMapping("/pp-discount-returns/search-all-dto")
    @Timed
    public ResponseEntity<Page<PPDiscountReturnOutWardDTO>> searchAllOrderDTO(Pageable pageable,
                                                                       @RequestParam(required = false) UUID accountingObject,
                                                                       @RequestParam(required = false) String fromDate,
                                                                       @RequestParam(required = false) String toDate) {
        log.debug("REST request to getAll PPDiscountReturnOutWardDTO");
        Page<PPDiscountReturnOutWardDTO> page = pPDiscountReturnService.findAllPPDisCountReturnDTO(pageable, accountingObject, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    /**
     * DELETE  /pp-discount-returns/:id : delete the "id" pPDiscountReturn.
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pp-discount-returns/{id}")
    @Timed
    public ResponseEntity<Void> deletePPDiscountReturn(@PathVariable UUID id) {
        log.debug("REST request to delete PPDiscountReturn : {}", id);
        pPDiscountReturnService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/pp-discount-returns/count-from-rsi/{id}")
    @Timed
    public ResponseEntity<Long> countFromRSI(@PathVariable UUID id) {
        log.debug("REST request to get PPDiscountReturn : {}", id);
        Long count = pPDiscountReturnService.countFromRSI(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/pp-discount-returns/multi-unrecord-pp-discount-returns")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiUnrecordSAReturn(@Valid @RequestBody List<PPDiscountReturn> ppDiscountReturns) {
        log.debug("REST request to closeBook : {}", ppDiscountReturns);
        HandlingResultDTO responeCloseBookDTO = pPDiscountReturnService.multiUnrecord(ppDiscountReturns);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }
}
