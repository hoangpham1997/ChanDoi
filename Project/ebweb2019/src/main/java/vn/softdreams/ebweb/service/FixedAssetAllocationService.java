package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.FixedAssetAllocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FixedAssetAllocation.
 */
public interface FixedAssetAllocationService {

    /**
     * Save a fixedAssetAllocation.
     *
     * @param fixedAssetAllocation the entity to save
     * @return the persisted entity
     */
    FixedAssetAllocation save(FixedAssetAllocation fixedAssetAllocation);

    /**
     * Get all the fixedAssetAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FixedAssetAllocation> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetAllocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FixedAssetAllocation> findOne(UUID id);

    /**
     * Delete the "id" fixedAssetAllocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
