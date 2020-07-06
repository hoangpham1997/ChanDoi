package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBTellerPaperDetailTaxService;
import vn.softdreams.ebweb.domain.MBTellerPaperDetailTax;
import vn.softdreams.ebweb.repository.MBTellerPaperDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBTellerPaperDetailTax.
 */
@Service
@Transactional
public class MBTellerPaperDetailTaxServiceImpl implements MBTellerPaperDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperDetailTaxServiceImpl.class);

    private final MBTellerPaperDetailTaxRepository mBTellerPaperDetailTaxRepository;

    public MBTellerPaperDetailTaxServiceImpl(MBTellerPaperDetailTaxRepository mBTellerPaperDetailTaxRepository) {
        this.mBTellerPaperDetailTaxRepository = mBTellerPaperDetailTaxRepository;
    }

    /**
     * Save a mBTellerPaperDetailTax.
     *
     * @param mBTellerPaperDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public MBTellerPaperDetailTax save(MBTellerPaperDetailTax mBTellerPaperDetailTax) {
        log.debug("Request to save MBTellerPaperDetailTax : {}", mBTellerPaperDetailTax);        return mBTellerPaperDetailTaxRepository.save(mBTellerPaperDetailTax);
    }

    /**
     * Get all the mBTellerPaperDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBTellerPaperDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all MBTellerPaperDetailTaxes");
        return mBTellerPaperDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one mBTellerPaperDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBTellerPaperDetailTax> findOne(UUID id) {
        log.debug("Request to get MBTellerPaperDetailTax : {}", id);
        return mBTellerPaperDetailTaxRepository.findById(id);
    }

    /**
     * Delete the mBTellerPaperDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBTellerPaperDetailTax : {}", id);
        mBTellerPaperDetailTaxRepository.deleteById(id);
    }
}
