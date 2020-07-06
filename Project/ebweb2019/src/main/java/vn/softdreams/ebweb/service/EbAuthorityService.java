package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbAuthority;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.web.rest.dto.AuthorityTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.EbGroupSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EbAuthorityService {

    /**
     * Save a unit.
     *
     * @param ebAuthority the entity to save
     * @return the persisted entity
     */
    EbAuthority save(EbAuthority ebAuthority);

    /**
     * Get all the units.
     *
     * @return the list of entities
     */
    Page<EbAuthority> findAll(Pageable pageable);

    List<EbAuthority> findAll();

    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EbAuthority> findOne(UUID id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Optional<EbAuthority> findOneById(UUID id);

    List<AuthorityTreeDTO> findAllAuthorityTree();
}
