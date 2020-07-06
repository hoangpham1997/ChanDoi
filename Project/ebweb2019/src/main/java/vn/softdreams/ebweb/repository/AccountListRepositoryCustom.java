package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.User;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

public interface AccountListRepositoryCustom {
    /**
     * @param pageable
     * @param accountNumber
     * @return
     */
    Page<AccountList> findAll(Pageable pageable, String accountNumber);

    List<AccountListDTO> getAccountType(UUID currentUserLoginAndOrg, Integer typeID, String columnName, Boolean ppType, Boolean checkParent);

    List<AccountListDTO> getAccountListByAccountTypeHasRestrictEdit(Integer ppServiceType, String columnName, UUID companyID);

    List<AccountListDTO> getAllAccountList();

    List<AccountListDTO> findByGOtherVoucher(UUID companyID);

    List<AccountListDTO> findAccountLike133(UUID companyID);

    Page<AccountList> findAllByCompanyID(Pageable pageable, UUID companyID);

    List<AccountForAccountDefaultDTO> getAccountForAccountDefault(String listFilterAccount, UUID companyID);

    List<AccountListDTO> findAllByIsActiveForOP(UUID companyID, String currencySearch, Integer typeLedger,  UUID companyIdParent);

    List<AccountListDTO> findAllByIsActiveCustom(UUID companyId);

    List<AccountList> findAllAccountListSimilarBranch(Boolean similarBranch, UUID orgID);

    List<AccountForAccountDefaultDTO> getAllAccountListActiveAndAccountingType(UUID orgID);
}
