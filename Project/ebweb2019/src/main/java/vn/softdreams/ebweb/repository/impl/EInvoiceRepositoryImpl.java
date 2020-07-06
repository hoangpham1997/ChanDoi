package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.EInvoice;
import vn.softdreams.ebweb.domain.EInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.repository.EInvoiceRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.InvoicesSDS;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceNo;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.ConnectEInvoiceDTO;
import vn.softdreams.ebweb.web.rest.dto.InformationVoucherDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.EInvoice.*;

public class EInvoiceRepositoryImpl implements EInvoiceRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<EInvoiceDetails> findAllEInvoiceDetailsByID(UUID id, String refTable) {
        StringBuilder sql = new StringBuilder();
        List<EInvoiceDetails> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select a.id, " +
            "       a." + refTable + "ID as eInvoiceID, " +
            "       a.materialGoodsID, " +
            "       c.materialGoodsCode, " +
            "       a.description, " +
            "       a.isPromotion, " +
            "       a.unitID, " +
            "       b.unitName, " +
            "       a.quantity, " +
            "       a.unitPrice, " +
            "       a.unitPriceOriginal, " +
            "       a.mainUnitID, " +
            "       a.mainQuantity, " +
            "       a.mainUnitPrice, " +
            "       a.mainConvertRate, " +
            "       a.formula, " +
            "       a.amount, " +
            "       a.amountOriginal, " +
            "       a.discountRate, " +
            "       a.discountAmount, " +
            "       a.discountAmountOriginal, " +
            "       a.vATRate, " +
            "       a.vATAmount, " +
            "       a.vATAmountOriginal, " +
            "       a.lotNo, " +
            "       a.expiryDate, " +
            "       a.orderPriority " +
            "from " + refTable + "Detail a " +
            "         left join Unit b on a.UnitID = b.ID " +
            "         left join MaterialGoods c on a.MaterialGoodsID = c.ID " +
            "where a." + refTable + "ID = :id order by a.orderPriority ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "EInvoiceDetails");
        Utils.setParams(query, params);
        return query.getResultList();
    }

    /**
     * @param uuids
     * @return
     * @Author Hautv
     */
    @Override
    public List<EInvoice> findAllByListID(List<UUID> uuids) {
        StringBuilder sql = new StringBuilder();
        List<EInvoice> lst = new ArrayList<>();
        if (uuids == null || uuids.size() == 0) {
            return lst;
        }
        Map<String, Object> params = new HashMap<>();
        String sql_ = "select * from ViewEInvoice where ID in ( ";
        for (UUID uuid : uuids
        ) {
            sql_ += "'" + Utils.uuidConvertToGUID(uuid).toString() + "'";
            if (uuids.indexOf(uuid) != uuids.size() - 1) {
                sql_ += ",";
            }
        }
        sql_ += ") order by RefDateTime ;";
        sql.append(sql_);
        Query query = entityManager.createNativeQuery(sql.toString(), EInvoice.class);
        Utils.setParams(query, params);
        lst = query.getResultList();
        return lst;
    }

    @Override
    public List<SaBill> findAllSABillByListID(List<UUID> uuids) {

        return null;
    }

    @Override
    public List<EInvoiceDetails> findAllDetailsByListID(List<UUID> uuids) {
        StringBuilder sql = new StringBuilder();
        List<EInvoiceDetails> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String sql_ = "select * from ViewEInvoiceDetail where eInvoiceID in ( ";
        for (UUID uuid : uuids
        ) {
            sql_ += "'" + Utils.uuidConvertToGUID(uuid).toString() + "'";
            if (uuids.indexOf(uuid) != uuids.size() - 1) {
                sql_ += ",";
            }
        }
        sql_ += ");";
        sql.append(sql_);
        Query query = entityManager.createNativeQuery(sql.toString(), "EInvoiceDetails");
        Utils.setParams(query, params);
        lst = query.getResultList();
        return lst;
    }

    @Override
    public Boolean updateSystemOption(ConnectEInvoiceDTO connectEInvoiceDTO, UUID companyID) {
        Boolean result = true;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("update SystemOption set data =:supplierCode ");
        params.put("supplierCode", connectEInvoiceDTO.getSupplierCode());
        sql.append("where CompanyID =:companyID ");
        params.put("companyID", companyID);
        sql.append("and Code = 'EI_IDNhaCungCapDichVu'; ");

        sql.append("update SystemOption set data =:userName ");
        params.put("userName", connectEInvoiceDTO.getUserName());
        sql.append("where CompanyID =:companyID ");
        sql.append("and Code = 'EI_TenDangNhap'; ");

        sql.append("update SystemOption set data =:pass ");
        params.put("pass", Utils.encryptAES_CBC_Base64(connectEInvoiceDTO.getPassword()));
        sql.append("where CompanyID =:companyID ");
        sql.append("and Code = 'EI_MatKhau'; ");

        sql.append("update SystemOption set data =:path ");
        params.put("path", connectEInvoiceDTO.getPath());
        sql.append("where CompanyID =:companyID ");
        sql.append("and Code = 'EI_Path'; ");

        sql.append("update SystemOption set data =:hdc ");
        params.put("hdc", connectEInvoiceDTO.getUseInvoceWait() ? 1 : 0);
        sql.append("where CompanyID =:companyID ");
        sql.append("and Code = 'CheckHDCD'; ");

        if (!StringUtils.isEmpty(connectEInvoiceDTO.getToken())) {
            sql.append("update SystemOption set data =:tokenMIV ");
            params.put("tokenMIV", connectEInvoiceDTO.getToken());
            sql.append("where CompanyID =:companyID ");
            sql.append("and Code = 'Token_MIV'; ");
        }

        sql.append("update SystemOption set data =:signType ");
        params.put("signType", connectEInvoiceDTO.getUseToken() ? 1 : 0);
        sql.append("where CompanyID =:companyID ");
        sql.append("and Code = 'SignType'; ");

        Query query = entityManager.createNativeQuery(sql.toString());
        Utils.setParams(query, params);
        int r = query.executeUpdate();
        if (r == 1) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean updateAfterPublishInvoice(Respone_SDS respone_sds) {
        Boolean result = true;
        Map<String, Object> params = new HashMap<>();
        List<KeyInvoiceNo> lstKeyInvoiceNos = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceNo().entrySet()
        ) {
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(Utils.uuidConvertToGUID(entry.getKey()));
            keyInvoiceNo.setNo(entry.getValue());
            lstKeyInvoiceNos.add(keyInvoiceNo);
        }
        String sql = "UPDATE SABill SET StatusInvoice = 1, InvoiceNo = ? WHERE id = ?; " +
            "UPDATE SAInvoice set InvoiceNo = ? where id = (select Top(1) SAInvoiceID from SAInvoiceDetail where SABillID = ?);" +
            "UPDATE PPDiscountReturn set InvoiceNo = ? where id = (select Top(1) PPDiscountReturnID from PPDiscountReturnDetail where SABillID = ?);" +
            "UPDATE SAReturn set InvoiceNo = ? where id = (select Top(1) SAReturnID from SAReturnDetail where SABillID = ?);";
        int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNo().entrySet(), Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(2, detail.getKey().toString());
            ps.setString(3, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(4, detail.getKey().toString());
            ps.setString(5, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(6, detail.getKey().toString());
            ps.setString(7, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(8, detail.getKey().toString());
        });
        respone_sds.getData().setKeyInvoiceNoDTO(lstKeyInvoiceNos);
        return result;
    }

    @Override
    public Boolean updateAfterPublishInvoiceReplaced(Respone_SDS respone_sds, EInvoice eInvoice) {
        Boolean result = true;
        List<KeyInvoiceNo> lstKeyInvoiceNos = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceNo().entrySet()) {
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(Utils.uuidConvertToGUID(entry.getKey()));
            keyInvoiceNo.setNo(entry.getValue());
            lstKeyInvoiceNos.add(keyInvoiceNo);
        }
        respone_sds.getData().setKeyInvoiceNoDTO(lstKeyInvoiceNos);
        if (respone_sds.getData().getKeyInvoiceNo().size() == 1) {
            String sql = "UPDATE SABill SET StatusInvoice = 1, InvoiceNo = ? WHERE id = ?; " +
                "UPDATE SAInvoice set InvoiceNo =? , InvoiceDate =?  where id = (select Top(1) SAInvoiceID from SAInvoiceDetail where SABillID = ?);" +
                "UPDATE PPDiscountReturn set InvoiceNo = ? , InvoiceDate =? where id = (select Top(1) PPDiscountReturnID from PPDiscountReturnDetail where SABillID = ?);" +
                "UPDATE SAReturn set InvoiceNo = ? , InvoiceDate =? where id = (select Top(1) SAReturnID from SAReturnDetail where SABillID = ?);" +
                "UPDATE SAInvoiceDetail set SABillDetailID = NULL where SABillID =? ;" +
                "UPDATE SAReturnDetail set SABillDetailID = NULL where SABillID =? ;" +
                "UPDATE SAReturnDetail set SABillDetailID = NULL where SABillID =? ;" +
                "UPDATE SAInvoiceDetail set SABillID = ? where SABillID = ?;" +
                "UPDATE SAReturnDetail set SABillID = ? where SABillID = ? ;" +
                "UPDATE PPDiscountReturnDetail set SABillID = ? where SABillID = ? ;" +
                "UPDATE SABill set StatusInvoice = 3 where id = ? ;";
            int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNo().entrySet(), Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setString(1, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(2, detail.getKey().toString());
                ps.setString(3, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(4, eInvoice.getInvoiceDate().toString());
                ps.setString(5, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(6, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(7, eInvoice.getInvoiceDate().toString());
                ps.setString(8, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(9, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(10, eInvoice.getInvoiceDate().toString());
                ps.setString(11, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(12, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(13, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(14, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(15, detail.getKey().toString());
                ps.setString(16, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(17, detail.getKey().toString());
                ps.setString(18, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(19, detail.getKey().toString());
                ps.setString(20, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
                ps.setString(21, Utils.uuidConvertToGUID(eInvoice.getiDReplaceInv()).toString());
            });
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean updateAfterPublishInvoiceAdjusted(Respone_SDS respone_sds, EInvoice eInvoice) {
        Boolean result = true;
        List<KeyInvoiceNo> lstKeyInvoiceNos = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceNo().entrySet()) {
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(Utils.uuidConvertToGUID(entry.getKey()));
            keyInvoiceNo.setNo(entry.getValue());
            lstKeyInvoiceNos.add(keyInvoiceNo);
        }
        respone_sds.getData().setKeyInvoiceNoDTO(lstKeyInvoiceNos);
        if (respone_sds.getData().getKeyInvoiceNo().size() == 1) {
            String sql = "UPDATE SABill SET StatusInvoice = 1, InvoiceNo = ? WHERE id = ?; " +
                "UPDATE SABill set StatusInvoice = 4 where id = ?";
            int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNo().entrySet(), Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setString(1, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(2, detail.getKey().toString());
                ps.setString(3, Utils.uuidConvertToGUID(eInvoice.getiDAdjustInv()).toString());
            });
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean updateAfterPublishInvoiceAdjusted(Respone_SDS respone_sds, List<EInvoice> eInvoices) {
        Boolean result = true;
        List<KeyInvoiceNo> lstKeyInvoiceNos = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceNo().entrySet()) {
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(Utils.uuidConvertToGUID(entry.getKey()));
            keyInvoiceNo.setNo(entry.getValue());
            lstKeyInvoiceNos.add(keyInvoiceNo);
        }
        respone_sds.getData().setKeyInvoiceNoDTO(lstKeyInvoiceNos);
        if (respone_sds.getData().getKeyInvoiceNo().size() == 1) {
            String sql = "UPDATE SABill SET StatusInvoice = 1, InvoiceNo = ? WHERE id = ?; " +
                "UPDATE SABill set StatusInvoice = 4 where id = ?";
            int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNo().entrySet(), Constants.BATCH_SIZE, (ps, detail) -> {
                ps.setString(1, StringUtils.leftPad(detail.getValue(), 7, '0'));
                ps.setString(2, detail.getKey().toString());
                ps.setString(3, Utils.uuidConvertToGUID(eInvoices.stream().filter(n -> n.getId().equals(Utils.uuidConvertToGUID(detail.getKey()))).findFirst().get().getiDAdjustInv()).toString());
            });
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean updateAfterCreateKeyInvoiceNoWaitSign(Respone_SDS respone_sds) {
        Boolean result = true;
        List<KeyInvoiceNo> lstKeyInvoiceNos = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : respone_sds.getData().getKeyInvoiceNo().entrySet()) {
            KeyInvoiceNo keyInvoiceNo = new KeyInvoiceNo();
            keyInvoiceNo.setId(Utils.uuidConvertToGUID(entry.getKey()));
            keyInvoiceNo.setNo(entry.getValue());
            lstKeyInvoiceNos.add(keyInvoiceNo);
        }
        String sql = "UPDATE SABill SET StatusInvoice = 6, InvoiceNo = ? WHERE id = ?; " +
            "UPDATE SAInvoice set InvoiceNo = ? where id = (select Top(1) SAInvoiceID from SAInvoiceDetail where SABillID = ?);" +
            "UPDATE PPDiscountReturn set InvoiceNo = ? where id = (select Top(1) PPDiscountReturnID from PPDiscountReturnDetail where SABillID = ?);" +
            "UPDATE SAReturn set InvoiceNo = ? where id = (select Top(1) SAReturnID from SAReturnDetail where SABillID = ?);";
        int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNo().entrySet(), Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(2, detail.getKey().toString());
            ps.setString(3, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(4, detail.getKey().toString());
            ps.setString(5, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(6, detail.getKey().toString());
            ps.setString(7, StringUtils.leftPad(detail.getValue(), 7, '0'));
            ps.setString(8, detail.getKey().toString());
        });
        respone_sds.getData().setKeyInvoiceNoDTO(lstKeyInvoiceNos);
        return result;
    }

    @Override
    public Boolean updateAfterCancelInvoice(Respone_SDS respone_sds) {
        Boolean result = true;
        String sql = "UPDATE SABill SET StatusInvoice = 5 WHERE id = ?";
        int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNoDTO(), Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
        return result;
    }

    @Override
    public Boolean updateAfterConvertedOrigin(Respone_SDS respone_sds) {
        Boolean result = true;
        String sql = "UPDATE SABill SET StatusConverted = 1 WHERE id = ?";
        int[][] ints = jdbcTemplate.batchUpdate(sql, respone_sds.getData().getKeyInvoiceNoDTO(), Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
        return result;
    }

    @Override
    public Boolean updateSABill(List<InvoicesSDS> invoicesSDSList) {
        Boolean result = true;
        List<UUID> lstUUID = invoicesSDSList.stream().map(n -> Utils.uuidConvertToGUID(n.getKey())).collect(Collectors.toList());
        List<EInvoice> lsEInvoices = findAllByListID(lstUUID);
        for (EInvoice item : lsEInvoices
        ) {
            List<Integer> lstSTT = Arrays.asList(6, 7, 8);
            InvoicesSDS invoicesSDS = invoicesSDSList.stream().filter(n -> n.getKey().equals(Utils.uuidConvertToGUID(item.getId()))).findFirst().get();
            Integer s = invoicesSDS.getInvoiceStatus();
            if (lstSTT.contains(item.getStatusInvoice())) {
                if (!s.equals(0)) {
                    item.setStatusInvoice(s);
                }
            } else {
                if (!s.equals(-1)) item.setStatusInvoice(s);
            }
            if (item.getStatusInvoice() < 6 && s == -1) item.setStatusInvoice(-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(invoicesSDS.getArisingDate(), formatter);
            item.setInvoiceDate(localDate);
            if (!StringUtils.isEmpty(invoicesSDS.getInvNo()) && !Integer.valueOf(invoicesSDS.getInvNo()).equals(0)) {
                item.setInvoiceNo(StringUtils.leftPad(invoicesSDS.getInvNo(), 7, '0'));
            }
            item.setInvoiceTemplate(invoicesSDS.getPattern());
            item.setInvoiceSeries(invoicesSDS.getSerial());
        }
        String sql = "UPDATE SABill SET StatusInvoice = ?, InvoiceNo = ?, InvoiceDate = ?, InvoiceTemplate = ?, InvoiceSeries = ?  WHERE id = ?";
        int[][] ints = jdbcTemplate.batchUpdate(sql, lsEInvoices, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, detail.getStatusInvoice().toString());
            ps.setString(2, detail.getInvoiceNo());
            ps.setString(3, detail.getInvoiceDate().toString());
            ps.setString(4, detail.getInvoiceTemplate());
            ps.setString(5, detail.getInvoiceSeries());
            ps.setString(6, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
        return result;
    }

    @Override
    public Boolean updateSendMailAfterPublish(List<EInvoice> eInvoices) {
        Boolean result = true;
        String sql = "UPDATE SABill SET Email = ?, StatusSendMail = ?  WHERE id = ?";
        int[][] ints = jdbcTemplate.batchUpdate(sql, eInvoices, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, getEmailAndDate(detail));
            ps.setString(2, StringUtils.isEmpty(detail.getAccountingObjectEmail()) ? "0" : "1");
            ps.setString(3, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
        return result;
    }

    @Override
    public Boolean updateSendMail(Map<UUID, String> ikeyEmail) {
        List<EInvoice> lstEInvocie = findAllByListID(ikeyEmail.keySet().stream().collect(Collectors.toList()));
        Boolean result = true;
        String sql = "UPDATE SABill SET Email = ?, StatusSendMail = 1  WHERE id = ?";
        int[][] ints = jdbcTemplate.batchUpdate(sql, lstEInvocie, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, getEmailAndDate(detail, ikeyEmail));
            ps.setString(2, Utils.uuidConvertToGUID(detail.getId()).toString());
        });
        return result;
    }

    @Override
    public Page<EInvoice> findAll(Pageable pageable, UUID companyID, SearchVoucher searchVoucher, Integer typeEInvoice) {
        StringBuilder sql = new StringBuilder();
        List<EInvoice> lstObject = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        String oderby = " ORDER BY InvoiceTemplate, InvoiceSeries DESC , InvoiceNo DESC";

        if (DANH_SACH_HOA_DON.equals(typeEInvoice)) {
            oderby = " ORDER BY case when InvoiceNo is null or InvoiceNo = '' then 0 else 1 end, case when InvoiceNo is not null then InvoiceTemplate end, case when InvoiceNo is not null then InvoiceSeries end ,InvoiceNo DESC, RefDateTime desc";
            sql.append("from viewEinvoice where companyID = :companyID and StatusInvoice != 6 ");
            params.put("companyID", companyID);
        } else if (DANH_SACH_HOA_DON_CHO_KY.equals(typeEInvoice)) {
            oderby = " ORDER BY case when InvoiceNo is null or InvoiceNo = '' then 0 else 1 end, case when InvoiceNo is not null then InvoiceTemplate end, case when InvoiceNo is not null then InvoiceSeries end ,InvoiceNo DESC, RefDateTime desc";
            sql.append("from viewEinvoice where companyID = :companyID and StatusInvoice = 6 ");
            params.put("companyID", companyID);
        } else if (DANH_SACH_HOA_DON_THAY_THE.equals(typeEInvoice)) {
            sql.append("from viewEinvoice where companyID = :companyID and StatusInvoice in (1,3,4,5) and IDReplaceInv is not null ");
            params.put("companyID", companyID);
        } else if (DANH_SACH_HOA_DON_DIEU_CHINH.equals(typeEInvoice)) {
            sql.append("from viewEinvoice where companyID = :companyID and StatusInvoice in (1,3,4,5)  and IDAdjustInv is not null ");
            params.put("companyID", companyID);
        } else if (DANH_SACH_HOA_DON_HUY.equals(typeEInvoice)) {
            sql.append("from viewEinvoice where companyID = :companyID and StatusInvoice = 5 ");
            params.put("companyID", companyID);
        } else if (DANH_SACH_HOA_CHUYEN_DOI.equals(typeEInvoice)) {
            sql.append("from viewEinvoice where companyID = :companyID and (StatusInvoice = 1 or StatusInvoice = 4) ");
            params.put("companyID", companyID);
        }
        if (searchVoucher != null) {
            if (searchVoucher.getFromDate() != null) {
                sql.append("AND InvoiceDate >= :fromDate ");
                params.put("fromDate", Utils.AddDate(searchVoucher.getFromDate(), 0));
            }
            if (searchVoucher.getToDate() != null) {
                sql.append("AND InvoiceDate < :toDate ");
                params.put("toDate", Utils.AddDate(searchVoucher.getToDate(), 1));
            }
            if (searchVoucher.getStatus() != null) {
                if (DANH_SACH_HOA_CHUYEN_DOI.equals(typeEInvoice)) {
                    if (searchVoucher.getStatus().equals(0)) {
                        sql.append("AND (StatusConverted = :status or StatusConverted is null) ");
                    } else {
                        sql.append("AND StatusConverted = :status ");
                    }
                } else {
                    sql.append("AND StatusInvoice = :status ");
                }
                params.put("status", searchVoucher.getStatus());
            }
            if (searchVoucher.getStatusSendMail() != null) {
                if (searchVoucher.getStatusSendMail().equals(0)) {
                    sql.append("AND (StatusSendMail = :StatusSendMail or StatusSendMail is null) ");
                    params.put("StatusSendMail", searchVoucher.getStatusSendMail());
                } else {
                    sql.append("AND StatusSendMail = :StatusSendMail ");
                    params.put("StatusSendMail", searchVoucher.getStatusSendMail());
                }
            }
            if (searchVoucher.getAccountingObjectID() != null) {
                if (DANH_SACH_HOA_DON_THAY_THE.equals(typeEInvoice) || DANH_SACH_HOA_DON_DIEU_CHINH.equals(typeEInvoice)) {
                    sql.append("AND (accountingObjectID = :accountingObjectID or AccountingObjectIDAdjusted = :accountingObjectID or AccountingObjectIDReplaced = :accountingObjectID ) ");
                    params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
                } else {
                    sql.append("AND accountingObjectID = :accountingObjectID ");
                    params.put("accountingObjectID", searchVoucher.getAccountingObjectID());
                }
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getInvoiceTemplate())) {
                sql.append("AND InvoiceTemplate LIKE :InvoiceTemplate ");
                params.put("InvoiceTemplate", "%" + searchVoucher.getInvoiceTemplate() + "%");
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getInvoiceSeries())) {
                sql.append("AND InvoiceSeries LIKE :InvoiceSeries ");
                params.put("InvoiceSeries", "%" + searchVoucher.getInvoiceSeries() + "%");
            }
            if (!Strings.isNullOrEmpty(searchVoucher.getTextSearch())) {
                sql.append("AND InvoiceNo LIKE :no ");
                params.put("no", "%" + searchVoucher.getTextSearch() + "%");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Utils.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select * " + sql.toString() + oderby, EInvoice.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lstObject = query.getResultList();

            Query querySum = entityManager.createNativeQuery("select SUM(TotalAmount) - SUM(TotalDiscountAmount) + SUM(TotalVATAmount) " + sql.toString());
            Common.setParams(querySum, params);
            Number totalamount = (Number) querySum.getSingleResult();
            lstObject.get(0).setTotal(totalamount);
        } else {
            return new PageImpl<>(lstObject);
        }
        return new PageImpl<>(lstObject, pageable, total.longValue());
    }

    @Override
    public InformationVoucherDTO getInformationVoucherByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        InformationVoucherDTO informationVoucherDT = new InformationVoucherDTO();
        Map<String, Object> params = new HashMap<>();
        sql.append("from (select a.PPDiscountReturnID as ID, a.SABillID, b.TypeID " +
            "from PPDiscountReturnDetail a " +
            "         left join PPDiscountReturn b on a.PPDiscountReturnID = b.ID " +
            "where SABillID is not null " +
            "union " +
            "select a.SAInvoiceID as ID, a.SABillID, b.TypeID " +
            "from SAInvoiceDetail a " +
            "         left join SAInvoice b on a.SAInvoiceID = b.ID " +
            "where SABillID is not null " +
            "union " +
            "select a.SAInvoiceID as ID, a.SABillID, b.TypeID " +
            "from SAReturnDetail a " +
            "         left join SAReturn b on a.SAReturnID = b.ID " +
            "where SABillID is not null) a where a.SABillID = :id");
        params.put("id", id);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Utils.setParams(countQuerry, params);
        Number count = (Number) countQuerry.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery("select * " + sql.toString(), "InformationVoucherDTO");
            Utils.setParams(query, params);
            informationVoucherDT = ((InformationVoucherDTO) query.getSingleResult());
        }

        return informationVoucherDT;
    }

    private String getEmailAndDate(EInvoice eInvoice, Map<UUID, String> ikeyEmail) {
        String result = "";
        if (!StringUtils.isEmpty(eInvoice.getEmail())) {
            result += eInvoice.getEmail() + ";" + ikeyEmail.get(eInvoice.getId()) + ",";
        } else {
            result += ikeyEmail.get(eInvoice.getId()) + ",";
        }
        result += LocalDate.now().toString();
        return result;
    }

    private String getEmailAndDate(EInvoice eInvoice) {
        String result = "";
        if (!StringUtils.isEmpty(eInvoice.getAccountingObjectEmail())) {
            if (!StringUtils.isEmpty(eInvoice.getEmail())) {
                result += eInvoice.getEmail() + ";" + eInvoice.getAccountingObjectEmail() + ",";
            } else {
                result += eInvoice.getAccountingObjectEmail() + ",";
            }
            result += LocalDate.now().toString();
        }
        return result;
    }
}
