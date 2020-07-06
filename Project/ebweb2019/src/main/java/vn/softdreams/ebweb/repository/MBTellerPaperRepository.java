package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBTellerPaper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the MBTellerPaper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBTellerPaperRepository extends JpaRepository<MBTellerPaper, UUID>, MBTellerPaperRepositoryCustom {
    @Query(value = "select * from MBTellerPaper a where a.ID = ?1", nativeQuery = true)
    Optional<MBTellerPaper> findOneById(UUID id);

    @Query(nativeQuery = true, value = "update mbtellerpaper set recorded = ?2 where id = ?1")
    @Modifying
    void updateRecordById(UUID uuid, boolean isRecord);

    @Query(nativeQuery = true, value = "update mbtellerpaper set recorded = ?2 where id in ?1")
    @Modifying
    void updateRecordInId(List<UUID> uuid, boolean isRecord);

    @Modifying
    @Query(value = "DELETE FROM MBTellerPaper WHERE ID IN ?1 ", nativeQuery = true)
    void deleteByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "UPDATE MBTellerPaper SET Recorded = 0 WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void multiUnRecord(List<UUID> uuidList, UUID companyID);

    @Modifying
    @Query(value = "DELETE FROM GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteGL(List<UUID> uuidList, UUID companyID);
}
