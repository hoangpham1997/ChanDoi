package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, UUID>, CurrencyRepositoryCustom {
    Page<Currency> findAllByIsActiveIsTrue(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<Currency> findAll();

    /**
     * @return
     * @Author hieugie
     * Hautv Edit find with companyID
     */
    @Query(value = "select b.* from Currency b where b.CompanyID = ?1 AND isActive = 1  ORDER BY CurrencyCode", nativeQuery = true)
    List<Currency> findByIsActiveTrue(UUID companyID);

    @Query(value = "select b.* from Currency b where b.CompanyID in ?1 AND isActive = 1  ORDER BY CurrencyCode", nativeQuery = true)
    List<Currency> findByIsActiveTrue(List<UUID> companyID);

    @Query(value = "select b.* from Currency b where b.CompanyID = ?1  ORDER BY CurrencyCode", nativeQuery = true)
    List<Currency> findAllByCompanyID(UUID companyID);

    @Query(value = "select b.* from Currency b where CompanyID IS NULL  ORDER BY CurrencyCode", nativeQuery = true)
    List<Currency> findAllByCompanyIDNull();

    int countByCurrencyCodeAndIsActiveTrue(String currencyCode);

    @Query(value = "select cr.* from Currency cr where cr.CurrencyCode = (select eb.CurrencyID from EbOrganizationUnit eb where eb.CompanyID = ?1 and eb.UnitType = 0)", nativeQuery = true)
    Optional<Currency> findActiveDefault(String companyID);

    @Query(value = "select count(*) from currency where upper(currencyCode) = upper(?1) and CompanyID = ?2", nativeQuery = true)
    int countByCurrencyCodeIgnoreCaseAndIsActiveTrue(String currencyCode, UUID companyID);

    @Query(value = "SELECT ExchangeRate FROM Currency WHERE CurrencyCode = ?1 AND CompanyID = (SELECT TOP (1) (CASE WHEN ParentID IS NOT NULL THEN ParentID ELSE ID END) FROM EbOrganizationUnit WHERE ID = ?2)", nativeQuery = true)
    BigDecimal getExchageRateByCurencyCode(String currencyID, UUID org);

    @Query(value = "select * from Currency A LEFT JOIN EbOrganizationUnit C ON A.CompanyID = C.ID where A.CompanyID = ?1 and A.IsActive = 1 and A.CurrencyCode <> C.CurrencyID", nativeQuery = true)
    List<Currency> findCurrencyByCompanyID(UUID companyID);
}
