package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBInternalTransferService;
import vn.softdreams.ebweb.domain.MBInternalTransfer;
import vn.softdreams.ebweb.repository.MBInternalTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBInternalTransfer.
 */
@Service
@Transactional
public class MBInternalTransferServiceImpl implements MBInternalTransferService {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferServiceImpl.class);

    private final MBInternalTransferRepository mBInternalTransferRepository;

    public MBInternalTransferServiceImpl(MBInternalTransferRepository mBInternalTransferRepository) {
        this.mBInternalTransferRepository = mBInternalTransferRepository;
    }

    /**
     * Save a mBInternalTransfer.
     *
     * @param mBInternalTransfer the entity to save
     * @return the persisted entity
     */
    @Override
    public MBInternalTransfer save(MBInternalTransfer mBInternalTransfer) {
        log.debug("Request to save MBInternalTransfer : {}", mBInternalTransfer);        return mBInternalTransferRepository.save(mBInternalTransfer);
    }

    /**
     * Get all the mBInternalTransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBInternalTransfer> findAll(Pageable pageable) {
        log.debug("Request to get all MBInternalTransfers");
        return mBInternalTransferRepository.findAll(pageable);
    }


    /**
     * Get one mBInternalTransfer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBInternalTransfer> findOne(UUID id) {
        log.debug("Request to get MBInternalTransfer : {}", id);
        return mBInternalTransferRepository.findById(id);
    }

    /**
     * Delete the mBInternalTransfer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBInternalTransfer : {}", id);
        mBInternalTransferRepository.deleteById(id);
    }
}
