package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitSearchDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnit entity.
 */
@SuppressWarnings("unused")
public interface OrganizationUnitRepositoryCustom{
    public List<String> GetListOrderFixCodeParentID(UUID id);

    Page<OrganizationUnit> findAllBigOrg(Pageable pageable);

    Page<OrganizationUnit> findAllBigOrgSearch(Pageable pageable, OrganizationUnitSearchDTO organizationUnitSearchDTO);

    OrganizationUnitCustomDTO findByIDCustom(UUID org);

    List<OrganizationUnitCustomDTO> findByParentIDCustomLogin(UUID parentID);

    List<OrganizationUnitCustomDTO> findAllByListCompanyID(Long id);
}
