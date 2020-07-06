package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.EMContract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.EMContractConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing EMContract.
 */
public interface EMContractService {

    /**
     * Save a eMContract.
     *
     * @param eMContract the entity to save
     * @return the persisted entity
     */
    EMContract save(EMContract eMContract);

    /**
     * Get all the eMContracts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EMContract> findAll(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<EMContract> findAll();


    /**
     * Get the "id" eMContract.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EMContract> findOne(UUID id);

    /**
     * Delete the "id" eMContract.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<EMContract> getAllEMContractsActive();

    List<EMContract> findAllActive();

    List<EMContract> getAllForReport(Boolean isDependent, UUID orgID);
}
