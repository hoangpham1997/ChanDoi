package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.GOtherVoucherDetailsService;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GOtherVoucherDetails.
 */
@Service
@Transactional
public class GOtherVoucherDetailsServiceImpl implements GOtherVoucherDetailsService {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailsServiceImpl.class);

    private final GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository;

    public GOtherVoucherDetailsServiceImpl(GOtherVoucherDetailsRepository gOtherVoucherDetailsRepository) {
        this.gOtherVoucherDetailsRepository = gOtherVoucherDetailsRepository;
    }

    /**
     * Save a gOtherVoucherDetails.
     *
     * @param gOtherVoucherDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public GOtherVoucherDetails save(GOtherVoucherDetails gOtherVoucherDetails) {
        log.debug("Request to save GOtherVoucherDetails : {}", gOtherVoucherDetails);        return gOtherVoucherDetailsRepository.save(gOtherVoucherDetails);
    }

    /**
     * Get all the gOtherVoucherDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GOtherVoucherDetails> findAll(Pageable pageable) {
        log.debug("Request to get all GOtherVoucherDetails");
        return gOtherVoucherDetailsRepository.findAll(pageable);
    }


    /**
     * Get one gOtherVoucherDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GOtherVoucherDetails> findOne(UUID id) {
        log.debug("Request to get GOtherVoucherDetails : {}", id);
        return gOtherVoucherDetailsRepository.findById(id);
    }

    /**
     * Delete the gOtherVoucherDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GOtherVoucherDetails : {}", id);
        gOtherVoucherDetailsRepository.deleteById(id);
    }
}
