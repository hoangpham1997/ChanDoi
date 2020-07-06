package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.service.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing FAIncrement.
 */
public interface FAIncrementService {

    /**
     * Save a faIncrement.
     *
     * @param faIncrement the entity to save
     * @return the persisted entity
     */
    FAIncrement save(FAIncrement faIncrement);

    /**
     * Get all the faIncrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FAIncrement> findAll(Pageable pageable);


    /**
     * Get the "id" faIncrement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FAIncrement> findOne(UUID id);

    /**
     * Delete the "id" faIncrement.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<FixedAssetDTO> getAllFixedAssetByActive();

    Page<FAIncrementConvertDTO> getAllFAIncrementsSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    FAIncrementAllDetailsConvertDTO findDetailsByID(UUID id);

    HandlingResultDTO multiDelete(List<FAIncrement> tiIncrements);

    HandlingResultDTO multiUnRecord(List<FAIncrement> tiIncrements);

    Optional<FAIncrement> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum);

    byte[] exportPdf(String fromDate, String toDate, String keySearch);

    byte[] exportExcel(String fromDate, String toDate, String keySearch);
}
