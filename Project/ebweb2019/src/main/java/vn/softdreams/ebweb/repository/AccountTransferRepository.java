package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.AccountTransfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountTransfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountTransferRepository extends JpaRepository<AccountTransfer, UUID> {
    @Query(value = "select * from AccountTransfer b where b.CompanyID = ?1 ORDER BY AccountTransferOrder, AccountTransferCode", nativeQuery = true)
    List<AccountTransfer> findAllAccountTransfers(UUID companyID);
}
