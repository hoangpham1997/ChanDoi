package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIIncrementDetailsService;
import vn.softdreams.ebweb.domain.TIIncrementDetails;
import vn.softdreams.ebweb.repository.TIIncrementDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIIncrementDetails.
 */
@Service
@Transactional
public class TIIncrementDetailsServiceImpl implements TIIncrementDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIIncrementDetailsServiceImpl.class);

    private final TIIncrementDetailsRepository tIIncrementDetailsRepository;

    public TIIncrementDetailsServiceImpl(TIIncrementDetailsRepository tIIncrementDetailsRepository) {
        this.tIIncrementDetailsRepository = tIIncrementDetailsRepository;
    }

    /**
     * Save a tIIncrementDetails.
     *
     * @param tIIncrementDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIIncrementDetails save(TIIncrementDetails tIIncrementDetails) {
        log.debug("Request to save TIIncrementDetails : {}", tIIncrementDetails);        return tIIncrementDetailsRepository.save(tIIncrementDetails);
    }

    /**
     * Get all the tIIncrementDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIIncrementDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIIncrementDetails");
        return tIIncrementDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIIncrementDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIIncrementDetails> findOne(UUID id) {
        log.debug("Request to get TIIncrementDetails : {}", id);
        return tIIncrementDetailsRepository.findById(id);
    }

    /**
     * Delete the tIIncrementDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIIncrementDetails : {}", id);
        tIIncrementDetailsRepository.deleteById(id);
    }
}
