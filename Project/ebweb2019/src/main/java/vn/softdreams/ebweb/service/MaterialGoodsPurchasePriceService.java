package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsPurchasePrice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsPurchasePrice.
 */
public interface MaterialGoodsPurchasePriceService {

    /**
     * Save a materialGoodsPurchasePrice.
     *
     * @param materialGoodsPurchasePrice the entity to save
     * @return the persisted entity
     */
    MaterialGoodsPurchasePrice save(MaterialGoodsPurchasePrice materialGoodsPurchasePrice);

    /**
     * Get all the materialGoodsPurchasePrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsPurchasePrice> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsPurchasePrice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsPurchasePrice> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsPurchasePrice.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialGoodsPurchasePrice> findByMaterialGoodsID(UUID id);
}
