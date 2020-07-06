package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MaterialGoodsPurchasePriceService;
import vn.softdreams.ebweb.domain.MaterialGoodsPurchasePrice;
import vn.softdreams.ebweb.repository.MaterialGoodsPurchasePriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MaterialGoodsPurchasePrice.
 */
@Service
@Transactional
public class MaterialGoodsPurchasePriceServiceImpl implements MaterialGoodsPurchasePriceService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsPurchasePriceServiceImpl.class);

    private final MaterialGoodsPurchasePriceRepository materialGoodsPurchasePriceRepository;

    public MaterialGoodsPurchasePriceServiceImpl(MaterialGoodsPurchasePriceRepository materialGoodsPurchasePriceRepository) {
        this.materialGoodsPurchasePriceRepository = materialGoodsPurchasePriceRepository;
    }

    /**
     * Save a materialGoodsPurchasePrice.
     *
     * @param materialGoodsPurchasePrice the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsPurchasePrice save(MaterialGoodsPurchasePrice materialGoodsPurchasePrice) {
        log.debug("Request to save MaterialGoodsPurchasePrice : {}", materialGoodsPurchasePrice);        return materialGoodsPurchasePriceRepository.save(materialGoodsPurchasePrice);
    }

    /**
     * Get all the materialGoodsPurchasePrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsPurchasePrice> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsPurchasePrices");
        return materialGoodsPurchasePriceRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsPurchasePrice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsPurchasePrice> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsPurchasePrice : {}", id);
        return materialGoodsPurchasePriceRepository.findById(id);
    }

    /**
     * Delete the materialGoodsPurchasePrice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsPurchasePrice : {}", id);
        materialGoodsPurchasePriceRepository.deleteById(id);
    }

    @Override
    public List<MaterialGoodsPurchasePrice> findByMaterialGoodsID(UUID id) {
        return materialGoodsPurchasePriceRepository.findByMaterialGoodsID(id);
    }
}
