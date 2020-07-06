package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPPeriod;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPPeriod.
 */
public interface CPPeriodService {

    /**
     * Save a cPPeriod.
     *
     * @param cPPeriod the entity to save
     * @return the persisted entity
     */
    CPPeriod save(CPPeriod cPPeriod);

    /**
     * Get all the cPPeriods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPPeriod> findAll(Pageable pageable);


    /**
     * Get the "id" cPPeriod.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPPeriod> findOne(UUID id);

    /**
     * Delete the "id" cPPeriod.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<CPPeriodDTO> findAllByType(Pageable pageable, Integer type);

    Integer checkDelete(UUID id);

    Boolean checkPeriod(String fromDate, String toDate, List<UUID> costSetIDs, Integer type);


    List<CPPeriod> getAllCPPeriod();

    byte[] exportExcel(Pageable pageable, Integer type);

    byte[] exportPdf(Pageable pageable, Integer type);

    CPPeriodDTO findByID(UUID id);

    CPPeriod accepted(CPPeriod cPPeriod);

    List<CPPeriod> getAllCPPeriodForReport(Boolean isDependent, UUID orgID);

    HandlingResultDTO multiDelete(List<CPPeriodDTO> cpPeriodDTOS);

}
