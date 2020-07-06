package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailTax;
import vn.softdreams.ebweb.service.GOtherVoucherDetailTaxService;
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
 * REST controller for managing GOtherVoucherDetailTax.
 */
@RestController
@RequestMapping("/api")
public class GOtherVoucherDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailTaxResource.class);

    private static final String ENTITY_NAME = "gOtherVoucherDetailTax";

    private final GOtherVoucherDetailTaxService gOtherVoucherDetailTaxService;

    public GOtherVoucherDetailTaxResource(GOtherVoucherDetailTaxService gOtherVoucherDetailTaxService) {
        this.gOtherVoucherDetailTaxService = gOtherVoucherDetailTaxService;
    }

    /**
     * POST  /g-other-voucher-detail-taxes : Create a new gOtherVoucherDetailTax.
     *
     * @param gOtherVoucherDetailTax the gOtherVoucherDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gOtherVoucherDetailTax, or with status 400 (Bad Request) if the gOtherVoucherDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/g-other-voucher-detail-taxes")
    @Timed
    public ResponseEntity<GOtherVoucherDetailTax> createGOtherVoucherDetailTax(@RequestBody GOtherVoucherDetailTax gOtherVoucherDetailTax) throws URISyntaxException {
        log.debug("REST request to save GOtherVoucherDetailTax : {}", gOtherVoucherDetailTax);
        if (gOtherVoucherDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new gOtherVoucherDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GOtherVoucherDetailTax result = gOtherVoucherDetailTaxService.save(gOtherVoucherDetailTax);
        return ResponseEntity.created(new URI("/api/g-other-voucher-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /g-other-voucher-detail-taxes : Updates an existing gOtherVoucherDetailTax.
     *
     * @param gOtherVoucherDetailTax the gOtherVoucherDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gOtherVoucherDetailTax,
     * or with status 400 (Bad Request) if the gOtherVoucherDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the gOtherVoucherDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/g-other-voucher-detail-taxes")
    @Timed
    public ResponseEntity<GOtherVoucherDetailTax> updateGOtherVoucherDetailTax(@RequestBody GOtherVoucherDetailTax gOtherVoucherDetailTax) throws URISyntaxException {
        log.debug("REST request to update GOtherVoucherDetailTax : {}", gOtherVoucherDetailTax);
        if (gOtherVoucherDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GOtherVoucherDetailTax result = gOtherVoucherDetailTaxService.save(gOtherVoucherDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gOtherVoucherDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /g-other-voucher-detail-taxes : get all the gOtherVoucherDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gOtherVoucherDetailTaxes in body
     */
    @GetMapping("/g-other-voucher-detail-taxes")
    @Timed
    public ResponseEntity<List<GOtherVoucherDetailTax>> getAllGOtherVoucherDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of GOtherVoucherDetailTaxes");
        Page<GOtherVoucherDetailTax> page = gOtherVoucherDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-voucher-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /g-other-voucher-detail-taxes/:id : get the "id" gOtherVoucherDetailTax.
     *
     * @param id the id of the gOtherVoucherDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gOtherVoucherDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/g-other-voucher-detail-taxes/{id}")
    @Timed
    public ResponseEntity<GOtherVoucherDetailTax> getGOtherVoucherDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get GOtherVoucherDetailTax : {}", id);
        Optional<GOtherVoucherDetailTax> gOtherVoucherDetailTax = gOtherVoucherDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gOtherVoucherDetailTax);
    }

    /**
     * DELETE  /g-other-voucher-detail-taxes/:id : delete the "id" gOtherVoucherDetailTax.
     *
     * @param id the id of the gOtherVoucherDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/g-other-voucher-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteGOtherVoucherDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete GOtherVoucherDetailTax : {}", id);
        gOtherVoucherDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
