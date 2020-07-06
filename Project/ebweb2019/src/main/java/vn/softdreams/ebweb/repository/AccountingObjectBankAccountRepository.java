package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.AccountingObjectBankAccountConvertDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObjectBankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingObjectBankAccountRepository extends JpaRepository<AccountingObjectBankAccount, UUID>, AccountingObjectBankAccountRepositoryCustom {

    int countByAccountingObjectIdAndBankAccount(UUID id, String soTaiKhoan);
}
