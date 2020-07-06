package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAllocationPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAllocationPost.
 */
public interface TIAllocationPostService {

    /**
     * Save a tIAllocationPost.
     *
     * @param tIAllocationPost the entity to save
     * @return the persisted entity
     */
    TIAllocationPost save(TIAllocationPost tIAllocationPost);

    /**
     * Get all the tIAllocationPosts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAllocationPost> findAll(Pageable pageable);


    /**
     * Get the "id" tIAllocationPost.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAllocationPost> findOne(UUID id);

    /**
     * Delete the "id" tIAllocationPost.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
