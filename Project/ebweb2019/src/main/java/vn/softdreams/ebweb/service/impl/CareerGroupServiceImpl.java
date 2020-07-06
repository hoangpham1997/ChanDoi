package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CareerGroupService;
import vn.softdreams.ebweb.domain.CareerGroup;
import vn.softdreams.ebweb.repository.CareerGroupRepository;
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

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMTKNH;

/**
 * Service Implementation for managing CareerGroup.
 */
@Service
@Transactional
public class CareerGroupServiceImpl implements CareerGroupService {

    private final Logger log = LoggerFactory.getLogger(CareerGroupServiceImpl.class);

    private final CareerGroupRepository careerGroupRepository;

    public CareerGroupServiceImpl(CareerGroupRepository careerGroupRepository) {
        this.careerGroupRepository = careerGroupRepository;
    }

    /**
     * Save a careerGroup.
     *
     * @param careerGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public CareerGroup save(CareerGroup careerGroup) {
        log.debug("Request to save CareerGroup : {}", careerGroup);        return careerGroupRepository.save(careerGroup);
    }

    /**
     * Get all the careerGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CareerGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CareerGroups");
        return careerGroupRepository.findAll(pageable);
    }


    /**
     * Get one careerGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CareerGroup> findOne(UUID id) {
        log.debug("Request to get CareerGroup : {}", id);
        return careerGroupRepository.findById(id);
    }

    /**
     * Delete the careerGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CareerGroup : {}", id);
        careerGroupRepository.deleteById(id);
    }

    @Override
    public List<CareerGroup> findAllCareerGroups() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return careerGroupRepository.findAllCareerGroups();
        }
        throw new BadRequestAlertException("", "", "");
    }
}
