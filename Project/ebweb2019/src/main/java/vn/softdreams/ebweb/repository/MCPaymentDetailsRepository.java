package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCPaymentDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCPaymentDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentDetailsRepository extends JpaRepository<MCPaymentDetails, UUID> {

}
