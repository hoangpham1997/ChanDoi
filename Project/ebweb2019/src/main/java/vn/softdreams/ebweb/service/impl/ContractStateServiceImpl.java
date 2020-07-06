package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.ContractStateService;
import vn.softdreams.ebweb.domain.ContractState;
import vn.softdreams.ebweb.repository.ContractStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing ContractState.
 */
@Service
@Transactional
public class ContractStateServiceImpl implements ContractStateService {

    private final Logger log = LoggerFactory.getLogger(ContractStateServiceImpl.class);

    private final ContractStateRepository contractStateRepository;

    public ContractStateServiceImpl(ContractStateRepository contractStateRepository) {
        this.contractStateRepository = contractStateRepository;
    }

    /**
     * Save a contractState.
     *
     * @param contractState the entity to save
     * @return the persisted entity
     */
    @Override
    public ContractState save(ContractState contractState) {
        log.debug("Request to save ContractState : {}", contractState);        return contractStateRepository.save(contractState);
    }

    /**
     * Get all the contractStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContractState> findAll(Pageable pageable) {
        log.debug("Request to get all ContractStates");
        return contractStateRepository.findAll(pageable);
    }


    /**
     * Get one contractState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContractState> findOne(Long id) {
        log.debug("Request to get ContractState : {}", id);
        return contractStateRepository.findById(id);
    }

    /**
     * Delete the contractState by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContractState : {}", id);
        contractStateRepository.deleteById(id);
    }
}
