package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SalePriceGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SalePriceGroup.
 */
public interface SalePriceGroupService {

    /**
     * Save a salePriceGroup.
     *
     * @param salePriceGroup the entity to save
     * @return the persisted entity
     */
    SalePriceGroup save(SalePriceGroup salePriceGroup);

    /**
     * Get all the salePriceGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SalePriceGroup> findAll(Pageable pageable);


    /**
     * Get the "id" salePriceGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SalePriceGroup> findOne(UUID id);

    /**
     * Delete the "id" salePriceGroup.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
