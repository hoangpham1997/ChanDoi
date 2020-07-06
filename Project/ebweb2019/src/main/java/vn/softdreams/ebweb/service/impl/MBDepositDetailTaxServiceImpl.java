package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBDepositDetailTaxService;
import vn.softdreams.ebweb.domain.MBDepositDetailTax;
import vn.softdreams.ebweb.repository.MBDepositDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBDepositDetailTax.
 */
@Service
@Transactional
public class MBDepositDetailTaxServiceImpl implements MBDepositDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailTaxServiceImpl.class);

    private final MBDepositDetailTaxRepository mBDepositDetailTaxRepository;

    public MBDepositDetailTaxServiceImpl(MBDepositDetailTaxRepository mBDepositDetailTaxRepository) {
        this.mBDepositDetailTaxRepository = mBDepositDetailTaxRepository;
    }

    /**
     * Save a mBDepositDetailTax.
     *
     * @param mBDepositDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MBDepositDetailTax save(MBDepositDetailTax mBDepositDetailTax) {
        log.debug("Request to save MBDepositDetailTax : {}", mBDepositDetailTax);        return mBDepositDetailTaxRepository.save(mBDepositDetailTax);
    }

    /**
     * Get all the mBDepositDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBDepositDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all MBDepositDetailTaxes");
        return mBDepositDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one mBDepositDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBDepositDetailTax> findOne(UUID id) {
        log.debug("Request to get MBDepositDetailTax : {}", id);
        return mBDepositDetailTaxRepository.findById(id);
    }

    /**
     * Delete the mBDepositDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBDepositDetailTax : {}", id);
        mBDepositDetailTaxRepository.deleteById(id);
    }
}
