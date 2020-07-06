package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCReceiptDetailTax;
import vn.softdreams.ebweb.service.MCReceiptDetailTaxService;
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
 * REST controller for managing MCReceiptDetailTax.
 */
@RestController
@RequestMapping("/api")
public class MCReceiptDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailTaxResource.class);

    private static final String ENTITY_NAME = "mCReceiptDetailTax";

    private final MCReceiptDetailTaxService mCReceiptDetailTaxService;

    public MCReceiptDetailTaxResource(MCReceiptDetailTaxService mCReceiptDetailTaxService) {
        this.mCReceiptDetailTaxService = mCReceiptDetailTaxService;
    }

    /**
     * POST  /m-c-receipt-detail-taxes : Create a new mCReceiptDetailTax.
     *
     * @param mCReceiptDetailTax the mCReceiptDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCReceiptDetailTax, or with status 400 (Bad Request) if the mCReceiptDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-c-receipt-detail-taxes")
    @Timed
    public ResponseEntity<MCReceiptDetailTax> createMCReceiptDetailTax(@Valid @RequestBody MCReceiptDetailTax mCReceiptDetailTax) throws URISyntaxException {
        log.debug("REST request to save MCReceiptDetailTax : {}", mCReceiptDetailTax);
        if (mCReceiptDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new mCReceiptDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCReceiptDetailTax result = mCReceiptDetailTaxService.save(mCReceiptDetailTax);
        return ResponseEntity.created(new URI("/api/m-c-receipt-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-c-receipt-detail-taxes : Updates an existing mCReceiptDetailTax.
     *
     * @param mCReceiptDetailTax the mCReceiptDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCReceiptDetailTax,
     * or with status 400 (Bad Request) if the mCReceiptDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the mCReceiptDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-c-receipt-detail-taxes")
    @Timed
    public ResponseEntity<MCReceiptDetailTax> updateMCReceiptDetailTax(@Valid @RequestBody MCReceiptDetailTax mCReceiptDetailTax) throws URISyntaxException {
        log.debug("REST request to update MCReceiptDetailTax : {}", mCReceiptDetailTax);
        if (mCReceiptDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCReceiptDetailTax result = mCReceiptDetailTaxService.save(mCReceiptDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCReceiptDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-c-receipt-detail-taxes : get all the mCReceiptDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCReceiptDetailTaxes in body
     */
    @GetMapping("/m-c-receipt-detail-taxes")
    @Timed
    public ResponseEntity<List<MCReceiptDetailTax>> getAllMCReceiptDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MCReceiptDetailTaxes");
        Page<MCReceiptDetailTax> page = mCReceiptDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-c-receipt-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-c-receipt-detail-taxes/:id : get the "id" mCReceiptDetailTax.
     *
     * @param id the id of the mCReceiptDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCReceiptDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/m-c-receipt-detail-taxes/{id}")
    @Timed
    public ResponseEntity<MCReceiptDetailTax> getMCReceiptDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get MCReceiptDetailTax : {}", id);
        Optional<MCReceiptDetailTax> mCReceiptDetailTax = mCReceiptDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCReceiptDetailTax);
    }

    /**
     * DELETE  /m-c-receipt-detail-taxes/:id : delete the "id" mCReceiptDetailTax.
     *
     * @param id the id of the mCReceiptDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-c-receipt-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCReceiptDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete MCReceiptDetailTax : {}", id);
        mCReceiptDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
