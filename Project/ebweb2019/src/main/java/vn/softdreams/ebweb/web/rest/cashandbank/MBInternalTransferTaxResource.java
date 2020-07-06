package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBInternalTransferTax;
import vn.softdreams.ebweb.service.MBInternalTransferTaxService;
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
 * REST controller for managing MBInternalTransferTax.
 */
@RestController
@RequestMapping("/api")
public class MBInternalTransferTaxResource {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferTaxResource.class);

    private static final String ENTITY_NAME = "mBInternalTransferTax";

    private final MBInternalTransferTaxService mBInternalTransferTaxService;

    public MBInternalTransferTaxResource(MBInternalTransferTaxService mBInternalTransferTaxService) {
        this.mBInternalTransferTaxService = mBInternalTransferTaxService;
    }

    /**
     * POST  /mb-internal-transfer-taxes : Create a new mBInternalTransferTax.
     *
     * @param mBInternalTransferTax the mBInternalTransferTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBInternalTransferTax, or with status 400 (Bad Request) if the mBInternalTransferTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-internal-transfer-taxes")
    @Timed
    public ResponseEntity<MBInternalTransferTax> createMBInternalTransferTax(@Valid @RequestBody MBInternalTransferTax mBInternalTransferTax) throws URISyntaxException {
        log.debug("REST request to save MBInternalTransferTax : {}", mBInternalTransferTax);
        if (mBInternalTransferTax.getId() != null) {
            throw new BadRequestAlertException("A new mBInternalTransferTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBInternalTransferTax result = mBInternalTransferTaxService.save(mBInternalTransferTax);
        return ResponseEntity.created(new URI("/api/mb-internal-transfer-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-internal-transfer-taxes : Updates an existing mBInternalTransferTax.
     *
     * @param mBInternalTransferTax the mBInternalTransferTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBInternalTransferTax,
     * or with status 400 (Bad Request) if the mBInternalTransferTax is not valid,
     * or with status 500 (Internal Server Error) if the mBInternalTransferTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-internal-transfer-taxes")
    @Timed
    public ResponseEntity<MBInternalTransferTax> updateMBInternalTransferTax(@Valid @RequestBody MBInternalTransferTax mBInternalTransferTax) throws URISyntaxException {
        log.debug("REST request to update MBInternalTransferTax : {}", mBInternalTransferTax);
        if (mBInternalTransferTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBInternalTransferTax result = mBInternalTransferTaxService.save(mBInternalTransferTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBInternalTransferTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-internal-transfer-taxes : get all the mBInternalTransferTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBInternalTransferTaxes in body
     */
    @GetMapping("/mb-internal-transfer-taxes")
    @Timed
    public ResponseEntity<List<MBInternalTransferTax>> getAllMBInternalTransferTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MBInternalTransferTaxes");
        Page<MBInternalTransferTax> page = mBInternalTransferTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-internal-transfer-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-internal-transfer-taxes/:id : get the "id" mBInternalTransferTax.
     *
     * @param id the id of the mBInternalTransferTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBInternalTransferTax, or with status 404 (Not Found)
     */
    @GetMapping("/mb-internal-transfer-taxes/{id}")
    @Timed
    public ResponseEntity<MBInternalTransferTax> getMBInternalTransferTax(@PathVariable UUID id) {
        log.debug("REST request to get MBInternalTransferTax : {}", id);
        Optional<MBInternalTransferTax> mBInternalTransferTax = mBInternalTransferTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBInternalTransferTax);
    }

    /**
     * DELETE  /mb-internal-transfer-taxes/:id : delete the "id" mBInternalTransferTax.
     *
     * @param id the id of the mBInternalTransferTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-internal-transfer-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBInternalTransferTax(@PathVariable UUID id) {
        log.debug("REST request to delete MBInternalTransferTax : {}", id);
        mBInternalTransferTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
