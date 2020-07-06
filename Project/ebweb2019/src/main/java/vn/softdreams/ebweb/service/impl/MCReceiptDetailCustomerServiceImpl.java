package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MCReceiptDetailCustomerService;
import vn.softdreams.ebweb.domain.MCReceiptDetailCustomer;
import vn.softdreams.ebweb.repository.MCReceiptDetailCustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MCReceiptDetailCustomer.
 */
@Service
@Transactional
public class MCReceiptDetailCustomerServiceImpl implements MCReceiptDetailCustomerService {

    private final Logger log = LoggerFactory.getLogger(MCReceiptDetailCustomerServiceImpl.class);

    private final MCReceiptDetailCustomerRepository mCReceiptDetailCustomerRepository;

    public MCReceiptDetailCustomerServiceImpl(MCReceiptDetailCustomerRepository mCReceiptDetailCustomerRepository) {
        this.mCReceiptDetailCustomerRepository = mCReceiptDetailCustomerRepository;
    }

    /**
     * Save a mCReceiptDetailCustomer.
     *
     * @param mCReceiptDetailCustomer the entity to save
     * @return the persisted entity
     */
    @Override
    public MCReceiptDetailCustomer save(MCReceiptDetailCustomer mCReceiptDetailCustomer) {
        log.debug("Request to save MCReceiptDetailCustomer : {}", mCReceiptDetailCustomer);        return mCReceiptDetailCustomerRepository.save(mCReceiptDetailCustomer);
    }

    /**
     * Get all the mCReceiptDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCReceiptDetailCustomer> findAll(Pageable pageable) {
        log.debug("Request to get all MCReceiptDetailCustomers");
        return mCReceiptDetailCustomerRepository.findAll(pageable);
    }


    /**
     * Get one mCReceiptDetailCustomer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCReceiptDetailCustomer> findOne(UUID id) {
        log.debug("Request to get MCReceiptDetailCustomer : {}", id);
        return mCReceiptDetailCustomerRepository.findById(id);
    }

    /**
     * Delete the mCReceiptDetailCustomer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCReceiptDetailCustomer : {}", id);
        mCReceiptDetailCustomerRepository.deleteById(id);
    }
}
