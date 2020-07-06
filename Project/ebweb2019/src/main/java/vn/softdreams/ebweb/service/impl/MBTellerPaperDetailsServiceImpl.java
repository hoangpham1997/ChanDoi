package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBTellerPaperDetailsService;
import vn.softdreams.ebweb.domain.MBTellerPaperDetails;
import vn.softdreams.ebweb.repository.MBTellerPaperDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBTellerPaperDetails.
 */
@Service
@Transactional
public class MBTellerPaperDetailsServiceImpl implements MBTellerPaperDetailsService {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailsServiceImpl.class);

    private final MBTellerPaperDetailsRepository mBTellerPaperDetailsRepository;

    public MBTellerPaperDetailsServiceImpl(MBTellerPaperDetailsRepository mBTellerPaperDetailsRepository) {
        this.mBTellerPaperDetailsRepository = mBTellerPaperDetailsRepository;
    }

    /**
     * Save a mBTellerPaperDetails.
     *
     * @param mBTellerPaperDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MBTellerPaperDetails save(MBTellerPaperDetails mBTellerPaperDetails) {
        log.debug("Request to save MBTellerPaperDetails : {}", mBTellerPaperDetails);        return mBTellerPaperDetailsRepository.save(mBTellerPaperDetails);
    }

    /**
     * Get all the mBTellerPaperDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBTellerPaperDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MBTellerPaperDetails");
        return mBTellerPaperDetailsRepository.findAll(pageable);
    }


    /**
     * Get one mBTellerPaperDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBTellerPaperDetails> findOne(UUID id) {
        log.debug("Request to get MBTellerPaperDetails : {}", id);
        return mBTellerPaperDetailsRepository.findById(id);
    }

    /**
     * Delete the mBTellerPaperDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBTellerPaperDetails : {}", id);
        mBTellerPaperDetailsRepository.deleteById(id);
    }
}
