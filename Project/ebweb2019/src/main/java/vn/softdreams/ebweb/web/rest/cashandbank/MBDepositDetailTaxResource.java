package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MBDepositDetailTax;
import vn.softdreams.ebweb.service.MBDepositDetailTaxService;
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
 * REST controller for managing MBDepositDetailTax.
 */
@RestController
@RequestMapping("/api")
public class MBDepositDetailTaxResource {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailTaxResource.class);

    private static final String ENTITY_NAME = "mBDepositDetailTax";

    private final MBDepositDetailTaxService mBDepositDetailTaxService;

    public MBDepositDetailTaxResource(MBDepositDetailTaxService mBDepositDetailTaxService) {
        this.mBDepositDetailTaxService = mBDepositDetailTaxService;
    }

    /**
     * POST  /m-b-deposit-detail-taxes : Create a new mBDepositDetailTax.
     *
     * @param mBDepositDetailTax the mBDepositDetailTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mBDepositDetailTax, or with status 400 (Bad Request) if the mBDepositDetailTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/m-b-deposit-detail-taxes")
    @Timed
    public ResponseEntity<MBDepositDetailTax> createMBDepositDetailTax(@Valid @RequestBody MBDepositDetailTax mBDepositDetailTax) throws URISyntaxException {
        log.debug("REST request to save MBDepositDetailTax : {}", mBDepositDetailTax);
        if (mBDepositDetailTax.getId() != null) {
            throw new BadRequestAlertException("A new mBDepositDetailTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MBDepositDetailTax result = mBDepositDetailTaxService.save(mBDepositDetailTax);
        return ResponseEntity.created(new URI("/api/m-b-deposit-detail-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /m-b-deposit-detail-taxes : Updates an existing mBDepositDetailTax.
     *
     * @param mBDepositDetailTax the mBDepositDetailTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mBDepositDetailTax,
     * or with status 400 (Bad Request) if the mBDepositDetailTax is not valid,
     * or with status 500 (Internal Server Error) if the mBDepositDetailTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/m-b-deposit-detail-taxes")
    @Timed
    public ResponseEntity<MBDepositDetailTax> updateMBDepositDetailTax(@Valid @RequestBody MBDepositDetailTax mBDepositDetailTax) throws URISyntaxException {
        log.debug("REST request to update MBDepositDetailTax : {}", mBDepositDetailTax);
        if (mBDepositDetailTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MBDepositDetailTax result = mBDepositDetailTaxService.save(mBDepositDetailTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mBDepositDetailTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /m-b-deposit-detail-taxes : get all the mBDepositDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mBDepositDetailTaxes in body
     */
    @GetMapping("/m-b-deposit-detail-taxes")
    @Timed
    public ResponseEntity<List<MBDepositDetailTax>> getAllMBDepositDetailTaxes(Pageable pageable) {
        log.debug("REST request to get a page of MBDepositDetailTaxes");
        Page<MBDepositDetailTax> page = mBDepositDetailTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/m-b-deposit-detail-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /m-b-deposit-detail-taxes/:id : get the "id" mBDepositDetailTax.
     *
     * @param id the id of the mBDepositDetailTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mBDepositDetailTax, or with status 404 (Not Found)
     */
    @GetMapping("/m-b-deposit-detail-taxes/{id}")
    @Timed
    public ResponseEntity<MBDepositDetailTax> getMBDepositDetailTax(@PathVariable UUID id) {
        log.debug("REST request to get MBDepositDetailTax : {}", id);
        Optional<MBDepositDetailTax> mBDepositDetailTax = mBDepositDetailTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mBDepositDetailTax);
    }

    /**
     * DELETE  /m-b-deposit-detail-taxes/:id : delete the "id" mBDepositDetailTax.
     *
     * @param id the id of the mBDepositDetailTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/m-b-deposit-detail-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMBDepositDetailTax(@PathVariable UUID id) {
        log.debug("REST request to delete MBDepositDetailTax : {}", id);
        mBDepositDetailTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
