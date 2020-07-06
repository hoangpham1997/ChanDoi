package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.domain.CareerGroup;
import vn.softdreams.ebweb.service.CareerGroupService;
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
 * REST controller for managing CareerGroup.
 */
@RestController
@RequestMapping("/api")
public class CareerGroupResource {

    private final Logger log = LoggerFactory.getLogger(CareerGroupResource.class);

    private static final String ENTITY_NAME = "careerGroup";

    private final CareerGroupService careerGroupService;

    public CareerGroupResource(CareerGroupService careerGroupService) {
        this.careerGroupService = careerGroupService;
    }

    /**
     * POST  /career-groups : Create a new careerGroup.
     *
     * @param careerGroup the careerGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new careerGroup, or with status 400 (Bad Request) if the careerGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/career-groups")
    @Timed
    public ResponseEntity<CareerGroup> createCareerGroup(@Valid @RequestBody CareerGroup careerGroup) throws URISyntaxException {
        log.debug("REST request to save CareerGroup : {}", careerGroup);
        if (careerGroup.getId() != null) {
            throw new BadRequestAlertException("A new careerGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CareerGroup result = careerGroupService.save(careerGroup);
        return ResponseEntity.created(new URI("/api/career-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /career-groups : Updates an existing careerGroup.
     *
     * @param careerGroup the careerGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated careerGroup,
     * or with status 400 (Bad Request) if the careerGroup is not valid,
     * or with status 500 (Internal Server Error) if the careerGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/career-groups")
    @Timed
    public ResponseEntity<CareerGroup> updateCareerGroup(@Valid @RequestBody CareerGroup careerGroup) throws URISyntaxException {
        log.debug("REST request to update CareerGroup : {}", careerGroup);
        if (careerGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CareerGroup result = careerGroupService.save(careerGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, careerGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /career-groups : get all the careerGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of careerGroups in body
     */
    @GetMapping("/career-groups")
    @Timed
    public ResponseEntity<List<CareerGroup>> getAllCareerGroups(Pageable pageable) {
        log.debug("REST request to get a page of CareerGroups");
        Page<CareerGroup> page = careerGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/career-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /career-groups/:id : get the "id" careerGroup.
     *
     * @param id the id of the careerGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the careerGroup, or with status 404 (Not Found)
     */
    @GetMapping("/career-groups/{id}")
    @Timed
    public ResponseEntity<CareerGroup> getCareerGroup(@PathVariable UUID id) {
        log.debug("REST request to get CareerGroup : {}", id);
        Optional<CareerGroup> careerGroup = careerGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(careerGroup);
    }

    /**
     * DELETE  /career-groups/:id : delete the "id" careerGroup.
     *
     * @param id the id of the careerGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/career-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteCareerGroup(@PathVariable UUID id) {
        log.debug("REST request to delete CareerGroup : {}", id);
        careerGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/career-groups/get-career-groups")
    @Timed
    public ResponseEntity<List<CareerGroup>> getCareersGroup() {
        log.debug("REST request to get a page of Accounts");
        List<CareerGroup> careerGroups = careerGroupService.findAllCareerGroups();
        return new ResponseEntity<>(careerGroups, HttpStatus.OK);
    }
}
