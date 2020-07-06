package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.GOtherVoucherDetailTaxService;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailTax;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GOtherVoucherDetailTax.
 */
@Service
@Transactional
public class GOtherVoucherDetailTaxServiceImpl implements GOtherVoucherDetailTaxService {

    private final Logger log = LoggerFactory.getLogger(GOtherVoucherDetailTaxServiceImpl.class);

    private final GOtherVoucherDetailTaxRepository gOtherVoucherDetailTaxRepository;

    public GOtherVoucherDetailTaxServiceImpl(GOtherVoucherDetailTaxRepository gOtherVoucherDetailTaxRepository) {
        this.gOtherVoucherDetailTaxRepository = gOtherVoucherDetailTaxRepository;
    }

    /**
     * Save a gOtherVoucherDetailTax.
     *
     * @param gOtherVoucherDetailTax the entity to save
     * @return the persisted entity
     */
    @Override
    public GOtherVoucherDetailTax save(GOtherVoucherDetailTax gOtherVoucherDetailTax) {
        log.debug("Request to save GOtherVoucherDetailTax : {}", gOtherVoucherDetailTax);        return gOtherVoucherDetailTaxRepository.save(gOtherVoucherDetailTax);
    }

    /**
     * Get all the gOtherVoucherDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GOtherVoucherDetailTax> findAll(Pageable pageable) {
        log.debug("Request to get all GOtherVoucherDetailTaxes");
        return gOtherVoucherDetailTaxRepository.findAll(pageable);
    }


    /**
     * Get one gOtherVoucherDetailTax by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GOtherVoucherDetailTax> findOne(UUID id) {
        log.debug("Request to get GOtherVoucherDetailTax : {}", id);
        return gOtherVoucherDetailTaxRepository.findById(id);
    }

    /**
     * Delete the gOtherVoucherDetailTax by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GOtherVoucherDetailTax : {}", id);
        gOtherVoucherDetailTaxRepository.deleteById(id);
    }
}
