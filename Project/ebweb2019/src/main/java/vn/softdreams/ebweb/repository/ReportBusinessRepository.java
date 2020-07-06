package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.service.dto.Report.*;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoKhoanMucChiPhiDTO;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoTaiKhoanDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.BieuDoDoanhThuChiPhiDTO;
import vn.softdreams.ebweb.web.rest.dto.BieuDoTongHopDTO;
import vn.softdreams.ebweb.web.rest.dto.SucKhoeTaiChinhDoanhNghiepDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReportBusinessRepository {

    /**
     * @param fromDate
     * @param toDate
     * @param groupTheSameItem
     * @param showAccumAmount
     * @param companyID
     * @param isFinancialBook
     * @return
     * @Author Hautv
     */
    List<SoNhatKyChungDTO> getSoNhatKyChung(LocalDate fromDate,
                                            LocalDate toDate,
                                            Boolean groupTheSameItem,
                                            Boolean showAccumAmount,
                                            UUID companyID,
                                            Boolean isFinancialBook,
                                            Boolean isDependent);

    List<SoNhatKyThuTienDTO> getSoNhatKyThuChi(LocalDate fromDate,
                                               LocalDate toDate,
                                               Boolean isSimilarSum,
                                               UUID companyID,
                                               String currencyID,
                                               String accountNumber,
                                               UUID bankAccountID,
                                               Boolean isFinancialBook,
                                               Boolean isDependent,
                                               String type, Boolean getAmountOriginal);
    List<BangKeMuaBanDTO> getBangKeMuaBan(LocalDate fromDate,
                                               LocalDate toDate,
                                               Boolean isSimilarSum,
                                               UUID companyID,
                                               Integer typeLedger,
                                               String type, Boolean isBill, Boolean isDependent);
    List<BangCanDoiKeToanDTO> getBangCanDoiKeToan(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer typeLedger, Boolean isDependent);


    /**
     * @param fromDate
     * @param toDate
     * @param accountNumber
     * @param isSimilarSum
     * @param companyID
     * @param isFinancialBook
     * @param accountingType
     * @return
     * @Author Hautv
     */
    List<SoCaiHTNhatKyChungDTO> getSoCaiHTNhatKyChung(LocalDate fromDate,
                                                      LocalDate toDate,
                                                      String accountNumber,
                                                      Boolean isSimilarSum,
                                                      UUID companyID,
                                                      Boolean isFinancialBook,
                                                      Integer accountingType, UserDTO userDTO, Boolean isDependent
    );

    List<SoChiTietVatLieuDTO> getSoChiTietVatLieu(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer unitType, UUID repositoryID, String listMaterialGoods, Integer currentBook, Boolean isDependent);

    List<SoQuyTienMatDTO> getSoQuyTienMat(UUID companyID, LocalDate fromDate,
                                          LocalDate toDate,
                                          String currencyID,
                                          Integer typeLedger,
                                          Boolean isDependent,
                                          Boolean typeShowCurrency
    );

    List<SoKeToanChiTietQuyTienMatDTO> getSoKeToanChiTietQuyTienMat(UUID companyID, LocalDate fromDate,
                                                                    LocalDate toDate,
                                                                    String currencyID,
                                                                    Integer typeLedger,
                                                                    List<String> listAccount, Boolean groupTheSameItem, Boolean dependent,
                                                                    Boolean typeShowCurrency
    );

    List<BangKeSoDuNganHangDTO> getBangKeSoDuNganHang(UUID companyID, LocalDate fromDate,
                                                      LocalDate toDate,
                                                      String currencyID,
                                                      Integer typeLedger,
                                                      String accountNumber,
                                                      Boolean isDependent,
                                                      Boolean typeShowCurrency
    );

    List<SoTienGuiNganHangDTO> getSoTienGuiNganHang(UUID companyID, LocalDate fromDate,
                                                    LocalDate toDate,
                                                    String accountNumber,
                                                    String currencyID,
                                                    String listBankAccountDetail,
                                                    Boolean groupTheSameItem,
                                                    Integer typeLedger,
                                                    Boolean dependent,
                                                    Boolean typeShowCurrency
    );

    List<BangCanDoiTaiKhoanDTO> getBangCanDoiTaiKhoan(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer grade, Integer currentBook, Boolean isDependent);

    List<TheKhoDTO> getTheKho(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID, Integer unitType, UUID repositoryID, String materialGoods, Integer currentBook, Boolean isDependent);

    List<SoChiTietTaiKhoanDTO> getSoChiTietTaiKhoan(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, UUID companyID, Boolean isFinancialBook, Integer accountingType, Boolean isDependent, Boolean getAmountOriginal);

    List<TongHopCongNoPhaiTraDTO> getTongHopCongNoPhaiTra(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, String listMaterialGoods, Boolean isFinancial, UUID companyID, Boolean isDependent);

    List<TongHopChiTietVatLieuDTO> getTongHopChiTietVatLieu(LocalDate fromDate, LocalDate toDate, String accountNum, UUID companyID, Integer currentBook, Boolean isDependent);

    List<ChiTietCongNoPhaiTraDTO> getChiTietCongNoPhaiTra(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean isFinancialBook, UUID uuid, Boolean dependent);

    List<TongHopTonKhoDTO> getTongHopTonKho(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer unitType, String repositoryIDs, Integer currentBook, UUID materialGoodsCategoryID, Boolean isDependent);

    List<BangCanDoiKeToanDTO> getKetQuaHDKD(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook, Boolean isDependent);

    List<BangCanDoiKeToanDTO> getLuuChuyenTienTe(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook, Boolean option, Boolean isDependent);

    List<BangCanDoiTaiKhoanDTO> getBaoCaoTaiChinh(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook);

    List<SoNhatKyMuaHangDTO> getSoNhatKyMuaHang(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID,
                                                Boolean IsMBook, Boolean isDependent);

    List<TongHopCongNoPhaiThuDTO> getTongHopCongNoPhaiThu(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean equals, UUID uuid, Boolean dependent);

    List<SoNhatKyBanHangDTO> getSoNhatKyBanHang(LocalDate fromDate, LocalDate toDate, Boolean option, UUID companyID,
                                                Boolean IsMBook, Boolean isDependent);

    List<ChiTietCongNoPhaiThuDTO> getChiTietCongNoPhaiThu(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, Boolean groupTheSameItem, String materialGoods, boolean equals, UUID uuid, Boolean dependent);

    List<SucKhoeTaiChinhDoanhNghiepDTO> getSucKhoeTaiChinhDoanhNghiep(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook);

    List<BieuDoTongHopDTO> getBieuDoTongHop(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook);

    List<BieuDoDoanhThuChiPhiDTO> getBieuDoDoanhThuChiPhi(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook);

    List<BieuDoDoanhThuChiPhiDTO> getBieuDoNoPhaiThuTra(LocalDate fromDate, LocalDate toDate, UUID companyID, Integer currentBook);

    List<SoChiTietMuaHangDTO> getSoChiTietMuaHang(LocalDate fromDate, LocalDate toDate, String accountingObjects,
                                                  String listMaterialGoods, UUID companyID, boolean isMBook,
                                                  UUID employeeID, Boolean isDependent);

    List<SoChiTietBanHangDTO> getSoChiTietBanHang(LocalDate fromDate, LocalDate toDate, String accountingObjects,
                                                  String materialGoods, UUID companyID, boolean isMBook, Boolean isDependent);

    List<PhanBoChiPhiTraTruocDTO> getPhanBoChiPhiTraTruoc(LocalDate fromDate, LocalDate toDate, UUID uuid, boolean equals, Boolean dependent);


    List<SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO> getSoTheoDoiTHCP(LocalDate fromDate, LocalDate toDate, String costSetID, String Account, Integer phienLamViec);

    List<SoTheTinhGiaThanhDTO> getTheTinhGiaThanh(UUID companyID, String listMaterialGoodsID, String listCostSetID, UUID cPPeriodID, Integer typeMethod, Boolean isDependent);

    List<SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO> getSoTheoDoiTHCPTheoChiPhi(LocalDate fromDate, LocalDate toDate, String costSetID, String expenseItemID);

    List<TongHopCPTheoKMCPDTO> getTongHopCPTheoKMCP(UUID companyID, LocalDate fromDate, LocalDate toDate, Integer phienSoLamViec, String listAccountNumber, String listExpenseItem, Boolean dependent);

    List<Map<String, Object>> getSoChiPhiSXKD(UUID companyID, String accountNumber, LocalDate fromDate, LocalDate toDate, Integer phienSoLamViec, String listAccountNumber, String listCostSets);

    List<TheoDoiMaThongKeTheoTaiKhoanDTO> getSoTheoDoiMaThongKeTheoTaiKhoan(LocalDate fromDate, LocalDate toDate, String statisticsCodeID, String account, UUID companyID, Boolean dependent, Integer typeLedger);

    List<TheoDoiMaThongKeTheoKhoanMucChiPhiDTO> getSoTheoDoiMaThongKeTheoKhoanMucChiPhi(LocalDate fromDate, LocalDate toDate, String statisticsCodeID, String expenseItems, UUID companyID, Boolean dependent, Integer typeLedger);

    List<SoCongCuDungCuDTO> getSoCongCuDungCu(LocalDate fromDate,
                                                      LocalDate toDate,
                                                      String toolIDs, UUID companyID, Boolean isDependent
    );

    List<SoTheoDoiCCDCTaiNoiSuDungDTO> getSoTheoDoiCCDCTaiNoiSD(LocalDate fromDate, LocalDate toDate, String departmentIDs, Integer phienSoLamViec);

    List<SoTheoDoiLaiLoTheoHoaDonDTO> getSoTheoDoiLaiLoTheoHoaDon(LocalDate fromDate, LocalDate toDate, UUID uuid, Boolean dependent, Integer phienSoLamViec);

    List<SoTheoDoiCongNoPhaiThuTheoHoaDonDTO> getSoTheoDoiCongNoPhaiThuTheoHoaDon(LocalDate fromDate, LocalDate toDate,
                                                                                  String accountNumber, String currencyID,
                                                                                  String toString, Boolean typeShowCurrency, UUID companyID);

    List<SoTheoDoiThanhToanBangNgoaiTeDTO> getSoTheoDoiThanhToanBangNgoaiTe(LocalDate fromDate, LocalDate toDate, String currencyID, String accountNumber, String listAccountingObjects, Integer phienLamViec, UUID companyID, Boolean dependent);
}
