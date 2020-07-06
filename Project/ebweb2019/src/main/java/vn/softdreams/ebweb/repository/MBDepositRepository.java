package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBDeposit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SaBill;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MBDeposit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBDepositRepository extends JpaRepository<MBDeposit, UUID>,MBDepositRepositoryCustom {

    @Query(value = "select * from MBDeposit where MBDeposit.ID = (select top 1 MBDepositID from SAInvoice where ID = ?1)", nativeQuery = true)
    Optional<MBDeposit> findBySaInvoiceID(UUID id);

    @Query(value = "select ID, RSInwardOutwardID, TypeID from SAInvoice where MBDepositID = ?1", nativeQuery = true)
    Object findIDRef(UUID id);

    @Query(value = "DELETE FROM SAInvoice  where ID = ?1", nativeQuery = true)
    Object deleteSAInvoiceID(UUID id);

    @Modifying
    @Query(value = "DELETE FROM MBDeposit  where ID IN ?1", nativeQuery = true)
    void deleteByListID(List<UUID> uuidListMBDeposit);

    @Modifying
    @Query(value = "Update MBDeposit set Recorded = 0 where id in ?1", nativeQuery = true)
    void updateUnrecord(List<UUID> uuidListMB);
}
