package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TypeGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing TypeGroup.
 */
public interface TypeGroupService {

    /**
     * Save a typeGroup.
     *
     * @param typeGroup the entity to save
     * @return the persisted entity
     */
    TypeGroup save(TypeGroup typeGroup);

    /**
     * Get all the typeGroups.
     *
     * @return the list of entities
     */
    List<TypeGroup> findAll();


    /**
     * Get the "id" typeGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TypeGroup> findOne(Long id);

    /**
     * Delete the "id" typeGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * @Author hieugie
     *
     * lấy dữ liệu cho combo box trong popup tham chiếu
     *
     * @return
     */
    List<TypeGroup> getAllTypeGroupsForPopup();
}
