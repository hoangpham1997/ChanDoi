package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.AccountingObjectBankAccountConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountingObjectBankAccount.
 */
public interface AccountingObjectBankAccountService {

    /**
     * Save a accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccount the entity to save
     * @return the persisted entity
     */
    AccountingObjectBankAccount save(AccountingObjectBankAccount accountingObjectBankAccount);

    /**
     * Get all the accountingObjectBankAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountingObjectBankAccount> findAll(Pageable pageable);


    /**
     * Get the "id" accountingObjectBankAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountingObjectBankAccount> findOne(UUID id);

    /**
     * Delete the "id" accountingObjectBankAccount.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @author mran
     * Get the "id" accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccounts of the accountingObject
     * @return the entity
     */
    void saveAll(List<AccountingObjectBankAccount> accountingObjectBankAccounts);

    Page<AccountingObjectBankAccountDTO> getByAccountingObjectId(UUID accountingObjectID);

    List<AccountingObjectBankAccountDTO> findAll();
}
