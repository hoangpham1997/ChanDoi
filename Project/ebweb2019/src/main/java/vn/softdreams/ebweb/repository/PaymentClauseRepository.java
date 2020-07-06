package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PaymentClause;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the PaymentClause entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentClauseRepository extends JpaRepository<PaymentClause, UUID> {

    @Query(value = "Select count(*) from PaymentClause where ID <> ?1 and CompanyID = ?2 and PaymentClauseCode = ?3 COLLATE Latin1_General_CS_AS", nativeQuery = true)
    Integer checkDuplicatePaymentClause(UUID ID, UUID companyID, String paymentClauseCode);

    @Query(value = "Select * from PaymentClause where CompanyID = ?1 order by PaymentClauseCode", nativeQuery = true)
    Page<PaymentClause> findAllPaymentClausesByCompanyID(UUID companyID, Pageable pageable);

    @Query(value = "select * from PaymentClause b where b.CompanyID = ?1 AND isActive = 1", nativeQuery = true)
    List<PaymentClause> findAllByCompanyID(UUID companyID);

    @Query(value = "select SUM(countError.countPaymentClause) " +
        "from ( " +
        "         select count(*) countPaymentClause " +
        "         from AccountingObject " +
        "         where PaymentClauseID = ?1 " +
        "           and CompanyID = ?2 " +
        "         union " +
        "         select count(*) countPaymentClause " +
        "         from ViewCheckConstraint " +
        "         where PaymentClauseID = ?1 " +
        "           and CompanyID = ?2) countError", nativeQuery = true)
    Long checkExistedInAccountingObject(UUID paymentClauseId, UUID id);

    void deleteByIdIn(List<UUID> listID);
}
