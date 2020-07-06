package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TITransferDetailsService;
import vn.softdreams.ebweb.domain.TITransferDetails;
import vn.softdreams.ebweb.repository.TITransferDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TITransferDetails.
 */
@Service
@Transactional
public class TITransferDetailsServiceImpl implements TITransferDetailsService {

    private final Logger log = LoggerFactory.getLogger(TITransferDetailsServiceImpl.class);

    private final TITransferDetailsRepository tITransferDetailsRepository;

    public TITransferDetailsServiceImpl(TITransferDetailsRepository tITransferDetailsRepository) {
        this.tITransferDetailsRepository = tITransferDetailsRepository;
    }

    /**
     * Save a tITransferDetails.
     *
     * @param tITransferDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TITransferDetails save(TITransferDetails tITransferDetails) {
        log.debug("Request to save TITransferDetails : {}", tITransferDetails);        return tITransferDetailsRepository.save(tITransferDetails);
    }

    /**
     * Get all the tITransferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TITransferDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TITransferDetails");
        return tITransferDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tITransferDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TITransferDetails> findOne(UUID id) {
        log.debug("Request to get TITransferDetails : {}", id);
        return tITransferDetailsRepository.findById(id);
    }

    /**
     * Delete the tITransferDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TITransferDetails : {}", id);
        tITransferDetailsRepository.deleteById(id);
    }
}
