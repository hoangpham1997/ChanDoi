package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAllocationAllocatedService;
import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.repository.TIAllocationAllocatedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAllocationAllocated.
 */
@Service
@Transactional
public class TIAllocationAllocatedServiceImpl implements TIAllocationAllocatedService {

    private final Logger log = LoggerFactory.getLogger(TIAllocationAllocatedServiceImpl.class);

    private final TIAllocationAllocatedRepository tIAllocationAllocatedRepository;

    public TIAllocationAllocatedServiceImpl(TIAllocationAllocatedRepository tIAllocationAllocatedRepository) {
        this.tIAllocationAllocatedRepository = tIAllocationAllocatedRepository;
    }

    /**
     * Save a tIAllocationAllocated.
     *
     * @param tIAllocationAllocated the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAllocationAllocated save(TIAllocationAllocated tIAllocationAllocated) {
        log.debug("Request to save TIAllocationAllocated : {}", tIAllocationAllocated);        return tIAllocationAllocatedRepository.save(tIAllocationAllocated);
    }

    /**
     * Get all the tIAllocationAllocateds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAllocationAllocated> findAll(Pageable pageable) {
        log.debug("Request to get all TIAllocationAllocateds");
        return tIAllocationAllocatedRepository.findAll(pageable);
    }


    /**
     * Get one tIAllocationAllocated by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAllocationAllocated> findOne(UUID id) {
        log.debug("Request to get TIAllocationAllocated : {}", id);
        return tIAllocationAllocatedRepository.findById(id);
    }

    /**
     * Delete the tIAllocationAllocated by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAllocationAllocated : {}", id);
        tIAllocationAllocatedRepository.deleteById(id);
    }
}
