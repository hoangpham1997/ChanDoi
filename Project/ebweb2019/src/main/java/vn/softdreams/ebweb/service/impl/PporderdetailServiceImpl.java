package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.repository.PporderdetailRepository;
import vn.softdreams.ebweb.service.PporderdetailService;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PPOrderDetail.
 */
@Service
@Transactional
public class PporderdetailServiceImpl implements PporderdetailService {

    private final Logger log = LoggerFactory.getLogger(PporderdetailServiceImpl.class);

    private final PporderdetailRepository pporderdetailRepository;

    public PporderdetailServiceImpl(PporderdetailRepository pporderdetailRepository) {
        this.pporderdetailRepository = pporderdetailRepository;
    }

    /**
     * Save a pporderdetail.
     *
     * @param pporderdetail the entity to save
     * @return the persisted entity
     */
    @Override
    public PPOrderDetail save(PPOrderDetail pporderdetail) {
        log.debug("Request to save PPOrderDetail : {}", pporderdetail);
        return pporderdetailRepository.save(pporderdetail);
    }

    /**
     * Get all the pporderdetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPOrderDetail> findAll(Pageable pageable) {
        log.debug("Request to get all Pporderdetails");
        return pporderdetailRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PPOrderDetail> findAll() {
        log.debug("Request to get all Pporderdetails");
        return pporderdetailRepository.findAll();
    }

    /**
     * Get one pporderdetail by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPOrderDetail> findOne(UUID id) {
        log.debug("Request to get PPOrderDetail : {}", id);
        return pporderdetailRepository.findById(id);
    }

    /**
     * Delete the pporderdetail by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPOrderDetail : {}", id);
        pporderdetailRepository.deleteById(id);
    }
}
