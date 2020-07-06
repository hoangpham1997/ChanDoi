package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CostSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.CostSetConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CostSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostSetRepository extends JpaRepository<CostSet, UUID>, CostSetRepositoryCustom {

    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<CostSet> findAll();

    List<CostSet> findAllByIsActiveTrue();

    @Query(value = "select * from CostSet b where b.CompanyID in ?1 AND isActive = 1 ORDER BY CostSetCode", nativeQuery = true)
    List<CostSet> findAllByIsActive(List<UUID> companyID);

    @Query(value = "select * from CostSet b where b.CompanyID in ?1 ORDER BY CostSetCode", nativeQuery = true)
    List<CostSet> getCostSetsByCompanyID(List<UUID> companyID);

    @Query(value = "select * from CostSet b where UPPER(CostSetCode) = UPPER (?1) AND b.CompanyID = ?2 AND isActive = 1 ", nativeQuery = true)
    UUID findIdByCostSetCode(String costSetCode, UUID companyId);

    @Query(value = "select top(1) * from CostSet where CompanyID in ?1 and costSetCode = ?2 and id <> ?3", nativeQuery = true)
    CostSet findByCompanyIDAndCostSetCode(List<UUID> companyID, String costSetCode, UUID id);

    @Query(value = "select * from CostSet b where b.CompanyID = ?1 ORDER BY CostSetCode ASC ", nativeQuery = true)
    Page<CostSet> findAllCostSetByCompanyID(Pageable pageable, List<UUID> companyID);

    @Query(value = "select * from CostSet b left join EbOrganizationUnit c on c.ID = ?1 and (c.ID = ?1 or c.ParentID = ?2) ORDER BY CostSetCode ASC", nativeQuery = true)
    Page<CostSet> findAllCostSetStorageByCompanyID(Pageable pageable, UUID id,  UUID parentID);

    @Modifying
    @Query(value = "DELETE FROM CostSet WHERE id in ?1", nativeQuery = true )
    void deleteAllById(List<UUID> uuids);

    @Query(value = "select Count(*) from MaterialQuantum a left join CostSet b on a.ObjectID = b.ID where a.ObjectID = ?1", nativeQuery = true)
    Integer checkCostSetMQId(UUID id);

    @Query(value = "select Data from SystemOption where CompanyId = ?1 and Code = ?2", nativeQuery = true)
    String checked(UUID companyId, String code );

    @Query(value = "select * from CostSet b where b.CompanyID in ?1 AND isActive = 1 ORDER BY CostSetCode", nativeQuery = true)
    List<CostSet> findAllByOrgID(List<UUID> listOrgID);
}
