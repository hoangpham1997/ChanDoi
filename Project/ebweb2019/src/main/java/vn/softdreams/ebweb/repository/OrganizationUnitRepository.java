package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.OrganizationUnitDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, UUID>, OrganizationUnitRepositoryCustom {

    /**
     * @param unitType
     * @return
     * @Author hieugie
     * <p>
     * Lấy ra danh sách tổ chức theo unit Type
     * Cấp 0: Tổng cty/cty, 1: Chi nhánh, 2: Văn phòng đại diện, 3: Địa điểm kinh doanh, 4: Phòng ban, 5: Khác
     */
    @Query(value = "select a.* from EbOrganizationUnit a " +
        "               left join EbUserOrganizationUnit b on a.ID = b.OrgId " +
        "               left join EbUser c on b.userid = c.id " +
        "           where a.UnitType = ?1 and c.id = ?2", nativeQuery = true)
    List<OrganizationUnit> findByUnitType(Integer unitType, Long userId);

    @Query(value = "select * from EbOrganizationUnit " +
        "where UnitType = 0 and ID not in ( " +
        "    select EbUserPackage.CompanyId from EbUserPackage " +
        "    )", nativeQuery = true)
    List<OrganizationUnit> findAllByUnitType(Integer unitType);


    @Query(value = "select * from EbOrganizationUnit " +
        "where UnitType = 0 and ID in ( " +
        "    select EbUserPackage.CompanyId from EbUserPackage " +
        "    )", nativeQuery = true)
    List<OrganizationUnit> findTotalAllByUnitType();

    @Query(value = "select a.* from EbUserPackage a left join EbOrganizationUnit b on a.UserId = b.UserId", nativeQuery = true)
    List<EbUserPackage> findAllUSerPackage(Integer unitType);

    /**
     * @param parentID
     * @return
     * @Author hieugie
     * <p>
     * Lấy ra danh sách tổ chức theo unit cha
     */
    @Query(value = "SELECT * FROM EbOrganizationUnit where parentID = ?1 AND ID <> ?1  ORDER BY UnitType, ParentID, OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findByParentID(UUID parentID);

    @Query(value = "SELECT * FROM EbOrganizationUnit where parentID = ?1 AND ID <> ?1  AND UnitType < 2 ORDER BY UnitType, ParentID, OrganizationUnitName", nativeQuery = true)
    List<OrganizationUnit> findByParentIDPopup(UUID parentID);

    @Query(value = "SELECT * FROM EbOrganizationUnit where parentID = ?1 AND ID <> ?1  AND UnitType < 2 ORDER BY UnitType, ParentID, OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findByParentIDCbb(UUID parentID);

    @Query(value = "select a.* from EbOrganizationUnit a left join EbUserOrganizationUnit b on a.ID = b.OrgId left join EbUser c on b.userid = c.id where a.parentId = ?1 and c.id = ?2", nativeQuery = true)
//    @EntityGraph(attributePaths = {"groups", "groups.authorities"})
    List<OrganizationUnit> findByUserIdAndParentID(UUID id, Long userId);


    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<OrganizationUnit> findAll();

    @Query(value = "select id from EbOrganizationUnit", nativeQuery = true)
    List<UUID> findAllOrg();

    @Query(value = "select id, OrganizationUnitCode, OrganizationUnitName from EbOrganizationUnit where IsActive = 1 and UnitType = 4", nativeQuery = true)
    List<OrganizationUnitDTO> getAllOrganizationUnitsActive();

    @Query(value = "select id, OrganizationUnitCode, OrganizationUnitName from ebOrganizationUnit where CompanyID = ?1", nativeQuery = true)
    Optional<OrganizationUnitDTO> getOrganizationUnitsByCompanyID(Long userOptional);

    @Query(value = "select * from ebOrganizationUnit b where b.CompanyID = ?1 AND isActive = 1 AND UnitType = 4 ORDER BY OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findAllByIsActive(UUID companyID);

    @Query(value = "select * from ebOrganizationUnit b where b.CompanyID = ?1", nativeQuery = true)
    List<OrganizationUnit> findAllByCompanyID(UUID companyID);

    @Query(value = "select currentBook from ebUserOrganizationUnit b where b.UserId = ?1 AND b.orgId = ?2", nativeQuery = true)
    String findCurrentBook(Long id, UUID org);

    /**
     * @return
     * @Author hieugie
     * <p>
     * Lấy ra danh sách tổ chức theo dạng cây của 1 user
     */
    @Query(value = "select a.* from EbOrganizationUnit a " +
        "               left join EbUserOrganizationUnit b on a.ID = b.OrgId " +
        "               left join EbUser c on b.userid = c.id " +
        "           where (a.UnitType = 0 or a.UnitType = 1) and c.id = ?1 order by UnitType", nativeQuery = true)
    List<OrganizationUnit> getOuTreeByUnitType(Long id);

    @Modifying
    @Query(value = "update EbUserOrganizationUnit set currentBook = ?2 where id in (select b.id from EbOrganizationUnit a " +
        "               left join EbUserOrganizationUnit b on a.ID = b.OrgId " +
        "           where a.id = ?1)", nativeQuery = true)
    void updateCurrentBook(UUID org, String currentBook);

    @Modifying
    @Query(value = "[InsertOrganizationUnit] ?1, ?2, ?3, ?4", nativeQuery = true)
    void insertDataCategoryToDB(UUID org, String currencyID, Integer unitType, UUID parentID);

    @Modifying
    @Query(value = "[DELETEOrganizationUnit] ?1", nativeQuery = true)
    void deleteDataByCompanyID(UUID org);

    @Modifying
    @Query(value = "update EbOrganizationUnit set userID = ?1 where id = ?2 ", nativeQuery = true)
    void updateOrganizationUnit(Long userID, UUID orgID);

    /**
     * @return
     * @Author anmt
     * <p>
     * Lấy ra danh sách tổ chức theo dạng cây của all - bang EbOrganizationUnit
     */
    @Query(value = "select a.* from EbOrganizationUnit a " +
        "           where (a.UnitType = 0 or a.UnitType = 1) order by UnitType", nativeQuery = true)
    List<OrganizationUnit> getAllOuTreeByUnitType();

    @Query(value = "select count(*) from EbOrganizationUnit where UPPER(OrganizationUnitCode) = UPPER (?1) and CompanyID = ?2", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndOrganizationUnitCode(String organizationUnitCode, UUID org);

    @Query(value = "select id from EbOrganizationUnit where UPPER(OrganizationUnitCode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByDepartmentCode(String departmentCode, UUID companyId);

    @Query(nativeQuery = true, value = "select CONVERT(varchar, StartDate, 103) from EbOrganizationUnit where ID = ?1")
    String findStartDateById(UUID id);

    @Query(nativeQuery = true, value = "select * from EbOrganizationUnit where ParentID = ?1 AND UnitType = ?2")
    List<OrganizationUnit> getChildCom(UUID org, int chiNhanh);

    @Query(value = "select * from EbOrganizationUnit b where ParentID = ?1", nativeQuery = true)
    List<OrganizationUnit> findByParentOrganizationUnitID(UUID parentAccountID);

    @Query(value = "select * from EbOrganizationUnit b where ParentID = ?1 AND (UnitType = 0 OR UnitType = 1)", nativeQuery = true)
    List<OrganizationUnit> findByParentOrganizationUnitIDCustom(UUID parentAccountID);

    @Query(value = "select * from EbOrganizationUnit b where ParentID = ?1 AND UnitType = 1", nativeQuery = true)
    List<OrganizationUnit> findByParentOrganizationUnitAndUnitType1(UUID parentAccountID);

    @Query(value = "select * from EbOrganizationUnit b where ID = ?1", nativeQuery = true)
    OrganizationUnit findByChildOrganizationUnitID(UUID parentAccountID);

    @Query(value = "select count(*) from EbOrganizationUnit b where ParentID = ?1", nativeQuery = true)
    int countChildByParentID(UUID parentID);

    @Query(value = "SELECT * from EbOrganizationUnit WHERE (ID = ?1) OR (ParentID = ?1 AND UnitType = 1)", nativeQuery = true)
    List<OrganizationUnit> findAllCompanyAndBranch(UUID companyID);

    @Query(value = "select a.* from EbOrganizationUnit a " +
        "           where (a.ID = ?1 OR a.ParentID = ?1 ) AND (a.UnitType = 0 or a.UnitType = 1) order by UnitType", nativeQuery = true)
    List<OrganizationUnit> getAllOuTreeByUnitTypeByOrgID(UUID org);

    @Query(value = "select a.* from EbOrganizationUnit a " +
        "                     left join EbUserOrganizationUnit b on a.ID = b.OrgId " +
        "                     left join EbUser c on b.userid = c.id " +
        "                  where (a.UnitType = 0 or a.UnitType = 1) and c.id = ?2 AND (a.ID = ?1 OR a.ParentID = ?1 ) AND a.IsActive = 1 order by a.UnitType, a.OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> getAllOuTreeActiveByUnitTypeByOrgID(UUID org, Long id);

    @Query(value = "select count(*) from EbOrganizationUnit where UnitType in (0,1,2,3) and ParentID = ?1", nativeQuery = true)
    int countByUnitTypeAndParentID(UUID CompanyId);

    @Query(value = "select * from EbOrganizationUnit where UnitType = 1 and ParentID = ?1", nativeQuery = true)
    List<OrganizationUnit> findOrganizationUnitByBranch(UUID CompanyId);

    @Query(value = "select * from EbOrganizationUnit where UnitType in (0, 1) and ParentID = ?1", nativeQuery = true)
    List<OrganizationUnit> findOrganizationUnitByBranchAndOrg(UUID CompanyId);

    @Query(value = "select count(*) from ViewVoucherNo where CompanyID = ?1 ", nativeQuery = true)
    int countVoucherNo(UUID CompanyId);

    @Query(value = "select count(*) from ViewCheckConstraint where DepartmentID = ?1 ", nativeQuery = true)
    int countDetailsVoucherNo(UUID CompanyId);

    @Query(value = "select * from EbOrganizationUnit where UnitType = 4 and IsActive =1 and ParentID = ?1 order by OrganizationUnitCode ", nativeQuery = true)
    List<OrganizationUnit> findAllByUnitTypeAndParentIDIsOrderByOrganizationUnitCode(UUID parentID);

    @Query(value = "select a.* from EbOrganizationUnit a " +
        "           where a.UserId = ?1 order by a.UnitType, a.OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findAllByUserId(Long userId);

    @Query(value = "select * from EbOrganizationUnit " +
        "where (ID = ?2 or UserId = ?1 or (UnitType = 1 and (ParentID in (select ID from EbOrganizationUnit where UserId = ?1)))" +
        " or (UnitType = 1 and (ParentID = ?2))) " +
        "order by UnitType, OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findAllCompanyAndBranchByUserId(Long id, UUID orgID);

    @Modifying
    @Query(value = "delete from EbOrganizationUnit where ID = ?1 ", nativeQuery = true)
    void deleteOrganizationUnitById(UUID orgId);

    @Modifying
    @Query(value = "delete from EbOrganizationUnit where ParentID = ?1 ", nativeQuery = true)
    void deleteOrganizationUnitByParentId(UUID orgId);

    @Query(value = "select count(*) from EbOrganizationUnit where TaxCode = ?1", nativeQuery = true)
    Long countTaxCode(String taxCode);

    @Query(value = "select count(*) from EbOrganizationUnit where UnitType = 0 and OrganizationUnitCode = ?1", nativeQuery = true)
    Integer countAllByOrganizationUnitCode(String organizationUnitCode);

    @Query(value = "select * from EbOrganizationUnit where ID = ?1", nativeQuery = true)
    OrganizationUnit findByID(UUID id);

    @Query(value = "select Count(*) from AccountingObject where DepartmentID = ?1", nativeQuery = true)
    int countExistInAccountingObject(UUID orgID);

    @Query(value = "SELECT * FROM EbOrganizationUnit WHERE (ParentID = ?1 And AccType = 0 And UnitType = 1) or id = ?1", nativeQuery = true)
    List<OrganizationUnit> getCompanyAndBranch(UUID id);

    @Query(value = "SELECT * FROM EbOrganizationUnit WHERE ParentID = ?1 AND UnitType = 4 AND IsActive = 1 order by OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findAllDepartment(UUID org);

    @Query(value = "select Count(*) from EbOrganizationUnit where ParentID = ?1", nativeQuery = true)
    int countBranchByParentID(UUID orgID);

    @Query(value = "SELECT COUNT(*) FROM EbOrganizationUnit WHERE (OrganizationUnitCode = ?1 AND UserID = ?2) OR (OrganizationUnitCode = ?1 AND ID = ?3)", nativeQuery = true)
    long checkExistIsTotalPackage(String organizationUnitCode, Integer userID, UUID orgID);

    @Query(value = "SELECT COUNT(*) FROM EbOrganizationUnit WHERE (OrganizationUnitCode = ?1 AND ParentID = ?2) ", nativeQuery = true)
    long checkExist(String organizationUnitCode, UUID parentID);

    @Query(value = "SELECT * FROM EbOrganizationUnit WHERE ParentID = ?1 AND UnitType = 4 ", nativeQuery = true)
    List<OrganizationUnit> getAllOrganizationUnitByCompanyID(UUID org);

    @Query(value = "SELECT c.ComType FROM EbOrganizationUnit a LEFT JOIN EbUserPackage b ON a.ID = b.CompanyId LEFT JOIN EbPackage c ON b.PackageId = c.ID WHERE a.ID = ?1 AND b.CompanyId = ?1", nativeQuery = true)
    Integer findComTypeByOrgID(UUID orgID);

    @Query(value = "SELECT COUNT(*) FROM EbOrganizationUnit WHERE IsActive = 1 AND UnitType = 0 AND OrganizationUnitCode like CONCAT(?1,'%') ", nativeQuery = true)
    Long countByIsActiveTrueAndOrganizationUnitCodeStartingWithIgnoreCase(String organizationUnitCode);

    @Query(value = "SELECT TOP 1 * FROM EbOrganizationUnit WHERE IsActive = 1 AND UnitType = 0 " +
                    "AND OrganizationUnitCode like CONCAT(?1,'%') ORDER BY OrganizationUnitCode DESC ", nativeQuery = true)
    OrganizationUnit findFirstByIsActiveTrueAndOrganizationUnitCodeStartingWithIgnoreCaseOrderByOrganizationUnitCodeDesc(String companyCode);

    @Query(value = "SELECT ID FROM EbOrganizationUnit WHERE ID = ?1 OR (ParentID = ?1 AND UnitType = 1 AND AccType = 0)", nativeQuery = true)
    List<UUID> findAllOrgAccType0(UUID orgID);

    @Query(value = "SELECT ID FROM EbOrganizationUnit WHERE ID = ?1 OR (ParentID = ?1 AND UnitType = 1 AND AccType = 0)", nativeQuery = true)
    List<String> findAllOrgStringAccType0(UUID orgID);

    @Query(value = "select * from EbOrganizationUnit where " +
        "(ParentID = ?1 or ParentID in (select ID from EbOrganizationUnit where (ParentID = ?1 and UnitType = 1 and IsActive = 1 and AccType = 0))) and UnitType = 4 and IsActive = 1 " +
        "order by OrganizationUnitCode", nativeQuery = true)
    List<OrganizationUnit> findAllDepartmentDependent(UUID orgID);
}
