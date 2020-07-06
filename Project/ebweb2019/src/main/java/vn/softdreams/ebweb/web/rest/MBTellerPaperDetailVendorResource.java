package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBTellerPaperDetailVendor;
import vn.softdreams.ebweb.service.MBTellerPaperDetailVendorService;
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
 * REST controller for managing MBTellerPaperDetailVendor.
 */
@RestController
@RequestMapping("/api")
public class MBTellerPaperDetailVendorResource {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailVendorResource.class);

    private static final String ENTITY_NAME = "mBTellerPaperDetailVendor";

    private final MBTellerPaperDetailVendorService mBTellerPaperDetailVendorService;

    public MBTellerPaperDetailVendorResource(MBTellerPaperDetailVendorService mBTellerPaperDetailVendorService) {
        this.mBTellerPaperDetailVendorService = mBTellerPaperDetailVendorService;
    }

    /**
     * POST  /mb-teller-paper-detail-vendors : Create a new mBTellerPaperDetailVendor.
     *
     * @param mBTellerPaperDetailVendor the mBTellerPaperDetailVendor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBTellerPaperDetailVendor, or with status 400 (Bad Request) if the mBTellerPaperDetailVendor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-teller-paper-detail-vendors")
    @Timed
    public ResponseEntity<MBTellerPaperDetailVendor> createMBTellerPaperDetailVendor(@RequestBody MBTellerPaperDetailVendor mBTellerPaperDetailVendor) throws URISyntaxException {
        log.debug("REST request to save MBTellerPaperDetailVendor : {}", mBTellerPaperDetailVendor);
        if (mBTellerPaperDetailVendor.getId() != null) {
            throw new BadRequestAlertException("A new mBTellerPaperDetailVendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBTellerPaperDetailVendor result = mBTellerPaperDetailVendorService.save(mBTellerPaperDetailVendor);
        return ResponseEntity.created(new URI("/api/mb-teller-paper-detail-vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-teller-paper-detail-vendors : Updates an existing mBTellerPaperDetailVendor.
     *
     * @param mBTellerPaperDetailVendor the mBTellerPaperDetailVendor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBTellerPaperDetailVendor,
     * or with status 400 (Bad Request) if the mBTellerPaperDetailVendor is not valid,
     * or with status 500 (Internal Server Error) if the mBTellerPaperDetailVendor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-teller-paper-detail-vendors")
    @Timed
    public ResponseEntity<MBTellerPaperDetailVendor> updateMBTellerPaperDetailVendor(@RequestBody MBTellerPaperDetailVendor mBTellerPaperDetailVendor) throws URISyntaxException {
        log.debug("REST request to update MBTellerPaperDetailVendor : {}", mBTellerPaperDetailVendor);
        if (mBTellerPaperDetailVendor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBTellerPaperDetailVendor result = mBTellerPaperDetailVendorService.save(mBTellerPaperDetailVendor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBTellerPaperDetailVendor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-teller-paper-detail-vendors : get all the mBTellerPaperDetailVendors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBTellerPaperDetailVendors in body
     */
    @GetMapping("/mb-teller-paper-detail-vendors")
    @Timed
    public ResponseEntity<List<MBTellerPaperDetailVendor>> getAllMBTellerPaperDetailVendors(Pageable pageable) {
        log.debug("REST request to get a page of MBTellerPaperDetailVendors");
        Page<MBTellerPaperDetailVendor> page = mBTellerPaperDetailVendorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-paper-detail-vendors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-teller-paper-detail-vendors/:id : get the "id" mBTellerPaperDetailVendor.
     *
     * @param id the id of the mBTellerPaperDetailVendor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaperDetailVendor, or with status 404 (Not Found)
     */
    @GetMapping("/mb-teller-paper-detail-vendors/{id}")
    @Timed
    public ResponseEntity<MBTellerPaperDetailVendor> getMBTellerPaperDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to get MBTellerPaperDetailVendor : {}", id);
        Optional<MBTellerPaperDetailVendor> mBTellerPaperDetailVendor = mBTellerPaperDetailVendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBTellerPaperDetailVendor);
    }

    /**
     * DELETE  /mb-teller-paper-detail-vendors/:id : delete the "id" mBTellerPaperDetailVendor.
     *
     * @param id the id of the mBTellerPaperDetailVendor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-teller-paper-detail-vendors/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBTellerPaperDetailVendor(@PathVariable UUID id) {
        log.debug("REST request to delete MBTellerPaperDetailVendor : {}", id);
        mBTellerPaperDetailVendorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
