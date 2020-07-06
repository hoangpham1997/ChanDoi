package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPExpenseListService;
import vn.softdreams.ebweb.domain.CPExpenseList;
import vn.softdreams.ebweb.repository.CPExpenseListRepository;
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
 * Service Implementation for managing CPExpenseList.
 */
@Service
@Transactional
public class CPExpenseListServiceImpl implements CPExpenseListService {

    private final Logger log = LoggerFactory.getLogger(CPExpenseListServiceImpl.class);

    private final CPExpenseListRepository cPExpenseListRepository;

    public CPExpenseListServiceImpl(CPExpenseListRepository cPExpenseListRepository) {
        this.cPExpenseListRepository = cPExpenseListRepository;
    }

    /**
     * Save a cPExpenseList.
     *
     * @param cPExpenseList the entity to save
     * @return the persisted entity
     */
    @Override
    public CPExpenseList save(CPExpenseList cPExpenseList) {
        log.debug("Request to save CPExpenseList : {}", cPExpenseList);        return cPExpenseListRepository.save(cPExpenseList);
    }

    /**
     * Get all the cPExpenseLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPExpenseList> findAll(Pageable pageable) {
        log.debug("Request to get all CPExpenseLists");
        return cPExpenseListRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CPExpenseList> findAllByCPPeriodID(UUID cPPeriodID) {
        log.debug("Request to get all CPExpenseLists");
        return cPExpenseListRepository.findAllByCPPeriodID(cPPeriodID);
    }

    /**
     * Get one cPExpenseList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPExpenseList> findOne(UUID id) {
        log.debug("Request to get CPExpenseList : {}", id);
        return cPExpenseListRepository.findById(id);
    }

    /**
     * Delete the cPExpenseList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPExpenseList : {}", id);
        cPExpenseListRepository.deleteById(id);
    }
}
