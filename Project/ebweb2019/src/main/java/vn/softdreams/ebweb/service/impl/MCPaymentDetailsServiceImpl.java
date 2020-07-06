package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCPaymentDetailsService;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.repository.MCPaymentDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCPaymentDetails.
 */
@Service
@Transactional
public class MCPaymentDetailsServiceImpl implements MCPaymentDetailsService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentDetailsServiceImpl.class);

    private final MCPaymentDetailsRepository mCPaymentDetailsRepository;

    public MCPaymentDetailsServiceImpl(MCPaymentDetailsRepository mCPaymentDetailsRepository) {
        this.mCPaymentDetailsRepository = mCPaymentDetailsRepository;
    }

    /**
     * Save a mCPaymentDetails.
     *
     * @param mCPaymentDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPaymentDetails save(MCPaymentDetails mCPaymentDetails) {
        log.debug("Request to save MCPaymentDetails : {}", mCPaymentDetails);        return mCPaymentDetailsRepository.save(mCPaymentDetails);
    }

    /**
     * Get all the mCPaymentDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPaymentDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MCPaymentDetails");
        return mCPaymentDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mCPaymentDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPaymentDetails> findOne(UUID id) {
        log.debug("Request to get MCPaymentDetails : {}", id);
        return mCPaymentDetailsRepository.findById(id);
    }

    /**
     * Delete the mCPaymentDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPaymentDetails : {}", id);
        mCPaymentDetailsRepository.deleteById(id);
    }
}
