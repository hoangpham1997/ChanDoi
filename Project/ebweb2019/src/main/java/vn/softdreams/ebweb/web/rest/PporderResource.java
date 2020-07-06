package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.service.PporderService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PPOrder.
 */
@RestController
@RequestMapping("/api")
public class PporderResource {

    private static final String ENTITY_NAME = "pporder";
    private final Logger log = LoggerFactory.getLogger(PporderResource.class);
    private final PporderService pporderService;

    @Autowired
    private UtilsRepository utilsRepository;

    public PporderResource(PporderService pporderService) {
        this.pporderService = pporderService;
    }

    /**
     * POST  /pporders : Create a new pporder.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new pporder, or with status 400 (Bad Request) if the pporder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pporders")
    @Timed
    public ResponseEntity<PPOrderSaveDTO> createPporder(@RequestBody PPOrderSaveDTO ppOrderSaveDTO) throws URISyntaxException {
        log.debug("REST request to save PPOrder : {}", ppOrderSaveDTO);
        if (ppOrderSaveDTO.getPpOrder().getId() != null) {
            throw new BadRequestAlertException("A new pporder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPOrderSaveDTO result = pporderService.save(ppOrderSaveDTO);
        utilsRepository.updateGencode(result.getPpOrder().getNo(), result.getPpOrder().getNo(), 20, 2);
        return ResponseEntity.created(new URI("/api/pporders/" + result.getPpOrder().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getPpOrder().getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pporders : Updates an existing pporder.
     *
     * @param ppOrderSaveDTO the pporder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pporder,
     * or with status 400 (Bad Request) if the pporder is not valid,
     * or with status 500 (Internal Server Error) if the pporder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pporders")
    @Timed
    public ResponseEntity<PPOrderSaveDTO> updatePporder(@RequestBody PPOrderSaveDTO ppOrderSaveDTO) throws URISyntaxException {
        log.debug("REST request to update PPOrderSaveDTO : {}", ppOrderSaveDTO);
        if (ppOrderSaveDTO.getPpOrder().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PPOrderSaveDTO result = pporderService.save(ppOrderSaveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ppOrderSaveDTO.getPpOrder().getId().toString()))
            .body(result);
    }

    /**
     * GET  /pporders : get all the pporders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pporders in body
     */
    @GetMapping("/pporders")
    @Timed
    public ResponseEntity<List<PPOrder>> getAllPporders(Pageable pageable) {
        log.debug("REST request to get a page of Pporders");
        Page<PPOrder> page = pporderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pporders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pporders/:id : get the "id" pporder.
     *
     * @param id the id of the pporder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pporder, or with status 404 (Not Found)
     */
    @GetMapping("/pporders/{id}")
    @Timed
    public ResponseEntity<PPOrder> getPporder(@PathVariable UUID id) {
        log.debug("REST request to get PPOrder : {}", id);
        Optional<PPOrder> pporder = pporderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pporder);
    }

    /**
     * DELETE  /pporders/:id : delete the "id" pporder.
     *
     * @param id the id of the pporder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pporders/{id}")
    @Timed
    public ResponseEntity<Void> deletePporder(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        pporderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /pporders/:id/valid : validate before delete.
     *
     * @param id the id of the pporder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pporders/{id}/validate")
    @Timed
    public ResponseEntity<ResultDTO> deletePporderValidate(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO dto = pporderService.validateDelete(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/pporders/{id}/validate-check-duplicate")
    @Timed
    public ResponseEntity<ResultDTO> deletePporderCheckDuplicat(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO dto = pporderService.validateCheckDuplicat(id);
        return ResponseEntity.ok().body(dto);
    }


    /**
     * @author dungvm
     * đếm số liên kết đến đơn mua hàng từ các màn khác
     */
    @GetMapping("/pporders/{id}/validate")
    @Timed
    public ResponseEntity<ResultDTO> checkReferencesCount(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO dto = pporderService.checkReferencesCount(id);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * DELETE  /pporders/:id/references : xóa pporder và id của nó ở các bảng tham chiếu
     *
     * @param id the id of the pporder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pporders/{id}/references")
    @Timed
    public ResponseEntity<ResultDTO> deletePPOrderAndReferences(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO dto = pporderService.deletePPOrderAndReferences(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/pporders/search-all")
    @Timed
    public ResponseEntity<List<PPOrder>> searchAll(Pageable pageable,
                                                   @RequestParam(required = false) String searchDTO
    ) throws IOException {
        log.debug("REST request to get a page of pporders");
        Page<PPOrder> page = pporderService.searchAll(pageable, searchDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/don-mua-hang");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/pporders/total-sum")
    @Timed
    public ResponseEntity<Number> findTotalSum(Pageable pageable,
                                               @RequestParam(required = false) String searchDTO) throws IOException {
        log.debug("REST request to findTotalSum");
        Number total = pporderService.findTotalSum(pageable, searchDTO);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @GetMapping("/pporders/export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) String searchDTO) throws IOException {
        byte[] export = pporderService.exportPdf(searchDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping("/pporders/export/excel")
    @Timed
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String searchDTO) throws IOException {
        byte[] export = pporderService.exportExcel(searchDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    /***
     *
     * @param accountingObject
     * @param fromDate
     * @param toDate
     * @param typeId material Goods Type: null : All
     * @return
     */
    @GetMapping("/pporders/search-all-dto")
    @Timed
    public ResponseEntity<Page<PPOrderDTO>> searchAllOrderDTO(Pageable pageable,
                                                              @RequestParam(required = false) UUID accountingObject,
                                                              @RequestParam(required = false) String fromDate,
                                                              @RequestParam(required = false) String toDate,
                                                              @RequestParam(required = false) Integer typeId,
                                                              @RequestParam(required = false) List<UUID> itemsSelected,
                                                              @RequestParam(required = false) String currency) {
        log.debug("REST request to getAll PP Order DTO");
        Page<PPOrderDTO> page = pporderService.findAllPPOrderDTO(pageable, accountingObject, fromDate, toDate, typeId, itemsSelected, currency);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @GetMapping("/pporders/index")
    @Timed
    public ResponseEntity<PPOrder> findByRowNum(@RequestParam(required = false) String searchDTO,
                                                @RequestParam(required = false) Integer rowNum
    ) throws IOException {
        log.debug("REST request to get a page of findByRowNum");
        PPOrder ppOrder = pporderService.findByRowNum(searchDTO, rowNum);
        return new ResponseEntity<>(ppOrder, HttpStatus.OK);
    }

    @GetMapping("/pporders/row-num")
    @Timed
    public ResponseEntity<Integer> findByRowNumByID(@RequestParam(required = false) String searchDTO,
                                                    @RequestParam(required = false) UUID id
    ) throws IOException {
        log.debug("REST request to get a page of findByRowNum");
        Integer rowNum = pporderService.findByRowNumByID(searchDTO, id);
        return new ResponseEntity<>(rowNum, HttpStatus.OK);
    }

    @GetMapping("/pporders/ref-voucher/{id}")
    @Timed
    public ResponseEntity<List<RefVoucherDTO>> refVouchersByPPOrderID(@PathVariable UUID id) {
        log.debug("REST request to get a page of refVouchersByPPOrderID");
        List<RefVoucherDTO> refVoucherDTOList = pporderService.refVouchersByPPOrderID(id);
        return new ResponseEntity<>(refVoucherDTOList, HttpStatus.OK);
    }

    /**
     * DELETE  /pporders/:id/delete-references : xóa pporder và id của nó ở các bảng tham chiếu
     */
    @DeleteMapping("/pporders/{id}/delete-references")
    @Timed
    public ResponseEntity<ResultDTO> deleteReferences(@PathVariable UUID id) {
        log.debug("REST request to delete PPOrder : {}", id);
        ResultDTO dto = pporderService.deleteReferences(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/pporders/multi-delete-pp-order")
    @Timed
    public ResponseEntity<HandlingResultDTO> multiDeletePPOrder(@Valid @RequestBody List<PPOrder> ppOrders) {
        log.debug("REST request to closeBook : {}", ppOrders);
        HandlingResultDTO responeCloseBookDTO = pporderService.multiDelete(ppOrders);
        return new ResponseEntity<>(responeCloseBookDTO, HttpStatus.OK);
    }

}
