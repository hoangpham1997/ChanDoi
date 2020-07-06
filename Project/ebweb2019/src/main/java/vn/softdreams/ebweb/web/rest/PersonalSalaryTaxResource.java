package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.PersonalSalaryTax;
import vn.softdreams.ebweb.service.PersonalSalaryTaxService;
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
 * REST controller for managing PersonalSalaryTax.
 */
@RestController
@RequestMapping("/api")
public class PersonalSalaryTaxResource {

    private final Logger log = LoggerFactory.getLogger(PersonalSalaryTaxResource.class);

    private static final String ENTITY_NAME = "personalSalaryTax";

    private final PersonalSalaryTaxService personalSalaryTaxService;

    public PersonalSalaryTaxResource(PersonalSalaryTaxService personalSalaryTaxService) {
        this.personalSalaryTaxService = personalSalaryTaxService;
    }

    /**
     * POST  /personal-salary-taxes : Create a new personalSalaryTax.
     *
     * @param personalSalaryTax the personalSalaryTax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalSalaryTax, or with status 400 (Bad Request) if the personalSalaryTax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personal-salary-taxes")
    @Timed
    public ResponseEntity<PersonalSalaryTax> createPersonalSalaryTax(@Valid @RequestBody PersonalSalaryTax personalSalaryTax) throws URISyntaxException {
        log.debug("REST request to save PersonalSalaryTax : {}", personalSalaryTax);
        if (personalSalaryTax.getId() != null) {
            throw new BadRequestAlertException("A new personalSalaryTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalSalaryTax result = personalSalaryTaxService.save(personalSalaryTax);
        return ResponseEntity.created(new URI("/api/personal-salary-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personal-salary-taxes : Updates an existing personalSalaryTax.
     *
     * @param personalSalaryTax the personalSalaryTax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalSalaryTax,
     * or with status 400 (Bad Request) if the personalSalaryTax is not valid,
     * or with status 500 (Internal Server Error) if the personalSalaryTax couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personal-salary-taxes")
    @Timed
    public ResponseEntity<PersonalSalaryTax> updatePersonalSalaryTax(@Valid @RequestBody PersonalSalaryTax personalSalaryTax) throws URISyntaxException {
        log.debug("REST request to update PersonalSalaryTax : {}", personalSalaryTax);
        if (personalSalaryTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonalSalaryTax result = personalSalaryTaxService.save(personalSalaryTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personalSalaryTax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personal-salary-taxes : get all the personalSalaryTaxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personalSalaryTaxes in body
     */
    @GetMapping("/personal-salary-taxes")
    @Timed
    public ResponseEntity<List<PersonalSalaryTax>> getAllPersonalSalaryTaxes(Pageable pageable) {
        log.debug("REST request to get a page of PersonalSalaryTaxes");
        Page<PersonalSalaryTax> page = personalSalaryTaxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personal-salary-taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /personal-salary-taxes/:id : get the "id" personalSalaryTax.
     *
     * @param id the id of the personalSalaryTax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalSalaryTax, or with status 404 (Not Found)
     */
    @GetMapping("/personal-salary-taxes/{id}")
    @Timed
    public ResponseEntity<PersonalSalaryTax> getPersonalSalaryTax(@PathVariable UUID id) {
        log.debug("REST request to get PersonalSalaryTax : {}", id);
        Optional<PersonalSalaryTax> personalSalaryTax = personalSalaryTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalSalaryTax);
    }

    /**
     * DELETE  /personal-salary-taxes/:id : delete the "id" personalSalaryTax.
     *
     * @param id the id of the personalSalaryTax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personal-salary-taxes/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonalSalaryTax(@PathVariable UUID id) {
        log.debug("REST request to delete PersonalSalaryTax : {}", id);
        personalSalaryTaxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
