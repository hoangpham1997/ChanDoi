package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CPAllocationDTO {
    private List<GiaThanhAllocationPoPupDTO> giaThanhAllocationPoPupDTOAll;
    private List<GiaThanhAllocationPoPupDTO> giaThanhAllocationPoPupDTOSums;

    public CPAllocationDTO() {
    }

    public List<GiaThanhAllocationPoPupDTO> getGiaThanhAllocationPoPupDTOAll() {
        return giaThanhAllocationPoPupDTOAll;
    }

    public void setGiaThanhAllocationPoPupDTOAll(List<GiaThanhAllocationPoPupDTO> giaThanhAllocationPoPupDTOAll) {
        this.giaThanhAllocationPoPupDTOAll = giaThanhAllocationPoPupDTOAll;
    }

    public List<GiaThanhAllocationPoPupDTO> getGiaThanhAllocationPoPupDTOSums() {
        return giaThanhAllocationPoPupDTOSums;
    }

    public void setGiaThanhAllocationPoPupDTOSums(List<GiaThanhAllocationPoPupDTO> giaThanhAllocationPoPupDTOSums) {
        this.giaThanhAllocationPoPupDTOSums = giaThanhAllocationPoPupDTOSums;
    }
}

