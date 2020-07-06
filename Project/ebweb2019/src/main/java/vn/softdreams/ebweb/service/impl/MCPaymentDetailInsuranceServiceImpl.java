package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCPaymentDetailInsuranceService;
import vn.softdreams.ebweb.domain.MCPaymentDetailInsurance;
import vn.softdreams.ebweb.repository.MCPaymentDetailInsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCPaymentDetailInsurance.
 */
@Service
@Transactional
public class MCPaymentDetailInsuranceServiceImpl implements MCPaymentDetailInsuranceService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailInsuranceServiceImpl.class);

    private final MCPaymentDetailInsuranceRepository mCPaymentDetailInsuranceRepository;

    public MCPaymentDetailInsuranceServiceImpl(MCPaymentDetailInsuranceRepository mCPaymentDetailInsuranceRepository) {
        this.mCPaymentDetailInsuranceRepository = mCPaymentDetailInsuranceRepository;
    }

    /**
     * Save a mCPaymentDetailInsurance.
     *
     * @param mCPaymentDetailInsurance the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPaymentDetailInsurance save(MCPaymentDetailInsurance mCPaymentDetailInsurance) {
        log.debug("Request to save MCPaymentDetailInsurance : {}", mCPaymentDetailInsurance);        return mCPaymentDetailInsuranceRepository.save(mCPaymentDetailInsurance);
    }

    /**
     * Get all the mCPaymentDetailInsurances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPaymentDetailInsurance> findAll(Pageable pageable) {
        log.debug("Request to get all MCPaymentDetailInsurances");
        return mCPaymentDetailInsuranceRepository.findAll(pageable);
    }


    /**
     * Get one mCPaymentDetailInsurance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPaymentDetailInsurance> findOne(UUID id) {
        log.debug("Request to get MCPaymentDetailInsurance : {}", id);
        return mCPaymentDetailInsuranceRepository.findById(id);
    }

    /**
     * Delete the mCPaymentDetailInsurance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPaymentDetailInsurance : {}", id);
        mCPaymentDetailInsuranceRepository.deleteById(id);
    }
}
