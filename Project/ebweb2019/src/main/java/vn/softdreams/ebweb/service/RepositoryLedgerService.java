package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPResult;
import vn.softdreams.ebweb.domain.RepositoryLedger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.IWVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;
import vn.softdreams.ebweb.web.rest.dto.ResultCalculateOWDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing RepositoryLedger.
 */
public interface RepositoryLedgerService {

    /**
     * Save a repositoryLedger.
     *
     * @param repositoryLedger the entity to save
     * @return the persisted entity
     */
    RepositoryLedger save(RepositoryLedger repositoryLedger);

    /**
     * Get all the repositoryLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RepositoryLedger> findAll(Pageable pageable);


    /**
     * Get the "id" repositoryLedger.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RepositoryLedger> findOne(UUID id);

    /**
     * Delete the "id" repositoryLedger.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<LotNoDTO> getListLotNoByMaterialGoodsID(UUID materialGoodsID);

    ResultCalculateOWDTO calculateOWPrice(Integer calculationMethod, List<UUID> materialGoods, String fromDate, String toDate);

    Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId);

    Boolean updateIWPriceFromCost(List<CPResult> cpResults);

    Boolean updateOWPriceFromCost(List<CPResult> cpResults);

    List<LotNoDTO> getListLotNo(UUID materialGoodsID);
}
