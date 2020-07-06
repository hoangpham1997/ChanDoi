package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.TimeSheetSymbols;
import vn.softdreams.ebweb.service.TimeSheetSymbolsService;
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
 * REST controller for managing TimeSheetSymbols.
 */
@RestController
@RequestMapping("/api")
public class TimeSheetSymbolsResource {

    private final Logger log = LoggerFactory.getLogger(TimeSheetSymbolsResource.class);

    private static final String ENTITY_NAME = "timeSheetSymbols";

    private final TimeSheetSymbolsService timeSheetSymbolsService;

    public TimeSheetSymbolsResource(TimeSheetSymbolsService timeSheetSymbolsService) {
        this.timeSheetSymbolsService = timeSheetSymbolsService;
    }

    /**
     * POST  /time-sheet-symbols : Create a new timeSheetSymbols.
     *
     * @param timeSheetSymbols the timeSheetSymbols to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeSheetSymbols, or with status 400 (Bad Request) if the timeSheetSymbols has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-sheet-symbols")
    @Timed
    public ResponseEntity<TimeSheetSymbols> createTimeSheetSymbols(@Valid @RequestBody TimeSheetSymbols timeSheetSymbols) throws URISyntaxException {
        log.debug("REST request to save TimeSheetSymbols : {}", timeSheetSymbols);
        if (timeSheetSymbols.getId() != null) {
            throw new BadRequestAlertException("A new timeSheetSymbols cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeSheetSymbols result = timeSheetSymbolsService.save(timeSheetSymbols);
        return ResponseEntity.created(new URI("/api/time-sheet-symbols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-sheet-symbols : Updates an existing timeSheetSymbols.
     *
     * @param timeSheetSymbols the timeSheetSymbols to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeSheetSymbols,
     * or with status 400 (Bad Request) if the timeSheetSymbols is not valid,
     * or with status 500 (Internal Server Error) if the timeSheetSymbols couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-sheet-symbols")
    @Timed
    public ResponseEntity<TimeSheetSymbols> updateTimeSheetSymbols(@Valid @RequestBody TimeSheetSymbols timeSheetSymbols) throws URISyntaxException {
        log.debug("REST request to update TimeSheetSymbols : {}", timeSheetSymbols);
        if (timeSheetSymbols.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TimeSheetSymbols result = timeSheetSymbolsService.save(timeSheetSymbols);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeSheetSymbols.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-sheet-symbols : get all the timeSheetSymbols.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of timeSheetSymbols in body
     */
    @GetMapping("/time-sheet-symbols")
    @Timed
    public ResponseEntity<List<TimeSheetSymbols>> getAllTimeSheetSymbols(Pageable pageable) {
        log.debug("REST request to get a page of TimeSheetSymbols");
        Page<TimeSheetSymbols> page = timeSheetSymbolsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-sheet-symbols");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /time-sheet-symbols/:id : get the "id" timeSheetSymbols.
     *
     * @param id the id of the timeSheetSymbols to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeSheetSymbols, or with status 404 (Not Found)
     */
    @GetMapping("/time-sheet-symbols/{id}")
    @Timed
    public ResponseEntity<TimeSheetSymbols> getTimeSheetSymbols(@PathVariable UUID id) {
        log.debug("REST request to get TimeSheetSymbols : {}", id);
        Optional<TimeSheetSymbols> timeSheetSymbols = timeSheetSymbolsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeSheetSymbols);
    }

    /**
     * DELETE  /time-sheet-symbols/:id : delete the "id" timeSheetSymbols.
     *
     * @param id the id of the timeSheetSymbols to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-sheet-symbols/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeSheetSymbols(@PathVariable UUID id) {
        log.debug("REST request to delete TimeSheetSymbols : {}", id);
        timeSheetSymbolsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
