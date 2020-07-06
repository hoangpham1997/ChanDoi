// role, authorities
export const ROLE = {
    ROLE_MGT: 'ROLE_MGT',
    ROLE_PERMISSION: 'ROLE_PERMISSION',
    ROLE_REPORT: 'ROLE_REPORT',
    ROLE_ADMIN: 'ROLE_ADMIN',
    // 1.Tien Mat Ngan Hang
    TienMatNganHang: '01',

    // Phieu thu
    PhieuThu: '0101',
    PhieuThu_Xem: '010101',
    PhieuThu_Them: '010102',
    PhieuThu_Sua: '010103',
    PhieuThu_Xoa: '010104',
    PhieuThu_GhiSo: '010105',
    PhieuThu_In: '010106',
    PhieuThu_KetXuat: '010107',

    // Phieu chi
    PhieuChi: '0102',
    PhieuChi_Xem: '010201',
    PhieuChi_Them: '010202',
    PhieuChi_Sua: '010203',
    PhieuChi_Xoa: '010204',
    PhieuChi_GhiSo: '010205',
    PhieuChi_In: '010206',
    PhieuChi_KetXuat: '010207',

    // Bao No
    BaoNo: '0103',
    BaoNo_Xem: '010301',
    BaoNo_Them: '010302',
    BaoNo_Sua: '010303',
    BaoNo_Xoa: '010304',
    BaoNo_GhiSo: '010305',
    BaoNo_In: '010306',
    BaoNo_KetXuat: '010307',

    // Bao Co
    BaoCo: '0104',
    BaoCo_Xem: '010401',
    BaoCo_Them: '010402',
    BaoCo_Sua: '010403',
    BaoCo_Xoa: '010404',
    BaoCo_GhiSo: '010405',
    BaoCo_In: '010406',
    BaoCo_KetXuat: '010407',

    // The Tin Dung
    TheTinDung: '0105',
    TheTinDung_Xem: '010501',
    TheTinDung_Them: '010502',
    TheTinDung_Sua: '010503',
    TheTinDung_Xoa: '010504',
    TheTinDung_GhiSo: '010505',
    TheTinDung_In: '010506',
    TheTinDung_KetXuat: '010507',

    // Kiem Ke Quy
    KiemKeQuy: '0106',
    KiemKeQuy_Xem: '010601',
    KiemKeQuy_Them: '010602',
    KiemKeQuy_Sua: '010603',
    KiemKeQuy_Xoa: '010604',
    KiemKeQuy_In: '010606',
    KiemKeQuy_KetXuat: '010607',

    // 2.Mua Hang
    MuaHang: '02',

    // Đơn mua hàng
    DonMuaHang: '0201',
    DonMuaHang_XEM: '020101',
    DonMuaHang_THEM: '020102',
    DonMuaHang_SUA: '020103',
    DonMuaHang_XOA: '020104',
    DonMuaHang_IN: '020106',
    DonMuaHang_KET_XUAT: '020107',

    // Nhan Hoa Don
    NhanHoaDon: '0204',
    NhanHoaDon_Xem: '020401',
    NhanHoaDon_Sua: '020403',

    // Tra Tien Nha Cung Cap
    TraTienNhaCungCap: '0208',
    TraTienNhaCungCap_Xem: '020801',

    // Hàng mua trả lại
    HangMuaTraLai: '0206',
    HangMuaTraLai_Xem: '020601',
    HangMuaTraLai_Them: '020602',
    HangMuaTraLai_Sua: '020603',
    HangMuaTraLai_Xoa: '020604',
    HangMuaTraLai_GhiSo: '020605',
    HangMuaTraLai_In: '020606',
    HangMuaTraLai_KetXuat: '020607',

    // Hàng mua Qua Kho
    MuaHangQuaKho: '0202',
    MuaHangQuaKho_Xem: '020201',
    MuaHangQuaKho_Them: '020202',
    MuaHangQuaKho_Sua: '020203',
    MuaHangQuaKho_Xoa: '020204',
    MuaHangQuaKho_GhiSo: '020205',
    MuaHangQuaKho_In: '020206',
    MuaHangQuaKho_KetXuat: '020207',

    // Hàng mua không qua kho
    MuaHangKhongQuaKho: '0203',
    MuaHangKhongQuaKho_Xem: '020301',
    MuaHangKhongQuaKho_Them: '020302',
    MuaHangKhongQuaKho_Sua: '020303',
    MuaHangKhongQuaKho_Xoa: '020304',
    MuaHangKhongQuaKho_GhiSo: '020305',
    MuaHangKhongQuaKho_In: '020306',
    MuaHangKhongQuaKho_KetXuat: '020307',

    // Hàng mua giảm giá
    HangMuaGiamGia: '0207',
    HangMuaGiamGia_Xem: '020701',
    HangMuaGiamGia_Them: '020702',
    HangMuaGiamGia_Sua: '020703',
    HangMuaGiamGia_Xoa: '020704',
    HangMuaGiamGia_GhiSo: '020705',
    HangMuaGiamGia_In: '020706',
    HangMuaGiamGia_KetXuat: '020707',

    MuaDichVu_Xem: '020501',
    MuaDichVu_Them: '020502',
    MuaDichVu_Sua: '020503',
    MuaDichVu_Xoa: '020504',
    MuaDichVu_GhiSo: '020505',
    MuaDichVu_In: '020506',
    MuaDichVu_KetXuat: '020507',

    // 3.Ban Hang
    BanHang: '03',
    // Bao Gia
    BaoGia: '0301',
    BaoGia_Xem: '030101',
    BaoGia_Them: '030102',
    BaoGia_Sua: '030103',
    BaoGia_Xoa: '030104',
    BaoGia_In: '030106',
    BaoGia_KetXuat: '030107',

    // Don dat hang
    DonDatHang: '0302',
    DonDatHang_Xem: '030201',
    DonDatHang_Them: '030202',
    DonDatHang_Sua: '030203',
    DonDatHang_Xoa: '030204',
    DonDatHang_In: '030206',
    DonDatHang_KetXuat: '030207',

    // Ban Hang Chua Thu Tien
    ChungTuBanHang: '0303',
    ChungTuBanHang_Xem: '030301',
    ChungTuBanHang_Them: '030302',
    ChungTuBanHang_Sua: '030303',
    ChungTuBanHang_Xoa: '030304',
    ChungTuBanHang_GhiSo: '030305',
    ChungTuBanHang_In: '030306',
    ChungTuBanHang_KetXuat: '030307',

    // Xuat Hoa Don
    XuatHoaDon: '0305',
    XuatHoaDon_Xem: '030501',
    XuatHoaDon_Them: '030502',
    XuatHoaDon_Sua: '030503',
    XuatHoaDon_Xoa: '030504',
    XuatHoaDon_In: '030506',
    XuatHoaDon_KetXuat: '030507',
    XuatHoaDon_Upload: '030508',

    // Hang Ban Tra Lai
    HangBanTraLai: '0306',
    HangBanTraLai_Xem: '030601',
    HangBanTraLai_Them: '030602',
    HangBanTraLai_Sua: '030603',
    HangBanTraLai_Xoa: '030604',
    HangBanTraLai_GhiSo: '030605',
    HangBanTraLai_In: '030606',
    HangBanTraLai_KetXuat: '030607',

    // Hang Ban Giam Gia
    HangBanGiamGia: '0307',
    HangBanGiamGia_Xem: '030701',
    HangBanGiamGia_Them: '030702',
    HangBanGiamGia_Sua: '030703',
    HangBanGiamGia_Xoa: '030704',
    HangBanGiamGia_GhiSo: '030705',
    HangBanGiamGia_In: '030706',
    HangBanGiamGia_KetXuat: '030707',

    // Thu Tien Khach Hang
    ThuTienKhachHang: '0308',
    ThuTienKhachHang_Xem: '030801',

    // 4.Kho
    Kho: '04',

    // Nhập kho
    NhapKho: '0401',
    NhapKho_XEM: '040101',
    NhapKho_THEM: '040102',
    NhapKho_SUA: '040103',
    NhapKho_XOA: '040104',
    NhapKho_GHI_SO: '040105',
    NhapKho_IN: '040106',
    NhapKho_KET_XUAT: '040107',

    // Tinh Gia Xuat Kho
    TinhGiaXuatKho: '0408',
    TinhGiaXuatKho_Xem: '040801',

    // Xuất kho
    XuatKho: '0402',
    XuatKho_XEM: '040201',
    XuatKho_THEM: '040202',
    XuatKho_SUA: '040203',
    XuatKho_XOA: '040204',
    XuatKho_GHI_SO: '040205',
    XuatKho_IN: '040206',
    XuatKho_KET_XUAT: '040207',

    // Chuyển kho
    ChuyenKho: '0403',
    ChuyenKho_XEM: '040301',
    ChuyenKho_THEM: '040302',
    ChuyenKho_SUA: '040303',
    ChuyenKho_XOA: '040304',
    ChuyenKho_GHI_SO: '040305',
    ChuyenKho_IN: '040306',
    ChuyenKho_KET_XUAT: '040307',

    // 5.Gia Thanh
    GiaThanh: '05',

    // Dinh muc gia thanh thanh pham
    DinhMucGiaThanhThanhPham: '0502',
    DinhMucGiaThanhThanhPham_Xem: '050201',
    DinhMucGiaThanhThanhPham_Sua: '050203',

    // Dinh muc phan bo chi phi
    DinhMucPhanBoChiPhi: '0503',
    DinhMucPhanBoChiPhi_Xem: '050301',
    DinhMucPhanBoChiPhi_Sua: '050303',

    // Chi phi do dang dau ky
    ChiPhiDoDangDauKy: '0504',
    ChiPhiDoDangDauKy_Xem: '050401',
    ChiPhiDoDangDauKy_Sua: '050403',

    // Phuong phap gian don
    PhuongPhapGianDon: '0505',
    PhuongPhapGianDon_Xem: '050501',
    PhuongPhapGianDon_Them: '050502',
    PhuongPhapGianDon_Sua: '050503',
    PhuongPhapGianDon_Xoa: '050504',
    PhuongPhapGianDon_KetXuat: '050507',

    // Phuong phap he so
    PhuongPhapHeSo: '0506',
    PhuongPhapHeSo_Xem: '050601',
    PhuongPhapHeSo_Them: '050602',
    PhuongPhapHeSo_Sua: '050603',
    PhuongPhapHeSo_Xoa: '050604',
    PhuongPhapHeSo_KetXuat: '050607',

    // Phuong phap ty le
    PhuongPhapTyLe: '0507',
    PhuongPhapTyLe_Xem: '050701',
    PhuongPhapTyLe_Them: '050702',
    PhuongPhapTyLe_Sua: '050703',
    PhuongPhapTyLe_Xoa: '050704',
    PhuongPhapTyLe_KetXuat: '050707',

    // Gia thanh theo cong trinh, vu viec
    GiaThanhTheoCTVV: '0508',
    GiaThanhTheoCTVV_Xem: '050801',
    GiaThanhTheoCTVV_Them: '050802',
    GiaThanhTheoCTVV_Sua: '050803',
    GiaThanhTheoCTVV_Xoa: '050804',
    GiaThanhTheoCTVV_KetXuat: '050807',

    // Gia thanh theo don hang
    GiaThanhTheoDonHang: '0509',
    GiaThanhTheoDonHang_Xem: '050901',
    GiaThanhTheoDonHang_Them: '050902',
    GiaThanhTheoDonHang_Sua: '050903',
    GiaThanhTheoDonHang_Xoa: '050904',
    GiaThanhTheoDonHang_KetXuat: '050907',

    // Gia thanh theo hop dong
    GiaThanhTheoHopDong: '0510',
    GiaThanhTheoHopDong_Xem: '051001',

    // Ket chuyen chi phi
    KetChuyenChiPhi: '0511',
    KetChuyenChiPhi_Xem: '051101',
    KetChuyenChiPhi_Them: '051102',
    KetChuyenChiPhi_Sua: '051103',
    KetChuyenChiPhi_Xoa: '051104',
    KetChuyenChiPhi_GhiSo: '051105',
    KetChuyenChiPhi_In: '051106',
    KetChuyenChiPhi_KetXuat: '051107',

    // 6. Tong Hop
    TongHop: '06',
    // Chung Tu Nghiep Vu Khac
    ChungTuNghiepVuKhac: '0601',
    ChungTuNghiepVuKhac_Xem: '060101',
    ChungTuNghiepVuKhac_Them: '060102',
    ChungTuNghiepVuKhac_Sua: '060103',
    ChungTuNghiepVuKhac_Xoa: '060104',
    ChungTuNghiepVuKhac_GhiSo: '060105',
    ChungTuNghiepVuKhac_In: '060106',
    ChungTuNghiepVuKhac_KetXuat: '060107',

    // Kết chuyển lãi lỗ
    KetChuyenLaiLo: '0602',
    KetChuyenLaiLo_Xem: '060201',
    KetChuyenLaiLo_Them: '060202',
    KetChuyenLaiLo_Sua: '060203',
    KetChuyenLaiLo_Xoa: '060204',
    KetChuyenLaiLo_GhiSo: '060205',
    KetChuyenLaiLo_In: '060206',
    KetChuyenLaiLo_KetXuat: '060207',

    // Khóa sổ kỳ kế toán
    KhoaSoKyKeToan: '0609',
    KhoaSo: '060901',

    // Bỏ khóa sổ kỳ kế toán
    BoKhoaSoKyKeToan: '0610',
    BoKhoaSo: '061001',

    // 7. Quản lý hóa đơn
    QLHD: '07',

    // Khởi tạo mẫu hóa đơn
    KTHD: '0701',
    KTHD_XEM: '070101',
    KTHD_THEM: '070102',
    KTHD_SUA: '070103',
    KTHD_XOA: '070104',
    KTHD_KET_XUAT: '070107',

    // Đăng ký sử dụng hóa đơn
    DKSD: '0702',
    DKSD_XEM: '070201',
    DKSD_THEM: '070202',
    DKSD_SUA: '070203',
    DKSD_XOA: '070204',
    DKSD_KET_XUAT: '070207',

    // Thông báo phát hành hóa đơn
    TBPH: '0703',
    TBPH_XEM: '070301',
    TBPH_THEM: '070302',
    TBPH_SUA: '070303',
    TBPH_XOA: '070304',
    TBPH_KET_XUAT: '070307',
    TBPH_IN: '070306',

    // 8. Hóa đơn điện tử
    HDDT: '08',

    // Danh sách hóa đơn
    DSHD: '0801',
    DSHD_XEM: '080101',
    DSHD_PHAT_HANH: '080110',
    DSHD_THAY_THE_HOA_DON: '080111',
    DSHD_DIEU_CHINH_HOA_DON: '080112',
    DSHD_HUY_BO_HOA_DON: '080113',
    DSHD_GUI_EMAIL: '080114',
    DSHD_TAO_CHO: '080115',

    // Danh sách hóa đơn chờ ký
    DSHD_CHO_KY: '0802',
    DSHD_CHO_KY_XEM: '080201',
    DSHD_CHO_KY_PHAT_HANH: '080210',

    // Hóa đơn thay thế
    HD_THAY_THE: '0803',
    HD_THAY_THE_XEM: '080301',

    // Hóa đơn điều chỉnh
    HD_DIEU_CHINH: '0804',
    HD_DIEU_CHINH_XEM: '080401',

    // Hóa đơn hủy
    HD_HUY: '0805',
    HD_HUY_XEM: '080501',

    // Chuyển đổi hóa đơn
    CDHD: '0806',
    CDHD_XEM: '080601',
    CDHD_CHUNG_MINH_NGUON_GOC: '080616',
    CDHD_LUU_TRU: '080617',

    // Kết nối hóa đơn
    KNHD: '0807',
    KNHD_KET_NOI_HD: '080718',

    HeThong: '14',
    // Ngay hach toan
    NgayHachToan_Xem: '140101',
    NgayHachToan_Sua: '140103',

    // TuyChon
    TuyChon_Xem: '140201',
    TuyChon_Sua: '140203',

    // 15.Danh Muc
    DanhMuc: '15',

    // Co cau to chuc
    CoCauToChuc: '1501',
    CoCauToChuc_Xem: '150101',
    CoCauToChuc_Them: '150102',
    CoCauToChuc_Sua: '150103',
    CoCauToChuc_Xoa: '150104',

    // He thong tai khoan
    HeThongTaiKhoan: '1502',
    HeThongTaiKhoan_Xem: '150201',
    HeThongTaiKhoan_Them: '150202',
    HeThongTaiKhoan_Sua: '150203',
    HeThongTaiKhoan_Xoa: '150204',

    // Tai khoan ket chuyen
    TaiKhoanKetChuyen: '1503',
    TaiKhoanKetChuyen_Xem: '150301',
    TaiKhoanKetChuyen_Them: '150302',
    TaiKhoanKetChuyen_Sua: '150303',
    TaiKhoanKetChuyen_Xoa: '150304',

    // Tai khoan ngam dinh
    TaiKhoanNgamDinh: '1504',
    TaiKhoanNgamDinh_Xem: '150401',
    TaiKhoanNgamDinh_Sua: '150403',

    // Danh Muc Khach Hang
    DanhMucKhachHang: '1506',
    DanhMucKhachHang_Xem: '150601',
    DanhMucKhachHang_Them: '150602',
    DanhMucKhachHang_Sua: '150603',
    DanhMucKhachHang_Xoa: '150604',

    // Danh Muc Khach Hang
    DanhMucNhaCungCap: '1507',
    DanhMucNhaCungCap_Xem: '150701',
    DanhMucNhaCungCap_Them: '150702',
    DanhMucNhaCungCap_Sua: '150703',
    DanhMucNhaCungCap_Xoa: '150704',

    // Danh Muc Khach Hang
    DanhMucNhanVien: '1508',
    DanhMucNhanVien_Xem: '150801',
    DanhMucNhanVien_Them: '150802',
    DanhMucNhanVien_Sua: '150803',
    DanhMucNhanVien_Xoa: '150804',

    // Danh Muc Khach Hang va Nha Cung Cap
    DanhMucKhachHangVaNhaCungCap: '1509',
    DanhMucKhachHangVaNhaCungCap_Xem: '150901',
    DanhMucKhachHangVaNhaCungCap_Them: '150902',
    DanhMucKhachHangVaNhaCungCap_Sua: '150903',
    DanhMucKhachHangVaNhaCungCap_Xoa: '150904',

    // Danh Muc Kho
    DanhMucKho: '1511',
    DanhMucKho_Xem: '151101',
    DanhMucKho_Them: '151102',
    DanhMucKho_Sua: '151103',
    DanhMucKho_Xoa: '151104',

    // Danh Muc Don Vi Tinh
    DanhMucDonViTinh: '1512',
    DanhMucDonViTinh_Xem: '151201',
    DanhMucDonViTinh_Them: '151202',
    DanhMucDonViTinh_Sua: '151203',
    DanhMucDonViTinh_Xoa: '151204',

    // Danh Muc Loai Vat Tu Hang Hoa
    DanhMucLoaiVatTuHangHoa: '1513',
    DanhMucLoaiVatTuHangHoa_Xem: '151301',
    DanhMucLoaiVatTuHangHoa_Them: '151302',
    DanhMucLoaiVatTuHangHoa_Sua: '151303',
    DanhMucLoaiVatTuHangHoa_Xoa: '151304',

    // Danh Muc Tai Khoan Ngan Hang
    DanhMucTaiKhoanNganHang: '1521',
    DanhMucTaiKhoanNganHang_Xem: '152101',
    DanhMucTaiKhoanNganHang_Them: '152102',
    DanhMucTaiKhoanNganHang_Sua: '152103',
    DanhMucTaiKhoanNganHang_Xoa: '152104',

    // Danh Muc dinh khoan tu dong
    DanhMucDinhKhoanTuDong: '1505',
    DanhMucDinhKhoanTuDong_Xem: '150501',
    DanhMucDinhKhoanTuDong_Them: '150502',
    DanhMucDinhKhoanTuDong_Sua: '150503',
    DanhMucDinhKhoanTuDong_Xoa: '150504',

    // Danh Muc vat tu hang hoa
    DanhMucVatTuHangHoa: '1510',
    DanhMucVatTuHangHoa_Xem: '151001',
    DanhMucVatTuHangHoa_Them: '151002',
    DanhMucVatTuHangHoa_Sua: '151003',
    DanhMucVatTuHangHoa_Xoa: '151004',

    // Danh Muc ngan hang
    DanhMucNganHang: '1520',
    DanhMucNganHang_Xem: '152001',
    DanhMucNganHang_Them: '152002',
    DanhMucNganHang_Sua: '152003',
    DanhMucNganHang_Xoa: '152004',

    // Danh Muc the tin dung
    DanhMucTheTinDung: '1522',
    DanhMucTheTinDung_Xem: '152201',
    DanhMucTheTinDung_Them: '152202',
    DanhMucTheTinDung_Sua: '152203',
    DanhMucTheTinDung_Xoa: '152204',

    // Danh Muc Loai Tien
    DanhMucLoaiTien: '1530',
    DanhMucLoaiTien_Xem: '153001',
    DanhMucLoaiTien_Them: '153002',
    DanhMucLoaiTien_Sua: '153003',
    DanhMucLoaiTien_Xoa: '153004',

    // Danh Muc Thu Chi
    DanhMucThuChi: '1529',
    DanhMucThuChi_Xem: '152901',
    DanhMucThuChi_Them: '152902',
    DanhMucThuChi_Sua: '152903',
    DanhMucThuChi_Xoa: '152904',

    // Danh Muc Phuong Thuc Van Chuyen
    DanhMucPhuongThucVanChuyen: '1532',
    DanhMucPhuongThucVanChuyen_Xem: '153201',
    DanhMucPhuongThucVanChuyen_Them: '153202',
    DanhMucPhuongThucVanChuyen_Sua: '153203',
    DanhMucPhuongThucVanChuyen_Xoa: '153204',

    // Danh Muc Dieu khoan thanh toan
    DanhMucDieuKhoanThanhToan: '1531',
    DanhMucDieuKhoanThanhToan_Xem: '153101',
    DanhMucDieuKhoanThanhToan_Them: '153102',
    DanhMucDieuKhoanThanhToan_Sua: '153103',
    DanhMucDieuKhoanThanhToan_Xoa: '153104',

    // Danh Muc Ma Thong Ke
    DanhMucMaThongKe: '1528',
    DanhMucMaThongKe_Xem: '152801',
    DanhMucMaThongKe_Them: '152802',
    DanhMucMaThongKe_Sua: '152803',
    DanhMucMaThongKe_Xoa: '152804',

    // Khoan Muc Chi Phi
    DanhMucKhoanMucChiPhi: '1527',
    DanhMucKhoanMucChiPhi_Xem: '152701',
    DanhMucKhoanMucChiPhi_Them: '152702',
    DanhMucKhoanMucChiPhi_Sua: '152703',
    DanhMucKhoanMucChiPhi_Xoa: '152704',

    // Danh Muc nhom HHDV chiu theu TTDB
    DanhMucHHDVChiuThueTTDB: '1517',
    DanhMucHHDVChiuThueTTDB_Xem: '151701',
    DanhMucHHDVChiuThueTTDB_Them: '151702',
    DanhMucHHDVChiuThueTTDB_Sua: '151703',
    DanhMucHHDVChiuThueTTDB_Xoa: '151704',

    // Dinh muc nguyen vat lieu
    DinhMucNguyenVatLieu: '1519',
    DinhMucNguyenVatLieu_Xem: '151901',
    DinhMucNguyenVatLieu_Them: '151902',
    DinhMucNguyenVatLieu_Sua: '151903',
    DinhMucNguyenVatLieu_Xoa: '151904',

    // Danh Muc Doi Tuong Tap Hop Chi Phi
    DanhMucDTTHCP: '1526',
    DanhMucDTTHCP_Xem: '152601',
    DanhMucDTTHCP_Them: '152602',
    DanhMucDTTHCP_Sua: '152603',
    DanhMucDTTHCP_Xoa: '152604',

    // 16. Tiện ích
    TienIch: '16',

    // Số dư đầu kỳ
    SoDuDauKy: '1601',
    SoDuDauKy_Xem: '160101',
    SoDuDauKy_Sua: '160103',
    SoDuDauKy_KetXuat: '160107',
    SoDuDauKy_NhapTuExcel: '160108',

    // Dữ liệu
    DuLieu: '16',

    // Số dư đầu kỳ
    QLDuLieu: '1602',
    QLDuLieu_Xem: '160201',
    QLDuLieu_Xoa: '160204',
    QLDuLieu_SaoLuuDuLieu: '160219',
    QLDuLieu_KhoiPhucDuLieu: '160220',

    // phân bổ chi phí trả trước
    PhanBoChiPhiTRaTruoc: '0605',
    PhanBoChiPhiTRaTruoc_Xem: '060501',
    PhanBoChiPhiTRaTruoc_Them: '060502',
    PhanBoChiPhiTRaTruoc_Sua: '060503',
    PhanBoChiPhiTRaTruoc_Xoa: '060504',
    PhanBoChiPhiTRaTruoc_GhiSo: '060505',
    PhanBoChiPhiTRaTruoc_In: '060506',
    PhanBoChiPhiTRaTruoc_KetXuat: '060507',

    // Chi phí trả trước
    ChiPhiTRaTruoc: '0604',
    ChiPhiTRaTruoc_Xem: '060401',
    ChiPhiTRaTruoc_Them: '060402',
    ChiPhiTRaTruoc_Sua: '060403',
    ChiPhiTRaTruoc_Xoa: '060404',

    // Báo cáo
    BaoCao: '17',
    BangCanDoiSoPhatSinh: '1701',
    BangCanDoiSoPhatSinh_Xem: '170101',
    BangCanDoiSoPhatSinh_KetXuat: '170107',
    BangCanDoiKeToan: '1702',
    BangCanDoiKeToan_Xem: '170201',
    BangCanDoiKeToan_KetXuat: '170207',
    BaoCaoKetQuaHoatDongKinhDoanh: '1703',
    BaoCaoKetQuaHoatDongKinhDoanh_Xem: '170301',
    BaoCaoKetQuaHoatDongKinhDoanh_KetXuat: '170307',
    BaoCaoLuuChuyenTienTeTT: '1704',
    BaoCaoLuuChuyenTienTeTT_Xem: '170401',
    BaoCaoLuuChuyenTienTeTT_KetXuat: '170407',
    BaoCaoLuuChuyenTienTeGT: '1705',
    BaoCaoLuuChuyenTienTeGT_Xem: '170501',
    BaoCaoLuuChuyenTienTeGT_KetXuat: '170507',
    BanThuyetMinhBaoCaoTaiChinh: '1706',
    BanThuyetMinhBaoCaoTaiChinh_Xem: '170601',
    BanThuyetMinhBaoCaoTaiChinh_KetXuat: '170607',
    SoQuyTienMat: '1707',
    SoQuyTienMat_Xem: '170701',
    SoQuyTienMat_KetXuat: '170707',
    SoKeToanChiTietQuyTienMat: '1708',
    SoKeToanChiTietQuyTienMat_Xem: '170801',
    SoKeToanChiTietQuyTienMat_KetXuat: '170807',
    SoTienGuiNganHang: '1709',
    SoTienGuiNganHang_Xem: '170901',
    SoTienGuiNganHang_KetXuat: '170907',
    BangKeSoDuNganHang: '1710',
    BangKeSoDuNganHang_Xem: '171001',
    BangKeSoDuNganHang_KetXuat: '171007',
    SoChiTietVatLieu: '1719',
    SoChiTietVatLieu_Xem: '171901',
    SoChiTietVatLieu_KetXuat: '171907',
    BangTongHopChiTietVatLieu: '1720',
    BangTongHopChiTietVatLieu_Xem: '172001',
    BangTongHopChiTietVatLieu_KetXuat: '172007',
    TheKho: '1721',
    TheKho_Xem: '172101',
    TheKho_KetXuat: '172107',
    TongHopTonKho: '1722',
    TongHopTonKho_Xem: '172201',
    TongHopTonKho_KetXuat: '172207',

    SoTheoDoiMaThongKeTheoTaiKhoan: '1736',
    SoTheoDoiMaThongKeTheoTaiKhoan_Xem: '173601',
    SoTheoDoiMaThongKeTheoTaiKhoan_KetXuat: '173607',

    SoTheoDoiMaThongKeTheoKhoanMucChiPhi: '1737',
    SoTheoDoiMaThongKeTheoKhoanMucChiPhi_Xem: '173701',
    SoTheoDoiMaThongKeTheoKhoanMucChiPhi_KetXuat: '173707',

    BaoCaoSoNhatKyChung_Xem: '172301',
    BaoCaoSoNhatKyChung_KetXuat: '172307',

    BaoCaoSoCaiNhatKyChung_Xem: '172401',
    BaoCaoSoCaiNhatKyChung_KetXuat: '172407',

    BaoCaoSoChiTietCacTaiKhoan_Xem: '172701',
    BaoCaoSoChiTietCacTaiKhoan_KetXuat: '172707',

    BaoCaoSoTheoDoiThanhToanBangNgoaiTe_Xem: '174001',
    BaoCaoSoTheoDoiThanhToanBangNgoaiTe_KetXuat: '174007',

    SoNhatKyThuTien_Xem: '172801',
    SoNhatKyThuTien_KetXuat: '172807',
    SoNhatKyChiTien_Xem: '172901',
    SoNhatKyChiTien_KetXuat: '172907',

    BangKeHDChungTuVTHHMuaVao_Xem: '173001',
    BangKeHDChungTuVTHHMuaVao_KetXuat: '173007',
    BangKeHDChungTuVTHHMuaVaoQuanTri_Xem: '173101',
    BangKeHDChungTuVTHHMuaVaoQuanTri_KetXuat: '173107',
    BangKeHDChungTuVTHHBanRa_Xem: '173201',
    BangKeHDChungTuVTHHBanRa_KetXuat: '173207',
    BangKeHDChungTuVTHHBanRaQuanTri_Xem: '173301',
    BangKeHDChungTuVTHHBanRaQuanTri_KetXuat: '173307',

    SoChiTietMuaHang_Xem: '171101',
    SoChiTietMuaHang_KetXuat: '171107',

    SoNhatKyMuaHang_Xem: '171201',
    SoNhatKyMuaHang_KetXuat: '171207',

    SoChiTietBanHang_Xem: '171501',
    SoChiTietBanHang_KetXuat: '171507',

    SoNhatKyBanHang_Xem: '171601',
    SoNhatKyBanHang_KetXuat: '171607',

    TongHopCongNoPhaiTra_Xem: '171401',
    TongHopCongNoPhaiTra_KetXuat: '171407',
    ChiTietCongNoPhaiTra_Xem: '171301',
    ChiTietCongNoPhaiTra_KetXuat: '171307',

    TongHopCongNoPhaiThu_Xem: '171801',
    TongHopCongNoPhaiThu_KetXuat: '171807',
    ChiTietCongNoPhaiThu_Xem: '171701',
    ChiTietCongNoPhaiThu_KetXuat: '171707',

    BangPhanBoChiPhiTraTruoc_Xem: '172601',
    BangPhanBoChiPhiTraTruoc_KetXuat: '172607',

    SoChiPhiSanXuatKinhDoanh_Xem: '174701',
    SoChiPhiSanXuatKinhDoanh_KetXuat: '174707',

    TongHopChiPhiTheoKhoanMucChiPhi_Xem: '174801',
    TongHopChiPhiTheoKhoanMucChiPhi_KetXuat: '174807',

    TheTinhGiaThanh_Xem: '174901',
    TheTinhGiaThanh_KetXuat: '174907',

    SoTheoDoiDoiTuongTHCPTheoTaiKhoan_Xem: '173801',
    SoTheoDoiDoiTuongTHCPTheoTaiKhoan_KetXuat: '173807',

    SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi_Xem: '173901',
    SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi_KetXuat: '173907',

    SoCongCuDungCu_Xem: '173401',
    SoCongCuDungCu_KetXuat: '173407',

    SoTheoDoiCCDCTaiNoiSD_Xem: '173501',
    SoTheoDoiCCDCTaiNoiSD_KetXuat: '173507',

    SoTheoDoiLaiLoTheoHoaDon_Xem: '174201',
    SoTheoDoiLaiLoTheoHoaDon_KetXuat: '174207',

    SoTheoDoiCongNoPhaiThuTheoHoaDon_Xem: '174301',
    SoTheoDoiCongNoPhaiThuTheoHoaDon_KetXuat: '174307'
};
