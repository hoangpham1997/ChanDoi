package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBInternalTransferDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MBInternalTransferDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBInternalTransferDetailsRepository extends JpaRepository<MBInternalTransferDetails, UUID> {

}
