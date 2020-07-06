package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PSSalaryTaxInsuranceRegulation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PSSalaryTaxInsuranceRegulation.
 */
public interface PSSalaryTaxInsuranceRegulationService {

    /**
     * Save a pSSalaryTaxInsuranceRegulation.
     *
     * @param pSSalaryTaxInsuranceRegulation the entity to save
     * @return the persisted entity
     */
    PSSalaryTaxInsuranceRegulation save(PSSalaryTaxInsuranceRegulation pSSalaryTaxInsuranceRegulation);

    /**
     * Get all the pSSalaryTaxInsuranceRegulations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PSSalaryTaxInsuranceRegulation> findAll(Pageable pageable);


    /**
     * Get the "id" pSSalaryTaxInsuranceRegulation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PSSalaryTaxInsuranceRegulation> findOne(UUID id);

    /**
     * Delete the "id" pSSalaryTaxInsuranceRegulation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
