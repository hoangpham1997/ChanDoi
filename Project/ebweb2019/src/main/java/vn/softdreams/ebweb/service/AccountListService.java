package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListAllDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.dto.ColumnDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountList.
 */
public interface AccountListService {

    /**
     * Save a accountList.
     *
     * @param accountList the entity to save
     * @return the persisted entity
     */
    AccountList save(AccountList accountList);

    /**
     * Get all the accountLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountList> findAll(Pageable pageable);


    /**
     * Get the "id" accountList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountList> findOne(UUID id);

    /**
     * Delete the "id" accountList.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @param pageable
     * @param accountList
     * @return
     */
    Page<AccountList> findAll(Pageable pageable, String accountList);

    List<AccountListDTO> getAccountType(Integer typeID, String columnName, Boolean ppType);

    /**
     * @return
     * @author namnh
     */
    List<AccountListDTO> findByGOtherVoucher();

    List<AccountListDTO> getAccountListByAccountType(Integer ppServiceType, Integer accountType);

    List<AccountListDTO> findAccountLike133();

    Optional<AccountListAllDTO> getAccountTypeSecond(Integer typeID, List<ColumnDTO> columnName);

    Page<AccountList> findAllByCompanyID(Pageable pageable);

    List<AccountList> findAllActive();

    List<AccountList> findAllExceptID(UUID id);

    List<AccountList> findAllActive1();

    List<AccountList> findAllActive2();

    List<AccountList> findAllForSystemOptions();

    List<AccountList> findAllAccountLists();

    List<AccountList> getAccountStartWith154();

    List<AccountList> getAccountStartWith112();

    List<AccountList> getAccountStartWith111();

    List<AccountForAccountDefaultDTO> getAccountForAccountDefault(String listFilterAccount);

    List<AccountList> findAllAccountList();

    List<AccountList> getAccountForOrganizationUnit();

    List<AccountListDTO> findAllActiveForOP(String currencyId);

    void deleteByAccountListID(UUID id);

    byte[] exportPdf(String currencySearch);

    Optional<AccountListAllDTO> getAccountTypeThird(Integer typeID, List<ColumnDTO> asList);

    List<AccountList> getAccountDetailType();

    List<Integer> getGradeAccount();

    List<AccountList> getAccountDetailTypeActive();

    List<AccountList> getAccountTypeFromGOV();

    List<AccountList> getAccountListSimilarBranch(Boolean similarBranch, UUID companyID);

    void checkOPNAndExistData(String accountNumber);

    List<AccountList> getListChildAccount(List<AccountList> accountLists, UUID parentAccountID);

    List<AccountList> findAllAccountListByOrg(UUID orgID);

    List<AccountList> getAccountForTHCPTheoKMCP();

    List<AccountForAccountDefaultDTO> findAllAccountListActiveAndAccountingType();
}
