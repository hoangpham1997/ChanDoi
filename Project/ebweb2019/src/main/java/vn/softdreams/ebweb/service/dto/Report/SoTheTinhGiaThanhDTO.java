package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheTinhGiaThanhDTO {
    private Integer stt;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID quantumID;
    private String quantumCode;
    private String quantumName;
    private String target;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountNLVL;
    private BigDecimal totalAmountNCTT;
    private BigDecimal totalAmountCPSDMTC;
    private BigDecimal totalAmountCPSXC;
    private String totalAmountToString;
    private String totalAmountNLVLToString;
    private String totalAmountNCTTToString;
    private String totalAmountCPSDMTCToString;
    private String totalAmountCPSXCToString;
    private Integer row;

    public SoTheTinhGiaThanhDTO() {
    }

    public SoTheTinhGiaThanhDTO(UUID costSetID, String costSetCode, String costSetName, UUID quantumID, String quantumCode, String quantumName, String target, BigDecimal totalAmount, BigDecimal totalAmountNLVL, BigDecimal totalAmountNCTT, BigDecimal totalAmountCPSDMTC, BigDecimal totalAmountCPSXC, Integer row) {
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.quantumID = quantumID;
        this.quantumCode = quantumCode;
        this.quantumName = quantumName;
        this.target = target;
        this.totalAmount = totalAmount;
        this.totalAmountNLVL = totalAmountNLVL;
        this.totalAmountNCTT = totalAmountNCTT;
        this.totalAmountCPSDMTC = totalAmountCPSDMTC;
        this.totalAmountCPSXC = totalAmountCPSXC;
        this.row = row;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getQuantumID() {
        return quantumID;
    }

    public void setQuantumID(UUID quantumID) {
        this.quantumID = quantumID;
    }

    public String getQuantumCode() {
        return quantumCode;
    }

    public void setQuantumCode(String quantumCode) {
        this.quantumCode = quantumCode;
    }

    public String getQuantumName() {
        return quantumName;
    }

    public void setQuantumName(String quantumName) {
        this.quantumName = quantumName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountNLVL() {
        return totalAmountNLVL;
    }

    public void setTotalAmountNLVL(BigDecimal totalAmountNLVL) {
        this.totalAmountNLVL = totalAmountNLVL;
    }

    public BigDecimal getTotalAmountNCTT() {
        return totalAmountNCTT;
    }

    public void setTotalAmountNCTT(BigDecimal totalAmountNCTT) {
        this.totalAmountNCTT = totalAmountNCTT;
    }

    public BigDecimal getTotalAmountCPSDMTC() {
        return totalAmountCPSDMTC;
    }

    public void setTotalAmountCPSDMTC(BigDecimal totalAmountCPSDMTC) {
        this.totalAmountCPSDMTC = totalAmountCPSDMTC;
    }

    public BigDecimal getTotalAmountCPSXC() {
        return totalAmountCPSXC;
    }

    public void setTotalAmountCPSXC(BigDecimal totalAmountCPSXC) {
        this.totalAmountCPSXC = totalAmountCPSXC;
    }

    public String getTotalAmountToString() {
        return totalAmountToString;
    }

    public void setTotalAmountToString(String totalAmountToString) {
        this.totalAmountToString = totalAmountToString;
    }

    public String getTotalAmountNLVLToString() {
        return totalAmountNLVLToString;
    }

    public void setTotalAmountNLVLToString(String totalAmountNLVLToString) {
        this.totalAmountNLVLToString = totalAmountNLVLToString;
    }

    public String getTotalAmountNCTTToString() {
        return totalAmountNCTTToString;
    }

    public void setTotalAmountNCTTToString(String totalAmountNCTTToString) {
        this.totalAmountNCTTToString = totalAmountNCTTToString;
    }

    public String getTotalAmountCPSDMTCToString() {
        return totalAmountCPSDMTCToString;
    }

    public void setTotalAmountCPSDMTCToString(String totalAmountCPSDMTCToString) {
        this.totalAmountCPSDMTCToString = totalAmountCPSDMTCToString;
    }

    public String getTotalAmountCPSXCToString() {
        return totalAmountCPSXCToString;
    }

    public void setTotalAmountCPSXCToString(String totalAmountCPSXCToString) {
        this.totalAmountCPSXCToString = totalAmountCPSXCToString;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }
}

