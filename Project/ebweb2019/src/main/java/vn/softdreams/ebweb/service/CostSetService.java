package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CostSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.CostSetSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CostSet.
 */
public interface CostSetService {

    /**
     * Save a costSet.
     *
     * @param costSet the entity to save
     * @return the persisted entity
     */
    CostSet save(CostSet costSet);

    /**
     * Get all the costSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CostSet> findAll(Pageable pageable, UUID branchID, String costSetCode, String costSetName, Integer costSetType,
                          String description, UUID parentID, Boolean isParentNode, String orderFixCode, Integer grade, Boolean isActive);

    Page<CostSet> findAll(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<CostSet> findAll();


    /**
     * Get the "id" costSet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CostSet> findOne(UUID id);

    /**
     * Delete the "id" costSet.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<CostSet> getAllCostSetsActive();

    List<CostSet> findAllActive();
    List<CostSet> getCostSetsByCompanyID();

//  Page<CostSet> findAllCostSetByCompanyID(Pageable pageable);
    Page<CostSet> findAllCostSetByCompanyID(Pageable pageable);

    CostSetSaveDTO saveDTO(CostSet costSet);

    HandlingResultDTO multiDelete(List<UUID> costSets);

    Page<CostSet> getCostSetsByTypeRaTio(Pageable pageable, Integer type);

    List<CostSetMaterialGoodsDTO> getCostSetByListID(List<UUID> uuids);

    List<CostSetDTO> findRevenueByCostSetID(CostSetDTO costSetDTO);

    List<CostSet> findAllByCompanyID();
    List<CostSet> findCostSetList(UUID companyID, Boolean dependent);

    List<CostSet> findAllByOrgID(UUID orgID, Boolean isDependent);
}
