package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.RepositoryLedgerRepositorycustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.CalculateOWPriceDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;
import vn.softdreams.ebweb.web.rest.dto.RepositoryLedgerDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RepositoryLedgerRepositoryImpl implements RepositoryLedgerRepositorycustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Boolean unrecord(UUID repositoryLedgerID) {
        boolean result = true;
        try {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("DELETE FROM RepositoryLedger where ReferenceID = :referenceID");
            params.put("referenceID", repositoryLedgerID);
            Query query = entityManager.createNativeQuery(sql.toString());
            Common.setParams(query, params);
            query.executeUpdate();
        }catch (Exception ex){
            return false;
        }
        return result;
    }

    @Override
    public Boolean unrecordList(List<UUID> repositoryLedgerID) {
        boolean result = true;
        try {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("DELETE FROM RepositoryLedger where ReferenceID IN :referenceID");
            params.put("referenceID", repositoryLedgerID);
            Query query = entityManager.createNativeQuery(sql.toString());
            Common.setParams(query, params);
            query.executeUpdate();
        }catch (Exception ex){
            return false;
        }
        return result;
    }

    @Override
    public List<LotNoDTO> getListLotNoByMaterialGoodsID(UUID materialGoodsID, UUID companyID) {
        List<LotNoDTO> viewLotNos = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM RepositoryLedger ");
        sql.append(" where IWQuantity > 0 AND LotNo IS NOT NULL AND CompanyID = :org ");
        params.put("org", companyID);
        sql.append("AND MaterialGoodsID = :mid ");
        params.put("mid", materialGoodsID);
        sql.append("GROUP BY LotNo, ExpiryDate, CompanyID, MaterialGoodsID");
        String selectQuery = "SELECT companyID, materialGoodsID, lotNo, expiryDate, ISNULL(SUM(IWQuantity), 0) AS totalIWQuantity," +
            " ISNULL(SUM(OWQuantity), 0) AS totalOWQuantity," +
            " SUM(ISNULL(IWQuantity, 0) - ISNULL(OWQuantity,0)) AS totalQuantityBalance ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), "LotNoDTOSA");
        Common.setParams(query, params);
        viewLotNos = query.getResultList();
        return viewLotNos;
    }

    @Override
    public List<CalculateOWPriceDTO> calculateOWPrice(List<UUID> materialGoods, String fromDate, String toDate, Boolean isByRepository, UUID org, Integer soLamViec, Integer lamTronTienVN) {
        List<CalculateOWPriceDTO> calculateOWPriceDTOs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (isByRepository) {
            sql.append("SELECT rl.MaterialGoodsID," +
                "              Amount   = ((SELECT ISNULL(SUM(ROUND((ISNULL(rl1.IWAmount, 0)), :round) - " +
                "                                      ROUND((ISNULL(rl1.OWAmount, 0)), :round)), 0) " +
                "                    FROM RepositoryLedger rl1 " +
                "                    WHERE rl1.MaterialGoodsID = rl.MaterialGoodsID " +
                "                      AND rl1.RepositoryID = rl.RepositoryID " +
                "                      AND (rl1.TypeLedger = :typeLedger OR rl1.TypeLedger = 2) " +
                "                      AND rl1.PostedDate < :fromDate)" +
                "                       + (SELECT ISNULL(SUM(ROUND((ISNULL(rl2.IWAmount, 0)), :round)), 0) " +
                "                          FROM RepositoryLedger rl2 " +
                "                          WHERE rl2.MaterialGoodsID = rl.MaterialGoodsID " +
                "                            AND rl2.RepositoryID = rl.RepositoryID " +
                "                            AND (rl2.TypeLedger = :typeLedger OR rl2.TypeLedger = 2) " +
                "                            AND rl2.PostedDate >= :fromDate " +
                "                            AND rl2.PostedDate <= :toDate) " +
                "           - (SELECT ISNULL(SUM(ROUND((ISNULL(ppd.Amount, 0)), :round)), 0) " +
                "              FROM PPDiscountReturnDetail ppd" +
                "                       LEFT JOIN PPDiscountReturn pp ON ppd.PPDiscountReturnID = pp.ID " +
                "              WHERE ppd.MaterialGoodsID = rl.MaterialGoodsID " +
                "                AND ppd.RepositoryID = rl.RepositoryID " +
                "                AND (pp.TypeLedger = :typeLedger OR pp.TypeLedger = 2) " +
                "                AND pp.Recorded = 1 " +
                "                AND pp.TypeID = 230 " +
                "                AND pp.PostedDate >= :fromDate " +
                "                AND pp.PostedDate <= :toDate))," +
                "       Quantity = ((SELECT ISNULL(SUM(ISNULL(rl3.MainIWQuantity, 0) - ISNULL(rl3.MainOWQuantity, 0)), 0) " +
                "                   FROM RepositoryLedger rl3 " +
                "                   WHERE rl3.MaterialGoodsID = rl.MaterialGoodsID AND rl3.RepositoryID = rl.RepositoryID AND (rl3.TypeLedger = :typeLedger OR rl3.TypeLedger = 2) AND rl3.PostedDate < :fromDate) " +
                "           + (SELECT ISNULL(SUM(ISNULL(rl4.MainIWQuantity, 0)), 0) " +
                "              FROM RepositoryLedger rl4 " +
                "              WHERE rl4.MaterialGoodsID = rl.MaterialGoodsID AND rl4.RepositoryID = rl.RepositoryID AND (rl4.TypeLedger = :typeLedger OR rl4.TypeLedger = 2) AND rl4.PostedDate >= :fromDate AND rl4.PostedDate <= :toDate)), " +
                "       rl.RepositoryID " +
                "FROM RepositoryLedger rl " +
                "WHERE rl.RepositoryID IS NOT NULL AND rl.MaterialGoodsID in :mid AND (rl.TypeLedger = :typeLedger OR rl.TypeLedger = 2)");
            params.put("round", lamTronTienVN);
            params.put("mid", materialGoods);
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("typeLedger", soLamViec);
            sql.append("GROUP BY MaterialGoodsID, RepositoryID");
        } else {
            sql.append("SELECT rl.MaterialGoodsID," +
                "       Amount  = ((SELECT ISNULL(SUM(ROUND((ISNULL(rl1.IWAmount, 0)), :round) - " +
                "                    ROUND((ISNULL(rl1.OWAmount, 0)), :round)), 0) " +
                "                   FROM RepositoryLedger rl1 " +
                "                   WHERE rl1.MaterialGoodsID = rl.MaterialGoodsID AND (rl1.TypeLedger = :typeLedger OR rl1.TypeLedger = 2) AND rl1.PostedDate < :fromDate) " +
                "           + (SELECT ISNULL(SUM(ROUND((ISNULL(rl2.IWAmount, 0)), :round)), 0) " +
                "              FROM RepositoryLedger rl2 " +
                "              WHERE rl2.MaterialGoodsID = rl.MaterialGoodsID AND (rl2.TypeLedger = :typeLedger OR rl2.TypeLedger = 2) AND rl2.PostedDate >= :fromDate AND rl2.PostedDate <= :toDate)" +
                "           - (SELECT ISNULL(SUM(ROUND((ISNULL(ppd.Amount, 0)), :round)), 0) " +
                "              FROM PPDiscountReturnDetail ppd " +
                "           LEFT JOIN PPDiscountReturn pp ON ppd.PPDiscountReturnID = pp.ID " +
                "              WHERE ppd.MaterialGoodsID = rl.MaterialGoodsID " +
                "                AND (pp.TypeLedger = :typeLedger OR pp.TypeLedger = 2)" +
                "                AND pp.Recorded = 1 " +
                "                AND pp.TypeID = 230 " +
                "                AND pp.PostedDate >= :fromDate " +
                "                AND pp.PostedDate <= :toDate)), " +
                "       Quantity = ((SELECT ISNULL(SUM(ISNULL(rl3.MainIWQuantity, 0) - ISNULL(rl3.MainOWQuantity, 0)), 0) " +
                "                   FROM RepositoryLedger rl3 " +
                "                   WHERE rl3.MaterialGoodsID = rl.MaterialGoodsID AND (rl3.TypeLedger = :typeLedger OR rl3.TypeLedger = 2) AND rl3.PostedDate < :fromDate) " +
                "           + (SELECT ISNULL(SUM(ISNULL(rl4.MainIWQuantity, 0)), 0) " +
                "              FROM RepositoryLedger rl4 " +
                "              WHERE rl4.MaterialGoodsID = rl.MaterialGoodsID AND (rl4.TypeLedger = :typeLedger OR rl4.TypeLedger = 2) AND rl4.PostedDate >= :fromDate AND rl4.PostedDate <= :toDate)), " +
                "        RepositoryID = NULL " +
                "FROM RepositoryLedger rl " +
                "WHERE rl.MaterialGoodsID in :mid AND (rl.TypeLedger = :typeLedger OR rl.TypeLedger = 2)");
            params.put("round", lamTronTienVN);
            params.put("mid", materialGoods);
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("typeLedger", soLamViec);
            sql.append("GROUP BY MaterialGoodsID");
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "CalculateOWPriceDTO");
        Common.setParams(query, params);
        calculateOWPriceDTOs = query.getResultList();
        return calculateOWPriceDTOs;
    }

    @Override
    public Boolean updateOwPrice(List<CalculateOWPriceDTO> calculateOWPriceDTOs, String fromDate, String toDate, Integer lamTronDonGia, Integer lamTronDonGiaNT, Integer lamTronTienVN, Integer lamTronTienNT) {
        for (int i = 0; i < calculateOWPriceDTOs.size(); i++) {
            BigDecimal oWPrice = BigDecimal.ZERO;
            if (calculateOWPriceDTOs.get(i).getQuantity().doubleValue() != 0) {
                oWPrice = Utils.round((calculateOWPriceDTOs.get(i).getAmount().divide(calculateOWPriceDTOs.get(i).getQuantity(), MathContext.DECIMAL64)), lamTronDonGia);
            }
            List<RepositoryLedger> repositoryLedgerList = getByMaterialGoodsAndPostedDate(calculateOWPriceDTOs.get(i).getMaterialGoodsID(), fromDate, toDate, calculateOWPriceDTOs.get(i).getRepositoryID());
            double sumOutQuantity = 0;
            sumOutQuantity = repositoryLedgerList.stream().filter(x -> x.getMainOWQuantity().compareTo(BigDecimal.ZERO) > 0).mapToDouble(x -> x.getMainOWQuantity().doubleValue()).sum();
            if (repositoryLedgerList.size() > 0) {
                String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, OWAmount = ?, MainUnitPrice = ?  WHERE DetailID = ? AND OWQuantity > 0;";
                BigDecimal finalOWPrice = oWPrice;
                int[][] ints = jdbcTemplate.batchUpdate(sql, repositoryLedgerList, Constants.BATCH_SIZE, (ps, detail) -> {
                    ps.setBigDecimal(1, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(2, Utils.round((detail.getOwQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(3, finalOWPrice);
                    ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());

                });
            }
            List<SAInvoiceDetails> saInvoiceDetails = getSAInvoiceDetailByMaterialGoodsAndPostedDate(calculateOWPriceDTOs.get(i).getMaterialGoodsID(), fromDate, toDate, calculateOWPriceDTOs.get(i).getRepositoryID());
            if (saInvoiceDetails.size() > 0) {
                String sql = "UPDATE SAInvoiceDetail SET OWPrice = ?, OWAmount = ?  WHERE id = ?;" +
                    "UPDATE SAInvoice SET TotalCapitalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?) WHERE id = ?;" +
                    "UPDATE RSInwardOutward SET TotalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?), " +
                    "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?)/ExchangeRate WHERE id = " +
                    "(SELECT TOP 1 RSInwardOutwardID FROM SAInvoice WHERE ID = ?);" +
                    "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?/ExchangeRate  WHERE Account = 632 AND DetailID = ?;" +
                    "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?/ExchangeRate  WHERE AccountCorresponding = 632 AND DetailID = ?;";
                BigDecimal finalOWPrice = oWPrice;
                int[][] ints = jdbcTemplate.batchUpdate(sql, saInvoiceDetails, Constants.BATCH_SIZE, (ps, detail) -> {
                    ps.setBigDecimal(1, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(2, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(3, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setString(4, Utils.uuidConvertToGUID(detail.getsAInvoiceId()).toString());
                    ps.setString(5, Utils.uuidConvertToGUID(detail.getsAInvoiceId()).toString());
                    ps.setString(6, Utils.uuidConvertToGUID(detail.getsAInvoiceId()).toString());
                    ps.setString(7, Utils.uuidConvertToGUID(detail.getsAInvoiceId()).toString());
                    ps.setString(8, Utils.uuidConvertToGUID(detail.getsAInvoiceId()).toString());
                    ps.setBigDecimal(9, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(10, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(11, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setBigDecimal(12, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(13, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(14, Utils.uuidConvertToGUID(detail.getId()).toString());
                });
                List<UUID> ids = saInvoiceDetails.stream().map(SAInvoiceDetails::getId).collect(Collectors.toList());
                if (ids.size() > 0) {
                    List<SaReturnDetails> saReturnDetails = getSaReturnDetailByMaterialGoodsAndPostedDate(ids);
                    if (saReturnDetails.size() > 0) {
                        String sqlUpdate = "UPDATE SAReturnDetail SET OWPrice = ?, OWAmount = ?  WHERE id = ?;" +
                            "UPDATE SAReturn SET TotalOWAmount = (SELECT SUM(OWAmount) FROM SAReturnDetail WHERE SAReturnID = ?) WHERE id = ?;" +
                            "UPDATE RSInwardOutward SET TotalAmount = (SELECT SUM(OWAmount) FROM SAReturnDetail WHERE SAReturnID = ?), " +
                            "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM SAReturnDetail WHERE SAReturnID = ?)/ExchangeRate WHERE id = " +
                            "(SELECT TOP 1 RSInwardOutwardID FROM SAReturn WHERE ID = ?);" +
                            "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?/ExchangeRate  WHERE Account = 632 AND DetailID = ?;" +
                            "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?/ExchangeRate  WHERE AccountCorresponding = 632 AND DetailID = ?;" +
                            "UPDATE RepositoryLedger SET UnitPrice = ?, IWAmount = ?, MainUnitPrice = ?  WHERE DetailID = ?";
                        int[][] intUpdates = jdbcTemplate.batchUpdate(sqlUpdate, saReturnDetails, Constants.BATCH_SIZE, (ps, detail) -> {
                            ps.setBigDecimal(1, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                            ps.setBigDecimal(2, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setString(3, Utils.uuidConvertToGUID(detail.getId()).toString());
                            ps.setString(4, Utils.uuidConvertToGUID(detail.getSaReturnID()).toString());
                            ps.setString(5, Utils.uuidConvertToGUID(detail.getSaReturnID()).toString());
                            ps.setString(6, Utils.uuidConvertToGUID(detail.getSaReturnID()).toString());
                            ps.setString(7, Utils.uuidConvertToGUID(detail.getSaReturnID()).toString());
                            ps.setString(8, Utils.uuidConvertToGUID(detail.getSaReturnID()).toString());
                            ps.setBigDecimal(9, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setBigDecimal(10, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setString(11, Utils.uuidConvertToGUID(detail.getId()).toString());
                            ps.setBigDecimal(12, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setBigDecimal(13, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setString(14, Utils.uuidConvertToGUID(detail.getId()).toString());
                            ps.setBigDecimal(15, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                            ps.setBigDecimal(16, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                            ps.setBigDecimal(17, finalOWPrice);
                            ps.setString(18, Utils.uuidConvertToGUID(detail.getId()).toString());
                        });
                    }
                }
            }
            List<RSInwardOutWardDetails> rsInwardOutWardDetails = getRSInwardOutwardDetailByMaterialGoodsAndPostedDate(calculateOWPriceDTOs.get(i).getMaterialGoodsID(), fromDate, toDate, calculateOWPriceDTOs.get(i).getRepositoryID());
            if (rsInwardOutWardDetails.size() > 0) {
                String sql = "UPDATE RSInwardOutWardDetail SET UnitPrice = ?, UnitPriceOriginal = ?, MainUnitPrice = ?, Amount = ?, AmountOriginal = ?  WHERE id = ?;" +
                    "UPDATE RSInwardOutWard SET TotalAmount = (SELECT SUM(Amount) FROM RSInwardOutWardDetail WHERE RSInwardOutWardID = ?), " +
                    "TotalAmountOriginal = (SELECT SUM(Amount) FROM RSInwardOutWardDetail WHERE RSInwardOutWardID = ?) WHERE id = ?;" +
                    "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                    "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
                BigDecimal finalOWPrice = oWPrice;
                int[][] ints = jdbcTemplate.batchUpdate(sql, rsInwardOutWardDetails, Constants.BATCH_SIZE, (ps, detail) -> {
                    ps.setBigDecimal(1, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(2, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(3, finalOWPrice);
                    ps.setBigDecimal(4, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(5, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(6, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setString(7, Utils.uuidConvertToGUID(detail.getRsInwardOutwardID()).toString());
                    ps.setString(8, Utils.uuidConvertToGUID(detail.getRsInwardOutwardID()).toString());
                    ps.setString(9, Utils.uuidConvertToGUID(detail.getRsInwardOutwardID()).toString());
                    ps.setBigDecimal(10, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(11, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(12, detail.getDebitAccount());
                    ps.setString(13, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setBigDecimal(14, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(15, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(16, detail.getDebitAccount());
                    ps.setString(17, Utils.uuidConvertToGUID(detail.getId()).toString());
                });
            }
            List<RSTransferDetailsDTO> rsTransferDetailsDTOS = getRSTransferDetailByMaterialGoodsAndPostedDate(calculateOWPriceDTOs.get(i).getMaterialGoodsID(), fromDate, toDate, calculateOWPriceDTOs.get(i).getRepositoryID());
            if (rsTransferDetailsDTOS.size() > 0) {
                String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, IWAmount = ?, MainUnitPrice = ?  WHERE DetailID = ? AND IWQuantity > 0;" +
                    "UPDATE RSTransferDetail SET OWPrice = ?, OWAmount = ? WHERE ID = ?;" +
                    "UPDATE RSTransfer SET TotalAmount = (SELECT SUM(OWAmount) FROM RSTransferDetail WHERE RSTransferID = ?), " +
                    "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM RSTransferDetail WHERE RSTransferID = ?) WHERE ID = ?;" +
                    "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                    "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
                BigDecimal finalOWPrice = oWPrice;
                int[][] ints = jdbcTemplate.batchUpdate(sql, rsTransferDetailsDTOS, Constants.BATCH_SIZE, (ps, detail) -> {
                    ps.setBigDecimal(1, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(2, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(3, finalOWPrice);
                    ps.setString(4, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setBigDecimal(5, (detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)));
                    ps.setBigDecimal(6, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(7, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setString(8, Utils.uuidConvertToGUID(detail.getRsTransferID()).toString());
                    ps.setString(9, Utils.uuidConvertToGUID(detail.getRsTransferID()).toString());
                    ps.setString(10, Utils.uuidConvertToGUID(detail.getRsTransferID()).toString());
                    ps.setBigDecimal(11, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(12, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(13, detail.getDebitAccount());
                    ps.setString(14, Utils.uuidConvertToGUID(detail.getId()).toString());
                    ps.setBigDecimal(15, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setBigDecimal(16, Utils.round((detail.getQuantity().multiply((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(finalOWPrice) : finalOWPrice.divide(detail.getMainConvertRate(), MathContext.DECIMAL64)))), lamTronTienVN));
                    ps.setString(17, detail.getDebitAccount());
                    ps.setString(18, Utils.uuidConvertToGUID(detail.getId()).toString());
                });
            }
            if (sumOutQuantity == calculateOWPriceDTOs.get(i).getQuantity().doubleValue() && repositoryLedgerList.size() > 0) {
                BigDecimal lastAmount = calculateOWPriceDTOs.get(i).getAmount();
                for (int j = 0; j < repositoryLedgerList.size() - 1; j++) {
                    lastAmount = lastAmount.subtract(Utils.round((repositoryLedgerList.get(j).getOwQuantity().multiply((repositoryLedgerList.get(j).getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? repositoryLedgerList.get(j).getMainConvertRate().multiply(oWPrice) : repositoryLedgerList.get(j).getMainConvertRate().divide(oWPrice)))), lamTronTienVN));
                }
                List<RepositoryLedger> newRepositoryLedgers = new ArrayList<>();
                newRepositoryLedgers.add(repositoryLedgerList.get(repositoryLedgerList.size() - 1));
                if (repositoryLedgerList.get(repositoryLedgerList.size() - 1).getTypeID().compareTo(TypeConstant.XUAT_KHO_TU_BAN_HANG) == 0) {
                    String sql = "UPDATE RepositoryLedger SET OWAmount = ? WHERE DetailID = ?;"+
                        "UPDATE SAInvoiceDetail SET OWAmount = ?  WHERE id = ?;" +
                        "UPDATE SAInvoice SET TotalCapitalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?) WHERE id = ?;" +
                        "UPDATE RSInwardOutward SET TotalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?), " +
                        "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?)/ExchangeRate WHERE id = " +
                        "(SELECT TOP 1 RSInwardOutwardID FROM SAInvoice WHERE ID = ?);" +
                        "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?/ExchangeRate  WHERE Account = 632 AND DetailID = ?;" +
                        "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?/ExchangeRate  WHERE AccountCorresponding = 632 AND DetailID = ?;";
                    BigDecimal finalLastAmount = lastAmount;
                    int[][] ints = jdbcTemplate.batchUpdate(sql, newRepositoryLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
                        ps.setBigDecimal(1, finalLastAmount);
                        ps.setString(2, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(3, finalLastAmount);
                        ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setString(5, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(6, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(7, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(8, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(9, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setBigDecimal(10, finalLastAmount);
                        ps.setBigDecimal(11, finalLastAmount);
                        ps.setString(12, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(13, finalLastAmount);
                        ps.setBigDecimal(14, finalLastAmount);
                        ps.setString(15, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                    });
                } else if (repositoryLedgerList.get(repositoryLedgerList.size() - 1).getTypeID().compareTo(TypeConstant.XUAT_KHO) == 0) {
                    String sql = "UPDATE RepositoryLedger SET OWAmount = ?  WHERE DetailID = ?;"+
                        "UPDATE RSInwardOutWardDetail SET Amount = ?, AmountOriginal = ?  WHERE id = ?;" +
                        "UPDATE RSInwardOutWard SET TotalAmount = (SELECT SUM(Amount) FROM RSInwardOutWardDetail WHERE RSInwardOutWardID = ?) WHERE id = ?;" +
                        "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                        "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
                    BigDecimal finalLastAmount = lastAmount;
                    int[][] ints = jdbcTemplate.batchUpdate(sql, newRepositoryLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
                        ps.setBigDecimal(1, finalLastAmount);
                        ps.setString(2, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(3, finalLastAmount);
                        ps.setBigDecimal(4, finalLastAmount);
                        ps.setString(5, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setString(6, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(7, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setBigDecimal(8, finalLastAmount);
                        ps.setBigDecimal(9, finalLastAmount);
                        ps.setString(10, detail.getAccountCorresponding());
                        ps.setString(11, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(12, finalLastAmount);
                        ps.setBigDecimal(13, finalLastAmount);
                        ps.setString(14, detail.getAccountCorresponding());
                        ps.setString(15, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                    });
                } else if (repositoryLedgerList.get(repositoryLedgerList.size() - 1).getTypeID().equals(TypeConstant.CHUYEN_KHO) ||
                    repositoryLedgerList.get(repositoryLedgerList.size() - 1).getTypeID().equals(TypeConstant.CHUYEN_KHO_GUI_DAI_LY) ||
                    repositoryLedgerList.get(repositoryLedgerList.size() - 1).getTypeID().equals(TypeConstant.CHUYEN_KHO_CHUYEN_NOI_BO)) {
                    String sql = "UPDATE RepositoryLedger SET OWAmount = ?  WHERE DetailID = ? AND OWQuantity > 0;" +
                        "UPDATE RepositoryLedger SET IWAmount = ?  WHERE DetailID = ? AND IWQuantity > 0;"+
                        "UPDATE RSTransferDetail SET Amount = ?, AmountOriginal = ?  WHERE id = ?;" +
                        "UPDATE RSTransfer SET TotalAmount = (SELECT SUM(OWAmount) FROM RSTransferDetail WHERE RSTransferID = ?), " +
                        "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM RSTransferDetail WHERE RSTransferID = ?) WHERE id = ?;" +
                        "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                        "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
                    BigDecimal finalLastAmount = lastAmount;
                    int[][] ints = jdbcTemplate.batchUpdate(sql, newRepositoryLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
                        ps.setBigDecimal(1, finalLastAmount);
                        ps.setString(2, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(3, finalLastAmount);
                        ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(5, finalLastAmount);
                        ps.setBigDecimal(6, finalLastAmount);
                        ps.setString(7, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setString(8, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(9, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setString(10, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                        ps.setBigDecimal(12, finalLastAmount);
                        ps.setBigDecimal(13, finalLastAmount);
                        ps.setString(14, detail.getAccountCorresponding());
                        ps.setString(15, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                        ps.setBigDecimal(16, finalLastAmount);
                        ps.setBigDecimal(17, finalLastAmount);
                        ps.setString(18, detail.getAccountCorresponding());
                        ps.setString(19, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                    });
                }
            }
        }
        return true;
    }

    @Override
    public Boolean updateOwPricePP2(List<RepositoryLedger> listRepositoryLedgersOW, String fromDate, String toDate, Integer lamTronDonGia, Integer lamTronDonGiaNT, Integer lamTronTienVN, Integer lamTronTienNT) {
        if (listRepositoryLedgersOW.size() > 0) {
            String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, OWAmount = ?, MainUnitPrice = ?  WHERE DetailID = ?;";
            int[][] ints = jdbcTemplate.batchUpdate(sql, listRepositoryLedgersOW, Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setBigDecimal(1, Utils.round((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(detail.getMainUnitPrice()) : detail.getMainUnitPrice().divide(detail.getMainConvertRate(), MathContext.DECIMAL64)), lamTronDonGia));
                ps.setBigDecimal(2, Utils.round(detail.getOwAmount(), lamTronTienVN));
                ps.setBigDecimal(3, Utils.round(detail.getMainUnitPrice(), lamTronDonGia));
                ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
            });
        }
        return true;
    }

    @Override
    public List<RepositoryLedgerDTO> getListRepositoryError(String fromDate, String toDate, UUID org, Integer soLamViec) {
        List<RepositoryLedgerDTO> repositoryLedgerList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select rl.ReferenceID, rl.NoFBook, rl.NoMBook, rl.PostedDate, rl.Date, rl.TypeID, rl.Reason " +
            "from RepositoryLedger rl " +
            "where rl.PostedDate >= :fromDate " +
            "  and rl.PostedDate <= :toDate " +
            "  and rl.CompanyID = :org " +
            "  and (rl.TypeLedger = :soLamViec or rl.TypeLedger = 2) " +
            "  and rl.TypeID in (410, 411, 413) " +
            "  and (rl.OWQuantity is null " +
            "  or rl.MainOWQuantity is null " +
            "  or rl.Account is null or rl.Account = '') " +
            "group by rl.ReferenceID, rl.NoFBook, rl.NoMBook, rl.PostedDate, rl.Date, rl.TypeID, rl.Reason order By PostedDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("org", org);
        params.put("soLamViec", soLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "RepositoryLedgerDTO");
        Common.setParams(query, params);
        repositoryLedgerList = query.getResultList();
        return repositoryLedgerList;
    }

    @Override
    public List<RepositoryLedgerDTO> getByCostSetID(List<UUID> collect, String fromDate, String toDate) {
        List<RepositoryLedgerDTO> repositoryLedgerDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT CostSetID, null  MaterialGoodsID, SUM(ISNULL(MainIWQuantity, 0)) AS Quantity FROM RepositoryLedger WHERE TypeID = 400 " +
            "AND Account LIKE '155%' AND AccountCorresponding LIKE '154%' AND CostSetID IN :ids AND PostedDate >= :fromDate AND PostedDate <= :toDate " +
            "GROUP BY CostSetID");
        params.put("ids", collect);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "QuantityCompleteDTO");
        Common.setParams(query, params);
        repositoryLedgerDTOS = query.getResultList();
        return repositoryLedgerDTOS;
    }

    public List<RepositoryLedger> getByMaterialGoodsAndPostedDate(UUID materialGoodsID, String fromDate, String toDate, UUID repositoryID) {
        List<RepositoryLedger> repositoryLedgerList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM RepositoryLedger ");
        sql.append(" where OWQuantity > 0 AND TypeID in (410, 411, 413, 420, 421, 422) AND MaterialGoodsID = :mid AND PostedDate >= :fromDate AND PostedDate <= :toDate ");
        params.put("mid", materialGoodsID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        if (repositoryID != null) {
            sql.append(" AND RepositoryID = :repositoryID");
            params.put("repositoryID", repositoryID);
        }
        sql.append(" Order By PostedDate");
        Query query = entityManager.createNativeQuery(sql.toString(), RepositoryLedger.class);
        Common.setParams(query, params);
        repositoryLedgerList = query.getResultList();
        return repositoryLedgerList;
    }

    public List<SAInvoiceDetails> getSAInvoiceDetailByMaterialGoodsAndPostedDate(UUID materialGoodsID, String fromDate, String toDate, UUID repositoryID) {
        List<SAInvoiceDetails> saInvoiceDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM SAInvoiceDetail LEFT JOIN SAInvoice ON SAInvoiceID = SAInvoice.ID ");
        sql.append(" where SAInvoice.IsDeliveryVoucher = 1 AND MaterialGoodsID = :mid AND SAInvoice.Recorded = 1 AND SAInvoice.PostedDate >= :fromDate AND SAInvoice.PostedDate <= :toDate ");
        params.put("mid", materialGoodsID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        if (repositoryID != null) {
            sql.append(" AND RepositoryID = :repositoryID");
            params.put("repositoryID", repositoryID);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), SAInvoiceDetails.class);
        Common.setParams(query, params);
        saInvoiceDetails = query.getResultList();
        return saInvoiceDetails;
    }

    public List<RSInwardOutWardDetails> getRSInwardOutwardDetailByMaterialGoodsAndPostedDate(UUID materialGoodsID, String fromDate, String toDate, UUID repositoryID) {
        List<RSInwardOutWardDetails> rsInwardOutWardDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM RSInwardOutWardDetail LEFT JOIN RSInwardOutWard ON RSInwardOutWardID = RSInwardOutWard.ID ");
        sql.append(" where TypeID LIKE '41%' AND MaterialGoodsID = :mid AND RSInwardOutWard.Recorded = 1 AND RSInwardOutWard.PostedDate >= :fromDate AND RSInwardOutWard.PostedDate <= :toDate ");
        params.put("mid", materialGoodsID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        if (repositoryID != null) {
            sql.append(" AND RepositoryID = :repositoryID");
            params.put("repositoryID", repositoryID);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), RSInwardOutWardDetails.class);
        Common.setParams(query, params);
        rsInwardOutWardDetails = query.getResultList();
        return rsInwardOutWardDetails;
    }

    public List<SaReturnDetails> getSaReturnDetailByMaterialGoodsAndPostedDate(List<UUID> listSAInvoiceDetailID) {
        List<SaReturnDetails> saReturnDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM SAReturnDetail LEFT JOIN SAReturn ON SAReturnID = SAReturn.ID ");
        sql.append(" where SAInvoiceDetailID in :mid AND SAReturn.Recorded = 1 AND SAReturn.AutoOWAmountCal = 0");
        params.put("mid", listSAInvoiceDetailID);
        Query query = entityManager.createNativeQuery(sql.toString(), SaReturnDetails.class);
        Common.setParams(query, params);
        saReturnDetails = query.getResultList();
        return saReturnDetails;
    }

    public List<RSTransferDetailsDTO> getRSTransferDetailByMaterialGoodsAndPostedDate(UUID materialGoodsID, String fromDate, String toDate, UUID repositoryID) {
        List<RSTransferDetailsDTO> rsTransferDetailsDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT rsd.ID, rsd.RSTransferID, rsd.MaterialGoodsID, rsd.DebitAccount, rsd.CreditAccount, rsd.Quantity, rsd.UnitPriceOriginal, rsd.OWPrice, " +
            "       rsd.OWAmount, rsd.FromRepositoryID, rsd.ToRepositoryID, rsd.UnitID, rsd.MainUnitID, rsd.MainQuantity, rsd.MainUnitPrice, rsd.MainConvertRate, " +
            "       rsd.Formula " +
            "FROM RSTransferDetail rsd " +
            "         LEFT JOIN RSTransfer rs ON rsd.RSTransferID = rs.ID " +
            "WHERE rs.Recorded = 1 " +
            "AND rs.PostedDate <= :toDate AND rs.PostedDate >= :fromDate AND rsd.MaterialGoodsID = :mid ");
        params.put("mid", materialGoodsID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        if (repositoryID != null) {
            sql.append(" AND FromRepositoryID = :repositoryID");
            params.put("repositoryID", repositoryID);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "RSTransferDetailsDTO1");
        Common.setParams(query, params);
        rsTransferDetailsDTOS = query.getResultList();
        return rsTransferDetailsDTOS;
    }

    @Override
    public Boolean updateIWPriceFromCost(List<RSInwardOutwardDetailsDTO> rsInwardOutWardDetails, UUID org, Integer soLamViec) {
        if (rsInwardOutWardDetails.size() > 0) {
            String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, MainUnitPrice = ?, IWAmount = ? WHERE DetailID = ?;" +
                "UPDATE RSInwardOutWardDetail SET UnitPrice = ?, UnitPriceOriginal = ?, MainUnitPrice = ?, Amount = ?, AmountOriginal = ?  WHERE id = ?;" +
                "UPDATE RSInwardOutWard SET TotalAmount = (SELECT SUM(Amount) FROM RSInwardOutWardDetail WHERE RSInwardOutWardID = ?) WHERE id = ?;" +
                "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
            int[][] ints = jdbcTemplate.batchUpdate(sql, rsInwardOutWardDetails, Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setBigDecimal(1, detail.getUnitPrice());
                ps.setBigDecimal(2, detail.getMainUnitPrice());
                ps.setBigDecimal(3, detail.getAmount());
                ps.setString(4, Utils.uuidConvertToGUID(detail.getId()).toString());
                ps.setBigDecimal(5, detail.getUnitPrice());
                ps.setBigDecimal(6, detail.getUnitPrice());
                ps.setBigDecimal(7, detail.getMainUnitPrice());
                ps.setBigDecimal(8, detail.getAmount());
                ps.setBigDecimal(9, detail.getAmount());
                ps.setString(10, Utils.uuidConvertToGUID(detail.getId()).toString());
                ps.setString(11, Utils.uuidConvertToGUID(detail.getRsInwardOutwardID()).toString());
                ps.setString(12, Utils.uuidConvertToGUID(detail.getRsInwardOutwardID()).toString());
                ps.setBigDecimal(13, detail.getAmount());
                ps.setBigDecimal(14, detail.getAmount());
                ps.setString(15, detail.getDebitAccount());
                ps.setString(16, Utils.uuidConvertToGUID(detail.getId()).toString());
                ps.setBigDecimal(17, detail.getAmount());
                ps.setBigDecimal(18, detail.getAmount());
                ps.setString(19, detail.getDebitAccount());
                ps.setString(20, Utils.uuidConvertToGUID(detail.getId()).toString());
            });
        }
        return true;
    }

    @Override
    public List<RepositoryLedger> getListUpdateUnitPrice(List<UUID> collect, Integer soLamViec, LocalDate fromDate, LocalDate toDate) {
        StringBuilder sql = new StringBuilder();
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select * " +
            "from RepositoryLedger rl " +
            "where rl.MaterialGoodsID in :ids " +
            "  and rl.TypeID in (410, 411, 413) " +
            "  and (rl.TypeLedger = :soLamViec or rl.TypeLedger = 2) " +
            "  and rl.PostedDate >= :fromDate " +
            "  and rl.PostedDate <= :toDate");
        params.put("ids", collect);
        params.put("soLamViec", soLamViec);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), RepositoryLedger.class);
        Common.setParams(query, params);
        repositoryLedgers = query.getResultList();
        return repositoryLedgers;
    }

    @Override
    public Boolean updateOWPriceFromCost(List<RepositoryLedger> repositoryLedgersRS, List<RepositoryLedger> repositoryLedgersSA, List<RepositoryLedger> repositoryLedgersAD, UUID org, Integer soLamViec) {
        if (repositoryLedgersSA.size() > 0) {
            String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, MainUnitPrice = ?, IWAmount = ? WHERE DetailID = ?;" +
                "UPDATE SAInvoiceDetail SET OWPrice = ?, OWAmount = ?  WHERE id = ?;" +
                "UPDATE SAInvoice SET TotalCapitalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?) WHERE id = ?;" +
                "UPDATE RSInwardOutward SET TotalAmount = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?), " +
                "TotalAmountOriginal = (SELECT SUM(OWAmount) FROM SAInvoiceDetail WHERE SAInvoiceID = ?)/ExchangeRate WHERE id = " +
                "(SELECT TOP 1 RSInwardOutwardID FROM SAInvoice WHERE ID = ?);" +
                "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?/ExchangeRate  WHERE Account = 632 AND DetailID = ?;" +
                "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?/ExchangeRate  WHERE AccountCorresponding = 632 AND DetailID = ?;";
            int[][] ints = jdbcTemplate.batchUpdate(sql, repositoryLedgersSA, Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setBigDecimal(1, detail.getUnitPrice());
                ps.setBigDecimal(2, detail.getMainUnitPrice());
                ps.setBigDecimal(3, detail.getOwAmount());
                ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                ps.setBigDecimal(5, detail.getUnitPrice());
                ps.setBigDecimal(6, detail.getOwAmount());
                ps.setString(7, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                ps.setString(8, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setString(9, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setString(10, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setString(11, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setString(12, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setBigDecimal(13, detail.getOwAmount());
                ps.setBigDecimal(14, detail.getOwAmount());
                ps.setString(15, Utils.uuidConvertToGUID(detail.getId()).toString());
                ps.setBigDecimal(16, detail.getOwAmount());
                ps.setBigDecimal(17, detail.getOwAmount());
                ps.setString(18, Utils.uuidConvertToGUID(detail.getId()).toString());
            });
        }
        if (repositoryLedgersRS.size() > 0) {
            String sql = "UPDATE RepositoryLedger SET UnitPrice = ?, MainUnitPrice = ?, IWAmount = ? WHERE DetailID = ?;" +
                "UPDATE RSInwardOutWardDetail SET UnitPrice = ?, UnitPriceOriginal = ?, MainUnitPrice = ?, Amount = ?, AmountOriginal = ?  WHERE id = ?;" +
                "UPDATE RSInwardOutWard SET TotalAmount = (SELECT SUM(Amount) FROM RSInwardOutWardDetail WHERE RSInwardOutWardID = ?) WHERE id = ?;" +
                "UPDATE GeneralLedger SET DebitAmount = ?, DebitAmountOriginal = ?  WHERE Account = ? AND DetailID = ?;" +
                "UPDATE GeneralLedger SET CreditAmount = ?, CreditAmountOriginal = ?  WHERE AccountCorresponding = ? AND DetailID = ?;";
            int[][] ints = jdbcTemplate.batchUpdate(sql, repositoryLedgersRS, Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setBigDecimal(1, detail.getUnitPrice());
                ps.setBigDecimal(2, detail.getMainUnitPrice());
                ps.setBigDecimal(3, detail.getOwAmount());
                ps.setString(4, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                ps.setBigDecimal(5, detail.getUnitPrice());
                ps.setBigDecimal(6, detail.getUnitPrice());
                ps.setBigDecimal(7, detail.getMainUnitPrice());
                ps.setBigDecimal(8, detail.getOwAmount());
                ps.setBigDecimal(9, detail.getOwAmount());
                ps.setString(10, Utils.uuidConvertToGUID(detail.getDetailID()).toString());
                ps.setString(11, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setString(12, Utils.uuidConvertToGUID(detail.getReferenceID()).toString());
                ps.setBigDecimal(13, detail.getOwAmount());
                ps.setBigDecimal(14, detail.getOwAmount());
                ps.setString(15, detail.getAccount());
                ps.setString(16, Utils.uuidConvertToGUID(detail.getId()).toString());
                ps.setBigDecimal(17, detail.getOwAmount());
                ps.setBigDecimal(18, detail.getOwAmount());
                ps.setString(19, detail.getAccount());
                ps.setString(20, Utils.uuidConvertToGUID(detail.getId()).toString());
            });
        }
        return true;
    }

    @Override
    public List<RepositoryLedgerDTO> getByCostSetIDAndMaterialGoods(List<UUID> uuids, List<UUID> collect, String fromDate, String toDate) {
        List<RepositoryLedgerDTO> repositoryLedgerDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT gl.CostSetID CostSetID, gl.MaterialGoodsID MaterialGoodsID, SUM(ISNULL(gl.MainIWQuantity, 0)) AS Quantity " +
            "FROM RepositoryLedger gl " +
            "WHERE TypeID = 400 " +
            "  AND Account LIKE '155%' " +
            "  AND AccountCorresponding LIKE '154%' " +
            "  AND gl.CostSetID IN :cids " +
            "  AND gl.MaterialGoodsID IN :mids " +
            "  AND PostedDate >= :fromDate " +
            "  AND PostedDate <= :toDate " +
            "GROUP BY gl.CostSetID, gl.MaterialGoodsID");
        params.put("cids", uuids);
        params.put("mids", collect);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "QuantityCompleteDTO");
        Common.setParams(query, params);
        repositoryLedgerDTOS = query.getResultList();
        return repositoryLedgerDTOS;
    }

    @Override
    public List<LotNoDTO> getListLotNo(UUID materialGoodsID) {
        Query lotNoQuery = entityManager.createNativeQuery("SELECT LotNo , ExpiryDate FROM RepositoryLedger" +
            " WHERE MaterialGoodsID = :id and LotNo is not null and LotNo != '' group by LotNo, ExpiryDate", "LotNoDTO");
        lotNoQuery.setParameter("id", materialGoodsID);
        return lotNoQuery.getResultList();
    }
}
