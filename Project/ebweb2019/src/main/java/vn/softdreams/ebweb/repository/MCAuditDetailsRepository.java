package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCAuditDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCAuditDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCAuditDetailsRepository extends JpaRepository<MCAuditDetails, UUID> {

}
