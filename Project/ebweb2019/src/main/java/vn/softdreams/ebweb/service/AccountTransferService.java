package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountTransfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountTransfer.
 */
public interface AccountTransferService {

    /**
     * Save a accountTransfer.
     *
     * @param accountTransfer the entity to save
     * @return the persisted entity
     */
    AccountTransfer save(AccountTransfer accountTransfer);

    /**
     * Get all the accountTransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountTransfer> findAll(Pageable pageable);


    /**
     * Get the "id" accountTransfer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountTransfer> findOne(UUID id);

    /**
     * Delete the "id" accountTransfer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<AccountTransfer> findAllAccountTransfers();

}
