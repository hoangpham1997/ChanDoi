package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.OrganizationUnitOptionReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnitOptionReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationUnitOptionReportRepository extends JpaRepository<OrganizationUnitOptionReport, UUID> {

    Optional<OrganizationUnitOptionReport> findOrganizationUnitOptionReportByOrganizationUnitID(UUID companyID);

    @Query(value = "select * from OrganizationUnitOptionReport b where  b.OrganizationUnitID = ?1", nativeQuery = true)
    OrganizationUnitOptionReport findByOrganizationUnitID(UUID org);
}
