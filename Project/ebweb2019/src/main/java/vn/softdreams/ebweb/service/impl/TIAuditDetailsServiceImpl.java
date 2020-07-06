package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAuditDetailsService;
import vn.softdreams.ebweb.domain.TIAuditDetails;
import vn.softdreams.ebweb.repository.TIAuditDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAuditDetails.
 */
@Service
@Transactional
public class TIAuditDetailsServiceImpl implements TIAuditDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIAuditDetailsServiceImpl.class);

    private final TIAuditDetailsRepository tIAuditDetailsRepository;

    public TIAuditDetailsServiceImpl(TIAuditDetailsRepository tIAuditDetailsRepository) {
        this.tIAuditDetailsRepository = tIAuditDetailsRepository;
    }

    /**
     * Save a tIAuditDetails.
     *
     * @param tIAuditDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAuditDetails save(TIAuditDetails tIAuditDetails) {
        log.debug("Request to save TIAuditDetails : {}", tIAuditDetails);        return tIAuditDetailsRepository.save(tIAuditDetails);
    }

    /**
     * Get all the tIAuditDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAuditDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIAuditDetails");
        return tIAuditDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIAuditDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAuditDetails> findOne(UUID id) {
        log.debug("Request to get TIAuditDetails : {}", id);
        return tIAuditDetailsRepository.findById(id);
    }

    /**
     * Delete the tIAuditDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAuditDetails : {}", id);
        tIAuditDetailsRepository.deleteById(id);
    }
}
