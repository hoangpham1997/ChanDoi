package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBDepositDetailsService;
import vn.softdreams.ebweb.domain.MBDepositDetails;
import vn.softdreams.ebweb.repository.MBDepositDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBDepositDetails.
 */
@Service
@Transactional
public class MBDepositDetailsServiceImpl implements MBDepositDetailsService {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailsServiceImpl.class);

    private final MBDepositDetailsRepository mBDepositDetailsRepository;

    public MBDepositDetailsServiceImpl(MBDepositDetailsRepository mBDepositDetailsRepository) {
        this.mBDepositDetailsRepository = mBDepositDetailsRepository;
    }

    /**
     * Save a mBDepositDetails.
     *
     * @param mBDepositDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MBDepositDetails save(MBDepositDetails mBDepositDetails) {
        log.debug("Request to save MBDepositDetails : {}", mBDepositDetails);        return mBDepositDetailsRepository.save(mBDepositDetails);
    }

    /**
     * Get all the mBDepositDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBDepositDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MBDepositDetails");
        return mBDepositDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mBDepositDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBDepositDetails> findOne(UUID id) {
        log.debug("Request to get MBDepositDetails : {}", id);
        return mBDepositDetailsRepository.findById(id);
    }

    /**
     * Delete the mBDepositDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBDepositDetails : {}", id);
        mBDepositDetailsRepository.deleteById(id);
    }
}
