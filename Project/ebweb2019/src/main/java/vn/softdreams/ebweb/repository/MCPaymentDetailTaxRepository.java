package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCPaymentDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCPaymentDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentDetailTaxRepository extends JpaRepository<MCPaymentDetailTax, UUID> {

}
