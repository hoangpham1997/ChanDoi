package vn.softdreams.ebweb.repository;

import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.MBTellerPaperDetailVendor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MBTellerPaperDetailVendor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBTellerPaperDetailVendorRepository extends JpaRepository<MBTellerPaperDetailVendor, UUID> {
    Optional<MBTellerPaperDetailVendor> findByPPInvoiceID(UUID paymentVoucherVendorId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM MBTellerPaperDetailVendor WHERE MBTellerPaperID IN ?1")
    void deleteByListMBTellerPaperID(List<UUID> uuid);
}
