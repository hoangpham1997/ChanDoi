package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.service.BankAccountDetailsService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.BankAccountDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.BankAccountDetailsSaveDTO;
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
 * REST controller for managing BankAccountDetails.
 */
@RestController
@RequestMapping("/api")
public class BankAccountDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountDetailsResource.class);

    private static final String ENTITY_NAME = "bankAccountDetails";

    private final BankAccountDetailsService bankAccountDetailsService;

    public BankAccountDetailsResource(BankAccountDetailsService bankAccountDetailsService) {
        this.bankAccountDetailsService = bankAccountDetailsService;
    }

    /**
     * POST  /bank-account-details : Create a new bankAccountDetails.
     *
     * @param bankAccountDetails the bankAccountDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bankAccountDetails, or with status 400 (Bad Request) if the bankAccountDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bank-account-details")
    @Timed
    public ResponseEntity<BankAccountDetailsSaveDTO> createBankAccountDetails(@Valid @RequestBody BankAccountDetails bankAccountDetails) throws URISyntaxException {
        log.debug("REST request to save Bank Account Details: {}", bankAccountDetails);
        if (bankAccountDetails.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankAccountDetailsSaveDTO result = bankAccountDetailsService.save(bankAccountDetails);
        if (result.getBankAccountDetails().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/accounting-objects/" + result.getBankAccountDetails().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getBankAccountDetails().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /bank-account-details : Updates an existing bankAccountDetails.
     *
     * @param bankAccountDetails the bankAccountDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bankAccountDetails,
     * or with status 400 (Bad Request) if the bankAccountDetails is not valid,
     * or with status 500 (Internal Server Error) if the bankAccountDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bank-account-details")
    @Timed
    public ResponseEntity<BankAccountDetailsSaveDTO> updateBankAccountDetails(@Valid @RequestBody BankAccountDetails bankAccountDetails) throws URISyntaxException {
        log.debug("REST request to update BankAccountDetails : {}", bankAccountDetails);
        if (bankAccountDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BankAccountDetailsSaveDTO result = bankAccountDetailsService.save(bankAccountDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bankAccountDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bank-account-details : get all the bankAccountDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bankAccountDetails in body
     */
    @GetMapping("/bank-account-details")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetails(Pageable pageable) {
        log.debug("REST request to get a page of BankAccountDetails");
        Page<BankAccountDetails> page = bankAccountDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-account-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bank-account-details : get all the bankAccountDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bankAccountDetails in body
     */
    @GetMapping("/bank-account-details/getAllBankAccountDetails")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetails() {
        log.debug("REST request to get a page of BankAccountDetails");
        Page<BankAccountDetails> page = bankAccountDetailsService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-account-details/getAllBankAccountDetails");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bankAccountDetails : get all the bankAccountDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/bank-account-details/pageable-all-bank-account-details")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> pageableAllBankAccountDetails(Pageable pageable) {
        log.debug("REST request to get a page of BankAccountDetails");
        Page<BankAccountDetails> page = bankAccountDetailsService.pageableAllBankAccountDetails(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-account-details/pageable-all-bank-account-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /bank-account-details/:id : get the "id" bankAccountDetails.
     *
     * @param id the id of the bankAccountDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bankAccountDetails, or with status 404 (Not Found)
     */
    @GetMapping("/bank-account-details/{id}")
    @Timed
    public ResponseEntity<BankAccountDetails> getBankAccountDetails(@PathVariable UUID id) {
        log.debug("REST request to get BankAccountDetails : {}", id);
        Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankAccountDetails);
    }

    @GetMapping("/bank-account-details/find-bank-account-details-id")
    @Timed
    public ResponseEntity<BankAccountDetails> getBankAccountDetailID(@PathVariable UUID id) {
        log.debug("REST request to get BankAccountDetails : {}", id);
        Optional<BankAccountDetails> bankAccountDetails = bankAccountDetailsService.findID(id);
        return ResponseUtil.wrapOrNotFound(bankAccountDetails);
    }

    /**
     * DELETE  /bank-account-details/:id : delete the "id" bankAccountDetails.
     *
     * @param id the id of the bankAccountDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bank-account-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteBankAccountDetails(@PathVariable UUID id) {
        log.debug("REST request to delete BankAccountDetails : {}", id);
        bankAccountDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/bank-account-details/delete-list-bank-account-details")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerUnit(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = bankAccountDetailsService.deleteBank(uuids);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    @GetMapping("/bank-account-details/find-all-bank-account-details-active-companyid")
    @Timed
    public ResponseEntity<List<ComboboxBankAccountDetailDTO>> getAllBankAccountDetailsActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<ComboboxBankAccountDetailDTO> page = bankAccountDetailsService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/bank-account-details/find-all-bank-account-details-by-companyid")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetailsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<BankAccountDetails> page = bankAccountDetailsService.getAllBankAccountDetailsByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/bank-account-details/find-all-for-acc-type")
    @Timed
    public ResponseEntity<List<ComboboxBankAccountDetailDTO>> findAllForAccType() {
        log.debug("REST request to get a page of Accounts");
        List<ComboboxBankAccountDetailDTO> page = bankAccountDetailsService.findAllForAccType();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/bank-account-details/find-all-bank-account-details-active-companyid-custom")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetailsActiveCompanyIDCustom() {
        log.debug("REST request to get a page of Accounts");
        List<BankAccountDetails> page = bankAccountDetailsService.findAllActiveCustom();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/bank-account-details/find-all-bank-account-details-active-by-orgid")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetailsByOrgID(@RequestParam UUID orgID) {
        log.debug("REST request to get a page of Accounts");
        List<BankAccountDetails> page = bankAccountDetailsService.getAllBankAccountDetailsByOrgID(orgID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/bank-account-details/find-all-bank-account-details")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> allBankAccountDetails() {
        log.debug("REST request to get a page of Accounts");
        List<BankAccountDetails> page = bankAccountDetailsService.allBankAccountDetails();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/bank-account-details/find-all-bank-account-details-active-companyid-not-credit-card")
    @Timed
    public ResponseEntity<List<BankAccountDetails>> getAllBankAccountDetailsActiveCompanyIDNotCreditCard() {
        log.debug("REST request to get a page of Accounts");
        List<BankAccountDetails> page = bankAccountDetailsService.getAllBankAccountDetailsActiveCompanyIDNotCreditCard();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
