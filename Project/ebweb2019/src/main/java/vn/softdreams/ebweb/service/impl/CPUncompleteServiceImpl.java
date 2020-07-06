package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.CPUncompleteService;
import vn.softdreams.ebweb.domain.CPUncomplete;
import vn.softdreams.ebweb.repository.CPUncompleteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CPUncomplete.
 */
@Service
@Transactional
public class CPUncompleteServiceImpl implements CPUncompleteService {

    private final Logger log = LoggerFactory.getLogger(CPUncompleteServiceImpl.class);

    private final CPUncompleteRepository cPUncompleteRepository;

    public CPUncompleteServiceImpl(CPUncompleteRepository cPUncompleteRepository) {
        this.cPUncompleteRepository = cPUncompleteRepository;
    }

    /**
     * Save a cPUncomplete.
     *
     * @param cPUncomplete the entity to save
     * @return the persisted entity
     */
    @Override
    public CPUncomplete save(CPUncomplete cPUncomplete) {
        log.debug("Request to save CPUncomplete : {}", cPUncomplete);        return cPUncompleteRepository.save(cPUncomplete);
    }

    /**
     * Get all the cPUncompletes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPUncomplete> findAll(Pageable pageable) {
        log.debug("Request to get all CPUncompletes");
        return cPUncompleteRepository.findAll(pageable);
    }


    /**
     * Get one cPUncomplete by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPUncomplete> findOne(Long id) {
        log.debug("Request to get CPUncomplete : {}", id);
        return cPUncompleteRepository.findById(id);
    }

    /**
     * Delete the cPUncomplete by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CPUncomplete : {}", id);
        cPUncompleteRepository.deleteById(id);
    }
}
