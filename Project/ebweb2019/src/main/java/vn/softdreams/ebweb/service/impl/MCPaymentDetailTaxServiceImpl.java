package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCPaymentDetailTaxService;
import vn.softdreams.ebweb.domain.MCPaymentDetailTax;
import vn.softdreams.ebweb.repository.MCPaymentDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCPaymentDetailTax.
 */
@Service
@Transactional
public class MCPaymentDetailTaxServiceImpl implements MCPaymentDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailTaxServiceImpl.class);

    private final MCPaymentDetailTaxRepository mCPaymentDetailTaxRepository;

    public MCPaymentDetailTaxServiceImpl(MCPaymentDetailTaxRepository mCPaymentDetailTaxRepository) {
        this.mCPaymentDetailTaxRepository = mCPaymentDetailTaxRepository;
    }

    /**
     * Save a mCPaymentDetailTax.
     *
     * @param mCPaymentDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPaymentDetailTax save(MCPaymentDetailTax mCPaymentDetailTax) {
        log.debug("Request to save MCPaymentDetailTax : {}", mCPaymentDetailTax);        return mCPaymentDetailTaxRepository.save(mCPaymentDetailTax);
    }

    /**
     * Get all the mCPaymentDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPaymentDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all MCPaymentDetailTaxes");
        return mCPaymentDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one mCPaymentDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPaymentDetailTax> findOne(UUID id) {
        log.debug("Request to get MCPaymentDetailTax : {}", id);
        return mCPaymentDetailTaxRepository.findById(id);
    }

    /**
     * Delete the mCPaymentDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPaymentDetailTax : {}", id);
        mCPaymentDetailTaxRepository.deleteById(id);
    }
}
