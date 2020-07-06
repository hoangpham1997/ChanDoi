package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBInternalTransferDetailsService;
import vn.softdreams.ebweb.domain.MBInternalTransferDetails;
import vn.softdreams.ebweb.repository.MBInternalTransferDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBInternalTransferDetails.
 */
@Service
@Transactional
public class MBInternalTransferDetailsServiceImpl implements MBInternalTransferDetailsService {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferDetailsServiceImpl.class);

    private final MBInternalTransferDetailsRepository mBInternalTransferDetailsRepository;

    public MBInternalTransferDetailsServiceImpl(MBInternalTransferDetailsRepository mBInternalTransferDetailsRepository) {
        this.mBInternalTransferDetailsRepository = mBInternalTransferDetailsRepository;
    }

    /**
     * Save a mBInternalTransferDetails.
     *
     * @param mBInternalTransferDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MBInternalTransferDetails save(MBInternalTransferDetails mBInternalTransferDetails) {
        log.debug("Request to save MBInternalTransferDetails : {}", mBInternalTransferDetails);        return mBInternalTransferDetailsRepository.save(mBInternalTransferDetails);
    }

    /**
     * Get all the mBInternalTransferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBInternalTransferDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MBInternalTransferDetails");
        return mBInternalTransferDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mBInternalTransferDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBInternalTransferDetails> findOne(UUID id) {
        log.debug("Request to get MBInternalTransferDetails : {}", id);
        return mBInternalTransferDetailsRepository.findById(id);
    }

    /**
     * Delete the mBInternalTransferDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBInternalTransferDetails : {}", id);
        mBInternalTransferDetailsRepository.deleteById(id);
    }
}
