package vn.softdreams.ebweb.web.rest.cashandbank;

import com.codahale.metrics.annotation.Timed;
import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import vn.softdreams.ebweb.service.MCAuditDetailMemberService;
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
 * REST controller for managing MCAuditDetailMember.
 */
@RestController
@RequestMapping("/api")
public class MCAuditDetailMemberResource {

    private final Logger log = LoggerFactory.getLogger(MCAuditDetailMemberResource.class);

    private static final String ENTITY_NAME = "mCAuditDetailMember";

    private final MCAuditDetailMemberService mCAuditDetailMemberService;

    public MCAuditDetailMemberResource(MCAuditDetailMemberService mCAuditDetailMemberService) {
        this.mCAuditDetailMemberService = mCAuditDetailMemberService;
    }

    /**
     * POST  /mc-audit-detail-members : Create a new mCAuditDetailMember.
     *
     * @param mCAuditDetailMember the mCAuditDetailMember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mCAuditDetailMember, or with status 400 (Bad Request) if the mCAuditDetailMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mc-audit-detail-members")
    @Timed
    public ResponseEntity<MCAuditDetailMember> createMCAuditDetailMember(@RequestBody MCAuditDetailMember mCAuditDetailMember) throws URISyntaxException {
        log.debug("REST request to save MCAuditDetailMember : {}", mCAuditDetailMember);
        if (mCAuditDetailMember.getId() != null) {
            throw new BadRequestAlertException("A new mCAuditDetailMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MCAuditDetailMember result = mCAuditDetailMemberService.save(mCAuditDetailMember);
        return ResponseEntity.created(new URI("/api/mc-audit-detail-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mc-audit-detail-members : Updates an existing mCAuditDetailMember.
     *
     * @param mCAuditDetailMember the mCAuditDetailMember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mCAuditDetailMember,
     * or with status 400 (Bad Request) if the mCAuditDetailMember is not valid,
     * or with status 500 (Internal Server Error) if the mCAuditDetailMember couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mc-audit-detail-members")
    @Timed
    public ResponseEntity<MCAuditDetailMember> updateMCAuditDetailMember(@RequestBody MCAuditDetailMember mCAuditDetailMember) throws URISyntaxException {
        log.debug("REST request to update MCAuditDetailMember : {}", mCAuditDetailMember);
        if (mCAuditDetailMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MCAuditDetailMember result = mCAuditDetailMemberService.save(mCAuditDetailMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mCAuditDetailMember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mc-audit-detail-members : get all the mCAuditDetailMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mCAuditDetailMembers in body
     */
    @GetMapping("/mc-audit-detail-members")
    @Timed
    public ResponseEntity<List<MCAuditDetailMember>> getAllMCAuditDetailMembers(Pageable pageable) {
        log.debug("REST request to get a page of MCAuditDetailMembers");
        Page<MCAuditDetailMember> page = mCAuditDetailMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mc-audit-detail-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mc-audit-detail-members/:id : get the "id" mCAuditDetailMember.
     *
     * @param id the id of the mCAuditDetailMember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mCAuditDetailMember, or with status 404 (Not Found)
     */
    @GetMapping("/mc-audit-detail-members/{id}")
    @Timed
    public ResponseEntity<MCAuditDetailMember> getMCAuditDetailMember(@PathVariable UUID id) {
        log.debug("REST request to get MCAuditDetailMember : {}", id);
        Optional<MCAuditDetailMember> mCAuditDetailMember = mCAuditDetailMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mCAuditDetailMember);
    }

    /**
     * DELETE  /mc-audit-detail-members/:id : delete the "id" mCAuditDetailMember.
     *
     * @param id the id of the mCAuditDetailMember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mc-audit-detail-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteMCAuditDetailMember(@PathVariable UUID id) {
        log.debug("REST request to delete MCAuditDetailMember : {}", id);
        mCAuditDetailMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
