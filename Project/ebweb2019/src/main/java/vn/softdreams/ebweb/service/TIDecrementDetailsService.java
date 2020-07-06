package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIDecrementDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIDecrementDetails.
 */
public interface TIDecrementDetailsService {

    /**
     * Save a tIDecrementDetails.
     *
     * @param tIDecrementDetails the entity to save
     * @return the persisted entity
     */
    TIDecrementDetails save(TIDecrementDetails tIDecrementDetails);

    /**
     * Get all the tIDecrementDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIDecrementDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIDecrementDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIDecrementDetails> findOne(UUID id);

    /**
     * Delete the "id" tIDecrementDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<TIDecrementDTO> getDetailsTIDecrementDTO (UUID id);


}
