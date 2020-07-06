package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TimeSheetSymbols;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TimeSheetSymbols.
 */
public interface TimeSheetSymbolsService {

    /**
     * Save a timeSheetSymbols.
     *
     * @param timeSheetSymbols the entity to save
     * @return the persisted entity
     */
    TimeSheetSymbols save(TimeSheetSymbols timeSheetSymbols);

    /**
     * Get all the timeSheetSymbols.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TimeSheetSymbols> findAll(Pageable pageable);


    /**
     * Get the "id" timeSheetSymbols.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TimeSheetSymbols> findOne(UUID id);

    /**
     * Delete the "id" timeSheetSymbols.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
