package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Tools;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Tools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolsRepository extends JpaRepository<Tools, UUID>, ToolsRepositoryCustom {

    @Query(value = "select * from Tools a where a.companyID = ?1 AND a.isActive = 1 order by a.toolCode", nativeQuery = true)
    List<Tools> findAllByCompanyID(UUID org);
    @Query(value = "select * from Tools where CompanyID = ?1 and IsActive = 1", nativeQuery = true)
    List<Tools> getToolsActive(UUID org);

    @Query(value = "select * from Tools where id = ?1", nativeQuery = true)
    Optional<Tools> getToolsById(UUID id);
}
