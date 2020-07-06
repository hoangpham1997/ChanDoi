package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIAllocationPostService;
import vn.softdreams.ebweb.domain.TIAllocationPost;
import vn.softdreams.ebweb.repository.TIAllocationPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAllocationPost.
 */
@Service
@Transactional
public class TIAllocationPostServiceImpl implements TIAllocationPostService {

    private final Logger log = LoggerFactory.getLogger(TIAllocationPostServiceImpl.class);

    private final TIAllocationPostRepository tIAllocationPostRepository;

    public TIAllocationPostServiceImpl(TIAllocationPostRepository tIAllocationPostRepository) {
        this.tIAllocationPostRepository = tIAllocationPostRepository;
    }

    /**
     * Save a tIAllocationPost.
     *
     * @param tIAllocationPost the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAllocationPost save(TIAllocationPost tIAllocationPost) {
        log.debug("Request to save TIAllocationPost : {}", tIAllocationPost);        return tIAllocationPostRepository.save(tIAllocationPost);
    }

    /**
     * Get all the tIAllocationPosts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAllocationPost> findAll(Pageable pageable) {
        log.debug("Request to get all TIAllocationPosts");
        return tIAllocationPostRepository.findAll(pageable);
    }


    /**
     * Get one tIAllocationPost by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAllocationPost> findOne(UUID id) {
        log.debug("Request to get TIAllocationPost : {}", id);
        return tIAllocationPostRepository.findById(id);
    }

    /**
     * Delete the tIAllocationPost by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAllocationPost : {}", id);
        tIAllocationPostRepository.deleteById(id);
    }
}
