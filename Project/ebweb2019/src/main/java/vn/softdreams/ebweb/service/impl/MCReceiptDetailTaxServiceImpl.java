package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCReceiptDetailTaxService;
import vn.softdreams.ebweb.domain.MCReceiptDetailTax;
import vn.softdreams.ebweb.repository.MCReceiptDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCReceiptDetailTax.
 */
@Service
@Transactional
public class MCReceiptDetailTaxServiceImpl implements MCReceiptDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailTaxServiceImpl.class);

    private final MCReceiptDetailTaxRepository mCReceiptDetailTaxRepository;

    public MCReceiptDetailTaxServiceImpl(MCReceiptDetailTaxRepository mCReceiptDetailTaxRepository) {
        this.mCReceiptDetailTaxRepository = mCReceiptDetailTaxRepository;
    }

    /**
     * Save a mCReceiptDetailTax.
     *
     * @param mCReceiptDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MCReceiptDetailTax save(MCReceiptDetailTax mCReceiptDetailTax) {
        log.debug("Request to save MCReceiptDetailTax : {}", mCReceiptDetailTax);        return mCReceiptDetailTaxRepository.save(mCReceiptDetailTax);
    }

    /**
     * Get all the mCReceiptDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCReceiptDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all MCReceiptDetailTaxes");
        return mCReceiptDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one mCReceiptDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCReceiptDetailTax> findOne(UUID id) {
        log.debug("Request to get MCReceiptDetailTax : {}", id);
        return mCReceiptDetailTaxRepository.findById(id);
    }

    /**
     * Delete the mCReceiptDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCReceiptDetailTax : {}", id);
        mCReceiptDetailTaxRepository.deleteById(id);
    }
}
