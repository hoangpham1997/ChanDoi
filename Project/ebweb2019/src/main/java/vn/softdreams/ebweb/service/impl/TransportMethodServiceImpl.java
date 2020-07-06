package vn.softdreams.ebweb.service.impl;

import io.undertow.util.BadRequestException;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.TransportMethodService;
import vn.softdreams.ebweb.domain.TransportMethod;
import vn.softdreams.ebweb.repository.TransportMethodRepository;
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
 * Service Implementation for managing TransportMethod.
 */
@Service
@Transactional
public class TransportMethodServiceImpl implements TransportMethodService {

    private final Logger log = LoggerFactory.getLogger(TransportMethodServiceImpl.class);

    private final TransportMethodRepository transportMethodRepository;

    private final UtilsRepository utilsRepository;

    public TransportMethodServiceImpl(TransportMethodRepository transportMethodRepository, UtilsRepository utilsRepository) {
        this.transportMethodRepository = transportMethodRepository;
        this.utilsRepository = utilsRepository;
    }

    /**
     * Save a transportMethod.
     *
     * @param transportMethod the entity to save
     * @return the persisted entity
     */
    @Override
    public TransportMethod save(TransportMethod transportMethod) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO -> {
            transportMethod.setCompanyId(securityDTO.getOrg());
            if (transportMethod.getId() != null) {
                Optional<TransportMethod> oldTransportMethod = transportMethodRepository.findById(transportMethod.getId());
                if (oldTransportMethod.isPresent()) {
                    if (oldTransportMethod.get().getTransportMethodCode().equals(transportMethod.getTransportMethodCode())) {
                        return;
                    }
                }
            }
            Integer count = transportMethodRepository.checkDuplicateTransportMethodCode(transportMethod.getCompanyId(), transportMethod.getTransportMethodCode());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("", "TransportMethod", "duplicateTransportMethodCode");
            }
        });
        return transportMethodRepository.save(transportMethod);
    }

    /**
     * Get all the transportMethods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransportMethod> findAll(Pageable pageable) {
        log.debug("Request to get all TransportMethods");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return transportMethodRepository.findAll(currentUserLoginAndOrg.get().getOrg(), pageable);
    }


    /**
     * Get one transportMethod by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransportMethod> findOne(UUID id) {
        log.debug("Request to get TransportMethod : {}", id);
        return transportMethodRepository.findById(id);
    }

    /**
     * Delete the transportMethod by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TransportMethod : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        boolean checkTransportID = utilsRepository.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrg(), id, "TransportMethodID");
        if (checkTransportID) {
            throw new BadRequestAlertException("", "", "");
        } else {
            transportMethodRepository.deleteById(id);
        }

    }

    @Override
    public List<TransportMethod> getTransportMethodCombobox() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return transportMethodRepository.getTransportMethodCombobox(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
