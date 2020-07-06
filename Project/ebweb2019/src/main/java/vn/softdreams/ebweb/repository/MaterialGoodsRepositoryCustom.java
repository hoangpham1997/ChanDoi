package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.domain.SearchVoucherMaterialGoods;
import vn.softdreams.ebweb.service.dto.MGForPPOrderConvertDTO;
import vn.softdreams.ebweb.service.dto.MGForPPOrderConvertQuantityDTO;
import vn.softdreams.ebweb.service.dto.MaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.ObjectDTO;

import java.util.List;
import java.util.UUID;

public interface MaterialGoodsRepositoryCustom {
    List<MaterialGoodsDTO> findAllForPPService();

    List<MaterialGoodsDTO> findAllForPPInvoice();

    Page<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox(UUID toString);

    List<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox1(List<UUID> companyIds, List<Integer> materialsGoodsType);

    Page<MaterialGoods> getAllByCompanyID(Pageable pageable, UUID orgID);

    Page<MaterialGoods> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID);

    Page<MaterialGoods> pageableAllMaterialGoods(Pageable sort, UUID org);

    Page<MaterialGoods> findVoucherByMaterialGoodsID(Pageable pageable, UUID id);

    Page<MaterialGoods> findAll1(Pageable sort, SearchVoucherMaterialGoods searchVoucherMaterialGoods, UUID companyID);

    List<MaterialGoodsDTO> findAllMaterialGoodsCustom(List<UUID> companyId);

    List<MaterialGoodsDTO> findAllForDTO(List<UUID> companyId);

    void rollBack();

    List<MGForPPOrderConvertQuantityDTO> getQuantityExistsTest(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs, String postedDate);

    List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs);

    Page<MaterialGoods> pageableAllMaterialGood(Pageable sort, List<UUID> org);

    List<MaterialGoodsDTO> findAllForDTOSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared);

    List<MaterialGoods> insertBulk(List<MaterialGoods> materialGoods);
}
