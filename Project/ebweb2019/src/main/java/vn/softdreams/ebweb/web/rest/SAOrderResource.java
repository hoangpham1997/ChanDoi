package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SAOrder;
import vn.softdreams.ebweb.service.SAOrderService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.SAOrderSaveDTO;
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
 * REST controller for managing SAOrder.
 */
@RestController
@RequestMapping("/api")
public class SAOrderResource {

    private final Logger log = LoggerFactory.getLogger(SAOrderResource.class);

    private static final String ENTITY_NAME = "sAOrder";

    private final SAOrderService sAOrderService;

    public SAOrderResource(SAOrderService sAOrderService) {
        this.sAOrderService = sAOrderService;
    }

    /**
     * POST  /s-a-orders : Create a new sAOrder.
     *
     * @param sAOrder the sAOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sAOrder, or with status 400 (Bad Request) if the sAOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/s-a-orders")
    @Timed
    public ResponseEntity<SAOrderSaveDTO> createSAOrder(@Valid @RequestBody SAOrder sAOrder) throws URISyntaxException {
        log.debug("REST request to save SAOrder : {}", sAOrder);
        if (sAOrder.getId() != null) {
            throw new BadRequestAlertException("A new sAOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SAOrderSaveDTO result = sAOrderService.saveDTO(sAOrder);
        if (result.getsAOrder().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/s-a-orders/" + result.getsAOrder().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getsAOrder().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /s-a-orders : Updates an existing sAOrder.
     *
     * @param sAOrder the sAOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sAOrder,
     * or with status 400 (Bad Request) if the sAOrder is not valid,
     * or with status 500 (Internal Server Error) if the sAOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/s-a-orders")
    @Timed
    public ResponseEntity<SAOrderSaveDTO> updateSAOrder(@Valid @RequestBody SAOrder sAOrder) throws URISyntaxException {
        log.debug("REST request to update SAOrder : {}", sAOrder);
        if (sAOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SAOrderSaveDTO result = sAOrderService.saveDTO(sAOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getsAOrder().getId().toString()))
            .body(result);
    }

    /**
     * GET  /s-a-orders : get all the sAOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAOrders in body
     */
    @GetMapping("/s-a-orders")
    @Timed
    public ResponseEntity<List<SAOrder>> getAllSAOrders(Pageable pageable) {
        log.debug("REST request to get a page of SAOrders");
        Page<SAOrder> page = sAOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/s-a-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /s-a-orders/:id : get the "id" sAOrder.
     *
     * @param id the id of the sAOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sAOrder, or with status 404 (Not Found)
     */
    @GetMapping("/s-a-orders/{id}")
    @Timed
    public ResponseEntity<SAOrder> getSAOrder(@PathVariable UUID id) {
        log.debug("REST request to get SAOrder : {}", id);
        Optional<SAOrder> sAOrder = sAOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sAOrder);
    }

    /**
     * DELETE  /s-a-orders/:id : delete the "id" sAOrder.
     *
     * @param id the id of the sAOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/s-a-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteSAOrder(@PathVariable UUID id) {
        log.debug("REST request to delete SAOrder : {}", id);
        sAOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /s-a-orders : get all the sAOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sAOrders in body
     */
    @GetMapping("/s-a-ordersDTO")
    @Timed
    public ResponseEntity<List<SAOrderDTO>> getAllSAOrdersDTO(Pageable pageable) {
        log.debug("REST request to get a page of SAOrders");
        Page<SAOrderDTO> page = sAOrderService.findAllDTOByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/s-a-ordersDTO");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/s-a-orders/ViewSAOrderDTO")
    @Timed
    public ResponseEntity<List<SAOrderDTO>> getViewSAOrderDTOPopup(Pageable pageable,
                                                                   @RequestParam(required = false) UUID accountingObjectID,
                                                                   @RequestParam(required = false) String fromDate,
                                                                   @RequestParam(required = false) String toDate,
                                                                   @RequestParam(required = false) String currencyID,
                                                                   @RequestParam(required = false) UUID objectId
    ) {
        log.debug("REST request to get a page of SAInvoicePopupDTO");
        Page<SAOrderDTO> page = sAOrderService.getViewSAOrderDTOPopup(pageable, accountingObjectID, fromDate, toDate, currencyID, objectId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sa-invoices/popup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/s-a-orders/check-relate-voucher")
    @Timed
    public ResponseEntity<Boolean> checkRelateVoucher(@RequestParam(required = true) UUID id) {
        log.debug("REST request to checkRelateVoucher SAOrder ");
        Boolean check = sAOrderService.checkRelateVoucher(id);
        return new ResponseEntity<Boolean>(check, HttpStatus.OK);
    }

    @PostMapping("/s-a-orders/delete-ref/{id}")
    @Timed
    public ResponseEntity<Boolean> deleteRefSAInvoiceAndRSInwardOutward(@PathVariable UUID id) {
        log.debug("REST request to sendMail EInvoice : {}", id);
        Boolean check = sAOrderService.deleteRefSAInvoiceAndRSInwardOutward(id);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    /**
     * add by Hautv
     * ghi sổ
     *
     * @param uuids
     * @return
     */
    @PostMapping("/s-a-orders/delete-list")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedger(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = sAOrderService.delete(uuids);
        // return new ResponseEntity<>(record, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }
}
