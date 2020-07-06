package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.service.TIIncrementDetailRefVoucherService;
import vn.softdreams.ebweb.domain.TIIncrementDetailRefVoucher;
import vn.softdreams.ebweb.repository.TIIncrementDetailRefVoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIIncrementDetailRefVoucher.
 */
@Service
@Transactional
public class TIIncrementDetailRefVoucherServiceImpl implements TIIncrementDetailRefVoucherService {

    private final Logger log = LoggerFactory.getLogger(TIIncrementDetailRefVoucherServiceImpl.class);

    private final TIIncrementDetailRefVoucherRepository tIIncrementDetailRefVoucherRepository;

    public TIIncrementDetailRefVoucherServiceImpl(TIIncrementDetailRefVoucherRepository tIIncrementDetailRefVoucherRepository) {
        this.tIIncrementDetailRefVoucherRepository = tIIncrementDetailRefVoucherRepository;
    }

    /**
     * Save a tIIncrementDetailRefVoucher.
     *
     * @param tIIncrementDetailRefVoucher the entity to save
     * @return the persisted entity
     */
    @Override
    public TIIncrementDetailRefVoucher save(TIIncrementDetailRefVoucher tIIncrementDetailRefVoucher) {
        log.debug("Request to save TIIncrementDetailRefVoucher : {}", tIIncrementDetailRefVoucher);        return tIIncrementDetailRefVoucherRepository.save(tIIncrementDetailRefVoucher);
    }

    /**
     * Get all the tIIncrementDetailRefVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIIncrementDetailRefVoucher> findAll(Pageable pageable) {
        log.debug("Request to get all TIIncrementDetailRefVouchers");
        return tIIncrementDetailRefVoucherRepository.findAll(pageable);
    }


    /**
     * Get one tIIncrementDetailRefVoucher by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIIncrementDetailRefVoucher> findOne(UUID id) {
        log.debug("Request to get TIIncrementDetailRefVoucher : {}", id);
        return tIIncrementDetailRefVoucherRepository.findById(id);
    }

    /**
     * Delete the tIIncrementDetailRefVoucher by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIIncrementDetailRefVoucher : {}", id);
        tIIncrementDetailRefVoucherRepository.deleteById(id);
    }
}
