package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.Repository;
import vn.softdreams.ebweb.service.RepositoryService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositorySaveDTO;
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
 * REST controller for managing Repository.
 */
@RestController
@RequestMapping("/api")
public class RepositoryResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryResource.class);

    private static final String ENTITY_NAME = "repository";

    private final RepositoryService repositoryService;

    public RepositoryResource(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * POST  /repositories : Create a new repository.
     *
     * @param repository the repository to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repository, or with status 400 (Bad Request) if the repository has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repositories")
    @Timed
    public ResponseEntity<RepositorySaveDTO> createRepository(@Valid @RequestBody Repository repository) throws URISyntaxException {
    log.debug("REST request to save Repository : {}", repository);
        if (repository.getId() != null) {
        throw new BadRequestAlertException("A new repository cannot already have an ID", ENTITY_NAME, "idexists");
    }
        RepositorySaveDTO result = repositoryService.save(repository);
        if (result.getRepository().getId() == null) {
        return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
        return ResponseEntity.created(new URI("/api/repositories/" + result.getRepository().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getRepository().getId().toString()))
            .body(result);
    }
}

    /**
     * PUT  /repositories : Updates an existing repository.
     *
     * @param repository the repository to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repository,
     * or with status 400 (Bad Request) if the repository is not valid,
     * or with status 500 (Internal Server Error) if the repository couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repositories")
    @Timed
    public ResponseEntity<RepositorySaveDTO> updateRepository(@Valid @RequestBody Repository repository) throws URISyntaxException {
        log.debug("REST request to update Repository : {}", repository);
        if (repository.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RepositorySaveDTO result = repositoryService.save(repository);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, repository.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repositories : get all the repositories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of repositories in body
     */
    @GetMapping("/repositories")
    @Timed
    public ResponseEntity<List<Repository>> getAllRepositories(Pageable pageable) {
        log.debug("REST request to get a page of Repositories");
        Page<Repository> page = repositoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repositories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/repositories/findAllByCompanyID")
    @Timed
    public ResponseEntity<List<Repository>> getAllRepositoriesByCompanyID(Pageable pageable) {
        log.debug("REST request to get a page of Repositories");
        List<Repository> page = repositoryService.findAllByCompanyID();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repositories");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /unit : get all the repository.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountingObjects in body
     */

    @GetMapping("/repositories/pageable-all-repositories")
    @Timed
    public ResponseEntity<List<Repository>> pageableAllRepositories(Pageable pageable, @RequestParam(required = false) Boolean isGetAllCompany) {
        log.debug("REST request to get a page of AccountingObjects");
        Page<Repository> page = repositoryService.pageableAllRepositories(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repositories/pageable-all-repositories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/repositories/list-all-repositories-combobox")
    @Timed
    public ResponseEntity<List<Repository>> pageableAllRepositories(@RequestParam(required = false) Boolean isGetAllCompany) {
        log.debug("REST request to get a page of AccountingObjects");
        List<Repository> lst = repositoryService.listAllRepositories(isGetAllCompany);
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }
    /**
     * GET  /repositories/:id : get the "id" repository.
     *
     * @param id the id of the repository to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repository, or with status 404 (Not Found)
     */
    @GetMapping("/repositories/{id}")
    @Timed
    public ResponseEntity<Repository> getRepository(@PathVariable UUID id) {
        log.debug("REST request to get Repository : {}", id);
        Optional<Repository> repository = repositoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repository);
    }

    /**
     * DELETE  /repositories/:id : delete the "id" repository.
     *
     * @param id the id of the repository to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repositories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRepository(@PathVariable UUID id) {
        log.debug("REST request to delete Repository : {}", id);
        repositoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/repositories/delete-list-repository")
    @Timed
    public ResponseEntity<HandlingResultDTO> recordGeneralLedgerUnit(@Valid @RequestBody List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = repositoryService.deleteRepository(uuids);
        return ResponseEntity.status(HttpStatus.OK).body(handlingResultDTO);
    }

    /**
     * get kho combobox
     * @return
     */
    @GetMapping("/repositories/combobox")
    @Timed
    public ResponseEntity<List<Repository>> getRepositoryCombobox(@RequestParam(required = false) Boolean similarBranch) {
        log.debug("REST request to get a page of Repositories");
        List<Repository> page = repositoryService.getRepositoryCombobox(similarBranch);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repositories");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * get kho combobox
     * @return
     */
    @GetMapping("/repositories/get-all")
    @Timed
    public ResponseEntity<List<Repository>> getRepositoryComboboxGetAll() {
        log.debug("REST request to get a page of Repositories");
        List<Repository> page = repositoryService.getRepositoryComboboxGetAll();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repositories");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/repositories/forReport")
    @Timed
    public ResponseEntity<List<Repository>> getRepositoryReport(@RequestParam(required = false) UUID companyID, @RequestParam(required = false) Boolean similarBranch) {
        log.debug("REST request to get a page of Repositories");
        List<Repository> page = repositoryService.getRepositoryReport(companyID, similarBranch);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
