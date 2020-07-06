package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.service.MaterialQuantumDetailsService;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;
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
 * REST controller for managing MaterialQuantumDetails.
 */
@RestController
@RequestMapping("/api")
public class MaterialQuantumDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumDetailsResource.class);

    private static final String ENTITY_NAME = "materialQuantumDetails";

    private final MaterialQuantumDetailsService materialQuantumDetailsService;

    public MaterialQuantumDetailsResource(MaterialQuantumDetailsService materialQuantumDetailsService) {
        this.materialQuantumDetailsService = materialQuantumDetailsService;
    }

    /**
     * POST  /material-quantum-details : Create a new materialQuantumDetails.
     *
     * @param materialQuantumDetails the materialQuantumDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new materialQuantumDetails, or with status 400 (Bad Request) if the materialQuantumDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/material-quantum-details")
    @Timed
    public ResponseEntity<MaterialQuantumDetails> createMaterialQuantumDetails(@Valid @RequestBody MaterialQuantumDetails materialQuantumDetails) throws URISyntaxException {
        log.debug("REST request to save MaterialQuantumDetails : {}", materialQuantumDetails);
        if (materialQuantumDetails.getId() != null) {
            throw new BadRequestAlertException("A new materialQuantumDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialQuantumDetails result = materialQuantumDetailsService.save(materialQuantumDetails);
        return ResponseEntity.created(new URI("/api/material-quantum-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /material-quantum-details : Updates an existing materialQuantumDetails.
     *
     * @param materialQuantumDetails the materialQuantumDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated materialQuantumDetails,
     * or with status 400 (Bad Request) if the materialQuantumDetails is not valid,
     * or with status 500 (Internal Server Error) if the materialQuantumDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/material-quantum-details")
    @Timed
    public ResponseEntity<MaterialQuantumDetails> updateMaterialQuantumDetails(@Valid @RequestBody MaterialQuantumDetails materialQuantumDetails) throws URISyntaxException {
        log.debug("REST request to update MaterialQuantumDetails : {}", materialQuantumDetails);
        if (materialQuantumDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialQuantumDetails result = materialQuantumDetailsService.save(materialQuantumDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, materialQuantumDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /material-quantum-details : get all the materialQuantumDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of materialQuantumDetails in body
     */
    @GetMapping("/material-quantum-details")
    @Timed
    public ResponseEntity<List<MaterialQuantumDetails>> getAllMaterialQuantumDetails(Pageable pageable) {
        log.debug("REST request to get a page of MaterialQuantumDetails");
        Page<MaterialQuantumDetails> page = materialQuantumDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/material-quantum-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /material-quantum-details/:id : get the "id" materialQuantumDetails.
     *
     * @param id the id of the materialQuantumDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the materialQuantumDetails, or with status 404 (Not Found)
     */
    @GetMapping("/material-quantum-details/{id}")
    @Timed
    public ResponseEntity<MaterialQuantumDetails> getMaterialQuantumDetails(@PathVariable UUID id) {
        log.debug("REST request to get MaterialQuantumDetails : {}", id);
        Optional<MaterialQuantumDetails> materialQuantumDetails = materialQuantumDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialQuantumDetails);
    }

    /**
     * DELETE  /material-quantum-details/:id : delete the "id" materialQuantumDetails.
     *
     * @param id the id of the materialQuantumDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/material-quantum-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaterialQuantumDetails(@PathVariable UUID id) {
        log.debug("REST request to delete MaterialQuantumDetails : {}", id);
        materialQuantumDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/material-quantum-details/details")
    @Timed
    public ResponseEntity<List<MaterialQuantumDetailsDTO>> findAllDetailsById(@RequestParam List<UUID> id) {
        log.debug("REST request to get SaReturn : {}", id);
        List<MaterialQuantumDetailsDTO> details = materialQuantumDetailsService.findAllDetailsById(id);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
