package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SystemOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SystemOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemOptionRepository extends JpaRepository<SystemOption, Long>, SystemOptionRepositoryCustom {

    List<SystemOption> findByUserIdAndCompanyId(Long id, UUID org);

    List<SystemOption> findByCompanyId(UUID org);

    @Query(value = "select * from SystemOption where UserId = ?1 and Code = ?2", nativeQuery = true)
    Optional<SystemOption> findOneByUser(Long id, String code);

    @Query(value = "select * from SystemOption where CompanyID = ?1 and Code = ?2", nativeQuery = true)
    Optional<SystemOption> findOneByUser(UUID companyID, String code);

    @Modifying
    @Query(value = "update SystemOption set Data = ?1, DefaultData = ?2 where CompanyID = ?3 AND Code ='NgayHachToan'", nativeQuery = true)
    void savePostedDate(String data, String dataDefault, UUID companyID);

    @Query(value = "select * from SystemOption b where b.CompanyID = ?1 ORDER BY ID", nativeQuery = true)
    List<SystemOption> findAllSystemOptions(UUID companyID);

    @Query(value = "select Data from SystemOption b where b.CompanyID = ?1 AND Code = ?2 ORDER BY ID", nativeQuery = true)
    Integer findByCode(UUID companyID, String code);

    @Query(value = "[CheckPrivateUse] ?1, ?2", nativeQuery = true)
    Integer checkGeneralToPrivateUse(String listsUUIDOrg, String listCheck);
}
