package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the AccountingObjectGroup entity.
 */
@SuppressWarnings("unused")

public interface StatisticsCodeRepositoryCustom {
    List<StatisticsCode> getAllStatisticsCodeByCompanyIDAndSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch);
}
