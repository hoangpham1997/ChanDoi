package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.SalePriceGroupService;
import vn.softdreams.ebweb.domain.SalePriceGroup;
import vn.softdreams.ebweb.repository.SalePriceGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing SalePriceGroup.
 */
@Service
@Transactional
public class SalePriceGroupServiceImpl implements SalePriceGroupService {

    private final Logger log = LoggerFactory.getLogger(SalePriceGroupServiceImpl.class);

    private final SalePriceGroupRepository salePriceGroupRepository;

    public SalePriceGroupServiceImpl(SalePriceGroupRepository salePriceGroupRepository) {
        this.salePriceGroupRepository = salePriceGroupRepository;
    }

    /**
     * Save a salePriceGroup.
     *
     * @param salePriceGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public SalePriceGroup save(SalePriceGroup salePriceGroup) {
        log.debug("Request to save SalePriceGroup : {}", salePriceGroup);        return salePriceGroupRepository.save(salePriceGroup);
    }

    /**
     * Get all the salePriceGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SalePriceGroup> findAll(Pageable pageable) {
        log.debug("Request to get all SalePriceGroups");
        return salePriceGroupRepository.findAll(pageable);
    }


    /**
     * Get one salePriceGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SalePriceGroup> findOne(UUID id) {
        log.debug("Request to get SalePriceGroup : {}", id);
        return salePriceGroupRepository.findById(id);
    }

    /**
     * Delete the salePriceGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SalePriceGroup : {}", id);
        salePriceGroupRepository.deleteById(id);
    }
}
