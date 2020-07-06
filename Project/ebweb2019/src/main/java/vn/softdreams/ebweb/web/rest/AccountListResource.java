package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.service.AccountListService;
import vn.softdreams.ebweb.service.dto.*;
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
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.util.*;

/**
 * REST controller for managing AccountList.
 */
@RestController
@RequestMapping("/api")
public class AccountListResource {

    private final Logger log = LoggerFactory.getLogger(AccountListResource.class);

    private static final String ENTITY_NAME = "accountList";

    private final AccountListService accountListService;

    public AccountListResource(AccountListService accountListService) {
        this.accountListService = accountListService;
    }

    /**
     * POST  /account-lists : Create a new accountList.
     *
     * @param accountList the accountList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountList, or with status 400 (Bad Request) if the accountList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-lists")
    @Timed
    public ResponseEntity<AccountList> createAccountList(@Valid @RequestBody AccountList accountList) throws URISyntaxException {
        log.debug("REST request to save AccountList : {}", accountList);
        if (accountList.getId() != null) {
            throw new BadRequestAlertException("A new accountList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountList result = accountListService.save(accountList);
        return ResponseEntity.created(new URI("/api/account-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-lists : Updates an existing accountList.
     *
     * @param accountList the accountList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountList,
     * or with status 400 (Bad Request) if the accountList is not valid,
     * or with status 500 (Internal Server Error) if the accountList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-lists")
    @Timed
    public ResponseEntity<AccountList> updateAccountList(@Valid @RequestBody AccountList accountList) throws URISyntaxException {
        log.debug("REST request to update AccountList : {}", accountList);
        if (accountList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountList result = accountListService.save(accountList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-lists : get all the accountLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountLists in body
     */
    @GetMapping("/account-lists")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountLists(Pageable pageable) {
        log.debug("REST request to get a page of AccountLists");
        Page<AccountList> page = accountListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-lists/:id : get the "id" accountList.
     *
     * @param id the id of the accountList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountList, or with status 404 (Not Found)
     */
    @GetMapping("/account-lists/{id}")
    @Timed
    public ResponseEntity<AccountList> getAccountList(@PathVariable UUID id) {
        log.debug("REST request to get AccountList : {}", id);
        Optional<AccountList> accountList = accountListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountList);
    }

    /**
     * DELETE  /account-lists/:id : delete the "id" accountList.
     *
     * @param id the id of the accountList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountList(@PathVariable UUID id) {
        log.debug("REST request to delete AccountList : {}", id);
        accountListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/account-lists/search-all")
    @Timed
    public ResponseEntity<List<AccountList>> findAll(Pageable pageable) {
        log.debug("REST request to get a page of AccountLists");
        Page<AccountList> page = accountListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @author quyennc
     * lấy tài khoản nợ tk có tk thuế ...vv.vv
     * hàm tự động lấy companyID
     * @param typeID
     * @param columnName :truyền columnme của loại tài khoản
     * @return
     */
    @GetMapping("/account-lists/getAllAccountType")
    @Timed
    public ResponseEntity<List<AccountListDTO>> getAccountType(@RequestParam Integer typeID,
                                                                      @RequestParam String columnName,
                                                                      @RequestParam (required = false) Boolean ppType
    ) throws IOException {
        log.debug("REST request to get a page of AccountDefaults");
        List<AccountListDTO>  page = accountListService.getAccountType(typeID, columnName, ppType);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getAllAccountType");
        return new ResponseEntity<>(page, HttpStatus.OK);

    }

    @GetMapping("/account-lists/get-all-account-type")
    @Timed
    public ResponseEntity<Optional<AccountListAllDTO>> getAccountTypeSecond(@RequestParam(required = false) Integer typeID,
                                                                      @RequestParam(required = false) String columnName
    ) throws IOException {
        log.debug("REST request to get a page of AccountDefaults");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ColumnDTO[] columnNames = mapper.readValue(columnName, ColumnDTO[].class);
        Optional<AccountListAllDTO> page = accountListService.getAccountTypeSecond(typeID, Arrays.asList(columnNames));
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getAllAccountType");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/get-all-account-type-from-gov")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountTypeFromGOV() throws IOException {
        log.debug("REST request to get a page of AccountDefaults");
        ObjectMapper mapper = new ObjectMapper();
        List<AccountList> page = accountListService.getAccountTypeFromGOV();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getAllAccountType");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/get-all-account-third")
    @Timed
    public ResponseEntity<Optional<AccountListAllDTO>> getAccountTypeThird(@RequestParam(required = false) Integer typeID,
                                                                      @RequestParam(required = false) String columnName
    ) throws IOException {
        log.debug("REST request to get a page of AccountDefaults");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ColumnDTO[] columnNames = mapper.readValue(columnName, ColumnDTO[].class);
        Optional<AccountListAllDTO> page = accountListService.getAccountTypeThird(typeID, Arrays.asList(columnNames));
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getAllAccountType");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/account-lists/get-all-account-four")
    @Timed
    public ResponseEntity<Optional<AccountListAllDTO>> getAccountTypeFour(@RequestBody AccountThridDTO accountThridDTO
                                                                          ) throws IOException {
        log.debug("REST request to get a page of AccountDefaults");
        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ColumnDTO[] columnNames = mapper.readValue(columnName, ColumnDTO[].class);
        Optional<AccountListAllDTO> page = accountListService.getAccountTypeThird(accountThridDTO.getTypeID(), accountThridDTO.getColumnName());
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getAllAccountType");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/account-lists/findByGOtherVoucher")
    @Timed
    public ResponseEntity<List<AccountListDTO>> findByGOtherVoucher() {
        log.debug("REST request to get a page of Bank");
        List<AccountListDTO> page = accountListService.findByGOtherVoucher();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    /**
     * @author phuonghv find Account List by Account type
     * @param ppServiceType 240: PP_SERVICE_UNPAID, 241: PP_SERVICE_CASH,
     *                     242: PP_SERVICE_PAYMENT_ORDER, 243: PPSERVICE_CHECK_TRANSFER
     *                     244: PP_SERVICE_CREDIT_CARD, 245: PPSERVICE_CASH_CHECK
     * @param accountType 0 - debit , 1 - credit
     * @return
     */
    @GetMapping("/account-lists/get-accountlist-by-account-type")
    @Timed
    public ResponseEntity<List<AccountListDTO>> getAccountListByAccountType(@RequestParam Integer ppServiceType,
                                                                            @RequestParam Integer accountType) {
        log.debug("REST request to get a page of accountList By Account Type");
        List<AccountListDTO> accountListDTOS = accountListService.getAccountListByAccountType(ppServiceType, accountType);
        return new ResponseEntity<>(accountListDTOS, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-account-like-133")
    @Timed
    public ResponseEntity<List<AccountListDTO>> getAllAccountLike133ActiveCompanyID() {
        log.debug("REST request to get a page of AccountListDTO");
        List<AccountListDTO> page = accountListService.findAccountLike133();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-by-company-id")
    @Timed
    public ResponseEntity<List<AccountList>> findAllByCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of AccountLists");
        Page<AccountList> page = accountListService.findAllByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-lists/find-all-by-company-id");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-lists-active-companyid")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountListActiveCompanyID() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-lists-active-companyid-except-id")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountListActiveExceptID(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.findAllExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-active-company-id")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountActiveCompanyID() {
        log.debug("REST request to get a page of Account");
        List<AccountList> page = accountListService.findAllActive1();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-active")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountActiveCompanyID1() {
        log.debug("REST request to get a page of Account");
        List<AccountList> page = accountListService.findAllActive2();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-for-system-options")
    @Timed
    public ResponseEntity<List<AccountList>> findAllForSystemOptions() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.findAllForSystemOptions();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-lists-active-companyid-and-op")
    @Timed
    public ResponseEntity<List<AccountListDTO>> getAllAccountListActiveCompanyIDAndOP(@RequestParam(required = false) String currencyId) {
        log.debug("REST request to get a page of AccountList");
        List<AccountListDTO> page = accountListService.findAllActiveForOP(currencyId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(value = "/account-lists/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String currencyId) {
        byte[] export = accountListService.exportPdf(currencyId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-lists-companyid")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountListCompanyID() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.findAllAccountLists();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-by-org")
    @Timed
    public ResponseEntity<List<AccountList>> getAllAccountListByOrg(@RequestParam (required = false) UUID orgID) {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.findAllAccountListByOrg(orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-start-with-154")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountStartWith154() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountStartWith154();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-by-detailType")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountDetailType() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountDetailType();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/account-lists/find-all-account-by-detail-type-active")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountDetailTypeActive() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountDetailTypeActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/account-lists/find-all-account-start-with-112")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountStartWith112() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountStartWith112();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-start-with-111")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountStartWith111() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountStartWith111();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-for-account-default")
    @Timed
    public ResponseEntity<List<AccountForAccountDefaultDTO>> getAccountForAccountDefault(@RequestParam String listFilterAccount) {
        log.debug("REST request to get a page of AccountList");
        List<AccountForAccountDefaultDTO> page = accountListService.getAccountForAccountDefault(listFilterAccount);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-list")
    @Timed
    public ResponseEntity<List<AccountList>> getPageAccountList(Pageable pageable) {
        log.debug("REST request to get a page of AccountListDTO");
        List<AccountList> page = accountListService.findAllAccountList();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/account-lists/get-account-for-organization-unit")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountForOrganizationUnit() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountForOrganizationUnit();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/account-lists/delete-by-account-list-id")
    @Timed
    public ResponseEntity<Void> deleteByAccountListID(@RequestParam(required = false) UUID id
    ) {
        accountListService.deleteByAccountListID(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/account-lists/check-opn-and-exist-data")
    @Timed
    public ResponseEntity<Void> checkOPNAndExistData(@RequestParam(required = false) String accountNumber
    ) {
        accountListService.checkOPNAndExistData(accountNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-list-by-company-id-similar-branch")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountListSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                            @RequestParam(required = false) UUID companyID) {
        List<AccountList> result = accountListService.getAccountListSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/account-lists/get-grade-account")
    @Timed
    public ResponseEntity<List<Integer>> getGradeAccount() {
        log.debug("REST request to get a page of AccountList");
        List<Integer> gradeAccounts = accountListService.getGradeAccount();
        return new ResponseEntity<>(gradeAccounts, HttpStatus.OK);
    }

    @GetMapping("/account-lists/find-all-account-for-thcp-theo-kmcp")
    @Timed
    public ResponseEntity<List<AccountList>> getAccountForTHCPTheoKMCP() {
        log.debug("REST request to get a page of AccountList");
        List<AccountList> page = accountListService.getAccountForTHCPTheoKMCP();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/account-lists/find-all-account-list-active-and-accounting-type")
    @Timed
    public ResponseEntity<List<AccountForAccountDefaultDTO>> findAllAccountListActiveAndAccountingType() {
        log.debug("REST request to get a page of AccountList");
        List<AccountForAccountDefaultDTO> page = accountListService.findAllAccountListActiveAndAccountingType();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
