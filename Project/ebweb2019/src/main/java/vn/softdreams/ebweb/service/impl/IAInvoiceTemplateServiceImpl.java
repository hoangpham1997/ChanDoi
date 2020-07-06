package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.IAInvoiceTemplateService;
import vn.softdreams.ebweb.domain.IAInvoiceTemplate;
import vn.softdreams.ebweb.repository.IAInvoiceTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing IAInvoiceTemplate.
 */
@Service
@Transactional
public class IAInvoiceTemplateServiceImpl implements IAInvoiceTemplateService {

    private final Logger log = LoggerFactory.getLogger(IAInvoiceTemplateServiceImpl.class);

    private final IAInvoiceTemplateRepository iAInvoiceTemplateRepository;

    public IAInvoiceTemplateServiceImpl(IAInvoiceTemplateRepository iAInvoiceTemplateRepository) {
        this.iAInvoiceTemplateRepository = iAInvoiceTemplateRepository;
    }

    /**
     * Save a iAInvoiceTemplate.
     *
     * @param iAInvoiceTemplate the entity to save
     * @return the persisted entity
     */
    @Override
    public IAInvoiceTemplate save(IAInvoiceTemplate iAInvoiceTemplate) {
        log.debug("Request to save IAInvoiceTemplate : {}", iAInvoiceTemplate);        return iAInvoiceTemplateRepository.save(iAInvoiceTemplate);
    }

    /**
     * Get all the iAInvoiceTemplates.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IAInvoiceTemplate> findAll() {
        log.debug("Request to get all IAInvoiceTemplates");
        return iAInvoiceTemplateRepository.findAll();
    }


    /**
     * Get one iAInvoiceTemplate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IAInvoiceTemplate> findOne(UUID id) {
        log.debug("Request to get IAInvoiceTemplate : {}", id);
        return iAInvoiceTemplateRepository.findById(id);
    }

    /**
     * Delete the iAInvoiceTemplate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete IAInvoiceTemplate : {}", id);
        iAInvoiceTemplateRepository.deleteById(id);
    }
}
