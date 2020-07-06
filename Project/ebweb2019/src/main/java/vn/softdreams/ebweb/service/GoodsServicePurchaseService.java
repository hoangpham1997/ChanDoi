package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GoodsServicePurchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.GoodsServicePurchaseContvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GoodsServicePurchase.
 */
public interface GoodsServicePurchaseService {

    /**
     * Save a goodsServicePurchase.
     *
     * @param goodsServicePurchase the entity to save
     * @return the persisted entity
     */
    GoodsServicePurchase save(GoodsServicePurchase goodsServicePurchase);

    /**
     * Get all the goodsServicePurchases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GoodsServicePurchase> findAll(Pageable pageable);


    /**
     * Get the "id" goodsServicePurchase.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GoodsServicePurchase> findOne(UUID id);

    /**
     * Delete the "id" goodsServicePurchase.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Optional<GoodsServicePurchase> getPurchaseName();

    Page<GoodsServicePurchaseContvertDTO> getPurchaseNameToCombobox();

    List<GoodsServicePurchase> findAllActive();

}
