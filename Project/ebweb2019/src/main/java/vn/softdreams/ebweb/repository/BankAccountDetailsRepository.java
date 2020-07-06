package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.BankAccountDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.BankAccountDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the BankAccountDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, UUID>, BankAccountDetailsRepositoryCustom {

    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<BankAccountDetails> findAll();

    @Query(value = "select * from BankAccountDetail b where b.CompanyID in ?1 AND isActive = 1 ORDER BY BankAccount", nativeQuery = true)
    List<BankAccountDetails> findAllByIsActive(List<UUID> companyID);

    @Query(value = "select * from BankAccountDetail b where b.CompanyID in ?1 ORDER BY BankAccount", nativeQuery = true)
    List<BankAccountDetails> getAllBankAccountDetailsByCompanyID(List<UUID> companyID);

    @Query(value = "select * from BankAccountDetail b where b.CompanyID = ?1", nativeQuery = true)
    List<BankAccountDetails> allBankAccountDetails(UUID companyID);

    @Query(value = "select * from BankAccountDetail b where b.Id = ?1", nativeQuery = true)
    Optional<BankAccountDetails> findOneById(UUID Id);

    @Query(value = "select count(*) from BankAccountDetail where UPPER(bankAccount) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndBankAccount(String bankAccount, UUID org);

    @Query(value = "select id from BankAccountDetail where UPPER(bankAccount) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByBankAccount(String bankAccount, UUID companyId);

    @Query(value = "select TOP(1) * from BankAccountDetail where CompanyID = ?1 and BankID = ?2", nativeQuery = true)
    BankAccountDetails findByCompanyIDAndUnitID(UUID companyID, UUID bankID);

    @Query(value = "select top(1) * from BankAccountDetail where CompanyID in ?1 and BankAccount = ?2 and id <> ?3", nativeQuery = true)
    BankAccountDetails findByCompanyIDAndBankAccount(List<UUID> companyID, String BankAccount, UUID id);

    @Query(value = "select BankAccountDetailID from ViewCheckConstraint where CompanyID = ?1 and BankAccountDetailID in ?2", nativeQuery = true)
    List<String> getIDRef(UUID orgID, List<UUID> uuids);

    @Modifying
    @Query(value = "delete BankAccountDetail where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);


    @Query(value = "select * from BankAccountDetail b where b.CompanyID in ?1 ORDER BY BankAccount", nativeQuery = true)
    List<BankAccountDetails> getAllBankAccountDetailsActiveCompanyIDNotCreditCard(List<UUID> companyID);
}
