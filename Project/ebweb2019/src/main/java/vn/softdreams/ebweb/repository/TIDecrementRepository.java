package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIDecrement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.web.rest.AuditResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the TIDecrement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIDecrementRepository extends JpaRepository<TIDecrement, UUID> , TIDecrementRepositoryCustom {

    @Query(value = "select * from TIDecrement where ID = '702C488A-3F8E-4246-A05A-DFE756E75EB8'", nativeQuery = true)
    Optional<TIDecrement> findByIdqq();
}
