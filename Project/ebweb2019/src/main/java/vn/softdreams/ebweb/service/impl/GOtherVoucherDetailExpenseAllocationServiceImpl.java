package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.GOtherVoucherDetailExpenseAllocationService;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailExpenseAllocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GOtherVoucherDetailExpenseAllocation.
 */
@Service
@Transactional
public class GOtherVoucherDetailExpenseAllocationServiceImpl implements GOtherVoucherDetailExpenseAllocationService {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailExpenseAllocationServiceImpl.class);

    private final GOtherVoucherDetailExpenseAllocationRepository gOtherVoucherDetailExpenseAllocationRepository;

    public GOtherVoucherDetailExpenseAllocationServiceImpl(GOtherVoucherDetailExpenseAllocationRepository gOtherVoucherDetailExpenseAllocationRepository) {
        this.gOtherVoucherDetailExpenseAllocationRepository = gOtherVoucherDetailExpenseAllocationRepository;
    }

    /**
     * Save a gOtherVoucherDetailExpenseAllocation.
     *
     * @param gOtherVoucherDetailExpenseAllocation the entity to save
     * @return the persisted entity
     */
    @Override
    public GOtherVoucherDetailExpenseAllocation save(GOtherVoucherDetailExpenseAllocation gOtherVoucherDetailExpenseAllocation) {
        log.debug("Request to save GOtherVoucherDetailExpenseAllocation : {}", gOtherVoucherDetailExpenseAllocation);        return gOtherVoucherDetailExpenseAllocationRepository.save(gOtherVoucherDetailExpenseAllocation);
    }

    /**
     * Get all the gOtherVoucherDetailExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GOtherVoucherDetailExpenseAllocation> findAll(Pageable pageable) {
        log.debug("Request to get all GOtherVoucherDetailExpenseAllocations");
        return gOtherVoucherDetailExpenseAllocationRepository.findAll(pageable);
    }


    /**
     * Get one gOtherVoucherDetailExpenseAllocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GOtherVoucherDetailExpenseAllocation> findOne(UUID id) {
        log.debug("Request to get GOtherVoucherDetailExpenseAllocation : {}", id);
        return gOtherVoucherDetailExpenseAllocationRepository.findById(id);
    }

    /**
     * Delete the gOtherVoucherDetailExpenseAllocation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GOtherVoucherDetailExpenseAllocation : {}", id);
        gOtherVoucherDetailExpenseAllocationRepository.deleteById(id);
    }
}
