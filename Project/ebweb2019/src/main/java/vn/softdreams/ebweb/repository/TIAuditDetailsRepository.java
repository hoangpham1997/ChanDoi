package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAuditDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAuditDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAuditDetailsRepository extends JpaRepository<TIAuditDetails, UUID>, TIAuditDetailsRepositoryCustom {

}
