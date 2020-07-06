package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAllocationAllocated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAllocationAllocated.
 */
public interface TIAllocationAllocatedService {

    /**
     * Save a tIAllocationAllocated.
     *
     * @param tIAllocationAllocated the entity to save
     * @return the persisted entity
     */
    TIAllocationAllocated save(TIAllocationAllocated tIAllocationAllocated);

    /**
     * Get all the tIAllocationAllocateds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAllocationAllocated> findAll(Pageable pageable);


    /**
     * Get the "id" tIAllocationAllocated.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAllocationAllocated> findOne(UUID id);

    /**
     * Delete the "id" tIAllocationAllocated.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
