package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MaterialQuantumDetailsService;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.repository.MaterialQuantumDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialQuantumDetails.
 */
@Service
@Transactional
public class MaterialQuantumDetailsServiceImpl implements MaterialQuantumDetailsService {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumDetailsServiceImpl.class);

    private final MaterialQuantumDetailsRepository materialQuantumDetailsRepository;

    public MaterialQuantumDetailsServiceImpl(MaterialQuantumDetailsRepository materialQuantumDetailsRepository) {
        this.materialQuantumDetailsRepository = materialQuantumDetailsRepository;
    }

    /**
     * Save a materialQuantumDetails.
     *
     * @param materialQuantumDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialQuantumDetails save(MaterialQuantumDetails materialQuantumDetails) {
        log.debug("Request to save MaterialQuantumDetails : {}", materialQuantumDetails);        return materialQuantumDetailsRepository.save(materialQuantumDetails);
    }

    /**
     * Get all the materialQuantumDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialQuantumDetails> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialQuantumDetails");
        return materialQuantumDetailsRepository.findAll(pageable);
    }


    /**
     * Get one materialQuantumDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialQuantumDetails> findOne(UUID id) {
        log.debug("Request to get MaterialQuantumDetails : {}", id);
        return materialQuantumDetailsRepository.findById(id);
    }

    /**
     * Delete the materialQuantumDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialQuantumDetails : {}", id);
        materialQuantumDetailsRepository.deleteById(id);
    }

    @Override
    public List<MaterialQuantumDetailsDTO> findAllDetailsById(List<UUID> id) {
        return materialQuantumDetailsRepository.findByMaterialQuantumIDOrderByOrderPriority(id);
    }
}
