package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAdjustmentDetailsService;
import vn.softdreams.ebweb.domain.TIAdjustmentDetails;
import vn.softdreams.ebweb.repository.TIAdjustmentDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAdjustmentDetails.
 */
@Service
@Transactional
public class TIAdjustmentDetailsServiceImpl implements TIAdjustmentDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIAdjustmentDetailsServiceImpl.class);

    private final TIAdjustmentDetailsRepository tIAdjustmentDetailsRepository;

    public TIAdjustmentDetailsServiceImpl(TIAdjustmentDetailsRepository tIAdjustmentDetailsRepository) {
        this.tIAdjustmentDetailsRepository = tIAdjustmentDetailsRepository;
    }

    /**
     * Save a tIAdjustmentDetails.
     *
     * @param tIAdjustmentDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAdjustmentDetails save(TIAdjustmentDetails tIAdjustmentDetails) {
        log.debug("Request to save TIAdjustmentDetails : {}", tIAdjustmentDetails);        return tIAdjustmentDetailsRepository.save(tIAdjustmentDetails);
    }

    /**
     * Get all the tIAdjustmentDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAdjustmentDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIAdjustmentDetails");
        return tIAdjustmentDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIAdjustmentDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAdjustmentDetails> findOne(UUID id) {
        log.debug("Request to get TIAdjustmentDetails : {}", id);
        return tIAdjustmentDetailsRepository.findById(id);
    }

    /**
     * Delete the tIAdjustmentDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAdjustmentDetails : {}", id);
        tIAdjustmentDetailsRepository.deleteById(id);
    }
}
