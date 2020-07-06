package vn.softdreams.ebweb.repository;

import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.MCPaymentDetailVendor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MCPaymentDetailVendor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentDetailVendorRepository extends JpaRepository<MCPaymentDetailVendor, UUID> {
       Optional<MCPaymentDetailVendor> findByPPInvoiceID(UUID uuid);
}
