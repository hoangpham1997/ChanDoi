package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.SystemOption;
import vn.softdreams.ebweb.service.SystemOptionService;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;
import vn.softdreams.ebweb.service.dto.SaveSystemOptionsDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing SystemOption.
 */
@RestController
@RequestMapping("/api")
public class SystemOptionResource {

    private final Logger log = LoggerFactory.getLogger(SystemOptionResource.class);

    private static final String ENTITY_NAME = "systemOption";

    private final SystemOptionService systemOptionService;

    public SystemOptionResource(SystemOptionService systemOptionService) {
        this.systemOptionService = systemOptionService;
    }

    /**
     * POST  /system-options : Create a new systemOption.
     *
     * @param systemOption the systemOption to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemOption, or with status 400 (Bad Request) if the systemOption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-options")
    @Timed
    public ResponseEntity<SystemOption> createSystemOption(@Valid @RequestBody SystemOption systemOption) throws URISyntaxException {
        log.debug("REST request to save SystemOption : {}", systemOption);
        if (systemOption.getId() != null) {
            throw new BadRequestAlertException("A new systemOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemOption result = systemOptionService.save(systemOption);
        return ResponseEntity.created(new URI("/api/system-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-options : Updates an existing systemOption.
     *
     * @param systemOption the systemOption to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemOption,
     * or with status 400 (Bad Request) if the systemOption is not valid,
     * or with status 500 (Internal Server Error) if the systemOption couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-options")
    @Timed
    public ResponseEntity<SystemOption> updateSystemOption(@Valid @RequestBody SystemOption systemOption) throws URISyntaxException {
        log.debug("REST request to update SystemOption : {}", systemOption);
        if (systemOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemOption result = systemOptionService.save(systemOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-options : get all the systemOptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of systemOptions in body
     */
    @GetMapping("/system-options")
    @Timed
    public ResponseEntity<List<SystemOption>> getAllSystemOptions(Pageable pageable) {
        log.debug("REST request to get a page of SystemOptions");
        Page<SystemOption> page = systemOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/system-options");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/system-options/findOne")
    @Timed
    public ResponseEntity<SystemOption> findOneByUser(@RequestParam String code) {
        log.debug("REST request to get a page of SystemOptions");
        Optional<SystemOption> page = systemOptionService.findOneByUser(code);
        return ResponseUtil.wrapOrNotFound(page);
    }

    /**
     * GET  /system-options/:id : get the "id" systemOption.
     *
     * @param id the id of the systemOption to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemOption, or with status 404 (Not Found)
     */
    @GetMapping("/system-options/{id}")
    @Timed
    public ResponseEntity<SystemOption> getSystemOption(@PathVariable Long id) {
        log.debug("REST request to get SystemOption : {}", id);
        Optional<SystemOption> systemOption = systemOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemOption);
    }

    /**
     * DELETE  /system-options/:id : delete the "id" systemOption.
     *
     * @param id the id of the systemOption to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-options/{id}")
    @Timed
    public ResponseEntity<Void> deleteSystemOption(@PathVariable Long id) {
        log.debug("REST request to delete SystemOption : {}", id);
        systemOptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/system-options/update-posted-date")
    @Timed
    public ResponseEntity<Void> updatePostedDate(@Valid @RequestBody String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        SystemOption systemOption = null;
        try {
            systemOption = objectMapper.readValue(data, SystemOption.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        systemOptionService.savePostedDate(systemOption.getData(), systemOption.getDefaultData());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/system-options/save-all")
    @Timed
    public ResponseEntity<List<PrivateToGeneralUse>> saveSystemOptions(@RequestBody SaveSystemOptionsDTO saveSystemOptionsDTO) {
        List<PrivateToGeneralUse> privateToGeneralUses = systemOptionService.saveSystemOptions(saveSystemOptionsDTO);
        return new ResponseEntity<>(privateToGeneralUses,HttpStatus.OK);
    }

    @GetMapping("/system-options/find-all-system-options-companyid")
    @Timed
    public ResponseEntity<List<SystemOption>> getAllSystemOptionCompanyID() {
        log.debug("REST request to get a page of SystemOption");
        List<SystemOption> page = systemOptionService.findAllSystemOptions();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/system-options/find-system-options-by-companyid")
    @Timed
    public ResponseEntity<List<SystemOption>> getSystemOptionsByCompanyID(@RequestParam UUID companyID) {
        log.debug("REST request to get a page of SystemOption");
        List<SystemOption> page = systemOptionService.getSystemOptionsByCompanyID(companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
