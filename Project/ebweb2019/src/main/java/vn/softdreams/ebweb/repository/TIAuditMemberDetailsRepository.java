package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAuditMemberDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAuditMemberDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAuditMemberDetailsRepository extends JpaRepository<TIAuditMemberDetails, UUID>, TIAuditMemberDetailsRepositoryCustom {

}
