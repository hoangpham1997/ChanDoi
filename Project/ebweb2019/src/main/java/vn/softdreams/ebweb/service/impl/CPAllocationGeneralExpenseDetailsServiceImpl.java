package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPAllocationGeneralExpenseDetailsService;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;
import vn.softdreams.ebweb.repository.CPAllocationGeneralExpenseDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing CPAllocationGeneralExpenseDetails.
 */
@Service
@Transactional
public class CPAllocationGeneralExpenseDetailsServiceImpl implements CPAllocationGeneralExpenseDetailsService {

    private final Logger log = LoggerFactory.getLogger(CPAllocationGeneralExpenseDetailsServiceImpl.class);

    private final CPAllocationGeneralExpenseDetailsRepository cPAllocationGeneralExpenseDetailsRepository;

    public CPAllocationGeneralExpenseDetailsServiceImpl(CPAllocationGeneralExpenseDetailsRepository cPAllocationGeneralExpenseDetailsRepository) {
        this.cPAllocationGeneralExpenseDetailsRepository = cPAllocationGeneralExpenseDetailsRepository;
    }

    /**
     * Save a cPAllocationGeneralExpenseDetails.
     *
     * @param cPAllocationGeneralExpenseDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAllocationGeneralExpenseDetails save(CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseDetails) {
        log.debug("Request to save CPAllocationGeneralExpenseDetails : {}", cPAllocationGeneralExpenseDetails);        return cPAllocationGeneralExpenseDetailsRepository.save(cPAllocationGeneralExpenseDetails);
    }

    /**
     * Get all the cPAllocationGeneralExpenseDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAllocationGeneralExpenseDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CPAllocationGeneralExpenseDetails");
        return cPAllocationGeneralExpenseDetailsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CPAllocationGeneralExpenseDetails> findAllByCPPeriodID(UUID cPPeriodID) {
        log.debug("Request to get all CPAllocationGeneralExpenseDetails");
        return cPAllocationGeneralExpenseDetailsRepository.findAllByCPPeriodID(cPPeriodID);
    }


    /**
     * Get one cPAllocationGeneralExpenseDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAllocationGeneralExpenseDetails> findOne(UUID id) {
        log.debug("Request to get CPAllocationGeneralExpenseDetails : {}", id);
        return cPAllocationGeneralExpenseDetailsRepository.findById(id);
    }

    /**
     * Delete the cPAllocationGeneralExpenseDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPAllocationGeneralExpenseDetails : {}", id);
        cPAllocationGeneralExpenseDetailsRepository.deleteById(id);
    }
}
