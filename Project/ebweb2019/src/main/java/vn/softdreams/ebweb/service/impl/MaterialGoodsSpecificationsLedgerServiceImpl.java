package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsSpecificationsLedgerService;
import vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger;
import vn.softdreams.ebweb.repository.MaterialGoodsSpecificationsLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialGoodsSpecificationsLedger.
 */
@Service
@Transactional
public class MaterialGoodsSpecificationsLedgerServiceImpl implements MaterialGoodsSpecificationsLedgerService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsSpecificationsLedgerServiceImpl.class);

    private final MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository;

    public MaterialGoodsSpecificationsLedgerServiceImpl(MaterialGoodsSpecificationsLedgerRepository materialGoodsSpecificationsLedgerRepository) {
        this.materialGoodsSpecificationsLedgerRepository = materialGoodsSpecificationsLedgerRepository;
    }

    /**
     * Save a materialGoodsSpecificationsLedger.
     *
     * @param materialGoodsSpecificationsLedger the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsSpecificationsLedger save(MaterialGoodsSpecificationsLedger materialGoodsSpecificationsLedger) {
        log.debug("Request to save MaterialGoodsSpecificationsLedger : {}", materialGoodsSpecificationsLedger);        return materialGoodsSpecificationsLedgerRepository.save(materialGoodsSpecificationsLedger);
    }

    /**
     * Get all the materialGoodsSpecificationsLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsSpecificationsLedger> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsSpecificationsLedgers");
        return materialGoodsSpecificationsLedgerRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsSpecificationsLedger by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsSpecificationsLedger> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsSpecificationsLedger : {}", id);
        return materialGoodsSpecificationsLedgerRepository.findById(id);
    }

    /**
     * Delete the materialGoodsSpecificationsLedger by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsSpecificationsLedger : {}", id);
        materialGoodsSpecificationsLedgerRepository.deleteById(id);
    }

    @Override
    public List<MaterialGoodsSpecificationsLedgerDTO> findByMaterialGoodsID(UUID id, UUID repositoryID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialGoodsSpecificationsLedgerDTO> materialGoodsSpecificationsLedgerDTOS = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            materialGoodsSpecificationsLedgerDTOS = materialGoodsSpecificationsLedgerRepository.findByMaterialGoodsID(currentUserLoginAndOrg.get().getOrg(), id, repositoryID);
        }
        return materialGoodsSpecificationsLedgerDTOS;
    }
}
