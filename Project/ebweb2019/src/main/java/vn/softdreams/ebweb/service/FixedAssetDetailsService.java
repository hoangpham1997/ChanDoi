package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.FixedAssetDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FixedAssetDetails.
 */
public interface FixedAssetDetailsService {

    /**
     * Save a fixedAssetDetails.
     *
     * @param fixedAssetDetails the entity to save
     * @return the persisted entity
     */
    FixedAssetDetails save(FixedAssetDetails fixedAssetDetails);

    /**
     * Get all the fixedAssetDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FixedAssetDetails> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FixedAssetDetails> findOne(UUID id);

    /**
     * Delete the "id" fixedAssetDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
