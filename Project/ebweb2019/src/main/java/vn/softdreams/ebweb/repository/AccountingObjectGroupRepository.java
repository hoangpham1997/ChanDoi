package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.AccountingObjectGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.StatisticsCode;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObjectGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingObjectGroupRepository extends JpaRepository<AccountingObjectGroup, UUID>,AccountingObjectGroupRepositoryCustom {
    @Query(value = "select * from AccountingObjectGroup b where b.CompanyID in ?1", nativeQuery = true)
    List<AccountingObjectGroup> findAllAccountingObjectGroup(List<UUID> companyID);

    @Query(value = "select count(*) from AccountingObjectGroup where UPPER(AccountingObjectGroupCode) = UPPER (?1) and CompanyID = ?2", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndAccountingObjectGroupCode(String accountingObjectGroupCode, UUID org);

    @Query(value = "select count(*) from AccountingObjectGroup where ID <> ?1 and CompanyID = ?2  and AccountingObjectGroupCode = ?3 ", nativeQuery = true)
    Integer checkDuplicateAccountingObjectGroup(UUID ID, UUID CompanyID, String accountingObjectGroupCode);

    @Query(value = "select * from AccountingObjectGroup where id in (select ParentID from AccountingObjectGroup where ID = ?1)", nativeQuery = true)
    AccountingObjectGroup findParentByChildID(UUID id);

    @Query(value = "select count(*) from AccountingObjectGroup where ParentID =?1", nativeQuery = true)
    Integer countChildrenByID(UUID id);

    @Query(value = "select * from AccountingObjectGroup  where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    List<AccountingObjectGroup> listChildByParentID(UUID companyID, UUID parentID);

    @Query(value = "select count(*) from AccountingObjectGroup where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    Integer countSiblings(UUID id, UUID ParentID);

    @Query(value = "select * from AccountingObjectGroup b where ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<AccountingObjectGroup> findByParentID(UUID parentAccountID, UUID companyID);

    @Query(value = "select * from AccountingObjectGroup where CompanyID = ?1  ORDER BY AccountingObjectGroupName", nativeQuery = true)
    List<AccountingObjectGroup> findAllByIsActive(UUID companyID);

    @Query(value = "select * from AccountingObjectGroup b where b.CompanyID in ?1 ", nativeQuery = true)
    List<AccountingObjectGroup> loadAllAccountingObjectGroup(List<UUID> companyID);
}
