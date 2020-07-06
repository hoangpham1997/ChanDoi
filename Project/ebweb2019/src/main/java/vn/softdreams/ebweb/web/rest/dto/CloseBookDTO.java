package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.ViewVoucherNo;

import java.time.LocalDate;
import java.util.List;

public class CloseBookDTO {
    private LocalDate postedDate; // dùng để lấy chứng từ cần xử lý
    private LocalDate postedDateNew; // dùng khi chọn choseFuntion là chuyển ngày hạch toán
    private List<ViewVoucherNo> listDataChangeDiff;
    private List<ViewVoucherNo> listChangePostedDateDiff;
    private Integer choseFuntion;
    private List<OrgTreeDTO> lstBranch;

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getPostedDateNew() {
        return postedDateNew;
    }

    public void setPostedDateNew(LocalDate postedDateNew) {
        this.postedDateNew = postedDateNew;
    }

    public List<ViewVoucherNo> getListDataChangeDiff() {
        return listDataChangeDiff;
    }

    public void setListDataChangeDiff(List<ViewVoucherNo> listDataChangeDiff) {
        this.listDataChangeDiff = listDataChangeDiff;
    }

    public List<ViewVoucherNo> getListChangePostedDateDiff() {
        return listChangePostedDateDiff;
    }

    public void setListChangePostedDateDiff(List<ViewVoucherNo> listChangePostedDateDiff) {
        this.listChangePostedDateDiff = listChangePostedDateDiff;
    }

    public Integer getChoseFuntion() {
        return choseFuntion;
    }

    public void setChoseFuntion(Integer choseFuntion) {
        this.choseFuntion = choseFuntion;
    }

    public List<OrgTreeDTO> getLstBranch() {
        return lstBranch;
    }

    public void setLstBranch(List<OrgTreeDTO> lstBranch) {
        this.lstBranch = lstBranch;
    }
}
