package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SAOrderDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SaReturnDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAOrderDetails.
 */
public interface SAOrderDetailsService {

    /**
     * Save a sAOrderDetails.
     *
     * @param sAOrderDetails the entity to save
     * @return the persisted entity
     */
    SAOrderDetails save(SAOrderDetails sAOrderDetails);

    /**
     * Get all the sAOrderDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAOrderDetails> findAll(Pageable pageable);


    /**
     * Get the "id" sAOrderDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAOrderDetails> findOne(UUID id);

    /**
     * Delete the "id" sAOrderDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<SAOrderDetails> findAllDetailsById(List<UUID> id);
}
