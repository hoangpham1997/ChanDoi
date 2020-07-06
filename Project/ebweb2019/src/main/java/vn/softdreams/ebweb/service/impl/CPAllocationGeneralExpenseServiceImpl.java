package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPAllocationGeneralExpenseService;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;
import vn.softdreams.ebweb.repository.CPAllocationGeneralExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing CPAllocationGeneralExpense.
 */
@Service
@Transactional
public class CPAllocationGeneralExpenseServiceImpl implements CPAllocationGeneralExpenseService {

    private final Logger log = LoggerFactory.getLogger(CPAllocationGeneralExpenseServiceImpl.class);

    private final CPAllocationGeneralExpenseRepository cPAllocationGeneralExpenseRepository;

    public CPAllocationGeneralExpenseServiceImpl(CPAllocationGeneralExpenseRepository cPAllocationGeneralExpenseRepository) {
        this.cPAllocationGeneralExpenseRepository = cPAllocationGeneralExpenseRepository;
    }

    /**
     * Save a cPAllocationGeneralExpense.
     *
     * @param cPAllocationGeneralExpense the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAllocationGeneralExpense save(CPAllocationGeneralExpense cPAllocationGeneralExpense) {
        log.debug("Request to save CPAllocationGeneralExpense : {}", cPAllocationGeneralExpense);        return cPAllocationGeneralExpenseRepository.save(cPAllocationGeneralExpense);
    }

    /**
     * Get all the cPAllocationGeneralExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAllocationGeneralExpense> findAll(Pageable pageable) {
        log.debug("Request to get all CPAllocationGeneralExpenses");
        return cPAllocationGeneralExpenseRepository.findAll(pageable);
    }


    /**
     * Get one cPAllocationGeneralExpense by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAllocationGeneralExpense> findOne(UUID id) {
        log.debug("Request to get CPAllocationGeneralExpense : {}", id);
        return cPAllocationGeneralExpenseRepository.findById(id);
    }

    /**
     * Delete the cPAllocationGeneralExpense by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPAllocationGeneralExpense : {}", id);
        cPAllocationGeneralExpenseRepository.deleteById(id);
    }
}
