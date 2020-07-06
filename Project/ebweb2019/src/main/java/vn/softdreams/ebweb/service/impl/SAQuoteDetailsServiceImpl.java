package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.SAQuoteDetailsService;
import vn.softdreams.ebweb.domain.SAQuoteDetails;
import vn.softdreams.ebweb.repository.SAQuoteDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing SAQuoteDetails.
 */
@Service
@Transactional
public class SAQuoteDetailsServiceImpl implements SAQuoteDetailsService {

    private final Logger log = LoggerFactory.getLogger(SAQuoteDetailsServiceImpl.class);

    private final SAQuoteDetailsRepository sAQuoteDetailsRepository;

    public SAQuoteDetailsServiceImpl(SAQuoteDetailsRepository sAQuoteDetailsRepository) {
        this.sAQuoteDetailsRepository = sAQuoteDetailsRepository;
    }

    /**
     * Save a sAQuoteDetails.
     *
     * @param sAQuoteDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public SAQuoteDetails save(SAQuoteDetails sAQuoteDetails) {
        log.debug("Request to save SAQuoteDetails : {}", sAQuoteDetails);        return sAQuoteDetailsRepository.save(sAQuoteDetails);
    }

    /**
     * Get all the sAQuoteDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAQuoteDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SAQuoteDetails");
        return sAQuoteDetailsRepository.findAll(pageable);
    }


    /**
     * Get one sAQuoteDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAQuoteDetails> findOne(UUID id) {
        log.debug("Request to get SAQuoteDetails : {}", id);
        return sAQuoteDetailsRepository.findById(id);
    }

    /**
     * Delete the sAQuoteDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAQuoteDetails : {}", id);
        sAQuoteDetailsRepository.deleteById(id);
    }
}
