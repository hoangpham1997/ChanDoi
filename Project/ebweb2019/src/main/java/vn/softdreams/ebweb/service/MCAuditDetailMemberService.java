package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCAuditDetailMember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCAuditDetailMember.
 */
public interface MCAuditDetailMemberService {

    /**
     * Save a mCAuditDetailMember.
     *
     * @param mCAuditDetailMember the entity to save
     * @return the persisted entity
     */
    MCAuditDetailMember save(MCAuditDetailMember mCAuditDetailMember);

    /**
     * Get all the mCAuditDetailMembers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCAuditDetailMember> findAll(Pageable pageable);


    /**
     * Get the "id" mCAuditDetailMember.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCAuditDetailMember> findOne(UUID id);

    /**
     * Delete the "id" mCAuditDetailMember.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
