package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Repository;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.dto.ObjectDTO;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RepositoryRepositoryCustom {
    Page<Repository> pageableAllRepositories(Pageable sort, List<UUID> org);
    Page<Repository> getAllByListCompany(Pageable sort,  List<UUID> listCompanyID);
    List<Repository> pageableAllRepositoriesCBB(UUID org);
    List<Repository> getAllByListCompanyCBB(List<UUID> listCompanyID);
    List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs);
    List<Repository> findAllRepositoryCustom(List<UUID> companyId);
}
