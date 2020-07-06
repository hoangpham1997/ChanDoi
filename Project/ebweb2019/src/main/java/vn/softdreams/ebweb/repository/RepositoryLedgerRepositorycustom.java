package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.RepositoryLedger;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.CalculateOWPriceDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositoryLedgerDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RepositoryLedgerRepositorycustom {
    Boolean unrecord(UUID repositoryLedgerID);

    Boolean unrecordList(List<UUID> repositoryLedgerID);

    List<LotNoDTO> getListLotNoByMaterialGoodsID(UUID materialGoodsID, UUID companyID);

    List<CalculateOWPriceDTO> calculateOWPrice(List<UUID> materialGoods, String fromDate, String toDate, Boolean isByRepository, UUID org, Integer soLamViec, Integer lamTronTienVN);

    Boolean updateOwPrice(List<CalculateOWPriceDTO> calculateOWPriceDTOs, String fromDate, String toDate, Integer lamTronDonGia, Integer lamTronDonGiaNT, Integer lamTronTienVN, Integer lamTronTienNT);

    Boolean updateOwPricePP2(List<RepositoryLedger> listRepositoryLedgersOW, String fromDate, String toDate, Integer lamTronDonGia, Integer lamTronDonGiaNT, Integer lamTronTienVN, Integer lamTronTienNT);

    List<RepositoryLedgerDTO> getListRepositoryError(String fromDate, String toDate, UUID org, Integer soLamViec);

    List<RepositoryLedgerDTO> getByCostSetID(List<UUID> collect, String fromDate, String toDate);

    Boolean updateIWPriceFromCost(List<RSInwardOutwardDetailsDTO> rsInwardOutWardDetails, UUID org, Integer soLamViec);

    List<RepositoryLedger> getListUpdateUnitPrice(List<UUID> collect, Integer soLamViec, LocalDate fromDate, LocalDate toDate);

    Boolean updateOWPriceFromCost(List<RepositoryLedger> repositoryLedgersRS, List<RepositoryLedger> repositoryLedgersSA, List<RepositoryLedger> repositoryLedgersAD, UUID org, Integer soLamViec);

    List<RepositoryLedgerDTO> getByCostSetIDAndMaterialGoods(List<UUID> uuids, List<UUID> collect, String fromDate, String toDate);

    List<LotNoDTO> getListLotNo(UUID materialGoodsID);
}
