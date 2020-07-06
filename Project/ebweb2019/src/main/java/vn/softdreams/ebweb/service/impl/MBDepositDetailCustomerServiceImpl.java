package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.MBDepositDetailCustomerService;
import vn.softdreams.ebweb.domain.MBDepositDetailCustomer;
import vn.softdreams.ebweb.repository.MBDepositDetailCustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing MBDepositDetailCustomer.
 */
@Service
@Transactional
public class MBDepositDetailCustomerServiceImpl implements MBDepositDetailCustomerService {

    private final Logger log = LoggerFactory.getLogger(MBDepositDetailCustomerServiceImpl.class);

    private final MBDepositDetailCustomerRepository mBDepositDetailCustomerRepository;

    public MBDepositDetailCustomerServiceImpl(MBDepositDetailCustomerRepository mBDepositDetailCustomerRepository) {
        this.mBDepositDetailCustomerRepository = mBDepositDetailCustomerRepository;
    }

    /**
     * Save a mBDepositDetailCustomer.
     *
     * @param mBDepositDetailCustomer the entity to save
     * @return the persisted entity
     */
    @Override
    public MBDepositDetailCustomer save(MBDepositDetailCustomer mBDepositDetailCustomer) {
        log.debug("Request to save MBDepositDetailCustomer : {}", mBDepositDetailCustomer);        return mBDepositDetailCustomerRepository.save(mBDepositDetailCustomer);
    }

    /**
     * Get all the mBDepositDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBDepositDetailCustomer> findAll(Pageable pageable) {
        log.debug("Request to get all MBDepositDetailCustomers");
        return mBDepositDetailCustomerRepository.findAll(pageable);
    }


    /**
     * Get one mBDepositDetailCustomer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBDepositDetailCustomer> findOne(UUID id) {
        log.debug("Request to get MBDepositDetailCustomer : {}", id);
        return mBDepositDetailCustomerRepository.findById(id);
    }

    /**
     * Delete the mBDepositDetailCustomer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBDepositDetailCustomer : {}", id);
        mBDepositDetailCustomerRepository.deleteById(id);
    }
}
