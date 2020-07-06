package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCPaymentDetailSalaryService;
import vn.softdreams.ebweb.domain.MCPaymentDetailSalary;
import vn.softdreams.ebweb.repository.MCPaymentDetailSalaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCPaymentDetailSalary.
 */
@Service
@Transactional
public class MCPaymentDetailSalaryServiceImpl implements MCPaymentDetailSalaryService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailSalaryServiceImpl.class);

    private final MCPaymentDetailSalaryRepository mCPaymentDetailSalaryRepository;

    public MCPaymentDetailSalaryServiceImpl(MCPaymentDetailSalaryRepository mCPaymentDetailSalaryRepository) {
        this.mCPaymentDetailSalaryRepository = mCPaymentDetailSalaryRepository;
    }

    /**
     * Save a mCPaymentDetailSalary.
     *
     * @param mCPaymentDetailSalary the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPaymentDetailSalary save(MCPaymentDetailSalary mCPaymentDetailSalary) {
        log.debug("Request to save MCPaymentDetailSalary : {}", mCPaymentDetailSalary);        return mCPaymentDetailSalaryRepository.save(mCPaymentDetailSalary);
    }

    /**
     * Get all the mCPaymentDetailSalaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPaymentDetailSalary> findAll(Pageable pageable) {
        log.debug("Request to get all MCPaymentDetailSalaries");
        return mCPaymentDetailSalaryRepository.findAll(pageable);
    }


    /**
     * Get one mCPaymentDetailSalary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPaymentDetailSalary> findOne(UUID id) {
        log.debug("Request to get MCPaymentDetailSalary : {}", id);
        return mCPaymentDetailSalaryRepository.findById(id);
    }

    /**
     * Delete the mCPaymentDetailSalary by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPaymentDetailSalary : {}", id);
        mCPaymentDetailSalaryRepository.deleteById(id);
    }
}
