package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.PPInvoiceRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class PPInvoiceRepositoryImpl implements PPInvoiceRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<PPInvoice> findAll(Pageable pageable, SearchVoucher searchVoucher, String formality) {
        StringBuilder sql = new StringBuilder();
        List<PPInvoice> ppInvoices = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM PPInvoice WHERE 1 = 1 ");
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (formality != null) {
            sql.append("AND IsImportPurchase = :formality ");
            params.put("formality", formality);
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }

        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        sql.append("AND BillReceived = 0 ");
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + Common.addSort(pageable.getSort()), PPInvoice.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            ppInvoices = query.getResultList();
        }
        return new PageImpl<>(ppInvoices, pageable, total.longValue());
    }

    @Override
    public Page<ReceiveBillSearchDTO> findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher, String formality, Boolean isNoMBook, UUID orgID) {
        StringBuilder sql = new StringBuilder();
        StringBuilder selectSql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ReceiveBillSearchDTO> listReceiveBillSearchDTO = new ArrayList<>();
        selectSql.append("pp.id, ppd.id as pPID, pp.typeID, pp.Date, pp.PostedDate, pp.NoFBook, pp.NoMBook, ppd.description, ppd.Amount, ppd.VATRate, ppd.VATAccount, ppd.InvoiceTemplate, ppd.InvoiceSeries, ppd.InvoiceNo, ppd.InvoiceDate, ppd.GoodsServicePurchaseID, ppd.AccountingObjectID ");
        sql.append("FROM PPInvoice pp INNER JOIN PPInvoiceDetail ppd on pp.ID = ppd.PPInvoiceID WHERE pp.BillReceived = 0 AND pp.CompanyID = :orgID ");
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (formality != null) {
            sql.append("AND IsImportPurchase = :formality ");
            params.put("formality", formality);
        }
        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND pp.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        params.put("orgID", orgID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString() + " ORDER BY DATE DESC, PostedDate DESC, NoMBook DESC", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString() + " ORDER BY DATE DESC, PostedDate DESC, NoFBook DESC", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            }
        }
        return new PageImpl<>(listReceiveBillSearchDTO, pageable, total.longValue());
    }

    @Override
    public Page<ReceiveBillSearchDTO> findAllReceiveBillDTO(Pageable pageable, SearchVoucher searchVoucher, Boolean isNoMBook, UUID orgID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<ReceiveBillSearchDTO> listReceiveBillSearchDTO = new ArrayList<>();
        sql.append("SELECT pp.id, ppd.id as pPID, pp.TypeID, pp.Date, pp.PostedDate, pp.NoFBook, pp.NoMBook, ppd.description, ppd.Amount as amount, ppd.VATRate, ppd.VATAccount, ppd.InvoiceTemplate, ppd.InvoiceSeries, ppd.InvoiceNo, ppd.InvoiceDate, ppd.GoodsServicePurchaseID, ppd.AccountingObjectID ");
//        selectSql2.append("pp.id, ppd.id as pPID, pp.Date, pp.PostedDate, pp.NoMBook, pp.NoFBook, ppd.description, pp.TotalAmount, ppd.VATRate, ppd.VATAccount, ppd.InvoiceType, ppd.InvoiceSeries, ppd.InvoiceNo, ppd.InvoiceDate, ppd.GoodsServicePurchaseID, ppd.AccountingObjectID ");
        sql.append("FROM PPInvoice pp INNER JOIN PPInvoiceDetail ppd on pp.ID = ppd.PPInvoiceID WHERE pp.BillReceived = 0 AND pp.CompanyID = :orgID ");
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }
        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND pp.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        sql.append("UNION ALL ");
        sql.append("SELECT pp.id, ppd.id as pPID, pp.TypeID, pp.Date, pp.PostedDate, pp.NoFBook, pp.NoMBook, ppd.description, ppd.Amount as amount, ppd.VATRate, ppd.VATAccount, ppd.InvoiceTemplate, ppd.InvoiceSeries, ppd.InvoiceNo, ppd.InvoiceDate, ppd.GoodsServicePurchaseID, ppd.AccountingObjectID ");
        sql.append("FROM PPService pp INNER JOIN PPServiceDetail ppd on pp.ID = ppd.PPServiceID WHERE pp.BillReceived = 0 AND pp.CompanyID = :orgID ");
        if (searchVoucher.getFromDate() != null) {
            sql.append("AND Date >= :fromDate ");
            params.put("fromDate", searchVoucher.getFromDate());
        }
        if (searchVoucher.getToDate() != null) {
            sql.append("AND Date <= :toDate ");
            params.put("toDate", searchVoucher.getToDate());
        }

        if (searchVoucher.getAccountingObjectID() != null) {
            sql.append("AND pp.accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
        }
        params.put("orgID", orgID);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            if (Boolean.TRUE.equals(isNoMBook)) {
                Query query = entityManager.createNativeQuery(sql.toString() + " ORDER BY Date DESC, PostedDate DESC, NoMBook DESC ", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(sql.toString() + " ORDER BY Date DESC, PostedDate DESC, NoFBook DESC ", "ReceiveBillSearchDTO");
                Utils.setParamsWithPageable(query, params, pageable, total);
                listReceiveBillSearchDTO = query.getResultList();
            }
        }
        return new PageImpl<>(listReceiveBillSearchDTO, pageable, total.longValue());
    }

    @Override
    public void updateNhanHoaDon(List<UUID> listIDPPInvoice) {
        String sql = "UPDATE PPInvoice SET BillReceived = 1 WHERE id = ?;";
        jdbcTemplate.batchUpdate(sql, listIDPPInvoice, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
        });
    }

    @Override
    public void updateQuantity(UUID refID, BigDecimal quantity, Boolean statusMinus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("UPDATE PPInvoiceDetail ");
        if (statusMinus) {
            sql.append(" SET Quantity = Quantity - :quantity ");
        } else {
            sql.append(" SET Quantity = Quantity + :quantity ");
        }
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND ID in (:id) ");
        params.put("quantity", quantity);
        params.put("id", refID);

        Query countQuery = entityManager.createNativeQuery(sql.toString());
        Common.setParams(countQuery, params);
        countQuery.executeUpdate();
    }

    @Override
    public Page<PPInvoiceSearchDTO> searchPPInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Integer status, String keySearch, boolean isNoMBook, UUID companyID, UUID employeeId, Boolean isRSI) {
        List<PPInvoiceSearchDTO> ppInvoiceSearchDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String str = " left join Type type on ppi.TypeID = type.ID ";
        Number total = getQueryString(accountingObjectID, status, currencyID, employeeId, fromDate, toDate, keySearch, sql, companyID, isNoMBook, params, isRSI, str);
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT ");
        sqlSelect.append("ppi.id, ");
        sqlSelect.append("ppi.date, ");
        sqlSelect.append("ppi.postedDate, ");
        sqlSelect.append("ppi.typeLedger, ");
        if (isNoMBook) {
            sqlSelect.append("ppi.noMBook as no, ");
        } else {
            sqlSelect.append("ppi.noFBook as no, ");
        }
        sqlSelect.append("ppi.accountingObjectName, ");
        sqlSelect.append("ppi.totalAmount, ");
        sqlSelect.append("ppi.totalDiscountAmount, ");
        sqlSelect.append("ppi.totalVATAmount, ");
        sqlSelect.append("ppi.billReceived, ");
        sqlSelect.append("ppi.isImportPurchase, ");
        sqlSelect.append("ppi.recorded, ");
        sqlSelect.append("ppi.typeId, ");
        sqlSelect.append("ppi.currencyId, ");
        sqlSelect.append("(select SUBSTRING((SELECT DISTINCT ', ' + ppid.invoiceNo FROM dbo.PPInvoiceDetail ppid where ppid.PPInvoiceID = ppi.id FOR XML PATH ('') ), 2, 9999)) as ppOrderNo, ");
        sqlSelect.append("type.TypeName as typeIdStr, ");
        sqlSelect.append("ppi.paymentVoucherId, ");
        sqlSelect.append("ppi.noFBook, ");
        sqlSelect.append("ppi.noMBook, ");
        sqlSelect.append("ppi.rsInwardOutwardId ");

        if (total.longValue() > 0) {

            Query sumQuery = entityManager.createNativeQuery(new StringBuilder("Select sum(a.sumTotalAmount) from (select iif(ppi.IsImportPurchase = 1, (COALESCE(ppi.totalAmount, 0) - COALESCE( ppi.TotalDiscountAmount, 0)),  (COALESCE(ppi.totalAmount, 0) - COALESCE( ppi.TotalDiscountAmount, 0) + COALESCE( ppi.TotalVATAmount, 0)))  as sumTotalAmount ")
                .append(sql).append(") a").toString());
            Common.setParams(sumQuery, params);

            BigDecimal sumTotalAmount = (BigDecimal) sumQuery.getSingleResult();
            Query query = entityManager.createNativeQuery(sqlSelect.toString() + sql.toString() + " order by Date desc, NoFbook DESC, NoMbook DESC ", "PPInvoiceSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            ppInvoiceSearchDTOS = query.getResultList();
            // set tổng vào phần tử đầu tiên
            if (ppInvoiceSearchDTOS.size() > 0) {
                ppInvoiceSearchDTOS.get(0).setSumTotalAmount(sumTotalAmount);
            }
        }
        if (pageable == null) {
            return new PageImpl<>(ppInvoiceSearchDTOS);
        }
        return new PageImpl<>(ppInvoiceSearchDTOS, pageable, total.longValue());
    }

    @Override
    public PPInvoiceDTO getPPInvoiceById(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceDTO> ppInvoiceDTOS = new ArrayList<>();
        params.put("id", id);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("pp.id                                                                          , ");
        sqlBuilder.append("pp.companyId                                                                   , ");
        sqlBuilder.append("pp.typeId                                                                      , ");
        sqlBuilder.append("pp.date                                                                        , ");
        sqlBuilder.append("pp.postedDate                                                                  , ");
        sqlBuilder.append("pp.typeLedger                                                                  , ");
        sqlBuilder.append("pp.noFBook                                                                     , ");
        sqlBuilder.append("pp.noMBook                                                                     , ");
        sqlBuilder.append("pp.rsInwardOutwardId                                                           , ");
        sqlBuilder.append("pp.paymentVoucherId                                                            , ");
        sqlBuilder.append("pp.accountingObjectId                                                          , ");
        sqlBuilder.append("pp.accountingObjectName                                                        , ");
        sqlBuilder.append("pp.accountingObjectAddress                                                     , ");
        sqlBuilder.append("pp.companyTaxCode                                                              , ");
        sqlBuilder.append("pp.contactName                                                                 , ");
        sqlBuilder.append("pp.billReceived                                                                , ");
        sqlBuilder.append("pp.currencyId                                                                  , ");
        sqlBuilder.append("pp.exchangeRate                                                                , ");
        sqlBuilder.append("pp.paymentClauseId                                                             , ");
        sqlBuilder.append("pp.dueDate                                                                     , ");
        sqlBuilder.append("pp.transportMethodId                                                           , ");
        sqlBuilder.append("pp.employeeId                                                                  , ");
        sqlBuilder.append("pp.isImportPurchase                                                            , ");
        sqlBuilder.append("pp.storedInRepository                                                          , ");
        sqlBuilder.append("pp.totalAmount                                                                 , ");
        sqlBuilder.append("pp.totalAmountOriginal                                                         , ");
        sqlBuilder.append("pp.totalImportTaxAmount                                                        , ");
        sqlBuilder.append("pp.totalImportTaxAmountOriginal                                                , ");
        sqlBuilder.append("pp.totalVATAmount                                                              , ");
        sqlBuilder.append("pp.totalVATAmountOriginal                                                      , ");
        sqlBuilder.append("pp.totalDiscountAmount                                                         , ");
        sqlBuilder.append("pp.totalDiscountAmountOriginal                                                 , ");
        sqlBuilder.append("pp.totalInwardAmount                                                           , ");
        sqlBuilder.append("pp.totalInwardAmountOriginal                                                   , ");
        sqlBuilder.append("pp.totalSpecialConsumeTaxAmount                                                , ");
        sqlBuilder.append("pp.totalSpecialConsumeTaxAmountOriginal                                        , ");
        sqlBuilder.append("pp.matchDate                                                                   , ");
        sqlBuilder.append("pp.templateID                                                                  , ");
        sqlBuilder.append("pp.recorded                                                                    , ");
        sqlBuilder.append("pp.totalFreightAmount                                                          , ");
        sqlBuilder.append("pp.totalFreightAmountOriginal                                                  , ");
        sqlBuilder.append("pp.totalImportTaxExpenseAmount                                                 , ");
        sqlBuilder.append("pp.totalImportTaxExpenseAmountOriginal                                         , ");
        sqlBuilder.append("rs.noFBook                                                         as noFBookRs, ");
        sqlBuilder.append("pp.reason                                                                      , ");
        sqlBuilder.append("rs.reason                                                           as reasonRs, ");
        sqlBuilder.append("pp.numberAttach                                                                , ");
        sqlBuilder.append("rs.numberAttach                                               as numberAttachRs, ");
        sqlBuilder.append("rs.noMBook                                                         as noMBookRs, ");
        sqlBuilder.append("mcp.Receiver                                                as receiverUserName, ");
        sqlBuilder.append("mbt.bankAccountDetailID                                     as accountPaymentId, ");
        sqlBuilder.append("mbt.BankName                                              as accountPaymentName, ");
        sqlBuilder.append("aoba.id                                                    as accountReceiverId, ");
        sqlBuilder.append("aoba.bankName                                            as accountReceiverName, ");
        sqlBuilder.append("mbc.creditCardNumber                                                           , ");
        sqlBuilder.append("mbc.accountingObjectBankAccountDetailID                as bankAccountReceiverId, ");
        sqlBuilder.append("mbc.accountingObjectBankName                         as bankAccountReceiverName, ");
        sqlBuilder.append("CONCAT(mcp.NoFBook, mbt.NoFBook, mbc.NoFBook)                   as otherNoFBook, ");
        sqlBuilder.append("CONCAT(mcp.reason, mbt.reason, mbc.reason)                       as otherReason, ");
        sqlBuilder.append("mcp.numberAttach                                           as otherNumberAttach, ");
        sqlBuilder.append("CONCAT(mcp.NoMBook, mbt.NoMBook, mbc.NoMBook)                   as otherNoMBook, ");
        sqlBuilder.append("mbt.receiver                                         as accountReceiverFullName, ");
        sqlBuilder.append("mbt.identificationNo                                        as identificationNo, ");
        sqlBuilder.append("mbt.issueDate                                                      as issueDate, ");
        sqlBuilder.append("mbt.issueBy                                                           as issueBy ");

        sqlBuilder.append("FROM PPINVOICE pp ");
        sqlBuilder.append("LEFT JOIN RSINWARDOUTWARD rs ON pp.rsInwardOutwardId  = rs.id  ");
        sqlBuilder.append("LEFT JOIN MCPayment mcp ON pp.paymentVoucherId = mcp.id and pp.typeId = 211 ");
        sqlBuilder.append("LEFT JOIN MBTellerPaper mbt ON pp.paymentVoucherId = mbt.id and pp.typeId in (212, 213, 215) ");
        sqlBuilder.append("LEFT JOIN MBCreditCard mbc ON pp.paymentVoucherId = mbc.id and pp.typeId = 214 ");
        sqlBuilder.append("LEFT JOIN AccountingObjectBankAccount aoba ON mbt.accountingObjectBankAccount = aoba.id and pp.typeId in (212, 213, 215) ");

        sqlBuilder.append("WHERE pp.id = :id ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetailDTO");
        Common.setParams(query, params);
        ppInvoiceDTOS = query.getResultList();

        return ppInvoiceDTOS.get(0);
    }

    @Override
    public List<PPInvoiceDetailDTO> getPPInvoiceDetailByIdPPInvoice(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceDetailDTO> ppInvoiceDetailDTOS = new ArrayList<>();
        params.put("id", id);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("a.id                                               , ");
        sqlBuilder.append("b.materialGoodsCode                                , ");
        sqlBuilder.append("a.description                  as materialGoodsName, ");
        sqlBuilder.append("c.repositoryCode                                   , ");
        sqlBuilder.append("a.debitAccount                                     , ");
        sqlBuilder.append("a.creditAccount                                    , ");
        sqlBuilder.append("d.accountingObjectCode                             , ");
        sqlBuilder.append("e.unitName                                         , ");
        sqlBuilder.append("a.quantity                                         , ");
        sqlBuilder.append("a.unitPriceOriginal                                , ");
        sqlBuilder.append("a.unitPrice                                        , ");
        sqlBuilder.append("f.unitName                          as mainUnitName, ");
        sqlBuilder.append("a.mainConvertRate                                  , ");
        sqlBuilder.append("a.formula                                          , ");
        sqlBuilder.append("a.mainQuantity                                     , ");
        sqlBuilder.append("a.mainUnitPrice                                    , ");
        sqlBuilder.append("a.amountOriginal                                   , ");
        sqlBuilder.append("a.amount                                           , ");
        sqlBuilder.append("a.discountRate                                     , ");
        sqlBuilder.append("a.discountAmountOriginal                           , ");
        sqlBuilder.append("a.discountAmount                                   , ");
        sqlBuilder.append("a.freightAmount                                    , ");
        sqlBuilder.append("a.importTaxExpenseAmount                           , ");
        sqlBuilder.append("a.inwardAmount                                     , ");
        sqlBuilder.append("a.lotNo                                            , ");
        sqlBuilder.append("a.expiryDate                                       , ");
        sqlBuilder.append("a.ppOrderDetailId                                  , ");
        sqlBuilder.append("a.vatDescription                                   , ");
        sqlBuilder.append("a.customUnitPrice                                  , ");
        sqlBuilder.append("a.importTaxRate                                    , ");
        sqlBuilder.append("a.importTaxAmount                                  , ");
        sqlBuilder.append("a.importTaxAccount                                 , ");
        sqlBuilder.append("a.specialConsumeTaxRate                            , ");
        sqlBuilder.append("a.specialConsumeTaxAmount                          , ");
        sqlBuilder.append("a.specialConsumeTaxAccount                         , ");
        sqlBuilder.append("a.vatRate                                          , ");
        sqlBuilder.append("a.vatAmountOriginal                                , ");
        sqlBuilder.append("a.vatAmount                                        , ");
        sqlBuilder.append("a.vatAccount                                       , ");
        sqlBuilder.append("a.deductionDebitAccount                            , ");
        sqlBuilder.append("a.invoiceTemplate                                  , ");
        sqlBuilder.append("a.invoiceSeries                                    , ");
        sqlBuilder.append("a.invoiceNo                                        , ");
        sqlBuilder.append("a.invoiceDate                                      , ");
        sqlBuilder.append("g.goodsServicePurchaseCode                         , ");
        sqlBuilder.append("h.expenseItemCode                                  , ");
        sqlBuilder.append("i.costSetCode                                      , ");
        sqlBuilder.append("j.noMBook                                          , ");
        sqlBuilder.append("k.budgetItemCode                                   , ");
        sqlBuilder.append("l.organizationUnitCode                             , ");
        sqlBuilder.append("m.statisticsCode                                   , ");
        sqlBuilder.append("n.no as                                   ppOrderNo, ");
        sqlBuilder.append("c.repositoryName                                     ");

        sqlBuilder.append("FROM PPINVOICEDETAIL a ");
        sqlBuilder.append("LEFT JOIN MATERIALGOODS b ON a.MaterialGoodsId = b.id  ");
        sqlBuilder.append("LEFT JOIN REPOSITORY c ON a.RepositoryID = c.id  ");
        sqlBuilder.append("LEFT JOIN ACCOUNTINGOBJECT d ON a.AccountingObjectID = d.id  ");
        sqlBuilder.append("LEFT JOIN UNIT e ON a.UnitID = e.id  ");
        sqlBuilder.append("LEFT JOIN UNIT f ON a.MainUnitID = f.id  ");
        sqlBuilder.append("LEFT JOIN GOODSSERVICEPURCHASE g ON a.GoodsServicePurchaseID = g.id  ");
        sqlBuilder.append("LEFT JOIN EXPENSEITEM h ON a.ExpenseItemID = h.id  ");
        sqlBuilder.append("LEFT JOIN COSTSET i ON a.CostSetID = i.id  ");
        sqlBuilder.append("LEFT JOIN EMCONTRACT j ON a.contractID = j.id  ");
        sqlBuilder.append("LEFT JOIN BUDGETITEM k ON a.BudgetItemID = k.id  ");
        sqlBuilder.append("LEFT JOIN EBORGANIZATIONUNIT l ON a.departmentId = l.id  ");
        sqlBuilder.append("LEFT JOIN STATISTICSCODE m ON a.StatisticCodeID = m.id  ");
        sqlBuilder.append("LEFT JOIN PPORDER n on n.id = a.PPOrderID ");


        sqlBuilder.append("WHERE a.PPInvoiceID = :id ");
        sqlBuilder.append("order by a.OrderPriority asc ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetailListDTO");
        Common.setParams(query, params);
        ppInvoiceDetailDTOS = query.getResultList();

        return ppInvoiceDetailDTOS;
    }

    @Override
    public List<PPInvoiceDetail1DTO> getPPInvoiceDetail1ByPPInvoiceId(UUID ppInvoiceId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(
            "select * " +
                "from ((select ppdtl.Description, " +
                "              ppdtl.CreditAccount, " +
                "              ppdtl.DebitAccount, " +
                "              (ppdtl.Amount - ppdtl.DiscountAmount) as Amount, " +
                "              (ppdtl.AmountOriginal - ppdtl.DiscountAmountOriginal) as AmountOriginal, " +
                "              mg.MaterialGoodsCode, " +
                "              ppdtl.OrderPriority " +
                "       from PPInvoiceDetail ppdtl " +
                "                left join PPInvoice pp on pp.id = ppdtl.PPInvoiceID " +
                "                left join MaterialGoods mg on ppdtl.MaterialGoodsID = mg.ID " +
                "       where ppdtl.PPInvoiceID = :id) " +
                "      UNION " +
                "      (select ppdtl1.VATDescription                                                          Description, " +
                "              IIF(pp1.IsImportPurchase = 0, ppdtl1.DeductionDebitAccount, ppdtl1.VATAccount) CreditAccount, " +
                "              IIF(pp1.IsImportPurchase = 0, ppdtl1.VATAccount, ppdtl1.DeductionDebitAccount) DebitAccount, " +
                "              ppdtl1.VATAmount                                                               Amount, " +
                "              ppdtl1.VATAmountOriginal                                                       AmountOriginal, " +
                "              mg.MaterialGoodsCode, " +
                "              ppdtl1.OrderPriority " +
                "       from PPInvoiceDetail ppdtl1 " +
                "                left join PPInvoice pp1 on pp1.id = ppdtl1.PPInvoiceID " +
                "                left join MaterialGoods mg on ppdtl1.MaterialGoodsID = mg.ID " +
                "       where ppdtl1.PPInvoiceID = :id " +
                "         and COALESCE(ppdtl1.VATAmountOriginal, 0) <> 0)) details " +
                "order by details.OrderPriority");
        List<PPInvoiceDetail1DTO> ppInvoiceDetail1DTOs;
        Map<String, Object> params = new HashMap<>();
        params.put("id", ppInvoiceId);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetail1DTO");
        Common.setParams(query, params);
        ppInvoiceDetail1DTOs = query.getResultList();
        return ppInvoiceDetail1DTOs;
    }

    /**
     * @param paymentVoucherID
     * @return
     * @Auhor Hautv
     */
    @Override
    public List<PPInvoiceDetailDTO> getPPInvoiceDetailByPaymentVoucherID(UUID paymentVoucherID) {
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceDetailDTO> ppInvoiceDetailDTOS = new ArrayList<>();
        params.put("paymentVoucherID", paymentVoucherID);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("a.id                                               , ");
        sqlBuilder.append("b.materialGoodsCode                                , ");
        sqlBuilder.append("a.description                  as materialGoodsName, ");
        sqlBuilder.append("c.repositoryCode                                   , ");
        sqlBuilder.append("c.repositoryName                                   , ");
        sqlBuilder.append("a.debitAccount                                     , ");
        sqlBuilder.append("a.creditAccount                                    , ");
        sqlBuilder.append("d.accountingObjectCode                             , ");
        sqlBuilder.append("e.unitName                                         , ");
        sqlBuilder.append("a.quantity                                         , ");
        sqlBuilder.append("a.unitPriceOriginal                                , ");
        sqlBuilder.append("a.unitPrice                                        , ");
        sqlBuilder.append("f.unitName                          as mainUnitName, ");
        sqlBuilder.append("a.mainConvertRate                                  , ");
        sqlBuilder.append("a.formula                                          , ");
        sqlBuilder.append("a.mainQuantity                                     , ");
        sqlBuilder.append("a.mainUnitPrice                                    , ");
        sqlBuilder.append("a.amountOriginal                                   , ");
        sqlBuilder.append("a.amount                                           , ");
        sqlBuilder.append("a.discountRate                                     , ");
        sqlBuilder.append("a.discountAmountOriginal                           , ");
        sqlBuilder.append("a.discountAmount                                   , ");
        sqlBuilder.append("a.freightAmount                                    , ");
        sqlBuilder.append("a.importTaxExpenseAmount                           , ");
        sqlBuilder.append("a.inwardAmount                                     , ");
        sqlBuilder.append("a.lotNo                                            , ");
        sqlBuilder.append("a.expiryDate                                       , ");
        sqlBuilder.append("a.ppOrderDetailId                                  , ");
        sqlBuilder.append("a.vatDescription                                   , ");
        sqlBuilder.append("a.customUnitPrice                                  , ");
        sqlBuilder.append("a.importTaxRate                                    , ");
        sqlBuilder.append("a.importTaxAmount                                  , ");
        sqlBuilder.append("a.importTaxAccount                                 , ");
        sqlBuilder.append("a.specialConsumeTaxRate                            , ");
        sqlBuilder.append("a.specialConsumeTaxAmount                          , ");
        sqlBuilder.append("a.specialConsumeTaxAccount                         , ");
        sqlBuilder.append("a.vatRate                                          , ");
        sqlBuilder.append("a.vatAmountOriginal                                , ");
        sqlBuilder.append("a.vatAmount                                        , ");
        sqlBuilder.append("a.vatAccount                                       , ");
        sqlBuilder.append("a.deductionDebitAccount                            , ");
        sqlBuilder.append("a.invoiceTemplate                                  , ");
        sqlBuilder.append("a.invoiceSeries                                    , ");
        sqlBuilder.append("a.invoiceNo                                        , ");
        sqlBuilder.append("a.invoiceDate                                      , ");
        sqlBuilder.append("g.goodsServicePurchaseCode                         , ");
        sqlBuilder.append("h.expenseItemCode                                  , ");
        sqlBuilder.append("i.costSetCode                                      , ");
        sqlBuilder.append("j.noMBook                                          , ");
        sqlBuilder.append("k.budgetItemCode                                   , ");
        sqlBuilder.append("l.organizationUnitCode                             , ");
        sqlBuilder.append("m.statisticsCode                                   , ");
        sqlBuilder.append("n.no as                                   ppOrderNo  ");

        sqlBuilder.append("FROM PPINVOICEDETAIL a ");
        sqlBuilder.append("LEFT JOIN MATERIALGOODS b ON a.MaterialGoodsId = b.id  ");
        sqlBuilder.append("LEFT JOIN REPOSITORY c ON a.RepositoryID = c.id  ");
        sqlBuilder.append("LEFT JOIN ACCOUNTINGOBJECT d ON a.AccountingObjectID = d.id  ");
        sqlBuilder.append("LEFT JOIN UNIT e ON a.UnitID = e.id  ");
        sqlBuilder.append("LEFT JOIN UNIT f ON a.MainUnitID = f.id  ");
        sqlBuilder.append("LEFT JOIN GOODSSERVICEPURCHASE g ON a.GoodsServicePurchaseID = g.id  ");
        sqlBuilder.append("LEFT JOIN EXPENSEITEM h ON a.ExpenseItemID = h.id  ");
        sqlBuilder.append("LEFT JOIN COSTSET i ON a.CostSetID = i.id  ");
        sqlBuilder.append("LEFT JOIN EMCONTRACT j ON a.contractID = j.id  ");
        sqlBuilder.append("LEFT JOIN BUDGETITEM k ON a.BudgetItemID = k.id  ");
        sqlBuilder.append("LEFT JOIN EBORGANIZATIONUNIT l ON a.departmentId = l.id  ");
        sqlBuilder.append("LEFT JOIN STATISTICSCODE m ON a.StatisticCodeID = m.id  ");
        sqlBuilder.append("LEFT JOIN PPORDER n on n.id = a.PPOrderID ");


        sqlBuilder.append("WHERE a.PPInvoiceID = (select ID from PPInvoice where PaymentVoucherID = :paymentVoucherID) ");
        sqlBuilder.append("order by a.OrderPriority asc ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetailListDTO");
        Common.setParams(query, params);
        ppInvoiceDetailDTOS = query.getResultList();

        return ppInvoiceDetailDTOS;
    }

    @Override
    public PPInvoice findIdByRowNum(Pageable pageable, UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String textSearch, Integer rowNum, UUID companyID, boolean isNoMBook, Boolean isRSI) {
        StringBuilder sql = new StringBuilder();
        PPInvoice ppInvoice = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(accountingObject, status, currency, employee, fromDate, toDate, textSearch, sql, companyID, isNoMBook, params, isRSI, null);
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (ORDER BY date DESC , NoFbook DESC, NoMbook DESC ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, PPInvoice.class);
            setParams(query, params);
            ppInvoice = (PPInvoice) query.getSingleResult();
        }
        return ppInvoice;
    }

    @Override
    public Page<ViewPPInvoiceDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String soLamViec, String currencyID) {
        StringBuilder sql = new StringBuilder();
        List<ViewPPInvoiceDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from PPInvoiceDetail a" +
            "         left join " +
            "     MaterialGoods ON MaterialGoods.ID = a.MaterialGoodsID " +
            "         right join PPInvoice on PPInvoice.ID = a.PPInvoiceID " +
            "where PPInvoice.Recorded = 1 AND  PPInvoice.CompanyID = :companyID ");
        params.put("companyID", org);
        sql.append("  and ( PPInvoice.TypeLedger = 2 or PPInvoice.TypeLedger = :typeLedger) ");
        params.put("typeLedger", soLamViec);
        if (currencyID != null) {
            sql.append("  and PPInvoice.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            sql.append("and a.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            sql.append("and Date <= :toDate ");
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select a.PPInvoiceID as ID," +
                "       a.ID as pPInvoiceDetailID," +
                "       TypeID," +
                "       PPInvoice.CompanyID," +
                "       case when :typeLedger = 0 then PPInvoice.NoFBook else PPInvoice.NoMBook end as No, " +
                "       Date," +
                "       a.MaterialGoodsID," +
                "       MaterialGoodsCode," +
                "       a.Description," +
                "       Reason," +
                "       a.Quantity," +
                "       a.UnitPrice," +
                "       a.Amount," +
                "       a.unitID," +
                "       a.unitPriceOriginal," +
                "       a.mainQuantity," +
                "       a.mainUnitID," +
                "       a.mainUnitPrice," +
                "       a.mainConvertRate," +
                "       a.formula," +
                "       a.vATDescription," +
                "       a.amountOriginal," +
                "       a.discountRate," +
                "       a.discountAmount," +
                "       a.discountAmountOriginal," +
                "       a.vATRate," +
                "       a.vATAmount," +
                "       a.vATAmountOriginal," +
                "       a.lotNo," +
                "       a.expiryDate " +
                sql.toString() + " order by Date DESC ,No DESC", "PPInvoiceDTOPopup");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<ViewPPInvoiceDTO>) lst), pageable, total.longValue());
    }

    public static String addSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null)
            return "";
        for (Sort.Order order : sort) {

            orderSql.append("ORDER BY ");
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            break;
        }
        return orderSql.toString();
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public void addDateSearchCustom(String fromDate, String toDate, Map<String, Object> params,
                                    StringBuilder sqlBuilder, String columnName, String param) {
        if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append("AND :from" + param + "<= convert(varchar," + columnName + "," + 112 + ") AND :to" + param +
                " >= convert(varchar," + columnName + "," + 112 + ") ");
            params.put("from" + param, fromDate);
            params.put("to" + param, toDate);
        } else if (!Strings.isNullOrEmpty(fromDate)) {
            sqlBuilder.append("AND :from" + param + " <= convert(varchar," + columnName + "," + 112 + ") ");
            params.put("from" + param, fromDate);
        } else if (!Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append("AND :to" + param + " >= convert(varchar," + columnName + "," + 112 + ") ");
            params.put("to" + param, toDate);
        }
    }

    private Number getQueryString(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue, StringBuilder sql, UUID companyID, boolean isNoMBook, Map<String, Object> params, boolean isRSI, String str) {
        sql.append("FROM PPINVOICE ppi ");
        if (!Strings.isNullOrEmpty(str)) {
            sql.append(str);
        }
        sql.append("WHERE 1 = 1 ");
        sql.append(" and ppi.CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (isRSI) {
            sql.append(" and ppi.StoredInRepository = 1 ");
        } else {
            sql.append(" and ppi.StoredInRepository = 0 ");
        }

        if (isNoMBook) {
            sql.append(" and ppi.typeLedger in (1, 2) ");
        } else {
            sql.append(" and ppi.typeLedger in (0, 2) ");
        }
        if (!Strings.isNullOrEmpty(currency)) {
            sql.append("AND ppi.CurrencyID = :currencyID ");
            params.put("currencyID", currency);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("AND ppi.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("AND ppi.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        if (employee != null) {
            sql.append("AND ppi.EmployeeID = :employeeID ");
            params.put("employeeID", employee);
        }
        if (status != null) {
            sql.append("AND ppi.Recorded = :status ");
            params.put("status", status);
        }
        if (accountingObject != null) {
            sql.append("AND ppi.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObject);
        }

        if (!Strings.isNullOrEmpty(searchValue)) {
            sql.append("AND (ppi.NoFbook LIKE :searchValue ");
            sql.append("OR ppi.NoMbook LIKE :searchValue ");
            sql.append("OR ppi.ContactName LIKE :searchValue ");
            sql.append("OR ppi.accountingObjectAddress LIKE :searchValue ");
            sql.append("OR ppi.Reason LIKE :searchValue ");
            sql.append("OR ppi.CompanyTaxCode LIKE :searchValue) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    @Override
    public int checkReferences(UUID id, List<String> referenceTables, String property) {
        if (id == null) return 0;

        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < referenceTables.size(); i++) {
            sql.append(" select count(*) as total from ");
            sql.append(referenceTables.get(i));
            sql.append(" where ");
            sql.append(property);
            sql.append(" = ?1 ");
            sql.append(i == referenceTables.size() - 1 ? "" : " union ");
        }

        Query query = entityManager.createNativeQuery("select sum(total) sum from (" + sql.toString() + ") a");
        query.setParameter(1, id);
        Number sum = (Number) query.getSingleResult();
        return sum != null ? sum.intValue() : -1;
    }

    @Override
    public void updateUnRecord(List<PPInvoice> ppiList) {
        String sql1 = "Update PPINVOICE SET Recorded = 0 WHERE id = ?;" +
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, ppiList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
    }

    @Override
    public void updateUnRecordMCP(List<PPInvoice> ppiList) {
        String sql1 = "Update PPINVOICE SET Recorded = 0 WHERE id = ?;" +
            "Delete GeneralLedger WHERE referenceID = ?;" +
            "Update MCPayment SET Recorded = 0 WHERE id = ?;";
        jdbcTemplate.batchUpdate(sql1, ppiList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail.getPaymentVoucherId()).toString());
        });
    }

    @Override
    public void updateUnRecordMBT(List<PPInvoice> ppiList) {
        String sql1 = "Update PPINVOICE SET Recorded = 0 WHERE id = ?;" +
            "Delete GeneralLedger WHERE referenceID = ?;" +
            "Update MBTellerPaper SET Recorded = 0 WHERE id = ?;";
        jdbcTemplate.batchUpdate(sql1, ppiList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail.getPaymentVoucherId()).toString());
        });
    }

    @Override
    public void updateUnRecordMBC(List<PPInvoice> ppiList) {
        String sql1 = "Update PPINVOICE SET Recorded = 0 WHERE id = ?;" +
            "Delete GeneralLedger WHERE referenceID = ?;" +
            "Update MBCreditCard SET Recorded = 0 WHERE id = ?;";
        jdbcTemplate.batchUpdate(sql1, ppiList, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail.getId()).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail.getPaymentVoucherId()).toString());
        });
    }

    public List<ViewPPInvoiceDTO> getAllPPInvoiceHasRSID(UUID org, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<ViewPPInvoiceDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from PPInvoice a " +
            "where a.rsInwardOutwardID is not null AND a.Recorded = 1 AND  a.CompanyID = :companyID ");
        params.put("companyID", org);
        sql.append("  and ( a.TypeLedger = 2 or a.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select a.ID, " +
                "       a.rsInwardOutwardID " +
                sql.toString(), "ViewPPInvoiceDTO");
            setParams(query, params);
            lst = query.getResultList();
        }
        return lst;
    }

    @Override
    public void updateUnrecord(List<UUID> uuidList) {
        String sql1 = "Update PPInvoice SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<UUID> findAllParentID(List<UUID> listIDPPDetail) {
        String sql = "select DISTINCT(PPInvoiceID) as uuid from PPInvoiceDetail WHERE ID in :listIDPPDetail";
        Map<String, Object> params = new HashMap<>();
        params.put("listIDPPDetail", listIDPPDetail);
        Query query = entityManager.createNativeQuery(sql, "UUIDDTO");
        setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }

    @Override
    public PPServiceCostVoucherDTO getByPPServiceId(UUID ppServiceID) {
        List<PPServiceCostVoucherDTO> costVoucherDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("pps.id, ");
        sql.append("pps.noFBook, ");
        sql.append("pps.noMBook, ");
        sql.append("pps.postedDate, ");
        sql.append("pps.date, ");
        sql.append("pps.accountingObjectID, ");
        sql.append("pps.accountingObjectName, ");
        sql.append("pps.reason, ");
        sql.append("pps.totalAmount, ");
        sql.append("pps.totalAmountOriginal, ");
        sql.append("pps.exchangeRate, ");
        sql.append("pps.totalDiscountAmount, ");
        sql.append("pps.totalDiscountAmountOriginal, ");
        sql.append("pps.totalVATAmount, ");
        sql.append("pps.totalVATAmountOriginal, ");
        sql.append("(SELECT sum(amount) FROM PPInvoiceDetailCost WHERE 1 = 1  ");
        sql.append("AND ppServiceId = pps.id ");
        sql.append(" ) as sumAmount,  ");
        sql.append("pps.exchangeRate ");
        StringBuilder sqlFrom = new StringBuilder();
        sqlFrom.append("from PPSERVICE pps ");
        sqlFrom.append("where pps.isFeightService = 1 ");
        sqlFrom.append(" and pps.id = :id ");
        params.put("id", ppServiceID);
        Query query = entityManager.createNativeQuery(sql.toString() + sqlFrom.toString(), "PPServiceCostVoucherDTO");
        Common.setParams(query, params);
        costVoucherDTOS = query.getResultList();
        if (costVoucherDTOS.size() > 0) {
            return costVoucherDTOS.get(0);
        }
        return null;
    }
//
//    @Override
//    public List<PPInvoiceDetails> findDetailsByListIDMBCreditCard(List<UUID> ppInvoiceList) {
//        StringBuilder sql = new StringBuilder();
//        List<PPInvoiceDetails> ppInvoiceDetails = new ArrayList<PPInvoiceDetails>();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("select * from PPInvoiceDetail where PPInvoiceID IN :ppInvoiceList ");
//        params.put("ppInvoiceList", ppInvoiceList);
//        Query query = entityManager.createNativeQuery(sql.toString(), PPInvoiceDetails.class);
//        setParams(query, params);
//        ppInvoiceDetails = query.getResultList();
//        return ppInvoiceDetails;
//    }
}
