package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TITransfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailsAllConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TITransfer.
 */
public interface TITransferService {

    /**
     * Save a tITransfer.
     *
     * @param tITransfer the entity to save
     * @return the persisted entity
     */
    TITransfer save(TITransfer tITransfer);

    /**
     * Get all the tITransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TITransfer> findAll(Pageable pageable);


    /**
     * Get the "id" tITransfer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TITransfer> findOne(UUID id);

    /**
     * Delete the "id" tITransfer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<ToolsConvertDTO> getToolsActiveByTITransfer();

    Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    TITransferDetailsAllConvertDTO findDetailsByID(UUID id);
}
