package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.service.dto.AccountingDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObject entity.
 */
@SuppressWarnings("unused")
@Repository

public interface AccountingObjectRepository extends JpaRepository<AccountingObject, UUID>, AccountingObjectRepositoryCustom {
    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<AccountingObject> findAll();

    @Query(value = "select * from AccountingObject where IsActive = 1 and ObjectType in (0,2) and CompanyID in ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findByObjecttypeAndIsactive(List<UUID> companyID);

    @Query(value = "select * from AccountingObject where IsActive = 1 and IsEmployee = 1 and CompanyID in ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findByObjecttypeAndIEmployee(List<UUID> companyID);

    @Query(value = "select * from AccountingObject where ObjectType in (0, 2) and IsActive = 1 and CompanyID in ?1 ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsForProvider(List<UUID> companyID);

    @Query(value = "Select * from AccountingObject where IsEmployee = 1 and IsActive = 1 and CompanyID in ?1 ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsForEmployee(List<UUID> companyID);

    @Query(value = "Select * from AccountingObject where IsEmployee = 1 and CompanyID in ?1 ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsForEmployees(List<UUID> companyID);

    @Query(value = "Select * from AccountingObject a where a.Id = ?1", nativeQuery = true)
    Optional<AccountingObject> findOneById(UUID Id);

    @Query(value = "select * from AccountingObject where IsActive = 1 and CompanyID in ?1 and ObjectType IS NOT NULL ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findByIsActive(List<UUID> companyID);

    @Query(value = "select * from AccountingObject where IsActive = 1 and CompanyID in ?1 ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findByTransferIsActive(List<UUID> companyID);

    @Query(value = "select top(1) * from AccountingObject where CompanyID in ?2 and AccountingObjectCode = ?1", nativeQuery = true)
    AccountingObject findByAccountingObjectCodeAndCompanyId(String maDoiTuong, List<UUID> org);

    @Query(value = "select top(1) * from AccountingObject where CompanyID in ?1 and AccountingObjectCode = ?2 and id <> ?3", nativeQuery = true)
    AccountingObject findByCompanyIDAndAccountingObjectCode(List<UUID> companyID, String AccountingObjectCode, UUID id);

    @Query(value = "select * from AccountingObject b where b.CompanyID = ?1 ORDER BY AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findAllAccountingObjectByCompanyID(UUID companyID);

    @Query(value = "select count(*) from AccountingObject where UPPER(AccountingObjectCode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndAccountingObjectCode(String accountingObjectCode, UUID org);

    @Query(value = "select * from AccountingObject where CompanyID in ?1 and accountingObjectCode in ?2", nativeQuery = true)
    List<AccountingObject> getAccountingObjectCodesByCompanyId(List<UUID> org, Set<String> accountingObjectCode);

    @Query(value = "select * from AccountingObject where CompanyID in ?1 and accountingObjectCode in ?2", nativeQuery = true)
    List<AccountingObject> getAccountingObjectCodesByCompanyId(List<UUID> org, List<String> accountingObjectCode);

    @Query(value = "select * from AccountingObject where IsActive = 1 and ObjectType in (1,2) and CompanyID = ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> findByObjecttypeAndIsActived(UUID companyID);

    @Query(value = "select * from AccountingObject where CompanyID in ?1 and IsActive = 1 and (ObjectType is not null or IsEmployee = 1) order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsActiveForRSInwardOutward(List<UUID> org);

    @Query(value = "select * from AccountingObject where CompanyID in ?1 and (ObjectType is not null or IsEmployee = 1) order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAllAccountingObjectsForRSInwardOutward(List<UUID> org);

    @Query(value = "select TOP(1) * from AccountingObject where CompanyID = ?1 and AccountObjectGroupID = ?2", nativeQuery = true)
    AccountingObject findByCompanyIDAndAccountingObjectID(UUID companyID, UUID AccountObjectGroupID);

    @Query(value = "select * from AccountingObject where IsActive = 1 and (ObjectType is not null or IsEmployee = 1) and CompanyID in ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsRSOutward(List<UUID> org);

    @Query(value = "select * from AccountingObject where (ObjectType is not null or IsEmployee = 1) and CompanyID in ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAllAccountingObjectsRSOutward(List<UUID> org);

//    @Query(value = "select * from AccountingObject where CompanyID = ?1 and ObjectType in (0,2) and IsActive = 1 and AccountObjectGroupID = ?2 order by AccountingObjectCode", nativeQuery = true)
//    List<AccountingObject> getAccountingObjectByGroupCode(UUID companyID, UUID groupID);

    @Query(value = "select AccountingObjectID from ViewCheckConstraint where CompanyID = ?1 and AccountingObjectID in ?2", nativeQuery = true)
    List<String> getIDRef(UUID orgID, List<UUID> uuids);

    @Query(value = "select AccountingObjectID from ViewCheckConstraint where CompanyID = ?1 and AccountingObjectID in ?2" +
        " union all " +
        " select EmployeeID from ViewCheckConstraint where CompanyID = ?1 and EmployeeID in ?2 ", nativeQuery = true)
    List<String> getIDRefEmployee(UUID orgID, List<UUID> uuids);

    @Modifying
    @Query(value = "delete AccountingObject where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);


    @Query(value ="select ao.AccountingObjectCode from ViewCheckConstraint vck " +
            "    left join AccountingObject ao on ao.ID = vck.AccountingObjectID " +
            "where vck.CompanyID in ?1 and ao.ObjectType in ?2 " +
            "group by AccountingObjectCode", nativeQuery = true)
    List<String> getAccountingObjectHasReference(List<UUID> companyId, List<Integer> objectType);

    @Query(value ="select ao.AccountingObjectCode from ViewCheckConstraint vck " +
        "    left join AccountingObject ao on ao.ID = vck.AccountingObjectID " +
        "where vck.CompanyID in ?1 " +
        "and ao.IsEmployee = 1 " +
        "group by AccountingObjectCode", nativeQuery = true)
    List<String> getAccountingObjectHasReferenceForEmployee(List<UUID> companyId);

    @Modifying
    @Query(value = "delete AccountingObject where companyid in ?1 and objectType in ?2 and IsEmployee = 0 ", nativeQuery = true)
    void deleteAllByCompanyIdIn(List<UUID> companyId, List<Integer> objectType);
    @Modifying
    @Query(value = "delete AccountingObject where companyid in ?1 and IsEmployee = 1 ", nativeQuery = true)
    void deleteAllByCompanyIdInWithEmployee(List<UUID> companyId);

    @Query(value = "select * from AccountingObject where IsActive = 1 and ObjectType in (1,2) and CompanyID in (select id from Func_getCompany(:CompanyID, :isDependent)) order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectActiveByReportRequest(UUID companyID, Boolean dependent);

    @Query(value = "select * from AccountingObject where ObjectType in (1, 2, 3) and CompanyID in ?1 order by AccountingObjectCode", nativeQuery = true)
    List<AccountingObject> getAccountingObjectsRSTransfer(List<UUID> org);
}
