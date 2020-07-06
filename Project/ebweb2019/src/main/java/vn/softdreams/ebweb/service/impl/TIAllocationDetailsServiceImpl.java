package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAllocationDetailsService;
import vn.softdreams.ebweb.domain.TIAllocationDetails;
import vn.softdreams.ebweb.repository.TIAllocationDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAllocationDetails.
 */
@Service
@Transactional
public class TIAllocationDetailsServiceImpl implements TIAllocationDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIAllocationDetailsServiceImpl.class);

    private final TIAllocationDetailsRepository tIAllocationDetailsRepository;

    public TIAllocationDetailsServiceImpl(TIAllocationDetailsRepository tIAllocationDetailsRepository) {
        this.tIAllocationDetailsRepository = tIAllocationDetailsRepository;
    }

    /**
     * Save a tIAllocationDetails.
     *
     * @param tIAllocationDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAllocationDetails save(TIAllocationDetails tIAllocationDetails) {
        log.debug("Request to save TIAllocationDetails : {}", tIAllocationDetails);        return tIAllocationDetailsRepository.save(tIAllocationDetails);
    }

    /**
     * Get all the tIAllocationDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAllocationDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIAllocationDetails");
        return tIAllocationDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIAllocationDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAllocationDetails> findOne(UUID id) {
        log.debug("Request to get TIAllocationDetails : {}", id);
        return tIAllocationDetailsRepository.findById(id);
    }

    /**
     * Delete the tIAllocationDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAllocationDetails : {}", id);
        tIAllocationDetailsRepository.deleteById(id);
    }
}
