package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.SaleDiscountPolicy;
import vn.softdreams.ebweb.service.SaleDiscountPolicyService;
import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;
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
 * REST controller for managing SaleDiscountPolicy.
 */
@RestController
@RequestMapping("/api")
public class SaleDiscountPolicyResource {

    private final Logger log = LoggerFactory.getLogger(SaleDiscountPolicyResource.class);

    private static final String ENTITY_NAME = "saleDiscountPolicy";

    private final SaleDiscountPolicyService saleDiscountPolicyService;

    public SaleDiscountPolicyResource(SaleDiscountPolicyService saleDiscountPolicyService) {
        this.saleDiscountPolicyService = saleDiscountPolicyService;
    }

    /**
     * POST  /sale-discount-policies : Create a new saleDiscountPolicy.
     *
     * @param saleDiscountPolicy the saleDiscountPolicy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleDiscountPolicy, or with status 400 (Bad Request) if the saleDiscountPolicy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-discount-policies")
    @Timed
    public ResponseEntity<SaleDiscountPolicy> createSaleDiscountPolicy(@RequestBody SaleDiscountPolicy saleDiscountPolicy) throws URISyntaxException {
        log.debug("REST request to save SaleDiscountPolicy : {}", saleDiscountPolicy);
        if (saleDiscountPolicy.getId() != null) {
            throw new BadRequestAlertException("A new saleDiscountPolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleDiscountPolicy result = saleDiscountPolicyService.save(saleDiscountPolicy);
        return ResponseEntity.created(new URI("/api/sale-discount-policies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-discount-policies : Updates an existing saleDiscountPolicy.
     *
     * @param saleDiscountPolicy the saleDiscountPolicy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleDiscountPolicy,
     * or with status 400 (Bad Request) if the saleDiscountPolicy is not valid,
     * or with status 500 (Internal Server Error) if the saleDiscountPolicy couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-discount-policies")
    @Timed
    public ResponseEntity<SaleDiscountPolicy> updateSaleDiscountPolicy(@RequestBody SaleDiscountPolicy saleDiscountPolicy) throws URISyntaxException {
        log.debug("REST request to update SaleDiscountPolicy : {}", saleDiscountPolicy);
        if (saleDiscountPolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SaleDiscountPolicy result = saleDiscountPolicyService.save(saleDiscountPolicy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleDiscountPolicy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-discount-policies : get all the saleDiscountPolicies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saleDiscountPolicies in body
     */
    @GetMapping("/sale-discount-policies")
    @Timed
    public ResponseEntity<List<SaleDiscountPolicy>> getAllSaleDiscountPolicies(Pageable pageable) {
        log.debug("REST request to get a page of SaleDiscountPolicies");
        Page<SaleDiscountPolicy> page = saleDiscountPolicyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sale-discount-policies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sale-discount-policies/:id : get the "id" saleDiscountPolicy.
     *
     * @param id the id of the saleDiscountPolicy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleDiscountPolicy, or with status 404 (Not Found)
     */
    @GetMapping("/sale-discount-policies/{id}")
    @Timed
    public ResponseEntity<SaleDiscountPolicy> getSaleDiscountPolicy(@PathVariable UUID id) {
        log.debug("REST request to get SaleDiscountPolicy : {}", id);
        Optional<SaleDiscountPolicy> saleDiscountPolicy = saleDiscountPolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleDiscountPolicy);
    }

    /**
     * DELETE  /sale-discount-policies/:id : delete the "id" saleDiscountPolicy.
     *
     * @param id the id of the saleDiscountPolicy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-discount-policies/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleDiscountPolicy(@PathVariable UUID id) {
        log.debug("REST request to delete SaleDiscountPolicy : {}", id);
        saleDiscountPolicyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


//    @GetMapping("/sale-discount-policies/material-goods-id/{id}")
//    @Timed
//    public ResponseEntity<List<SaleDiscountPolicy>> findByMaterialGoodsID(@PathVariable UUID id) {
//        log.debug("REST request to get SaleDiscountPolicy : {}", id);
//        List<SaleDiscountPolicy> saleDiscountPolicy = saleDiscountPolicyService.findByMaterialGoodsID(id);
//        return new ResponseEntity<>(saleDiscountPolicy, HttpStatus.OK
//        );
//    }


    @GetMapping("/sale-discount-policiesDTO")
    @Timed
    public ResponseEntity<List<SaleDiscountPolicyDTO>> getAllSaleDiscountPoliciesDTO() {
        log.debug("REST request to get a page of SaleDiscountPolicies");
        List<SaleDiscountPolicyDTO> lst = saleDiscountPolicyService.findAllSaleDiscountPolicyDTO();
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @GetMapping("/sale-discount-policies/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<SaleDiscountPolicy>> findByMaterialGoodsID(@RequestParam(required = false) UUID id
    ) {
        List<SaleDiscountPolicy> lstsaleDiscountPolicy = saleDiscountPolicyService.findByMaterialGoodsID(id);
        return new ResponseEntity<>(lstsaleDiscountPolicy, HttpStatus.OK);
    }
}
