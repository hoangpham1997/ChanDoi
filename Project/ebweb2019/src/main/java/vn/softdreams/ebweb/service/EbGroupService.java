package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.service.dto.OrgGroupDTO;
import vn.softdreams.ebweb.web.rest.dto.EbGroupSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EbGroupService {

    /**
     * Save a unit.
     *
     * @param ebGroup the entity to save
     * @return the persisted entity
     */
    EbGroupSaveDTO save(EbGroup ebGroup);

    /**
     * Get all the units.
     *
     * @return the list of entities
     */
    Page<EbGroup> findAll(Pageable pageable);

    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EbGroup> findOne(UUID id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Optional<EbGroup> findOneById(UUID id);

    EbGroup saveAuthorities(EbGroup ebGroup);

    List<EbGroup> findAllByOrgId(UUID orgID);

    EbGroupSaveDTO saveOrgGroup(OrgGroupDTO orgGroupDTO);
}
