package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.WarrantyService;
import vn.softdreams.ebweb.domain.Warranty;
import vn.softdreams.ebweb.repository.WarrantyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Warranty.
 */
@Service
@Transactional
public class WarrantyServiceImpl implements WarrantyService {

    private final Logger log = LoggerFactory.getLogger(WarrantyServiceImpl.class);

    private final WarrantyRepository warrantyRepository;

    public WarrantyServiceImpl(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
    }

    /**
     * Save a warranty.
     *
     * @param warranty the entity to save
     * @return the persisted entity
     */
    @Override
    public Warranty save(Warranty warranty) {
        log.debug("Request to save Warranty : {}", warranty);        return warrantyRepository.save(warranty);
    }

    /**
     * Get all the warranties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Warranty> findAll(Pageable pageable) {
        log.debug("Request to get all Warranties");
        return warrantyRepository.findAll(pageable);
    }


    /**
     * Get one warranty by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Warranty> findOne(long id) {
        log.debug("Request to get Warranty : {}", id);
        return warrantyRepository.findById(id);
    }

    /**
     * Delete the warranty by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(long id) {
        log.debug("Request to delete Warranty : {}", id);
        warrantyRepository.deleteById(id);
    }

    @Override
    public List<Warranty> getAllWarrantyByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return warrantyRepository.getAllWarrantyByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
