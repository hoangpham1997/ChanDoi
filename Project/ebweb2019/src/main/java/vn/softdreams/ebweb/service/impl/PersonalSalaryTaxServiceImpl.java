package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.PersonalSalaryTaxService;
import vn.softdreams.ebweb.domain.PersonalSalaryTax;
import vn.softdreams.ebweb.repository.PersonalSalaryTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PersonalSalaryTax.
 */
@Service
@Transactional
public class PersonalSalaryTaxServiceImpl implements PersonalSalaryTaxService {

    private final Logger log = LoggerFactory.getLogger(PersonalSalaryTaxServiceImpl.class);

    private final PersonalSalaryTaxRepository personalSalaryTaxRepository;

    public PersonalSalaryTaxServiceImpl(PersonalSalaryTaxRepository personalSalaryTaxRepository) {
        this.personalSalaryTaxRepository = personalSalaryTaxRepository;
    }

    /**
     * Save a personalSalaryTax.
     *
     * @param personalSalaryTax the entity to save
     * @return the persisted entity
     */
    @Override
    public PersonalSalaryTax save(PersonalSalaryTax personalSalaryTax) {
        log.debug("Request to save PersonalSalaryTax : {}", personalSalaryTax);        return personalSalaryTaxRepository.save(personalSalaryTax);
    }

    /**
     * Get all the personalSalaryTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PersonalSalaryTax> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalSalaryTaxes");
        return personalSalaryTaxRepository.findAll(pageable);
    }


    /**
     * Get one personalSalaryTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PersonalSalaryTax> findOne(UUID id) {
        log.debug("Request to get PersonalSalaryTax : {}", id);
        return personalSalaryTaxRepository.findById(id);
    }

    /**
     * Delete the personalSalaryTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PersonalSalaryTax : {}", id);
        personalSalaryTaxRepository.deleteById(id);
    }
}
