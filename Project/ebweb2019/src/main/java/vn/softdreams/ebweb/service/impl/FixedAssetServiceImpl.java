package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.FixedAssetAccessoriesRepository;
import vn.softdreams.ebweb.repository.FixedAssetAllocationRepository;
import vn.softdreams.ebweb.repository.FixedAssetDetailsRepository;
import vn.softdreams.ebweb.service.FixedAssetService;
import vn.softdreams.ebweb.repository.FixedAssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing FixedAsset.
 */
@Service
@Transactional
public class FixedAssetServiceImpl implements FixedAssetService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetServiceImpl.class);

    private final FixedAssetRepository fixedAssetRepository;
    private final FixedAssetAccessoriesRepository fixedAssetAccessoriesRepository;
    private final FixedAssetAllocationRepository fixedAssetAllocationRepository;
    private final FixedAssetDetailsRepository fixedAssetDetailsRepository;

    public FixedAssetServiceImpl(FixedAssetRepository fixedAssetRepository,
                                 FixedAssetAllocationRepository fixedAssetAllocationRepository,
                                 FixedAssetAccessoriesRepository fixedAssetAccessoriesRepository,
                                 FixedAssetDetailsRepository fixedAssetDetailsRepository) {
        this.fixedAssetRepository = fixedAssetRepository;
        this.fixedAssetAccessoriesRepository = fixedAssetAccessoriesRepository;
        this.fixedAssetAllocationRepository = fixedAssetAllocationRepository;
        this.fixedAssetDetailsRepository = fixedAssetDetailsRepository;
    }

    /**
     * Save a fixedAsset.
     *
     * @param fixedAsset the entity to save
     * @return the persisted entity
     */
    @Override
    public FixedAsset save(FixedAsset fixedAsset) {
        log.debug("Request to save FixedAsset : {}", fixedAsset);
        FixedAsset fA = new FixedAsset();
        if(fixedAsset.getId() == null){
            fixedAsset.setId(UUID.randomUUID());
        }
        for (FixedAssetDetails details: fixedAsset.getFixedAssetDetails()){
            details.setFixedassetID(fixedAsset.getId());
        }
        for (FixedAssetAllocation details: fixedAsset.getFixedAssetAllocation()){
            details.setFixedassetID(fixedAsset.getId());
        }
        for (FixedAssetAccessories details: fixedAsset.getFixedAssetAccessories()){
            details.setFixedassetID(fixedAsset.getId());
        }
        try {
            fA = fixedAssetRepository.save(fixedAsset);
        } catch (Exception ex) {
            log.debug("Request to save FixedAsset ERR : {}", ex.getMessage());
        }
        return fA;
    }

    /**
     * Get all the fixedAssets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FixedAsset> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssets");
        return fixedAssetRepository.findAll(pageable);
    }


    /**
     * Get one fixedAsset by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAsset> findOne(UUID id) {
        log.debug("Request to get FixedAsset : {}", id);
        return fixedAssetRepository.findById(id);
    }

    /**
     * Delete the fixedAsset by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FixedAsset : {}", id);
        fixedAssetRepository.deleteById(id);
    }
}
