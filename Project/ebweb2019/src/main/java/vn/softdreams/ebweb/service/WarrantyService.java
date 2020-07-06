package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Warranty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Warranty.
 */
public interface WarrantyService {

    /**
     * Save a warranty.
     *
     * @param warranty the entity to save
     * @return the persisted entity
     */
    Warranty save(Warranty warranty);

    /**
     * Get all the warranties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Warranty> findAll(Pageable pageable);


    /**
     * Get the "id" warranty.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Warranty> findOne(long id);

    /**
     * Delete the "id" warranty.
     *
     * @param id the id of the entity
     */
    void delete(long id);

    List<Warranty> getAllWarrantyByCompanyID();
}
