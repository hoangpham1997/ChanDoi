package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import vn.softdreams.ebweb.domain.Repository;
import org.springframework.data.jpa.repository.*;
import vn.softdreams.ebweb.service.dto.ObjectDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the Repository entity.
 */
@SuppressWarnings("unused")
@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, UUID>, RepositoryRepositoryCustom {
    @Query(value = "select * from Repository where CompanyID in ?1 and IsActive = 1 order by RepositoryCode", nativeQuery = true)
    List<Repository> findAllRepository(List<UUID> org);

    @Query(value = "select id from repository where UPPER(repositorycode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByRepositoryCode(String repositoryCode, UUID companyId);

    @Query(value = "select count(*) from Repository where UPPER(repositorycode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndRepositoryCode(String repositoryCode, UUID org);

    @Query(value = "select top(1) * from Repository where CompanyID in ?1 and RepositoryCode = ?2 and id <> ?3", nativeQuery = true)
    Repository findByCompanyIDAndRepositoryCode(List<UUID> companyID, String RepositoryCode, UUID id);

    @Query(value = "select RepositoryID from MaterialGoods where CompanyID = ?1 and RepositoryID in ?2" +
        " union all " +
        " select RepositoryID from ViewCheckConstraint where CompanyID = ?1 and RepositoryID in ?2 ", nativeQuery = true)
    List<String> getIDRef(UUID orgID, List<UUID> uuids);

    @Modifying
    @Query(value = "delete Repository where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);

    @Query(value = "select * from Repository where CompanyID in ?1 order by RepositoryCode", nativeQuery = true)
    List<Repository> getRepositoryComboboxGetAll(List<UUID> org);

//    @Query(value = "select mg.id ID, mg.RepositoryCode name from Repository mg where mg.id in ?1 and mg.id not in (select r.id from RepositoryLedger r where r.id in ?1)", nativeQuery = true)
//    List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs);
}
