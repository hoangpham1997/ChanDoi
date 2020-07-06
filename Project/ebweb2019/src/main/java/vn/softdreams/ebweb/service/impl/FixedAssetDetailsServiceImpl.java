package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.FixedAssetDetailsService;
import vn.softdreams.ebweb.domain.FixedAssetDetails;
import vn.softdreams.ebweb.repository.FixedAssetDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing FixedAssetDetails.
 */
@Service
@Transactional
public class FixedAssetDetailsServiceImpl implements FixedAssetDetailsService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetDetailsServiceImpl.class);

    private final FixedAssetDetailsRepository fixedAssetDetailsRepository;

    public FixedAssetDetailsServiceImpl(FixedAssetDetailsRepository fixedAssetDetailsRepository) {
        this.fixedAssetDetailsRepository = fixedAssetDetailsRepository;
    }

    /**
     * Save a fixedAssetDetails.
     *
     * @param fixedAssetDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public FixedAssetDetails save(FixedAssetDetails fixedAssetDetails) {
        log.debug("Request to save FixedAssetDetails : {}", fixedAssetDetails);        return fixedAssetDetailsRepository.save(fixedAssetDetails);
    }

    /**
     * Get all the fixedAssetDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetDetails> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetDetails");
        return fixedAssetDetailsRepository.findAll(pageable);
    }


    /**
     * Get one fixedAssetDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetDetails> findOne(UUID id) {
        log.debug("Request to get FixedAssetDetails : {}", id);
        return fixedAssetDetailsRepository.findById(id);
    }

    /**
     * Delete the fixedAssetDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FixedAssetDetails : {}", id);
        fixedAssetDetailsRepository.deleteById(id);
    }
}
