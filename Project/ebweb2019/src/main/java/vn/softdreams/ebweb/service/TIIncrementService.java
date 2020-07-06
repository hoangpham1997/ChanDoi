package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIIncrement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementAllDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIIncrement.
 */
public interface TIIncrementService {

    /**
     * Save a tIIncrement.
     *
     * @param tIIncrement the entity to save
     * @return the persisted entity
     */
    TIIncrement save(TIIncrement tIIncrement);

    /**
     * Get all the tIIncrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIIncrement> findAll(Pageable pageable);


    /**
     * Get the "id" tIIncrement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIIncrement> findOne(UUID id);

    /**
     * Delete the "id" tIIncrement.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<ToolsConvertDTO> getAllToolsByActive();

    Page<TIIncrementConvertDTO> getAllTIIncrementsSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    TIIncrementAllDetailsConvertDTO findDetailsByID(UUID id);

    HandlingResultDTO multiDelete(List<TIIncrement> tiIncrements);

    HandlingResultDTO multiUnRecord(List<TIIncrement> tiIncrements);

    Optional<TIIncrement> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum);

    List<ToolsConvertDTO> getToolsActiveByIncrements(String date);

    List<ToolsConvertDTO> getToolsActiveByTIDecrement(String date);

    List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIDecrement(UUID id, String date);

    List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIIncrement(UUID id, String date);
}
