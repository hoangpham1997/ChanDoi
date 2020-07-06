package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MCPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentRepository extends JpaRepository<MCPayment, UUID> ,MCPaymentRepositoryCustom {

    @Query(nativeQuery = true, value = "update mcpayment set recorded = ?2 where id = ?1")
    @Modifying
    void updateRecordById(UUID uuid, boolean isRecord);
    @Query(nativeQuery = true, value = "update mcpayment set recorded = ?2 where id in ?1")
    @Modifying
    void updateRecordInId(List<UUID> uuid, boolean isRecord);
    @Query(nativeQuery = true, value = "select receiver from  mcpayment where id = ?1")
    String getReceiverById(UUID uuid);

    @Modifying
    @Query(value = "UPDATE MCPayment SET Recorded = 0 WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void multiUnRecord(List<UUID> uuidList, UUID companyID);

    @Modifying
    @Query(value = "DELETE FROM GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteGL(List<UUID> uuidList, UUID companyID);
}
