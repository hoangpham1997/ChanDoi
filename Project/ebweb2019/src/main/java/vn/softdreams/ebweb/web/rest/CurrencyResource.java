package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.SearchVoucherCurrency;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CurrencyService;
import vn.softdreams.ebweb.web.rest.dto.CurrencySaveDTO;
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
 * REST controller for managing Currency.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    private final CurrencyService currencyService;

    public CurrencyResource(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * POST  /currencies : Create a new currency.
     *
     * @param currency the currency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currency, or with status 400 (Bad Request) if the currency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencySaveDTO> createCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currency);
        if (currency.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencySaveDTO result = currencyService.save(currency);
        if (result.getCurrency().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/currencies/" + result.getCurrency().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getCurrency().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /currencies : Updates an existing currency.
     *
     * @param currency the currency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currency,
     * or with status 400 (Bad Request) if the currency is not valid,
     * or with status 500 (Internal Server Error) if the currency couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencySaveDTO> updateCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currency);
        if (currency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencySaveDTO result = currencyService.save(currency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currencies : get all the currencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencies in body
     */
    @GetMapping("/currencies")
    @Timed
    public ResponseEntity<List<Currency>> getAllCurrencies(Pageable pageable) {
//        log.debug("REST request to get a page of Currencies");
        Page<Currency> page    = currencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currencies : get all the currencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencies in body
     */
    @GetMapping("/currencies/is-active")
    @Timed
    public ResponseEntity<List<Currency>> getAllActiveCurrencies(Pageable pageable) {
        log.debug("REST request to get a page of Currencies");
        Page<Currency> page    = currencyService.findAllByIsActiveIsTrue(pageable);
        HttpHeaders    headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currencies : get all the currencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of currencies in body
     */
    @GetMapping("/currencies/getAllCurrencies")
    @Timed
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        log.debug("REST request to get a page of Currencies");
        Page<Currency> page = currencyService.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies/getAllCurrencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/currencies/pageable-all-currency")
    @Timed
    public ResponseEntity<List<Currency>> pageableAllCurrency(Pageable pageable) {
        log.debug("REST request to get a page of Currencies");
        Page<Currency> page = currencyService.pageableAllCurrency(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/creditCards/pageable-all-credit-card");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currencies/:id : get the "id" currency.
     *
     * @param id the id of the currency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currency, or with status 404 (Not Found)
     */
    @GetMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<Currency> getCurrency(@PathVariable UUID id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<Currency> currency = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currency);
    }

    /**
     * DELETE  /currencies/:id : delete the "id" currency.
     *
     * @param id the id of the currency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrency(@PathVariable UUID id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/get-currency")
    @Timed
    public ResponseEntity<List<Currency>> getCurrencies() {
        log.debug("REST request to get a page of Currencies");
        Page<Currency> page = currencyService.findCurrencies();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/currencies/active")
    @Timed
    public ResponseEntity<List<Currency>> getAllCurrenciesActive() {
        log.debug("REST request to get a page of Currencies");
        List<Currency> page = currencyService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/currencies/find-all-by-company-id")
    @Timed
    public ResponseEntity<List<Currency>> findAllByCompanyID() {
        List<Currency> page = currencyService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/currencies/find-all-by-company-id-null")
    @Timed
    public ResponseEntity<List<Currency>> findAllByCompanyIDNull() {
        log.debug("REST request to get a page of Currencies");
        List<Currency> page = currencyService.findAllByCompanyIDNull();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/currencies/active/default")
    @Timed
    public ResponseEntity<Currency> findActiveDefault() {
        log.debug("REST request to get a page of Currencies");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<Currency> page = currencyService.findActiveDefault(currentUserLoginAndOrg.get().getOrg());
        return ResponseUtil.wrapOrNotFound(page);
    }

    @GetMapping("/currencies/search-all-currency")
    @Timed
    public ResponseEntity<List<Currency>> searchAll(Pageable pageable,
                                                    @RequestParam(required = false) String searchVoucherCurrency
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucherCurrency searchVoucherCurrency1 = null;
        try {
            searchVoucherCurrency1 = objectMapper.readValue(searchVoucherCurrency, SearchVoucherCurrency.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<Currency> page = currencyService.findAll1(pageable, searchVoucherCurrency1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/currencies/find-currency-by-company-id")
    @Timed
    public ResponseEntity<List<Currency>> findCurrencyByCompanyID() {
        log.debug("REST request to get a page of Currencies");
        List<Currency> page = currencyService.findCurrencyByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
