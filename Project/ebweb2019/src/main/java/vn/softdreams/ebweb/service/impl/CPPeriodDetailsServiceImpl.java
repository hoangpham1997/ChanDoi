package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPPeriodDetailsService;
import vn.softdreams.ebweb.domain.CPPeriodDetails;
import vn.softdreams.ebweb.repository.CPPeriodDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing CPPeriodDetails.
 */
@Service
@Transactional
public class CPPeriodDetailsServiceImpl implements CPPeriodDetailsService {

    private final Logger log = LoggerFactory.getLogger(CPPeriodDetailsServiceImpl.class);

    private final CPPeriodDetailsRepository cPPeriodDetailsRepository;

    public CPPeriodDetailsServiceImpl(CPPeriodDetailsRepository cPPeriodDetailsRepository) {
        this.cPPeriodDetailsRepository = cPPeriodDetailsRepository;
    }

    /**
     * Save a cPPeriodDetails.
     *
     * @param cPPeriodDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public CPPeriodDetails save(CPPeriodDetails cPPeriodDetails) {
        log.debug("Request to save CPPeriodDetails : {}", cPPeriodDetails);        return cPPeriodDetailsRepository.save(cPPeriodDetails);
    }

    /**
     * Get all the cPPeriodDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPPeriodDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CPPeriodDetails");
        return cPPeriodDetailsRepository.findAll(pageable);
    }


    /**
     * Get one cPPeriodDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPPeriodDetails> findOne(UUID id) {
        log.debug("Request to get CPPeriodDetails : {}", id);
        return cPPeriodDetailsRepository.findById(id);
    }

    /**
     * Delete the cPPeriodDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPPeriodDetails : {}", id);
        cPPeriodDetailsRepository.deleteById(id);
    }
}
