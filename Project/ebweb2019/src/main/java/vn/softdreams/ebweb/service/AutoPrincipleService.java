package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AutoPrinciple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AutoPrinciple.
 */
public interface AutoPrincipleService {

    /**
     * Save a autoPrinciple.
     *
     * @param autoPrinciple the entity to save
     * @return the persisted entity
     */
    AutoPrinciple save(AutoPrinciple autoPrinciple);

    /**
     * Get all the autoPrinciples.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AutoPrinciple> findAll(Pageable pageable);
    Page<AutoPrinciple> pageableAllAutoPrinciples(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<AutoPrinciple> findAll();


    /**
     * Get the "id" autoPrinciple.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AutoPrinciple> findOne(UUID id);

    /**
     * Delete the "id" autoPrinciple.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<AutoPrinciple> findAllActive();

    List<AutoPrinciple> findAllByCompanyID();

    List<AutoPrinciple> getByTypeAndCompanyID(Integer type);
}
