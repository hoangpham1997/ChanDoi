package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.service.dto.Report.BangCanDoiTaiKhoanDTO;

import java.util.List;

public class HomeDTO {
    private List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOList;
    private List<SucKhoeTaiChinhDoanhNghiepDTO> sucKhoeTaiChinhDoanhNghiepDTOS;
    private List<BieuDoTongHopDTO> bieuDoTongHopDTOList;
    private List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOList;
    private List<BieuDoDoanhThuChiPhiDTO> bieuDoNoPhaiThuTraList;

    public List<BangCanDoiTaiKhoanDTO> getBangCanDoiTaiKhoanDTOList() {
        return bangCanDoiTaiKhoanDTOList;
    }

    public void setBangCanDoiTaiKhoanDTOList(List<BangCanDoiTaiKhoanDTO> bangCanDoiTaiKhoanDTOList) {
        this.bangCanDoiTaiKhoanDTOList = bangCanDoiTaiKhoanDTOList;
    }

    public List<SucKhoeTaiChinhDoanhNghiepDTO> getSucKhoeTaiChinhDoanhNghiepDTOS() {
        return sucKhoeTaiChinhDoanhNghiepDTOS;
    }

    public void setSucKhoeTaiChinhDoanhNghiepDTOS(List<SucKhoeTaiChinhDoanhNghiepDTO> sucKhoeTaiChinhDoanhNghiepDTOS) {
        this.sucKhoeTaiChinhDoanhNghiepDTOS = sucKhoeTaiChinhDoanhNghiepDTOS;
    }

    public List<BieuDoTongHopDTO> getBieuDoTongHopDTOList() {
        return bieuDoTongHopDTOList;
    }

    public void setBieuDoTongHopDTOList(List<BieuDoTongHopDTO> bieuDoTongHopDTOList) {
        this.bieuDoTongHopDTOList = bieuDoTongHopDTOList;
    }

    public List<BieuDoDoanhThuChiPhiDTO> getBieuDoDoanhThuChiPhiDTOList() {
        return bieuDoDoanhThuChiPhiDTOList;
    }

    public void setBieuDoDoanhThuChiPhiDTOList(List<BieuDoDoanhThuChiPhiDTO> bieuDoDoanhThuChiPhiDTOList) {
        this.bieuDoDoanhThuChiPhiDTOList = bieuDoDoanhThuChiPhiDTOList;
    }

    public List<BieuDoDoanhThuChiPhiDTO> getBieuDoNoPhaiThuTraList() {
        return bieuDoNoPhaiThuTraList;
    }

    public void setBieuDoNoPhaiThuTraList(List<BieuDoDoanhThuChiPhiDTO> bieuDoNoPhaiThuTraList) {
        this.bieuDoNoPhaiThuTraList = bieuDoNoPhaiThuTraList;
    }
}
