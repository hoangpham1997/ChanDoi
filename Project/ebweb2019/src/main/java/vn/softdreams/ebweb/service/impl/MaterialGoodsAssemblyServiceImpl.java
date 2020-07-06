package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsAssemblyService;
import vn.softdreams.ebweb.domain.MaterialGoodsAssembly;
import vn.softdreams.ebweb.repository.MaterialGoodsAssemblyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialGoodsAssembly.
 */
@Service
@Transactional
public class MaterialGoodsAssemblyServiceImpl implements MaterialGoodsAssemblyService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsAssemblyServiceImpl.class);

    private final MaterialGoodsAssemblyRepository materialGoodsAssemblyRepository;

    public MaterialGoodsAssemblyServiceImpl(MaterialGoodsAssemblyRepository materialGoodsAssemblyRepository) {
        this.materialGoodsAssemblyRepository = materialGoodsAssemblyRepository;
    }

    /**
     * Save a materialGoodsAssembly.
     *
     * @param materialGoodsAssembly the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsAssembly save(MaterialGoodsAssembly materialGoodsAssembly) {
        log.debug("Request to save MaterialGoodsAssembly : {}", materialGoodsAssembly);        return materialGoodsAssemblyRepository.save(materialGoodsAssembly);
    }

    /**
     * Get all the materialGoodsAssemblies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsAssembly> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsAssemblies");
        return materialGoodsAssemblyRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsAssembly by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsAssembly> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsAssembly : {}", id);
        return materialGoodsAssemblyRepository.findById(id);
    }

    /**
     * Delete the materialGoodsAssembly by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsAssembly : {}", id);
        materialGoodsAssemblyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialGoodsAssembly> findByMaterialGoodsID(UUID id) {
        return materialGoodsAssemblyRepository.findByMaterialGoodsID(id);
    }
}
