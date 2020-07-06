package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.CPResult;
import vn.softdreams.ebweb.domain.RepositoryLedger;
import vn.softdreams.ebweb.service.RepositoryLedgerService;
import vn.softdreams.ebweb.web.rest.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing RepositoryLedger.
 */
@RestController
@RequestMapping("/api")
public class RepositoryLedgerResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryLedgerResource.class);

    private static final String ENTITY_NAME = "repositoryLedger";

    private final RepositoryLedgerService repositoryLedgerService;

    public RepositoryLedgerResource(RepositoryLedgerService repositoryLedgerService) {
        this.repositoryLedgerService = repositoryLedgerService;
    }

    /**
     * POST  /repository-ledgers : Create a new repositoryLedger.
     *
     * @param repositoryLedger the repositoryLedger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repositoryLedger, or with status 400 (Bad Request) if the repositoryLedger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repository-ledgers")
    @Timed
    public ResponseEntity<RepositoryLedger> createRepositoryLedger(@Valid @RequestBody RepositoryLedger repositoryLedger) throws URISyntaxException {
        log.debug("REST request to save RepositoryLedger : {}", repositoryLedger);
        if (repositoryLedger.getId() != null) {
            throw new BadRequestAlertException("A new repositoryLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepositoryLedger result = repositoryLedgerService.save(repositoryLedger);
        return ResponseEntity.created(new URI("/api/repository-ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repository-ledgers : Updates an existing repositoryLedger.
     *
     * @param repositoryLedger the repositoryLedger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repositoryLedger,
     * or with status 400 (Bad Request) if the repositoryLedger is not valid,
     * or with status 500 (Internal Server Error) if the repositoryLedger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repository-ledgers")
    @Timed
    public ResponseEntity<RepositoryLedger> updateRepositoryLedger(@Valid @RequestBody RepositoryLedger repositoryLedger) throws URISyntaxException {
        log.debug("REST request to update RepositoryLedger : {}", repositoryLedger);
        if (repositoryLedger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RepositoryLedger result = repositoryLedgerService.save(repositoryLedger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, repositoryLedger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repository-ledgers : get all the repositoryLedgers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of repositoryLedgers in body
     */
    @GetMapping("/repository-ledgers")
    @Timed
    public ResponseEntity<List<RepositoryLedger>> getAllRepositoryLedgers(Pageable pageable) {
        log.debug("REST request to get a page of RepositoryLedgers");
        Page<RepositoryLedger> page = repositoryLedgerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repository-ledgers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /repository-ledgers/:id : get the "id" repositoryLedger.
     *
     * @param id the id of the repositoryLedger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repositoryLedger, or with status 404 (Not Found)
     */
    @GetMapping("/repository-ledgers/{id}")
    @Timed
    public ResponseEntity<RepositoryLedger> getRepositoryLedger(@PathVariable UUID id) {
        log.debug("REST request to get RepositoryLedger : {}", id);
        Optional<RepositoryLedger> repositoryLedger = repositoryLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repositoryLedger);
    }

    /**
     * DELETE  /repository-ledgers/:id : delete the "id" repositoryLedger.
     *
     * @param id the id of the repositoryLedger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repository-ledgers/{id}")
    @Timed
    public ResponseEntity<Void> deleteRepositoryLedger(@PathVariable UUID id) {
        log.debug("REST request to delete RepositoryLedger : {}", id);
        repositoryLedgerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * Add by chuongnv
     * lấy danh sách số lô theo mã hàng
     *
     * @param materialGoodsID
     * @return
     */
    @GetMapping("/repository-ledgers/getListLotNoByMaterialGoodsID")
    @Timed
    public ResponseEntity<List<LotNoDTO>> getCollectionVoucher(@RequestParam UUID materialGoodsID) {
        List<LotNoDTO> listLotNo = repositoryLedgerService.getListLotNoByMaterialGoodsID(materialGoodsID);
        return new ResponseEntity<>(listLotNo, HttpStatus.OK);
    }

    /**
     * Add by chuongnv
     * tính giá xuất kho
     *
     * @return
     */
    @PostMapping("/repository-ledgers/calculate_OWPrice")
    @Timed
    public ResponseEntity<ResultCalculateOWDTO> calculateOWPrice(@RequestBody CalculateOWDTO calculateOWDTO) {
        ResultCalculateOWDTO result = repositoryLedgerService.calculateOWPrice(calculateOWDTO.getCalculationMethod(), calculateOWDTO.getMaterialGoods(), calculateOWDTO.getFromDate(), calculateOWDTO.getToDate());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Add by chuongnv
     * tính giá xuất kho
     *
     * @return
     */
    @PostMapping("/repository-ledgers/update_IW_PriceFromCost")
    @Timed
    public ResponseEntity<Boolean> updateIWPriceFromCost(@RequestBody List<CPResult> cpResults) {
        Boolean result = repositoryLedgerService.updateIWPriceFromCost(cpResults);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/repository-ledgers/update_OW_PriceFromCost")
    @Timed
    public ResponseEntity<Boolean> updateOWPriceFromCost(@RequestBody List<CPResult> cpResults) {
        Boolean result = repositoryLedgerService.updateOWPriceFromCost(cpResults);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/repository-ledgers/get_IWVoucher")
    @Timed
    public ResponseEntity<List<IWVoucherDTO>> getIWVoucher(Pageable pageable,
                                                           @RequestParam(required = false) String fromDate,
                                                           @RequestParam(required = false) String toDate,
                                                           @RequestParam(required = false) UUID objectId) {
        Page<IWVoucherDTO> page = repositoryLedgerService.getIWVoucher(pageable, fromDate, toDate, objectId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repository-ledgers/get_IWVoucher");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @author Huy Xoăn
     * @param materialGoodsID
     * @return
     */
    @GetMapping("/repository-ledgers/lot-no-array/{materialGoodsID}")
    @Timed
    public ResponseEntity<List<LotNoDTO>> getLotNoArray(Pageable pageable, @PathVariable UUID materialGoodsID) {
        log.debug("REST request to get a page of RepositoryLedger");
        List<LotNoDTO> LotNo = repositoryLedgerService.getListLotNo(materialGoodsID);
        return new ResponseEntity<>(LotNo, HttpStatus.OK);
    }
}
