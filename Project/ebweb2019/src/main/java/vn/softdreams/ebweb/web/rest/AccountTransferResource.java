package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.AccountTransfer;
import vn.softdreams.ebweb.service.AccountTransferService;
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
 * REST controller for managing AccountTransfer.
 */
@RestController
@RequestMapping("/api")
public class AccountTransferResource {

    private final Logger log = LoggerFactory.getLogger(AccountTransferResource.class);

    private static final String ENTITY_NAME = "accountTransfer";

    private final AccountTransferService accountTransferService;

    public AccountTransferResource(AccountTransferService accountTransferService) {
        this.accountTransferService = accountTransferService;
    }

    /**
     * POST  /account-transfers : Create a new accountTransfer.
     *
     * @param accountTransfer the accountTransfer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountTransfer, or with status 400 (Bad Request) if the accountTransfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-transfers")
    @Timed
    public ResponseEntity<AccountTransfer> createAccountTransfer(@Valid @RequestBody AccountTransfer accountTransfer) throws URISyntaxException {
        log.debug("REST request to save AccountTransfer : {}", accountTransfer);
        if (accountTransfer.getId() != null) {
            throw new BadRequestAlertException("A new accountTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountTransfer result = accountTransferService.save(accountTransfer);
        return ResponseEntity.created(new URI("/api/account-transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-transfers : Updates an existing accountTransfer.
     *
     * @param accountTransfer the accountTransfer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountTransfer,
     * or with status 400 (Bad Request) if the accountTransfer is not valid,
     * or with status 500 (Internal Server Error) if the accountTransfer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-transfers")
    @Timed
    public ResponseEntity<AccountTransfer> updateAccountTransfer(@Valid @RequestBody AccountTransfer accountTransfer) throws URISyntaxException {
        log.debug("REST request to update AccountTransfer : {}", accountTransfer);
        if (accountTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountTransfer result = accountTransferService.save(accountTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountTransfer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-transfers : get all the accountTransfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountTransfers in body
     */
    @GetMapping("/account-transfers")
    @Timed
    public ResponseEntity<List<AccountTransfer>> getAllAccountTransfers(Pageable pageable) {
        log.debug("REST request to get a page of AccountTransfers");
        Page<AccountTransfer> page = accountTransferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-transfers/:id : get the "id" accountTransfer.
     *
     * @param id the id of the accountTransfer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountTransfer, or with status 404 (Not Found)
     */
    @GetMapping("/account-transfers/{id}")
    @Timed
    public ResponseEntity<AccountTransfer> getAccountTransfer(@PathVariable UUID id) {
        log.debug("REST request to get AccountTransfer : {}", id);
        Optional<AccountTransfer> accountTransfer = accountTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountTransfer);
    }

    /**
     * DELETE  /account-transfers/:id : delete the "id" accountTransfer.
     *
     * @param id the id of the accountTransfer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountTransfer(@PathVariable UUID id) {
        log.debug("REST request to delete AccountTransfer : {}", id);
        accountTransferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/account-transfers/find-all-account-transfers-companyid")
    @Timed
    public ResponseEntity<List<AccountTransfer>> getAllAccountTransferCompanyID() {
        log.debug("REST request to get a page of AccountList");
        List<AccountTransfer> page = accountTransferService.findAllAccountTransfers();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
