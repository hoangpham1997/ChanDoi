package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.poi.ss.formula.functions.T;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.PrepaidExpenseService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;
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
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PrepaidExpense.
 */
@RestController
@RequestMapping("/api")
public class PrepaidExpenseResource {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseResource.class);

    private static final String ENTITY_NAME = "prepaidExpense";

    private final PrepaidExpenseService prepaidExpenseService;

    public PrepaidExpenseResource(PrepaidExpenseService prepaidExpenseService) {
        this.prepaidExpenseService = prepaidExpenseService;
    }

    /**
     * POST  /prepaid-expenses : Create a new prepaidExpense.
     *

     * @return the ResponseEntity with status 201 (Created) and with body the new prepaidExpense, or with status 400 (Bad Request) if the prepaidExpense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prepaid-expenses")
    @Timed
    public ResponseEntity<PrepaidExpense> createPrepaidExpense(@RequestBody PrepaidExpense prepaidExpenseDTO) throws URISyntaxException {
        log.debug("REST request to save PrepaidExpense : {}", prepaidExpenseDTO);
        if (prepaidExpenseDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaidExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaidExpense result = prepaidExpenseService.save(prepaidExpenseDTO);
        return ResponseEntity.created(new URI("/api/prepaid-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prepaid-expenses : Updates an existing prepaidExpense.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated prepaidExpense,
     * or with status 400 (Bad Request) if the prepaidExpense is not valid,
     * or with status 500 (Internal Server Error) if the prepaidExpense couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prepaid-expenses")
    @Timed
    public ResponseEntity<PrepaidExpense> updatePrepaidExpense(@Valid @RequestBody PrepaidExpense prepaidExpenseDTO) throws URISyntaxException {
        log.debug("REST request to update PrepaidExpense : {}", prepaidExpenseDTO);
        if (prepaidExpenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrepaidExpense result = prepaidExpenseService.save(prepaidExpenseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/prepaid-expenses/is-active/{id}")
    @Timed
    public ResponseEntity<Void> updateIsActive(@PathVariable UUID id) throws URISyntaxException {
        log.debug("REST request to update PrepaidExpense : {}", id);
//        if (prepaidExpenseDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
        prepaidExpenseService.updateIsActive(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /prepaid-expenses : get all the prepaidExpenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prepaidExpenses in body
     */
    @GetMapping("/prepaid-expenses")
    @Timed
    public ResponseEntity<List<PrepaidExpense>> getAllPrepaidExpenses(Pageable pageable) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Page<PrepaidExpense> page = prepaidExpenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/prepaid-expenses/get-prepaid-expense-code")
    @Timed
    public ResponseEntity<List<PrepaidExpenseCodeDTO>> getPrepaidExpenseCode() {
        log.debug("REST request to get a page of PrepaidExpenses");
        List<PrepaidExpenseCodeDTO> prepaidExpenseCodes = prepaidExpenseService.getPrepaidExpenseCode();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    @GetMapping("/prepaid-expenses/get-prepaid-expense-code-can-active")
    @Timed
    public ResponseEntity<List<PrepaidExpenseCodeDTO>> getPrepaidExpenseCodeCanActive() {
        log.debug("REST request to get a page of PrepaidExpenses");
        List<PrepaidExpenseCodeDTO> prepaidExpenseCodes = prepaidExpenseService.getPrepaidExpenseCodeCanActive();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }
    @GetMapping("/prepaid-expenses/get-cost-accounts")
    @Timed
    public ResponseEntity<List<AccountList>> getCostAccounts() {
        log.debug("REST request to get a page of PrepaidExpenses");
        List<AccountList> prepaidExpenseCodes = prepaidExpenseService.getCostAccounts();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }
    @GetMapping("/prepaid-expenses/get-all")
    @Timed
    public ResponseEntity<List<PrepaidExpenseAllDTO>> getAll(Pageable pageable,
                                                             @RequestParam(required = false) Integer typeExpense,
                                                             @RequestParam(required = false) String fromDate,
                                                             @RequestParam(required = false) String toDate,
                                                             @RequestParam(required = false) String textSearch) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Page<PrepaidExpenseAllDTO> prepaidExpenseCodes = prepaidExpenseService.getAll(pageable, typeExpense, fromDate, toDate, textSearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(prepaidExpenseCodes, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseCodes.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/prepaid-expenses/prepaid-expense-allocation")
    @Timed
    public ResponseEntity<PrepaidExpenseObjectConvertDTO> getPrepaidExpenseAllocation(
                                                                @RequestParam(required = false) Integer month,
                                                                @RequestParam(required = false) Integer year
                                                                ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        PrepaidExpenseObjectConvertDTO prepaidExpenseCodes = prepaidExpenseService.getPrepaidExpenseAllocation(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    // đếm số lượng chi phí trả trước theo tháng và năm
    @GetMapping("/prepaid-expenses/prepaid-expense-allocation-count")
    @Timed
    public ResponseEntity<Long> getPrepaidExpenseAllocationCount(
                                                                @RequestParam(required = false) Integer month,
                                                                @RequestParam(required = false) Integer year
                                                                ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Long prepaidExpenseCodes = prepaidExpenseService.getPrepaidExpenseAllocationCount(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }
    // kiểm tra xem chi phí trả trước có dduwcowcj phép xóa không (theo id)
    @GetMapping("/prepaid-expenses/count-by-prepaid-expense-id/{id}")
    @Timed
    public ResponseEntity<Long> countByPrepaidExpenseID(@PathVariable UUID id) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Long prepaidExpenseCodes = prepaidExpenseService.countByPrepaidExpenseID(id);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    @GetMapping("/prepaid-expenses/prepaid-expense-allocation-duplicate")
    @Timed
    public ResponseEntity<GOtherVoucher> getPrepaidExpenseAllocationDuplicate(
                                                                @RequestParam(required = false) Integer month,
                                                                @RequestParam(required = false) Integer year
                                                                ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        GOtherVoucher prepaidExpenseCodes = prepaidExpenseService.getPrepaidExpenseAllocationDuplicate(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    /**
     * GET  /prepaid-expenses/:id : get the "id" prepaidExpense.
     *
     * @param id the id of the prepaidExpense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prepaidExpense, or with status 404 (Not Found)
     */
    @GetMapping("/prepaid-expenses/{id}")
    @Timed
    public ResponseEntity<PrepaidExpense> getPrepaidExpense(@PathVariable UUID id) {
        log.debug("REST request to get PrepaidExpense : {}", id);
        Optional<PrepaidExpense> prepaidExpense = prepaidExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaidExpense);
    }

    @GetMapping("/prepaid-expenses/ref/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherSecondDTO>> findPrepaidExpenseByID(@PathVariable UUID id) {
        log.debug("REST request to get PrepaidExpense : {}", id);
        List<RefVoucherSecondDTO> prepaidExpenseVoucher = prepaidExpenseService.findPrepaidExpenseByID(id);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(prepaidExpenseVoucher, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseVoucher, HttpStatus.OK);
    }

    @GetMapping("/prepaid-expenses/prepaid-expense-allocation-by-id/{id}")
    @Timed
    public ResponseEntity<List<PrepaidExpenseAllocation>> getPrepaidExpenseAllocationByID(@PathVariable UUID id) {
        log.debug("REST request to get PrepaidExpense : {}", id);
        List<PrepaidExpenseAllocation> prepaidExpenseVoucher = prepaidExpenseService.getPrepaidExpenseAllocationByID(id);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(prepaidExpenseVoucher, "/api/prepaid-expenses");
        return new ResponseEntity<>(prepaidExpenseVoucher, HttpStatus.OK);
    }

    /**
     * DELETE  /prepaid-expenses/:id : delete the "id" prepaidExpense.
     *
     * @param id the id of the prepaidExpense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prepaid-expenses/{id}")
    @Timed
    public ResponseEntity<Void> deletePrepaidExpense(@PathVariable UUID id) {
        log.debug("REST request to delete PrepaidExpense : {}", id);
        prepaidExpenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/prepaid-expenses/get-prepaid-expense-by-current-book-to-modal")
    @Timed
    public ResponseEntity<List<PrepaidExpense>> getPrepaidExpenseByCurrentBookToModal(Pageable pageable,
                                                                           @RequestParam(required = false) String fromDate,
                                                                           @RequestParam(required = false) String toDate,
                                                                           @RequestParam(required = false) Integer typeExpense) {
        log.debug("REST request to get a page of RefVouchers");
        Page<PrepaidExpense> page =  prepaidExpenseService.getPrepaidExpenseByCurrentBookToModal(pageable, fromDate,
            toDate, typeExpense);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/prepaid-expenses/find-by-company-id")
    @Timed
    public ResponseEntity<List<PrepaidExpense>> getPrepaidExpensesByCompanyID() {
        log.debug("REST request to get a page of AccountListDTO");
        List<PrepaidExpense> prepaidExpenses = prepaidExpenseService.getPrepaidExpensesByCompanyID();
        return new ResponseEntity<>(prepaidExpenses, HttpStatus.OK);
    }

    @PostMapping("/prepaid-expenses/multi-delete-prepaid-expenses")
    @Timed
    public ResponseEntity<HandlingResultDTO> MultiDeleteGOtherVoucher(@RequestBody List<PrepaidExpense> prepaidExpense) {
        log.debug("REST request to closeBook : {}", prepaidExpense);
        HandlingResultDTO responeCloseBookDTO = prepaidExpenseService.multiDelete(prepaidExpense);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

}
