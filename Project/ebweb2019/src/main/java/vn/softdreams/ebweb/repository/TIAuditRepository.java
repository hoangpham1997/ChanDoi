package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAuditRepository extends JpaRepository<TIAudit, UUID>, TIAuditRepositoryCustom{

    @Query(value = "select top 1 TIIncrementID from TIIncrementDetail where TIAuditID = ?1", nativeQuery = true)
    UUID getTIIncrement(UUID id);

    @Query(value = "select top 1 TIDecrementID from TIDecrementDetail where TIAuditID = ?1", nativeQuery = true)
    UUID getTIDecrement(UUID id);
}
