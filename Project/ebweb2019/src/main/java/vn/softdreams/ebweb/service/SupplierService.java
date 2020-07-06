package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Supplier.
 */
public interface SupplierService {

    /**
     * Save a Supplier.
     *
     * @param Supplier the entity to save
     * @return the persisted entity
     */
    Supplier save(Supplier Supplier);

    /**
     * Get all the Suppliers.
     *
     * @return the list of entities
     */
    List<Supplier> findAll();

    /**
     * Get all the Suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Supplier> findAll(Pageable pageable);


    /**
     * Get the "id" Supplier.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Supplier> findOne(UUID id);

    /**
     * Delete the "id" Supplier.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);


}
