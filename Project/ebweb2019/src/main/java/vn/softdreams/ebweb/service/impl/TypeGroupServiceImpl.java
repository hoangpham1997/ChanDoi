package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TypeGroupService;
import vn.softdreams.ebweb.domain.TypeGroup;
import vn.softdreams.ebweb.repository.TypeGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing TypeGroup.
 */
@Service
@Transactional
public class TypeGroupServiceImpl implements TypeGroupService {

    private final Logger log = LoggerFactory.getLogger(TypeGroupServiceImpl.class);

    private final TypeGroupRepository typeGroupRepository;

    public TypeGroupServiceImpl(TypeGroupRepository typeGroupRepository) {
        this.typeGroupRepository = typeGroupRepository;
    }

    /**
     * Save a typeGroup.
     *
     * @param typeGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public TypeGroup save(TypeGroup typeGroup) {
        log.debug("Request to save TypeGroup : {}", typeGroup);
        return typeGroupRepository.save(typeGroup);
    }

    /**
     * Get all the typeGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TypeGroup> findAll() {
        log.debug("Request to get all TypeGroups");
        return typeGroupRepository.findAll();
    }


    /**
     * Get one typeGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TypeGroup> findOne(Long id) {
        log.debug("Request to get TypeGroup : {}", id);
        return typeGroupRepository.findById(id);
    }

    /**
     * Delete the typeGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeGroup : {}", id);
        typeGroupRepository.deleteById(id);
    }

    @Override
    public List<TypeGroup> getAllTypeGroupsForPopup() {
        return typeGroupRepository.findByIdNotIn(Arrays.asList(50L, 60L, 71L, 72L, 73L, 74L, 75L, 76L, 77L, 80L, 81L,
            82L, 83L, 84L, 85L, 86L, 90L, 91L, 92L, 93L, 94L, 95L));
    }
}
