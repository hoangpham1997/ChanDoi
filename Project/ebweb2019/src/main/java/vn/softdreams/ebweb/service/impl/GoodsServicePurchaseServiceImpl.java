package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GoodsServicePurchaseService;
import vn.softdreams.ebweb.domain.GoodsServicePurchase;
import vn.softdreams.ebweb.repository.GoodsServicePurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.GoodsServicePurchaseContvertDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GoodsServicePurchase.
 */
@Service
@Transactional
public class GoodsServicePurchaseServiceImpl implements GoodsServicePurchaseService {

    private final Logger log = LoggerFactory.getLogger(GoodsServicePurchaseServiceImpl.class);

    private final GoodsServicePurchaseRepository goodsServicePurchaseRepository;

    public GoodsServicePurchaseServiceImpl(GoodsServicePurchaseRepository goodsServicePurchaseRepository) {
        this.goodsServicePurchaseRepository = goodsServicePurchaseRepository;
    }

    /**
     * Save a goodsServicePurchase.
     *
     * @param goodsServicePurchase the entity to save
     * @return the persisted entity
     */
    @Override
    public GoodsServicePurchase save(GoodsServicePurchase goodsServicePurchase) {
        log.debug("Request to save GoodsServicePurchase : {}", goodsServicePurchase);        return goodsServicePurchaseRepository.save(goodsServicePurchase);
    }

    /**
     * Get all the goodsServicePurchases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsServicePurchase> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsServicePurchases");
        return goodsServicePurchaseRepository.findAll(pageable);
    }


    /**
     * Get one goodsServicePurchase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GoodsServicePurchase> findOne(UUID id) {
        log.debug("Request to get GoodsServicePurchase : {}", id);
        return goodsServicePurchaseRepository.findById(id);
    }

    /**
     * Delete the goodsServicePurchase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GoodsServicePurchase : {}", id);
        goodsServicePurchaseRepository.deleteById(id);
    }

    @Override
    public Optional<GoodsServicePurchase> getPurchaseName() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return goodsServicePurchaseRepository.getPurchaseName(currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public Page<GoodsServicePurchaseContvertDTO> getPurchaseNameToCombobox() {
        return new PageImpl<>(goodsServicePurchaseRepository.getPurchaseNameToCombobox());
    }

    public List<GoodsServicePurchase> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return goodsServicePurchaseRepository.findAllByIsActive();
        }
        throw new BadRequestAlertException("", "", "");
    }
}
