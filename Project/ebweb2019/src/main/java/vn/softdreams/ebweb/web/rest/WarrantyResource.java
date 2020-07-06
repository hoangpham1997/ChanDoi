package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Warranty;
import vn.softdreams.ebweb.service.WarrantyService;
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
 * REST controller for managing Warranty.
 */
@RestController
@RequestMapping("/api")
public class WarrantyResource {

    private final Logger log = LoggerFactory.getLogger(WarrantyResource.class);

    private static final String ENTITY_NAME = "warranty";

    private final WarrantyService warrantyService;

    public WarrantyResource(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    /**
     * POST  /warranties : Create a new warranty.
     *
     * @param warranty the warranty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new warranty, or with status 400 (Bad Request) if the warranty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/warranties")
    @Timed
    public ResponseEntity<Warranty> createWarranty(@Valid @RequestBody Warranty warranty) throws URISyntaxException {
        log.debug("REST request to save Warranty : {}", warranty);
        if (warranty.getId() != null) {
            throw new BadRequestAlertException("A new warranty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Warranty result = warrantyService.save(warranty);
        return ResponseEntity.created(new URI("/api/warranties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /warranties : Updates an existing warranty.
     *
     * @param warranty the warranty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated warranty,
     * or with status 400 (Bad Request) if the warranty is not valid,
     * or with status 500 (Internal Server Error) if the warranty couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/warranties")
    @Timed
    public ResponseEntity<Warranty> updateWarranty(@Valid @RequestBody Warranty warranty) throws URISyntaxException {
        log.debug("REST request to update Warranty : {}", warranty);
        if (warranty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Warranty result = warrantyService.save(warranty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, warranty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /warranties : get all the warranties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of warranties in body
     */
    @GetMapping("/warranties")
    @Timed
    public ResponseEntity<List<Warranty>> getAllWarranties(Pageable pageable) {
        log.debug("REST request to get a page of Warranties");
        Page<Warranty> page = warrantyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/warranties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /warranties/:id : get the "id" warranty.
     *
     * @param id the id of the warranty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the warranty, or with status 404 (Not Found)
     */
    @GetMapping("/warranties/{id}")
    @Timed
    public ResponseEntity<Warranty> getWarranty(@PathVariable long id) {
        log.debug("REST request to get Warranty : {}", id);
        Optional<Warranty> warranty = warrantyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warranty);
    }

    /**
     * DELETE  /warranties/:id : delete the "id" warranty.
     *
     * @param id the id of the warranty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/warranties/{id}")
    @Timed
    public ResponseEntity<Void> deleteWarranty(@PathVariable long id) {
        log.debug("REST request to delete Warranty : {}", id);
        warrantyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    @GetMapping("/warranties/find-all-warranty-company-id")
    @Timed
    public ResponseEntity<List<Warranty>> getAllWarrantyByCompanyID() {
        log.debug("REST request to get a page of Warranty");
        List<Warranty> page = warrantyService.getAllWarrantyByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
