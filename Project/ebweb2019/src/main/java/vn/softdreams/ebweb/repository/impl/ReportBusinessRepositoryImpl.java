package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.ReportBusinessRepository;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.Report.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoKhoanMucChiPhiDTO;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoTaiKhoanDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.BieuDoDoanhThuChiPhiDTO;
import vn.softdreams.ebweb.web.rest.dto.BieuDoTongHopDTO;
import vn.softdreams.ebweb.web.rest.dto.SucKhoeTaiChinhDoanhNghiepDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReportBusinessRepositoryImpl implements ReportBusinessRepository {
    @Autowired
    @PersistenceContext(unitName = "reportEntityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private GenCodeService genCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationUnitRepository organizationUnitRepository;

    @Override
    public List<SoNhatKyChungDTO> getSoNhatKyChung(LocalDate fromDate, LocalDate toDate, Boolean groupTheSameItem, Boolean showAccumAmount, UUID companyID, Boolean isFinancialBook, Boolean isDependent) {
        List<SoNhatKyChungDTO> soNhatKyChungDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_NHAT_KY_CHUNG] @FromDate = :FromDate, @ToDate = :ToDate, @GroupTheSameItem = :GroupTheSameItem, @IsShowAccumAmount = :IsShowAccumAmount, @CompanyID = :CompanyID, @IsFinancialBook = :IsFinancialBook");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("GroupTheSameItem", groupTheSameItem);
        params.put("IsShowAccumAmount", showAccumAmount);
        params.put("CompanyID", companyID);
        params.put("IsFinancialBook", isFinancialBook);
        sql.append(" , @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoNhatKyChungDTO");
        Common.setParams(query, params);
        soNhatKyChungDTOS = query.getResultList();
        return soNhatKyChungDTOS;
    }

    @Override
    public List<SoNhatKyThuTienDTO> getSoNhatKyThuChi(LocalDate fromDate, LocalDate toDate, Boolean isSimilarSum,
                                                      UUID companyID, String currencyID, String accountNumber,
                                                      UUID bankAccountID, Boolean isFinancialBook, Boolean isDependent, String type, Boolean getAmountOriginal) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (type.equals(TypeConstant.BAO_CAO.SO_NHAT_KY_THU_TIEN)) {
            sql.append("exec PROC_SO_NHAT_KY_THU_TIEN ");
        } else {
            sql.append("exec PROC_SO_NHAT_KY_CHI_TIEN ");
        }


        if (fromDate != null) {
            sql.append(" @FromDate = :fromDate");
            params.put("fromDate", fromDate);
        }
        if (toDate != null) {
            sql.append(", @ToDate = :toDate");
            params.put("toDate", toDate);
        }
        if (currencyID != null) {
            sql.append(", @CurrencyID = :currencyID");
            params.put("currencyID", currencyID);
        }
        if (accountNumber != null) {
            sql.append(", @AccountNumber = :accountNumber");
            params.put("accountNumber", accountNumber);
        }
        if (bankAccountID != null) {
            sql.append(", @BankAccountID = :bankAccountID");
            params.put("bankAccountID", bankAccountID);
        } else {
            sql.append(", @BankAccountID = null");
        }
        sql.append(", @IsSimilarSum = :isSimilarSum");
        if (isSimilarSum != null) {
            params.put("isSimilarSum", isSimilarSum);
        } else {
            params.put("isSimilarSum", false);
        }
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        sql.append(", @GetAmountOriginal = :getAmountOriginal");
        if (isDependent != null) {
            params.put("getAmountOriginal", getAmountOriginal);
        } else {
            params.put("getAmountOriginal", false);
        }
        sql.append(", @CompanyID = :companyID, @IsFinancialBook = :isFinancialBook");
        params.put("companyID", companyID);
        params.put("isFinancialBook", isFinancialBook);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoNhatKyThuTienDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<BangKeMuaBanDTO> getBangKeMuaBan(LocalDate fromDate, LocalDate toDate,
                                                 Boolean isSimilarSum, UUID companyID,
                                                 Integer typeLedger, String type,
                                                 Boolean isBill, Boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (type.equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA)) {
            sql.append("execute [PROC_BANG_KE_BAN_RA]");
        } else {
            sql.append("exec [PROC_BANG_KE_MUA_VAO] ");
        }
        sql.append(" @FromDate = :fromDate");
        params.put("fromDate", fromDate);
        sql.append(", @ToDate = :toDate");
        params.put("toDate", toDate);
        sql.append(", @IsSimilarBranch = :isSimilarSum");
        if (isSimilarSum != null) {
            params.put("isSimilarSum", isSimilarSum);
        } else {
            params.put("isSimilarSum", false);
        }
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        sql.append(", @CompanyID = :companyID, @TypeLedger = :typeLedger, @IsBill = :isBill");
        params.put("companyID", companyID);
        params.put("typeLedger", typeLedger);
        params.put("isBill", isBill);
        Query query = entityManager.createNativeQuery(sql.toString(), type.equals(TypeConstant.BAO_CAO.BANG_KE_BAN_RA) ? "BangKeBanRaDTO" : "BangKeMuaVaoDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<BangCanDoiKeToanDTO> getBangCanDoiKeToan(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer typeLedger, Boolean isDependent) {
        List<BangCanDoiKeToanDTO> bangCanDoiKeToanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BANG_CAN_DOI_KE_TOAN] @CompanyID = :companyID, @TypeLedger = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate, @IncludeDependentBranch = :dependentBranch, @isPrintByYear = :isPrintByYear, @PrevFromDate = :fromDate, @PrevToDate = :toDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", typeLedger);
        params.put("companyID", companyID);
        params.put("dependentBranch", isDependent);
        params.put("isPrintByYear", 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "BangCanDoiKeToanDTO");
        Common.setParams(query, params);
        bangCanDoiKeToanDTOS = query.getResultList();
        return bangCanDoiKeToanDTOS;
    }

    @Override
    public List<SoCaiHTNhatKyChungDTO> getSoCaiHTNhatKyChung(LocalDate fromDate, LocalDate toDate, String accountNumber, Boolean isSimilarSum, UUID companyID, Boolean isFinancialBook, Integer accountingType, UserDTO userDTO, Boolean isDependent) {
        List<SoCaiHTNhatKyChungDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_CAI_HT_NHAT_KY_CHUNG_S03b] @FromDate = :FromDate, @ToDate = :ToDate, @AccountNumber = :AccountNumber, @IsSimilarSum = :IsSimilarSum, @CompanyID = :CompanyID, @IsFinancialBook = :IsFinancialBook, @AccountingType =:AccountingType, @Calcular =:Calcular");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("AccountNumber", accountNumber);
        params.put("IsSimilarSum", isSimilarSum);
        params.put("CompanyID", companyID);
        params.put("IsFinancialBook", isFinancialBook);
        params.put("AccountingType", accountingType);
        sql.append(" , @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query1 = entityManager.createNativeQuery(sql.toString(), "SoCaiHTNhatKyChungCalcularDTO").setParameter("Calcular", true);
        Query query2 = entityManager.createNativeQuery(sql.toString(), "SoCaiHTNhatKyChungDTO").setParameter("Calcular", false);
        Common.setParams(query1, params);
        Common.setParams(query2, params);
        List<SoCaiHTNhatKyChungDTO> object1 = query1.getResultList();
        List<SoCaiHTNhatKyChungDTO> object2 = query2.getResultList();
        BigDecimal congPhatSinhNo = BigDecimal.ZERO;
        BigDecimal congPhatSinhCo = BigDecimal.ZERO;
        BigDecimal soDuCuoiKyNo = BigDecimal.ZERO;
        BigDecimal soDuCuoiKyCo = BigDecimal.ZERO;
        BigDecimal congLuyKeNo = BigDecimal.ZERO;
        BigDecimal congLuyKeCo = BigDecimal.ZERO;
        for (SoCaiHTNhatKyChungDTO calcular : object1) {
            List<SoCaiHTNhatKyChungDTO> lstDetail = object2.stream().filter(n -> n.getAccountNumber().equals(calcular.getAccountNumber())).collect(Collectors.toList());
            congPhatSinhNo = calcular.getAccumDebitAmount();
            congPhatSinhCo = calcular.getAccumCreditAmount();
            soDuCuoiKyNo = calcular.getOpenningDebitAmount().add(congPhatSinhNo).subtract(calcular.getOpenningCreditAmount()).subtract(congPhatSinhCo);
            soDuCuoiKyCo = calcular.getOpenningCreditAmount().add(congPhatSinhCo).subtract(calcular.getOpenningDebitAmount()).subtract(congPhatSinhNo);
            congLuyKeNo = congPhatSinhNo.add(calcular.getOpenningDebitAmount());
            congLuyKeCo = congPhatSinhCo.add(calcular.getOpenningCreditAmount());

            calcular.setCongSoPhatSinhNoAm(congPhatSinhNo);
            calcular.setCongSoPhatSinhCoAm(congPhatSinhCo);
            if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_NO) {
                if (soDuCuoiKyNo.compareTo(BigDecimal.ZERO) != 0) {
                    calcular.setSoDuCuoiKyNoAm(soDuCuoiKyNo);
                }
            } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_CO) {
                if (soDuCuoiKyCo.compareTo(BigDecimal.ZERO) != 0) {
                    calcular.setSoDuCuoiKyCoAm(soDuCuoiKyCo);
                }
            } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_LUONG_TINH || calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_KHONG_CO_SO_DU) {
                if (soDuCuoiKyNo.compareTo(BigDecimal.ZERO) > 0) {
                    calcular.setSoDuCuoiKyNoAm(soDuCuoiKyNo);
                }
                if (soDuCuoiKyCo.compareTo(BigDecimal.ZERO) > 0) {
                    calcular.setSoDuCuoiKyCoAm(soDuCuoiKyCo);
                }
            }
            /*calcular.setSoDuCuoiKyNoAm(soDuCuoiKyNo);
            calcular.setSoDuCuoiKyCoAm(soDuCuoiKyCo);*/
            calcular.setCongLuyKeNoAm(congLuyKeNo);
            calcular.setCongLuyKeCoAm(congLuyKeCo);

            calcular.setHeaderDetail(true);
            if (lstDetail.size() > 0) {
                lstDetail.get(lstDetail.size() - 1).setBreakPage(true);
                lstDetail.get(lstDetail.size() - 1).setCongSoPhatSinhNo(Utils.formatTien(congPhatSinhNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                lstDetail.get(lstDetail.size() - 1).setCongSoPhatSinhCo(Utils.formatTien(congPhatSinhCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_NO) {
                    lstDetail.get(lstDetail.size() - 1).setSoDuCuoiKyNo(Utils.formatTien(soDuCuoiKyNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_CO) {
                    lstDetail.get(lstDetail.size() - 1).setSoDuCuoiKyCo(Utils.formatTien(soDuCuoiKyCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_LUONG_TINH) {
                    if (soDuCuoiKyNo.compareTo(BigDecimal.ZERO) > 0) {
                        lstDetail.get(lstDetail.size() - 1).setSoDuCuoiKyNo(Utils.formatTien(soDuCuoiKyNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                    if (soDuCuoiKyCo.compareTo(BigDecimal.ZERO) > 0) {
                        lstDetail.get(lstDetail.size() - 1).setSoDuCuoiKyCo(Utils.formatTien(soDuCuoiKyCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                }
                lstDetail.get(lstDetail.size() - 1).setCongLuyKeNo(Utils.formatTien(congLuyKeNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                lstDetail.get(lstDetail.size() - 1).setCongLuyKeCo(Utils.formatTien(congLuyKeCo, Constants.SystemOption.DDSo_TienVND, userDTO));
            } else {
                if (congPhatSinhNo.compareTo(BigDecimal.ZERO) == 0) {
                    calcular.setCongSoPhatSinhNo("");
                } else {
                    calcular.setCongSoPhatSinhNo(Utils.formatTien(congPhatSinhNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (congPhatSinhCo.compareTo(BigDecimal.ZERO) == 0) {
                    calcular.setCongSoPhatSinhCo("");
                } else {
                    calcular.setCongSoPhatSinhCo(Utils.formatTien(congPhatSinhCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_NO) {
                    if (soDuCuoiKyNo.compareTo(BigDecimal.ZERO) == 0) {
                        calcular.setSoDuCuoiKyNo("");
                    } else {
                        calcular.setSoDuCuoiKyNo(Utils.formatTien(soDuCuoiKyNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_DU_CO) {
                    if (soDuCuoiKyCo.compareTo(BigDecimal.ZERO) == 0) {
                        calcular.setSoDuCuoiKyCo("");
                    } else {
                        calcular.setSoDuCuoiKyCo(Utils.formatTien(soDuCuoiKyCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                } else if (calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_LUONG_TINH || calcular.getAccountCategoryKind() == TypeConstant.LOAI_TAI_KHOAN.TK_KHONG_CO_SO_DU) {
                    if (soDuCuoiKyNo.compareTo(BigDecimal.ZERO) > 0) {
                        calcular.setSoDuCuoiKyNo(Utils.formatTien(soDuCuoiKyNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                    if (soDuCuoiKyCo.compareTo(BigDecimal.ZERO) > 0) {
                        calcular.setSoDuCuoiKyCo(Utils.formatTien(soDuCuoiKyCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                    }
                }
                if (congLuyKeNo.compareTo(BigDecimal.ZERO) == 0) {
                    calcular.setCongLuyKeNo("");
                } else {
                    calcular.setCongLuyKeNo(Utils.formatTien(congLuyKeNo, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                if (congLuyKeCo.compareTo(BigDecimal.ZERO) == 0) {
                    calcular.setCongLuyKeCo("");
                } else {
                    calcular.setCongLuyKeCo(Utils.formatTien(congLuyKeCo, Constants.SystemOption.DDSo_TienVND, userDTO));
                }
                calcular.setBreakPage(true);
                result.add(calcular);
                continue;
            }
            congPhatSinhNo = BigDecimal.ZERO;
            congPhatSinhCo = BigDecimal.ZERO;
            soDuCuoiKyNo = BigDecimal.ZERO;
            soDuCuoiKyCo = BigDecimal.ZERO;
            congLuyKeNo = BigDecimal.ZERO;
            congLuyKeCo = BigDecimal.ZERO;
            result.add(calcular);
            result.addAll(lstDetail);
        }
        return result;
    }

    @Override
    public List<SoChiTietVatLieuDTO> getSoChiTietVatLieu(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer unitType, UUID repositoryID, String listMaterialGoods, Integer currentBook, Boolean isDependent) {
        List<SoChiTietVatLieuDTO> soChiTietVatLieuDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_CHI_TIET_VAT_LIEU] @CompanyID = :companyID, @IncludeDependentBranch = :includeDependentBranch, @FromDate = :fromDate, @ToDate = :toDate, @UnitType = :unitType, @RepositoryID = :repositoryID, @ListMaterialGoodsID = :listMaterialGoodsID, @IsWorkingWithManagementBook = :isWorkingWithManagementBook");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("isWorkingWithManagementBook", currentBook);
        params.put("companyID", companyID);
        params.put("includeDependentBranch", 0);
        if (repositoryID == null) {
            params.put("repositoryID", "00000000-0000-0000-0000-000000000000");
        } else {
            params.put("repositoryID", repositoryID);
        }
        params.put("unitType", unitType);
        params.put("listMaterialGoodsID", listMaterialGoods);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoChiTietVatLieuDTO");
        Common.setParams(query, params);
        soChiTietVatLieuDTOS = query.getResultList();
        return soChiTietVatLieuDTOS;
    }

    @Override
    public List<SoQuyTienMatDTO> getSoQuyTienMat(UUID companyID, LocalDate fromDate, LocalDate toDate, String currencyID, Integer typeLedger, Boolean isDependent, Boolean typeShowCurrency) {
        List<SoQuyTienMatDTO> soQuyTienMatDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_QUY_TIEN_MAT] :companyID, :fromDate, :toDate, :currencyID, :typeLedger, :dependent, :typeShowCurrency");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currencyID", currencyID);
        params.put("typeLedger", typeLedger);
        params.put("dependent", isDependent);
        params.put("typeShowCurrency", typeShowCurrency);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoQuyTienMatDTO");
        Common.setParams(query, params);
        soQuyTienMatDTOS = query.getResultList();
        return soQuyTienMatDTOS;
    }

    @Override
    public List<SoKeToanChiTietQuyTienMatDTO> getSoKeToanChiTietQuyTienMat(UUID companyID, LocalDate fromDate, LocalDate toDate, String currencyID, Integer typeLedger, List<String> listAccount, Boolean groupTheSameItem, Boolean dependent, Boolean typeShowCurrency) {
        List<SoKeToanChiTietQuyTienMatDTO> soKeToanChiTietQuyTienMatDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String strAccount = "";
        for (int i = 0; i < listAccount.size(); i++) {
            strAccount += "," + listAccount.get(i);
        }
        strAccount += ",";
        sql.append("EXEC [Proc_SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT] :companyID, :fromDate, :toDate, :currencyID, :typeLedger, :strAccount, :groupTheSameItem, :dependent, :typeShowCurrency");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currencyID", currencyID);
        params.put("typeLedger", typeLedger);
        params.put("strAccount", strAccount);
        params.put("groupTheSameItem", groupTheSameItem);
        params.put("dependent", dependent);
        params.put("typeShowCurrency", typeShowCurrency);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoKeToanChiTietQuyTienMatDTO");
        Common.setParams(query, params);
        soKeToanChiTietQuyTienMatDTOS = query.getResultList();
        return soKeToanChiTietQuyTienMatDTOS;
    }

    @Override
    public List<BangKeSoDuNganHangDTO> getBangKeSoDuNganHang(UUID companyID, LocalDate fromDate, LocalDate toDate, String currencyID, Integer typeLedger, String accountNumber, Boolean isDependent, Boolean typeShowCurrency) {
        List<BangKeSoDuNganHangDTO> bangKeSoDuNganHangDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BANG_KE_SO_DU_NGAN_HANG] :companyID, :fromDate, :toDate, :currencyID, :typeLedger, :accountNumber, :dependent, :typeShowCurrency ");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currencyID", currencyID);
        params.put("typeLedger", typeLedger);
        params.put("accountNumber", accountNumber);
        params.put("dependent", isDependent);
        params.put("typeShowCurrency", typeShowCurrency);
        Query query = entityManager.createNativeQuery(sql.toString(), "BangKeSoDuNganHangDTO");
        Common.setParams(query, params);
        bangKeSoDuNganHangDTOS = query.getResultList();
        return bangKeSoDuNganHangDTOS;
    }

    @Override
    public List<SoTienGuiNganHangDTO> getSoTienGuiNganHang(UUID companyID, LocalDate fromDate, LocalDate toDate, String accountNumber, String currencyID, String listBankAccountDetail, Boolean groupTheSameItem, Integer typeLedger, Boolean isDependent, Boolean typeShowCurrency) {
        List<SoTienGuiNganHangDTO> soTienGuiNganHangDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_TIEN_GUI_NGAN_HANG] :companyID, :fromDate, :toDate, :accountNumber, :currencyID, :listBankAccountDetail, :groupTheSameItem, :typeLedger, :dependent, :typeShowCurrency");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("accountNumber", accountNumber);
        params.put("currencyID", currencyID);
        params.put("listBankAccountDetail", listBankAccountDetail);
        params.put("groupTheSameItem", groupTheSameItem);
        params.put("typeLedger", typeLedger);
        params.put("dependent", isDependent);
        params.put("typeShowCurrency", typeShowCurrency);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTienGuiNganHangDTO");
        Common.setParams(query, params);
        soTienGuiNganHangDTOS = query.getResultList();
        return soTienGuiNganHangDTOS;
    }

    @Override
    public List<BangCanDoiTaiKhoanDTO> getBangCanDoiTaiKhoan(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer grade, Integer currentBook, Boolean isDependent) {
        List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BANG_CAN_DOI_TAI_KHOAN] @CompanyID = :companyID, @IncludeDependentBranch = :dependentBranch, @FromDate = :fromDate, @ToDate = :toDate, @MaxAccountGrade = :grade, @IsBalanceBothSide = :isBalanceBothSide, @IsSimilarBranch = :isSimilarBranch, @IsWorkingWithManagementBook = :currentBook");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currentBook", currentBook);
        params.put("companyID", companyID);
        params.put("isSimilarBranch", 0);
        params.put("dependentBranch", isDependent);
        params.put("isBalanceBothSide", option);
        params.put("grade", grade);
        Query query = entityManager.createNativeQuery(sql.toString(), "BangCanDoiTaiKhoanDTO");
        Common.setParams(query, params);
        bangCanDoiTaiKhoanDTOS = query.getResultList();
        return bangCanDoiTaiKhoanDTOS;
    }

    @Override
    public List<TheKhoDTO> getTheKho(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer unitType, UUID repositoryID, String materialGoods, Integer currentBook, Boolean isDependent) {
        List<TheKhoDTO> theKhoDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_THE_KHO] @CompanyID = :companyID, @IncludeDependentBranch = :includeDependentBranch, @FromDate = :fromDate, @ToDate = :toDate, @UnitType = :unitType, @RepositoryID = :repositoryID, @ListMaterialGoodsID = :listMaterialGoodsID, @IsWorkingWithManagementBook = :isWorkingWithManagementBook");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("isWorkingWithManagementBook", currentBook);
        params.put("companyID", companyID);
        params.put("includeDependentBranch", 0);
        if (repositoryID == null) {
            params.put("repositoryID", "00000000-0000-0000-0000-000000000000");
        } else {
            params.put("repositoryID", repositoryID);
        }
        params.put("unitType", unitType);
        params.put("listMaterialGoodsID", materialGoods);
        Query query = entityManager.createNativeQuery(sql.toString(), "TheKhoDTO");
        Common.setParams(query, params);
        theKhoDTOS = query.getResultList();
        return theKhoDTOS;
    }

    @Override
    public List<SoChiTietTaiKhoanDTO> getSoChiTietTaiKhoan(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, UUID companyID, Boolean isFinancialBook, Integer accountingType, Boolean isDependent, Boolean getAmountOriginal) {
        List<SoChiTietTaiKhoanDTO> soChiTietTaiKhoanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_CHI_TIET_CAC_TAI_KHOAN] @FromDate = :FromDate, @ToDate = :ToDate, @CurrencyID = :CurrencyID, @AccountNumber =:AccountNumber, @GroupTheSameItem = :GroupTheSameItem,@IsVietNamese =:IsVietNamese, @CompanyID = :CompanyID, @IsFinancialBook = :IsFinancialBook,  @AccountingType=:AccountingType");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CurrencyID", currencyID);
        params.put("AccountNumber", accountNumber);
        params.put("GroupTheSameItem", groupTheSameItem);
        params.put("IsVietNamese", true);
        params.put("CompanyID", companyID);
        params.put("IsFinancialBook", isFinancialBook);
        params.put("AccountingType", accountingType);
        sql.append(" , @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        sql.append(" , @getAmountOriginal = :getAmountOriginal");
        params.put("getAmountOriginal", getAmountOriginal);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoChiTietTaiKhoanDTO");
        Common.setParams(query, params);
        soChiTietTaiKhoanDTOS = query.getResultList();
        return soChiTietTaiKhoanDTOS;
    }

    @Override
    public List<TongHopCongNoPhaiTraDTO> getTongHopCongNoPhaiTra(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, String listMaterialGoods, Boolean isFinancialBook, UUID companyID, Boolean isDependent) {
        List<TongHopCongNoPhaiTraDTO> tongHopCongNoPhaiTraDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("exec Proc_Tong_Hop_Cong_No_Phai_Tra @FromDate = :FromDate," +
            " @ToDate = :ToDate, " +
            "     @CompanyID = :CompanyID, " +
            "     @AccountNumber = :AccountNumber, " +
            "     @AccountObjectID = :AccountObjectID, " +
            "     @CurrentBook = :CurrentBook, " +
            "     @CurrencyID = :CurrencyID");
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CurrencyID", currencyID);
        params.put("AccountNumber", accountNumber);
        params.put("CompanyID", companyID);
        params.put("AccountObjectID", listMaterialGoods);
        params.put("CurrentBook", isFinancialBook ? 0 : 1);
        Query query = entityManager.createNativeQuery(sql.toString(), "TongHopCongNoPhaiTraDTO");
        Common.setParams(query, params);
        tongHopCongNoPhaiTraDTOS = query.getResultList();
        return tongHopCongNoPhaiTraDTOS;
    }

    public List<TongHopChiTietVatLieuDTO> getTongHopChiTietVatLieu(LocalDate fromDate, LocalDate toDate, String accountNum, UUID companyID, Integer currentBook, Boolean isDependent) {
        List<TongHopChiTietVatLieuDTO> tongHopChiTietVatLieuDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BANG_TONG_HOP_CHI_TIET_VAT_LIEU] @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @IsWorkingWithManagementBook = :isWorkingWithManagementBook, @Account = :listAccount, @IncludeDependentBranch = :dependentBranch");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("isWorkingWithManagementBook", currentBook);
        params.put("companyID", companyID);
        params.put("listAccount", accountNum);
        params.put("dependentBranch", isDependent);
        Query query = entityManager.createNativeQuery(sql.toString(), "TongHopChiTietVatLieuDTO");
        Common.setParams(query, params);
        tongHopChiTietVatLieuDTOS = query.getResultList();
        return tongHopChiTietVatLieuDTOS;
    }

    @Override
    public List<ChiTietCongNoPhaiTraDTO> getChiTietCongNoPhaiTra(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean isFinancialBook, UUID uuid, Boolean dependent) {
        List<ChiTietCongNoPhaiTraDTO> chiTietCongNoPhaiTraDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("exec Proc_Chi_Tiet_Cong_No_Phai_Tra @FromDate = :FromDate, @ToDate = :ToDate, @AccountNumber = :AccountNumber, " +
            "     @companyID = :CompanyID, @CurrencyID = :CurrencyID, " +
            "     @AccountObjectID = :AccountObjectID, @GroupTheSameItem = :groupTheSameItem,  @CurrentBook = :CurrentBook ");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CurrencyID", currencyID);
        params.put("AccountNumber", accountNumber);
        params.put("CompanyID", uuid);
        params.put("groupTheSameItem", groupTheSameItem);
        params.put("AccountObjectID", materialGoods);
        params.put("CurrentBook", isFinancialBook ? 0 : 1);
        sql.append(", @isDependent = :isDependent");
        if (dependent != null) {
            params.put("isDependent", dependent);
        } else {
            params.put("isDependent", false);
        }
//        params.put("CurrentBook", isFinancialBook ? 1 : 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "ChiTietCongNoPhaiTraDTO");
        Common.setParams(query, params);
        chiTietCongNoPhaiTraDTOS = query.getResultList();
        return chiTietCongNoPhaiTraDTOS;
    }

    public List<TongHopTonKhoDTO> getTongHopTonKho(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer unitType, String repositoryIDs, Integer currentBook, UUID materialGoodsCategoryID, Boolean isDependent) {
        List<TongHopTonKhoDTO> tongHopTonKhoDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_TONG_HOP_TON_KHO] @FromDate = :fromDate, @ToDate = :toDate, @StockID = :repositoryIDs, @CompanyID = :companyID,  @UnitType = :unitType, @IsWorkingWithManagementBook = :isWorkingWithManagementBook, @MaterialGoodsCategoryID = :materialGoodsCategoryID, @IncludeDependentBranch = :dependentBranch");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("isWorkingWithManagementBook", currentBook);
        params.put("companyID", companyID);
        params.put("unitType", unitType);
        params.put("repositoryIDs", repositoryIDs);
        params.put("dependentBranch", isDependent);
        if (materialGoodsCategoryID == null) {
            params.put("materialGoodsCategoryID", "00000000-0000-0000-0000-000000000000");
        } else {
            params.put("materialGoodsCategoryID", materialGoodsCategoryID);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "TongHopTonKhoDTO");
        Common.setParams(query, params);
        tongHopTonKhoDTOS = query.getResultList();
        return tongHopTonKhoDTOS;
    }

    @Override
    public List<BangCanDoiKeToanDTO> getKetQuaHDKD(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook, Boolean isDependent) {
        List<BangCanDoiKeToanDTO> bangCanDoiKeToanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BAO_CAO_KET_QUA_HDKD] @CompanyID = :companyID, @IsWorkingWithManagementBook = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate, @PrevFromDate = :fromDatePre, @PrevToDate = :toDatePre, @IncludeDependentBranch = :dependentBranch");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("fromDatePre", fromDate);
        params.put("toDatePre", toDate);
        params.put("typeLedger", currentBook);
        params.put("companyID", companyID);
        params.put("dependentBranch", isDependent);
        Query query = entityManager.createNativeQuery(sql.toString(), "BangCanDoiKeToanDTO");
        Common.setParams(query, params);
        bangCanDoiKeToanDTOS = query.getResultList();
        return bangCanDoiKeToanDTOS;
    }

    @Override
    public List<BangCanDoiKeToanDTO> getLuuChuyenTienTe(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook, Boolean option, Boolean isDependent) {
        List<BangCanDoiKeToanDTO> bangCanDoiKeToanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (Boolean.TRUE.equals(option)) {
            sql.append("EXEC [Proc_LUU_CHUYEN_TIEN_TE] @CompanyID = :companyID, @IsWorkingWithManagementBook = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate, @PrevFromDate = :fromDatePre, @PrevToDate = :toDatePre, @IncludeDependentBranch = :dependentBranch");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("fromDatePre", fromDate);
            params.put("toDatePre", toDate);
            params.put("typeLedger", currentBook);
            params.put("companyID", companyID);
            params.put("dependentBranch", isDependent);
        } else {
            sql.append("EXEC [Proc_LUU_CHUYEN_TIEN_TE_GT] @CompanyID = :companyID, @FromDate = :fromDate, @ToDate = :toDate, @PrevFromDate = :fromDatePre, @PrevToDate = :toDatePre, @IsWorkingWithManagementBook = :typeLedger, @IncludeDependentBranch = :dependentBranch");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("fromDatePre", fromDate.plusYears(-1));
            params.put("toDatePre", toDate.plusYears(-1));
            params.put("typeLedger", currentBook);
            params.put("companyID", companyID);
            params.put("dependentBranch", isDependent);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "BangCanDoiKeToanDTO");
        Common.setParams(query, params);
        bangCanDoiKeToanDTOS = query.getResultList();
        return bangCanDoiKeToanDTOS;
    }

    @Override
    public List<BangCanDoiTaiKhoanDTO> getBaoCaoTaiChinh(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook) {
        List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BANG_CAN_DOI_TAI_KHOAN] @CompanyID = :companyID, @IncludeDependentBranch = :includeDependentBranch, @FromDate = :fromDate, @ToDate = :toDate, @MaxAccountGrade = :grade, @IsBalanceBothSide = :isBalanceBothSide, @IsSimilarBranch = :isSimilarBranch, @IsWorkingWithManagementBook = :currentBook");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currentBook", currentBook);
        params.put("companyID", companyID);
        params.put("isSimilarBranch", 0);
        params.put("includeDependentBranch", 0);
        params.put("isBalanceBothSide", 1);
        params.put("grade", 2);
        Query query = entityManager.createNativeQuery(sql.toString(), "BangCanDoiTaiKhoanDTO");
        Common.setParams(query, params);
        bangCanDoiTaiKhoanDTOS = query.getResultList();
        return bangCanDoiTaiKhoanDTOS;
    }

    @Override
    public List<SoNhatKyMuaHangDTO> getSoNhatKyMuaHang(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID,
                                                       Boolean isMBook, Boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_NHAT_KY_MUA_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @IsNotPaid = :IsNotPaid, @CompanyID = :CompanyID, @IsMBook = :IsMBook");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("IsNotPaid", option);
        params.put("CompanyID", companyID);
        params.put("IsMBook", isMBook);
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoNhatKyMuaHangDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<SoNhatKyBanHangDTO> getSoNhatKyBanHang(LocalDate fromDate, LocalDate toDate, Boolean option,
                                                       UUID companyID, Boolean isMBook, Boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SO_NHAT_KY_BAN_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @IsNotPaid = :IsNotPaid, @CompanyID = :CompanyID, @IsMBook = :IsMBook");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("IsNotPaid", option);
        params.put("CompanyID", companyID);
        params.put("IsMBook", isMBook);
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoNhatKyBanHangDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    public List<TongHopCongNoPhaiThuDTO> getTongHopCongNoPhaiThu(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean equals, UUID companyID, Boolean dependent) {
        List<TongHopCongNoPhaiThuDTO> tongHopCongNoPhaiThuDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("exec Proc_Tong_Hop_Cong_No_Phai_Thu @FromDate = :FromDate," +
            " @ToDate = :ToDate, " +
            "     @CompanyID = :CompanyID, " +
            "     @AccountNumber = :AccountNumber, " +
            "     @AccountObjectID = :AccountObjectID, " +
            "     @CurrentBook = :CurrentBook, " +
            "     @IsShowInPeriodOnly = :IsShowInPeriodOnly, " +
            "     @CurrencyID = :CurrencyID");
        sql.append(", @isDependent = :isDependent");
        if (dependent != null) {
            params.put("isDependent", dependent);
        } else {
            params.put("isDependent", false);
        }
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CurrencyID", currencyID);
        params.put("AccountNumber", accountNumber);
        params.put("CompanyID", companyID);
        params.put("CurrentBook", equals ? 0 : 1);
        params.put("IsShowInPeriodOnly", groupTheSameItem);
        params.put("AccountObjectID", materialGoods);
        Query query = entityManager.createNativeQuery(sql.toString(), "TongHopCongNoPhaiThuDTO");
        Common.setParams(query, params);
        tongHopCongNoPhaiThuDTOS = query.getResultList();
        return tongHopCongNoPhaiThuDTOS;
    }

    @Override
    public List<ChiTietCongNoPhaiThuDTO> getChiTietCongNoPhaiThu(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean equals, UUID companyID, Boolean dependent) {
        List<ChiTietCongNoPhaiThuDTO> chiTietCongNoPhaiThuDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("exec Proc_Chi_Tiet_Cong_No_Phai_Thu @IsSimilarSum = :IsSimilarSum, @FromDate = :FromDate, " +
            "     @ToDate = :ToDate, @CompanyID = :CompanyID, " +
            "     @AccountNumber = :AccountNumber, " +
            "     @AccountObjectID = :AccountObjectID, " +
            "     @currentBook = :currentBook, " +
            "     @CurrencyID = :CurrencyID");
        sql.append(", @isDependent = :isDependent");
        if (dependent != null) {
            params.put("isDependent", dependent);
        } else {
            params.put("isDependent", false);
        }
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CurrencyID", currencyID);
        params.put("AccountNumber", accountNumber);
        params.put("CompanyID", companyID);
        params.put("IsSimilarSum", groupTheSameItem);
        params.put("AccountObjectID", materialGoods);
        params.put("currentBook", equals ? 0 : 1);
        Query query = entityManager.createNativeQuery(sql.toString(), "ChiTietCongNoPhaiThuDTO");
        Common.setParams(query, params);
        chiTietCongNoPhaiThuDTOS = query.getResultList();
        return chiTietCongNoPhaiThuDTOS;
    }

    @Override
    public List<SucKhoeTaiChinhDoanhNghiepDTO> getSucKhoeTaiChinhDoanhNghiep(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook) {
        List<SucKhoeTaiChinhDoanhNghiepDTO> sucKhoeTaiChinhDoanhNghiepDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SUC_KHOE_TAI_CHINH_DN] @CompanyID = :companyID, @TypeLedger = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate, @IsSimilarBranch = :isSimilarBranch, @isPrintByYear = :isPrintByYear, @PrevFromDate = :fromDate, @PrevToDate = :toDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", currentBook);
        params.put("companyID", companyID);
        params.put("isSimilarBranch", 0);
        params.put("isPrintByYear", 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "SucKhoeTaiChinhDoanhNghiepDTO");
        Common.setParams(query, params);
        sucKhoeTaiChinhDoanhNghiepDTOS = query.getResultList();
        return sucKhoeTaiChinhDoanhNghiepDTOS;
    }

    @Override
    public List<BieuDoTongHopDTO> getBieuDoTongHop(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook) {
        List<BieuDoTongHopDTO> bieuDoTongHopDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BIEU_DO_TONG_HOP] @CompanyID = :companyID, @TypeLedger = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate, @IsSimilarBranch = :isSimilarBranch, @isPrintByYear = :isPrintByYear, @PrevFromDate = :fromDate, @PrevToDate = :toDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", currentBook);
        params.put("companyID", companyID);
        params.put("isSimilarBranch", 0);
        params.put("isPrintByYear", 0);
        Query query = entityManager.createNativeQuery(sql.toString(), "BieuDoTongHopDTO");
        Common.setParams(query, params);
        bieuDoTongHopDTOS = query.getResultList();
        return bieuDoTongHopDTOS;
    }

    @Override
    public List<BieuDoDoanhThuChiPhiDTO> getBieuDoDoanhThuChiPhi(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook) {
        List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BIEU_DO_DOANH_THU_CHI_PHI] @CompanyID = :companyID, @TypeLedger = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", currentBook);
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "BieuDoDoanhThuChiPhiDTO");
        Common.setParams(query, params);
        bieuDoDoanhThuChiPhiDTOS = query.getResultList();
        return bieuDoDoanhThuChiPhiDTOS;
    }

    @Override
    public List<BieuDoDoanhThuChiPhiDTO> getBieuDoNoPhaiThuTra(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook) {
        List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_BIEU_DO_NO_PHAI_THU_NO_PHAI_TRA] @CompanyID = :companyID, @TypeLedger = :typeLedger, @FromDate = :fromDate, @ToDate = :toDate");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", currentBook);
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "BieuDoNoPhaiThuPhaiTraDTO");
        Common.setParams(query, params);
        bieuDoDoanhThuChiPhiDTOS = query.getResultList();
        return bieuDoDoanhThuChiPhiDTOS;
    }

    @Override
    public List<SoChiTietMuaHangDTO> getSoChiTietMuaHang(LocalDate fromDate, LocalDate toDate, String accountingObjects,
                                                         String listMaterialGoods, UUID companyID, boolean isMBook,
                                                         UUID employeeID, Boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_CHI_TIET_MUA_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @AccountObjectID = :accountingObjects, @MaterialGoods = :listMaterialGoods, @CompanyID = :CompanyID, @IsMBook = :IsMBook, @EmployeeID = :employeeID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("accountingObjects", !Strings.isNullOrEmpty(accountingObjects) ? accountingObjects : null);
        params.put("listMaterialGoods", !Strings.isNullOrEmpty(listMaterialGoods) ? listMaterialGoods : null);
        params.put("CompanyID", companyID);
        params.put("IsMBook", isMBook);
        if (employeeID == null) {
            params.put("employeeID", "00000000-0000-0000-0000-000000000000");
        } else {
            params.put("employeeID", employeeID);
        }
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoChiTietMuaHangDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<SoChiTietBanHangDTO> getSoChiTietBanHang(LocalDate fromDate, LocalDate toDate, String accountingObjects,
                                                         String materialGoods, UUID companyID, boolean isMBook, Boolean isDependent) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_CHI_TIET_BAN_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @AccountObjectID = :accountingObjects, @MaterialGoods = :listMaterialGoods, @CompanyID = :CompanyID, @IsMBook = :IsMBook");
//        sql.append("EXEC [Proc_CHI_TIET_MUA_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @AccountObjectID = :accountingObjects, @MaterialGoods = :listMaterialGoods, @CompanyID = :CompanyID, @IsMBook = :IsMBook, @EmployeeID = :employeeID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("accountingObjects", !Strings.isNullOrEmpty(accountingObjects) ? accountingObjects : null);
        params.put("listMaterialGoods", !Strings.isNullOrEmpty(materialGoods) ? materialGoods : null);
        params.put("CompanyID", companyID);
        params.put("IsMBook", isMBook);
        sql.append(", @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoChiTietBanHangDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<TheoDoiMaThongKeTheoTaiKhoanDTO> getSoTheoDoiMaThongKeTheoTaiKhoan(LocalDate fromDate, LocalDate toDate, String statisticsCodeID, String account, UUID companyID, Boolean dependent, Integer typeLedger) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SoTheoDoiMaThongKeTheoSoTaiKhoan] @FromDate = :FromDate, @ToDate = :ToDate, @StatisticsCodeID = :statisticsCodeID, @Account = :account, @CompanyID = :companyID, @IsDependent = :isDependent, @TypeLedger = :typeLedger");
//        sql.append("EXEC [Proc_CHI_TIET_MUA_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @AccountObjectID = :accountingObjects, @MaterialGoods = :listMaterialGoods, @CompanyID = :CompanyID, @IsMBook = :IsMBook, @EmployeeID = :employeeID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("statisticsCodeID", !Strings.isNullOrEmpty(statisticsCodeID) ? statisticsCodeID : null);
        params.put("account", !Strings.isNullOrEmpty(account) ? account : null);
        params.put("companyID", companyID);
        params.put("isDependent", dependent);
        params.put("typeLedger", typeLedger);
//        params.put("CompanyID", companyID);
//        params.put("IsMBook", isMBook);
//        sql.append(", @isDependent = :isDependent");
//        if (isDependent != null) {
//            params.put("isDependent", isDependent);
//        } else {
//            params.put("isDependent", false);
//        }
        Query query = entityManager.createNativeQuery(sql.toString(), "TheoDoiMaThongKeTheoTaiKhoanDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<TheoDoiMaThongKeTheoKhoanMucChiPhiDTO> getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(LocalDate fromDate, LocalDate toDate, String statisticsCodeID, String expenseItems, UUID companyID, Boolean dependent, Integer typeLedger) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SoTheoDoiMaThongKeTheoKhoanMucChiPhi] @FromDate = :FromDate, @ToDate = :ToDate, @StatisticsCodeID = :statisticsCodeID, @ExpenseItemID = :expenseItemID, @CompanyID = :companyID, @IsDependent = :isDependent, @TypeLedger = :typeLedger");
//        sql.append("EXEC [Proc_CHI_TIET_MUA_HANG] @FromDate = :FromDate, @ToDate = :ToDate, @AccountObjectID = :accountingObjects, @MaterialGoods = :listMaterialGoods, @CompanyID = :CompanyID, @IsMBook = :IsMBook, @EmployeeID = :employeeID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("statisticsCodeID", !Strings.isNullOrEmpty(statisticsCodeID) ? statisticsCodeID : null);
        params.put("expenseItemID", !Strings.isNullOrEmpty(expenseItems) ? expenseItems : null);
        params.put("companyID", companyID);
        params.put("isDependent", dependent);
        params.put("typeLedger", typeLedger);
//        params.put("CompanyID", companyID);
//        params.put("IsMBook", isMBook);
//        sql.append(", @isDependent = :isDependent");
//        if (isDependent != null) {
//            params.put("isDependent", isDependent);
//        } else {
//            params.put("isDependent", false);
//        }
        Query query = entityManager.createNativeQuery(sql.toString(), "TheoDoiMaThongKeTheoKhoanMucChiPhiDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    public List<PhanBoChiPhiTraTruocDTO> getPhanBoChiPhiTraTruoc(LocalDate fromDate, LocalDate toDate, UUID uuid, boolean equals, Boolean dependent) {
        List<PhanBoChiPhiTraTruocDTO> phanBoChiPhiTraTruocDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select aa.id                                                   ID, " +
            "       aa.companyid, " +
            "       aa.typeledger, " +
            "       aa.typeexpense, " +
            "       aa.prepaidexpensecode, " +
            "       aa.prepaidexpensename, " +
            "       convert(nvarchar, aa.date, 103)                         date, " +
            "       aa.amount, " +
            "       aa.allocationtime, " +
            "       aa.allocatedperiod, " +
            "       aa.allocatedamount, " +
            "       aa.allocationaccount, " +
            "       aa.isactive, " +
            "       aa.isAllocation, " +
            "       aa.allocationAmount1                                    allocationAmountAfter, " +
            "       aa.allocatedPeriod1                                     allocatedPeriodAfter,  " +
            "       (aa.AllocationTime - aa.allocatedPeriod1)               allocatedPeriodRest, " +
            "       aa.allocationAmount1             accumulated, " +
            "       aa.Amount - aa.allocationAmount1 amountRest " +
            "from ( " +
            "         select pr.id                                                                                ID, " +
            "                pr.companyid, " +
            "                pr.typeledger, " +
            "                pr.typeexpense, " +
            "                pr.prepaidexpensecode, " +
            "                pr.prepaidexpensename, " +
            "                pr.date, " +
            "                pr.amount, " +
            "                COALESCE((select sum(AllocationAmount) " +
            "                          from GotherVoucherDetailExpense gov " +
            "                                   left join GOtherVoucher gv on gv.id = gov.GOtherVoucherID and TypeID = 709 " +
            "                          where TypeID = 709 and gv.PostedDate <= :ToDate " +
            "                            and gv.CompanyID in (select id from Func_getCompany (:CompanyID, :isDependent)) and gv.Recorded = 1 " +
            "                            and TypeLedger in (:typeLedger, 2) " +
            "                            and pr.id = gov.PrepaidExpenseID), 0) + " +
            "                COALESCE(pr.AllocationAmount, 0)                                                     allocationAmount1, " +
            "                ((select count(*) " +
            "                                  from GotherVoucherDetailExpense godtl " +
            "                                           left join PrepaidExpense pr1 on pr1.ID = godtl.PrepaidExpenseID " +
            "                                           left join GotherVoucher gov on gov.id = godtl.GOtherVoucherID " +
            "                                  where gov.TypeLedger = :typeLedger and gov.PostedDate <= :ToDate" +
            "                                    and gov.CompanyID in (select id from Func_getCompany (:CompanyID, :isDependent)) and gov.Recorded = 1 " +
            "                                    and pr1.ID = pr.ID) + COALESCE(pr.AllocatedPeriod, 0))                    AllocatedPeriod1, " +
            "                pr.allocationtime, " +
            "                pr.allocatedperiod, " +
            "                pr.allocatedamount, " +
            "                pr.allocationaccount, " +
            "                pr.isactive, " +
            "                case " +
            "                    when (COALESCE((select count(*) from GotherVoucherDetailExpense where PrepaidExpenseID = pr.id), " +
            "                                   0)) <> 0 " +
            "                        then CAST(1 AS BIT) " +
            "                    else CAST(0 AS BIT) end                                                          isAllocation " +
            "         from PrepaidExpense pr " +
            "         where pr.CompanyID in (select id from Func_getCompany (:CompanyID, :isDependent)) " +
            "           and pr.Date <= :ToDate " +
            "           and pr.TypeLedger in (:typeLedger, 2)) aa " +
            "order by aa.PrepaidExpenseCode asc");
        if (dependent != null) {
            params.put("isDependent", dependent);
        } else {
            params.put("isDependent", false);
        }
        params.put("typeLedger", equals ? 1 : 0);
        params.put("CompanyID", uuid);
//        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "PhanBoChiPhiTraTruocDTO");
        Common.setParams(query, params);
        phanBoChiPhiTraTruocDTOS = query.getResultList();
        return phanBoChiPhiTraTruocDTOS;
    }

    @Override
    public List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> getSoTheoDoiTHCP(LocalDate fromDate, LocalDate toDate, String costSetID, String account,  Integer phienLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC Proc_SoTheoDoiChiTietTheoDoiTuongTHCP @FromDate = :fromDate, @ToDate = :toDate, @CostSetID = :costSetID, @Account = :account, @PhienLamViec = :phienLamViec ");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("costSetID", !Strings.isNullOrEmpty(costSetID) ? costSetID : null);
        params.put("account", !Strings.isNullOrEmpty(account) ? account : null);
        params.put("phienLamViec", phienLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO");
        Common.setParams(query, params);
        List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> detail = new ArrayList<>();
        detail = query.getResultList();
        return detail;
    }

    @Override
    public List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> getSoTheoDoiTHCPTheoChiPhi(LocalDate fromDate, LocalDate toDate, String costSetID, String expenseItemID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC Proc_SoTheoDoiDoiTuongTHCP  @FromDate = :fromDate, @ToDate = :toDate, @CostSetID = :costSetID, @ExpenseItemID = :expenseItemID");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("costSetID", !Strings.isNullOrEmpty(costSetID) ? costSetID : null);
        params.put("expenseItemID", !Strings.isNullOrEmpty(expenseItemID) ? expenseItemID : null);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO");
        Common.setParams(query, params);
        List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> detail = new ArrayList<>();
        detail = query.getResultList();
        return detail;
    }

    @Override
    public List<SoTheTinhGiaThanhDTO> getTheTinhGiaThanh(UUID companyID, String listMaterialGoodsID, String listCostSetID, UUID cPPeriodID, Integer typeMethod, Boolean isDependent) {
        List<SoTheTinhGiaThanhDTO> soTheTinhGiaThanhDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_THE_TINH_GIA_THANH] :companyID, :listMaterialGoodsID, :listCostSetID, :cPPeriodID, 0, :typeMethod, :isDependent");
        params.put("companyID", companyID);
        params.put("listMaterialGoodsID", listMaterialGoodsID);
        params.put("listCostSetID", listCostSetID);
        params.put("cPPeriodID", cPPeriodID);
        params.put("typeMethod", typeMethod);
        params.put("isDependent", isDependent);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheTinhGiaThanhDTO");
        Common.setParams(query, params);
        soTheTinhGiaThanhDTOS = query.getResultList();
        return soTheTinhGiaThanhDTOS;
    }

    @Override
    public List<TongHopCPTheoKMCPDTO> getTongHopCPTheoKMCP(UUID companyID, LocalDate fromDate, LocalDate toDate, Integer typeLedger, String accountNumber, String expenseItems, Boolean isDependent) {
        List<TongHopCPTheoKMCPDTO> tongHopCPTheoKMCPDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_TONG_HOP_CHI_PHI_THEO_KMCP] :companyID, :fromDate, :toDate, :typeLedger, :accountNumber, :expenseItems , :dependent");
        params.put("companyID", companyID);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("typeLedger", typeLedger);
        params.put("accountNumber", accountNumber);
        params.put("expenseItems", expenseItems);
        params.put("dependent", isDependent);
        Query query = entityManager.createNativeQuery(sql.toString(), "TongHopCPTheoKMCPDTO");
        Common.setParams(query, params);
        tongHopCPTheoKMCPDTOS = query.getResultList();
        return tongHopCPTheoKMCPDTOS;
    }

    @Override
    public List<SoCongCuDungCuDTO> getSoCongCuDungCu(LocalDate fromDate, LocalDate toDate, String toolIDs,
                                                     UUID companyID, Boolean isDependent) {
        List<SoCongCuDungCuDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SA_SoCongCuDungCu] @FromDate = :FromDate, @ToDate = :ToDate, @ToolsIDs = :ToolsIDs, @CompanyID = :CompanyID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("ToolsIDs", toolIDs);
        params.put("CompanyID", companyID);
        sql.append(" , @isDependent = :isDependent");
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoCongCuDungCuDTO");
        Common.setParams(query, params);
        result = query.getResultList();
        return result;
    }

    @Override
    public List<SoTheoDoiCCDCTaiNoiSuDungDTO> getSoTheoDoiCCDCTaiNoiSD(LocalDate fromDate, LocalDate toDate, String departmentIDs, Integer phienSoLamViec) {
        List<SoTheoDoiCCDCTaiNoiSuDungDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SoTheoDoiCCDCTaiNoiSuDung] @FromDate = :FromDate, @ToDate = :ToDate, @DepartmentIDs = :DepartmentIDs, @TypeLedger = :TypeLedger");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("DepartmentIDs", departmentIDs);
        params.put("TypeLedger", phienSoLamViec);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiCCDCTaiNoiSuDungDTO");
        Common.setParams(query, params);
        result = query.getResultList();
        return result;
    }

    @Override
    public List<SoTheoDoiLaiLoTheoHoaDonDTO> getSoTheoDoiLaiLoTheoHoaDon(LocalDate fromDate, LocalDate toDate, UUID companyID, Boolean isDependent, Integer phienSoLamViec) {
        List<SoTheoDoiLaiLoTheoHoaDonDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SA_SoTheoDoiLaiLoTheoHoaDon] @FromDate = :FromDate, @ToDate = :ToDate, @CompanyID = :CompanyID, @isDependent = :isDependent, @IsMBook = :IsMBook");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("CompanyID", companyID);
        params.put("IsMBook", phienSoLamViec);
        if (isDependent != null) {
            params.put("isDependent", isDependent);
        } else {
            params.put("isDependent", false);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiLaiLoTheoHoaDonDTO");
        Common.setParams(query, params);
        result = query.getResultList();
        return result;
    }

    @Override
    public List<SoTheoDoiCongNoPhaiThuTheoHoaDonDTO> getSoTheoDoiCongNoPhaiThuTheoHoaDon(LocalDate fromDate, LocalDate toDate, String accountNumber,
                                                                                         String currencyID, String accountingObjectIDs,
                                                                                         Boolean typeShowCurrency, UUID companyID) {
        List<SoTheoDoiCongNoPhaiThuTheoHoaDonDTO> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [Proc_SA_SoTheoDoiCongNoPhaiThuTheoHoaDon] @FromDate = :FromDate, @ToDate = :ToDate, " +
            "@ListAccountNumber = :accountNumber, @CurrencyID = :currencyID, " +
            "@AccountObjectID = :accountingObjectIDs, @typeShowCurrency = :typeShowCurrency, @CompanyID = :companyID");
        params.put("FromDate", fromDate);
        params.put("ToDate", toDate);
        params.put("accountNumber", accountNumber);
        params.put("currencyID", currencyID);
        params.put("accountingObjectIDs", accountingObjectIDs);
        params.put("typeShowCurrency", typeShowCurrency);
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiCongNoPhaiThuTheoHoaDonDTO");
        Common.setParams(query, params);
        result = query.getResultList();
        return result;
    }

    @Override
    public List<Map<String, Object>> getSoChiPhiSXKD(UUID companyID, String accountNumber, LocalDate fromDate, LocalDate toDate, Integer phienSoLamViec, String listAccountNumber, String listCostSets) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [PROC_SO_CHI_PHI_SAN_XUAT_KINH_DOANH] :companyID, :accountNumber ,:fromDate, :toDate, :phienSoLamViec, :listAccountNumber, :listCostSets");
        params.put("companyID", companyID);
        params.put("accountNumber", accountNumber);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("phienSoLamViec", phienSoLamViec);
        params.put("listAccountNumber", listAccountNumber);
        params.put("listCostSets", listCostSets);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String,Object>> result = nativeQuery.getResultList();
        return result;
    }

    @Override
    public List<SoTheoDoiThanhToanBangNgoaiTeDTO> getSoTheoDoiThanhToanBangNgoaiTe(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, String listAccountingObjects, Integer phienLamViec, UUID companyID, Boolean dependent) {
        List<SoTheoDoiThanhToanBangNgoaiTeDTO> details = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC Proc_SA_SoTheoDoiThanhToanBangNgoaiTe @FromDate = :fromDate, @ToDate = :toDate, @TypeMoney = :currencyID, @Account = :account, @AccountObjectID = :accountingObjectID, @PhienLamViec = :phienLamViec, @CompanyID = :id, @Dependent = :isDependent ");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("currencyID", currencyID);
        params.put("account", accountNumber);
        params.put("accountingObjectID", listAccountingObjects);
        params.put("phienLamViec", phienLamViec);
        params.put("id", companyID);
        params.put("isDependent", dependent);
        Query query = entityManager.createNativeQuery(sql.toString(), "SoTheoDoiThanhToanBangNgoaiTeDTO");
        Common.setParams(query, params);
        details = query.getResultList();
        return details;
    }
}
