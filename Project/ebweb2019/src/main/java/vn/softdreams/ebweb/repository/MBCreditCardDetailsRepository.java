package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBCreditCardDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBCreditCardDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBCreditCardDetailsRepository extends JpaRepository<MBCreditCardDetails, UUID> {
    @Query(value = "SELECT a FROM MBCreditCardDetails a WHERE MBCreditCardID = ?1")
    List<MBCreditCardDetails> findByMBCreditCardID(UUID mbCreditCardId);

}
