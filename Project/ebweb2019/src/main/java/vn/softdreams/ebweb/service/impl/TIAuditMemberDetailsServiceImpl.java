package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAuditMemberDetailsService;
import vn.softdreams.ebweb.domain.TIAuditMemberDetails;
import vn.softdreams.ebweb.repository.TIAuditMemberDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAuditMemberDetails.
 */
@Service
@Transactional
public class TIAuditMemberDetailsServiceImpl implements TIAuditMemberDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIAuditMemberDetailsServiceImpl.class);

    private final TIAuditMemberDetailsRepository tIAuditMemberDetailsRepository;

    public TIAuditMemberDetailsServiceImpl(TIAuditMemberDetailsRepository tIAuditMemberDetailsRepository) {
        this.tIAuditMemberDetailsRepository = tIAuditMemberDetailsRepository;
    }

    /**
     * Save a tIAuditMemberDetails.
     *
     * @param tIAuditMemberDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAuditMemberDetails save(TIAuditMemberDetails tIAuditMemberDetails) {
        log.debug("Request to save TIAuditMemberDetails : {}", tIAuditMemberDetails);        return tIAuditMemberDetailsRepository.save(tIAuditMemberDetails);
    }

    /**
     * Get all the tIAuditMemberDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAuditMemberDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIAuditMemberDetails");
        return tIAuditMemberDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIAuditMemberDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAuditMemberDetails> findOne(UUID id) {
        log.debug("Request to get TIAuditMemberDetails : {}", id);
        return tIAuditMemberDetailsRepository.findById(id);
    }

    /**
     * Delete the tIAuditMemberDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAuditMemberDetails : {}", id);
        tIAuditMemberDetailsRepository.deleteById(id);
    }
}
