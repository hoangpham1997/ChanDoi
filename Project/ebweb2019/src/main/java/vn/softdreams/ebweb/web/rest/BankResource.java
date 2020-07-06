package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.SearchVoucherBank;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.BankService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.BankConvertDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.BankSaveDTO;
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
 * REST controller for managing Bank.
 */
@RestController
@RequestMapping("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bank";

    private final BankService bankService;

    public BankResource(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * POST  /banks : Create a new bank.
     *
     * @param bank the bank to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bank, or with status 400 (Bad Request) if the bank has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banks")
    @Timed
    public ResponseEntity<BankSaveDTO> createBank(@Valid @RequestBody Bank bank) throws URISyntaxException {
        log.debug("REST request to save Bank : {}", bank);
        if (bank.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankSaveDTO result = bankService.save(bank);
        if (result.getBank().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/currencies/" + result.getBank().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getBank().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /banks : Updates an existing bank.
     *
     * @param bank the bank to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bank,
     * or with status 400 (Bad Request) if the bank is not valid,
     * or with status 500 (Internal Server Error) if the bank couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banks")
    @Timed
    public ResponseEntity<BankSaveDTO> updateBank(@Valid @RequestBody Bank bank) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", bank);
        if (bank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BankSaveDTO result = bankService.save(bank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bank.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banks : get all the banks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of banks in body
     */
    @GetMapping("/banks")
    @Timed
    public ResponseEntity<List<Bank>> getAllBanks(Pageable pageable) {
        log.debug("REST request to get a page of Banks");
        Page<Bank> page = bankService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks : get all the banks.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of banks in body
     */
    @GetMapping("/banks/getAllBanks")
    @Timed
    public ResponseEntity<List<Bank>> getAllBanks() {
        log.debug("REST request to get a page of Banks");
        Page<Bank> page = bankService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks/getAllBanks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks : get all the banks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/banks/pageable-all-bank")
    @Timed
    public ResponseEntity<List<Bank>> pageableAllBank(Pageable pageable) {
        log.debug("REST request to get a page of Banks");
        Page<Bank> page = bankService.pageableAllBank(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks/pageable-all-bank");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks/:id : get the "id" bank.
     *
     * @param id the id of the bank to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bank, or with status 404 (Not Found)
     */
    @GetMapping("/banks/{id}")
    @Timed
    public ResponseEntity<Bank> getBank(@PathVariable UUID id) {
        log.debug("REST request to get Bank : {}", id);
        Optional<Bank> bank = bankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bank);
    }

    @GetMapping("/banks/find-bank-id/{id}")
    @Timed
    public ResponseEntity<Bank> getIDBank(@PathVariable UUID id) {
        log.debug("REST request to get Bank : {}", id);
        Optional<Bank> bank = bankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bank);
    }

    /**
     * DELETE  /banks/:id : delete the "id" bank.
     *
     * @param id the id of the bank to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banks/{id}")
    @Timed
    public ResponseEntity<Void> deleteBank(@PathVariable UUID id) {
        log.debug("REST request to delete Bank : {}", id);
        bankService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/banks/search-all")
    @Timed
    public ResponseEntity<List<Bank>> findAll(Pageable pageable, @RequestParam(required = false) String bankCode,
                                              @RequestParam(required = false) String bankName,
                                              @RequestParam(required = false) String bankNameRepresent,
                                              @RequestParam(required = false) String address,
                                              @RequestParam(required = false) String description,
                                              @RequestParam(required = false) Boolean isActive) {
        log.debug("REST request to get a page of Units");
        Page<Bank> page = bankService.findAll(pageable, bankCode, bankName, bankNameRepresent, address, description, isActive);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/banks/find-all-banks-active-companyid")
    @Timed
    public ResponseEntity<List<Bank>> getAllBankActiveCompanyID() {
        log.debug("REST request to get a page of Bank");
        List<Bank> page = bankService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/banks/find-all-bank-by-companyid")
    @Timed
    public ResponseEntity<List<Bank>> getAllBankByCompanyID() {
        log.debug("REST request to get a page of Bank");
        List<Bank> page = bankService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/banks/find-all-banks-by-companyid")
    @Timed
    public ResponseEntity<List<Bank>> getAllBankCompanyID() {
        log.debug("REST request to get a page of AccountList");
        List<Bank> page = bankService.findAllBankByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/banks/bank-search-all")
    @Timed
    public ResponseEntity<List<Bank>> searchAllBank(Pageable pageable,
                                                       @RequestParam(required = false) String searchVoucherBank
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucherBank searchVoucherBank1 = null;
        try {
            searchVoucherBank1 = objectMapper.readValue(searchVoucherBank, SearchVoucherBank.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<Bank> page = bankService.findAllBank(pageable, searchVoucherBank1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/banks/delete-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedger(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = bankService.delete(uuids);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }
}
