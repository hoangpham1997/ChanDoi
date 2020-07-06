package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AccountDefaultService;
import vn.softdreams.ebweb.domain.AccountDefault;
import vn.softdreams.ebweb.repository.AccountDefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing AccountDefault.
 */
@Service
@Transactional
public class AccountDefaultServiceImpl implements AccountDefaultService {

    private final Logger log = LoggerFactory.getLogger(AccountDefaultServiceImpl.class);

    private final AccountDefaultRepository accountDefaultRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;

    public AccountDefaultServiceImpl(AccountDefaultRepository accountDefaultRepository, UserService userService, OrganizationUnitRepository organizationUnitRepository, UtilsService utilsService) {
        this.accountDefaultRepository = accountDefaultRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a accountDefault.
     *
     * @param accountDefault the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountDefault save(AccountDefault accountDefault) {
        log.debug("Request to save AccountDefault : {}", accountDefault);
        return accountDefaultRepository.save(accountDefault);
    }

    /**
     * Get all the accountDefaults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountDefault> findAll(Pageable pageable) {
        log.debug("Request to get all AccountDefaults");
        return accountDefaultRepository.findAll(pageable);
    }

    @Override
    public Page<AccountDefault> findAll() {
        return new PageImpl<AccountDefault>(accountDefaultRepository.findAll());
    }


    /**
     * Get one accountDefault by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDefault> findOne(UUID id) {
        log.debug("Request to get AccountDefault : {}", id);
        return accountDefaultRepository.findById(id);
    }

    /**
     * Delete the accountDefault by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountDefault : {}", id);
        accountDefaultRepository.deleteById(id);
    }

    public List<AccountDefaultDTO> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountDefaultRepository.findAllForAccountDefaultCategory(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AccountDefault> findByTypeID(Integer typeID) {
        log.debug("Request to get AccountDefault : {}", typeID);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountDefaultRepository.findByTypeID(typeID, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");

    }

    @Override
    public void saveAll(List<AccountDefault> accountDefaults) {
        log.debug("Request to save accountDefaults : {}", accountDefaults);
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        if (currentUserLoginAndOrg.isPresent()) {
//            for (int i = 0; i < accountDefaults.size(); i++) {
//                accountDefaults.get(i).setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
//            }
//        }
        accountDefaultRepository.saveAll(accountDefaults);
    }
}
