package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.Supplier;
import vn.softdreams.ebweb.service.SupplierService;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Supplier.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    private static final String ENTITY_NAME = "Supplier";

    private final SupplierService supplierService;

    public SupplierResource(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * POST  /Suppliers : Create a new Supplier.
     *
     * @param supplier the Supplier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new Supplier, or with status 400 (Bad Request) if the Supplier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/e-invoices/suppliers")
    @Timed
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            throw new BadRequestAlertException("A new Supplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.created(new URI("/api/Suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /Suppliers : Updates an existing Supplier.
     *
     * @param supplier the Supplier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated Supplier,
     * or with status 400 (Bad Request) if the Supplier is not valid,
     * or with status 500 (Internal Server Error) if the Supplier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/e-invoices/suppliers")
    @Timed
    public ResponseEntity<Supplier> updateSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplier);
        if (supplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, supplier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /Suppliers : get all the Suppliers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of Suppliers in body
     */
    @GetMapping("/e-invoices/suppliers")
    @Timed
    public ResponseEntity<List<Supplier>> getAllSuppliers(Pageable pageable) {
        log.debug("REST request to get a page of Suppliers");
        List<Supplier> page = supplierService.findAll();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /Suppliers/:id : get the "id" Supplier.
     *
     * @param id the id of the Supplier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the Supplier, or with status 404 (Not Found)
     */
    @GetMapping("/e-invoices/suppliers/{id}")
    @Timed
    public ResponseEntity<Supplier> getSupplier(@PathVariable UUID id) {
        log.debug("REST request to get Supplier : {}", id);
        Optional<Supplier> Supplier = supplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Supplier);
    }

    /**
     * DELETE  /Suppliers/:id : delete the "id" Supplier.
     *
     * @param id the id of the Supplier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/e-invoices/suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
