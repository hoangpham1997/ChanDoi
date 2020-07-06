package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.GoodsServicePurchase;
import vn.softdreams.ebweb.service.GoodsServicePurchaseService;
import vn.softdreams.ebweb.service.dto.GoodsServicePurchaseContvertDTO;
import vn.softdreams.ebweb.service.dto.OrganizationUnitDTO;
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
 * REST controller for managing GoodsServicePurchase.
 */
@RestController
@RequestMapping("/api")
public class GoodsServicePurchaseResource {

    private final Logger log = LoggerFactory.getLogger(GoodsServicePurchaseResource.class);

    private static final String ENTITY_NAME = "goodsServicePurchase";

    private final GoodsServicePurchaseService goodsServicePurchaseService;

    public GoodsServicePurchaseResource(GoodsServicePurchaseService goodsServicePurchaseService) {
        this.goodsServicePurchaseService = goodsServicePurchaseService;
    }

    /**
     * POST  /goods-service-purchases : Create a new goodsServicePurchase.
     *
     * @param goodsServicePurchase the goodsServicePurchase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goodsServicePurchase, or with status 400 (Bad Request) if the goodsServicePurchase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/goods-service-purchases")
    @Timed
    public ResponseEntity<GoodsServicePurchase> createGoodsServicePurchase(@Valid @RequestBody GoodsServicePurchase goodsServicePurchase) throws URISyntaxException {
        log.debug("REST request to save GoodsServicePurchase : {}", goodsServicePurchase);
        if (goodsServicePurchase.getId() != null) {
            throw new BadRequestAlertException("A new goodsServicePurchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoodsServicePurchase result = goodsServicePurchaseService.save(goodsServicePurchase);
        return ResponseEntity.created(new URI("/api/goods-service-purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goods-service-purchases : Updates an existing goodsServicePurchase.
     *
     * @param goodsServicePurchase the goodsServicePurchase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goodsServicePurchase,
     * or with status 400 (Bad Request) if the goodsServicePurchase is not valid,
     * or with status 500 (Internal Server Error) if the goodsServicePurchase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/goods-service-purchases")
    @Timed
    public ResponseEntity<GoodsServicePurchase> updateGoodsServicePurchase(@Valid @RequestBody GoodsServicePurchase goodsServicePurchase) throws URISyntaxException {
        log.debug("REST request to update GoodsServicePurchase : {}", goodsServicePurchase);
        if (goodsServicePurchase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GoodsServicePurchase result = goodsServicePurchaseService.save(goodsServicePurchase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, goodsServicePurchase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goods-service-purchases : get all the goodsServicePurchases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of goodsServicePurchases in body
     */
    @GetMapping("/goods-service-purchases")
    @Timed
    public ResponseEntity<List<GoodsServicePurchase>> getAllGoodsServicePurchases(Pageable pageable) {
        log.debug("REST request to get a page of GoodsServicePurchases");
        Page<GoodsServicePurchase> page = goodsServicePurchaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goods-service-purchases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /goods-service-purchases/:id : get the "id" goodsServicePurchase.
     *
     * @param id the id of the goodsServicePurchase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goodsServicePurchase, or with status 404 (Not Found)
     */
    @GetMapping("/goods-service-purchases/{id}")
    @Timed
    public ResponseEntity<GoodsServicePurchase> getGoodsServicePurchase(@PathVariable UUID id) {
        log.debug("REST request to get GoodsServicePurchase : {}", id);
        Optional<GoodsServicePurchase> goodsServicePurchase = goodsServicePurchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(goodsServicePurchase);
    }

    /**
     * DELETE  /goods-service-purchases/:id : delete the "id" goodsServicePurchase.
     *
     * @param id the id of the goodsServicePurchase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/goods-service-purchases/{id}")
    @Timed
    public ResponseEntity<Void> deleteGoodsServicePurchase(@PathVariable UUID id) {
        log.debug("REST request to delete GoodsServicePurchase : {}", id);
        goodsServicePurchaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/goods-service-purchases/getPurchaseName")
    @Timed
    public ResponseEntity<GoodsServicePurchase> getPurchaseName() {
        log.debug("REST request to get a page of OrganizationUnits");
        Optional<GoodsServicePurchase> goodsServicePurchase =  goodsServicePurchaseService.getPurchaseName();
        return ResponseUtil.wrapOrNotFound(goodsServicePurchase);
    }

    @GetMapping("/goods-service-purchases/getOrganizationUnit")
    @Timed
    public ResponseEntity<List<GoodsServicePurchaseContvertDTO>> getPurchaseNameToCombobox() {
        log.debug("REST request to get a page of OrganizationUnits");
        Page<GoodsServicePurchaseContvertDTO> page = goodsServicePurchaseService.getPurchaseNameToCombobox();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units/getAllOrganizationUnits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/goods-service-purchases/find-all-good-service-purchase-active-companyid")
    @Timed
    public ResponseEntity<List<GoodsServicePurchase>> getAllGoodServicePurchaseActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<GoodsServicePurchase> page = goodsServicePurchaseService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
