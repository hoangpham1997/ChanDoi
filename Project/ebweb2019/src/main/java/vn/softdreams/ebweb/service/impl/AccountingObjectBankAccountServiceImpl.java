package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.service.AccountingObjectBankAccountService;
import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;
import vn.softdreams.ebweb.repository.AccountingObjectBankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.AccountingObjectBankAccountConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing AccountingObjectBankAccount.
 */
@Service
@Transactional
public class AccountingObjectBankAccountServiceImpl implements AccountingObjectBankAccountService {

    private final Logger log = LoggerFactory.getLogger(AccountingObjectBankAccountServiceImpl.class);

    private final AccountingObjectBankAccountRepository accountingObjectBankAccountRepository;

    public AccountingObjectBankAccountServiceImpl(AccountingObjectBankAccountRepository accountingObjectBankAccountRepository) {
        this.accountingObjectBankAccountRepository = accountingObjectBankAccountRepository;
    }

    /**
     * Save a accountingObjectBankAccount.
     *
     * @param accountingObjectBankAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountingObjectBankAccount save(AccountingObjectBankAccount accountingObjectBankAccount) {
        log.debug("Request to save AccountingObjectBankAccount : {}", accountingObjectBankAccount);
        return accountingObjectBankAccountRepository.save(accountingObjectBankAccount);
    }

    /**
     * Get all the accountingObjectBankAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountingObjectBankAccount> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingObjectBankAccounts");
        return accountingObjectBankAccountRepository.findAll(pageable);
    }


    /**
     * Get one accountingObjectBankAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingObjectBankAccount> findOne(UUID id) {
        log.debug("Request to get AccountingObjectBankAccount : {}", id);
        return accountingObjectBankAccountRepository.findById(id);
    }

    /**
     * Delete the accountingObjectBankAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountingObjectBankAccount : {}", id);
        accountingObjectBankAccountRepository.deleteById(id);
    }
    /**
     * @author mran
     * saveAll the accountingObjectBankAccount
     *
     * @param  accountingObjectBankAccounts of the entity
     */
    @Override
    public void saveAll(List<AccountingObjectBankAccount> accountingObjectBankAccounts) {
        log.debug("Request to delete AccountingObjectBankAccount : {}", accountingObjectBankAccounts);
        accountingObjectBankAccountRepository.saveAll(accountingObjectBankAccounts);
    }

    /**
     * @author quyennc lấy AccountingObject theo id
     * @param accountingObjectID
     * @return
     */
    @Override
    public Page<AccountingObjectBankAccountDTO> getByAccountingObjectId(UUID accountingObjectID) {
        return new PageImpl<>(accountingObjectBankAccountRepository.getByAccountingObjectId(accountingObjectID));
    }
    public List<AccountingObjectBankAccountDTO> findAll() {
        return accountingObjectBankAccountRepository.findAllAOBA();
    }
}
