package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucherMaterialGoods;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoods.
 */
public interface MaterialGoodsService {

    /**
     * Save a materialGoods.
     *
     * @param materialGoods the entity to save
     * @return the persisted entity
     */
    MaterialGoodSaveDTO save(MaterialGoods materialGoods);

    /**
     * Get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoods> findAll(Pageable pageable);

    /**
     * Get all the materialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoods> getAllByCompanyID(Pageable pageable);

    Page<MaterialGoods> pageableAllMaterialGoods(Pageable pageable);

    /**
     * Get the "id" materialGoods.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoods> findOne(UUID id);
    Page<MaterialGoods> findVoucherByMaterialGoodsID(Pageable pageable, UUID id);

    /**
     * Delete the "id" materialGoods.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    HandlingResultDTO deleteEmployee(List<UUID> uuids);

    List<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox1(List<Integer> materialsGoodsType);

    List<MGForPPOrderDTO> getMaterialGoodsForCombobox();

    Long getMaterialGoodsPPInvoiceQuantity(UUID id, UUID ppOrderDetailID);

    Page<MaterialGoods> findAll1(Pageable pageable, SearchVoucherMaterialGoods searchVoucherMaterialGoods);

    /**
     * @Author hieugie
     *
     * Lấy hàng hóa theo com id
     *
     * @return
     */
    List<MaterialGoods> findByCompanyIDAndIsActiveTrue();

    List<MaterialGoodsConvertDTO> ConvertToCombobox();

    List<MaterialGoodsDTO> findAllForPPService();

    List<MaterialGoodsDTO> findAllForPPInvoice();

    Optional<MaterialGoods> getByUUID(UUID id);

    List<MaterialGoods> findAllActive();

    List<MaterialGoods> getAllMaterialGoodsActiveCompanyIDByRepository(UUID repositoryId);

    List<MaterialGoods> findAllByCompanyID();

    List<MaterialGoodsDTO> findAllMaterialGoodsCustom();

    List<MaterialGoodsDTO> findAllForDTO(UUID companyID, Boolean similarBranch);

    List<MGForPPOrderConvertQuantityDTO> getQuantityExistsTest(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs, String postedDate);

    ListObjectDTO getMaterialGoodAndRepository(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs);

    List<MaterialGoodsDTO> findAllForDTOSimilarBranch(Boolean similarBranch, UUID companyID);
}
