package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.StatisticsCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.StatisticsConvertDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the StatisticsCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsCodeRepository extends JpaRepository<StatisticsCode, UUID>, StatisticsCodeRepositoryCustom {
    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<StatisticsCode> findAll();

    @Query( value = "select * from StatisticsCode where CompanyID = ?1 ORDER BY StatisticsCode", nativeQuery = true)
    List<StatisticsCode> findAllByCompanyID(UUID companyID);

    @Query( value = "select id, StatisticsCode, StatisticsCodeName from StatisticsCode where IsActive = 1", nativeQuery = true)
    List<StatisticsConvertDTO> findAllByIsActiveTrue();

    @Query(value = "select * from StatisticsCode where CompanyID = ?1 AND IsActive = 1 ORDER BY StatisticsCode", nativeQuery = true)
    List<StatisticsCode> findAllByIsActive(UUID companyID);

    @Query(value = "select * from StatisticsCode where CompanyID = ?1ORDER BY StatisticsCode", nativeQuery = true)
    List<StatisticsCode> getAllStatisticsCodesByCompanyID(UUID companyID);

    @Query(value = "select id from StatisticsCode where UPPER(statisticscode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByStatisticsCode(String statisticsCode, UUID companyId);

    @Query(value = "select count(*) from StatisticsCode where ID <> ?1 and CompanyID = ?2  and StatisticsCode = ?3 COLLATE Latin1_General_CS_AS", nativeQuery = true)
    Integer checkDuplicateStatisticsCode(UUID ID, UUID CompanyID, String statisticsCode);

    @Query(value = "select count(*) from StatisticsCode where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    Integer countSiblings(UUID id, UUID ParentID);

    @Query(value = "select * from StatisticsCode  where CompanyID = ?1 and ParentID = ?2 ", nativeQuery = true)
    List<StatisticsCode> listChildByParentID(UUID companyID, UUID parentID);

//    @Query(value = "select * from StatisticsCode where CompanyID = ?1 and StatisticsCode = ?2 ",nativeQuery = true)
//    StatisticsCode findByStatisticsCode(UUID CompanyID, String StatisticsCode);

    @Query(value = "select * from StatisticsCode where id in (select ParentID from StatisticsCode where ID = ?1)", nativeQuery = true)
    StatisticsCode findParentByChildID(UUID id);

    @Query(value = "select count(*) from StatisticsCode where ParentID =?1", nativeQuery = true)
    Integer countChildrenByID(UUID id);

    @Query(value = "select count(*) from ViewCheckConstraint where StatisticsCodeID = ?1", nativeQuery = true)
    Integer checkExistedInAccountingObject(UUID id);

    @Query(value = "select statisticsCode from StatisticsCode where companyID = ?1", nativeQuery = true)
    List<String> findByStatisticsCode(UUID companyId);

    @Query(value = "select id from Func_getCompany(?1, ?2)", nativeQuery = true)
    List<UUID> getIDByCompanyIDAndSimilarBranch(UUID CompanyID, Boolean isDependent);
}
