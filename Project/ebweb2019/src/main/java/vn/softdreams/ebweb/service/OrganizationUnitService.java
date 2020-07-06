package vn.softdreams.ebweb.service;

import org.springframework.web.bind.annotation.RequestParam;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.DeleteOrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.OrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing OrganizationUnit.
 */
public interface OrganizationUnitService {

    /**
     * Save a organizationUnit.
     *
     * @param organizationUnit the entity to save
     * @return the persisted entity
     */
    OrganizationUnitSaveDTO save(OrganizationUnit organizationUnit);

    OrganizationUnitSaveDTO saveBigOrg(OrganizationUnit organizationUnit);

    /**
     * Get all the organizationUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrganizationUnit> findAll(Pageable pageable);

    Page<OrganizationUnit> findAllBigOrg(Pageable pageable);

    Page<OrganizationUnit> findAllBigOrgSearch(Pageable pageable, OrganizationUnitSearchDTO organizationUnitSearchDTO);

    List<OrganizationUnit> findAllBigOrg(Long userDTO);

    List<OrganizationUnit> findTotalAllBigOrg();

    List<EbUserPackage> findAllEbUSerPackage(Long userDTO);

    /**
     * Get all the organizationUnits.
     * add by namnh
     *
     * @return the list of entities
     */
    Page<OrganizationUnit> findAll();


    /**
     * Get the "id" organizationUnit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganizationUnit> findOne(UUID id);

    /**
     * Delete the "id" organizationUnit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @param id
     * @return
     * @author Hautv
     */
    List<String> GetListOrderFixCodeParentID(UUID id);

    /**
     * @Author hieugie
     *
     * Lấy ra danh sách tổ chức theo unit Type
     * Cấp 0: Tổng cty/cty, 1: Chi nhánh, 2: Văn phòng đại diện, 3: Địa điểm kinh doanh, 4: Phòng ban, 5: Khác
     *
     * @param unitType
     * @param id
     * @return
     */
    List<OrganizationUnit> getOuByUnitType(Long userId, Integer unitType, UUID id);

    Page<OrganizationUnitDTO> getAllOrganizationUnitsActive();

    Optional<OrganizationUnitDTO> getOrganizationUnitsByCompanyID();

    List<OrganizationUnit> findAllActive();
    List<OrganizationUnit> getAllOrganizationUnitByCompanyID();

    List<OrganizationUnit> getOrganizationUnitByParentID();
    List<OrganizationUnit> getOrganizationUnitByParentIDPopup();

    List<OrganizationUnit> findAllByCompanyID();

    List<TreeOrganizationUnitDTO> getTreeOrganizationUnit();


    List<TreeOrganizationUnitDTO> getAllTreeOrganizationUnitDTO();


    /**
     * @Author hieugie
     *
     * Lấy ra danh sách tổ chức theo dạng cây
     *
     * @return
     */
    ChangeSessionDTO getOuTreeByUnitType();

    void deleteByOrganizationUnitID(DeleteOrganizationUnitDTO deleteOrganizationUnitDTO);

    void deleteByBigOrganizationUnitID(UUID orgID);

    List<OrgTreeTableDTO> getAllOuTreeByUnitType();

    List<OrgTreeTableDTO> getAllOuTreeByUnitTypeByOrgID(String userLogin);

    List<OrganizationUnit> findAllByListID(List<UUID> listID);

    LocalDate getPostedDate();

    List<OrganizationUnit> findAllExceptID(UUID id);

    List<OrganizationUnit> getAllOrganizationUnitByListParentID();

    ChangeSessionDTO getChildCompanyByID();

    List<TreeOrganizationUnitDTO> getTreeOrganizationUnitByOfUserId();
    List<TreeOrganizationUnitDTO> getTreeAllOrganizationUnitsByOfUserId();

    List<OrganizationUnit> findAllByUserId();

    List<OrganizationUnit> getListChildOrganizationUnit(List<OrganizationUnit> organizationUnits, UUID childID);

    List<OrganizationUnit> getListChildOrganizationUnitCustom(List<OrganizationUnit> organizationUnits, UUID childID);

    List<OrganizationUnit> getListParentOrganizationUnit(List<OrganizationUnit> organizationUnits, UUID parentID);

    UUID findTopParent(UUID orgID);

    List<OrganizationUnit> findAllDepartments(UUID orgID, boolean isDependent);
}
