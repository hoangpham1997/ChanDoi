package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.OrganizationUnitOptionReport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing OrganizationUnitOptionReport.
 */
public interface OrganizationUnitOptionReportService {

    /**
     * Save a organizationUnitOptionReport.
     *
     * @param organizationUnitOptionReport the entity to save
     * @return the persisted entity
     */
    OrganizationUnitOptionReport save(OrganizationUnitOptionReport organizationUnitOptionReport);

    /**
     * Get all the organizationUnitOptionReports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrganizationUnitOptionReport> findAll(Pageable pageable);


    /**
     * Get the "id" organizationUnitOptionReport.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganizationUnitOptionReport> findOne(UUID id);

    OrganizationUnitOptionReport findByOrganizationUnitID(UUID orgID);


    /**
     * Delete the "id" organizationUnitOptionReport.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Optional<OrganizationUnitOptionReport> findOneByCompanyID();
}
