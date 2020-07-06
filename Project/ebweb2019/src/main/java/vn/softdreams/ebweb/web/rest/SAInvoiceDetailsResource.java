package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.service.SAInvoiceDetailsService;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;
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
 * REST controller for managing SAInvoiceDetails.
 */
@RestController
@RequestMapping("/api")
public class SAInvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SAInvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "sAInvoiceDetails";

    private final SAInvoiceDetailsService sAInvoiceDetailsService;

    public SAInvoiceDetailsResource(SAInvoiceDetailsService sAInvoiceDetailsService) {
        this.sAInvoiceDetailsService = sAInvoiceDetailsService;
    }

    /**
     * POST  /sa-invoice-details : Create a new sAInvoiceDetails.
     *
     * @param sAInvoiceDetails the sAInvoiceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAInvoiceDetails, or with status 400 (Bad Request) if the sAInvoiceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sa-invoice-details")
    @Timed
    public ResponseEntity<SAInvoiceDetails> createSAInvoiceDetails(@RequestBody SAInvoiceDetails sAInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to save SAInvoiceDetails : {}", sAInvoiceDetails);
        if (sAInvoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new sAInvoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SAInvoiceDetails result = sAInvoiceDetailsService.save(sAInvoiceDetails);
        return ResponseEntity.created(new URI("/api/sa-invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sa-invoice-details : Updates an existing sAInvoiceDetails.
     *
     * @param sAInvoiceDetails the sAInvoiceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAInvoiceDetails,
     * or with status 400 (Bad Request) if the sAInvoiceDetails is not valid,
     * or with status 500 (Internal Server Error) if the sAInvoiceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sa-invoice-details")
    @Timed
    public ResponseEntity<SAInvoiceDetails> updateSAInvoiceDetails(@RequestBody SAInvoiceDetails sAInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to update SAInvoiceDetails : {}", sAInvoiceDetails);
        if (sAInvoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SAInvoiceDetails result = sAInvoiceDetailsService.save(sAInvoiceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sAInvoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sa-invoice-details : get all the sAInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAInvoiceDetails in body
     */
    @GetMapping("/sa-invoice-details")
    @Timed
    public ResponseEntity<List<SAInvoiceDetails>> getAllSAInvoiceDetails(Pageable pageable) {
        log.debug("REST request to get a page of SAInvoiceDetails");
        Page<SAInvoiceDetails> page = sAInvoiceDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoice-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sa-invoice-details/:id : get the "id" sAInvoiceDetails.
     *
     * @param id the id of the sAInvoiceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAInvoiceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sa-invoice-details/{id}")
    @Timed
    public ResponseEntity<SAInvoiceDetails> getSAInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to get SAInvoiceDetails : {}", id);
        Optional<SAInvoiceDetails> sAInvoiceDetails = sAInvoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAInvoiceDetails);
    }

    /**
     * DELETE  /sa-invoice-details/:id : delete the "id" sAInvoiceDetails.
     *
     * @param id the id of the sAInvoiceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sa-invoice-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAInvoiceDetails(@PathVariable UUID id) {
        log.debug("REST request to delete SAInvoiceDetails : {}", id);
        sAInvoiceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/sa-invoice-details/by-id")
    @Timed
    public ResponseEntity<List<SAInvoiceDetails>> getSAInvoiceDetailsByID(@RequestParam UUID sAInvoiceID) {
        log.debug("REST request to get a page of SAInvoiceDetails");
        List<SAInvoiceDetails> saInvoiceDetails = sAInvoiceDetailsService.getSAInvoiceDetailsByID(sAInvoiceID);
        return new ResponseEntity<>(saInvoiceDetails, HttpStatus.OK);
    }

    @GetMapping("/sa-invoice-details/by-mcreceipt-id")
    @Timed
    public ResponseEntity<List<SAInvoiceDetails>> getSAInvoiceDetailsByMCReceiptID(@RequestParam UUID mCReceiptID) {
        log.debug("REST request to get a page of SAInvoiceDetails");
        List<SAInvoiceDetails> saInvoiceDetails = sAInvoiceDetailsService.getSAInvoiceDetailsByMCReceiptID(mCReceiptID);
        return new ResponseEntity<>(saInvoiceDetails, HttpStatus.OK);
    }

    @GetMapping("/sa-invoice-details/by-mb-deposit-id")
    @Timed
    public ResponseEntity<List<SAInvoiceDetails>> getSAInvoiceDetailsByMCDepositID(@RequestParam UUID mBDepositID) {
        log.debug("REST request to get a page of SAInvoiceDetails");
        List<SAInvoiceDetails> saInvoiceDetails = sAInvoiceDetailsService.getSAInvoiceDetailsByMCDepositID(mBDepositID);
        return new ResponseEntity<>(saInvoiceDetails, HttpStatus.OK);
    }

    @GetMapping("/sa-invoice-details/details")
    @Timed
    public ResponseEntity<List<SAInvoiceDetailsOutWardDTO>> findAllDetailsById(@RequestParam List<UUID> id) {
        log.debug("REST request to get SaReturn : {}", id);
        List<SAInvoiceDetailsOutWardDTO> details = sAInvoiceDetailsService.findAllDetailsById(id);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
