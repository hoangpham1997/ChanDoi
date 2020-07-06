package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.AccountingObjectGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccountingObjectGroup.
 */
public interface AccountingObjectGroupService {

    /**
     * Save a accountingObjectGroup.
     *
     * @param accountingObjectGroup the entity to save
     * @return the persisted entity
     */
    AccountingObjectGroup save(AccountingObjectGroup accountingObjectGroup);

    /**
     * Get all the accountingObjectGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountingObjectGroup> findAll(Pageable pageable);


    /**
     * Get the "id" accountingObjectGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountingObjectGroup> findOne(UUID id);

    /**
     * Delete the "id" accountingObjectGroup.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Get all the accountingObjectGroups.
     *
     * @param pageable ,accountingobjectgroupcode
     * @return the list of entities
     */
    Page<AccountingObjectGroup> findAll(Pageable pageable, String accountingobjectgroupcode);


    List<AccountingObjectGroup> getAllAccountingObjectGroupSimilarBranch(Boolean similarBranch, UUID companyID);

    List<AccountingObjectGroup> findAllAccountingObjectGroup(Boolean similarBranch, UUID companyID);

    List<AccountingObjectGroup> getCbxAccountingObjectGroup ( UUID id);

    List<AccountingObjectGroup> loadAllAccountingObjectGroup();
}
