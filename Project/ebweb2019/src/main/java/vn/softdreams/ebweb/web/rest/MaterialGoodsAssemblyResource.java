package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.MaterialGoodsAssembly;
import vn.softdreams.ebweb.service.MaterialGoodsAssemblyService;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing MaterialGoodsAssembly.
 */
@RestController
@RequestMapping("/api")
public class MaterialGoodsAssemblyResource {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsAssemblyResource.class);

    private static final String ENTITY_NAME = "materialGoodsAssembly";

    private final MaterialGoodsAssemblyService materialGoodsAssemblyService;

    public MaterialGoodsAssemblyResource(MaterialGoodsAssemblyService materialGoodsAssemblyService) {
        this.materialGoodsAssemblyService = materialGoodsAssemblyService;
    }

    /**
     * POST  /material-goods-assemblies : Create a new materialGoodsAssembly.
     *
     * @param materialGoodsAssembly the materialGoodsAssembly to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialGoodsAssembly, or with status 400 (Bad Request) if the materialGoodsAssembly has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-goods-assemblies")
    @Timed
    public ResponseEntity<MaterialGoodsAssembly> createMaterialGoodsAssembly(@Valid @RequestBody MaterialGoodsAssembly materialGoodsAssembly) throws URISyntaxException {
        log.debug("REST request to save MaterialGoodsAssembly : {}", materialGoodsAssembly);
        if (materialGoodsAssembly.getId() != null) {
            throw new BadRequestAlertException("A new materialGoodsAssembly cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialGoodsAssembly result = materialGoodsAssemblyService.save(materialGoodsAssembly);
        return ResponseEntity.created(new URI("/api/material-goods-assemblies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-goods-assemblies : Updates an existing materialGoodsAssembly.
     *
     * @param materialGoodsAssembly the materialGoodsAssembly to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialGoodsAssembly,
     * or with status 400 (Bad Request) if the materialGoodsAssembly is not valid,
     * or with status 500 (Internal Server Error) if the materialGoodsAssembly couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-goods-assemblies")
    @Timed
    public ResponseEntity<MaterialGoodsAssembly> updateMaterialGoodsAssembly(@Valid @RequestBody MaterialGoodsAssembly materialGoodsAssembly) throws URISyntaxException {
        log.debug("REST request to update MaterialGoodsAssembly : {}", materialGoodsAssembly);
        if (materialGoodsAssembly.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialGoodsAssembly result = materialGoodsAssemblyService.save(materialGoodsAssembly);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialGoodsAssembly.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-goods-assemblies : get all the materialGoodsAssemblies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialGoodsAssemblies in body
     */
    @GetMapping("/material-goods-assemblies")
    @Timed
    public ResponseEntity<List<MaterialGoodsAssembly>> getAllMaterialGoodsAssemblies(Pageable pageable) {
        log.debug("REST request to get a page of MaterialGoodsAssemblies");
        Page<MaterialGoodsAssembly> page = materialGoodsAssemblyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-goods-assemblies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-goods-assemblies/:id : get the "id" materialGoodsAssembly.
     *
     * @param id the id of the materialGoodsAssembly to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialGoodsAssembly, or with status 404 (Not Found)
     */
    @GetMapping("/material-goods-assemblies/{id}")
    @Timed
    public ResponseEntity<MaterialGoodsAssembly> getMaterialGoodsAssembly(@PathVariable UUID id) {
        log.debug("REST request to get MaterialGoodsAssembly : {}", id);
        Optional<MaterialGoodsAssembly> materialGoodsAssembly = materialGoodsAssemblyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialGoodsAssembly);
    }

    /**
     * DELETE  /material-goods-assemblies/:id : delete the "id" materialGoodsAssembly.
     *
     * @param id the id of the materialGoodsAssembly to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-goods-assemblies/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialGoodsAssembly(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialGoodsAssembly : {}", id);
        materialGoodsAssemblyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/material-goods-assemblies/find-by-material-goods-id")
    @Timed
    public ResponseEntity<List<MaterialGoodsAssembly>> findByMaterialGoodsID(@RequestParam(required = false) UUID id
    ) {
        List<MaterialGoodsAssembly> lstMaterialGoodsAssembly = materialGoodsAssemblyService.findByMaterialGoodsID(id);
        return new ResponseEntity<>(lstMaterialGoodsAssembly, HttpStatus.OK);
    }
}
