package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.BudgetItem;
import vn.softdreams.ebweb.domain.PaymentClause;
import vn.softdreams.ebweb.service.PaymentClauseService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
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
 * REST controller for managing PaymentClause.
 */
@RestController
@RequestMapping("/api")
public class PaymentClauseResource {

    private final Logger log = LoggerFactory.getLogger(PaymentClauseResource.class);

    private static final String ENTITY_NAME = "paymentClause";

    private final PaymentClauseService paymentClauseService;

    public PaymentClauseResource(PaymentClauseService paymentClauseService) {
        this.paymentClauseService = paymentClauseService;
    }

    /**
     * POST  /payment-clauses : Create a new paymentClause.
     *
     * @param paymentClause the paymentClause to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentClause, or with status 400 (Bad Request) if the paymentClause has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-clauses")
    @Timed
    public ResponseEntity<PaymentClause> createPaymentClause(@Valid @RequestBody PaymentClause paymentClause) throws URISyntaxException {
        log.debug("REST request to save PaymentClause : {}", paymentClause);
        if (paymentClause.getId() != null) {
            throw new BadRequestAlertException("A new paymentClause cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //save
        PaymentClause result = paymentClauseService.save(paymentClause);
        return ResponseEntity.created(new URI("/api/payment-clauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-clauses : Updates an existing paymentClause.
     *
     * @param paymentClause the paymentClause to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentClause,
     * or with status 400 (Bad Request) if the paymentClause is not valid,
     * or with status 500 (Internal Server Error) if the paymentClause couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-clauses")
    @Timed
    public ResponseEntity<PaymentClause> updatePaymentClause(@Valid @RequestBody PaymentClause paymentClause) throws URISyntaxException {
        log.debug("REST request to update PaymentClause : {}", paymentClause);
        if (paymentClause.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentClause result = paymentClauseService.save(paymentClause);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentClause.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-clauses : get all the paymentClauses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentClauses in body
     */
    @GetMapping("/payment-clauses")
    @Timed
    public ResponseEntity<List<PaymentClause>> getAllPaymentClauses(Pageable pageable) {
        log.debug("REST request to get a page of PaymentClauses");
        Page<PaymentClause> page = paymentClauseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-clauses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-clauses : get all the paymentClauses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentClauses in body
     */
    @GetMapping("/payment-clauses/getAllPaymentClauses")
    @Timed
    public ResponseEntity<List<PaymentClause>> getAllPaymentClausesByCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of PaymentClauses");
        Page<PaymentClause> page = paymentClauseService.findAllPaymentClauses(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-clauses/getAllPaymentClauses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-clauses/:id : get the "id" paymentClause.
     *
     * @param id the id of the paymentClause to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentClause, or with status 404 (Not Found)
     */
    @GetMapping("/payment-clauses/{id}")
    @Timed
    public ResponseEntity<PaymentClause> getPaymentClause(@PathVariable UUID id) {
        log.debug("REST request to get PaymentClause : {}", id);
        Optional<PaymentClause> paymentClause = paymentClauseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentClause);
    }

    /**
     * DELETE  /payment-clauses/:id : delete the "id" paymentClause.
     *
     * @param id the id of the paymentClause to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-clauses/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentClause(@PathVariable UUID id) {
        log.debug("REST request to delete PaymentClause : {}", id);
        paymentClauseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/payment-clauses/find-all-payment-clause-by-companyid")
    @Timed
    public ResponseEntity<List<PaymentClause>> findAllByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<PaymentClause> page = paymentClauseService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/payment-clauses/multi-delete-payment-clause")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDelete(@RequestBody List<PaymentClause> paymentClauses) throws URISyntaxException {
        log.debug("REST request to delete multi BudgetItem : {}", paymentClauses);
        HandlingResultDTO handlingResultDTO = paymentClauseService.deleteMulti(paymentClauses);
        return new ResponseEntity<>(handlingResultDTO, HttpStatus.OK);
    }
}
