package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;
import vn.softdreams.ebweb.service.IaPublishInvoiceDetailsService;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;
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

/**
 * REST controller for managing IaPublishInvoiceDetails.
 */
@RestController
@RequestMapping("/api")
public class IaPublishInvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(IaPublishInvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "iaPublishInvoiceDetails";

    private final IaPublishInvoiceDetailsService iaPublishInvoiceDetailsService;

    public IaPublishInvoiceDetailsResource(IaPublishInvoiceDetailsService iaPublishInvoiceDetailsService) {
        this.iaPublishInvoiceDetailsService = iaPublishInvoiceDetailsService;
    }

    /**
     * POST  /ia-publish-invoice-details : Create a new iaPublishInvoiceDetails.
     *
     * @param iaPublishInvoiceDetails the iaPublishInvoiceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iaPublishInvoiceDetails, or with status 400 (Bad Request) if the iaPublishInvoiceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ia-publish-invoice-details")
    @Timed
    public ResponseEntity<IaPublishInvoiceDetails> createIaPublishInvoiceDetails(@RequestBody IaPublishInvoiceDetails iaPublishInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to save IaPublishInvoiceDetails : {}", iaPublishInvoiceDetails);
        if (iaPublishInvoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new iaPublishInvoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IaPublishInvoiceDetails result = iaPublishInvoiceDetailsService.save(iaPublishInvoiceDetails);
        return ResponseEntity.created(new URI("/api/ia-publish-invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ia-publish-invoice-details : Updates an existing iaPublishInvoiceDetails.
     *
     * @param iaPublishInvoiceDetails the iaPublishInvoiceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iaPublishInvoiceDetails,
     * or with status 400 (Bad Request) if the iaPublishInvoiceDetails is not valid,
     * or with status 500 (Internal Server Error) if the iaPublishInvoiceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ia-publish-invoice-details")
    @Timed
    public ResponseEntity<IaPublishInvoiceDetails> updateIaPublishInvoiceDetails(@RequestBody IaPublishInvoiceDetails iaPublishInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to update IaPublishInvoiceDetails : {}", iaPublishInvoiceDetails);
        if (iaPublishInvoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IaPublishInvoiceDetails result = iaPublishInvoiceDetailsService.save(iaPublishInvoiceDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iaPublishInvoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ia-publish-invoice-details : get all the iaPublishInvoiceDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of iaPublishInvoiceDetails in body
     */
    @GetMapping("/ia-publish-invoice-details")
    @Timed
    public ResponseEntity<List<IaPublishInvoiceDetails>> getAllIaPublishInvoiceDetails() {
        log.debug("REST request to get a page of IaPublishInvoiceDetails");
        List<IaPublishInvoiceDetails> page = iaPublishInvoiceDetailsService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/ia-publish-invoice-details/get-by-company")
    @Timed
    public ResponseEntity<List<IAPublishInvoiceDetailDTO>> getAllByCompany() {
        log.debug("REST request to get a page of IaPublishInvoiceDetails");
        List<IAPublishInvoiceDetailDTO> page = iaPublishInvoiceDetailsService.getAllByCompany();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/ia-publish-invoice-details/get-follow-transfer-by-company")
    @Timed
    public ResponseEntity<List<IAPublishInvoiceDetailDTO>> getFollowTransferByCompany() {
        log.debug("REST request to get a page of IaPublishInvoiceDetails");
        List<IAPublishInvoiceDetailDTO> page = iaPublishInvoiceDetailsService.getFollowTransferByCompany();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    /**
     * GET  /ia-publish-invoice-details/:id : get the "id" iaPublishInvoiceDetails.
     *
     * @param id the id of the iaPublishInvoiceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iaPublishInvoiceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/ia-publish-invoice-details/{id}")
    @Timed
    public ResponseEntity<IaPublishInvoiceDetails> getIaPublishInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to get IaPublishInvoiceDetails : {}", id);
        Optional<IaPublishInvoiceDetails> iaPublishInvoiceDetails = iaPublishInvoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iaPublishInvoiceDetails);
    }

    /**
     * DELETE  /ia-publish-invoice-details/:id : delete the "id" iaPublishInvoiceDetails.
     *
     * @param id the id of the iaPublishInvoiceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ia-publish-invoice-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteIaPublishInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to delete IaPublishInvoiceDetails : {}", id);
        iaPublishInvoiceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
