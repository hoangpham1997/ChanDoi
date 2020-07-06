package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CostSetMaterialGood;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CostSetMaterialGood.
 */
public interface CostSetMaterialGoodService {

    /**
     * Save a costSetMaterialGood.
     *
     * @param costSetMaterialGood the entity to save
     * @return the persisted entity
     */
    CostSetMaterialGood save(CostSetMaterialGood costSetMaterialGood);

    /**
     * Get all the costSetMaterialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CostSetMaterialGood> findAll(Pageable pageable);


    /**
     * Get the "id" costSetMaterialGood.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CostSetMaterialGood> findOne(UUID id);

    /**
     * Delete the "id" costSetMaterialGood.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<TheTinhGiaThanhDTO> getAllByCompanyID(Integer typeMethod);

    List<TheTinhGiaThanhDTO> getAllForReport(Integer typeMethod, Boolean isDependent, UUID orgID);
}
