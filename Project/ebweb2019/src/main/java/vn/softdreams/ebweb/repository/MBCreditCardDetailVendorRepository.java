package vn.softdreams.ebweb.repository;

import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.MBCreditCardDetailVendor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MBCreditCardDetailVendor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBCreditCardDetailVendorRepository extends JpaRepository<MBCreditCardDetailVendor, UUID> {
    Optional<MBCreditCardDetailVendor> findByPPInvoiceID(UUID paymentVoucherVendorId);
    List<MBCreditCardDetailVendor> findAllByPPInvoiceID(List<UUID> paymentVoucherVendorId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM MBCreditCardDetailVendor WHERE MBCreditCardID IN ?1")
    void deleteByListMBCreditCardID(List<UUID> uuid);
}
