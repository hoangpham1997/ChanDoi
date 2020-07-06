package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialGoodsConvertUnit.
 */
public interface MaterialGoodsConvertUnitService {

    /**
     * Save a materialGoodsConvertUnit.
     *
     * @param materialGoodsConvertUnit the entity to save
     * @return the persisted entity
     */
    MaterialGoodsConvertUnit save(MaterialGoodsConvertUnit materialGoodsConvertUnit);

    /**
     * Get all the materialGoodsConvertUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialGoodsConvertUnit> findAll(Pageable pageable);


    /**
     * Get the "id" materialGoodsConvertUnit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialGoodsConvertUnit> findOne(UUID id);

    /**
     * Delete the "id" materialGoodsConvertUnit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * lấy tỉ lệ chuyển đổ với phép tính từ mua hàng qua kho
     * @author congnd
     * @param materialGoodsId id hàng
     * @param unitId id đơn vị tính
     * @return
     */
    MaterialGoodsConvertUnit getMaterialGoodsConvertUnitPPInvoice(UUID materialGoodsId, UUID unitId);

    /**
     * @return
     * @Author Hautv
     */
    List<MaterialGoodsConvertUnitDTO> getAll();

    List<MaterialGoodsConvertUnit> findByMaterialGoodsID(UUID id);

    List<Integer> getNumberOrder(UUID companyID, Boolean similarBranch);

}
