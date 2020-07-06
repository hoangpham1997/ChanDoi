package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.service.StatisticsCodeService;
import vn.softdreams.ebweb.service.dto.StatisticsConvertDTO;
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
 * REST controller for managing StatisticsCode.
 */
@RestController
@RequestMapping("/api")
public class StatisticsCodeResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsCodeResource.class);

    private static final String ENTITY_NAME = "statisticsCode";

    private final StatisticsCodeService statisticsCodeService;

    public StatisticsCodeResource(StatisticsCodeService statisticsCodeService) {
        this.statisticsCodeService = statisticsCodeService;
    }

    /**
     * POST  /statistics-codes : Create a new statisticsCode.
     *
     * @param statisticsCode the statisticsCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statisticsCode, or with status 400 (Bad Request) if the statisticsCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/statistics-codes")
    @Timed
    public ResponseEntity<StatisticsCode> createStatisticsCode(@Valid @RequestBody StatisticsCode statisticsCode) throws URISyntaxException {
        log.debug("REST request to save StatisticsCode : {}", statisticsCode);
        if (statisticsCode.getId() != null) {
            throw new BadRequestAlertException("A new statisticsCode cannot already have an ID", ENTITY_NAME, "idexists");
        }

        StatisticsCode result = statisticsCodeService.save(statisticsCode);
        return ResponseEntity.created(new URI("/api/statistics-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statistics-codes : Updates an existing statisticsCode.
     *
     * @param statisticsCode the statisticsCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statisticsCode,
     * or with status 400 (Bad Request) if the statisticsCode is not valid,
     * or with status 500 (Internal Server Error) if the statisticsCode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/statistics-codes")
    @Timed
    public ResponseEntity<StatisticsCode> updateStatisticsCode(@Valid @RequestBody StatisticsCode statisticsCode) throws URISyntaxException {
        log.debug("REST request to update StatisticsCode : {}", statisticsCode);
        if (statisticsCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatisticsCode result = statisticsCodeService.save(statisticsCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statisticsCode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statistics-codes : get all the statisticsCodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of statisticsCodes in body
     */
    @GetMapping("/statistics-codes")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodes(Pageable pageable) {
        log.debug("REST request to get a page of StatisticsCodes");
        List<StatisticsCode> page = statisticsCodeService.findAllByCompanyID();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics-codes");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/getCbxStatisticsCodes/{id}")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getCbxStatisticsCodes(@PathVariable UUID id) {
        log.debug("REST request to get a combobox list of StatisticsCodes", id);
        List<StatisticsCode> listCbx = statisticsCodeService.findCbxStatisticsCode(id);
        return new ResponseEntity<>(listCbx, HttpStatus.OK);
    }
    /**
     * GET  /statistics-codes : get all the statisticsCodes.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of statisticsCodes in body
     */
//    @GetMapping("/statistics-codes/getAllStatisticsCodes")
//    @Timed
//    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodes() {
//        log.debug("REST request to get a page of StatisticsCodes");
//        Page<StatisticsCode> page = statisticsCodeService.findAll();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics-codes/getAllStatisticsCodes");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }

    /**
     * GET  /statistics-codes/:id : get the "id" statisticsCode.
     *
     * @param id the id of the statisticsCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statisticsCode, or with status 404 (Not Found)
     */
    @GetMapping("/statistics-codes/{id}")
    @Timed
    public ResponseEntity<StatisticsCode> getStatisticsCode(@PathVariable UUID id) {
        log.debug("REST request to get StatisticsCode : {}", id);
        Optional<StatisticsCode> statisticsCode = statisticsCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statisticsCode);
    }

    /**
     * DELETE  /statistics-codes/:id : delete the "id" statisticsCode.
     *
     * @param id the id of the statisticsCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/statistics-codes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStatisticsCode(@PathVariable UUID id) {
        log.debug("REST request to delete StatisticsCode : {}", id);
        statisticsCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/statistics-codes/get-all-active")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllActiveStatisticsCodes() {
        log.debug("REST request to get a list of active StatisticsCode");
        List<StatisticsCode> list = statisticsCodeService.findAllActive();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/active")
    @Timed
    public ResponseEntity<List<StatisticsConvertDTO>> getAllStatisticsCodesActive() {
        log.debug("REST request to get a page of StatisticsCodes");
        Page<StatisticsConvertDTO> page = statisticsCodeService.getAllStatisticsCodesActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics-codes/active");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/find-all-statistics-codes-active-companyid")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodesActiveCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<StatisticsCode> page = statisticsCodeService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/find-all-statistics-codes-by-company-id")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodesCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<StatisticsCode> page = statisticsCodeService.findAllStatisticsCode();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/find-all-statistics-codes-by-companyid")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodesByCompanyID() {
        log.debug("REST request to get a page of Accounts");
        List<StatisticsCode> page = statisticsCodeService.getAllStatisticsCodesByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/statistics-codes/find-all-statistics-code-by-company-id-similar-branch")
    @Timed
    public ResponseEntity<List<StatisticsCode>> getAllStatisticsCodesByCompanyID(@RequestParam(required = false) Boolean similarBranch,
                                                                                 @RequestParam(required = false) UUID companyID) {
        log.debug("REST request to get a page of Accounts");
        List<StatisticsCode> page = statisticsCodeService.getAllStatisticsCodesByCompanyIDSimilarBranch(similarBranch, companyID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
