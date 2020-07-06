package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;
import vn.softdreams.ebweb.service.PrepaidExpenseVoucherService;
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
 * REST controller for managing PrepaidExpenseVoucher.
 */
@RestController
@RequestMapping("/api")
public class PrepaidExpenseVoucherResource {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseVoucherResource.class);

    private static final String ENTITY_NAME = "prepaidExpenseVoucher";

    private final PrepaidExpenseVoucherService prepaidExpenseVoucherService;

    public PrepaidExpenseVoucherResource(PrepaidExpenseVoucherService prepaidExpenseVoucherService) {
        this.prepaidExpenseVoucherService = prepaidExpenseVoucherService;
    }

    /**
     * POST  /prepaid-expense-vouchers : Create a new prepaidExpenseVoucher.
     *
     * @param prepaidExpenseVoucher the prepaidExpenseVoucher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prepaidExpenseVoucher, or with status 400 (Bad Request) if the prepaidExpenseVoucher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prepaid-expense-vouchers")
    @Timed
    public ResponseEntity<PrepaidExpenseVoucher> createPrepaidExpenseVoucher(@Valid @RequestBody PrepaidExpenseVoucher prepaidExpenseVoucher) throws URISyntaxException {
        log.debug("REST request to save PrepaidExpenseVoucher : {}", prepaidExpenseVoucher);
        if (prepaidExpenseVoucher.getId() != null) {
            throw new BadRequestAlertException("A new prepaidExpenseVoucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaidExpenseVoucher result = prepaidExpenseVoucherService.save(prepaidExpenseVoucher);
        return ResponseEntity.created(new URI("/api/prepaid-expense-vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prepaid-expense-vouchers : Updates an existing prepaidExpenseVoucher.
     *
     * @param prepaidExpenseVoucher the prepaidExpenseVoucher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prepaidExpenseVoucher,
     * or with status 400 (Bad Request) if the prepaidExpenseVoucher is not valid,
     * or with status 500 (Internal Server Error) if the prepaidExpenseVoucher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prepaid-expense-vouchers")
    @Timed
    public ResponseEntity<PrepaidExpenseVoucher> updatePrepaidExpenseVoucher(@Valid @RequestBody PrepaidExpenseVoucher prepaidExpenseVoucher) throws URISyntaxException {
        log.debug("REST request to update PrepaidExpenseVoucher : {}", prepaidExpenseVoucher);
        if (prepaidExpenseVoucher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrepaidExpenseVoucher result = prepaidExpenseVoucherService.save(prepaidExpenseVoucher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prepaidExpenseVoucher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prepaid-expense-vouchers : get all the prepaidExpenseVouchers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prepaidExpenseVouchers in body
     */
    @GetMapping("/prepaid-expense-vouchers")
    @Timed
    public ResponseEntity<List<PrepaidExpenseVoucher>> getAllPrepaidExpenseVouchers(Pageable pageable) {
        log.debug("REST request to get a page of PrepaidExpenseVouchers");
        Page<PrepaidExpenseVoucher> page = prepaidExpenseVoucherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepaid-expense-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prepaid-expense-vouchers/:id : get the "id" prepaidExpenseVoucher.
     *
     * @param id the id of the prepaidExpenseVoucher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prepaidExpenseVoucher, or with status 404 (Not Found)
     */
    @GetMapping("/prepaid-expense-vouchers/{id}")
    @Timed
    public ResponseEntity<PrepaidExpenseVoucher> getPrepaidExpenseVoucher(@PathVariable UUID id) {
        log.debug("REST request to get PrepaidExpenseVoucher : {}", id);
        Optional<PrepaidExpenseVoucher> prepaidExpenseVoucher = prepaidExpenseVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaidExpenseVoucher);
    }

    /**
     * DELETE  /prepaid-expense-vouchers/:id : delete the "id" prepaidExpenseVoucher.
     *
     * @param id the id of the prepaidExpenseVoucher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prepaid-expense-vouchers/{id}")
    @Timed
    public ResponseEntity<Void> deletePrepaidExpenseVoucher(@PathVariable UUID id) {
        log.debug("REST request to delete PrepaidExpenseVoucher : {}", id);
        prepaidExpenseVoucherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
