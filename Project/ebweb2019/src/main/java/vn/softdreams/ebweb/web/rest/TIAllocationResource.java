package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.TIAdjustmentDetails;
import vn.softdreams.ebweb.domain.TIAllocation;
import vn.softdreams.ebweb.service.TIAllocationService;
import vn.softdreams.ebweb.service.dto.*;
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

import javax.tools.Tool;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing TIAllocation.
 */
@RestController
@RequestMapping("/api")
public class TIAllocationResource {

    private final Logger log = LoggerFactory.getLogger(TIAllocationResource.class);

    private static final String ENTITY_NAME = "tIAllocation";

    private final TIAllocationService tIAllocationService;

    public TIAllocationResource(TIAllocationService tIAllocationService) {
        this.tIAllocationService = tIAllocationService;
    }

    /**
     * POST  /t-i-allocations : Create a new tIAllocation.
     *
     * @param tIAllocation the tIAllocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tIAllocation, or with status 400 (Bad Request) if the tIAllocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/t-i-allocations")
    @Timed
    public ResponseEntity<TIAllocation> createTIAllocation(@RequestBody TIAllocationSaveConvertDTO tIAllocation) throws URISyntaxException {
        log.debug("REST request to save TIAllocation : {}", tIAllocation);
//        if (tIAllocation.getId() != null) {
//            throw new BadRequestAlertException("A new tIAllocation cannot already have an ID", ENTITY_NAME, "idexists");
//        }
        TIAllocation result = tIAllocationService.save(tIAllocation);
        return ResponseEntity.created(new URI("/api/t-i-allocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /t-i-allocations : Updates an existing tIAllocation.
     *
     * @param tIAllocation the tIAllocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tIAllocation,
     * or with status 400 (Bad Request) if the tIAllocation is not valid,
     * or with status 500 (Internal Server Error) if the tIAllocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/t-i-allocations")
    @Timed
    public ResponseEntity<TIAllocation> updateTIAllocation(@RequestBody TIAllocationSaveConvertDTO tIAllocation) throws URISyntaxException {
        log.debug("REST request to update TIAllocation : {}", tIAllocation);
//        if (tIAllocation.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
        TIAllocation result = tIAllocationService.save(tIAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tIAllocation.getTiAllocation().getId().toString()))
            .body(result);
    }

    /**
     * GET  /t-i-allocations : get all the tIAllocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIAllocations in body
     */
    @GetMapping("/t-i-allocations")
    @Timed
    public ResponseEntity<List<TIAllocation>> getAllTIAllocations(Pageable pageable) {
        log.debug("REST request to get a page of TIAllocations");
        Page<TIAllocation> page = tIAllocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-allocations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/t-i-allocations/count-tools-allocation")
    @Timed
    public ResponseEntity<Long> countToolsAllocation(@RequestParam(required = false) List<UUID> uuidList, @RequestParam(required = false) String date) {
        log.debug("REST request to get a page of TIAllocations");
        Long count = tIAllocationService.countToolsAllocation(uuidList, date);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-allocations");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * GET  /t-i-allocations/:id : get the "id" tIAllocation.
     *
     * @param id the id of the tIAllocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tIAllocation, or with status 404 (Not Found)
     */
    @GetMapping("/t-i-allocations/{id}")
    @Timed
    public ResponseEntity<TIAllocation> getTIAllocation(@PathVariable UUID id) {
        log.debug("REST request to get TIAllocation : {}", id);
        Optional<TIAllocation> tIAllocation = tIAllocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tIAllocation);
    }

    /**
     * DELETE  /t-i-allocations/:id : delete the "id" tIAllocation.
     *
     * @param id the id of the tIAllocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/t-i-allocations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTIAllocation(@PathVariable UUID id) {
        log.debug("REST request to delete TIAllocation : {}", id);
        tIAllocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/t-i-allocations/get-allocation-details")
    @Timed
    public ResponseEntity<List<TIAllocationDetailConvertDTO>> getTIAllocationDetails(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer date
        ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        List<TIAllocationDetailConvertDTO> prepaidExpenseCodes = tIAllocationService.getTIAllocationDetails(month, year, date);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

//    lấy dữ liệu khi thêm mới phân bổ ccdc
    @GetMapping("/t-i-allocations/prepaid-expense-allocation")
    @Timed
    public ResponseEntity<List<Tool>> getTIAllocations(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        List<Tool> prepaidExpenseCodes = tIAllocationService.getTIAllocations(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    @GetMapping("/t-i-allocations/ti-allocation-count")
    @Timed
    public ResponseEntity<Long> getTIAllocationCount(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        Long prepaidExpenseCodes = tIAllocationService.getTIAllocationCount(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }


    @GetMapping("/t-i-allocations/ti-allocation-duplicate")
    @Timed
    public ResponseEntity<TIAllocation> getTIAllocationDuplicate(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        log.debug("REST request to get a page of PrepaidExpenses");
        TIAllocation prepaidExpenseCodes = tIAllocationService.getTIAllocationDuplicate(month, year);
        return new ResponseEntity<>(prepaidExpenseCodes, HttpStatus.OK);
    }

    // xem xem từng dòng detail trong của chứng từ có tháng phát sinh phân bổ lớn nhất là bao nhiêu
    @GetMapping("/t-i-allocations/get-max-month/{id}")
    @Timed
    public ResponseEntity<LocalDate> getMaxMonth(@PathVariable UUID id) {
        log.debug("REST request to get a page of GOtherVouchers");
        LocalDate page = tIAllocationService.getMaxMonth(id);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/g-other-vouchers");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /t-i-increments : get all the tIIncrements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tIIncrements in body
     */
    @GetMapping("/t-i-allocations/load-all")
    @Timed
    public ResponseEntity<List<TIAllocationConvertDTO>> getAllTIAllocationSearch(Pageable pageable, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String keySearch) {
        log.debug("REST request to get a page of TIIncrements");
        Page<TIAllocationConvertDTO> page = tIAllocationService.getAllTIAllocationSearch(pageable, fromDate, toDate, keySearch);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/t-i-increments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
