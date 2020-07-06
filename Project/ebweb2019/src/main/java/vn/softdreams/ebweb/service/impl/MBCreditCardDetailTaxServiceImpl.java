package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBCreditCardDetailTaxService;
import vn.softdreams.ebweb.domain.MBCreditCardDetailTax;
import vn.softdreams.ebweb.repository.MBCreditCardDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBCreditCardDetailTax.
 */
@Service
@Transactional
public class MBCreditCardDetailTaxServiceImpl implements MBCreditCardDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailTaxServiceImpl.class);

    private final MBCreditCardDetailTaxRepository mBCreditCardDetailTaxRepository;

    public MBCreditCardDetailTaxServiceImpl(MBCreditCardDetailTaxRepository mBCreditCardDetailTaxRepository) {
        this.mBCreditCardDetailTaxRepository = mBCreditCardDetailTaxRepository;
    }

    /**
     * Save a mBCreditCardDetailTax.
     *
     * @param mBCreditCardDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MBCreditCardDetailTax save(MBCreditCardDetailTax mBCreditCardDetailTax) {
        log.debug("Request to save MBCreditCardDetailTax : {}", mBCreditCardDetailTax);        return mBCreditCardDetailTaxRepository.save(mBCreditCardDetailTax);
    }

    /**
     * Get all the mBCreditCardDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBCreditCardDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all MBCreditCardDetailTaxes");
        return mBCreditCardDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one mBCreditCardDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBCreditCardDetailTax> findOne(UUID id) {
        log.debug("Request to get MBCreditCardDetailTax : {}", id);
        return mBCreditCardDetailTaxRepository.findById(id);
    }

    /**
     * Delete the mBCreditCardDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBCreditCardDetailTax : {}", id);
        mBCreditCardDetailTaxRepository.deleteById(id);
    }
}
