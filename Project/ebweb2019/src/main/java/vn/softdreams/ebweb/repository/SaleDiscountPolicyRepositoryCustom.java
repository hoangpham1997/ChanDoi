package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;

import java.util.List;


/**
 * Spring Data  repository for the SaleDiscountPolicy entity.
 */
@SuppressWarnings("unused")
public interface SaleDiscountPolicyRepositoryCustom {
    List<SaleDiscountPolicyDTO> findAllSaleDiscountPolicyDTO();
}
