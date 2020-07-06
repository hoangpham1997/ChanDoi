package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.AutoPrincipleService;
import vn.softdreams.ebweb.domain.AutoPrinciple;
import vn.softdreams.ebweb.repository.AutoPrincipleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing AutoPrinciple.
 */
@Service
@Transactional
public class AutoPrincipleServiceImpl implements AutoPrincipleService {

    private final Logger log = LoggerFactory.getLogger(AutoPrincipleServiceImpl.class);

    private final AutoPrincipleRepository autoPrincipleRepository;
    private final UtilsService utilsService;

    public AutoPrincipleServiceImpl(AutoPrincipleRepository autoPrincipleRepository, UtilsService utilsService) {
        this.autoPrincipleRepository = autoPrincipleRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a autoPrinciple.
     *
     * @param autoPrinciple the entity to save
     * @return the persisted entity
     */
    @Override
    public AutoPrinciple save(AutoPrinciple autoPrinciple) {
        log.debug("Request to save AutoPrinciple : {}", autoPrinciple);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            autoPrinciple.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        return autoPrincipleRepository.save(autoPrinciple);
    }

    /**
     * Get all the autoPrinciples.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AutoPrinciple> findAll(Pageable pageable) {
        log.debug("Request to get all AutoPrinciples");
        return autoPrincipleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AutoPrinciple> pageableAllAutoPrinciples(Pageable pageable) {
        log.debug("Request to get all CreditCards");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return autoPrincipleRepository.pageableAllAutoPrinciple(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<AutoPrinciple> findAll() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return new PageImpl<AutoPrinciple>(autoPrincipleRepository.findAllByCompanyIDAndTypeID(currentUserLoginAndOrg.get().getOrgGetData()));
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one autoPrinciple by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AutoPrinciple> findOne(UUID id) {
        log.debug("Request to get AutoPrinciple : {}", id);
        return autoPrincipleRepository.findById(id);
    }

    /**
     * Delete the autoPrinciple by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AutoPrinciple : {}", id);
        autoPrincipleRepository.deleteById(id);
    }

    public List<AutoPrinciple> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return autoPrincipleRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }
    public List<AutoPrinciple> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return autoPrincipleRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<AutoPrinciple> getByTypeAndCompanyID(Integer type) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return autoPrincipleRepository.getByTypeAndCompanyID(currentUserLoginAndOrg.get().getOrgGetData(), type);
        }
        throw new BadRequestAlertException("", "", "");
    }
}
