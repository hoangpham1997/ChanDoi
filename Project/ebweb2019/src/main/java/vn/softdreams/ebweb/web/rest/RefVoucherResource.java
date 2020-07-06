package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.service.RefVoucherService;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing RefVoucher.
 */
@RestController
@RequestMapping("/api")
public class RefVoucherResource {

    private final Logger log = LoggerFactory.getLogger(RefVoucherResource.class);

    private static final String ENTITY_NAME = "refVoucher";

    private final RefVoucherService refVoucherService;

    public RefVoucherResource(RefVoucherService refVoucherService) {
        this.refVoucherService = refVoucherService;
    }

    /**
     * POST  /view-vouchers : Create a new refVoucher.
     *
     * @param refVoucher the refVoucher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new refVoucher, or with status 400 (Bad Request) if the refVoucher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/view-vouchers")
    @Timed
    public ResponseEntity<RefVoucher> createRefVoucher(@RequestBody RefVoucher refVoucher) throws URISyntaxException {
        log.debug("REST request to save RefVoucher : {}", refVoucher);
        if (refVoucher.getId() != null) {
            throw new BadRequestAlertException("A new refVoucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RefVoucher result = refVoucherService.save(refVoucher);
        return ResponseEntity.created(new URI("/api/view-vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /view-vouchers : Updates an existing refVoucher.
     *
     * @param refVoucher the refVoucher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated refVoucher,
     * or with status 400 (Bad Request) if the refVoucher is not valid,
     * or with status 500 (Internal Server Error) if the refVoucher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/view-vouchers")
    @Timed
    public ResponseEntity<RefVoucher> updateRefVoucher(@RequestBody RefVoucher refVoucher) throws URISyntaxException {
        log.debug("REST request to update RefVoucher : {}", refVoucher);
        if (refVoucher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RefVoucher result = refVoucherService.save(refVoucher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, refVoucher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /view-vouchers : get all the refVouchers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of refVouchers in body
     */
    @GetMapping("/view-vouchers")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> getAllRefVouchers(Pageable pageable,
                                                                 @RequestParam(required = false) Integer typeGroup,
                                                                 @RequestParam(required = false) Integer typeSearch,
                                                                 @RequestParam(required = false) Integer status,
                                                                 @RequestParam(required = false) String no,
                                                                 @RequestParam(required = false) String invoiceNo,
                                                                 @RequestParam(required = false) Boolean recorded,
                                                                 @RequestParam(required = false) String fromDate,
                                                                 @RequestParam(required = false) String toDate) {
        log.debug("REST request to get a page of RefVouchers");
        Page<RefVoucherDTO> page = refVoucherService.findAll(pageable, typeGroup, no, invoiceNo, recorded, fromDate,
            toDate, typeSearch, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/view-vouchers/find-by-ref-id")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> getAllRefVouchersByRefId(@RequestParam(required = false) UUID refId,
                                                                        @RequestParam(required = false) Integer currentBook) {
        List<RefVoucherDTO> refVoucherDTOS = refVoucherService.getRefViewVoucher(refId, currentBook);
        return new ResponseEntity<>(refVoucherDTOS, HttpStatus.OK);
    }

    /**
     * @Author Hautv
     * @param paymentVoucherID
     * @param currentBook
     * @return
     */
    @GetMapping("/view-vouchers/find-by-ref-paymentVoucherid")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> getAllRefVouchersByPaymentVoucherID(@RequestParam(required = false) UUID paymentVoucherID,
                                                                                   @RequestParam(required = false) Integer typeID,
                                                                                   @RequestParam(required = false) Integer currentBook) {
        List<RefVoucherDTO> refVoucherDTOS = refVoucherService.getRefViewVoucherByPaymentVoucherID(typeID, paymentVoucherID, currentBook);
        return new ResponseEntity<>(refVoucherDTOS, HttpStatus.OK);
    }

    @GetMapping("/view-vouchers/find-by-ref-id-ppinvoice")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> getAllRefVouchersByRefIdPPInvoice(@RequestParam(required = false) UUID refId) {
        List<RefVoucherDTO> refVoucherDTOS = refVoucherService.getRefViewVoucherPPinvoice(refId);
        return new ResponseEntity<>(refVoucherDTOS, HttpStatus.OK);
    }

    /**
     * GET  /view-vouchers/:id : get the "id" refVoucher.
     *
     * @param id the id of the refVoucher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the refVoucher, or with status 404 (Not Found)
     */
    @GetMapping("/view-vouchers/{id}")
    @Timed
    public ResponseEntity<RefVoucher> getRefVoucher(@PathVariable Long id) {
        log.debug("REST request to get RefVoucher : {}", id);
        Optional<RefVoucher> refVoucher = refVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refVoucher);
    }

    /**
     * DELETE  /view-vouchers/:id : delete the "id" refVoucher.
     *
     * @param id the id of the refVoucher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/view-vouchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteRefVoucher(@PathVariable Long id) {
        log.debug("REST request to delete RefVoucher : {}", id);
        refVoucherService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/view-vouchers/get-view-voucher-to-modal")
    @Timed
    public ResponseEntity<List<RefVoucherSecondDTO>> getViewVoucherToModal(Pageable pageable,
                                                                           @RequestParam(required = false) Integer typeGroup,
                                                                           @RequestParam(required = false) String fromDate,
                                                                           @RequestParam(required = false) String toDate) {
        log.debug("REST request to get a page of RefVouchers");
        Page<RefVoucherSecondDTO> page = refVoucherService.getViewVoucherToModal(pageable, typeGroup, fromDate,
            toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-vouchers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
