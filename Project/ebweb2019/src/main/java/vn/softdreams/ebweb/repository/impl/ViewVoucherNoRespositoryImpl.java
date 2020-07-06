package vn.softdreams.ebweb.repository.impl;

import com.google.common.collect.Lists;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.domain.RepositoryLedger;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.repository.ViewVoucherNoRespositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.ViewVoucherNoDetailDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.searchVoucher.*;

public class ViewVoucherNoRespositoryImpl implements ViewVoucherNoRespositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Autowired
    public ViewVoucherNoRespositoryImpl() {
    }

    @Override
    public List<ViewVoucherNoDetailDTO> findAllViewVoucherNoDetailDTOByListParentID(List<UUID> uuids) {
        if (uuids.size() != 0) {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("SELECT * from ViewVoucherNoDetailForCloseBook where refParentID in :uuids");
            params.put("uuids", uuids);
            Query query = entityManager.createNativeQuery(sql.toString(), "ViewVoucherNoDetailDTO");
            Common.setParams(query, params);
            return query.getResultList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void saveGeneralLedger(List<GeneralLedger> lsGeneralLedgers) {
        String sql = "INSERT INTO GeneralLedger (ID, " +
            "                           CompanyID, " +
            "                           BranchID, " +
            "                           ReferenceID, " +
            "                           DetailID, " +
            "                           TypeID, " +
            "                           Date, " +
            "                           PostedDate, " +
            "                           TypeLedger, " +
            "                           NoFBook, " +
            "                           NoMBook, " +
            "                           InvoiceSeries, " +
            "                           InvoiceDate, " +
            "                           InvoiceNo, " +
            "                           Account, " +
            "                           AccountCorresponding, " +
            "                           BankAccountDetailID, " +
            "                           BankAccount, " +
            "                           BankName, " +
            "                           CurrencyID, " +
            "                           ExchangeRate, " +
            "                           DebitAmount, " +
            "                           DebitAmountOriginal, " +
            "                           CreditAmount, " +
            "                           CreditAmountOriginal, " +
            "                           Reason, " +
            "                           Description, " +
            "                           AccountingObjectID, " +
            "                           AccountingObjectCode, " +
            "                           AccountingObjectName, " +
            "                           AccountingObjectAddress, " +
            "                           ContactName, " +
            "                           EmployeeID, " +
            "                           EmployeeCode, " +
            "                           EmployeeName, " +
            "                           MaterialGoodsID, " +
            "                           MaterialGoodsCode, " +
            "                           MaterialGoodsName, " +
            "                           RepositoryID, " +
            "                           RepositoryCode, " +
            "                           RepositoryName, " +
            "                           UnitID, " +
            "                           Quantity, " +
            "                           UnitPrice, " +
            "                           UnitPriceOriginal, " +
            "                           MainUnitID, " +
            "                           MainQuantity, " +
            "                           MainUnitPrice, " +
            "                           MainConvertRate, " +
            "                           Formula, " +
            "                           DepartmentID, " +
            "                           ExpenseItemID, " +
            "                           BudgetItemID, " +
            "                           CostSetID, " +
            "                           ContractID, " +
            "                           StatisticsCodeID, " +
            "                           RefDateTime " +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        int[][] ints = jdbcTemplate.batchUpdate(sql, lsGeneralLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
            // Insert
            setParam(ps, 1, UUID.randomUUID());
            setParam(ps, 2, detail.getCompanyID());
            setParam(ps, 3, detail.getBranchID());
            setParam(ps, 4, detail.getReferenceID());
            setParam(ps, 5, detail.getDetailID());
            setParam(ps, 6, detail.getTypeID());
            setParam(ps, 7, detail.getDate());
            setParam(ps, 8, detail.getPostedDate());
            setParam(ps, 9, detail.getTypeLedger());
            setParam(ps, 10, detail.getNoFBook());
            setParam(ps, 11, detail.getNoMBook());
            setParam(ps, 12, detail.getInvoiceSeries());
            setParam(ps, 13, detail.getInvoiceDate());
            setParam(ps, 14, detail.getInvoiceNo());
            setParam(ps, 15, detail.getAccount());
            setParam(ps, 16, detail.getAccountCorresponding());
            setParam(ps, 17, detail.getBankAccountDetailID());
            setParam(ps, 18, detail.getBankAccount());
            setParam(ps, 19, detail.getBankName());
            setParam(ps, 20, detail.getCurrencyID());
            setParam(ps, 21, detail.getExchangeRate());
            setParam(ps, 22, detail.getDebitAmount());
            setParam(ps, 23, detail.getDebitAmountOriginal());
            setParam(ps, 24, detail.getCreditAmount());
            setParam(ps, 25, detail.getCreditAmountOriginal());
            setParam(ps, 26, detail.getReason());
            setParam(ps, 27, detail.getDescription());
            setParam(ps, 28, detail.getAccountingObjectID());
            setParam(ps, 29, detail.getAccountingObjectCode());
            setParam(ps, 30, detail.getAccountingObjectName());
            setParam(ps, 31, detail.getAccountingObjectAddress());
            setParam(ps, 32, detail.getContactName());
            setParam(ps, 33, detail.getEmployeeID());
            setParam(ps, 34, detail.getEmployeeCode());
            setParam(ps, 35, detail.getEmployeeName());
            setParam(ps, 36, detail.getMaterialGoodsID());
            setParam(ps, 37, detail.getMaterialGoodsCode());
            setParam(ps, 38, detail.getMaterialGoodsName());
            setParam(ps, 39, detail.getRepositoryID());
            setParam(ps, 40, detail.getRepositoryCode());
            setParam(ps, 41, detail.getRepositoryName());
            setParam(ps, 42, detail.getUnitID());
            setParam(ps, 43, detail.getQuantity());
            setParam(ps, 44, detail.getUnitPrice());
            setParam(ps, 45, detail.getUnitPriceOriginal());
            setParam(ps, 46, detail.getMainUnitID());
            setParam(ps, 47, detail.getMainQuantity());
            setParam(ps, 48, detail.getMainUnitPrice());
            setParam(ps, 49, detail.getMainConvertRate());
            setParam(ps, 50, detail.getFormula());
            setParam(ps, 51, detail.getDepartmentID());
            setParam(ps, 52, detail.getExpenseItemID());
            setParam(ps, 53, detail.getBudgetItemID());
            setParam(ps, 54, detail.getCostSetID());
            setParam(ps, 55, detail.getContractID());
            setParam(ps, 56, detail.getStatisticsCodeID());
            setParam(ps, 57, detail.getRefDateTime());
        });
    }

    @Override
    public void saveRepositoryLedger(List<RepositoryLedger> repositoryLedgers) {
        String sql = "INSERT INTO RepositoryLedger(ID, " +
            "                             CompanyID, " +
            "                             BranchID, " +
            "                             ReferenceID, " +
            "                             Date, " +
            "                             PostedDate, " +
            "                             TypeLedger, " +
            "                             NoFBook, " +
            "                             NoMBook, " +
            "                             Account, " +
            "                             AccountCorresponding, " +
            "                             RepositoryID, " +
            "                             RepositoryCode, " +
            "                             RepositoryName, " +
            "                             MaterialGoodsID, " +
            "                             MaterialGoodsCode, " +
            "                             MaterialGoodsName, " +
            "                             UnitID, " +
            "                             UnitPrice, " +
            "                             IWQuantity, " +
            "                             OWQuantity, " +
            "                             IWAmount, " +
            "                             OWAmount, " +
            "                             MainUnitID, " +
            "                             MainUnitPrice, " +
            "                             MainIWQuantity, " +
            "                             MainOWQuantity, " +
            "                             MainConvertRate, " +
            "                             Formula, " +
            "                             Reason, " +
            "                             Description, " +
            "                             ExpiryDate, " +
            "                             LotNo, " +
            "                             BudgetItemID, " +
            "                             CostSetID, " +
            "                             StatisticsCodeID, " +
            "                             ExpenseItemID, " +
            "                             DetailID, " +
            "                             TypeID, " +
            "                             ConfrontID, " +
            "                             ConfrontDetailID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
        int[][] ints = jdbcTemplate.batchUpdate(sql, repositoryLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
            // Insert
            setParam(ps, 1, UUID.randomUUID());
            setParam(ps, 2, detail.getCompanyID());
            setParam(ps, 3, detail.getBranchID());
            setParam(ps, 4, detail.getReferenceID());
            setParam(ps, 5, detail.getDate());
            setParam(ps, 6, detail.getPostedDate());
            setParam(ps, 7, detail.getTypeLedger());
            setParam(ps, 8, detail.getNoFBook());
            setParam(ps, 9, detail.getNoMBook());
            setParam(ps, 10, detail.getAccount());
            setParam(ps, 11, detail.getAccountCorresponding());
            setParam(ps, 12, detail.getRepositoryID());
            setParam(ps, 13, detail.getRepositoryCode());
            setParam(ps, 14, detail.getRepositoryName());
            setParam(ps, 15, detail.getMaterialGoodsID());
            setParam(ps, 16, detail.getMaterialGoodsCode());
            setParam(ps, 17, detail.getMaterialGoodsName());
            setParam(ps, 18, detail.getUnitID());
            setParam(ps, 19, detail.getUnitPrice());
            setParam(ps, 20, detail.getIwQuantity());
            setParam(ps, 21, detail.getOwQuantity());
            setParam(ps, 22, detail.getIwAmount());
            setParam(ps, 23, detail.getOwAmount());
            setParam(ps, 24, detail.getMainUnitID());
            setParam(ps, 25, detail.getMainUnitPrice());
            setParam(ps, 26, detail.getMainIWQuantity());
            setParam(ps, 27, detail.getMainOWQuantity());
            setParam(ps, 28, detail.getMainConvertRate());
            setParam(ps, 29, detail.getFormula());
            setParam(ps, 30, detail.getReason());
            setParam(ps, 31, detail.getDescription());
            setParam(ps, 32, detail.getExpiryDate());
            setParam(ps, 33, detail.getLotNo());
            setParam(ps, 34, detail.getBudgetItemID());
            setParam(ps, 35, detail.getCostSetID());
            setParam(ps, 36, detail.getStatisticsCodeID());
            setParam(ps, 37, detail.getExpenseItemID());
            setParam(ps, 38, detail.getDetailID());
            setParam(ps, 39, detail.getTypeID());
            setParam(ps, 40, detail.getConfrontID());
            setParam(ps, 41, detail.getConfrontDetailID());
        });
    }


    @Override
    public void updateVoucherRefRecorded(List<ViewVoucherNo> lstViewVoucherNos) {
        List<ViewVoucherNo> lstRef = lstViewVoucherNos.stream().filter(n -> n.getmCReceiptID() != null || n.getmBDepositID() != null || n.getPaymentVoucherID() != null || n.getrSInwardOutwardID() != null).collect(Collectors.toList());
        String sql = "UPDATE MCReceipt set Recorded = 1 where id = ? ; " +
            "UPDATE MCPayment set Recorded = 1 where id = ? ; " +
            "UPDATE MBDeposit set Recorded = 1 where id = ? ; " +
            "UPDATE RSInwardOutward set Recorded = 1 where id = ? ; " +
            "UPDATE MBTellerPaper set Recorded = 1 where id = ? ; " +
            "UPDATE MBCreditCard set Recorded = 1 where id = ? ; ";
        int[][] ints = jdbcTemplate.batchUpdate(sql, lstRef, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, detail.getmCReceiptID());
            setParam(ps, 2, detail.getPaymentVoucherID());
            setParam(ps, 3, detail.getmBDepositID());
            setParam(ps, 4, detail.getrSInwardOutwardID());
            setParam(ps, 5, detail.getPaymentVoucherID());
            setParam(ps, 6, detail.getPaymentVoucherID());
        });
//        updateTableRecord(lstViewVoucherNos);
    }

    @Override
    public void updateVoucherPostedDate(List<ViewVoucherNo> lstViewVoucherNos, List<ViewVoucherNo> listSubChangePostedDate, LocalDate postedDate) {
        Map<String, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstViewVoucherNos.stream().collect(Collectors.groupingBy(w -> w.getRefTable()));
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ViewVoucherNo> lstPr0 = lstViewVoucherNos.stream().filter(n -> !listSubChangePostedDate.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
        List<UUID> lstPr = lstPr0.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList());
        if (lstPr.size() > 0) {
            for (Map.Entry<String, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
                sql.append("Update " + listEntry.getKey() + " Set PostedDate = :postedDate where id in :lstID ;");
            }
            params.put("postedDate", postedDate);
            params.put("lstID", lstPr);
        }
        int index = 0;
        List<ViewVoucherNo> lstViewSub = lstViewVoucherNos.stream().filter(n -> listSubChangePostedDate.stream().map(c -> c.getRefID()).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
        List<UUID> lstMCReceipt = lstViewSub.stream().filter(mcr -> mcr.getmCReceiptID() != null).map(ViewVoucherNo::getmCReceiptID).collect(Collectors.toList());
        List<UUID> lstMCReceipt1 = lstPr0.stream().filter(mcr -> mcr.getmCReceiptID() != null).map(ViewVoucherNo::getmCReceiptID).collect(Collectors.toList());
        lstMCReceipt.addAll(lstMCReceipt1);
        List<UUID> lstPaymentVoucherID = lstViewSub.stream().filter(mcr -> mcr.getPaymentVoucherID() != null).map(ViewVoucherNo::getPaymentVoucherID).collect(Collectors.toList()); // phiếu chi, ủy nhiệm chi, ...
        List<UUID> lstPaymentVoucherID1 = lstPr0.stream().filter(mcr -> mcr.getPaymentVoucherID() != null).map(ViewVoucherNo::getPaymentVoucherID).collect(Collectors.toList()); // phiếu chi, ủy nhiệm chi, ...
        lstPaymentVoucherID.addAll(lstPaymentVoucherID1);
        List<UUID> lstMBDeposit = lstViewSub.stream().filter(mcr -> mcr.getmBDepositID() != null).map(ViewVoucherNo::getmBDepositID).collect(Collectors.toList());
        List<UUID> lstMBDeposit1 = lstPr0.stream().filter(mcr -> mcr.getmBDepositID() != null).map(ViewVoucherNo::getmBDepositID).collect(Collectors.toList());
        lstMBDeposit.addAll(lstMBDeposit1);
        List<UUID> lstRSInwardOutwardID = lstViewSub.stream().filter(mcr -> mcr.getrSInwardOutwardID() != null).map(ViewVoucherNo::getrSInwardOutwardID).collect(Collectors.toList());
        List<UUID> lstRSInwardOutwardID1 = lstPr0.stream().filter(mcr -> mcr.getrSInwardOutwardID() != null).map(ViewVoucherNo::getrSInwardOutwardID).collect(Collectors.toList());
        lstRSInwardOutwardID.addAll(lstRSInwardOutwardID1);
        for (ViewVoucherNo viewVoucherNo : lstViewSub) {
            sql.append("Update " + viewVoucherNo.getRefTable() + " Set PostedDate = :postedDate" + index + " where id=:id" + index + " ;");
            params.put("postedDate" + index, listSubChangePostedDate.stream().filter(n -> n.getRefID().equals(viewVoucherNo.getRefID())).findFirst().get().getPostedDateChange());
            params.put("id" + index, viewVoucherNo.getRefID());
        }

        // Chứng từ liên quan
        // Phiếu thu
        String sqlMCReceipt = "UPDATE MCReceipt set PostedDate = ? where id = ? ; ";
        int[][] intsMCReceipt = jdbcTemplate.batchUpdate(sqlMCReceipt, lstMCReceipt, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getmCReceiptID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getmCReceiptID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 2, detail);
        });

        //Báo có
        String sqlMBDeposit = "UPDATE MBDeposit set PostedDate = ? where id = ? ; ";
        int[][] intsMBDeposit = jdbcTemplate.batchUpdate(sqlMBDeposit, lstMBDeposit, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getmBDepositID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getmBDepositID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 2, detail);
        });
        // Xuất kho, nhập kho
        String sqlRSInwardOutward = "UPDATE RSInwardOutward set PostedDate = ? where id = ? ; ";
        int[][] intsRSInwardOutward = jdbcTemplate.batchUpdate(sqlRSInwardOutward, lstRSInwardOutwardID, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getrSInwardOutwardID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getrSInwardOutwardID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 2, detail);
        });

        // PChi, Báo nợ, Thẻ tín dụng
        String sqlPaymentVoucher = "UPDATE MCPayment set PostedDate = ? where id = ? ; " +
            "UPDATE MBTellerPaper set PostedDate = ? where id = ? ; " +
            "UPDATE MBCreditCard set PostedDate = ? where id = ? ; ";
        int[][] intsPaymentVoucher = jdbcTemplate.batchUpdate(sqlPaymentVoucher, lstPaymentVoucherID, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getPaymentVoucherID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 2, detail);
            setParam(ps, 3, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getPaymentVoucherID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 4, detail);
            setParam(ps, 5, listSubChangePostedDate.stream().anyMatch(n -> detail.equals(n.getPaymentVoucherID())) ? listSubChangePostedDate.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange() : postedDate);
            setParam(ps, 6, detail);
        });

        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        int result = query.executeUpdate();
    }

    @Override
    public void deleteVoucher(List<ViewVoucherNo> lstViewVoucherNos) {
        List<ViewVoucherNoDetailDTO> viewVoucherNoDetailDTOS = new ArrayList<>();
        for (int i = 0; i < lstViewVoucherNos.size(); i++) {
            viewVoucherNoDetailDTOS.addAll(lstViewVoucherNos.get(i).getViewVoucherNoDetailDTOS().stream()
                .filter(x -> x.getPpOrderDetailId() != null && x.getPpOrderDetailQuantity() != null).collect(Collectors.toList()));
        }
        List<UUID> lstIDRoot = lstViewVoucherNos.stream().map(n -> n.getRefID()).collect(Collectors.toList());
        List<UUID> lstMCReceipt = lstViewVoucherNos.stream().filter(mcr -> mcr.getmCReceiptID() != null).map(ViewVoucherNo::getmCReceiptID).collect(Collectors.toList());
        List<UUID> lstPaymentVoucherID = lstViewVoucherNos.stream().filter(mcr -> mcr.getPaymentVoucherID() != null).map(ViewVoucherNo::getPaymentVoucherID).collect(Collectors.toList()); // phiếu chi, ủy nhiệm chi, ...
        List<UUID> lstMBDeposit = lstViewVoucherNos.stream().filter(mcr -> mcr.getmBDepositID() != null).map(ViewVoucherNo::getmBDepositID).collect(Collectors.toList());
        List<UUID> lstRSInwardOutwardID = lstViewVoucherNos.stream().filter(mcr -> mcr.getrSInwardOutwardID() != null).map(ViewVoucherNo::getrSInwardOutwardID).collect(Collectors.toList());
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        /*Xóa chứng từ liên quan*/
        if (lstPaymentVoucherID.size() > 0) {
            String LstPaymentVoucherID = listUUIDToStringForSQL(lstPaymentVoucherID);
            sql.append("Delete RefVoucher where RefID1 in " + LstPaymentVoucherID + " or RefID2 in " + LstPaymentVoucherID + "; ");
            // Phiếu chi
            sql.append("delete MCPaymentDetail where MCPaymentID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MCPaymentDetailInsurance where MCPaymentID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MCPaymentDetailSalary where MCPaymentID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MCPaymentDetailTax where MCPaymentID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MCPaymentDetailVendor where MCPaymentID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MCPayment where ID in " + LstPaymentVoucherID + "; ");
            // Báo nợ
            sql.append("delete MBTellerPaperDetail where MBTellerPaperID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBTellerPaperDetailInsurance where MBTellerPaperID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBTellerPaperDetailSalary where MBTellerPaperID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBTellerPaperDetailTax where MBTellerPaperID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBTellerPaperDetailVendor where MBTellerPaperID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBTellerPaper where ID in " + LstPaymentVoucherID + "; ");
            // Thẻ tín dụng
            sql.append("delete MBCreditCardDetail where MBCreditCardID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBCreditCardDetailTax where MBCreditCardID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBCreditCardDetailVendor where MBCreditCardID in " + LstPaymentVoucherID + "; ");
            sql.append("delete MBCreditCard where ID in " + LstPaymentVoucherID + "; ");
        }

        /* Trả lại số lượng hàng hóa cho đơn mua hàng */
        for (int i = 0; i < viewVoucherNoDetailDTOS.size(); i++) {
            sql.append("update PPOrderDetail set QuantityReceipt = " +
                viewVoucherNoDetailDTOS.get(i).getPpOrderDetailQuantity().add(viewVoucherNoDetailDTOS.get(i).getQuantity()) +
                " where id = '" + Utils.uuidConvertToGUID((viewVoucherNoDetailDTOS.get(i).getPpOrderDetailId())).toString() + "';");
        }
        // Phiếu thu
        if (lstMCReceipt.size() > 0) {
            String LstMCReceiptID = listUUIDToStringForSQL(lstMCReceipt);
            sql.append("Delete RefVoucher where RefID1 in " + LstMCReceiptID + " or RefID2 in " + LstMCReceiptID + "; ");
            sql.append("delete MCReceiptDetail where MCReceiptID in " + LstMCReceiptID + "; ");
            sql.append("delete MCReceiptDetailCustomer where MCReceiptID in " + LstMCReceiptID + "; ");
            sql.append("delete MCReceiptDetailTax where MCReceiptID in " + LstMCReceiptID + "; ");
            sql.append("delete MCReceipt where ID in " + LstMCReceiptID + "; ");
        }
        // Báo có
        if (lstMBDeposit.size() > 0) {
            String LstMBDepositID = listUUIDToStringForSQL(lstMBDeposit);
            sql.append("Delete RefVoucher where RefID1 in " + LstMBDepositID + " or RefID2 in " + LstMBDepositID + "; ");
            sql.append("delete MBDepositDetail where MBDepositID in " + LstMBDepositID + "; ");
            sql.append("delete MBDepositDetailCustomer where MBDepositID in " + LstMBDepositID + "; ");
            sql.append("delete MBDepositDetailTax where MBDepositID in " + LstMBDepositID + "; ");
            sql.append("delete MBDeposit where ID in " + LstMBDepositID + "; ");
        }
        // Nhập kho, xuất kho
        if (lstRSInwardOutwardID.size() > 0) {
            String lstRSInwardOutward = listUUIDToStringForSQL(lstRSInwardOutwardID);
            sql.append("Delete RefVoucher where RefID1 in " + lstRSInwardOutward + " or RefID2 in " + lstRSInwardOutward + "; ");
            sql.append("delete RSInwardOutwardDetail where RSInwardOutwardID in " + lstRSInwardOutward + "; ");
            sql.append("delete RSInwardOutward where ID in " + lstRSInwardOutward + "; ");
        }

        /*Xóa tham chiếu*/
        if (lstIDRoot.size() > 0) {
            sql.append("Delete RefVoucher where RefID1 in " + listUUIDToStringForSQL(lstIDRoot) + " or RefID2 in " + listUUIDToStringForSQL(lstIDRoot) + "; ");
        }
        /*Xóa chứng từ gốc*/
        Map<String, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstViewVoucherNos.stream().collect(Collectors.groupingBy(ViewVoucherNo::getRefTable));
        for (Map.Entry<String, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            // Xóa các bảng liên quan
            String lstRoot = listUUIDToStringForSQL(lstIDRoot);
            switch (listEntry.getKey()) {
                case "MCReceipt":
                    sql.append("Delete RefVoucher where RefID1 in " + lstRoot + " or RefID2 in " + lstRoot + "; ");
                    sql.append("delete MCReceiptDetail where MCReceiptID in " + lstRoot + "; ");
                    sql.append("delete MCReceiptDetailCustomer where MCReceiptID in " + lstRoot + "; ");
                    sql.append("delete MCReceiptDetailTax where MCReceiptID in " + lstRoot + "; ");
                    break;
                case "MCPayment":
                    sql.append("delete MCPaymentDetail where MCPaymentID in " + lstRoot + "; ");
                    sql.append("delete MCPaymentDetailInsurance where MCPaymentID in " + lstRoot + "; ");
                    sql.append("delete MCPaymentDetailSalary where MCPaymentID in " + lstRoot + "; ");
                    sql.append("delete MCPaymentDetailTax where MCPaymentID in " + lstRoot + "; ");
                    sql.append("delete MCPaymentDetailVendor where MCPaymentID in " + lstRoot + "; ");
                    break;
                case "MBDeposit":
                    sql.append("delete MBDepositDetail where MBDepositID in " + lstRoot + "; ");
                    sql.append("delete MBDepositDetailCustomer where MBDepositID in " + lstRoot + "; ");
                    sql.append("delete MBDepositDetailTax where MBDepositID in " + lstRoot + "; ");
                    break;
                case "MBTellerPaper":
                    sql.append("delete MBTellerPaperDetail where MBTellerPaperID in " + lstRoot + "; ");
                    sql.append("delete MBTellerPaperDetailInsurance where MBTellerPaperID in " + lstRoot + "; ");
                    sql.append("delete MBTellerPaperDetailSalary where MBTellerPaperID in " + lstRoot + "; ");
                    sql.append("delete MBTellerPaperDetailTax where MBTellerPaperID in " + lstRoot + "; ");
                    sql.append("delete MBTellerPaperDetailVendor where MBTellerPaperID in " + lstRoot + "; ");
                    break;
                case "MBCreditCard":
                    sql.append("delete MBCreditCardDetail where MBCreditCardID in " + lstRoot + "; ");
                    sql.append("delete MBCreditCardDetailTax where MBCreditCardID in " + lstRoot + "; ");
                    sql.append("delete MBCreditCardDetailVendor where MBCreditCardID in " + lstRoot + "; ");
                    break;
                case "PPInvoice":
                    sql.append("delete PPInvoiceDetail where PPInvoiceID in " + lstRoot + "; ");
                    sql.append("delete PPInvoiceDetailCost where RefID in " + lstRoot + "; ");
                    break;
                case "PPService":
                    sql.append("delete PPServiceDetail where PPServiceID in " + lstRoot + "; ");
                    break;
                case "PPDiscountReturn":
                    sql.append("delete PPServiceDetail where PPServiceID in " + lstRoot + "; ");
                    break;
                case "SAInvoice":
                    sql.append("delete SAInvoiceDetail where SAInvoiceID in " + lstRoot + "; ");
                    break;
                case "SAReturn":
                    sql.append("delete SAReturnDetail where SAReturnID in " + lstRoot + "; ");
                    break;
            }
            // Xóa ở bảng cha
            sql.append("delete " + listEntry.getKey() + " where id in " + lstRoot + "; ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
//        Common.setParams(query, params);
        int result = query.executeUpdate();

    }

    String listUUIDToStringForSQL(List<UUID> uuids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ( ");
        if (uuids.size() > 0) {
            for (UUID uuid : uuids) {
                sql.append("'" + Utils.uuidConvertToGUID(uuid).toString() + "'");
                if (uuids.indexOf(uuid) == uuids.size() - 1) {
                    sql.append(")");
                } else {
                    sql.append(",");
                }
            }
        } else {
            return "('')";
        }
        return sql.toString();
    }

    @Override
    public void handleChangePostedDate(List<ViewVoucherNo> listSubDateRoot, List<ViewVoucherNo> listDateChangeAfter) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        int index = 0;
        List<ViewVoucherNo> lstViewSub = listSubDateRoot.stream().filter(n -> listDateChangeAfter.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
        List<UUID> lstMCReceipt = lstViewSub.stream().filter(mcr -> mcr.getmCReceiptID() != null).map(ViewVoucherNo::getmCReceiptID).collect(Collectors.toList());
        List<UUID> lstPaymentVoucherID = lstViewSub.stream().filter(mcr -> mcr.getPaymentVoucherID() != null).map(ViewVoucherNo::getPaymentVoucherID).collect(Collectors.toList()); // phiếu chi, ủy nhiệm chi, ...
        List<UUID> lstMBDeposit = lstViewSub.stream().filter(mcr -> mcr.getmBDepositID() != null).map(ViewVoucherNo::getmBDepositID).collect(Collectors.toList());
        List<UUID> lstRSInwardOutwardID = lstViewSub.stream().filter(mcr -> mcr.getrSInwardOutwardID() != null).map(ViewVoucherNo::getrSInwardOutwardID).collect(Collectors.toList());
        for (ViewVoucherNo viewVoucherNo : lstViewSub) {
            sql.append("Update " + viewVoucherNo.getRefTable() + " Set PostedDate = :postedDate" + index + " where id=:id" + index + " ;");
            params.put("postedDate" + index, listDateChangeAfter.stream().filter(n -> n.getRefID().equals(viewVoucherNo.getRefID())).findAny().get().getPostedDateChange());
            params.put("id" + index, viewVoucherNo.getRefID());
            index++;
        }
        // Chứng từ liên quan
        // Phiếu thu
        String sqlMCReceipt = "UPDATE MCReceipt set PostedDate = ? where id = ? ; ";
        int[][] intsMCReceipt = jdbcTemplate.batchUpdate(sqlMCReceipt, lstMCReceipt, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listDateChangeAfter.stream().filter(n -> detail.equals(n.getmCReceiptID())).findAny().get().getPostedDateChange());
            setParam(ps, 2, detail);
        });

        //Báo có
        String sqlMBDeposit = "UPDATE MBDeposit set PostedDate = ? where id = ? ; ";
        int[][] intsMBDeposit = jdbcTemplate.batchUpdate(sqlMBDeposit, lstMBDeposit, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listDateChangeAfter.stream().filter(n -> detail.equals(n.getmBDepositID())).findAny().get().getPostedDateChange());
            setParam(ps, 2, detail);
        });
        // Xuất kho, nhập kho
        String sqlRSInwardOutward = "UPDATE RSInwardOutward set PostedDate = ? where id = ? ; ";
        int[][] intsRSInwardOutward = jdbcTemplate.batchUpdate(sqlRSInwardOutward, lstRSInwardOutwardID, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listDateChangeAfter.stream().filter(n -> detail.equals(n.getrSInwardOutwardID())).findAny().get().getPostedDateChange());
            setParam(ps, 2, detail);
        });

        // PChi, Báo nợ, Thẻ tín dụng
        String sqlPaymentVoucher = "UPDATE MCPayment set PostedDate = ? where id = ? ; " +
            "UPDATE MBTellerPaper set PostedDate = ? where id = ? ; " +
            "UPDATE MBCreditCard set PostedDate = ? where id = ? ; ";
        int[][] intsPaymentVoucher = jdbcTemplate.batchUpdate(sqlPaymentVoucher, lstPaymentVoucherID, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, listDateChangeAfter.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange());
            setParam(ps, 2, detail);
            setParam(ps, 3, listDateChangeAfter.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange());
            setParam(ps, 4, detail);
            setParam(ps, 5, listDateChangeAfter.stream().filter(n -> detail.equals(n.getPaymentVoucherID())).findAny().get().getPostedDateChange());
            setParam(ps, 6, detail);
        });

        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        int result = query.executeUpdate();
    }

    @Override
    public Boolean updateDateClosedBook(UUID companyID, String code, String codeDateOld, LocalDate dateClose, LocalDate dateCloseOld) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("UPDATE SystemOption Set data = :dateClose where CompanyID=:companyID and Code =:code ; ");
        params.put("dateClose", dateClose);
        params.put("code", code);
        if (dateCloseOld != null) {
            sql.append("UPDATE SystemOption Set data = :dateCloseOld where CompanyID=:companyID and Code = :codeDateOld ; ");
            params.put("dateCloseOld", dateCloseOld);
            params.put("codeDateOld", codeDateOld);
        }
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        query.executeUpdate();
        return true;
    }

    @Override
    public void updateCloseBookDateForBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate) {
        String sql = "Update SystemOption " +
            "set Data = (select Top (1) Data from SystemOption where CompanyID = ? and code = 'DBDateClosed') " +
            "where CompanyID = ? " +
            "  and Code = 'DBDateClosedOld' ;" +
            "UPDATE SystemOption set Data = ? where CompanyID = ? and Code = 'DBDateClosed'";
        int[][] ints = jdbcTemplate.batchUpdate(sql, orgTreeDTOS, Constants.BATCH_SIZE, (ps, detail) -> {
            setParam(ps, 1, detail.getValue());
            setParam(ps, 2, detail.getValue());
            setParam(ps, 3, postedDate);
            setParam(ps, 4, detail.getValue());
        });
    }

    @Override
    public void deletePaymentVoucherInID(List<UUID> uuids) {
        String sql = "DELETE FROM MCPaymentDetailVendor WHERE MCPaymentID = ?; " +
            "DELETE FROM MCPayment WHERE ID = ?; " +
            "DELETE FROM MBTellerPaperDetailVendor WHERE MBTellerPaperID = ?; " +
            "DELETE FROM MBTellerPaper WHERE ID = ?; " +
            "DELETE FROM MBCreditCardDetailVendor WHERE MBCreditCardID = ?; " +
            "DELETE FROM MBCreditCard WHERE ID = ?;";
        int[][] ints = jdbcTemplate.batchUpdate(sql, uuids, Constants.BATCH_SIZE, (ps, uuid) -> {
            setParam(ps, 1, uuid);
            setParam(ps, 2, uuid);
            setParam(ps, 3, uuid);
            setParam(ps, 4, uuid);
            setParam(ps, 5, uuid);
            setParam(ps, 6, uuid);
        });
    }

    @Override
    public void saveToolLedger(List<GeneralLedger> ledgers) {
//        String sql = "insert into ToolLedger(companyid," +
//            "                       branchid," +
//            "                       typeid," +
//            "                       referenceid," +
//            "                       typeledger," +
//            "                       nofbook," +
//            "                       nombook," +
//            "                       date," +
//            "                       posteddate," +
//            "                       toolsid," +
//            "                       toolcode," +
//            "                       toolname," +
//            "                       reason," +
//            "                       description," +
//            "                       unitprice," +
//            "                       incrementallocationtime," +
//            "                       decrementallocationtime," +
//            "                       incrementquantity," +
//            "                       decrementquantity," +
//            "                       incrementamount," +
//            "                       decrementamount," +
//            "                       allocationamount," +
//            "                       allocatedamount," +
//            "                       departmentid," +
//            "                       remainingquantity," +
//            "                       remainingallocaitontimes," +
//            "                       remainingamount," +
//            "                       orderpriority)" +
//            "values ();";
//        int[][] ints = jdbcTemplate.batchUpdate(sql, repositoryLedgers, Constants.BATCH_SIZE, (ps, detail) -> {
//            // Insert
//            setParam(ps, 1, UUID.randomUUID());
//            setParam(ps, 2, detail.getCompanyID());
//            setParam(ps, 3, detail.getBranchID());
//            setParam(ps, 4, detail.getReferenceID());
//            setParam(ps, 5, detail.getDate());
//            setParam(ps, 6, detail.getPostedDate());
//            setParam(ps, 7, detail.getTypeLedger());
//            setParam(ps, 8, detail.getNoFBook());
//            setParam(ps, 9, detail.getNoMBook());
//            setParam(ps, 10, detail.getAccount());
//            setParam(ps, 11, detail.getAccountCorresponding());
//            setParam(ps, 12, detail.getRepositoryID());
//            setParam(ps, 13, detail.getRepositoryCode());
//            setParam(ps, 14, detail.getRepositoryName());
//            setParam(ps, 15, detail.getMaterialGoodsID());
//            setParam(ps, 16, detail.getMaterialGoodsCode());
//            setParam(ps, 17, detail.getMaterialGoodsName());
//            setParam(ps, 18, detail.getUnitID());
//            setParam(ps, 19, detail.getUnitPrice());
//            setParam(ps, 20, detail.getIwQuantity());
//            setParam(ps, 21, detail.getOwQuantity());
//            setParam(ps, 22, detail.getIwAmount());
//            setParam(ps, 23, detail.getOwAmount());
//            setParam(ps, 24, detail.getMainUnitID());
//            setParam(ps, 25, detail.getMainUnitPrice());
//            setParam(ps, 26, detail.getMainIWQuantity());
//            setParam(ps, 27, detail.getMainOWQuantity());
//            setParam(ps, 28, detail.getMainConvertRate());
//            setParam(ps, 29, detail.getFormula());
//            setParam(ps, 30, detail.getReason());
//            setParam(ps, 31, detail.getDescription());
//            setParam(ps, 32, detail.getExpiryDate());
//            setParam(ps, 33, detail.getLotNo());
//            setParam(ps, 34, detail.getBudgetItemID());
//            setParam(ps, 35, detail.getCostSetID());
//            setParam(ps, 36, detail.getStatisticsCodeID());
//            setParam(ps, 37, detail.getExpenseItemID());
//            setParam(ps, 38, detail.getDetailID());
//            setParam(ps, 39, detail.getTypeID());
//            setParam(ps, 40, detail.getConfrontID());
//            setParam(ps, 41, detail.getConfrontDetailID());
//        });
    }

    @Override
    public Page<ViewVoucherDTO> findAllByTypeGroupID(Pageable pageable, Integer typeGroupID, UUID companyID, Integer typeLedger, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ViewVoucherDTO> lst = new ArrayList<>();
        String orderBy = "";
        sql.append("FROM ViewVoucherNo WHERE CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (typeLedger.equals(0)) {
            orderBy = " order by Date ASC ,PostedDate ASC ,noFbook ASC";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
        } else if (typeLedger.equals(1)) {
            orderBy = " order by Date ASC ,PostedDate ASC ,NoMBook ASC";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
        }
        sql.append(" and TypeGroupID = :typeGroupID ");
        params.put("typeGroupID", typeGroupID);

        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            LocalDate localDate = LocalDate.parse(toDate);
            localDate = localDate.plusDays(1);
            sql.append("and Date < :toDate ");
            params.put("toDate", localDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT RefID as id, " +
                "       typeID, " +
                "       typeGroupID, " +
                "       companyID, " +
                "       noMBook, " +
                "       noFBook, " +
                "       date, " +
                "       postedDate, " +
                "       reason, " +
                "       recorded, " +
                "       typeName, " +
                "       refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<ViewVoucherDTO>) lst), pageable, total.longValue());
    }

    @Override
    public List<ViewVoucherDTO> findAllByTypeGroupID(Integer typeGroupID, UUID companyID, Integer typeLedger, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ViewVoucherDTO> lst = new ArrayList<>();
        String orderBy = "";
        sql.append("FROM ViewVoucherNo WHERE CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (typeLedger.equals(0)) {
            orderBy = " order by Date ASC ,PostedDate ASC ,noFbook ASC";
            sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
        } else if (typeLedger.equals(1)) {
            orderBy = " order by Date ASC ,PostedDate ASC ,NoMBook ASC";
            sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
        }
        sql.append(" and TypeGroupID = :typeGroupID ");
        params.put("typeGroupID", typeGroupID);
        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            LocalDate localDate = LocalDate.parse(toDate);
            localDate = localDate.plusDays(1);
            sql.append("and Date < :toDate ");
            params.put("toDate", localDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT RefID as id, " +
                "       typeID, " +
                "       typeGroupID, " +
                "       companyID, " +
                "       noMBook, " +
                "       noFBook, " +
                "       date, " +
                "       postedDate, " +
                "       reason, " +
                "       recorded, " +
                "       typeName, " +
                "       refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
            Common.setParams(query, params);
            lst = query.getResultList();
        }
        return lst;
    }

    @Override
    public Page<ViewVoucherDTO> searchVoucher(Pageable pageable, Integer typeSearch, Integer typeGroupID, String no, String invoiceDate, Boolean recorded, String fromDate, String toDate, Integer phienSoLamViec, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ViewVoucherDTO> lst = new ArrayList<>();
        String orderBy = "";
        Number total = 0;
        Query countQuerry;
        switch (typeSearch) {
            case LoaiChungTu:
                sql.append("FROM ViewVoucherNo WHERE CompanyID = :companyID ");
                params.put("companyID", companyID);
                if (phienSoLamViec == 0) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                sql.append("and typeGroupID = :typeGroupID ");
                params.put("typeGroupID", typeGroupID);
                if (recorded != null) {
                    /*sql.append("and Recorded = :Recorded ");
                    params.put("Recorded", recorded == true ? 1 : 0);*/
                    if (recorded) {
                        sql.append("and Recorded = 1 ");
                    } else {
                        sql.append("and (Recorded = 0 or Recorded is null) ");
                    }
                }
                if (!StringUtils.isEmpty(fromDate)) {
                    sql.append("and Date >= :fromDate ");
                    params.put("fromDate", fromDate);
                }
                if (!StringUtils.isEmpty(toDate)) {
                    LocalDate localDate = LocalDate.parse(toDate);
                    localDate = localDate.plusDays(1);
                    sql.append("and Date < :toDate ");
                    params.put("toDate", localDate);
                }
                countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
                Common.setParams(countQuerry, params);
                total = (Number) countQuerry.getSingleResult();
                if (total.longValue() > 0) {
                    Query query = entityManager.createNativeQuery("SELECT RefID as id, " +
                        "       typeID, " +
                        "       typeGroupID, " +
                        "       companyID, " +
                        "       noMBook, " +
                        "       noFBook, " +
                        "       date, " +
                        "       postedDate, " +
                        "       reason, " +
                        "       recorded, " +
                        "       typeName, " +
                        "       refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
                    Common.setParamsWithPageable(query, params, pageable, total);
                    lst = query.getResultList();
                }
                break;
            case SoChungTu:
                sql.append("FROM ViewVoucherNo WHERE CompanyID = :companyID ");
                params.put("companyID", companyID);
                if (phienSoLamViec == 0) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (recorded != null) {
                    if (recorded) {
                        sql.append("and Recorded = 1 ");
                    } else {
                        sql.append("and (Recorded = 0 or Recorded is null) ");
                    }
                }
                if (!StringUtils.isEmpty(fromDate)) {
                    sql.append("and Date >= :fromDate ");
                    params.put("fromDate", fromDate);
                }
                if (!StringUtils.isEmpty(toDate)) {
                    LocalDate localDate = LocalDate.parse(toDate);
                    localDate = localDate.plusDays(1);
                    sql.append("and Date < :toDate ");
                    params.put("toDate", localDate);
                }
                if (!StringUtils.isEmpty(no)) {
                    if (phienSoLamViec == 0) {
                        sql.append("and NoFBook like :No ");
                    } else if (phienSoLamViec == 1) {
                        sql.append("and NoMBook like :No ");
                    }
                    params.put("No", "%" + no + "%");
                }
                countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
                Common.setParams(countQuerry, params);
                total = (Number) countQuerry.getSingleResult();
                if (total.longValue() > 0) {
                    Query query = entityManager.createNativeQuery("SELECT RefID as id, " +
                        "       typeID, " +
                        "       typeGroupID, " +
                        "       companyID, " +
                        "       noMBook, " +
                        "       noFBook, " +
                        "       date, " +
                        "       postedDate, " +
                        "       reason, " +
                        "       recorded, " +
                        "       typeName, " +
                        "       refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
                    Common.setParamsWithPageable(query, params, pageable, total);
                    lst = query.getResultList();
                }
                break;
            case SoHoaDon:
                sql.append("FROM (SELECT RefID as id, " +
                    "             typeID, " +
                    "             typeGroupID, " +
                    "             companyID, " +
                    "             noMBook, " +
                    "             noFBook, " +
                    "             date, " +
                    "             postedDate, " +
                    "             reason, " +
                    "             recorded, " +
                    "             typeName, " +
                    "             refTable, " +
                    "             InvoiceDate, " +
                    "             InvoiceNo, " +
                    "             TypeLedger " +
                    "      from ViewVoucherNo " +
                    "      union all " +
                    "      SELECT RefParentID as id, " +
                    "             typeID, " +
                    "             typeGroupID, " +
                    "             companyID, " +
                    "             noMBook, " +
                    "             noFBook, " +
                    "             date, " +
                    "             postedDate, " +
                    "             reason, " +
                    "             recorded, " +
                    "             typeName, " +
                    "             refTable, " +
                    "             InvoiceDate, " +
                    "             InvoiceNo, " +
                    "             TypeLedger " +
                    "      from ViewVoucherNoDetail) a WHERE CompanyID = :companyID ");
                params.put("companyID", companyID);
                if (phienSoLamViec == 0) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (recorded != null) {
                    if (recorded) {
                        sql.append("and Recorded = 1 ");
                    } else {
                        sql.append("and (Recorded = 0 or Recorded is null) ");
                    }
                }
                if (!StringUtils.isEmpty(fromDate)) {
                    sql.append("and Date >= :fromDate ");
                    params.put("fromDate", fromDate);
                }
                if (!StringUtils.isEmpty(toDate)) {
                    LocalDate localDate = LocalDate.parse(toDate);
                    localDate = localDate.plusDays(1);
                    sql.append("and Date < :toDate ");
                    params.put("toDate", localDate);
                }
                if (!StringUtils.isEmpty(no)) {
                    sql.append("and InvoiceNo like :No ");
                    params.put("No", "%" + no + "%");
                }
               /* if (!StringUtils.isEmpty(invoiceDate)) {
                    LocalDate localDate = LocalDate.parse(invoiceDate);
                    sql.append("and InvoiceDate >= :invoiceDate1 ");
                    params.put("invoiceDate1", localDate);
                    sql.append("and InvoiceDate < :invoiceDate2 ");
                    localDate = localDate.plusDays(1);
                    params.put("invoiceDate2", localDate);
                }*/
                countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
                Common.setParams(countQuerry, params);
                total = (Number) countQuerry.getSingleResult();
                if (total.longValue() > 0) {
                    Query query = entityManager.createNativeQuery("SELECT DISTINCT id, " +
                        "       typeID, " +
                        "       typeGroupID, " +
                        "       companyID, " +
                        "       noMBook, " +
                        "       noFBook, " +
                        "       date, " +
                        "       postedDate, " +
                        "       reason, " +
                        "       recorded, " +
                        "       typeName, " +
                        "       NULL as  refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
                    Common.setParamsWithPageable(query, params, pageable, total);
                    lst = query.getResultList();
                }
                break;
            case NgayHachToan:
                sql.append("FROM ViewVoucherNo WHERE CompanyID = :companyID ");
                params.put("companyID", companyID);
                if (phienSoLamViec == 0) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (recorded != null) {
                    if (recorded) {
                        sql.append("and Recorded = 1 ");
                    } else {
                        sql.append("and (Recorded = 0 or Recorded is null) ");
                    }
                }
                if (!StringUtils.isEmpty(fromDate)) {
                    sql.append("and PostedDate >= :fromDate ");
                    params.put("fromDate", fromDate);
                }
                if (!StringUtils.isEmpty(toDate)) {
                    LocalDate localDate = LocalDate.parse(toDate);
                    localDate = localDate.plusDays(1);
                    sql.append("and PostedDate < :toDate ");
                    params.put("toDate", localDate);
                }
                countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
                Common.setParams(countQuerry, params);
                total = (Number) countQuerry.getSingleResult();
                if (total.longValue() > 0) {
                    Query query = entityManager.createNativeQuery("SELECT RefID as id, " +
                        "       typeID, " +
                        "       typeGroupID, " +
                        "       companyID, " +
                        "       noMBook, " +
                        "       noFBook, " +
                        "       date, " +
                        "       postedDate, " +
                        "       reason, " +
                        "       recorded, " +
                        "       typeName, " +
                        "       refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
                    Common.setParamsWithPageable(query, params, pageable, total);
                    lst = query.getResultList();
                }
                break;
            case NgayHoaDon:
                sql.append("FROM (SELECT RefID as id, " +
                    "             typeID, " +
                    "             typeGroupID, " +
                    "             companyID, " +
                    "             noMBook, " +
                    "             noFBook, " +
                    "             date, " +
                    "             postedDate, " +
                    "             reason, " +
                    "             recorded, " +
                    "             typeName, " +
                    "             refTable, " +
                    "             InvoiceDate, " +
                    "             InvoiceNo, " +
                    "             TypeLedger " +
                    "      from ViewVoucherNo " +
                    "      union all " +
                    "      SELECT RefParentID as id, " +
                    "             typeID, " +
                    "             typeGroupID, " +
                    "             companyID, " +
                    "             noMBook, " +
                    "             noFBook, " +
                    "             date, " +
                    "             postedDate, " +
                    "             reason, " +
                    "             recorded, " +
                    "             typeName, " +
                    "             refTable, " +
                    "             InvoiceDate, " +
                    "             InvoiceNo, " +
                    "             TypeLedger " +
                    "      from ViewVoucherNoDetail) a WHERE CompanyID = :companyID ");
                params.put("companyID", companyID);
                if (phienSoLamViec == 0) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,noFbook DESC";
                    sql.append("and (TypeLedger = 0 or TypeLedger = 2) ");
                } else if (phienSoLamViec == 1) {
                    orderBy = " order by Date DESC ,PostedDate DESC ,NoMBook DESC";
                    sql.append("and (TypeLedger = 1 or TypeLedger = 2) ");
                }
                if (recorded != null) {
                    if (recorded) {
                        sql.append("and Recorded = 1 ");
                    } else {
                        sql.append("and (Recorded = 0 or Recorded is null) ");
                    }
                }
                if (!StringUtils.isEmpty(fromDate)) {
                    sql.append("and InvoiceDate >= :fromDate ");
                    params.put("fromDate", fromDate);
                }
                if (!StringUtils.isEmpty(toDate)) {
                    LocalDate localDate = LocalDate.parse(toDate);
                    localDate = localDate.plusDays(1);
                    sql.append("and InvoiceDate < :toDate ");
                    params.put("toDate", localDate);
                }
                countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
                Common.setParams(countQuerry, params);
                total = (Number) countQuerry.getSingleResult();
                if (total.longValue() > 0) {
                    Query query = entityManager.createNativeQuery("SELECT  id, " +
                        "       typeID, " +
                        "       typeGroupID, " +
                        "       companyID, " +
                        "       noMBook, " +
                        "       noFBook, " +
                        "       date, " +
                        "       postedDate, " +
                        "       reason, " +
                        "       recorded, " +
                        "       typeName, " +
                        "     NULL as  refTable " + sql.toString() + orderBy, "UtilitiesSearchVoucherDTO");
                    Common.setParamsWithPageable(query, params, pageable, total);
                    lst = query.getResultList();
                }
                break;
            default:

                break;
        }
        return new PageImpl<>(((List<ViewVoucherDTO>) lst), pageable, total.longValue());
    }

    @Override
    public void resetNo(List<ViewVoucherDTO> viewVoucherDTOS, Integer phienSoLamViec, Boolean onlyOneBook) {
        List<List<ViewVoucherDTO>> smallerLists = Lists.partition(viewVoucherDTOS, 500);
        if (onlyOneBook) {
            for (List<ViewVoucherDTO> lst : smallerLists) {
                StringBuilder sql = new StringBuilder();
                sql.append("DISABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                Map<String, Object> params = new HashMap<>();
                int i = 0;
                for (ViewVoucherDTO item : lst) {
                    sql.append("Update " + item.getRefTable() + " set No = :No" + i + " where ID = :id" + i + " ;");
                    params.put("No" + i, item.getNoNew());
                    params.put("id" + i, item.getId());
                    i++;
                }
                sql.append("ENABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                Query query = entityManager.createNativeQuery(sql.toString());
                Common.setParams(query, params);
                query.executeUpdate();
            }
        } else {
            if (phienSoLamViec.equals(vn.softdreams.ebweb.service.util.Constants.TypeLedger.FINANCIAL_BOOK)) {

                for (List<ViewVoucherDTO> lst : smallerLists) {
                    StringBuilder sql = new StringBuilder();
                    Map<String, Object> params = new HashMap<>();
                    int i = 0;
                    sql.append("DISABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                    for (ViewVoucherDTO item : lst) {
                        sql.append("Update " + item.getRefTable() + " set NoFBook = :NoFBook" + i + " where ID = :id" + i + " ;");
                        params.put("NoFBook" + i, item.getNoNew());
                        params.put("id" + i, item.getId());
                        i++;
                    }
                    sql.append("ENABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                    Query query = entityManager.createNativeQuery(sql.toString());
                    Common.setParams(query, params);
                    query.executeUpdate();
                }
                String sql = "UPDATE RepositoryLedger set NoFBook =?  where ReferenceID =? ; " +
                    "UPDATE GeneralLedger set NoFBook =? where ReferenceID =? ; " +
                    "UPDATE ToolLedger set NoFBook =? where ReferenceID =? ; " +
                    "UPDATE FixedAssetLedger set NoFBook =? where ReferenceID =? ; " ;
                int[][] ints = jdbcTemplate.batchUpdate(sql, viewVoucherDTOS, Constants.BATCH_SIZE, (ps, detail) -> {
                    setParam(ps, 1, detail.getNoNew());
                    setParam(ps, 2, detail.getId());
                    setParam(ps, 3, detail.getNoNew());
                    setParam(ps, 4, detail.getId());
                    setParam(ps, 5, detail.getNoNew());
                    setParam(ps, 6, detail.getId());
                    setParam(ps, 7, detail.getNoNew());
                    setParam(ps, 8, detail.getId());
                });
            } else {
                for (List<ViewVoucherDTO> lst : smallerLists) {
                    StringBuilder sql = new StringBuilder();
                    Map<String, Object> params = new HashMap<>();
                    int i = 0;
                    sql.append("DISABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                    for (ViewVoucherDTO item : lst) {
                        sql.append("Update " + item.getRefTable() + " set NoMBook = :NoMBook" + i + " where ID = :id" + i + " ;");
                        params.put("NoMBook" + i, item.getNoNew());
                        params.put("id" + i, item.getId());
                        i++;
                    }
                    sql.append("ENABLE TRIGGER ALL ON " + lst.get(0).getRefTable() + " ;");
                    Query query = entityManager.createNativeQuery(sql.toString());
                    Common.setParams(query, params);
                    query.executeUpdate();
                }
                String sql = "UPDATE RepositoryLedger set NoMBook =?  where ReferenceID =? ; " +
                    "UPDATE GeneralLedger set NoMBook =? where ReferenceID =? ; " +
                    "UPDATE ToolLedger set NoMBook =? where ReferenceID =? ; " +
                    "UPDATE FixedAssetLedger set NoMBook =? where ReferenceID =? ; " ;
                int[][] ints = jdbcTemplate.batchUpdate(sql, viewVoucherDTOS, Constants.BATCH_SIZE, (ps, detail) -> {
                    setParam(ps, 1, detail.getNoNew());
                    setParam(ps, 2, detail.getId());
                    setParam(ps, 3, detail.getNoNew());
                    setParam(ps, 4, detail.getId());
                    setParam(ps, 5, detail.getNoNew());
                    setParam(ps, 6, detail.getId());
                    setParam(ps, 7, detail.getNoNew());
                    setParam(ps, 8, detail.getId());
                });
            }
        }
    }

    @Override
    public void updateTableRecord(List<ViewVoucherNo> lstViewVoucherNos) {
        Map<String, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstViewVoucherNos.stream().collect(Collectors.groupingBy(w -> w.getRefTable()));
        StringBuilder sql = new StringBuilder();
        for (Map.Entry<String, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            List<UUID> uuidList = listEntry.getValue().stream().map(n -> n.getRefID()).collect(Collectors.toList());
            sql.append(" Update " + listEntry.getKey() + " Set Recorded = 1 where id in (");
            for (UUID uuid : uuidList) {
                sql.append("'" + Utils.uuidConvertToGUID(uuid).toString() + "'");
                if (uuidList.indexOf(uuid) == uuidList.size() - 1) {
                    sql.append(");");
                } else {
                    sql.append(",");
                }
            }
        }
        Query query = entityManager.createNativeQuery(sql.toString());
        query.executeUpdate();
    }

    void setParam(PreparedStatement ps, int index, UUID value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.NVARCHAR);
        } else {
            ps.setString(index, Utils.uuidConvertToGUID(value).toString());
        }
    }

    void setParam(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    void setParam(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.NVARCHAR);
        } else {
            ps.setString(index, value);
        }
    }

    void setParam(PreparedStatement ps, int index, LocalDate value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.NVARCHAR);
        } else {
            ps.setString(index, value.toString());
        }
    }

    void setParam(PreparedStatement ps, int index, BigDecimal value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DECIMAL);
        } else {
            ps.setBigDecimal(index, value);
        }
    }
}
