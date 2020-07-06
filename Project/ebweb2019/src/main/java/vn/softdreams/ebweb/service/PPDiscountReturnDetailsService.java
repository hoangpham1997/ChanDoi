package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailOutWardDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPDiscountReturnDetails.
 */
public interface PPDiscountReturnDetailsService {

    /**
     * Save a pPDiscountReturnDetails.
     *
     * @param pPDiscountReturnDetails the entity to save
     * @return the persisted entity
     */
    PPDiscountReturnDetails save(PPDiscountReturnDetails pPDiscountReturnDetails);

    /**
     * Get all the pPDiscountReturnDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPDiscountReturnDetails> findAll(Pageable pageable);


    /**
     * Get the "id" pPDiscountReturnDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPDiscountReturnDetails> findOne(Long id);

    /**
     * Delete the "id" pPDiscountReturnDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Page<PPDiscountReturnDetailConvertDTO> getAllPPDiscountReturnDetailsByID(UUID ppDiscountReturnId);

    List<PPDiscountReturnDetailDTO> getPPDiscountReturnDetailsByID(UUID ppDiscountReturnId);

    List<PPDiscountReturnDetailOutWardDTO> findAllDetailsById(List<UUID> id);
}
