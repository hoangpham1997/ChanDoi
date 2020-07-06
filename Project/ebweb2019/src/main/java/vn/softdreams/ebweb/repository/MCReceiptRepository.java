package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCReceipt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SaBill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MCReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCReceiptRepository extends JpaRepository<MCReceipt, UUID>, MCReceiptRepositoryCustom {

    @Query(value = "select * from MCReceipt where MCReceipt.ID = (select top 1 MCReceiptID from SAInvoice where ID = ?1)", nativeQuery = true)
    Optional<MCReceipt> findBySaInvoiceID(UUID id);

    @Query(value = "select ID from SAInvoice where MCReceiptID = ?1", nativeQuery = true)
    Object findIDRef(UUID id);

    @Query(value = "select ID, RSInwardOutwardID, TypeID, IsBill, InvoiceForm, InvoiceNo  from SAInvoice where MCReceiptID = ?1", nativeQuery = true)
    Object findRefSAInvoice(UUID id);

    @Modifying
    @Query(value = "DELETE FROM MCReceipt  where ID IN ?1", nativeQuery = true)
    void deleteByListID(List<UUID> uuidListMC);

    @Modifying
    @Query(value = "Update MCReceipt set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecord(List<UUID> uuidListMC);
}
