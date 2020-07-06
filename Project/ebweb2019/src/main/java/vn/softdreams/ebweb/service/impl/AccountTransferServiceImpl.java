package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AccountTransferService;
import vn.softdreams.ebweb.domain.AccountTransfer;
import vn.softdreams.ebweb.repository.AccountTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing AccountTransfer.
 */
@Service
@Transactional
public class AccountTransferServiceImpl implements AccountTransferService {

    private final Logger log = LoggerFactory.getLogger(AccountTransferServiceImpl.class);

    private final AccountTransferRepository accountTransferRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;

    public AccountTransferServiceImpl(AccountTransferRepository accountTransferRepository, UserService userService, OrganizationUnitRepository organizationUnitRepository, UtilsService utilsService) {
        this.accountTransferRepository = accountTransferRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a accountTransfer.
     *
     * @param accountTransfer the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountTransfer save(AccountTransfer accountTransfer) {
        log.debug("Request to save AccountTransfer : {}", accountTransfer);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if(accountTransfer.getCompanyID() == null){
                accountTransfer.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
            }
            accountTransfer.setAccountingType(0);
        }
        return accountTransferRepository.save(accountTransfer);
    }

    /**
     * Get all the accountTransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountTransfer> findAll(Pageable pageable) {
        log.debug("Request to get all AccountTransfers");
        return accountTransferRepository.findAll(pageable);
    }


    /**
     * Get one accountTransfer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountTransfer> findOne(UUID id) {
        log.debug("Request to get AccountTransfer : {}", id);
        return accountTransferRepository.findById(id);
    }

    /**
     * Delete the accountTransfer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountTransfer : {}", id);
        accountTransferRepository.deleteById(id);
    }

    public List<AccountTransfer> findAllAccountTransfers() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return accountTransferRepository.findAllAccountTransfers(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
