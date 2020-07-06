package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.FixedAssetCategoryService;
import vn.softdreams.ebweb.domain.FixedAssetCategory;
import vn.softdreams.ebweb.repository.FixedAssetCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing FixedAssetCategory.
 */
@Service
@Transactional
public class FixedAssetCategoryServiceImpl implements FixedAssetCategoryService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetCategoryServiceImpl.class);

    private final FixedAssetCategoryRepository fixedAssetCategoryRepository;

    public FixedAssetCategoryServiceImpl(FixedAssetCategoryRepository fixedAssetCategoryRepository) {
        this.fixedAssetCategoryRepository = fixedAssetCategoryRepository;
    }

    /**
     * Save a fixedAssetCategory.
     *
     * @param fixedAssetCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public FixedAssetCategory save(FixedAssetCategory fixedAssetCategory) {
        log.debug("Request to save FixedAssetCategory : {}", fixedAssetCategory);        return fixedAssetCategoryRepository.save(fixedAssetCategory);
    }

    /**
     * Get all the fixedAssetCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetCategory> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetCategories");
        return fixedAssetCategoryRepository.findAll(pageable);
    }


    /**
     * Get one fixedAssetCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetCategory> findOne(UUID id) {
        log.debug("Request to get FixedAssetCategory : {}", id);
        return fixedAssetCategoryRepository.findById(id);
    }

    /**
     * Delete the fixedAssetCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FixedAssetCategory : {}", id);
        fixedAssetCategoryRepository.deleteById(id);
    }
}
