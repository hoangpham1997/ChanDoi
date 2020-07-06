package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.InvoiceType;
import vn.softdreams.ebweb.service.InvoiceTypeService;
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
 * REST controller for managing InvoiceType.
 */
@RestController
@RequestMapping("/api")
public class InvoiceTypeResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceTypeResource.class);

    private static final String ENTITY_NAME = "invoiceType";

    private final InvoiceTypeService invoiceTypeService;

    public InvoiceTypeResource(InvoiceTypeService invoiceTypeService) {
        this.invoiceTypeService = invoiceTypeService;
    }

    /**
     * POST  /invoice-types : Create a new invoiceType.
     *
     * @param invoiceType the invoiceType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceType, or with status 400 (Bad Request) if the invoiceType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-types")
    @Timed
    public ResponseEntity<InvoiceType> createInvoiceType(@RequestBody InvoiceType invoiceType) throws URISyntaxException {
        log.debug("REST request to save InvoiceType : {}", invoiceType);
        if (invoiceType.getId() != null) {
            throw new BadRequestAlertException("A new invoiceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceType result = invoiceTypeService.save(invoiceType);
        return ResponseEntity.created(new URI("/api/invoice-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-types : Updates an existing invoiceType.
     *
     * @param invoiceType the invoiceType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceType,
     * or with status 400 (Bad Request) if the invoiceType is not valid,
     * or with status 500 (Internal Server Error) if the invoiceType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-types")
    @Timed
    public ResponseEntity<InvoiceType> updateInvoiceType(@RequestBody InvoiceType invoiceType) throws URISyntaxException {
        log.debug("REST request to update InvoiceType : {}", invoiceType);
        if (invoiceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceType result = invoiceTypeService.save(invoiceType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-types : get all the invoiceTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @GetMapping("/invoice-types")
    @Timed
    public ResponseEntity<List<InvoiceType>> getAllInvoiceTypes() {
        log.debug("REST request to get a page of InvoiceTypes");
        List<InvoiceType> page = invoiceTypeService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /invoice-types/:id : get the "id" invoiceType.
     *
     * @param id the id of the invoiceType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceType, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-types/{id}")
    @Timed
    public ResponseEntity<InvoiceType> getInvoiceType(@PathVariable UUID id) {
        log.debug("REST request to get InvoiceType : {}", id);
        Optional<InvoiceType> invoiceType = invoiceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceType);
    }

    /**
     * DELETE  /invoice-types/:id : delete the "id" invoiceType.
     *
     * @param id the id of the invoiceType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceType(@PathVariable UUID id) {
        log.debug("REST request to delete InvoiceType : {}", id);
        invoiceTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
