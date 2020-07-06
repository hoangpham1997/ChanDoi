package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialQuantum;
import vn.softdreams.ebweb.service.MaterialQuantumService;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnOutWardDTO;
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
 * REST controller for managing MaterialQuantum.
 */
@RestController
@RequestMapping("/api")
public class MaterialQuantumResource {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumResource.class);

    private static final String ENTITY_NAME = "materialQuantum";

    private final MaterialQuantumService materialQuantumService;

    public MaterialQuantumResource(MaterialQuantumService materialQuantumService) {
        this.materialQuantumService = materialQuantumService;
    }

    /**
     * POST  /material-quantums : Create a new materialQuantum.
     *
     * @param materialQuantum the materialQuantum to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialQuantum, or with status 400 (Bad Request) if the materialQuantum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-quantums")
    @Timed
    public ResponseEntity<MaterialQuantum> createMaterialQuantum(@RequestBody MaterialQuantum materialQuantum) throws URISyntaxException {
        log.debug("REST request to save MaterialQuantum : {}", materialQuantum);
        if (materialQuantum.getId() != null) {
            throw new BadRequestAlertException("A new materialQuantum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialQuantum result = materialQuantumService.save(materialQuantum);
        return ResponseEntity.created(new URI("/api/material-quantums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-quantums : Updates an existing materialQuantum.
     *
     * @param materialQuantum the materialQuantum to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialQuantum,
     * or with status 400 (Bad Request) if the materialQuantum is not valid,
     * or with status 500 (Internal Server Error) if the materialQuantum couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-quantums")
    @Timed
    public ResponseEntity<MaterialQuantum> updateMaterialQuantum(@RequestBody MaterialQuantum materialQuantum) throws URISyntaxException {
        log.debug("REST request to update MaterialQuantum : {}", materialQuantum);
        if (materialQuantum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialQuantum result = materialQuantumService.save(materialQuantum);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialQuantum.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-quantums : get all the materialQuantums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialQuantums in body
     */
    @GetMapping("/material-quantums")
    @Timed
    public ResponseEntity<List<MaterialQuantum>> getAllMaterialQuantums(Pageable pageable) {
        log.debug("REST request to get a page of MaterialQuantums");
        Page<MaterialQuantum> page = materialQuantumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-quantums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-quantums/:id : get the "id" materialQuantum.
     *
     * @param id the id of the materialQuantum to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialQuantum, or with status 404 (Not Found)
     */
    @GetMapping("/material-quantums/{id}")
    @Timed
    public ResponseEntity<MaterialQuantum> getMaterialQuantum(@PathVariable UUID id) {
        log.debug("REST request to get MaterialQuantum : {}", id);
        Optional<MaterialQuantum> materialQuantum = materialQuantumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialQuantum);
    }

    /**
     * DELETE  /material-quantums/:id : delete the "id" materialQuantum.
     *
     * @param id the id of the materialQuantum to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-quantums/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialQuantum(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialQuantum : {}", id);
        materialQuantumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/material-quantums/find-all-material-quantums-by-companyid")
    @Timed
    public ResponseEntity<List<MaterialQuantumGeneralDTO>> getAllMaterialQuantumsByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<MaterialQuantumGeneralDTO> page = materialQuantumService.findAllByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/material-quantums/find-all-object-active-by-companyid")
    @Timed
    public ResponseEntity<List<ObjectsMaterialQuantumDTO>> findAllObjectActive() {
        log.debug("REST request to get a page of Accounts");
        List<ObjectsMaterialQuantumDTO> page = materialQuantumService.findAllObjectActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @param pageable
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping("/material-quantums/search-all-dto")
    @Timed
    public ResponseEntity<Page<MaterialQuantumDTO>> searchAllOrderDTO(Pageable pageable,
                                                                      @RequestParam(required = false) String fromDate,
                                                                      @RequestParam(required = false) String toDate) {
        log.debug("REST request to getAll MaterialQuantumOutWardDTO");
        Page<MaterialQuantumDTO> page = materialQuantumService.findAllMaterialQuantumDTO(pageable, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ppService/find-all");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
}
