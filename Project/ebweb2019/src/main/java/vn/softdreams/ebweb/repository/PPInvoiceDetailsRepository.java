package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the PPInvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPInvoiceDetailsRepository extends JpaRepository<PPInvoiceDetails, UUID>, PPInvoiceDetailsRepositoryCustom {

    List<PPInvoiceDetails> findAllByppInvoiceId(UUID id);

    List<PPInvoiceDetails> findAllByppInvoiceIdOrderByOrderPriorityAsc(UUID id);

    List<PPInvoiceDetails> findAllByppInvoiceIdIn(List<UUID> ppInvoiceList);

    @Modifying
    @Query(value = "DELETE FROM PPInvoiceDetail WHERE PPInvoiceID IN ?1 ", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);
}
