package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MaterialGoodsSpecificationsService;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecifications;
import vn.softdreams.ebweb.repository.MaterialGoodsSpecificationsRepository;
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
 * Service Implementation for managing MaterialGoodsSpecifications.
 */
@Service
@Transactional
public class MaterialGoodsSpecificationsServiceImpl implements MaterialGoodsSpecificationsService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecificationsServiceImpl.class);

    private final MaterialGoodsSpecificationsRepository materialGoodsSpecificationsRepository;

    public MaterialGoodsSpecificationsServiceImpl(MaterialGoodsSpecificationsRepository materialGoodsSpecificationsRepository) {
        this.materialGoodsSpecificationsRepository = materialGoodsSpecificationsRepository;
    }

    /**
     * Save a materialGoodsSpecifications.
     *
     * @param materialGoodsSpecifications the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsSpecifications save(MaterialGoodsSpecifications materialGoodsSpecifications) {
        log.debug("Request to save MaterialGoodsSpecifications : {}", materialGoodsSpecifications);
        return materialGoodsSpecificationsRepository.save(materialGoodsSpecifications);
    }

    /**
     * Get all the materialGoodsSpecifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsSpecifications> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsSpecifications");
        return materialGoodsSpecificationsRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsSpecifications by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsSpecifications> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsSpecifications : {}", id);
        return materialGoodsSpecificationsRepository.findById(id);
    }

    /**
     * Delete the materialGoodsSpecifications by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsSpecifications : {}", id);
        materialGoodsSpecificationsRepository.deleteById(id);
    }

    @Override
    public List<MaterialGoodsSpecifications> findByMaterialGoodsID(UUID id) {
        return materialGoodsSpecificationsRepository.findByMaterialGoodsID(id);
    }
}
