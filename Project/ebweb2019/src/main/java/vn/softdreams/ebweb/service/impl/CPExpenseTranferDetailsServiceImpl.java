package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPExpenseTranferDetailsService;
import vn.softdreams.ebweb.domain.CPExpenseTranferDetails;
import vn.softdreams.ebweb.repository.CPExpenseTranferDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CPExpenseTranferDetails.
 */
@Service
@Transactional
public class CPExpenseTranferDetailsServiceImpl implements CPExpenseTranferDetailsService {

    private final Logger log = LoggerFactory.getLogger(CPExpenseTranferDetailsServiceImpl.class);

    private final CPExpenseTranferDetailsRepository cPExpenseTranferDetailsRepository;

    public CPExpenseTranferDetailsServiceImpl(CPExpenseTranferDetailsRepository cPExpenseTranferDetailsRepository) {
        this.cPExpenseTranferDetailsRepository = cPExpenseTranferDetailsRepository;
    }

    /**
     * Save a cPExpenseTranferDetails.
     *
     * @param cPExpenseTranferDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public CPExpenseTranferDetails save(CPExpenseTranferDetails cPExpenseTranferDetails) {
        log.debug("Request to save CPExpenseTranferDetails : {}", cPExpenseTranferDetails);        return cPExpenseTranferDetailsRepository.save(cPExpenseTranferDetails);
    }

    /**
     * Get all the cPExpenseTranferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPExpenseTranferDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CPExpenseTranferDetails");
        return cPExpenseTranferDetailsRepository.findAll(pageable);
    }


    /**
     * Get one cPExpenseTranferDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPExpenseTranferDetails> findOne(Long id) {
        log.debug("Request to get CPExpenseTranferDetails : {}", id);
        return cPExpenseTranferDetailsRepository.findById(id);
    }

    /**
     * Delete the cPExpenseTranferDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CPExpenseTranferDetails : {}", id);
        cPExpenseTranferDetailsRepository.deleteById(id);
    }
}
