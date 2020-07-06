package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBCreditCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBCreditCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBCreditCardRepository extends JpaRepository<MBCreditCard, UUID>, MBCreditCardRepositoryCustom {

    @Query(nativeQuery = true, value = "update mbcreditcard set recorded = ?2 where id = ?1")
    @Modifying
    void updateRecordById(UUID uuid, boolean isRecord);

    @Query(nativeQuery = true, value = "update mbcreditcard set recorded = ?2 where id in ?1")
    @Modifying
    void updateRecordInId(List<UUID> uuid, boolean isRecord);

    @Modifying
    @Query(value = "DELETE FROM MBCreditCard WHERE ID IN ?1 ", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "UPDATE MBCreditCard SET Recorded = 0 WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void multiUnRecord(List<UUID> uuidList, UUID companyID);

    @Modifying
    @Query(value = "DELETE FROM GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteGL(List<UUID> uuidList, UUID companyID);
}
