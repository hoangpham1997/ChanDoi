package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.TypeService;
import vn.softdreams.ebweb.domain.Type;
import vn.softdreams.ebweb.repository.TypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Type.
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);

    private final TypeRepository typeRepository;

    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Save a type.
     *
     * @param type the entity to save
     * @return the persisted entity
     */
    @Override
    public Type save(Type type) {
        log.debug("Request to save Type : {}", type);        return typeRepository.save(type);
    }

    /**
     * Get all the types.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Type> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typeRepository.findAll(pageable);
    }

    @Override
    public Page<Type> findAll() {
        return new PageImpl<Type>(typeRepository.findAll());
    }


    /**
     * Get one type by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Type> findOne(Integer id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }

    public List<Type> findAllActive() {
        return typeRepository.findAllByIsActive();
    }


    @Override
    public List<Type> findAllByGroupId(Integer groupId) {
        return typeRepository.findAllByTypeGroupID(groupId);
    }
}
