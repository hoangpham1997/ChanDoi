package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountGroup.
 */
public interface AccountGroupService {

    /**
     * Save a accountGroup.
     *
     * @param accountGroup the entity to save
     * @return the persisted entity
     */
    AccountGroup save(AccountGroup accountGroup);

    /**
     * Get all the accountGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountGroup> findAll(Pageable pageable);


    /**
     * Get the "id" accountGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountGroup> findOne(String id);

    /**
     * Delete the "id" accountGroup.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    List<AccountGroup> findAllActive();

}
