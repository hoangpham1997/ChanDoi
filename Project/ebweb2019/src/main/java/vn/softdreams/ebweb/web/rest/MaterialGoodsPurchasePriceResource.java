package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialGoodsPurchasePrice;
import vn.softdreams.ebweb.service.MaterialGoodsPurchasePriceService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MaterialGoodsPurchasePrice.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsPurchasePriceResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsPurchasePriceResource.class);

    private static final String ENTITY_NAME = "materialGoodsPurchasePrice";

    private final MaterialGoodsPurchasePriceService materialGoodsPurchasePriceService;

    public MaterialGoodsPurchasePriceResource(MaterialGoodsPurchasePriceService materialGoodsPurchasePriceService) {
        this.materialGoodsPurchasePriceService = materialGoodsPurchasePriceService;
    }

    /**
     * POST  /material-goods-purchase-prices : Create a new materialGoodsPurchasePrice.
     *
     * @param materialGoodsPurchasePrice the materialGoodsPurchasePrice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsPurchasePrice, or with status 400 (Bad Request) if the materialGoodsPurchasePrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-purchase-prices")
    @Timed
    public ResponseEntity<MaterialGoodsPurchasePrice> createMaterialGoodsPurchasePrice(@RequestBody MaterialGoodsPurchasePrice materialGoodsPurchasePrice) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsPurchasePrice : {}", materialGoodsPurchasePrice);
        if (materialGoodsPurchasePrice.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsPurchasePrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsPurchasePrice result = materialGoodsPurchasePriceService.save(materialGoodsPurchasePrice);
        return ResponseEntity.created(new URI("/api/material-goods-purchase-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-purchase-prices : Updates an existing materialGoodsPurchasePrice.
     *
     * @param materialGoodsPurchasePrice the materialGoodsPurchasePrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsPurchasePrice,
     * or with status 400 (Bad Request) if the materialGoodsPurchasePrice is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsPurchasePrice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-purchase-prices")
    @Timed
    public ResponseEntity<MaterialGoodsPurchasePrice> updateMaterialGoodsPurchasePrice(@RequestBody MaterialGoodsPurchasePrice materialGoodsPurchasePrice) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsPurchasePrice : {}", materialGoodsPurchasePrice);
        if (materialGoodsPurchasePrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsPurchasePrice result = materialGoodsPurchasePriceService.save(materialGoodsPurchasePrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsPurchasePrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-purchase-prices : get all the materialGoodsPurchasePrices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsPurchasePrices in body
     */
    @GetMapping("/material-goods-purchase-prices")
    @Timed
    public ResponseEntity<List<MaterialGoodsPurchasePrice>> getAllMaterialGoodsPurchasePrices(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsPurchasePrices");
        Page<MaterialGoodsPurchasePrice> page = materialGoodsPurchasePriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-purchase-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-purchase-prices/:id : get the "id" materialGoodsPurchasePrice.
     *
     * @param id the id of the materialGoodsPurchasePrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsPurchasePrice, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-purchase-prices/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsPurchasePrice> getMaterialGoodsPurchasePrice(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsPurchasePrice : {}", id);
        Optional<MaterialGoodsPurchasePrice> materialGoodsPurchasePrice = materialGoodsPurchasePriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsPurchasePrice);
    }

    /**
     * DELETE  /material-goods-purchase-prices/:id : delete the "id" materialGoodsPurchasePrice.
     *
     * @param id the id of the materialGoodsPurchasePrice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-purchase-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsPurchasePrice(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsPurchasePrice : {}", id);
        materialGoodsPurchasePriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/material-goods-purchase-prices/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsPurchasePrice>> findByMaterialGoodsID(@RequestParam(required = false) UUID id
    ) {
        List<MaterialGoodsPurchasePrice> lstMaterialGoodsAssembly = materialGoodsPurchasePriceService.findByMaterialGoodsID(id);
        return new ResponseEntity<>(lstMaterialGoodsAssembly, HttpStatus.OK);
    }
}
