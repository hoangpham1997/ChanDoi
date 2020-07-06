package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.AccountingObjectService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;
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
 * REST controller for managing AccountingObject.
 */
@RestController
@RequestMapping("/api")
public class AccountingObjectResource {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectResource.class);

    private static final String ENTITY_NAME = "accountingObject";

    private final AccountingObjectService accountingObjectService;

    public AccountingObjectResource(AccountingObjectService accountingObjectService) {
        this.accountingObjectService = accountingObjectService;
    }

    /**
     * POST  /accounting-objects : Create a new accountingObject.
     *
     * @param accountingObject the accountingObject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingObject, or with status 400 (Bad Request) if the accountingObject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-objects")
    @Timed
    public ResponseEntity<AccountingObjectSaveDTO> createAccountingObject(@Valid @RequestBody AccountingObject accountingObject) throws URISyntaxException {
        log.debug("REST request to save Accounting Object: {}", accountingObject);
        if (accountingObject.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountingObjectSaveDTO result = accountingObjectService.save(accountingObject);
        if (result.getAccountingObject().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/accounting-objects/" + result.getAccountingObject().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getAccountingObject().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /accounting-objects : Updates an existing accountingObject.
     *
     * @param accountingObject the accountingObject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingObject,
     * or with status 400 (Bad Request) if the accountingObject is not valid,
     * or with status 500 (Internal Server Error) if the accountingObject couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-objects")
    @Timed
    public ResponseEntity<AccountingObjectSaveDTO> updateAccountingObject(@Valid @RequestBody AccountingObject accountingObject) throws URISyntaxException {
        log.debug("REST request to update Accounting Object : {}", accountingObject);
        if (accountingObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountingObjectSaveDTO result = accountingObjectService.save(accountingObject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingObject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-objects : get all the accountingObjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */
    @GetMapping("/accounting-objects")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjects(Pageable pageable) {
        log.debug("REST request to get a page of AccountingObjects");
        Page<AccountingObject> page = accountingObjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects : get all the accountingObjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/accounting-objects/pageable-all-accounting-objects")
    @Timed
    public ResponseEntity<List<AccountingObject>> pageableAllAccountingObjects(Pageable pageable, Integer typeAccountingObject) {
        log.debug("REST request to get a page of AccountingObjects");
        Page<AccountingObject> page = accountingObjectService.pageableAllAccountingObjects(pageable, typeAccountingObject);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/pageable-all-accounting-objects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects : get all the accountingObjects.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */
    @GetMapping("/accounting-objects/getAllAccountingObjects")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjects() {
        log.debug("REST request to get a page of AccountingObjects");
        Page<AccountingObject> page = accountingObjectService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getAllAccountingObjects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects/:id : get the "id" accountingObject.
     *
     * @param id the id of the accountingObject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingObject, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-objects/{id}")
    @Timed
    public ResponseEntity<AccountingObject> getAccountingObject(@PathVariable UUID id) {
        log.debug("REST request to get AccountingObject : {}", id);
        Optional<AccountingObject> accountingObject = accountingObjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingObject);
    }
    @GetMapping("/accounting-objects/customer-id")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAccountingObjectByGroupIDKH(@RequestParam(required = false) UUID id,
                                                                                 @RequestParam(required = false) Boolean similarBranch,
                                                                                    @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get AccountingObject : {}", id);
        List<AccountingObjectDTO> page = accountingObjectService.getAccountingObjectByGroupIDKH(id, similarBranch, companyID);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getAllAccountingObjects");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    /**
     * @author congnd
     * @param id
     * @param similarBranch
     * @param companyID
     * @return
     */
    @GetMapping("/accounting-objects/customer-id-similar-branch")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectByGroupIDKHSimilarBranch(@RequestParam(required = false) UUID id,
                                                                                              @RequestParam(required = false) Boolean similarBranch,
                                                                                              @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get AccountingObject : {}", id);
        List<AccountingObject> page = accountingObjectService.getAccountingObjectByGroupIDKHSimilarBranch(id, similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/get-one-no-voucher-pageable")
    @Timed
    public ResponseEntity<AccountingObject> getOneNoVoucher (Pageable pageable, @RequestParam UUID id) {
        log.debug("REST request to get AccountingObject : {}");
        Optional<AccountingObject> accountingObject = accountingObjectService.findOneWithPageable(pageable, id);
        return ResponseUtil.wrapOrNotFound(accountingObject);
    }

    @GetMapping("/accounting-objects/getVoucherByAccountingObjectID")
    @Timed
    public ResponseEntity<List<AccountingObject>> getVoucherByAccountingObjectID(Pageable pageable, @RequestParam UUID id) {
        log.debug("REST request to get AccountingObject : {}", id);
        Page<AccountingObject> page = accountingObjectService.findVoucherByAccountingObjectID(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getVoucherByAccountingObjectID");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/find-accounting-objects-by-group-id/by-id")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAccountingObjectsByGroupID(@RequestParam(required = false) UUID id,
                                                                                   @RequestParam(required = false) Boolean similarBranch,
                                                                                   @RequestParam(required = false) UUID companyID) {
        List<AccountingObjectDTO> page = accountingObjectService.getAccountingObjectsByGroupID(id, similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @param id
     * @param similarBranch
     * @param companyID
     * @return
     */
    @GetMapping("/find-accounting-objects-by-group-id/by-id-similar-branch")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAccountingObjectsByGroupIDSimilarBranch(@RequestParam(required = false) UUID id,
                                                                                                @RequestParam(required = false) Boolean similarBranch,
                                                                                                @RequestParam(required = false) UUID companyID) {
        List<AccountingObjectDTO> page = accountingObjectService.getAccountingObjectsByGroupIDSimilarBranch(id, similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * DELETE  /accounting-objects/:id : delete the "id" accountingObject.
     *
     * @param id the id of the accountingObject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-objects/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingObject(@PathVariable UUID id) {
        log.debug("REST request to delete AccountingObject : {}", id);
        accountingObjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @return lấy ra danh sách đối tượng
     */
    @GetMapping("/accounting-objects-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsActive() {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsActive();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects-active-by-report-request")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectActiveByReportRequest(@RequestBody RequestReport requestReport) {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectActiveByReportRequest(requestReport);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects-all-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getByAllAccountingObjectsActive() {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getByAllAccountingObjectsActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @return lấy ra danh sách nhân viên thực hiện
     */
    @GetMapping("/accounting-objects-employee")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsEmployee() {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsEmployee();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    /**
     * @author congnd
     * @param similarBranch
     * @param companyID
     * @return
     */
    @GetMapping("/accounting-objects-employee-similar-branch")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsEmployeeSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                                                               @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsEmployeeSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * Hautv
     * GET  /m-c-payments : get all the mCPaymentsDTO.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mCPayments in body
     */
    @GetMapping("/accounting-objectsDTO")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAllMCPaymentsDTO(@RequestParam(defaultValue = "0") Boolean isObjectType12) {
        log.debug("REST request to get a page of MCPayments");
        List<AccountingObjectDTO> page = accountingObjectService.findAllDTO(isObjectType12);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objectsDTO/getAll")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAllAccountingObjectDTO(@RequestParam(defaultValue = "0") Boolean isObjectType12) {
        log.debug("REST request to get a page of MCPayments");
        List<AccountingObjectDTO> page = accountingObjectService.findAllAccountingObjectDTO(isObjectType12);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/find-all-accounting-objects-by-companyid")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsCompanyID() {
        log.debug("REST request to get a page of AccountingObject");
        List<AccountingObject> page = accountingObjectService.findAllAccountingObjectByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects/order :  Load dữ liệu mặc định từ AccountingObject với điều kiện IsEmployee = 1 và IsActive = 1
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */
    @GetMapping("/accounting-objects/employee")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsForEmployee() {
        log.debug("REST Lấy accounting object phần nhân viên thực hiện");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsForEmployee();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects/order :  Load dữ liệu mặc định từ AccountingObject với điều kiện IsEmployee = 1
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */
    @GetMapping("/accounting-objects/employees")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsForEmployees() {
        log.debug("REST Lấy accounting object phần nhân viên thực hiện");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsForEmployees();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /accounting-objects/order :  Load mặc định dữ liệu danh mục Nhà cung cấp từ bảng AccountingObject theo điều
     * kiện ObjectType = 0 or 2 và IsActive = 1
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */
    @GetMapping("/accounting-objects/provider")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsForProvider() {
        log.debug("REST Lấy accounting object phần nhà cung cấp");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsForProvider();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/is-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsActive() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/is-active-transfer")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsTransferActive() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsTransferActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/rs-inwardoutward/is-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsActiveForRSInwardOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsActiveForRSInwardOutward();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/rs-inwardoutward/get-all")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsForRSInwardOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsForRSInwardOutward();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/rs-outward/is-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsRSOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsRSOutward();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/rs-outward/get-all")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsRSOutward() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsRSOutward();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/rs-transfer/is-active")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAccountingObjectsRSTransfer() {
        log.debug("REST Lấy accounting object hoạt động");
        List<AccountingObject> page = accountingObjectService.getAccountingObjectsRSTransfer();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /***
     * @author jsp
     * get Account Object DTO
     * @param taskMethod 0: mua dich vu
     */
    @GetMapping("/find-accounting-objectsDTOs")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAccountingObjects(@RequestParam(required = false) Integer taskMethod
    ) {
        log.debug("REST request to get a page of MCPayments");
        List<AccountingObjectDTO> page = accountingObjectService.findAllDTO(taskMethod);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /***
     * @author congnd
     * get Account Object DTO
     * @param taskMethod 0: mua dich vu
     */
    @GetMapping("/find-accounting-objectsDTOs-all")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> getAccountingObjectsAll(@RequestParam(required = false) Integer taskMethod
    ) {
        log.debug("REST request to get a page of MCPayments");
        List<AccountingObjectDTO> page = accountingObjectService.findAllDTOAll(taskMethod);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     */
    @GetMapping("/accounting-objects/getPPPayVendor")
    @Timed
    public ResponseEntity<List<PPPayVendorDTO>> getPPPayVendors(@RequestParam(required = false) String fromDate,
                                                                @RequestParam(required = false) String toDate
    ) {
        Page<PPPayVendorDTO> page = accountingObjectService.getPPPayVendorByDate(fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getPPPayVendor");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /***
     * @author chuongnv
     * get Danh sách trả tiền nhà cung cấp
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     * @param accountObjectID : mã nhà cung cấp
     */
    @GetMapping("/accounting-objects/getPPPayVendorBill")
    @Timed
    public ResponseEntity<List<PPPayVendorBillDTO>> getPPPayVendorBills(@RequestParam(required = false) String fromDate,
                                                                        @RequestParam(required = false) String toDate,
                                                                        @RequestParam(required = false) UUID accountObjectID
    ) {
        Page<PPPayVendorBillDTO> page = accountingObjectService.getPPPayVendorBillByAccountingObjectID(fromDate, toDate, accountObjectID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getPPPayVendor");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /***
     * @author chuongnv
     * get Danh sách thu tiền khách hàng
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     */
    @GetMapping("/accounting-objects/getSAReceiptDebit")
    @Timed
    public ResponseEntity<List<SAReceiptDebitDTO>> getGetReceiptDebits(@RequestParam(required = false) String fromDate,
                                                                       @RequestParam(required = false) String toDate
    ) {
        Page<SAReceiptDebitDTO> page = accountingObjectService.getSAReceiptDebitByDate(fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getSAReceiptDebit");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /***
     * @author chuongnv
     * get Danh sách thu tiền khách hàng
     * @param fromDate : từ ngày
     * @param toDate : đến ngày
     * @param accountObjectID : mã khách hàng
     */
    @GetMapping("/accounting-objects/getSAReceiptDebitBill")
    @Timed
    public ResponseEntity<List<SAReceiptDebitBillDTO>> getGetReceiptDebitBills(@RequestParam(required = false) String fromDate,
                                                                               @RequestParam(required = false) String toDate,
                                                                               @RequestParam(required = false) UUID accountObjectID
    ) {
        Page<SAReceiptDebitBillDTO> page = accountingObjectService.getSAReceiptDebitBillByAccountingObjectID(fromDate, toDate, accountObjectID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/getSAReceiptDebitBill");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }



    @GetMapping("/accounting-objects/accounting-object-search-all")
    @Timed
    public ResponseEntity<List<AccountingObject>> findAll(Pageable pageable,Integer typeAccountingObject,
                                              @RequestParam(required = false) String searchVoucherAccountingObjects
    ) {
        log.debug("REST request to get a page of Accounting Object");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucherAccountingObjects searchVoucherAccountingObjects1 = null;
        try {
            searchVoucherAccountingObjects1 = objectMapper.readValue(searchVoucherAccountingObjects, SearchVoucherAccountingObjects.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<AccountingObject> page = accountingObjectService.findAll1(pageable,typeAccountingObject, searchVoucherAccountingObjects1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/accounting-object-search-all");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/employee-search-all")
    @Timed
    public ResponseEntity<List<AccountingObject>> findAllEmployee(Pageable pageable,
                                                          @RequestParam(required = false) String searchVoucherEmployee
    ) {
        log.debug("REST request to get a page of Accounting Object");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucherEmployee searchVoucherEmployee1 = null;
        try {
            searchVoucherEmployee1 = objectMapper.readValue(searchVoucherEmployee, SearchVoucherEmployee.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<AccountingObject> page = accountingObjectService.findAllEmloyee1(pageable, searchVoucherEmployee1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-objects/employee-search-all");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/find-one-with-pageable")
    @Timed
    public ResponseEntity<AccountingObject> findOneWithPageable(@RequestParam UUID accId, int page, int size) {
        log.debug("REST Lấy accounting object hoạt động");
        Pageable pageable = new PageRequest(page, size);
        AccountingObject res = accountingObjectService.findOneWithPageable(pageable, accId).get();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/accounting-objects/delete-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedger(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = accountingObjectService.delete(uuids);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    @PostMapping("/accounting-objects/delete-list-employee")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerEmployee(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = accountingObjectService.deleteEmployee(uuids);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    /**
     * @author anmt
     * @return
     */
    @GetMapping("/get-all-accounting-objects-by-selected-companyID")
    @Timed
    public ResponseEntity<List<AccountingObject>> getAllAccountingObjectsByCompanyID(@RequestParam(required = false) UUID orgID,
                                                                                               @RequestParam(required = false) boolean isDependent) {
        log.debug("REST request to get a page of AccountingObjects");
        List<AccountingObject> page = accountingObjectService.getAllAccountingObjectsByCompanyID(orgID, isDependent);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/accounting-objects/find-all-accounting-object-by-company")
    @Timed
    public ResponseEntity<List<AccountingObjectDTO>> findAllAccountingObjectByCompany(@RequestParam UUID companyID, @RequestParam Boolean dependent) {
        List<AccountingObjectDTO> page = accountingObjectService.findAllAccountingObjectByCompany(companyID, dependent);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
