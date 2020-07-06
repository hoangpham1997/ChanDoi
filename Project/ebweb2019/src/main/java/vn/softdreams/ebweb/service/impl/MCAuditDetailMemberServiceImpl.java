package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCAuditDetailMemberService;
import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import vn.softdreams.ebweb.repository.MCAuditDetailMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCAuditDetailMember.
 */
@Service
@Transactional
public class MCAuditDetailMemberServiceImpl implements MCAuditDetailMemberService {

    private final Logger log = LoggerFactory.getLogger(MCAuditDetailMemberServiceImpl.class);

    private final MCAuditDetailMemberRepository mCAuditDetailMemberRepository;

    public MCAuditDetailMemberServiceImpl(MCAuditDetailMemberRepository mCAuditDetailMemberRepository) {
        this.mCAuditDetailMemberRepository = mCAuditDetailMemberRepository;
    }

    /**
     * Save a mCAuditDetailMember.
     *
     * @param mCAuditDetailMember the entity to save
     * @return the persisted entity
     */
    @Override
    public MCAuditDetailMember save(MCAuditDetailMember mCAuditDetailMember) {
        log.debug("Request to save MCAuditDetailMember : {}", mCAuditDetailMember);        return mCAuditDetailMemberRepository.save(mCAuditDetailMember);
    }

    /**
     * Get all the mCAuditDetailMembers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCAuditDetailMember> findAll(Pageable pageable) {
        log.debug("Request to get all MCAuditDetailMembers");
        return mCAuditDetailMemberRepository.findAll(pageable);
    }


    /**
     * Get one mCAuditDetailMember by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCAuditDetailMember> findOne(UUID id) {
        log.debug("Request to get MCAuditDetailMember : {}", id);
        return mCAuditDetailMemberRepository.findById(id);
    }

    /**
     * Delete the mCAuditDetailMember by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCAuditDetailMember : {}", id);
        mCAuditDetailMemberRepository.deleteById(id);
    }
}
