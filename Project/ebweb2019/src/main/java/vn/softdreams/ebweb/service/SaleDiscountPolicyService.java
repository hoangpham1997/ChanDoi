package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SaleDiscountPolicy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SaleDiscountPolicy.
 */
public interface SaleDiscountPolicyService {

    /**
     * Save a saleDiscountPolicy.
     *
     * @param saleDiscountPolicy the entity to save
     * @return the persisted entity
     */
    SaleDiscountPolicy save(SaleDiscountPolicy saleDiscountPolicy);

    /**
     * Get all the saleDiscountPolicies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaleDiscountPolicy> findAll(Pageable pageable);


    /**
     * Get the "id" saleDiscountPolicy.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SaleDiscountPolicy> findOne(UUID id);

    /**
     * Delete the "id" saleDiscountPolicy.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @Author hieugie
     *
     * @param id
     * @return
     */
    List<SaleDiscountPolicy> findByMaterialGoodsID(UUID id);

    /**
     * @Author Hautv
     * @return
     */
    List<SaleDiscountPolicyDTO> findAllSaleDiscountPolicyDTO();
}
