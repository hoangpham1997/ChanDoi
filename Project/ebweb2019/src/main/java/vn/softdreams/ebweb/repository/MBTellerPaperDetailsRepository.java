package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBTellerPaperDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBTellerPaperDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBTellerPaperDetailsRepository extends JpaRepository<MBTellerPaperDetails, UUID> {
    @Query(value = "SELECT a FROM MBTellerPaperDetails a WHERE MBTellerPaperID = ?1")
    List<MBTellerPaperDetails> findByMBTellerPaperID(UUID mBTellerPaperID);
}
