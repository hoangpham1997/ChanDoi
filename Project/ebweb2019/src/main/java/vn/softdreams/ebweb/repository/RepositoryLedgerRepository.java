package vn.softdreams.ebweb.repository;

import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.RepositoryLedger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the RepositoryLedger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryLedgerRepository extends JpaRepository<RepositoryLedger, UUID>, RepositoryLedgerRepositorycustom{

    void deleteAllByReferenceID(UUID id);

    @Query(nativeQuery = true, value = " DELETE FROM repositoryledger WHERE referenceid IN ?1 ")
    @Modifying
    void deleteAllByReferenceID(List<UUID> refIds);

    @Query(value = "select * from RepositoryLedger rl where rl.MaterialGoodsID = ?1 and rl.PostedDate >= ?2 and rl.PostedDate <= ?3", nativeQuery = true)
    List<RepositoryLedger> getByMaterialGoodsAndPostedDate(UUID materialGoodsID, String fromDate, String toDate);

    @Query(value = "select * from RepositoryLedger rl where rl.IWAmount > 0 and rl.IWQuantity > 0 and rl.TypeID = 741 and rl.PostedDate = ?1 and rl.CompanyID = ?2", nativeQuery = true)
    List<RepositoryLedger> getListRepositoryIWDK(String opnDate, UUID org);

    @Query(value = "select * from RepositoryLedger rl where rl.PostedDate >= ?1 and rl.PostedDate <= ?2 and rl.CompanyID = ?3 and rl.TypeID in (410, 411, 413, 420, 421, 422) and rl.OWQuantity > 0 order by rl.PostedDate, rl.OrderPriority", nativeQuery = true)
    List<RepositoryLedger> getListRepositoryOW(String fromDate, String toDate, UUID org);

    @Query(value = "select * from RepositoryLedger rl where rl.PostedDate >= ?1 and rl.PostedDate <= ?2 and rl.CompanyID = ?3 and rl.TypeID in (400, 401, 402, 403) and rl.IWQuantity > 0 order by rl.PostedDate", nativeQuery = true)
    List<RepositoryLedger> getListRepositoryIW(String fromDate, String toDate, UUID org);

    @Query(value = "select * from RepositoryLedger rl where rl.MaterialGoodsID = ?1 and rl.PostedDate <= ?2 and rl.TypeID in (410, 411, 413, 420, 421, 422) and rl.OWQuantity > 0 order by rl.PostedDate", nativeQuery = true)
    List<RepositoryLedger> getListRepositoryOWNTXT(UUID materialGoodsID, String toDate);

    @Query(value = "select * from RepositoryLedger rl where rl.MaterialGoodsID = ?1 and rl.PostedDate <= ?2 and rl.TypeID in (400, 401, 402, 403, 420, 421, 422) and rl.IWQuantity > 0 order by rl.PostedDate", nativeQuery = true)
    List<RepositoryLedger> getListRepositoryIWNTXT(UUID materialGoodsID, String toDate);

    @Query(value = "select ISNULL(SUM(rl.IWQuantity), 0) - ISNULL(SUM(rl.OWQuantity), 0) from RepositoryLedger rl where rl.ConfrontDetailID = ?1 or rl.DetailID = ?1", nativeQuery = true)
    BigDecimal getIwQuantityByConfrontDetailID(UUID contractDetailID);

    boolean existsByReferenceID(UUID id);

    @Query(nativeQuery = true, value = "select * from repositoryledger where referenceid = ?1")
    RepositoryLedger findByRefIDRepo(UUID id);
}
