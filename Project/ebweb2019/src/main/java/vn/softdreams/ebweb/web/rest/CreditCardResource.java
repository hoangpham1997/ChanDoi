package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CreditCard;
import vn.softdreams.ebweb.service.CreditCardService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CreditCardSaveDTO;
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
 * REST controller for managing CreditCard.
 */
@RestController
@RequestMapping("/api")
public class CreditCardResource {

    private final Logger log = LoggerFactory.getLogger(CreditCardResource.class);

    private static final String ENTITY_NAME = "creditCard";

    private final CreditCardService creditCardService;

    public CreditCardResource(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    /**
     * POST  /credit-cards : Create a new creditCard.
     *
     * @param creditCard the creditCard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creditCard, or with status 400 (Bad Request) if the creditCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/credit-cards")
    @Timed
    public ResponseEntity<CreditCardSaveDTO> createCreditCard(@Valid @RequestBody CreditCard creditCard) throws URISyntaxException {
        log.debug("REST request to save CreditCard : {}", creditCard);
        if (creditCard.getId() != null) {
            throw new BadRequestAlertException("A new creditCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditCardSaveDTO result = creditCardService.save(creditCard);
        return ResponseEntity.created(new URI("/api/credit-cards/" + result.getCreditCard().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCreditCard().getId().toString()))
            .body(result);
    }

    /**
     * PUT  /credit-cards : Updates an existing creditCard.
     *
     * @param creditCard the creditCard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creditCard,
     * or with status 400 (Bad Request) if the creditCard is not valid,
     * or with status 500 (Internal Server Error) if the creditCard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/credit-cards")
    @Timed
    public ResponseEntity<CreditCardSaveDTO> updateCreditCard(@Valid @RequestBody CreditCard creditCard) throws URISyntaxException {
        log.debug("REST request to update CreditCard : {}", creditCard);
        if (creditCard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CreditCardSaveDTO result = creditCardService.save(creditCard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creditCard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /credit-cards : get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of creditCards in body
     */
    @GetMapping("/credit-cards")
    @Timed
    public ResponseEntity<List<CreditCard>> getAllCreditCards(Pageable pageable) {
        log.debug("REST request to get a page of CreditCards");
        Page<CreditCard> page = creditCardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /credit-cards : get all the creditCards.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of creditCards in body
     */
    @GetMapping("/credit-cards/getAllCreditCards")
    @Timed
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        log.debug("REST request to get a page of CreditCards");
        Page<CreditCard> page = creditCardService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards/getAllCreditCards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks : get all the creditCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/credit-cards/pageable-all-credit-card")
    @Timed
    public ResponseEntity<List<CreditCard>> pageableAllCreditCard(Pageable pageable) {
        log.debug("REST request to get a page of CreditCards");
        Page<CreditCard> page = creditCardService.pageableAllCreditCard(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/creditCards/pageable-all-credit-card");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    //check dung chung

    @GetMapping("/credit-cards//pageable-all-credit-cards")
    @Timed
    public ResponseEntity<List<CreditCard>> pageableAllCreditCards(Pageable pageable, @RequestParam(required = false) Boolean isGetAllCompany) {
        log.debug("REST request to get a page of CreditCard");
        Page<CreditCard> page = creditCardService.pageableAllCreditCards(pageable, isGetAllCompany);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards/pageable-all-/pageable-all-credit-cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /credit-cards/:id : get the "id" creditCard.
     *
     * @param id the id of the creditCard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creditCard, or with status 404 (Not Found)
     */
    @GetMapping("/credit-cards/{id}")
    @Timed
    public ResponseEntity<CreditCard> getCreditCard(@PathVariable UUID id) {
        log.debug("REST request to get CreditCard : {}", id);
        Optional<CreditCard> creditCard = creditCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditCard);
    }

    /**
     * DELETE  /credit-cards/:id : delete the "id" creditCard.
     *
     * @param id the id of the creditCard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/credit-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreditCard(@PathVariable UUID id) {
        log.debug("REST request to delete CreditCard : {}", id);
        creditCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/credit-cards/findByCreditCardNumber")
    @Timed
    public ResponseEntity<CreditCard> getCreditCardByCreditCardNumber(@RequestParam(required = false) String creditCardNumber) {
        CreditCard creditCard = creditCardService.findByCreditCardNumber(creditCardNumber);
        return new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    @GetMapping("/credit-cards/find-all-credit-cards-by-companyid")
    @Timed
    public ResponseEntity<List<CreditCard>> getAllCreditCardsByCompanyID() {
        List<CreditCard> page = creditCardService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/credit-cards/find-all-credit-cards-active-by-companyid")
    @Timed
    public ResponseEntity<List<CreditCard>> getAllCreditCardsActiveByCompanyID() {
        List<CreditCard> page = creditCardService.findAllActiveByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/credit-cards/delete-credit-card")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerEmployee(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = creditCardService.deleteEmployee(uuids);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }
}
