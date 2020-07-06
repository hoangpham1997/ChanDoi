package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCReceiptDetailsService;
import vn.softdreams.ebweb.domain.MCReceiptDetails;
import vn.softdreams.ebweb.repository.MCReceiptDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCReceiptDetails.
 */
@Service
@Transactional
public class MCReceiptDetailsServiceImpl implements MCReceiptDetailsService {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailsServiceImpl.class);

    private final MCReceiptDetailsRepository mCReceiptDetailsRepository;

    public MCReceiptDetailsServiceImpl(MCReceiptDetailsRepository mCReceiptDetailsRepository) {
        this.mCReceiptDetailsRepository = mCReceiptDetailsRepository;
    }

    /**
     * Save a mCReceiptDetails.
     *
     * @param mCReceiptDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MCReceiptDetails save(MCReceiptDetails mCReceiptDetails) {
        log.debug("Request to save MCReceiptDetails : {}", mCReceiptDetails);        return mCReceiptDetailsRepository.save(mCReceiptDetails);
    }

    /**
     * Get all the mCReceiptDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCReceiptDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MCReceiptDetails");
        return mCReceiptDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mCReceiptDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCReceiptDetails> findOne(UUID id) {
        log.debug("Request to get MCReceiptDetails : {}", id);
        return mCReceiptDetailsRepository.findById(id);
    }

    /**
     * Delete the mCReceiptDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCReceiptDetails : {}", id);
        mCReceiptDetailsRepository.deleteById(id);
    }
}
