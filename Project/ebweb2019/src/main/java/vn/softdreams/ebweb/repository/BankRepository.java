package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Bank;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.BankConvertDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the Bank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankRepository extends JpaRepository<Bank, UUID>, BankRepositoryCustom {
    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<Bank> findAll();

    @Query(value = "select * from Bank b where b.CompanyID = ?1 AND isActive = 1 ORDER BY BankCode", nativeQuery = true)
    List<Bank> findAllByIsActive(UUID companyID);

    @Query(value = "select * from Bank b where b.CompanyID = ?1  ORDER BY BankCode", nativeQuery = true)
    List<Bank> findAllByCompanyID(UUID companyID);

    @Query(value = "select * from Bank b where b.CompanyID in ?1 ORDER BY BankCode", nativeQuery = true)
    List<Bank> findAllBankByCompanyIDIn(List<UUID> companyID);

    @Query(value = "select * from Bank b where b.CompanyID = ?1 ORDER BY BankCode", nativeQuery = true)
    List<Bank> findAllBankByCompanyIDIn(UUID companyID);

    @Query(value = "select count(*) from Bank b where upper(bankCode) = upper(?1) AND b.CompanyID = ?2", nativeQuery = true)
    int countByBankCodeIgnoreCaseAndIsActiveTrue(String bankCode, UUID companyID);

    @Modifying
    @Query(value = "delete bank where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);

    @Query(value = "select bankid from BankAccountDetail where CompanyID = ?1 and bankid in ?2", nativeQuery = true)
    List<String> getIDRef(UUID orgID, List<UUID> uuids);

    Page<Bank> pageableAllBank(Pageable pageable, UUID org);
}
