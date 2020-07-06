package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPAcceptanceService;
import vn.softdreams.ebweb.domain.CPAcceptance;
import vn.softdreams.ebweb.repository.CPAcceptanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CPAcceptance.
 */
@Service
@Transactional
public class CPAcceptanceServiceImpl implements CPAcceptanceService {

    private final Logger log = LoggerFactory.getLogger(CPAcceptanceServiceImpl.class);

    private final CPAcceptanceRepository cPAcceptanceRepository;

    public CPAcceptanceServiceImpl(CPAcceptanceRepository cPAcceptanceRepository) {
        this.cPAcceptanceRepository = cPAcceptanceRepository;
    }

    /**
     * Save a cPAcceptance.
     *
     * @param cPAcceptance the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAcceptance save(CPAcceptance cPAcceptance) {
        log.debug("Request to save CPAcceptance : {}", cPAcceptance);        return cPAcceptanceRepository.save(cPAcceptance);
    }

    /**
     * Get all the cPAcceptances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAcceptance> findAll(Pageable pageable) {
        log.debug("Request to get all CPAcceptances");
        return cPAcceptanceRepository.findAll(pageable);
    }


    /**
     * Get one cPAcceptance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAcceptance> findOne(Long id) {
        log.debug("Request to get CPAcceptance : {}", id);
        return cPAcceptanceRepository.findById(id);
    }

    /**
     * Delete the cPAcceptance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CPAcceptance : {}", id);
        cPAcceptanceRepository.deleteById(id);
    }
}
