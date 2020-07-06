package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.domain.CareerGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CareerGroup.
 */
public interface CareerGroupService {

    /**
     * Save a careerGroup.
     *
     * @param careerGroup the entity to save
     * @return the persisted entity
     */
    CareerGroup save(CareerGroup careerGroup);

    /**
     * Get all the careerGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CareerGroup> findAll(Pageable pageable);


    /**
     * Get the "id" careerGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CareerGroup> findOne(UUID id);

    /**
     * Delete the "id" careerGroup.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<CareerGroup> findAllCareerGroups();
}
