package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.SaleDiscountPolicyService;
import vn.softdreams.ebweb.domain.SaleDiscountPolicy;
import vn.softdreams.ebweb.repository.SaleDiscountPolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing SaleDiscountPolicy.
 */
@Service
@Transactional
public class SaleDiscountPolicyServiceImpl implements SaleDiscountPolicyService {

    private final Logger log = LoggerFactory.getLogger(SaleDiscountPolicyServiceImpl.class);

    private final SaleDiscountPolicyRepository saleDiscountPolicyRepository;

    public SaleDiscountPolicyServiceImpl(SaleDiscountPolicyRepository saleDiscountPolicyRepository) {
        this.saleDiscountPolicyRepository = saleDiscountPolicyRepository;
    }

    /**
     * Save a saleDiscountPolicy.
     *
     * @param saleDiscountPolicy the entity to save
     * @return the persisted entity
     */
    @Override
    public SaleDiscountPolicy save(SaleDiscountPolicy saleDiscountPolicy) {
        log.debug("Request to save SaleDiscountPolicy : {}", saleDiscountPolicy);        return saleDiscountPolicyRepository.save(saleDiscountPolicy);
    }

    /**
     * Get all the saleDiscountPolicies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaleDiscountPolicy> findAll(Pageable pageable) {
        log.debug("Request to get all SaleDiscountPolicies");
        return saleDiscountPolicyRepository.findAll(pageable);
    }


    /**
     * Get one saleDiscountPolicy by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SaleDiscountPolicy> findOne(UUID id) {
        log.debug("Request to get SaleDiscountPolicy : {}", id);
        return saleDiscountPolicyRepository.findById(id);
    }

    /**
     * Delete the saleDiscountPolicy by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SaleDiscountPolicy : {}", id);
        saleDiscountPolicyRepository.deleteById(id);
    }

    @Override
    public List<SaleDiscountPolicy> findByMaterialGoodsID(UUID id) {
        return saleDiscountPolicyRepository.findByMaterialGoodsID(id);
    }

    @Override
    public List<SaleDiscountPolicyDTO> findAllSaleDiscountPolicyDTO() {
        return saleDiscountPolicyRepository.findAllSaleDiscountPolicyDTO();
    }
}
