package vn.softdreams.ebweb.service;

import io.undertow.security.idm.Account;
import vn.softdreams.ebweb.domain.AccountDefault;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountDefault.
 */
public interface AccountDefaultService {

    /**
     * Save a accountDefault.
     *
     * @param accountDefault the entity to save
     * @return the persisted entity
     */
    AccountDefault save(AccountDefault accountDefault);

    /**
     * Get all the accountDefaults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountDefault> findAll(Pageable pageable);

    /**
     * Get all the accountDefaults.
     * add by namnh
     *
     * @return the list of entities
     */
    Page<AccountDefault> findAll();


    /**
     * Get the "id" accountDefault.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountDefault> findOne(UUID id);

    /**
     * Delete the "id" accountDefault.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<AccountDefaultDTO> findAllActive();

    List<AccountDefault> findByTypeID(Integer typeID);

    void saveAll(List<AccountDefault> accountDefaults);



}
