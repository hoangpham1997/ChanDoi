package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCAuditDetailsService;
import vn.softdreams.ebweb.domain.MCAuditDetails;
import vn.softdreams.ebweb.repository.MCAuditDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCAuditDetails.
 */
@Service
@Transactional
public class MCAuditDetailsServiceImpl implements MCAuditDetailsService {

    private final Logger log = LoggerFactory.getLogger(MCAuditDetailsServiceImpl.class);

    private final MCAuditDetailsRepository mCAuditDetailsRepository;

    public MCAuditDetailsServiceImpl(MCAuditDetailsRepository mCAuditDetailsRepository) {
        this.mCAuditDetailsRepository = mCAuditDetailsRepository;
    }

    /**
     * Save a mCAuditDetails.
     *
     * @param mCAuditDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MCAuditDetails save(MCAuditDetails mCAuditDetails) {
        log.debug("Request to save MCAuditDetails : {}", mCAuditDetails);        return mCAuditDetailsRepository.save(mCAuditDetails);
    }

    /**
     * Get all the mCAuditDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCAuditDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MCAuditDetails");
        return mCAuditDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mCAuditDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCAuditDetails> findOne(UUID id) {
        log.debug("Request to get MCAuditDetails : {}", id);
        return mCAuditDetailsRepository.findById(id);
    }

    /**
     * Delete the mCAuditDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCAuditDetails : {}", id);
        mCAuditDetailsRepository.deleteById(id);
    }
}
