package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCPaymentDetailVendorService;
import vn.softdreams.ebweb.domain.MCPaymentDetailVendor;
import vn.softdreams.ebweb.repository.MCPaymentDetailVendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCPaymentDetailVendor.
 */
@Service
@Transactional
public class MCPaymentDetailVendorServiceImpl implements MCPaymentDetailVendorService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailVendorServiceImpl.class);

    private final MCPaymentDetailVendorRepository mCPaymentDetailVendorRepository;

    public MCPaymentDetailVendorServiceImpl(MCPaymentDetailVendorRepository mCPaymentDetailVendorRepository) {
        this.mCPaymentDetailVendorRepository = mCPaymentDetailVendorRepository;
    }

    /**
     * Save a mCPaymentDetailVendor.
     *
     * @param mCPaymentDetailVendor the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPaymentDetailVendor save(MCPaymentDetailVendor mCPaymentDetailVendor) {
        log.debug("Request to save MCPaymentDetailVendor : {}", mCPaymentDetailVendor);        return mCPaymentDetailVendorRepository.save(mCPaymentDetailVendor);
    }

    /**
     * Get all the mCPaymentDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPaymentDetailVendor> findAll(Pageable pageable) {
        log.debug("Request to get all MCPaymentDetailVendors");
        return mCPaymentDetailVendorRepository.findAll(pageable);
    }


    /**
     * Get one mCPaymentDetailVendor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPaymentDetailVendor> findOne(UUID id) {
        log.debug("Request to get MCPaymentDetailVendor : {}", id);
        return mCPaymentDetailVendorRepository.findById(id);
    }

    /**
     * Delete the mCPaymentDetailVendor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPaymentDetailVendor : {}", id);
        mCPaymentDetailVendorRepository.deleteById(id);
    }
}
