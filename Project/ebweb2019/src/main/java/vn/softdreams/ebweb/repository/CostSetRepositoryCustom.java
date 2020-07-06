package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.domain.CostSetMaterialGood;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CostSetRepositoryCustom {
    Page<CostSet> findAll(Pageable pageable, UUID branchID, String costSetCode, String costSetName, Integer costSetType,
                          String description, UUID parentID, Boolean isParentNode, String orderFixCode, Integer grade, Boolean isActive);
    //change
    Page<CostSet> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID);

    Page<CostSet> pageableAllCostSets(Pageable sort, UUID org);



//    Page<CostSet> pageableAllCostSet(Pageable sort, List<UUID> org);

    void multideleteCostSets(UUID orgID, List<UUID> uuidList);

    List<UpdateDataDTO> getUsedCostSetID(List<UUID> costSets);

    Page<CostSet> getCostSetsByTypeRaTio(Pageable pageable, Integer type, List<UUID> allCompanyByCompanyIdAndCode);

    Boolean checkCostSetRelatedVoucher(UUID id);

    List<CostSetMaterialGoodsDTO> getCostSetByListID(List<UUID> orgCostSet, List<UUID> orgMaterialGoods, List<UUID> uuids);

    List<CostSetMaterialGoodsDTO> getMaterialGoodsByCostSetID(List<UUID> collect);

    List<CostSetDTO> getCostSetAndRevenueAmount(List<UUID> collect, String fromDate, String toDate);

}
