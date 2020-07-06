package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCPaymentDetailInsurance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCPaymentDetailInsurance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentDetailInsuranceRepository extends JpaRepository<MCPaymentDetailInsurance, UUID> {

}
