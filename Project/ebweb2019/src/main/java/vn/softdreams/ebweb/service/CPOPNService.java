package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPOPN;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPOPN.
 */
public interface CPOPNService {

    /**
     * Save a cPOPN.
     *
     * @param cPOPN the entity to save
     * @return the persisted entity
     */
    CPOPN save(CPOPN cPOPN);

    /**
     * Get all the cPOPNS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPOPN> findAll(Pageable pageable);


    /**
     * Get the "id" cPOPN.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPOPN> findOne(UUID id);

    /**
     * Delete the "id" cPOPN.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<CPOPNSDTO> findAllActive(Pageable pageable);

    void saveAll(List<CPOPN> cpopns);


}
