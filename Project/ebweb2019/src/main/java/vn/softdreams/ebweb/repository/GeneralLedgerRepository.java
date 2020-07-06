package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GeneralLedger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.web.rest.AuditResource;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GeneralLedger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, UUID>, GeneralLedgerRepositoryCustom {

    @Query(nativeQuery = true, value = " DELETE FROM generalledger WHERE referenceid IN ?1 ")
    @Modifying
    void deleteAllByReferenceID(List<UUID> referenceIds);

    boolean existsByReferenceID(UUID uuid);

    @Query(nativeQuery = true, value = "select * from generalledger where referenceid = ?1")
    List<GeneralLedger> findByRefIDRepo(UUID id);

    @Query(value = "Select * from GeneralLedger gl where gl.DetailID = ?1", nativeQuery = true)
    List<GeneralLedger> findByDetailID(UUID id);

    @Query(value = "SELECT SUM(ISNULL(DebitAmountOriginal, 0) - ISNULL(CreditAmountOriginal, 0)) FROM GeneralLedger WHERE PostedDate <= ?1 AND CurrencyID = ?2 AND Account LIKE ?3 AND CompanyID = ?4 AND (TypeLedger = ?5 OR TypeLedger = 2)", nativeQuery = true)
    BigDecimal getTotalBalanceAmount(String auditDate, String currencyID, String account, UUID companyID, Integer valueOf);


    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM GeneralLedger WHERE (NoFBook = 'OPN' OR NoMBook ='OPN') AND CompanyID = ?1")
    Integer countAllOPN(UUID companyID);
}
