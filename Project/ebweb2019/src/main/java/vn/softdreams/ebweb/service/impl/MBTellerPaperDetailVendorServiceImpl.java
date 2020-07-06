package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBTellerPaperDetailVendorService;
import vn.softdreams.ebweb.domain.MBTellerPaperDetailVendor;
import vn.softdreams.ebweb.repository.MBTellerPaperDetailVendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBTellerPaperDetailVendor.
 */
@Service
@Transactional
public class MBTellerPaperDetailVendorServiceImpl implements MBTellerPaperDetailVendorService {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailVendorServiceImpl.class);

    private final MBTellerPaperDetailVendorRepository mBTellerPaperDetailVendorRepository;

    public MBTellerPaperDetailVendorServiceImpl(MBTellerPaperDetailVendorRepository mBTellerPaperDetailVendorRepository) {
        this.mBTellerPaperDetailVendorRepository = mBTellerPaperDetailVendorRepository;
    }

    /**
     * Save a mBTellerPaperDetailVendor.
     *
     * @param mBTellerPaperDetailVendor the entity to save
     * @return the persisted entity
     */
    @Override
    public MBTellerPaperDetailVendor save(MBTellerPaperDetailVendor mBTellerPaperDetailVendor) {
        log.debug("Request to save MBTellerPaperDetailVendor : {}", mBTellerPaperDetailVendor);        return mBTellerPaperDetailVendorRepository.save(mBTellerPaperDetailVendor);
    }

    /**
     * Get all the mBTellerPaperDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBTellerPaperDetailVendor> findAll(Pageable pageable) {
        log.debug("Request to get all MBTellerPaperDetailVendors");
        return mBTellerPaperDetailVendorRepository.findAll(pageable);
    }


    /**
     * Get one mBTellerPaperDetailVendor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBTellerPaperDetailVendor> findOne(UUID id) {
        log.debug("Request to get MBTellerPaperDetailVendor : {}", id);
        return mBTellerPaperDetailVendorRepository.findById(id);
    }

    /**
     * Delete the mBTellerPaperDetailVendor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBTellerPaperDetailVendor : {}", id);
        mBTellerPaperDetailVendorRepository.deleteById(id);
    }
}
