package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBCreditCardDetailsService;
import vn.softdreams.ebweb.domain.MBCreditCardDetails;
import vn.softdreams.ebweb.repository.MBCreditCardDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBCreditCardDetails.
 */
@Service
@Transactional
public class MBCreditCardDetailsServiceImpl implements MBCreditCardDetailsService {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardDetailsServiceImpl.class);

    private final MBCreditCardDetailsRepository mBCreditCardDetailsRepository;

    public MBCreditCardDetailsServiceImpl(MBCreditCardDetailsRepository mBCreditCardDetailsRepository) {
        this.mBCreditCardDetailsRepository = mBCreditCardDetailsRepository;
    }

    /**
     * Save a mBCreditCardDetails.
     *
     * @param mBCreditCardDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MBCreditCardDetails save(MBCreditCardDetails mBCreditCardDetails) {
        log.debug("Request to save MBCreditCardDetails : {}", mBCreditCardDetails);        return mBCreditCardDetailsRepository.save(mBCreditCardDetails);
    }

    /**
     * Get all the mBCreditCardDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBCreditCardDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MBCreditCardDetails");
        return mBCreditCardDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mBCreditCardDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBCreditCardDetails> findOne(UUID id) {
        log.debug("Request to get MBCreditCardDetails : {}", id);
        return mBCreditCardDetailsRepository.findById(id);
    }

    /**
     * Delete the mBCreditCardDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBCreditCardDetails : {}", id);
        mBCreditCardDetailsRepository.deleteById(id);
    }
}
