package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.ToolledgerService;
import vn.softdreams.ebweb.domain.Toolledger;
import vn.softdreams.ebweb.repository.ToolledgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Toolledger.
 */
@Service
@Transactional
public class ToolledgerServiceImpl implements ToolledgerService {

    private final Logger log = LoggerFactory.getLogger(ToolledgerServiceImpl.class);

    private final ToolledgerRepository toolledgerRepository;

    public ToolledgerServiceImpl(ToolledgerRepository toolledgerRepository) {
        this.toolledgerRepository = toolledgerRepository;
    }

    /**
     * Save a toolledger.
     *
     * @param toolledger the entity to save
     * @return the persisted entity
     */
    @Override
    public Toolledger save(Toolledger toolledger) {
        log.debug("Request to save Toolledger : {}", toolledger);        return toolledgerRepository.save(toolledger);
    }

    /**
     * Get all the toolledgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Toolledger> findAll(Pageable pageable) {
        log.debug("Request to get all Toolledgers");
        return toolledgerRepository.findAll(pageable);
    }


    /**
     * Get one toolledger by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Toolledger> findOne(UUID id) {
        log.debug("Request to get Toolledger : {}", id);
        return toolledgerRepository.findById(id);
    }

    /**
     * Delete the toolledger by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Toolledger : {}", id);
        toolledgerRepository.deleteById(id);
    }
}
