package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBDepositDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBDepositDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBDepositDetailsRepository extends JpaRepository<MBDepositDetails, UUID> {

    @Query(value = "SELECT a FROM MBDepositDetails a WHERE MBDepositID = ?1 ORDER BY OrderPriority")
    List<MBDepositDetails> findByMBDepositID(UUID mbDepositId);
}
