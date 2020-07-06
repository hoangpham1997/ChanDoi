package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TransportMethod;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TransportMethod.
 */
public interface TransportMethodService {

    /**
     * Save a transportMethod.
     *
     * @param transportMethod the entity to save
     * @return the persisted entity
     */
    TransportMethod save(TransportMethod transportMethod);

    /**
     * Get all the transportMethods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransportMethod> findAll(Pageable pageable);


    /**
     * Get the "id" transportMethod.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TransportMethod> findOne(UUID id);

    /**
     * Delete the "id" transportMethod.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<TransportMethod> getTransportMethodCombobox();
}
