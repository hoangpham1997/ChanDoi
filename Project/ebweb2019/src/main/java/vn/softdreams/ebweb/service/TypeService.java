package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Type;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Type.
 */
public interface TypeService {

    /**
     * Save a type.
     *
     * @param type the entity to save
     * @return the persisted entity
     */
    Type save(Type type);

    /**
     * Get all the types.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Type> findAll(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<Type> findAll();


    /**
     * Get the "id" type.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Type> findOne(Integer id);

    /**
     * Delete the "id" type.
     *
     * @param id the id of the entity
     */
    void delete(Integer id);

    List<Type> findAllByGroupId(Integer groupId);

    List<Type> findAllActive();

}
