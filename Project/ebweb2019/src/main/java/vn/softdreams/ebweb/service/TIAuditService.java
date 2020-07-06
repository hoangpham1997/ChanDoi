package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAudit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailAllDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAudit.
 */
public interface TIAuditService {

    /**
     * Save a tIAudit.
     *
     * @param tIAudit the entity to save
     * @return the persisted entity
     */
    TIAudit save(TIAudit tIAudit);

    /**
     * Get all the tIAudits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAudit> findAll(Pageable pageable);


    /**
     * Get the "id" tIAudit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAudit> findOne(UUID id);

    /**
     * Delete the "id" tIAudit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<ToolsDetailsTiAuditConvertDTO> getTIAudit(String date);

    List<ToolsDetailsTiAuditConvertDTO> getAllToolsByTiAuditID(UUID id);

    TIAuditDetailAllDTO findDetailsByID(UUID id);

    Page<TIAuditConvertDTO> getAllTIAuditSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    Optional<TIAudit> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum);

    HandlingResultDTO multiDelete(List<TIAudit> tiAudits);

    void deleteRelationShip(UUID id);
}
