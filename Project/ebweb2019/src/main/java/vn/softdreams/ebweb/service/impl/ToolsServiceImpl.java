package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.ToolsService;
import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.repository.ToolsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Tools.
 */
@Service
@Transactional
public class ToolsServiceImpl implements ToolsService {

    private final Logger log = LoggerFactory.getLogger(ToolsServiceImpl.class);

    private final ToolsRepository toolsRepository;
    private final UserService userService;

    public ToolsServiceImpl(ToolsRepository toolsRepository, UserService userService) {
        this.toolsRepository = toolsRepository;
        this.userService = userService;
    }

    /**
     * Save a tools.
     *
     * @param tools the entity to save
     * @return the persisted entity
     */
    @Override
    public Tools save(Tools tools) {
        log.debug("Request to save Tools : {}", tools);        return toolsRepository.save(tools);
    }

    /**
     * Get all the tools.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Tools> findAll(Pageable pageable) {
        log.debug("Request to get all Tools");
        return toolsRepository.findAll(pageable);
    }


    /**
     * Get one tools by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Tools> findOne(UUID id) {
        log.debug("Request to get Tools : {}", id);
        return toolsRepository.findById(id);
    }

    /**
     * Delete the tools by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Tools : {}", id);
        toolsRepository.deleteById(id);
    }

    @Override
    public List<Tools> findAllByCompanyID(UUID orgID, boolean isDependent) {
        log.debug("Request to get all Tools");
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        return toolsRepository.findAllToolsByCompanyID(orgID, isDependent, phienSoLamViec);
    }

    @Override
    public List<Tools> getToolsActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return toolsRepository.getToolsActive(currentUserLoginAndOrg.get().getOrg());
    }
}
