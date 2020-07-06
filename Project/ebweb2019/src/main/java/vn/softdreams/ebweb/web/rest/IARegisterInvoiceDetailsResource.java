package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.IARegisterInvoiceDetails;
import vn.softdreams.ebweb.service.IARegisterInvoiceDetailsService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing IARegisterInvoiceDetails.
 */
@RestController
@RequestMapping("/api")
public class IARegisterInvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(IARegisterInvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "iARegisterInvoiceDetails";

    private final IARegisterInvoiceDetailsService iARegisterInvoiceDetailsService;

    public IARegisterInvoiceDetailsResource(IARegisterInvoiceDetailsService iARegisterInvoiceDetailsService) {
        this.iARegisterInvoiceDetailsService = iARegisterInvoiceDetailsService;
    }

    /**
     * POST  /ia-register-invoice-details : Create a new iARegisterInvoiceDetails.
     *
     * @param iARegisterInvoiceDetails the iARegisterInvoiceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iARegisterInvoiceDetails, or with status 400 (Bad Request) if the iARegisterInvoiceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-register-invoice-details")
    @Timed
    public ResponseEntity<IARegisterInvoiceDetails> createIARegisterInvoiceDetails(@RequestBody IARegisterInvoiceDetails iARegisterInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to save IARegisterInvoiceDetails : {}", iARegisterInvoiceDetails);
        if (iARegisterInvoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new iARegisterInvoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IARegisterInvoiceDetails result = iARegisterInvoiceDetailsService.save(iARegisterInvoiceDetails);
        return ResponseEntity.created(new URI("/api/ia-register-invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-register-invoice-details : Updates an existing iARegisterInvoiceDetails.
     *
     * @param iARegisterInvoiceDetails the iARegisterInvoiceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iARegisterInvoiceDetails,
     * or with status 400 (Bad Request) if the iARegisterInvoiceDetails is not valid,
     * or with status 500 (Internal Server Error) if the iARegisterInvoiceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-register-invoice-details")
    @Timed
    public ResponseEntity<IARegisterInvoiceDetails> updateIARegisterInvoiceDetails(@RequestBody IARegisterInvoiceDetails iARegisterInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to update IARegisterInvoiceDetails : {}", iARegisterInvoiceDetails);
        if (iARegisterInvoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IARegisterInvoiceDetails result = iARegisterInvoiceDetailsService.save(iARegisterInvoiceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iARegisterInvoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-register-invoice-details : get all the iARegisterInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of iARegisterInvoiceDetails in body
     */
    @GetMapping("/ia-register-invoice-details")
    @Timed
    public ResponseEntity<List<IARegisterInvoiceDetails>> getAllIARegisterInvoiceDetails(Pageable pageable) {
        log.debug("REST request to get a page of IARegisterInvoiceDetails");
        Page<IARegisterInvoiceDetails> page = iARegisterInvoiceDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ia-register-invoice-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ia-register-invoice-details/:id : get the "id" iARegisterInvoiceDetails.
     *
     * @param id the id of the iARegisterInvoiceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iARegisterInvoiceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/ia-register-invoice-details/{id}")
    @Timed
    public ResponseEntity<IARegisterInvoiceDetails> getIARegisterInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to get IARegisterInvoiceDetails : {}", id);
        Optional<IARegisterInvoiceDetails> iARegisterInvoiceDetails = iARegisterInvoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iARegisterInvoiceDetails);
    }

    /**
     * DELETE  /ia-register-invoice-details/:id : delete the "id" iARegisterInvoiceDetails.
     *
     * @param id the id of the iARegisterInvoiceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-register-invoice-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteIARegisterInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to delete IARegisterInvoiceDetails : {}", id);
        iARegisterInvoiceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
