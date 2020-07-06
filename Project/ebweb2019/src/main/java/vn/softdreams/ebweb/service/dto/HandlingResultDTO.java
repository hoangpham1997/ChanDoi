package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.ViewVoucherNo;

import java.util.List;
import java.util.UUID;

public class HandlingResultDTO {
    private Integer countTotalVouchers;
    private Integer countSuccessVouchers;
    private Integer countFailVouchers;
    private List<DetailDelFailCategoryDTO> listFailCategory;
    private List<ViewVoucherNo> listFail;
    private List<UUID> listIDFail;
    private List<UUID> setDeletedSuccess;
    private List<UUID> setDeletedFail;

    public List<DetailDelFailCategoryDTO> getListFailCategory() {
        return listFailCategory;
    }

    public void setListFailCategory(List<DetailDelFailCategoryDTO> listFailCategory) {
        this.listFailCategory = listFailCategory;
    }

    public List<UUID> getDeletedSuccess() {
        return setDeletedSuccess;
    }

    public void setDeletedSuccess(List<UUID> deleteSuccess) {
        this.setDeletedSuccess = deleteSuccess;
    }

    public List<UUID> getDeletedFail() {
        return setDeletedFail;
    }

    public void setDeletedFail(List<UUID> deleteFail) {
        this.setDeletedFail = deleteFail;
    }

    public Integer getCountTotalVouchers() {
        return countTotalVouchers;
    }

    public void setCountTotalVouchers(Integer countTotalVouchers) {
        this.countTotalVouchers = countTotalVouchers;
    }

    public Integer getCountSuccessVouchers() {
        return countSuccessVouchers;
    }

    public void setCountSuccessVouchers(Integer countSuccessVouchers) {
        this.countSuccessVouchers = countSuccessVouchers;
    }

    public Integer getCountFailVouchers() {
        return countFailVouchers;
    }

    public void setCountFailVouchers(Integer countFailVouchers) {
        this.countFailVouchers = countFailVouchers;
    }

    public List<ViewVoucherNo> getListFail() {
        return listFail;
    }

    public void setListFail(List<ViewVoucherNo> listFail) {
        this.listFail = listFail;
    }

    public List<UUID> getListIDFail() {
        return listIDFail;
    }

    public void setListIDFail(List<UUID> listIDFail) {
        this.listIDFail = listIDFail;
    }
}
