package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.EbPackage;
import vn.softdreams.ebweb.service.EbPackageService;
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
 * REST controller for managing EbPackage.
 */
@RestController
@RequestMapping("/api")
public class EbPackageResource {

    private final Logger log = LoggerFactory.getLogger(EbPackageResource.class);

    private static final String ENTITY_NAME = "ebPackage";

    private final EbPackageService ebPackageService;

    public EbPackageResource(EbPackageService ebPackageService) {
        this.ebPackageService = ebPackageService;
    }

    /**
     * POST  /eb-packages : Create a new ebPackage.
     *
     * @param ebPackage the ebPackage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ebPackage, or with status 400 (Bad Request) if the ebPackage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/eb-packages")
    @Timed
    public ResponseEntity<EbPackage> createEbPackage(@Valid @RequestBody EbPackage ebPackage) throws URISyntaxException {
        log.debug("REST request to save EbPackage : {}", ebPackage);
        if (ebPackage.getId() != null) {
            throw new BadRequestAlertException("A new ebPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EbPackage result = ebPackageService.save(ebPackage);
        return ResponseEntity.created(new URI("/api/eb-packages/" + (result.getId())))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, (result.getId() != null ? result.getId().toString() : "")))
            .body(result);
    }

//    @GetMapping("/eb-packages/get-PackageByUserAndCompany")
//    public ResponseEntity<Integer> getEbPackageByUserAndCompany() {
//        Integer ebPackage = ebPackageService.findOneByOrgIdAndUserId();
//        return new ResponseEntity<>(ebPackage, HttpStatus.OK);
//    }

    @GetMapping("/eb-packages/get-PackageByUser")
    public ResponseEntity<Integer> getEbPackageByUser() {
        Integer ebPackage = ebPackageService.findOneByUserId();
        return new ResponseEntity<>(ebPackage, HttpStatus.OK);
    }

    /**
     * PUT  /eb-packages : Updates an existing ebPackage.
     *
     * @param ebPackage the ebPackage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ebPackage,
     * or with status 400 (Bad Request) if the ebPackage is not valid,
     * or with status 500 (Internal Server Error) if the ebPackage couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/eb-packages")
    @Timed
    public ResponseEntity<EbPackage> updateEbPackage(@Valid @RequestBody EbPackage ebPackage) throws URISyntaxException {
        log.debug("REST request to update EbPackage : {}", ebPackage);
        if (ebPackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EbPackage result = ebPackageService.save(ebPackage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ebPackage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eb-packages : get all the ebPackages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ebPackages in body
     */
    @GetMapping("/eb-packages")
    @Timed
    public ResponseEntity<List<EbPackage>> getAllEbPackages(Pageable pageable) {
        log.debug("REST request to get a page of EbPackages");
        Page<EbPackage> page = ebPackageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eb-packages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/eb-packages/list-ebPackage")
    @Timed
    public ResponseEntity<List<EbPackage>> getListEbPackages(Pageable pageable) {
        log.debug("REST request to get a page of EbPackages");
        List<EbPackage> packageList = ebPackageService.findAll();
        return new ResponseEntity<>(packageList, HttpStatus.OK);
    }

    /**
     * GET  /eb-packages/:id : get the "id" ebPackage.
     *
     * @param id the id of the ebPackage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ebPackage, or with status 404 (Not Found)
     */
    @GetMapping("/eb-packages/{id}")
    @Timed
    public ResponseEntity<EbPackage> getEbPackage(@PathVariable UUID id) {
        log.debug("REST request to get EbPackage : {}", id);
        Optional<EbPackage> ebPackage = ebPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ebPackage);
    }

    /**
     * DELETE  /eb-packages/:id : delete the "id" ebPackage.
     *
     * @param id the id of the ebPackage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/eb-packages/{id}")
    @Timed
    public ResponseEntity<Void> deleteEbPackage(@PathVariable UUID id) {
        log.debug("REST request to delete EbPackage : {}", id);
        ebPackageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
