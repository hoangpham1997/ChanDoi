package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TIIncrementDetailRefVoucher;
import vn.softdreams.ebweb.service.TIIncrementDetailRefVoucherService;
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
 * REST controller for managing TIIncrementDetailRefVoucher.
 */
@RestController
@RequestMapping("/api")
public class TIIncrementDetailRefVoucherResource {

    private final Logger log = LoggerFactory.getLogger(TIIncrementDetailRefVoucherResource.class);

    private static final String ENTITY_NAME = "tIIncrementDetailRefVoucher";

    private final TIIncrementDetailRefVoucherService tIIncrementDetailRefVoucherService;

    public TIIncrementDetailRefVoucherResource(TIIncrementDetailRefVoucherService tIIncrementDetailRefVoucherService) {
        this.tIIncrementDetailRefVoucherService = tIIncrementDetailRefVoucherService;
    }

    /**
     * POST  /t-i-increment-detail-ref-vouchers : Create a new tIIncrementDetailRefVoucher.
     *
     * @param tIIncrementDetailRefVoucher the tIIncrementDetailRefVoucher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIIncrementDetailRefVoucher, or with status 400 (Bad Request) if the tIIncrementDetailRefVoucher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-increment-detail-ref-vouchers")
    @Timed
    public ResponseEntity<TIIncrementDetailRefVoucher> createTIIncrementDetailRefVoucher(@Valid @RequestBody TIIncrementDetailRefVoucher tIIncrementDetailRefVoucher) throws URISyntaxException {
        log.debug("REST request to save TIIncrementDetailRefVoucher : {}", tIIncrementDetailRefVoucher);
        if (tIIncrementDetailRefVoucher.getId() != null) {
            throw new BadRequestAlertException("A new tIIncrementDetailRefVoucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TIIncrementDetailRefVoucher result = tIIncrementDetailRefVoucherService.save(tIIncrementDetailRefVoucher);
        return ResponseEntity.created(new URI("/api/t-i-increment-detail-ref-vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-increment-detail-ref-vouchers : Updates an existing tIIncrementDetailRefVoucher.
     *
     * @param tIIncrementDetailRefVoucher the tIIncrementDetailRefVoucher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIIncrementDetailRefVoucher,
     * or with status 400 (Bad Request) if the tIIncrementDetailRefVoucher is not valid,
     * or with status 500 (Internal Server Error) if the tIIncrementDetailRefVoucher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-increment-detail-ref-vouchers")
    @Timed
    public ResponseEntity<TIIncrementDetailRefVoucher> updateTIIncrementDetailRefVoucher(@Valid @RequestBody TIIncrementDetailRefVoucher tIIncrementDetailRefVoucher) throws URISyntaxException {
        log.debug("REST request to update TIIncrementDetailRefVoucher : {}", tIIncrementDetailRefVoucher);
        if (tIIncrementDetailRefVoucher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TIIncrementDetailRefVoucher result = tIIncrementDetailRefVoucherService.save(tIIncrementDetailRefVoucher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIIncrementDetailRefVoucher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-increment-detail-ref-vouchers : get all the tIIncrementDetailRefVouchers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIIncrementDetailRefVouchers in body
     */
    @GetMapping("/t-i-increment-detail-ref-vouchers")
    @Timed
    public ResponseEntity<List<TIIncrementDetailRefVoucher>> getAllTIIncrementDetailRefVouchers(Pageable pageable) {
        log.debug("REST request to get a page of TIIncrementDetailRefVouchers");
        Page<TIIncrementDetailRefVoucher> page = tIIncrementDetailRefVoucherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increment-detail-ref-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /t-i-increment-detail-ref-vouchers/:id : get the "id" tIIncrementDetailRefVoucher.
     *
     * @param id the id of the tIIncrementDetailRefVoucher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIIncrementDetailRefVoucher, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-increment-detail-ref-vouchers/{id}")
    @Timed
    public ResponseEntity<TIIncrementDetailRefVoucher> getTIIncrementDetailRefVoucher(@PathVariable UUID id) {
        log.debug("REST request to get TIIncrementDetailRefVoucher : {}", id);
        Optional<TIIncrementDetailRefVoucher> tIIncrementDetailRefVoucher = tIIncrementDetailRefVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIIncrementDetailRefVoucher);
    }

    /**
     * DELETE  /t-i-increment-detail-ref-vouchers/:id : delete the "id" tIIncrementDetailRefVoucher.
     *
     * @param id the id of the tIIncrementDetailRefVoucher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-increment-detail-ref-vouchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIIncrementDetailRefVoucher(@PathVariable UUID id) {
        log.debug("REST request to delete TIIncrementDetailRefVoucher : {}", id);
        tIIncrementDetailRefVoucherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
