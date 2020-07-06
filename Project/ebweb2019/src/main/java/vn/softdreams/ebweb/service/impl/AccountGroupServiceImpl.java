package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AccountGroupService;
import vn.softdreams.ebweb.domain.AccountGroup;
import vn.softdreams.ebweb.repository.AccountGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing AccountGroup.
 */
@Service
@Transactional
public class AccountGroupServiceImpl implements AccountGroupService {

    private final Logger log = LoggerFactory.getLogger(AccountGroupServiceImpl.class);

    private final AccountGroupRepository accountGroupRepository;

    public AccountGroupServiceImpl(AccountGroupRepository accountGroupRepository) {
        this.accountGroupRepository = accountGroupRepository;
    }

    /**
     * Save a accountGroup.
     *
     * @param accountGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountGroup save(AccountGroup accountGroup) {
        log.debug("Request to save AccountGroup : {}", accountGroup);        return accountGroupRepository.save(accountGroup);
    }

    /**
     * Get all the accountGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountGroup> findAll(Pageable pageable) {
        log.debug("Request to get all AccountGroups");
        return accountGroupRepository.findAll(pageable);
    }


    /**
     * Get one accountGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountGroup> findOne(String id) {
        log.debug("Request to get AccountGroup : {}", id);
        return accountGroupRepository.findById(id);
    }

    /**
     * Delete the accountGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete AccountGroup : {}", id);
        accountGroupRepository.deleteById(id);
    }

    public List<AccountGroup> findAllActive() {
        return accountGroupRepository.findAllByIsActive();
    }
}
