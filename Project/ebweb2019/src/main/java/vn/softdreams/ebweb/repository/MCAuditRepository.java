package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import vn.softdreams.ebweb.domain.MCAuditDetails;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MCAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCAuditRepository extends JpaRepository<MCAudit, UUID>, MCAuditRepositoryCustom {

}
