package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CreditCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the CreditCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, UUID>, CreditCardRepositoryCustom {
    @Override
    List<CreditCard> findAll();

    @Query(value = "select * from CreditCard b where b.CompanyID in ?1", nativeQuery = true)
    List<CreditCard> findAllByCompanyID(List<UUID> companyID);

    @Query(value = "select * from CreditCard b where b.CompanyID = ?1 AND b.IsActive = 1", nativeQuery = true)
    List<CreditCard> findAllActiveByCompanyID(UUID companyID);

    Page<CreditCard> pageableAllCreditCard(Pageable pageable, UUID org);

    @Query(value = "select count(*) from CreditCard b where upper(creditCardNumber) = upper(?1) AND b.CompanyID = ?2", nativeQuery = true)
    int countByCreditCardNumberIgnoreCaseAndIsActiveTrue(String creditCardNumber, UUID companyID);

    @Query(value = "select * from CreditCard b where upper(creditCardNumber) = upper(?1) AND b.CompanyID = ?2", nativeQuery = true)
    Optional<CreditCard> findByCreditCardNumberAndCompanyID(String creditCardNumber, UUID companyId);

    @Query(value = "select * from CreditCard b where upper(creditCardNumber) = upper(?1) AND b.CompanyID IN ?2", nativeQuery = true)
    Optional<CreditCard> findByCreditCardNumberAndListCompanyID(String creditCardNumber, List<UUID> companyId);

    @Query(value = "select CreditCardID from ViewCheckConstraint where CreditCardID in ?1", nativeQuery = true)
    List<String> getIDRefEmployee(List<UUID> uuids);

    @Modifying
    @Query(value = "delete CreditCard where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);
}
