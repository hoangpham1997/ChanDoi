package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.PSSalaryTaxInsuranceRegulationService;
import vn.softdreams.ebweb.domain.PSSalaryTaxInsuranceRegulation;
import vn.softdreams.ebweb.repository.PSSalaryTaxInsuranceRegulationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PSSalaryTaxInsuranceRegulation.
 */
@Service
@Transactional
public class PSSalaryTaxInsuranceRegulationServiceImpl implements PSSalaryTaxInsuranceRegulationService {

    private final Logger log = LoggerFactory.getLogger(PSSalaryTaxInsuranceRegulationServiceImpl.class);

    private final PSSalaryTaxInsuranceRegulationRepository pSSalaryTaxInsuranceRegulationRepository;

    public PSSalaryTaxInsuranceRegulationServiceImpl(PSSalaryTaxInsuranceRegulationRepository pSSalaryTaxInsuranceRegulationRepository) {
        this.pSSalaryTaxInsuranceRegulationRepository = pSSalaryTaxInsuranceRegulationRepository;
    }

    /**
     * Save a pSSalaryTaxInsuranceRegulation.
     *
     * @param pSSalaryTaxInsuranceRegulation the entity to save
     * @return the persisted entity
     */
    @Override
    public PSSalaryTaxInsuranceRegulation save(PSSalaryTaxInsuranceRegulation pSSalaryTaxInsuranceRegulation) {
        log.debug("Request to save PSSalaryTaxInsuranceRegulation : {}", pSSalaryTaxInsuranceRegulation);        return pSSalaryTaxInsuranceRegulationRepository.save(pSSalaryTaxInsuranceRegulation);
    }

    /**
     * Get all the pSSalaryTaxInsuranceRegulations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PSSalaryTaxInsuranceRegulation> findAll(Pageable pageable) {
        log.debug("Request to get all PSSalaryTaxInsuranceRegulations");
        return pSSalaryTaxInsuranceRegulationRepository.findAll(pageable);
    }


    /**
     * Get one pSSalaryTaxInsuranceRegulation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PSSalaryTaxInsuranceRegulation> findOne(UUID id) {
        log.debug("Request to get PSSalaryTaxInsuranceRegulation : {}", id);
        return pSSalaryTaxInsuranceRegulationRepository.findById(id);
    }

    /**
     * Delete the pSSalaryTaxInsuranceRegulation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PSSalaryTaxInsuranceRegulation : {}", id);
        pSSalaryTaxInsuranceRegulationRepository.deleteById(id);
    }
}
