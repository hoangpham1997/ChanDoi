package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OPAccountRepository extends JpaRepository<OPAccount, UUID>, OPAccountRepositoryCustom {
    List<OPAccount> findAllByAccountNumberAndCompanyIdAndCurrencyIdAndTypeLedger(String accountNumber, UUID org, String currencyId, Integer typeLedger);

    List<OPAccount> findAllByAccountNumberAndCompanyIdAndTypeLedger(String accountNumber, UUID org, Integer typeLedger);

    @Query(nativeQuery = true, value = " DELETE FROM OPAccount WHERE ID IN ?1 ")
    @Modifying
    void deleteByIds(List<UUID> uuids);

    @Modifying
    void deleteAllByTypeIdAndCompanyIdAndTypeLedger(Integer typeId, UUID companyId, Integer typeLedger);

    Boolean existsByCompanyIdAndTypeLedgerAndTypeId(UUID companyId, Integer typeLedger, Integer typeId);

    @Query(nativeQuery = true, value = "select id from opaccount where companyID = ?1 and typeLedger =?2 and typeId = ?3")
    List<UUID> findAllUUIDForCompanyIdAndTypeLedgerAndType(UUID companyId, Integer typeLedger, Integer type);

    @Query(nativeQuery = true, value = "select ((select count(1) as count from MCReceiptDetailCustomer where SaleInvoiceID in ?1) + " +
            "    (select count(1) as count from MCPaymentDetailVendor where PPInvoiceID in ?1) + " +
            "    (select count(1) as count from MBTellerPaperDetailVendor where PPInvoiceID in ?1) +" +
            "    (select count(1) as count from MBDepositDetailCustomer where SaleInvoiceID in ?1) +" +
            "        (select count(1) as count from MBCreditCardDetailVendor where PPInvoiceID in ?1)" +
            "    )")
    Integer checkReferenceData(List<UUID> uuid);
}
