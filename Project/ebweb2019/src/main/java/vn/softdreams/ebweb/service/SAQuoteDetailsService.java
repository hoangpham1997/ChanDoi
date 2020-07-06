package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SAQuoteDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAQuoteDetails.
 */
public interface SAQuoteDetailsService {

    /**
     * Save a sAQuoteDetails.
     *
     * @param sAQuoteDetails the entity to save
     * @return the persisted entity
     */
    SAQuoteDetails save(SAQuoteDetails sAQuoteDetails);

    /**
     * Get all the sAQuoteDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAQuoteDetails> findAll(Pageable pageable);


    /**
     * Get the "id" sAQuoteDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAQuoteDetails> findOne(UUID id);

    /**
     * Delete the "id" sAQuoteDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
