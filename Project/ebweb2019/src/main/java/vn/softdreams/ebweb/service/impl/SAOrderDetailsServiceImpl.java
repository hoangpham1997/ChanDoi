package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.service.SAOrderDetailsService;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.repository.SAOrderDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing SAOrderDetails.
 */
@Service
@Transactional
public class SAOrderDetailsServiceImpl implements SAOrderDetailsService {

    private final Logger log = LoggerFactory.getLogger(SAOrderDetailsServiceImpl.class);

    private final SAOrderDetailsRepository sAOrderDetailsRepository;

    public SAOrderDetailsServiceImpl(SAOrderDetailsRepository sAOrderDetailsRepository) {
        this.sAOrderDetailsRepository = sAOrderDetailsRepository;
    }

    /**
     * Save a sAOrderDetails.
     *
     * @param sAOrderDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public SAOrderDetails save(SAOrderDetails sAOrderDetails) {
        log.debug("Request to save SAOrderDetails : {}", sAOrderDetails);        return sAOrderDetailsRepository.save(sAOrderDetails);
    }

    /**
     * Get all the sAOrderDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAOrderDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SAOrderDetails");
        return sAOrderDetailsRepository.findAll(pageable);
    }


    /**
     * Get one sAOrderDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAOrderDetails> findOne(UUID id) {
        log.debug("Request to get SAOrderDetails : {}", id);
        return sAOrderDetailsRepository.findById(id);
    }

    /**
     * Delete the sAOrderDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAOrderDetails : {}", id);
        sAOrderDetailsRepository.deleteById(id);
    }

    @Override
    public List<SAOrderDetails> findAllDetailsById(List<UUID> id) {
        return sAOrderDetailsRepository.findBySaOrderIDOrderByOrderPriority(id);
    }
}
