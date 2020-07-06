package vn.softdreams.ebweb.service;

import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.ContractState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing ContractState.
 */
public interface ContractStateService {

    /**
     * Save a contractState.
     *
     * @param contractState the entity to save
     * @return the persisted entity
     */
    ContractState save(ContractState contractState);

    /**
     * Get all the contractStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContractState> findAll(Pageable pageable);


    /**
     * Get the "id" contractState.
     *
     * @param id the id of the entity
     * @return the entity
     */


    /**
     * Delete the "id" contractState.
     *
     * @param id the id of the entity
     */

    @Transactional(readOnly = true)
    Optional<ContractState> findOne(Long id);

    void delete(Long id);
}
