package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;

import java.util.List;
import java.util.UUID;

public interface ToolledgerRepositoryCustom {

    boolean unrecord(UUID refID);
}
