package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPOrderDetail.
 */
public interface PporderdetailService {

    /**
     * Save a pporderdetail.
     *
     * @param pporderdetail the entity to save
     * @return the persisted entity
     */
    PPOrderDetail save(PPOrderDetail pporderdetail);

    /**
     * Get all the pporderdetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPOrderDetail> findAll(Pageable pageable);

    List<PPOrderDetail> findAll();
    /**
     * Get the "id" pporderdetail.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPOrderDetail> findOne(UUID id);

    /**
     * Delete the "id" pporderdetail.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
