package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.FixedAssetAccessoriesService;
import vn.softdreams.ebweb.domain.FixedAssetAccessories;
import vn.softdreams.ebweb.repository.FixedAssetAccessoriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing FixedAssetAccessories.
 */
@Service
@Transactional
public class FixedAssetAccessoriesServiceImpl implements FixedAssetAccessoriesService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAccessoriesServiceImpl.class);

    private final FixedAssetAccessoriesRepository fixedAssetAccessoriesRepository;

    public FixedAssetAccessoriesServiceImpl(FixedAssetAccessoriesRepository fixedAssetAccessoriesRepository) {
        this.fixedAssetAccessoriesRepository = fixedAssetAccessoriesRepository;
    }

    /**
     * Save a fixedAssetAccessories.
     *
     * @param fixedAssetAccessories the entity to save
     * @return the persisted entity
     */
    @Override
    public FixedAssetAccessories save(FixedAssetAccessories fixedAssetAccessories) {
        log.debug("Request to save FixedAssetAccessories : {}", fixedAssetAccessories);        return fixedAssetAccessoriesRepository.save(fixedAssetAccessories);
    }

    /**
     * Get all the fixedAssetAccessories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetAccessories> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetAccessories");
        return fixedAssetAccessoriesRepository.findAll(pageable);
    }


    /**
     * Get one fixedAssetAccessories by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetAccessories> findOne(UUID id) {
        log.debug("Request to get FixedAssetAccessories : {}", id);
        return fixedAssetAccessoriesRepository.findById(id);
    }

    /**
     * Delete the fixedAssetAccessories by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FixedAssetAccessories : {}", id);
        fixedAssetAccessoriesRepository.deleteById(id);
    }
}
