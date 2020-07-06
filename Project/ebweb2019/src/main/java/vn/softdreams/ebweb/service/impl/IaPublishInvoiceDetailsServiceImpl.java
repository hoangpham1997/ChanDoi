package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.IaPublishInvoiceDetailsService;
import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;
import vn.softdreams.ebweb.repository.IaPublishInvoiceDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing IaPublishInvoiceDetails.
 */
@Service
@Transactional
public class IaPublishInvoiceDetailsServiceImpl implements IaPublishInvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(IaPublishInvoiceDetailsServiceImpl.class);

    private final IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository;

    public IaPublishInvoiceDetailsServiceImpl(IaPublishInvoiceDetailsRepository iaPublishInvoiceDetailsRepository) {
        this.iaPublishInvoiceDetailsRepository = iaPublishInvoiceDetailsRepository;
    }

    /**
     * Save a iaPublishInvoiceDetails.
     *
     * @param iaPublishInvoiceDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public IaPublishInvoiceDetails save(IaPublishInvoiceDetails iaPublishInvoiceDetails) {
        log.debug("Request to save IaPublishInvoiceDetails : {}", iaPublishInvoiceDetails);
        return iaPublishInvoiceDetailsRepository.save(iaPublishInvoiceDetails);
    }

    /**
     * Get all the iaPublishInvoiceDetails.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IaPublishInvoiceDetails> findAll() {
        log.debug("Request to get all IaPublishInvoiceDetails");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return iaPublishInvoiceDetailsRepository.findByIaPublishInvoiceCompanyId(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }


    /**
     * Get one iaPublishInvoiceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IaPublishInvoiceDetails> findOne(Long id) {
        log.debug("Request to get IaPublishInvoiceDetails : {}", id);
        return iaPublishInvoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the iaPublishInvoiceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IaPublishInvoiceDetails : {}", id);
        iaPublishInvoiceDetailsRepository.deleteById(id);
    }

    @Override
    public List<IAPublishInvoiceDetailDTO> getAllByCompany() {
        log.debug("Request to get all IaPublishInvoiceDetails");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return iaPublishInvoiceDetailsRepository.getAllByCompany(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    @Override
    public List<IAPublishInvoiceDetailDTO> getFollowTransferByCompany() {
        log.debug("Request to get all IaPublishInvoiceDetails");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return iaPublishInvoiceDetailsRepository.getFollowTransferByCompany(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }
}
