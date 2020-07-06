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
import vn.softdreams.ebweb.domain.PPInvoiceDetailCost;
import vn.softdreams.ebweb.service.PPInvoiceDetailCostService;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PPInvoiceDetailCostResource {
    private final Logger log = LoggerFactory.getLogger(PPInvoiceDetailCostResource.class);

    private static final String ENTITY_NAME = "ppInvoiceDetailCost";

    private final PPInvoiceDetailCostService pPInvoiceDetailCostService;

    public PPInvoiceDetailCostResource(PPInvoiceDetailCostService pPInvoiceDetailCostService) {
        this.pPInvoiceDetailCostService = pPInvoiceDetailCostService;
    }

    /**
     * POST  /pp-invoice-detail-cost : Create a new pPInvoiceDetails.
     *
     * @param ppInvoiceDetailCost the pPInvoiceDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pPInvoiceDetails, or with status 400 (Bad Request) if the pPInvoiceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pp-invoice-detail-cost")
    @Timed
    public ResponseEntity<PPInvoiceDetailCost> createPPInvoiceDetailCost(@Valid @RequestBody PPInvoiceDetailCost ppInvoiceDetailCost) throws URISyntaxException {
        log.debug("REST request to save PPInvoiceDetailCost : {}", ppInvoiceDetailCost);
        if (ppInvoiceDetailCost.getId() != null) {
            throw new BadRequestAlertException("A new pPInvoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPInvoiceDetailCost result = pPInvoiceDetailCostService.save(ppInvoiceDetailCost);
        return ResponseEntity.created(new URI("/api/pp-invoice-detail-cost/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /pp-invoice-detail-cost : Updates an existing pPInvoiceDetails.
     *
     * @param pPInvoiceDetails the pPInvoiceDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pPInvoiceDetails,
     * or with status 400 (Bad Request) if the pPInvoiceDetails is not valid,
     * or with status 500 (Internal Server Error) if the pPInvoiceDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pp-invoice-detail-cost")
    @Timed
    public ResponseEntity<PPInvoiceDetailCost> updatePPInvoiceDetailCost(@Valid @RequestBody PPInvoiceDetailCost pPInvoiceDetails) throws URISyntaxException {
        log.debug("REST request to update PPInvoiceDetailCost : {}", pPInvoiceDetails);
        if (pPInvoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPInvoiceDetailCost result = pPInvoiceDetailCostService.save(pPInvoiceDetails);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pPInvoiceDetails.getId().toString()))
                .body(result);
    }

    /**
     * GET  /pp-invoice-detail-cost : get all the pPInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pPInvoiceDetails in body
     */
    @GetMapping("/pp-invoice-detail-cost")
    @Timed
    public ResponseEntity<List<PPInvoiceDetailCost>> getAllPPInvoiceDetailCost(Pageable pageable) {
        log.debug("REST request to get a page of PPInvoiceDetailCost");
        Page<PPInvoiceDetailCost> page = pPInvoiceDetailCostService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pp-invoice-detail-cost");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pp-invoice-detail-cost/:id : get the "id" pPInvoiceDetails.
     *
     * @param id the id of the pPInvoiceDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pPInvoiceDetails, or with status 404 (Not Found)
     */
    @GetMapping("/pp-invoice-detail-cost/{id}")
    @Timed
    public ResponseEntity<PPInvoiceDetailCost> getPPInvoiceDetailCost(@PathVariable UUID id) {
        log.debug("REST request to get PPInvoiceDetailCost : {}", id);
        Optional<PPInvoiceDetailCost> pPInvoiceDetails = pPInvoiceDetailCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPInvoiceDetails);
    }

    /**
     * DELETE  /pp-invoice-detail-cost/:id : delete the "id" pPInvoiceDetails.
     *
     * @param id the id of the pPInvoiceDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pp-invoice-detail-cost/{id}")
    @Timed
    public ResponseEntity<Void> deletePPInvoiceDetailCost(@PathVariable UUID id) {
        log.debug("REST request to delete PPInvoiceDetailCost : {}", id);
        pPInvoiceDetailCostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/pp-invoice-detail-cost/get-by-ppinvoiceid")
    @Timed
    public ResponseEntity<List<PPInvoiceDetailCostDTO>> getPPInvoiceDetailCostByRefId(@RequestParam(required = false) UUID refId) {
        List<PPInvoiceDetailCostDTO> refVoucherDTOS = pPInvoiceDetailCostService.getPPInvoiceDetailCost(refId);
        return new ResponseEntity<>(refVoucherDTOS, HttpStatus.OK);
    }

    /**
     * @Author Hautv
     * @param paymentvoucherID
     * @return
     */
    @GetMapping("/pp-invoice-detail-cost/get-by-paymentvoucherid")
    @Timed
    public ResponseEntity<List<PPInvoiceDetailCostDTO>> getPPInvoiceDetailCostByPaymentVoucherID(@RequestParam(required = false) UUID paymentvoucherID) {
        List<PPInvoiceDetailCostDTO> refVoucherDTOS = pPInvoiceDetailCostService.getPPInvoiceDetailCostByPaymentVoucherID(paymentvoucherID);
        return new ResponseEntity<>(refVoucherDTOS, HttpStatus.OK);
    }

    /**
     * @author congnd
     * @return
     */
    @GetMapping("/pp-invoice-detail-cost/get-sum-amount-by-ppserviceid")
    @Timed
    public ResponseEntity<ResultDTO> getSumAmountByPPServiceId(@RequestParam UUID ppServiceId, @RequestParam(required = false) UUID ppInvoiceId, @RequestParam(required = false) Boolean isHaiQuan)
    {
        log.debug("REST request to get a page of PPDiscountReturns");
        ResultDTO resultDTO = pPInvoiceDetailCostService.getSumAmountByPPServiceId(ppServiceId, ppInvoiceId, isHaiQuan);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
