package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBCreditCardDetailVendorService;
import vn.softdreams.ebweb.domain.MBCreditCardDetailVendor;
import vn.softdreams.ebweb.repository.MBCreditCardDetailVendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBCreditCardDetailVendor.
 */
@Service
@Transactional
public class MBCreditCardDetailVendorServiceImpl implements MBCreditCardDetailVendorService {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailVendorServiceImpl.class);

    private final MBCreditCardDetailVendorRepository mBCreditCardDetailVendorRepository;

    public MBCreditCardDetailVendorServiceImpl(MBCreditCardDetailVendorRepository mBCreditCardDetailVendorRepository) {
        this.mBCreditCardDetailVendorRepository = mBCreditCardDetailVendorRepository;
    }

    /**
     * Save a mBCreditCardDetailVendor.
     *
     * @param mBCreditCardDetailVendor the entity to save
     * @return the persisted entity
     */
    @Override
    public MBCreditCardDetailVendor save(MBCreditCardDetailVendor mBCreditCardDetailVendor) {
        log.debug("Request to save MBCreditCardDetailVendor : {}", mBCreditCardDetailVendor);        return mBCreditCardDetailVendorRepository.save(mBCreditCardDetailVendor);
    }

    /**
     * Get all the mBCreditCardDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBCreditCardDetailVendor> findAll(Pageable pageable) {
        log.debug("Request to get all MBCreditCardDetailVendors");
        return mBCreditCardDetailVendorRepository.findAll(pageable);
    }


    /**
     * Get one mBCreditCardDetailVendor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBCreditCardDetailVendor> findOne(UUID id) {
        log.debug("Request to get MBCreditCardDetailVendor : {}", id);
        return mBCreditCardDetailVendorRepository.findById(id);
    }

    /**
     * Delete the mBCreditCardDetailVendor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBCreditCardDetailVendor : {}", id);
        mBCreditCardDetailVendorRepository.deleteById(id);
    }
}
