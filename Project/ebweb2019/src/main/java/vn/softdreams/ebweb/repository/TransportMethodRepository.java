package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.TransportMethod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TransportMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransportMethodRepository extends JpaRepository<TransportMethod, UUID> {
    @Query(value = "SELECT count(*) from TransportMethod WHERE CompanyID = ?1 and TransportMethodCode = ?2 COLLATE Latin1_General_CS_AS", nativeQuery = true)
    Integer checkDuplicateTransportMethodCode(UUID org, String no);

    @Query(value = "select * from TransportMethod WHERE CompanyID = ?1 ORDER BY transportMethodCode", nativeQuery = true)
    Page<TransportMethod> findAll(UUID org, Pageable pageable);

    @Query(value = "select * from TransportMethod WHERE CompanyID = ?1 AND IsActive = 1 ORDER BY transportMethodCode ", nativeQuery = true)
    List<TransportMethod> getTransportMethodCombobox(UUID org);
}
