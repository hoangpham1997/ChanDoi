package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.CollectionVoucher;
import vn.softdreams.ebweb.domain.GLCPExpenseList;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.domain.Record;
import vn.softdreams.ebweb.repository.SaReturnRepository;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.dto.CPAllocationDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestRecordListDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing GeneralLedger.
 */
@RestController
@RequestMapping("/api")
public class GeneralLedgerResource {

    private final Logger log = LoggerFactory.getLogger(GeneralLedgerResource.class);

    private static final String ENTITY_NAME = "generalLedger";

    private final GeneralLedgerService generalLedgerService;

    public GeneralLedgerResource(GeneralLedgerService generalLedgerService) {
        this.generalLedgerService = generalLedgerService;
    }

    /**
     * POST  /general-ledgers : Create a new generalLedger.
     *
     * @param generalLedger the generalLedger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new generalLedger, or with status 400 (Bad Request) if the generalLedger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/general-ledgers")
    @Timed
    public ResponseEntity<GeneralLedger> createGeneralLedger(@Valid @RequestBody GeneralLedger generalLedger) throws
        URISyntaxException {
        log.debug("REST request to save GeneralLedger : {}", generalLedger);
        if (generalLedger.getId() != null) {
            throw new BadRequestAlertException("A new generalLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneralLedger result = generalLedgerService.save(generalLedger);
        return ResponseEntity.created(new URI("/api/general-ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /general-ledgers : Updates an existing generalLedger.
     *
     * @param generalLedger the generalLedger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated generalLedger,
     * or with status 400 (Bad Request) if the generalLedger is not valid,
     * or with status 500 (Internal Server Error) if the generalLedger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/general-ledgers")
    @Timed
    public ResponseEntity<GeneralLedger> updateGeneralLedger(@Valid @RequestBody GeneralLedger generalLedger) throws
        URISyntaxException {
        log.debug("REST request to update GeneralLedger : {}", generalLedger);
        if (generalLedger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeneralLedger result = generalLedgerService.save(generalLedger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, generalLedger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /general-ledgers : get all the generalLedgers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of generalLedgers in body
     */
    @GetMapping("/general-ledgers")
    @Timed
    public ResponseEntity<List<GeneralLedger>> getAllGeneralLedgers(Pageable pageable) {
        log.debug("REST request to get a page of GeneralLedgers");
        Page<GeneralLedger> page = generalLedgerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/general-ledgers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @author quyênnc
     *
     * @param status true ? Tập hợp chi phí trực tiếp : Tập hợp các khoản giảm giá thành
     */
    @GetMapping("/general-ledgers/get-cp-expenselist")
    @Timed
    public ResponseEntity<List<GiaThanhPoPupDTO>> getByRatioMethod(Pageable pageable, @RequestParam(required = false) String fromDate,
                                                                   @RequestParam(required = false) String toDate, @RequestParam(required = false)
                                                                        List<UUID> costSetID, @RequestParam(required = false)
                                                                        Integer status) {
        log.debug("REST request to get a page of GeneralLedgers");
        Page<GiaThanhPoPupDTO> page = generalLedgerService.getByRatioMethod(pageable, fromDate, toDate, costSetID, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/general-ledgers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/get-cp-expenselist-all")
    @Timed
    public ResponseEntity<List<GiaThanhPoPupDTO>> getExpenseList(@RequestParam(required = false) String fromDate,
                                                                   @RequestParam(required = false) String toDate, @RequestParam(required = false)
                                                                        List<UUID> costSetID) {
        log.debug("REST request to get a page of GeneralLedgers");
        List<GiaThanhPoPupDTO> page = generalLedgerService.getExpenseList(fromDate, toDate, costSetID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/allocation-method")
    @Timed
    public ResponseEntity<CPAllocationDTO> getByAllocationMethod(@RequestParam(required = false) String fromDate,
                                                                 @RequestParam(required = false) String toDate, @RequestParam(required = false)
                                                                                           List<UUID> costSetID, @RequestParam(required = false)
                                                                                           Integer status) {
        log.debug("REST request to get a page of GeneralLedgers");
        CPAllocationDTO page = generalLedgerService.getByAllocationMethod(fromDate, toDate, costSetID, status);
        return new ResponseEntity<CPAllocationDTO>(page, HttpStatus.OK);
    }

    /**
     * GET  /general-ledgers/:id : get the "id" generalLedger.
     *
     * @param id the id of the generalLedger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the generalLedger, or with status 404 (Not Found)
     */
    @GetMapping("/general-ledgers/{id}")
    @Timed
    public ResponseEntity<GeneralLedger> getGeneralLedger(@PathVariable UUID id) {
        log.debug("REST request to get GeneralLedger : {}", id);
        Optional<GeneralLedger> generalLedger = generalLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generalLedger);
    }


    /**
     * DELETE  /general-ledgers/:id : delete the "id" generalLedger.
     *
     * @param id the id of the generalLedger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/general-ledgers/{id}")
    @Timed
    public ResponseEntity<Void> deleteGeneralLedger(@PathVariable UUID id) {
        log.debug("REST request to delete GeneralLedger : {}", id);
        generalLedgerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * add by Hautv
     * ghi sổ
     *
     * @param record
     * @return
     */
    @PostMapping("/general-ledgers/record")
    @Timed
    public ResponseEntity<Record> recordGeneralLedger(@Valid @RequestBody Record record) {
        Record record1 = generalLedgerService.record(record);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        // edit by anmt
        return ResponseEntity.status(HttpStatus.OK)
            .headers(HeaderUtil.createEntityRecordedAlert(ENTITY_NAME, record1.getMsg()))
            .body(record1);
    }

    /**
     * add by Hautv
     * ghi sổ
     *
     * @param requestRecordListDTO
     * @return
     */
    @PostMapping("/general-ledgers/record-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedger(@Valid @RequestBody RequestRecordListDTO requestRecordListDTO) {
        HandlingResultDTO record1 = generalLedgerService.record(requestRecordListDTO);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(record1);
    }

    /**
     * Add by hautv
     * bỏ ghi sổ
     *
     * @param record
     * @return
     */
    @PostMapping("/general-ledgers/unrecord")
    @Timed
    public ResponseEntity<Record> unrecordGeneralLedger(@Valid @RequestBody Record record) {
        Record record1 = generalLedgerService.unRecord(record);
        //  return new ResponseEntity<>(record, HttpStatus.OK);
        // edit by anmt
        return ResponseEntity.status(HttpStatus.OK)
            .headers(HeaderUtil.createEntityUnrecordedAlert(ENTITY_NAME, record1.getMsg()))
            .body(record1);
    }


    @PostMapping("/general-ledgers/unrecord-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> unrecordGeneralLedger(@Valid @RequestBody RequestRecordListDTO requestRecordListDTO) {
        HandlingResultDTO handlingResultDTO = generalLedgerService.unRecord(requestRecordListDTO);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    /**
     * Add by chuongnv
     * lấy số dư sổ quỹ
     *
     * @param auditDate
     * @param currencyID
     * @return
     */
    @GetMapping("/general-ledgers/getTotalBalanceAmount")
    @Timed
    public ResponseEntity<BigDecimal> getTotalBalanceAmount(@RequestParam String auditDate,
                                                            @RequestParam String currencyID) {
        BigDecimal totalBalanceAmount = generalLedgerService.getTotalBalanceAmount(auditDate, currencyID);
        return new ResponseEntity<BigDecimal>(totalBalanceAmount, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/getCollectionVoucher")
    @Timed
    public ResponseEntity<List<CollectionVoucher>> getCollectionVoucher(@RequestParam(required = false) String
                                                                            date,
                                                                        @RequestParam(required = false) String currencyID,
                                                                        @RequestParam(required = false) UUID bankID) {
        List<CollectionVoucher> collectionVoucherList = generalLedgerService.getCollectionVoucher(date, currencyID, bankID);
        return new ResponseEntity<List<CollectionVoucher>>(collectionVoucherList, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/getSpendingVoucher")
    @Timed
    public ResponseEntity<List<CollectionVoucher>> getSpendingVoucher(@RequestParam(required = false) String date,
                                                                      @RequestParam(required = false) String currencyID,
                                                                      @RequestParam(required = false) UUID bankID) {
        List<CollectionVoucher> collectionVoucherList = generalLedgerService.getSpendingVoucher(date, currencyID, bankID);
        return new ResponseEntity<List<CollectionVoucher>>(collectionVoucherList, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/getListMatch")
    @Timed
    public ResponseEntity<List<CollectionVoucher>> getListMatch() {
        List<CollectionVoucher> collectionVoucherList = generalLedgerService.getListMatch();
        return new ResponseEntity<List<CollectionVoucher>>(collectionVoucherList, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/calculating-liabilities")
    @Timed
    public ResponseEntity<UpdateDataDTO> calculatingLiabilities(@RequestParam(required = false) UUID accountingObjectId,
                                                                @RequestParam(required = false) String postDate) {
        UpdateDataDTO updateDataDTO = generalLedgerService.calculatingLiabilities(accountingObjectId, postDate);
        return new ResponseEntity<>(updateDataDTO, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/getListForCPExpenseList")
    @Timed
    public ResponseEntity<List<GLCPExpenseList>> getListForCPExpenseList() {
        List<GLCPExpenseList> gLCPExpenseList = generalLedgerService.getListForCPExpenseList();
        return new ResponseEntity<List<GLCPExpenseList>>(gLCPExpenseList, HttpStatus.OK);
    }

    @GetMapping("/general-ledgers/getViewGLPayExceedCash")
    @Timed
    public ResponseEntity<List<ViewGLPayExceedCashDTO>> getViewGLPayExceedCash() {
        List<ViewGLPayExceedCashDTO> glPayExceedCashDTOS = generalLedgerService.getViewGLPayExceedCash();
        return new ResponseEntity<List<ViewGLPayExceedCashDTO>>(glPayExceedCashDTOS, HttpStatus.OK);
    }


}
