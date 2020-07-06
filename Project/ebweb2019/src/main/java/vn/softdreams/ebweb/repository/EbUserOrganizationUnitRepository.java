package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EbUserGroup;
import vn.softdreams.ebweb.domain.EbUserOrganizationUnit;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for the EbAuthority entity.
 */
public interface EbUserOrganizationUnitRepository extends JpaRepository<EbUserOrganizationUnit, Long> {
    @Query(value = "select a.* from EbUserOrganizationUnit a where a.UserId = ?1 and a.orgId = ?2", nativeQuery = true)
    EbUserOrganizationUnit findOneByUserIdAndOrgId(Long userId, UUID orgId);

    @Modifying
    @Query(value = "insert into EbUserOrganizationUnit(UserID, OrgID, CurrentBook) values (?1, ?2, 0)", nativeQuery = true)
    void insertUserAndOrg(Long userId, UUID orgId);

    @Modifying
    @Query(value = "update EbUserOrganizationUnit set OrgId = ?1 " +
        " where userID = ?2", nativeQuery = true)
    void updateOrg(UUID orgID, Long userID);

    @Modifying
    @Query(value = "delete from EbUserOrganizationUnit where UserId = ?1 and OrgId = ?2 ", nativeQuery = true)
    void deleteUserAndOrg(Long userId, UUID orgId);

    @Query(value = "select * from EbUserOrganizationUnit ebo where UserId = ?1", nativeQuery = true)
    EbUserOrganizationUnit getOrgByUser(Long userId);

    @Modifying
    @Query(value = "delete from EbUserOrganizationUnit where OrgId = ?1 ", nativeQuery = true)
    void deleteByOrgID(UUID orgID);

    @Modifying
    @Query(value = "delete from EbUserOrganizationUnit where userId = ?1 ", nativeQuery = true)
    void deleteEbUserOrganizationUnitByUserId(Long userId);

    @Modifying
    @Query(value = "delete from EbUserOrganizationUnit where OrgId = ?1 ", nativeQuery = true)
    void deleteEbUserOrganizationUnitByOrgId(UUID orgId);

    int countByUserId(Long id);
}
