package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBInternalTransfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MBInternalTransfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBInternalTransferRepository extends JpaRepository<MBInternalTransfer, UUID> {

}
