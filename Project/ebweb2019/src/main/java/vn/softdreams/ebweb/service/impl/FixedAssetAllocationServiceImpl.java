package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.FixedAssetAllocationService;
import vn.softdreams.ebweb.domain.FixedAssetAllocation;
import vn.softdreams.ebweb.repository.FixedAssetAllocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing FixedAssetAllocation.
 */
@Service
@Transactional
public class FixedAssetAllocationServiceImpl implements FixedAssetAllocationService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAllocationServiceImpl.class);

    private final FixedAssetAllocationRepository fixedAssetAllocationRepository;

    public FixedAssetAllocationServiceImpl(FixedAssetAllocationRepository fixedAssetAllocationRepository) {
        this.fixedAssetAllocationRepository = fixedAssetAllocationRepository;
    }

    /**
     * Save a fixedAssetAllocation.
     *
     * @param fixedAssetAllocation the entity to save
     * @return the persisted entity
     */
    @Override
    public FixedAssetAllocation save(FixedAssetAllocation fixedAssetAllocation) {
        log.debug("Request to save FixedAssetAllocation : {}", fixedAssetAllocation);        return fixedAssetAllocationRepository.save(fixedAssetAllocation);
    }

    /**
     * Get all the fixedAssetAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetAllocation> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetAllocations");
        return fixedAssetAllocationRepository.findAll(pageable);
    }


    /**
     * Get one fixedAssetAllocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetAllocation> findOne(UUID id) {
        log.debug("Request to get FixedAssetAllocation : {}", id);
        return fixedAssetAllocationRepository.findById(id);
    }

    /**
     * Delete the fixedAssetAllocation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FixedAssetAllocation : {}", id);
        fixedAssetAllocationRepository.deleteById(id);
    }
}
