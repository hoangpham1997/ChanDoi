package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.domain.TIAllocationPost;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TIAllocationDetailAllConvertDTO {
    private List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS; // màn thống kê
    private List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS; // lấy dữ liệu từ màn toolsdetail
    private List<TIAllocationAllocated> tiAllocationAllocateds; // màn phân bổ
    private List<TIAllocationPost> tiAllocationPosts; // màn hạch toán

    public TIAllocationDetailAllConvertDTO() {
    }

    public TIAllocationDetailAllConvertDTO(List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS, List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS, List<TIAllocationAllocated> tiAllocationAllocateds, List<TIAllocationPost> tiAllocationPosts) {
        this.tiAllocationDetailConvertDTOS = tiAllocationDetailConvertDTOS;
        this.toolsDetailsConvertDTOS = toolsDetailsConvertDTOS;
        this.tiAllocationAllocateds = tiAllocationAllocateds;
        this.tiAllocationPosts = tiAllocationPosts;
    }

    public List<TIAllocationDetailConvertDTO> getTiAllocationDetailConvertDTOS() {
        return tiAllocationDetailConvertDTOS;
    }

    public void setTiAllocationDetailConvertDTOS(List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS) {
        this.tiAllocationDetailConvertDTOS = tiAllocationDetailConvertDTOS;
    }

    public List<ToolsDetailsConvertDTO> getToolsDetailsConvertDTOS() {
        return toolsDetailsConvertDTOS;
    }

    public void setToolsDetailsConvertDTOS(List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS) {
        this.toolsDetailsConvertDTOS = toolsDetailsConvertDTOS;
    }

    public List<TIAllocationAllocated> getTiAllocationAllocateds() {
        return tiAllocationAllocateds;
    }

    public void setTiAllocationAllocateds(List<TIAllocationAllocated> tiAllocationAllocateds) {
        this.tiAllocationAllocateds = tiAllocationAllocateds;
    }

    public List<TIAllocationPost> getTiAllocationPosts() {
        return tiAllocationPosts;
    }

    public void setTiAllocationPosts(List<TIAllocationPost> tiAllocationPosts) {
        this.tiAllocationPosts = tiAllocationPosts;
    }
}
