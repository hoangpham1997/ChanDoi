package vn.softdreams.ebweb.repository;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.FixedAsset;
import vn.softdreams.ebweb.service.dto.FixedAssetDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FixedAssetRepositoryCustom {
    /**
     * @param pageable
     * @param branchID
     * @param fixedAssetCategoryID
     * @param organizationUnitID
     * @param fixedAssetCode
     * @param fixedAssetName
     * @param description
     * @param productionYear
     * @param madeIn
     * @param serialNumber
     * @param accountingObjectID
     * @param accountingObjectName
     * @param warranty
     * @param guaranteeCodition
     * @param isSecondHand
     * @param currentState
     * @param deliveryRecordNo
     * @param deliveryRecordDate
     * @param purchasedDate
     * @param incrementDate
     * @param depreciationDate
     * @param usedDate
     * @param depreciationAccount
     * @param originalPriceAccount
     * @param expenditureAccount
     * @param purchasePrice
     * @param originalPrice
     * @param depreciationMethod
     * @param usedTime
     * @param displayMonthYear
     * @param periodDepreciationAmount
     * @param depreciationRate
     * @param monthDepreciationRate
     * @param monthPeriodDepreciationAmount
     * @param isActive
     * @return
     */
    Page<FixedAsset> findAll(Pageable pageable, UUID branchID, UUID fixedAssetCategoryID, UUID organizationUnitID,
                             String fixedAssetCode, String fixedAssetName, String description,
                             Integer productionYear, String madeIn, String serialNumber, UUID accountingObjectID,
                             String accountingObjectName, String warranty, String guaranteeCodition,
                             Boolean isSecondHand, Integer currentState, String deliveryRecordNo,
                             LocalDateTime deliveryRecordDate, LocalDateTime purchasedDate,
                             LocalDateTime incrementDate, LocalDateTime depreciationDate, LocalDateTime usedDate,
                             String depreciationAccount, String originalPriceAccount, String expenditureAccount,
                             BigDecimal purchasePrice, BigDecimal originalPrice, Integer depreciationMethod,
                             BigDecimal usedTime, Boolean displayMonthYear, BigDecimal periodDepreciationAmount,
                             BigDecimal depreciationRate, BigDecimal monthDepreciationRate,
                             BigDecimal monthPeriodDepreciationAmount, Boolean isActive);

    /**
     * @author congnd
     * @param org
     * @return
     */
    List<FixedAssetDTO> getAllFixedAssetByActive(UUID org);

    FixedAssetDTO getById(UUID fixedAssetID);
}
