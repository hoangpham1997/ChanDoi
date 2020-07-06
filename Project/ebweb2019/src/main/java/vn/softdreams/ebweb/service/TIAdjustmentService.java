package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAdjustment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailAllConvertDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAdjustment.
 */
public interface TIAdjustmentService {

    /**
     * Save a tIAdjustment.
     *
     * @param tIAdjustment the entity to save
     * @return the persisted entity
     */
    TIAdjustment save(TIAdjustment tIAdjustment);

    /**
     * Get all the tIAdjustments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAdjustment> findAll(Pageable pageable);


    /**
     * Get the "id" tIAdjustment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAdjustment> findOne(UUID id);

    /**
     * Delete the "id" tIAdjustment.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<TITransferAndTIAdjustmentConvertDTO> getAllTIAdjustmentsSearch(Pageable pageable, String fromDate, String toDate, String keySearch);

    TIAdjustmentDetailAllConvertDTO findDetailsByID(UUID id);
}
