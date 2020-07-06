package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.domain.SearchVoucherMaterialGoods;
import vn.softdreams.ebweb.service.MaterialGoodsService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodSaveDTO;
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
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.service.MaterialGoodsService;
import vn.softdreams.ebweb.service.dto.MGForPPOrderDTO;
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
 * REST controller for managing MaterialGoods.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsResource.class);

    private static final String ENTITY_NAME = "materialGoods";

    private final MaterialGoodsService materialGoodsService;

    public MaterialGoodsResource(MaterialGoodsService materialGoodsService) {
        this.materialGoodsService = materialGoodsService;
    }

    /**
     * POST  /material-goods : Create a new materialGoods.
     *
     * @param materialGoods the materialGoods to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoods, or with status 400 (Bad Request) if the materialGoods has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods")
    @Timed
//    public ResponseEntity<MaterialGoods> createMaterialGoods(@Valid @RequestBody MaterialGoods materialGoods) throws URISyntaxException {
//        log.debug("REST request to save MaterialGoods : {}", materialGoods);
//        if (materialGoods.getId() != null) {
//            throw new BadRequestAlertException("A new materialGoods cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        MaterialGoodSaveDTO result = materialGoodsService.save(materialGoods);
//        return ResponseEntity.created(new URI("/api/material-goods/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    public ResponseEntity<MaterialGoodSaveDTO> createMaterialGoods(@Valid @RequestBody MaterialGoods materialGoods) throws URISyntaxException {
        log.debug("REST request to save MaterialGoods : {}", materialGoods);
        if (materialGoods.getId() != null) {
            throw new BadRequestAlertException("A new MaterialGoods cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodSaveDTO result = materialGoodsService.save(materialGoods);
        if (result.getMaterialGoods().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * PUT  /material-goods : Updates an existing materialGoods.
     *
     * @param materialGoods the materialGoods to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoods,
     * or with status 400 (Bad Request) if the materialGoods is not valid,
     * or with status 500 (Internal Server Error) if the materialGoods couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods")
    @Timed
    public ResponseEntity<MaterialGoodSaveDTO> updateMaterialGoods(@Valid @RequestBody MaterialGoods materialGoods) throws URISyntaxException {
        log.debug("REST request to update MaterialGoods : {}", materialGoods);
        if (materialGoods.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodSaveDTO result = materialGoodsService.save(materialGoods);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoods.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods : get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoods in body
     */
    @GetMapping("/material-goods")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getAllMaterialGoods(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoods");
        Page<MaterialGoods> page = materialGoodsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods : get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoods in body
     */
    @GetMapping("/material-goods/get-all-by-company-id")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getAllMaterialGoodsByCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoods");
        Page<MaterialGoods> page = materialGoodsService.getAllByCompanyID(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods/get-all-by-company-id");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /material-goods : get all the materialGoods.
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoods in body
     */
    @PostMapping("/material-goods/get-all-by-quantity-exists")
    @Timed
    public ResponseEntity<List<MGForPPOrderConvertQuantityDTO>> getQuantityExistsTest(
        @RequestBody ParamQuantityDTO paramQuantityDTO) {
        log.debug("REST request to get a page of MaterialGoods");
        List<MGForPPOrderConvertQuantityDTO> page = materialGoodsService.getQuantityExistsTest(paramQuantityDTO.getMaterialGoodsIDs(),
            paramQuantityDTO.getRepositoryIDs(), paramQuantityDTO.getPostedDate());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/material-goods/material-good-and-repository")
    @Timed
    public ResponseEntity<ListObjectDTO> getMaterialGoodAndRepository(@RequestBody ParamQuantityDTO paramQuantityDTO) {
        log.debug("REST request to get a page of MaterialGoods");
        ListObjectDTO page = materialGoodsService.getMaterialGoodAndRepository(paramQuantityDTO.getMaterialGoodsIDs(), paramQuantityDTO.getRepositoryIDs());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /material-goods : get all the materialGoods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoods in body
     */
    @GetMapping("/material-goods/combobox")
    @Timed
    public ResponseEntity<List<MGForPPOrderDTO>> getMaterialGoodsForCombobox() {
        log.debug("REST request to get a page of MaterialGoods for PPOrder");
        List<MGForPPOrderDTO> page = materialGoodsService.getMaterialGoodsForCombobox();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /material-goods/:id : get the "id" materialGoods.
     *
     * @param id the id of the materialGoods to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoods, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods/{id}")
    @Timed
    public ResponseEntity<MaterialGoods> getMaterialGoods(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoods : {}", id);
        Optional<MaterialGoods> materialGoods = materialGoodsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoods);
    }

    @GetMapping("/material-goods/getVoucherByMaterialGoodsID")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getVoucherByMaterialGoodsID(Pageable pageable, @RequestParam UUID id) {
        log.debug("REST request to get MaterialGoods : {}", id);
        Page<MaterialGoods> page = materialGoodsService.findVoucherByMaterialGoodsID(pageable,id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods/getVoucherByMaterialGoodsID");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /material-goods/:id : delete the "id" materialGoods.
     *
     * @param id the id of the materialGoods to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoods(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoods : {}", id);
        materialGoodsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /material-goods : get all the materialGoods.
     *
     * @param materialsGoodsType author phuong hv
     * @return the ResponseEntity with status 200  and the list of materialGoods in body
     */
    @GetMapping("/material-goods/combobox-good")
    @Timed
    public ResponseEntity<List<MGForPPOrderConvertDTO>> getMaterialGoodsForCombobox1(@RequestParam(required = false) List<Integer> materialsGoodsType) {
        log.debug("REST request to get a page of MaterialGoods for PPOrder");
        List<MGForPPOrderConvertDTO> page = materialGoodsService.getMaterialGoodsForCombobox1(materialsGoodsType);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods/pageable-all-material-goods1")
    @Timed
    public ResponseEntity<List<MaterialGoods>> pageableAllMaterialGoods(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoods");
        Page<MaterialGoods> page = materialGoodsService.pageableAllMaterialGoods(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods/pageable-all-material-goods1");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     * @Author hieugie
     * <p>
     * Lấy hàng hóa theo com id
     */
    @GetMapping("/material-goods/company")
    @Timed
    public ResponseEntity<List<MaterialGoods>> findByCompanyID() {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoods> materialGoods = materialGoodsService.findByCompanyIDAndIsActiveTrue();
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    @GetMapping("/material-goods/combobox1")
    @Timed
    public ResponseEntity<List<MaterialGoodsConvertDTO>> ConvertToCombobox() {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoodsConvertDTO> materialGoods = materialGoodsService.ConvertToCombobox();
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    /***
     * @author phuonghv
     * @return
     */
    @GetMapping("/material-goods/find-all-for-pp-service")
    @Timed
    public ResponseEntity<List<MaterialGoodsDTO>> findAllForPPService() {
        log.debug("REST request to find all of MaterialGoods for PP Service");
        List<MaterialGoodsDTO> materialGoods = materialGoodsService.findAllForPPService();
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    /***
     * @author congnd
     * @return
     */
    @GetMapping("/material-goods/find-all-for-pp-invoice")
    @Timed
    public ResponseEntity<List<MaterialGoodsDTO>> findAllForPPInvoice() {
        log.debug("REST request to find all of MaterialGoods for PP Service");
        List<MaterialGoodsDTO> materialGoods = materialGoodsService.findAllForPPInvoice();
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    /***
     * @author chuongnv
     * @return
     */
    @GetMapping("/material-goods/find-all-dto")
    @Timed
    public ResponseEntity<List<MaterialGoodsDTO>> findAllForDTO(@RequestParam(required = false) UUID companyID, @RequestParam(required = false) Boolean similarBranch) {
        log.debug("REST request to find all of MaterialGoods for PP Service");
        List<MaterialGoodsDTO> materialGoods = materialGoodsService.findAllForDTO(companyID, similarBranch);
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    /***
     * @author congnd
     * @return
     */
    @GetMapping("/material-goods/find-all-dto-similar-branch")
    @Timed
    public ResponseEntity<List<MaterialGoodsDTO>> findAllForDTOSimilarBranch(@RequestParam(required = false) Boolean similarBranch,
                                                                             @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to find all of MaterialGoods for PP Service");
        List<MaterialGoodsDTO> materialGoods = materialGoodsService.findAllForDTOSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(materialGoods, HttpStatus.OK);
    }

    /**
     * GET  /material-goods/:id : get the "id" materialGoods.
     *
     * @param id the id of the materialGoods to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoods, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods/pp-invoice-quantity")
    @Timed
    public ResponseEntity<Long> getMaterialGoodsPPInvoiceQuantity(@RequestParam UUID id, @RequestParam UUID ppOrderDetailID) {
        log.debug("REST request to get getMaterialGoodsPPInvoiceQuantity : {}", id);
        Long ppInvoiceQuantity = materialGoodsService.getMaterialGoodsPPInvoiceQuantity(id, ppOrderDetailID);
        return new ResponseEntity<>(ppInvoiceQuantity, HttpStatus.OK);
    }

    @GetMapping("/material-goods/{id}/uuid")
    @Timed
    public ResponseEntity<MaterialGoods> getByUUID(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoods : {}", id);
        Optional<MaterialGoods> materialGoods = materialGoodsService.getByUUID(id);
        return ResponseUtil.wrapOrNotFound(materialGoods);
    }

    @GetMapping("/material-goods/find-all-material-goods-active-companyid")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getAllMaterialGoodsActiveCompanyID() {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoods> page = materialGoodsService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/material-goods/find-all-material-goods-custom")
    @Timed
    public ResponseEntity<List<MaterialGoodsDTO>> getAllMaterialGoodsCustom(@RequestParam(required = false) UUID repositoryID) {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoodsDTO> page = materialGoodsService.findAllMaterialGoodsCustom();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/material-goods/find-all-material-goods-by-repository")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getAllMaterialGoodsActiveCompanyIDByRepository(@RequestParam UUID repositoryId) {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoods> page = materialGoodsService.getAllMaterialGoodsActiveCompanyIDByRepository(repositoryId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods/find-all-by-company-id")
    @Timed
    public ResponseEntity<List<MaterialGoods>> getAllMaterialGoodsByCompanyID() {
        log.debug("REST request to get a page of MaterialGoods");
        List<MaterialGoods> page = materialGoodsService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/material-goods/search-all")
    @Timed
    public ResponseEntity<List<MaterialGoods>> findAll(Pageable pageable,
                                                       @RequestParam(required = false) String searchVoucherMaterialGoods
    ) {
        log.debug("REST request to get a page of mBTellerPapers");
        ObjectMapper objectMapper = new ObjectMapper();
        SearchVoucherMaterialGoods searchVoucherMaterialGoods1 = null;
        try {
            searchVoucherMaterialGoods1 = objectMapper.readValue(searchVoucherMaterialGoods, SearchVoucherMaterialGoods.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<MaterialGoods> page = materialGoodsService.findAll1(pageable, searchVoucherMaterialGoods1);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/material-goods/delete-material-goods-employee")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerEmployee(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = materialGoodsService.deleteEmployee(uuids);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }
}
