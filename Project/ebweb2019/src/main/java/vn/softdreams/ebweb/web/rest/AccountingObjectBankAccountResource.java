package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hibernate.annotations.Parameter;
import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;
import vn.softdreams.ebweb.service.AccountingObjectBankAccountService;
import vn.softdreams.ebweb.service.dto.AccountingObjectBankAccountConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing AccountingObjectBankAccount.
 */
@RestController
@RequestMapping("/api")
public class AccountingObjectBankAccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectBankAccountResource.class);

    private static final String ENTITY_NAME = "accountingObjectBankAccount";

    private final AccountingObjectBankAccountService accountingObjectBankAccountService;

    public AccountingObjectBankAccountResource(AccountingObjectBankAccountService accountingObjectBankAccountService) {
        this.accountingObjectBankAccountService = accountingObjectBankAccountService;
    }

    /**
     * POST  /accounting-object-bank-accounts : Create a new accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccount the accountingObjectBankAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingObjectBankAccount, or with status 400 (Bad Request) if the accountingObjectBankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-object-bank-accounts")
    @Timed
    public ResponseEntity<AccountingObjectBankAccount> createAccountingObjectBankAccount(@Valid @RequestBody AccountingObjectBankAccount accountingObjectBankAccount) throws URISyntaxException {
        log.debug("REST request to save AccountingObjectBankAccount : {}", accountingObjectBankAccount);
        if (accountingObjectBankAccount.getId() != null) {
            throw new BadRequestAlertException("A new accountingObjectBankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountingObjectBankAccount result = accountingObjectBankAccountService.save(accountingObjectBankAccount);
        return ResponseEntity.created(new URI("/api/accounting-object-bank-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounting-object-bank-accounts : Updates an existing accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccount the accountingObjectBankAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountingObjectBankAccount,
     * or with status 400 (Bad Request) if the accountingObjectBankAccount is not valid,
     * or with status 500 (Internal Server Error) if the accountingObjectBankAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounting-object-bank-accounts")
    @Timed
    public ResponseEntity<AccountingObjectBankAccount> updateAccountingObjectBankAccount(@Valid @RequestBody AccountingObjectBankAccount accountingObjectBankAccount) throws URISyntaxException {
        log.debug("REST request to update AccountingObjectBankAccount : {}", accountingObjectBankAccount);
        if (accountingObjectBankAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountingObjectBankAccount result = accountingObjectBankAccountService.save(accountingObjectBankAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountingObjectBankAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounting-object-bank-accounts : get all the accountingObjectBankAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjectBankAccounts in body
     */
    @GetMapping("/accounting-object-bank-accounts")
    @Timed
    public ResponseEntity<List<AccountingObjectBankAccount>> getAllAccountingObjectBankAccounts(Pageable pageable) {
        log.debug("REST request to get a page of AccountingObjectBankAccounts");
        Page<AccountingObjectBankAccount> page = accountingObjectBankAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-object-bank-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/accounting-object-bank-accounts/by-id")
    @Timed
    public ResponseEntity<List<AccountingObjectBankAccountDTO>> getByAccountingObjectId(@RequestParam UUID accountingObjectID) {
        log.debug("REST request to get a page of AccountingObjectBankAccounts");
        Page<AccountingObjectBankAccountDTO> page = accountingObjectBankAccountService.getByAccountingObjectId(accountingObjectID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accounting-object-bank-accounts/by-id");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accounting-object-bank-accounts/:id : get the "id" accountingObjectBankAccount.
     *
     * @param id the id of the accountingObjectBankAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountingObjectBankAccount, or with status 404 (Not Found)
     */
    @GetMapping("/accounting-object-bank-accounts/{id}")
    @Timed
    public ResponseEntity<AccountingObjectBankAccount> getAccountingObjectBankAccount(@PathVariable UUID id) {
        log.debug("REST request to get AccountingObjectBankAccount : {}", id);
        Optional<AccountingObjectBankAccount> accountingObjectBankAccount = accountingObjectBankAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingObjectBankAccount);
    }

    /**
     * DELETE  /accounting-object-bank-accounts/:id : delete the "id" accountingObjectBankAccount.
     *
     * @param id the id of the accountingObjectBankAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounting-object-bank-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountingObjectBankAccount(@PathVariable UUID id) {
        log.debug("REST request to delete AccountingObjectBankAccount : {}", id);
        accountingObjectBankAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /accounting-object-bank-accounts : Create a new accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccounts the accountingObjectBankAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountingObjectBankAccount, or with status 400 (Bad Request) if the accountingObjectBankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounting-object-bank-accounts/create-all")
    @Timed
    public ResponseEntity<Void> createAccountingObjectBankAccounts(@Valid @RequestBody AccountingObjectBankAccount[] accountingObjectBankAccounts)
        throws URISyntaxException {
        log.debug("REST request to save AccountingObjectBankAccounts : {}", accountingObjectBankAccounts);

//        if (accountingObjectBankAccount.getId() != null) {
//            throw new BadRequestAlertException("A new accountingObjectBankAccount cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        AccountingObjectBankAccount result = accountingObjectBankAccountService.save(accountingObjectBankAccount);
        accountingObjectBankAccountService.saveAll(Arrays.asList(accountingObjectBankAccounts));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, " new accountingObjectBankAccounts")).build();
    }

    @GetMapping("/accounting-object-bank-accounts/dto")
    @Timed
    public ResponseEntity<List<AccountingObjectBankAccountDTO>> getDTO() {
        log.debug("REST request to get a page of AccountingObjectBankAccounts");
        List<AccountingObjectBankAccountDTO> page = accountingObjectBankAccountService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
