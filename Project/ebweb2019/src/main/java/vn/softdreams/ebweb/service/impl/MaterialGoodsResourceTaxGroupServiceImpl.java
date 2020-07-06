package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MaterialGoodsResourceTaxGroupService;
import vn.softdreams.ebweb.domain.MaterialGoodsResourceTaxGroup;
import vn.softdreams.ebweb.repository.MaterialGoodsResourceTaxGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialGoodsResourceTaxGroup.
 */
@Service
@Transactional
public class MaterialGoodsResourceTaxGroupServiceImpl implements MaterialGoodsResourceTaxGroupService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsResourceTaxGroupServiceImpl.class);

    private final MaterialGoodsResourceTaxGroupRepository materialGoodsResourceTaxGroupRepository;

    public MaterialGoodsResourceTaxGroupServiceImpl(MaterialGoodsResourceTaxGroupRepository materialGoodsResourceTaxGroupRepository) {
        this.materialGoodsResourceTaxGroupRepository = materialGoodsResourceTaxGroupRepository;
    }

    /**
     * Save a materialGoodsResourceTaxGroup.
     *
     * @param materialGoodsResourceTaxGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsResourceTaxGroup save(MaterialGoodsResourceTaxGroup materialGoodsResourceTaxGroup) {
        log.debug("Request to save MaterialGoodsResourceTaxGroup : {}", materialGoodsResourceTaxGroup);        return materialGoodsResourceTaxGroupRepository.save(materialGoodsResourceTaxGroup);
    }

    /**
     * Get all the materialGoodsResourceTaxGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsResourceTaxGroup> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsResourceTaxGroups");
        return materialGoodsResourceTaxGroupRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsResourceTaxGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsResourceTaxGroup> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsResourceTaxGroup : {}", id);
        return materialGoodsResourceTaxGroupRepository.findById(id);
    }

    /**
     * Delete the materialGoodsResourceTaxGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsResourceTaxGroup : {}", id);
        materialGoodsResourceTaxGroupRepository.deleteById(id);
    }
}
