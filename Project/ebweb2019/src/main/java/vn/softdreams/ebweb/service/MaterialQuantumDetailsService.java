package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialQuantumDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialQuantumDetails.
 */
public interface MaterialQuantumDetailsService {

    /**
     * Save a materialQuantumDetails.
     *
     * @param materialQuantumDetails the entity to save
     * @return the persisted entity
     */
    MaterialQuantumDetails save(MaterialQuantumDetails materialQuantumDetails);

    /**
     * Get all the materialQuantumDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialQuantumDetails> findAll(Pageable pageable);


    /**
     * Get the "id" materialQuantumDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialQuantumDetails> findOne(UUID id);

    /**
     * Delete the "id" materialQuantumDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialQuantumDetailsDTO> findAllDetailsById(List<UUID> id);
}
