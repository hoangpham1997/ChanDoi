package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GenCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GenCode.
 */
public interface GenCodeService {

    /**
     * Save a genCode.
     *
     * @param genCode the entity to save
     * @return the persisted entity
     */
    GenCode save(GenCode genCode);

    /**
     * Get all the genCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GenCode> findAll(Pageable pageable);


    /**
     * Get the "id" genCode.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GenCode> findOne(UUID id);

    /**
     * Delete the "id" genCode.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Hautv
     * @param typeGroupID
     * @param displayOnBook
     * @return
     */
    GenCode findWithTypeID(int typeGroupID, int displayOnBook);

	String getCodeVoucher(int typeGroupID, int displayOnBook);

    List<GenCode> getAllGenCodeForSystemOption();


}
