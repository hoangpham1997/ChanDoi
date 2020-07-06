package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.PrepaidExpenseVoucherService;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;
import vn.softdreams.ebweb.repository.PrepaidExpenseVoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PrepaidExpenseVoucher.
 */
@Service
@Transactional
public class PrepaidExpenseVoucherServiceImpl implements PrepaidExpenseVoucherService {

    private final Logger log = LoggerFactory.getLogger(PrepaidExpenseVoucherServiceImpl.class);

    private final PrepaidExpenseVoucherRepository prepaidExpenseVoucherRepository;

    public PrepaidExpenseVoucherServiceImpl(PrepaidExpenseVoucherRepository prepaidExpenseVoucherRepository) {
        this.prepaidExpenseVoucherRepository = prepaidExpenseVoucherRepository;
    }

    /**
     * Save a prepaidExpenseVoucher.
     *
     * @param prepaidExpenseVoucher the entity to save
     * @return the persisted entity
     */
    @Override
    public PrepaidExpenseVoucher save(PrepaidExpenseVoucher prepaidExpenseVoucher) {
        log.debug("Request to save PrepaidExpenseVoucher : {}", prepaidExpenseVoucher);        return prepaidExpenseVoucherRepository.save(prepaidExpenseVoucher);
    }

    /**
     * Get all the prepaidExpenseVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrepaidExpenseVoucher> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaidExpenseVouchers");
        return prepaidExpenseVoucherRepository.findAll(pageable);
    }


    /**
     * Get one prepaidExpenseVoucher by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaidExpenseVoucher> findOne(UUID id) {
        log.debug("Request to get PrepaidExpenseVoucher : {}", id);
        return prepaidExpenseVoucherRepository.findById(id);
    }

    /**
     * Delete the prepaidExpenseVoucher by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PrepaidExpenseVoucher : {}", id);
        prepaidExpenseVoucherRepository.deleteById(id);
    }
}
