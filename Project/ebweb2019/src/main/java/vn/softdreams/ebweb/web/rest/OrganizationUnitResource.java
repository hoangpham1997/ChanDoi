package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.DeleteOrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.OrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.*;
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
 * REST controller for managing OrganizationUnit.
 */
@RestController
@RequestMapping("/api")
public class OrganizationUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUnitResource.class);

    private static final String ENTITY_NAME = "organizationUnit";

    private final OrganizationUnitService organizationUnitService;

    public OrganizationUnitResource(OrganizationUnitService organizationUnitService) {
        this.organizationUnitService = organizationUnitService;
    }

    /**
     * POST  /organization-units : Create a new organizationUnit.
     *
     * @param organizationUnit the organizationUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizationUnit, or with status 400 (Bad Request) if the organizationUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organization-units")
    @Timed
    public ResponseEntity<OrganizationUnitSaveDTO> createOrganizationUnit(@RequestBody OrganizationUnit organizationUnit) throws URISyntaxException {
        if (organizationUnit.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationUnitSaveDTO result = organizationUnitService.save(organizationUnit);
        if (result.getOrganizationUnit().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/units/" + result.getOrganizationUnit().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getOrganizationUnit().getId().toString()))
                .body(result);
        }
    }

    @PostMapping("/organization-units/big-org")
    @Timed
    public ResponseEntity<OrganizationUnitSaveDTO> createOrganizationUnitBig(@RequestBody OrganizationUnit organizationUnit) throws URISyntaxException {
        if (organizationUnit.getId() != null) {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationUnitSaveDTO result = organizationUnitService.saveBigOrg(organizationUnit);
        if (result.getOrganizationUnit().getId() == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.created(new URI("/api/units/" + result.getOrganizationUnit().getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getOrganizationUnit().getId().toString()))
                .body(result);
        }
    }

    /**
     * PUT  /organization-units : Updates an existing organizationUnit.
     *
     * @param organizationUnit the organizationUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationUnit,
     * or with status 400 (Bad Request) if the organizationUnit is not valid,
     * or with status 500 (Internal Server Error) if the organizationUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organization-units")
    @Timed
    public ResponseEntity<OrganizationUnitSaveDTO> updateOrganizationUnit(@RequestBody OrganizationUnit organizationUnit) throws URISyntaxException {
        if (organizationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationUnitSaveDTO result = organizationUnitService.save(organizationUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationUnit.getId().toString()))
            .body(result);
    }

    @PutMapping("/organization-units/big-org")
    @Timed
    public ResponseEntity<OrganizationUnitSaveDTO> updateOrganizationUnitBig(@RequestBody OrganizationUnit organizationUnit) throws URISyntaxException {
        if (organizationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationUnitSaveDTO result = organizationUnitService.saveBigOrg(organizationUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationUnit.getId().toString()))
            .body(result);
    }

    @GetMapping("/organization-units/find-all-organization-units-search")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getOrganizationUnitSearch(Pageable pageable,
                                                                            @RequestParam(required = false) String organizationUnitSearch) throws URISyntaxException {
        log.debug("REST request to get a page of OrganizationUnits");
        ObjectMapper objectMapper = new ObjectMapper();
        OrganizationUnitSearchDTO organizationUnitSearchDTO = new OrganizationUnitSearchDTO();
        try {
            organizationUnitSearchDTO = objectMapper.readValue(organizationUnitSearch, OrganizationUnitSearchDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Page<OrganizationUnit> page = organizationUnitService.findAllBigOrgSearch(pageable, organizationUnitSearchDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organization-units : get all the organizationUnits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationUnits in body
     */
    @GetMapping("/organization-units")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnits(Pageable pageable) {
        log.debug("REST request to get a page of OrganizationUnits");
        Page<OrganizationUnit> page = organizationUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organization-units/big-org")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitsBig(Pageable pageable) {
        log.debug("REST request to get a page of OrganizationUnits");
        Page<OrganizationUnit> page = organizationUnitService.findAllBigOrg(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organization-units/list-big-org")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitsBig(@RequestParam(required = false) Long userId) {
        log.debug("REST request to get a page of OrganizationUnits");
        List<OrganizationUnit> page = organizationUnitService.findAllBigOrg(userId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/organization-units/list-user-package")
    @Timed
    public ResponseEntity<List<EbUserPackage>> getAllUSerPackage(@RequestParam(required = false) Long userId) {
        log.debug("REST request to get all User Package");
        List<EbUserPackage> page = organizationUnitService.findAllEbUSerPackage(userId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /organization-units : get all the organizationUnits.
     * add by namnh
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizationUnits in body
     */
    @GetMapping("/organization-units/getAllOrganizationUnits")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllEbOrganizationUnits(Pageable pageable) {
        log.debug("REST request to get a page of OrganizationUnits");
        Page<OrganizationUnit> page = organizationUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units/getAllOrganizationUnits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organization-units/list-total-big-org")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllTotalOrganizationUnitsBig(@RequestParam(required = false) Long userId) {
        log.debug("REST request to get a page of OrganizationUnits");
        List<OrganizationUnit> page = organizationUnitService.findTotalAllBigOrg();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /organization-units/:id : get the "id" organizationUnit.
     *
     * @param id the id of the organizationUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationUnit, or with status 404 (Not Found)
     */
    @GetMapping("/organization-units/{id}")
    @Timed
    public ResponseEntity<OrganizationUnit> getOrganizationUnit(@PathVariable UUID id) {
        log.debug("REST request to get OrganizationUnit : {}", id);
        Optional<OrganizationUnit> organizationUnit = organizationUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationUnit);
    }

    /**
     * DELETE  /organization-units/:id : delete the "id" organizationUnit.
     *
     * @param id the id of the organizationUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organization-units/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganizationUnit(@PathVariable UUID id) {
        log.debug("REST request to delete OrganizationUnit : {}", id);
        organizationUnitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * @param unitType
     * @return
     * @Author hieugie
     * <p>
     * Lấy ra danh sách tổ chức theo unit Type
     * Cấp 0: Tổng cty/cty, 1: Chi nhánh, 2: Văn phòng đại diện, 3: Địa điểm kinh doanh, 4: Phòng ban, 5: Khác
     */
    @GetMapping("/p/organization-units/unit-type")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getOuByUnitType(@RequestParam Long userId,
                                                                  @RequestParam Integer unitType,
                                                                  @RequestParam(required = false) UUID parentId) {
        log.debug("REST request to get a page of OrganizationUnits");
        List<OrganizationUnit> page = organizationUnitService.getOuByUnitType(userId, unitType,
            parentId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @return
     * @Author hieugie
     * <p>
     * Lấy ra danh sách tổ chức theo unit Type
     * Cấp 0: Tổng cty/cty, 1: Chi nhánh, 2: Văn phòng đại diện, 3: Địa điểm kinh doanh, 4: Phòng ban, 5: Khác
     */
    @GetMapping("/p/organization-units/tree")
    @Timed
    public ResponseEntity<ChangeSessionDTO> getOuTreeByUnitType() {
        log.debug("REST request to get a page of OrganizationUnits");
        ChangeSessionDTO page = organizationUnitService.getOuTreeByUnitType();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @return
     * @Author anmt
     */
    @GetMapping("/p/organization-units/tree/getAll")
    @Timed
    public ResponseEntity<List<OrgTreeTableDTO>> getAllOuTreeByUnitType() {
        log.debug("REST request to get a page of OrganizationUnits");
        List<OrgTreeTableDTO> page = organizationUnitService.getAllOuTreeByUnitType();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @return
     * @Author anmt
     */
    @GetMapping("/p/organization-units/tree/getAll-By-OrgID")
    @Timed
    public ResponseEntity<List<OrgTreeTableDTO>> getAllOuTreeByUnitTypeByOrgID(@RequestParam String userLogin) {
        log.debug("REST request to get a page of OrganizationUnits");
        List<OrgTreeTableDTO> page = organizationUnitService.getAllOuTreeByUnitTypeByOrgID(userLogin);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/organization-units/active")
    @Timed
    public ResponseEntity<List<OrganizationUnitDTO>> getAllOrganizationUnitsActive() {
        log.debug("REST request to get a page of OrganizationUnits");
        Page<OrganizationUnitDTO> page = organizationUnitService.getAllOrganizationUnitsActive();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units/active");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organization-units/by-companyID")
    @Timed
    public ResponseEntity<OrganizationUnitDTO> getOrganizationUnitsByCompanyID() {
        log.debug("REST request to get a page of OrganizationUnits");
        Optional<OrganizationUnitDTO> page = organizationUnitService.getOrganizationUnitsByCompanyID();
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-units/active");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        return ResponseUtil.wrapOrNotFound(page);
    }

    @GetMapping("/organization-units/find-all-organization-units-active-companyid")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitActiveCompanyID() {
        log.debug("REST request to get a page of Organization Units");
        List<OrganizationUnit> page = organizationUnitService.findAllActive();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/organization-units/find-all-organization-units-by-companyid")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitByCompanyID() {
        log.debug("REST request to get a page of Organization Units");
        List<OrganizationUnit> page = organizationUnitService.getAllOrganizationUnitByCompanyID();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/organization-units/recursive-organization-units-by-parent-id")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getOrganizationUnitByParentID() {
        List<OrganizationUnit> organizationUnitList = organizationUnitService.getOrganizationUnitByParentID();
        return new ResponseEntity<>(organizationUnitList, HttpStatus.OK);
    }

    @GetMapping("/organization-units/recursive-organization-units-by-parent-id-popup")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getOrganizationUnitByParentIDPopup() {
        List<OrganizationUnit> organizationUnitList = organizationUnitService.getOrganizationUnitByParentIDPopup();
        return new ResponseEntity<>(organizationUnitList, HttpStatus.OK);
    }

    @PostMapping("/organization-units/delete-by-organization-unit-id")
    @Timed
    public ResponseEntity<Void> deleteByOrganizationUnitID(@RequestBody(required = false) DeleteOrganizationUnitDTO org) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        DeleteOrganizationUnitDTO deleteOrganizationUnitDTO = null;
//        try {
//            deleteOrganizationUnitDTO = objectMapper.readValue(org, DeleteOrganizationUnitDTO.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        organizationUnitService.deleteByOrganizationUnitID(org);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param listID
     * @return
     * @author anmt
     */
    @GetMapping("/organization-units/find-all-by-list-id")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitByListID(@RequestParam List<UUID> listID) {
        log.debug("REST request to get a page of Organization Units");
        List<OrganizationUnit> page = organizationUnitService.findAllByListID(listID);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/organization-units/get-posted-date")
    @Timed
    public ResponseEntity<LocalDate> getPostedDate() {
        log.debug("REST request to get a page of Organization Units");
        LocalDate startDate = organizationUnitService.getPostedDate();
        return new ResponseEntity<>(startDate, HttpStatus.OK);
    }

    @GetMapping("/organization-units/get-tree-organization-unit")
    @Timed
    public ResponseEntity<List<TreeOrganizationUnitDTO>> getTreeOrganizationUnit() {
        List<TreeOrganizationUnitDTO> treeOrganizationUnit = organizationUnitService.getTreeOrganizationUnit();
        return new ResponseEntity<>(treeOrganizationUnit, HttpStatus.OK);
    }

    /**
     * @author anmt
     * @return
     */
    @GetMapping("/organization-units/get-tree-organization-unit-of-userid")
    @Timed
    public ResponseEntity<List<TreeOrganizationUnitDTO>> getTreeOrganizationUnitsByOfUserId() {
        List<TreeOrganizationUnitDTO> treeOrganizationUnit = organizationUnitService.getTreeOrganizationUnitByOfUserId();
        return new ResponseEntity<>(treeOrganizationUnit, HttpStatus.OK);
    }

    /**
     * @author namnh
     * @return
     */
    @GetMapping("/organization-units/get-tree-all-organization-unit-of-userid")
    @Timed
    public ResponseEntity<List<TreeOrganizationUnitDTO>> getTreeAllOrganizationUnitsByOfUserId() {
        List<TreeOrganizationUnitDTO> treeOrganizationUnit = organizationUnitService.getTreeAllOrganizationUnitsByOfUserId();
        return new ResponseEntity<>(treeOrganizationUnit, HttpStatus.OK);
    }

    @GetMapping("/organization-units/find-all-organization-units-active-companyid-except-id")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitActiveExceptID(@RequestParam(required = false) UUID id) {
        log.debug("REST request to get a page of OrganizationUnit");
        List<OrganizationUnit> page = organizationUnitService.findAllExceptID(id);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * @author anmt
     * @return
     */
    @GetMapping("/organization-units/get-all-tree-organization-unit")
    @Timed
    public ResponseEntity<List<TreeOrganizationUnitDTO>> getAllTreeOrganizationUnits() {
        List<TreeOrganizationUnitDTO> treeOrganizationUnit = organizationUnitService.getAllTreeOrganizationUnitDTO();
        return new ResponseEntity<>(treeOrganizationUnit, HttpStatus.OK);
    }

    /**
     * @author anmt
     * @return
     */
    @GetMapping("/organization-units/get-list-organization-units")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitByParentID() {
        List<OrganizationUnit> organizationUnitList = organizationUnitService.getAllOrganizationUnitByListParentID();
        return new ResponseEntity<>(organizationUnitList, HttpStatus.OK);
    }

    /**
     * @author chuongnv
     * @return
     */
    @GetMapping("/organization-units/get-companys-by-id")
    @Timed
    public ResponseEntity<ChangeSessionDTO> getChildCompanyByID() {
        ChangeSessionDTO organizationUnit = organizationUnitService.getChildCompanyByID();
        return new ResponseEntity<>(organizationUnit, HttpStatus.OK);
    }

    /**
     * @author anmt
     * @return
     */
    @GetMapping("/organization-units/get-list-company-cbb")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllOrganizationUnitIsCompany() {
        log.debug("REST request to get a page of Organization Units");
        List<OrganizationUnit> page = organizationUnitService.findAllByUserId();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/organization-units/get-all-departments")
    @Timed
    public ResponseEntity<List<OrganizationUnit>> getAllDepartments(@RequestParam(required = false) UUID orgID,
                                                                    @RequestParam(required = false) boolean isDependent
    ) {
        log.debug("REST request to get a page of Organization Units");
        List<OrganizationUnit> page = organizationUnitService.findAllDepartments(orgID, isDependent);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @DeleteMapping("/organization-units/delete-by-big-organization-unit-id")
    @Timed
    public ResponseEntity<Void> deleteByBigOrganizationUnitID(@RequestParam(required = false) String org) {
        ObjectMapper objectMapper = new ObjectMapper();
        DeleteOrganizationUnitDTO deleteOrganizationUnitDTO = null;
        try {
            deleteOrganizationUnitDTO = objectMapper.readValue(org, DeleteOrganizationUnitDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        organizationUnitService.deleteByBigOrganizationUnitID(deleteOrganizationUnitDTO.getOrganizationUnit().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
