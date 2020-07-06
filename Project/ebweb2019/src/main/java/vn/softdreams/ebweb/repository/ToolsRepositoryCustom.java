package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import java.util.List;
import java.util.UUID;

public interface ToolsRepositoryCustom {

    List<ToolsConvertDTO> getAllToolsByActive(UUID org, boolean currentBook);

    List<ToolsConvertDTO> getToolsActiveByIncrements(UUID org, Integer currentBook, String date);

    List<ToolsDetailsConvertDTO> getToolsDetailsByID(UUID toolsID);

    List<ToolsConvertDTO> getToolsActiveByTIDecrement(UUID org, Integer currentBook, String date);

    List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIDecrement(UUID org, Integer currentBook, UUID id, String date);

    List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIIncrement(UUID org, Integer currentBook, UUID id, String date);

    List<Tools> findAllToolsByCompanyID(UUID orgID, boolean isDependent, int phienSoLamViec);
}
