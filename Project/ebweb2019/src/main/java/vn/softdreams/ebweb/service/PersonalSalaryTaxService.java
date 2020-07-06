package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PersonalSalaryTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PersonalSalaryTax.
 */
public interface PersonalSalaryTaxService {

    /**
     * Save a personalSalaryTax.
     *
     * @param personalSalaryTax the entity to save
     * @return the persisted entity
     */
    PersonalSalaryTax save(PersonalSalaryTax personalSalaryTax);

    /**
     * Get all the personalSalaryTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PersonalSalaryTax> findAll(Pageable pageable);


    /**
     * Get the "id" personalSalaryTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PersonalSalaryTax> findOne(UUID id);

    /**
     * Delete the "id" personalSalaryTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
