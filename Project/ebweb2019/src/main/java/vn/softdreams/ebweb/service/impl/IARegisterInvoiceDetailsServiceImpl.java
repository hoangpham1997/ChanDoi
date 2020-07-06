package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.IARegisterInvoiceDetailsService;
import vn.softdreams.ebweb.domain.IARegisterInvoiceDetails;
import vn.softdreams.ebweb.repository.IARegisterInvoiceDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing IARegisterInvoiceDetails.
 */
@Service
@Transactional
public class IARegisterInvoiceDetailsServiceImpl implements IARegisterInvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(IARegisterInvoiceDetailsServiceImpl.class);

    private final IARegisterInvoiceDetailsRepository iARegisterInvoiceDetailsRepository;

    public IARegisterInvoiceDetailsServiceImpl(IARegisterInvoiceDetailsRepository iARegisterInvoiceDetailsRepository) {
        this.iARegisterInvoiceDetailsRepository = iARegisterInvoiceDetailsRepository;
    }

    /**
     * Save a iARegisterInvoiceDetails.
     *
     * @param iARegisterInvoiceDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public IARegisterInvoiceDetails save(IARegisterInvoiceDetails iARegisterInvoiceDetails) {
        log.debug("Request to save IARegisterInvoiceDetails : {}", iARegisterInvoiceDetails);        return iARegisterInvoiceDetailsRepository.save(iARegisterInvoiceDetails);
    }

    /**
     * Get all the iARegisterInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IARegisterInvoiceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all IARegisterInvoiceDetails");
        return iARegisterInvoiceDetailsRepository.findAll(pageable);
    }


    /**
     * Get one iARegisterInvoiceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IARegisterInvoiceDetails> findOne(UUID id) {
        log.debug("Request to get IARegisterInvoiceDetails : {}", id);
        return iARegisterInvoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the iARegisterInvoiceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete IARegisterInvoiceDetails : {}", id);
        iARegisterInvoiceDetailsRepository.deleteById(id);
    }
}
