package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.RSInwardOutWardDetailsService;
import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.repository.RSInwardOutWardDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing RSInwardOutWardDetails.
 */
@Service
@Transactional
public class RSInwardOutWardDetailsServiceImpl implements RSInwardOutWardDetailsService {

    private final Logger log = LoggerFactory.getLogger(RSInwardOutWardDetailsServiceImpl.class);

    private final RSInwardOutWardDetailsRepository rSInwardOutWardDetailsRepository;

    public RSInwardOutWardDetailsServiceImpl(RSInwardOutWardDetailsRepository rSInwardOutWardDetailsRepository) {
        this.rSInwardOutWardDetailsRepository = rSInwardOutWardDetailsRepository;
    }

    /**
     * Save a rSInwardOutWardDetails.
     *
     * @param rSInwardOutWardDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public RSInwardOutWardDetails save(RSInwardOutWardDetails rSInwardOutWardDetails) {
        log.debug("Request to save RSInwardOutWardDetails : {}", rSInwardOutWardDetails);        return rSInwardOutWardDetailsRepository.save(rSInwardOutWardDetails);
    }

    /**
     * Get all the rSInwardOutWardDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RSInwardOutWardDetails> findAll(Pageable pageable) {
        log.debug("Request to get all RSInwardOutWardDetails");
        return rSInwardOutWardDetailsRepository.findAll(pageable);
    }


    /**
     * Get one rSInwardOutWardDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RSInwardOutWardDetails> findOne(Long id) {
        log.debug("Request to get RSInwardOutWardDetails : {}", id);
        return rSInwardOutWardDetailsRepository.findById(id);
    }

    /**
     * Delete the rSInwardOutWardDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RSInwardOutWardDetails : {}", id);
        rSInwardOutWardDetailsRepository.deleteById(id);
    }
}
