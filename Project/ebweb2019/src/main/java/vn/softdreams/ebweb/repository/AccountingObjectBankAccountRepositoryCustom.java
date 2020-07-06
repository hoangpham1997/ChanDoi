package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObjectBankAccount entity.
 */
@SuppressWarnings("unused")
public interface AccountingObjectBankAccountRepositoryCustom {

    List<AccountingObjectBankAccountDTO> findAllAOBA();

    List<AccountingObjectBankAccountDTO> getByAccountingObjectId(UUID accountingObjectID);

}
