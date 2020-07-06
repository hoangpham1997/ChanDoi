package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBTellerPaperDetailTax;
import vn.softdreams.ebweb.service.MBTellerPaperDetailTaxService;
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
 * REST controller for managing MBTellerPaperDetailTax.
 */
@RestController
@RequestMapping("/api")
public class MBTellerPaperDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailTaxResource.class);

    private static final String ENTITY_NAME = "mBTellerPaperDetailTax";

    private final MBTellerPaperDetailTaxService mBTellerPaperDetailTaxService;

    public MBTellerPaperDetailTaxResource(MBTellerPaperDetailTaxService mBTellerPaperDetailTaxService) {
        this.mBTellerPaperDetailTaxService = mBTellerPaperDetailTaxService;
    }

    /**
     * POST  /mb-teller-paper-detail-taxes : Create a new mBTellerPaperDetailTax.
     *
     * @param mBTellerPaperDetailTax the mBTellerPaperDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBTellerPaperDetailTax, or with status 400 (Bad Request) if the mBTellerPaperDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mb-teller-paper-detail-taxes")
    @Timed
    public ResponseEntity<MBTellerPaperDetailTax> createMBTellerPaperDetailTax(@Valid @RequestBody MBTellerPaperDetailTax mBTellerPaperDetailTax) throws URISyntaxException {
        log.debug("REST request to save MBTellerPaperDetailTax : {}", mBTellerPaperDetailTax);
        if (mBTellerPaperDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new mBTellerPaperDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBTellerPaperDetailTax result = mBTellerPaperDetailTaxService.save(mBTellerPaperDetailTax);
        return ResponseEntity.created(new URI("/api/mb-teller-paper-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mb-teller-paper-detail-taxes : Updates an existing mBTellerPaperDetailTax.
     *
     * @param mBTellerPaperDetailTax the mBTellerPaperDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBTellerPaperDetailTax,
     * or with status 400 (Bad Request) if the mBTellerPaperDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the mBTellerPaperDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mb-teller-paper-detail-taxes")
    @Timed
    public ResponseEntity<MBTellerPaperDetailTax> updateMBTellerPaperDetailTax(@Valid @RequestBody MBTellerPaperDetailTax mBTellerPaperDetailTax) throws URISyntaxException {
        log.debug("REST request to update MBTellerPaperDetailTax : {}", mBTellerPaperDetailTax);
        if (mBTellerPaperDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBTellerPaperDetailTax result = mBTellerPaperDetailTaxService.save(mBTellerPaperDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBTellerPaperDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mb-teller-paper-detail-taxes : get all the mBTellerPaperDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBTellerPaperDetailTaxes in body
     */
    @GetMapping("/mb-teller-paper-detail-taxes")
    @Timed
    public ResponseEntity<List<MBTellerPaperDetailTax>> getAllMBTellerPaperDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MBTellerPaperDetailTaxes");
        Page<MBTellerPaperDetailTax> page = mBTellerPaperDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mb-teller-paper-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mb-teller-paper-detail-taxes/:id : get the "id" mBTellerPaperDetailTax.
     *
     * @param id the id of the mBTellerPaperDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBTellerPaperDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/mb-teller-paper-detail-taxes/{id}")
    @Timed
    public ResponseEntity<MBTellerPaperDetailTax> getMBTellerPaperDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get MBTellerPaperDetailTax : {}", id);
        Optional<MBTellerPaperDetailTax> mBTellerPaperDetailTax = mBTellerPaperDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBTellerPaperDetailTax);
    }

    /**
     * DELETE  /mb-teller-paper-detail-taxes/:id : delete the "id" mBTellerPaperDetailTax.
     *
     * @param id the id of the mBTellerPaperDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mb-teller-paper-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBTellerPaperDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete MBTellerPaperDetailTax : {}", id);
        mBTellerPaperDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
