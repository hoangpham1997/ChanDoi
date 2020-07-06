package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAllocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.TIAllocationConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAllocationDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAllocationSaveConvertDTO;

import javax.tools.Tool;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAllocation.
 */
public interface TIAllocationService {

    /**
     * Save a tIAllocation.
     *
     * @param tIAllocation the entity to save
     * @return the persisted entity
     */
    TIAllocation save(TIAllocationSaveConvertDTO tIAllocation);

    /**
     * Get all the tIAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAllocation> findAll(Pageable pageable);


    /**
     * Get the "id" tIAllocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAllocation> findOne(UUID id);

    /**
     * Delete the "id" tIAllocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<TIAllocationDetailConvertDTO> getTIAllocationDetails(Integer month, Integer year, Integer date);

    List<Tool> getTIAllocations(Integer month, Integer year);

    Long countToolsAllocation(List<UUID> uuidList, String date);

    Long getTIAllocationCount(Integer month, Integer year);

    TIAllocation getTIAllocationDuplicate(Integer month, Integer year);

    LocalDate getMaxMonth(UUID id);

    Page<TIAllocationConvertDTO> getAllTIAllocationSearch(Pageable pageable, String fromDate, String toDate, String keySearch);
}
