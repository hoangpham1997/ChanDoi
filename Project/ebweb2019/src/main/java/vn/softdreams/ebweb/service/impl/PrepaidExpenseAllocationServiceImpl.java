package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.PrepaidExpenseAllocationService;
import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;
import vn.softdreams.ebweb.repository.PrepaidExpenseAllocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PrepaidExpenseAllocation.
 */
@Service
@Transactional
public class PrepaidExpenseAllocationServiceImpl implements PrepaidExpenseAllocationService {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseAllocationServiceImpl.class);

    private final PrepaidExpenseAllocationRepository prepaidExpenseAllocationRepository;

    public PrepaidExpenseAllocationServiceImpl(PrepaidExpenseAllocationRepository prepaidExpenseAllocationRepository) {
        this.prepaidExpenseAllocationRepository = prepaidExpenseAllocationRepository;
    }

    /**
     * Save a prepaidExpenseAllocation.
     *
     * @param prepaidExpenseAllocation the entity to save
     * @return the persisted entity
     */
    @Override
    public PrepaidExpenseAllocation save(PrepaidExpenseAllocation prepaidExpenseAllocation) {
        log.debug("Request to save PrepaidExpenseAllocation : {}", prepaidExpenseAllocation);        return prepaidExpenseAllocationRepository.save(prepaidExpenseAllocation);
    }

    /**
     * Get all the prepaidExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrepaidExpenseAllocation> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaidExpenseAllocations");
        return prepaidExpenseAllocationRepository.findAll(pageable);
    }


    /**
     * Get one prepaidExpenseAllocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaidExpenseAllocation> findOne(UUID id) {
        log.debug("Request to get PrepaidExpenseAllocation : {}", id);
        return prepaidExpenseAllocationRepository.findById(id);
    }

    /**
     * Delete the prepaidExpenseAllocation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PrepaidExpenseAllocation : {}", id);
        prepaidExpenseAllocationRepository.deleteById(id);
    }
}
