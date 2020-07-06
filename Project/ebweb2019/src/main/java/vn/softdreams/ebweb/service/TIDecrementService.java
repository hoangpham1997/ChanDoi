package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIDecrement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementAllDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIDecrement.
 */
public interface TIDecrementService {

    /**
     * Save a tIDecrement.
     *
     * @param tIDecrement the entity to save
     * @return the persisted entity
     */
    TIDecrement save(TIDecrement tIDecrement);

    /**
     * Get all the tIDecrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIDecrement> findAll(Pageable pageable);


    /**
     * Get the "id" tIDecrement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIDecrement> findOne(UUID id);

    /**
     * Delete the "id" tIDecrement.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<TIDecrementConvertDTO> getAllTIDecrementSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    TIDecrementAllDetailsConvertDTO findDetailsByID(UUID id);

    HandlingResultDTO multiDelete(List<TIDecrement> tiDecrements);

    HandlingResultDTO multiUnRecord(List<TIDecrement> tiDecrements);
}
