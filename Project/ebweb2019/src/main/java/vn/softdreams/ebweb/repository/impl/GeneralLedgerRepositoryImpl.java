package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPExpenseList;
import vn.softdreams.ebweb.domain.CollectionVoucher;
import vn.softdreams.ebweb.domain.GLCPExpenseList;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.repository.GeneralLedgerRepositoryCustom;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralLedgerRepositoryImpl implements GeneralLedgerRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    /**
     * Add by Hautv
     * @param refID
     * @return
     */
    @Override
    public List<GeneralLedger> findByRefID(UUID refID) {
        StringBuilder sql = new StringBuilder();
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM GeneralLedger WHERE ReferenceID = :ReferenceID");
        params.put("ReferenceID", refID);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        generalLedgers = query.getResultList();
        return generalLedgers;
    }

    /** Add by Hautv
     * @param refID
     * @return
     */
    @Override
    public boolean unrecord(UUID refID) {
        boolean result = true;
        try {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("DELETE FROM GeneralLedger WHERE ReferenceID = :ReferenceID");
            params.put("ReferenceID", refID);
            Query query = entityManager.createNativeQuery(sql.toString());
            setParams(query, params);
            query.executeUpdate();
        }catch (Exception ex){
            result = false;
        }
        return result;
    }

    @Override
    public boolean unrecordList(List<UUID> refID) {
        boolean result = true;
        try {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("DELETE FROM GeneralLedger WHERE ReferenceID IN :ReferenceID");
            params.put("ReferenceID", refID);
            Query query = entityManager.createNativeQuery(sql.toString());
            setParams(query, params);
            query.executeUpdate();
        }catch (Exception ex){
            result = false;
        }
        return result;
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    @Override
    public List<CollectionVoucher> getCollectionVoucher(String date, String currencyID, UUID bankID) {
        StringBuilder sql = new StringBuilder();
        List<CollectionVoucher> collectionVoucherList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("(SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCPaymentDetail ON GeneralLedger.DetailID = dbo.MCPaymentDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCPaymentDetail.IsMatch = 0 " +
            "    GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBDeposit ON GeneralLedger.ReferenceID = dbo.MBDeposit.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBDeposit.IsMatch = 0 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBInternalTransferDetail ON GeneralLedger.DetailID = dbo.MBInternalTransferDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBInternalTransferDetail.IsMatchTo = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, " +
            "         Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCReceiptDetail ON GeneralLedger.DetailID = dbo.MCReceiptDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCReceiptDetail.IsMatch = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBTellerPaper ON GeneralLedger.ReferenceID = dbo.MBTellerPaper.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBTellerPaper.IsMatch = 0 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.GOtherVoucherDetail ON GeneralLedger.DetailID = dbo.GOtherVoucherDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND GOtherVoucherDetail.IsMatch = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, Reason, GeneralLedger.BankAccountDetailID) " +
            "ORDER BY No");
        params.put("BankAccountDetailID", bankID);
        params.put("CurrencyID", currencyID);
        params.put("PostedDate", date);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        collectionVoucherList = query.getResultList();
        return collectionVoucherList;
    }

    @Override
    public List<CollectionVoucher> getSpendingVoucher(String date, String currencyID, UUID bankID) {
        StringBuilder sql = new StringBuilder();
        List<CollectionVoucher> collectionVoucherList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("(SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCPaymentDetail ON GeneralLedger.DetailID = dbo.MCPaymentDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCPaymentDetail.IsMatch = 0 " +
            "    GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBDeposit ON GeneralLedger.ReferenceID = dbo.MBDeposit.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBDeposit.IsMatch = 0 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBInternalTransferDetail ON GeneralLedger.DetailID = dbo.MBInternalTransferDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBInternalTransferDetail.IsMatchTo = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, " +
            "         Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCReceiptDetail ON GeneralLedger.DetailID = dbo.MCReceiptDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCReceiptDetail.IsMatch = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBTellerPaper ON GeneralLedger.ReferenceID = dbo.MBTellerPaper.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBTellerPaper.IsMatch = 0 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason, GeneralLedger.BankAccountDetailID " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description, " +
            "       GeneralLedger.BankAccountDetailID AS BankAccountDetailID " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.GOtherVoucherDetail ON GeneralLedger.DetailID = dbo.GOtherVoucherDetail.ID " +
            "WHERE GeneralLedger.CurrencyID = :CurrencyID AND GeneralLedger.Account LIKE '112%' AND GeneralLedger.PostedDate < :PostedDate " +
            "        AND GeneralLedger.BankAccountDetailID = :BankAccountDetailID AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND GOtherVoucherDetail.IsMatch = 0 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, Reason, GeneralLedger.BankAccountDetailID) " +
            "ORDER BY No");
        params.put("BankAccountDetailID", bankID);
        params.put("CurrencyID", currencyID);
        params.put("PostedDate", date);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        collectionVoucherList = query.getResultList();
        return collectionVoucherList;
    }

    @Override
    public List<CollectionVoucher> getListMatch() {
        StringBuilder sql = new StringBuilder();
        List<CollectionVoucher> collectionVoucherList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("(SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCPaymentDetail ON GeneralLedger.DetailID = dbo.MCPaymentDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCPaymentDetail.IsMatch = 1 " +
            "    GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBDeposit ON GeneralLedger.ReferenceID = dbo.MBDeposit.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBDeposit.IsMatch = 1 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       CASE " +
            "           WHEN SUM(GeneralLedger.CreditAmount) > 0 THEN SUM(GeneralLedger.CreditAmount) " +
            "           WHEN SUM(GeneralLedger.DebitAmount) > 0 THEN SUM(GeneralLedger.DebitAmount) " +
            "        END AS Amount, " +
            "       CASE " +
            "           WHEN SUM(GeneralLedger.CreditAmountOriginal) > 0 THEN SUM(GeneralLedger.CreditAmountOriginal) " +
            "           WHEN SUM(GeneralLedger.DebitAmountOriginal) > 0 THEN SUM(GeneralLedger.DebitAmountOriginal) " +
            "        END AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBInternalTransferDetail ON GeneralLedger.DetailID = dbo.MBInternalTransferDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%' " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL " +
            "  AND (GeneralLedger.CreditAmount > 0 OR GeneralLedger.DebitAmount > 0) " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) " +
            "  AND (MBInternalTransferDetail.IsMatchTo = 1 OR MBInternalTransferDetail.IsMatch = 1) " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCReceiptDetail ON GeneralLedger.DetailID = dbo.MCReceiptDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCReceiptDetail.IsMatch = 1 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBTellerPaper ON GeneralLedger.ReferenceID = dbo.MBTellerPaper.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBTellerPaper.IsMatch = 1 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.DebitAmount) AS Amount, " +
            "       SUM(GeneralLedger.DebitAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.GOtherVoucherDetail ON GeneralLedger.DetailID = dbo.GOtherVoucherDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.DebitAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND GOtherVoucherDetail.IsMatch = 1 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, Reason, " +
            "         GeneralLedger.BankAccountDetailID " +
            "    UNION ALL  " +
            "    SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCPaymentDetail ON GeneralLedger.DetailID = dbo.MCPaymentDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCPaymentDetail.IsMatch = 1 " +
            "    GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBDeposit ON GeneralLedger.ReferenceID = dbo.MBDeposit.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBDeposit.IsMatch = 1 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MCReceiptDetail ON GeneralLedger.DetailID = dbo.MCReceiptDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MCReceiptDetail.IsMatch = 1 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, ExchangeRate, Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.MBTellerPaper ON GeneralLedger.ReferenceID = dbo.MBTellerPaper.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND MBTellerPaper.IsMatch = 1 " +
            "GROUP BY GeneralLedger.TypeID, GeneralLedger.PostedDate, GeneralLedger.No, GeneralLedger.AccountingObjectID, " +
            "         GeneralLedger.ExchangeRate, GeneralLedger.Reason " +
            " UNION ALL " +
            "SELECT GeneralLedger.TypeID AS TypeID, GeneralLedger.PostedDate AS Date, GeneralLedger.No AS No, " +
            "       GeneralLedger.AccountingObjectID AS AccountObject, GeneralLedger.ExchangeRate AS Rate, " +
            "       SUM(GeneralLedger.CreditAmount) AS Amount, " +
            "       SUM(GeneralLedger.CreditAmountOriginal) AS AmountOriginal, GeneralLedger.Reason AS Description " +
            "        " +
            "FROM            dbo.GeneralLedger LEFT JOIN " +
            "                dbo.GOtherVoucherDetail ON GeneralLedger.DetailID = dbo.GOtherVoucherDetail.ID " +
            "WHERE  GeneralLedger.Account LIKE '112%'  " +
            "        AND GeneralLedger.BankAccountDetailID IS NOT NULL AND GeneralLedger.CreditAmount > 0 " +
            "  AND (GeneralLedger.TypeID < 700 OR GeneralLedger.TypeID > 709) AND GOtherVoucherDetail.IsMatch = 1 " +
            "GROUP BY TypeID, PostedDate, No, GeneralLedger.AccountingObjectID, GeneralLedger.ExchangeRate, Reason, " +
            "         GeneralLedger.BankAccountDetailID) " +
            "ORDER BY No");
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        collectionVoucherList = query.getResultList();
        return collectionVoucherList;
    }

    @Override
    public List<GLCPExpenseList> getListForCPExpenseList() {
        StringBuilder sql = new StringBuilder();
        List<GLCPExpenseList> gLCPExpenseList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        return null;
    }

    @Override
    public UpdateDataDTO calculatingLiabilities(UUID accountingObjectId, String postDate) {
        UpdateDataDTO updateDataDTO = new UpdateDataDTO();
        String sql = "SELECT sum (CreditAmount - DebitAmount) FROM GeneralLedger where Account like '331%' and AccountingobjectID = :accountingObjectId and CONVERT(varchar, Date, 112) <= :postDate ";
        Map<String, Object> params = new HashMap<>();
        params.put("accountingObjectId", accountingObjectId);
        params.put("postDate", postDate);
        Query query = entityManager.createNativeQuery(sql);
        Common.setParams(query, params);
        try {
            updateDataDTO.setResult(query.getSingleResult());
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.SUCCESS);
        } catch (Exception e) {
            updateDataDTO.setMessages(Constants.UpdateDataDTOMessages.FAIL);
        }
        return updateDataDTO;
    }

    @Override
    public List<ViewGLPayExceedCashDTO> getViewGLPayExceedCash(Integer phienSoLamViec, UUID companyID, LocalDate date) {
        List<ViewGLPayExceedCashDTO> glPayExceedCashDTOS = new ArrayList<>();
        String sql = "EXEC [Proc_CHI_QUA_TON_QUY] @CompanyID = :companyID, @TypeLedger = :phienSoLamViec, @Date = :date";
        Map<String, Object> params = new HashMap<>();
        params.put("phienSoLamViec", phienSoLamViec);
        params.put("companyID", companyID);
        params.put("date", date);
        Query query = entityManager.createNativeQuery(sql.toString(), "ViewGLPayExceedCashDTO");
        Common.setParams(query, params);
        glPayExceedCashDTOS = query.getResultList();
        return glPayExceedCashDTOS;
    }

    @Override
    public List<UUID> findAllPaymentVoucherByPPInvoiceId(List<UUID> uuidsPPService) {
        StringBuilder sql = new StringBuilder("select MCPaymentID as uuid from MCPaymentDetailVendor a where a.PPInvoiceID in :uuidsPPService ")
            .append("union all ")
            .append("select MBTellerPaperID as uuid from MBTellerPaperDetailVendor a where a.PPInvoiceID in :uuidsPPService ")
            .append("union all ")
            .append("select MBCreditCardID as uuid from MBCreditCardDetailVendor a where a.PPInvoiceID in :uuidsPPService ");
        Map<String, Object> params = new HashMap<>();
        params.put("uuidsPPService", uuidsPPService);
        Query query = entityManager.createNativeQuery(sql.toString(), "UUIDDTO");
        setParams(query, params);
        List<UpdateDataDTO> updateDataDTOS = query.getResultList();
        return updateDataDTOS.stream().map(UpdateDataDTO::getUuid).collect(Collectors.toList());
    }

    @Override
    public Page<GiaThanhPoPupDTO> getByRatioMethod(Pageable pageable, String fromDate, String toDate, List<UUID> costSetID, Integer status, Integer phienSoLamViec, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<GiaThanhPoPupDTO> generalLedgers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from GeneralLedger gl" +
            " left join Type t ON t.ID = gl.TypeID " +
            " left join ExpenseItem EI on gl.ExpenseItemID = EI.id " +
            " left join CostSet cs on cs.ID = gl.CostSetID " +
            " where  (gl.Account like '621%' or gl.Account like '622%' or gl.Account like '623%' or " +
            " gl.Account like '627%' or (gl.Account like '154%' and gl.ExpenseItemID is not null)) and gl.CompanyID =:companyID and gl.TypeID != 701 " +
            " and gl.TypeLedger in (:TypeLedger, 2) and cs.ID in :costSetID");
        params.put("companyID", companyID);
        params.put("TypeLedger", phienSoLamViec);
        params.put("costSetID", costSetID);
        if (status != null ) {
            if (status == 1) {
                sql.append(" and gl.DebitAmount > 0 ");
            } else if (status ==  2) {
                sql.append(" and gl.CreditAmount > 0 ");
            }
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            sql.append(" and gl.PostedDate >= :fromDate and gl.PostedDate <= :toDate");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select cs.ID       CostSetID," +
                "       cs.CostSetCode costSetCode," +
                "       gl.PostedDate postedDate," +
                "       gl.Date date," +
                "       case :TypeLedger when 0 then NoFBook else NoMBook end no," +
                "       gl.Reason description," +
                "       case :status" +
                "           when 1 then" +
                "               gl.DebitAmount" +
                "           when 2 then gl.CreditAmount end                   Amount," +
                "       gl.Account accountNumber, " +
                "       ei.ExpenseItemCode expenseItemCode, " +
                "       ei.ExpenseType ExpenseItemType, " +
                "       ei.id ExpenseItemID, " +
                "       :status typeVoucher, " +
                "       gl.typeID typeID, " +
                "       t.typeGroupID  typeGroupID, " +
                "       gl.ReferenceID refID2 "
                + sql.toString() + " order by gl.PostedDate DESC, gl.NoFBook DESC, gl.NoMBook DESC", "GiaThanhPoPupDTO");
            if (status != null) {
                params.put("status", status);
            }
            Common.setParamsWithPageable(query, params, pageable, total);
            generalLedgers = query.getResultList();
        }
        return new PageImpl<>(generalLedgers, pageable, total.longValue());
    }

    @Override
    public List<GiaThanhPoPupDTO> getExpenseList(String fromDate, String toDate, List<UUID> costSetID, Integer status, Integer phienSoLamViec, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<GiaThanhPoPupDTO> generalLedgers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from GeneralLedger gl" +
            " left join Type t ON t.ID = gl.TypeID " +
            " left join ExpenseItem EI on gl.ExpenseItemID = EI.id " +
            " left join CostSet cs on cs.ID = gl.CostSetID " +
            " where  (gl.Account like '621%' or gl.Account like '622%' or gl.Account like '623%' or " +
            " gl.Account like '627%' or (gl.Account like '154%' and gl.ExpenseItemID is not null)) and gl.CompanyID =:companyID" +
            " and gl.TypeLedger in (:TypeLedger, 2) and cs.ID in :costSetID ");
        params.put("companyID", companyID);
        params.put("TypeLedger", phienSoLamViec);
        params.put("costSetID", costSetID);
        if (status != null ) {
            if (status == 0) {
                sql.append(" and gl.DebitAmount > 0 ");
            } else if (status ==  1) {
                sql.append(" and gl.CreditAmount > 0 ");
            }
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            sql.append(" and gl.PostedDate >= :fromDate and gl.PostedDate <= :toDate");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select cs.ID       CostSetID," +
                "       cs.CostSetCode," +
                "       gl.PostedDate," +
                "       gl.Date," +
                "       case :TypeLedger when 0 then NoFBook else NoMBook end no," +
                "       gl.Reason description ," +
                "       case :status" +
                "           when 0 then" +
                "               gl.DebitAmount" +
                "           when 1 then gl.CreditAmount end                   Amount," +
                "       gl.Account accountNumber, " +
                "       ei.ExpenseItemCode, " +
                "       ei.ExpenseType ExpenseItemType, " +
                "       ei.id ExpenseItemID, " +
                "       :status typeVoucher, " +
                "       gl.typeID typeID, " +
                "       t.typeGroupID  typeGroupID, " +
                "       gl.ReferenceID refID2 "
                + sql.toString() + " order by gl.PostedDate DESC, gl.NoFBook DESC, gl.NoMBook DESC", "GiaThanhPoPupDTO");
            if (status != null) {
                params.put("status", status);
            }
            Common.setParams(query, params);
            generalLedgers = query.getResultList();
        }
        return generalLedgers;
    }
}
