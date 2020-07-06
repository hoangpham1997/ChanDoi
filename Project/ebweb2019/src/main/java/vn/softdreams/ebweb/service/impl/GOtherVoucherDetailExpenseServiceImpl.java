package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.GOtherVoucherDetailExpenseService;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GOtherVoucherDetailExpense.
 */
@Service
@Transactional
public class GOtherVoucherDetailExpenseServiceImpl implements GOtherVoucherDetailExpenseService {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailExpenseServiceImpl.class);

    private final GOtherVoucherDetailExpenseRepository gOtherVoucherDetailExpenseRepository;

    public GOtherVoucherDetailExpenseServiceImpl(GOtherVoucherDetailExpenseRepository gOtherVoucherDetailExpenseRepository) {
        this.gOtherVoucherDetailExpenseRepository = gOtherVoucherDetailExpenseRepository;
    }

    /**
     * Save a gOtherVoucherDetailExpense.
     *
     * @param gOtherVoucherDetailExpense the entity to save
     * @return the persisted entity
     */
    @Override
    public GOtherVoucherDetailExpense save(GOtherVoucherDetailExpense gOtherVoucherDetailExpense) {
        log.debug("Request to save GOtherVoucherDetailExpense : {}", gOtherVoucherDetailExpense);        return gOtherVoucherDetailExpenseRepository.save(gOtherVoucherDetailExpense);
    }

    /**
     * Get all the gOtherVoucherDetailExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GOtherVoucherDetailExpense> findAll(Pageable pageable) {
        log.debug("Request to get all GOtherVoucherDetailExpenses");
        return gOtherVoucherDetailExpenseRepository.findAll(pageable);
    }


    /**
     * Get one gOtherVoucherDetailExpense by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GOtherVoucherDetailExpense> findOne(UUID id) {
        log.debug("Request to get GOtherVoucherDetailExpense : {}", id);
        return gOtherVoucherDetailExpenseRepository.findById(id);
    }

    /**
     * Delete the gOtherVoucherDetailExpense by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GOtherVoucherDetailExpense : {}", id);
        gOtherVoucherDetailExpenseRepository.deleteById(id);
    }
}
