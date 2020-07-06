package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBInternalTransferTaxService;
import vn.softdreams.ebweb.domain.MBInternalTransferTax;
import vn.softdreams.ebweb.repository.MBInternalTransferTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBInternalTransferTax.
 */
@Service
@Transactional
public class MBInternalTransferTaxServiceImpl implements MBInternalTransferTaxService {

    private final Logger log = LoggerFactory.getLogger(MBInternalTransferTaxServiceImpl.class);

    private final MBInternalTransferTaxRepository mBInternalTransferTaxRepository;

    public MBInternalTransferTaxServiceImpl(MBInternalTransferTaxRepository mBInternalTransferTaxRepository) {
        this.mBInternalTransferTaxRepository = mBInternalTransferTaxRepository;
    }

    /**
     * Save a mBInternalTransferTax.
     *
     * @param mBInternalTransferTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MBInternalTransferTax save(MBInternalTransferTax mBInternalTransferTax) {
        log.debug("Request to save MBInternalTransferTax : {}", mBInternalTransferTax);        return mBInternalTransferTaxRepository.save(mBInternalTransferTax);
    }

    /**
     * Get all the mBInternalTransferTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBInternalTransferTax> findAll(Pageable pageable) {
        log.debug("Request to get all MBInternalTransferTaxes");
        return mBInternalTransferTaxRepository.findAll(pageable);
    }


    /**
     * Get one mBInternalTransferTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBInternalTransferTax> findOne(UUID id) {
        log.debug("Request to get MBInternalTransferTax : {}", id);
        return mBInternalTransferTaxRepository.findById(id);
    }

    /**
     * Delete the mBInternalTransferTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBInternalTransferTax : {}", id);
        mBInternalTransferTaxRepository.deleteById(id);
    }
}
