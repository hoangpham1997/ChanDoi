package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.OrganizationUnitOptionReport;
import vn.softdreams.ebweb.repository.OrganizationUnitOptionReportRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.OrganizationUnitOptionReportService;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing OrganizationUnitOptionReport.
 */
@Service
@Transactional
public class OrganizationUnitOptionReportServiceImpl implements OrganizationUnitOptionReportService {

    private final Logger log = LoggerFactory.getLogger(OrganizationUnitOptionReportServiceImpl.class);

    private final OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    public OrganizationUnitOptionReportServiceImpl(OrganizationUnitOptionReportRepository organizationUnitOptionReportRepository,
                                                   OrganizationUnitRepository organizationUnitRepository) {
        this.organizationUnitOptionReportRepository = organizationUnitOptionReportRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a organizationUnitOptionReport.
     *
     * @param organizationUnitOptionReport the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationUnitOptionReport save(OrganizationUnitOptionReport organizationUnitOptionReport) {
        log.debug("Request to save OrganizationUnitOptionReport : {}", organizationUnitOptionReport);
        return organizationUnitOptionReportRepository.save(organizationUnitOptionReport);
    }

    /**
     * Get all the organizationUnitOptionReports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationUnitOptionReport> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationUnitOptionReports");
        return organizationUnitOptionReportRepository.findAll(pageable);
    }


    /**
     * Get one organizationUnitOptionReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationUnitOptionReport> findOne(UUID id) {
        log.debug("Request to get OrganizationUnitOptionReport : {}", id);
        return organizationUnitOptionReportRepository.findById(id);
    }

    @Override
    public OrganizationUnitOptionReport findByOrganizationUnitID(UUID orgID) {
        return organizationUnitOptionReportRepository.findByOrganizationUnitID(orgID);
    }

    /**
     * Delete the organizationUnitOptionReport by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrganizationUnitOptionReport : {}", id);
        organizationUnitOptionReportRepository.deleteById(id);
    }

    @Override
    public Optional<OrganizationUnitOptionReport> findOneByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return organizationUnitOptionReportRepository.findOrganizationUnitOptionReportByOrganizationUnitID(currentUserLoginAndOrg.get().getOrg());
        }
        return Optional.empty();
    }
}
