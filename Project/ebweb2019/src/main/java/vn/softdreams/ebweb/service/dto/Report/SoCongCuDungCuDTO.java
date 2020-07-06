package vn.softdreams.ebweb.service.dto.Report;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoCongCuDungCuDTO {
    private UUID toolsID; // ma CCDC
    private String toolsCode; // ma CCDC
    private String toolsName; // ten CCDC
    private LocalDate incrementDate; // ngay ghi tang
    private LocalDate postedDate; // ngay ghi giam
    private BigDecimal amount; // tong gia tri
    private Integer allocationTimes; // tong so ky phan bo
    private BigDecimal allocatedAmount; // gia tri phan bo hang ky
    private Integer decrementAllocationTime; // so ky da phan bo
    private Integer remainingAllocationTime; // so ky phan bo con lai
    private BigDecimal decrementAmount; // gia tri da phan bo
    private BigDecimal remainingAmount; // gia tri con lai
    private Integer tongSoKyPB;
    private BigDecimal gtPBHangKy;
    private Integer soKyDaPB;
    private Integer soKyDaPBConLai;
    private BigDecimal gtDaPB;
    private BigDecimal gtConLai;
    private String amountString;
    private String incrementDateString;
    private String postedDateString;
    private String allocationTimesString;
    private String allocatedAmountString;
    private String decrementAllocationTimeString;
    private String remainingAllocationTimeString;
    private String decrementAmountString;
    private String remainingAmountString;
    private String tongSoKyPBString;
    private String gtPBHangKyString;
    private String soKyDaPBString;
    private String soKyDaPBConLaiString;
    private String gtDaPBString;
    private String gtConLaiString;

    public SoCongCuDungCuDTO() {
    }

    public SoCongCuDungCuDTO(UUID toolsID, String toolsCode, String toolsName, LocalDate incrementDate, LocalDate postedDate, BigDecimal amount, Integer allocationTimes, BigDecimal allocatedAmount, Integer decrementAllocationTime, Integer remainingAllocationTime, BigDecimal decrementAmount, BigDecimal remainingAmount) {
        this.toolsID = toolsID;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.incrementDate = incrementDate;
        this.postedDate = postedDate;
        this.amount = amount;
        this.allocationTimes = allocationTimes;
        this.allocatedAmount = allocatedAmount;
        this.decrementAllocationTime = decrementAllocationTime;
        this.remainingAllocationTime = remainingAllocationTime;
        this.decrementAmount = decrementAmount;
        this.remainingAmount = remainingAmount;
    }

    public LocalDate getIncrementDate() {
        return incrementDate;
    }

    public void setIncrementDate(LocalDate incrementDate) {
        this.incrementDate = incrementDate;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAllocationTimes() {
        return allocationTimes;
    }

    public void setAllocationTimes(Integer allocationTimes) {
        this.allocationTimes = allocationTimes;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Integer getDecrementAllocationTime() {
        return decrementAllocationTime;
    }

    public void setDecrementAllocationTime(Integer decrementAllocationTime) {
        this.decrementAllocationTime = decrementAllocationTime;
    }

    public Integer getRemainingAllocationTime() {
        return remainingAllocationTime;
    }

    public void setRemainingAllocationTime(Integer remainingAllocationTime) {
        this.remainingAllocationTime = remainingAllocationTime;
    }

    public BigDecimal getDecrementAmount() {
        return decrementAmount;
    }

    public void setDecrementAmount(BigDecimal decrementAmount) {
        this.decrementAmount = decrementAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public Integer getTongSoKyPB() {
        return tongSoKyPB;
    }

    public void setTongSoKyPB(Integer tongSoKyPB) {
        this.tongSoKyPB = tongSoKyPB;
    }

    public BigDecimal getGtPBHangKy() {
        return gtPBHangKy;
    }

    public void setGtPBHangKy(BigDecimal gtPBHangKy) {
        this.gtPBHangKy = gtPBHangKy;
    }

    public Integer getSoKyDaPB() {
        return soKyDaPB;
    }

    public void setSoKyDaPB(Integer soKyDaPB) {
        this.soKyDaPB = soKyDaPB;
    }

    public Integer getSoKyDaPBConLai() {
        return soKyDaPBConLai;
    }

    public void setSoKyDaPBConLai(Integer soKyDaPBConLai) {
        this.soKyDaPBConLai = soKyDaPBConLai;
    }

    public BigDecimal getGtDaPB() {
        return gtDaPB;
    }

    public void setGtDaPB(BigDecimal gtDaPB) {
        this.gtDaPB = gtDaPB;
    }

    public BigDecimal getGtConLai() {
        return gtConLai;
    }

    public void setGtConLai(BigDecimal gtConLai) {
        this.gtConLai = gtConLai;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getIncrementDateString() {
        return incrementDateString;
    }

    public void setIncrementDateString(String incrementDateString) {
        this.incrementDateString = incrementDateString;
    }

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(String postedDateString) {
        this.postedDateString = postedDateString;
    }

    public String getAllocationTimesString() {
        return allocationTimesString;
    }

    public void setAllocationTimesString(String allocationTimesString) {
        this.allocationTimesString = allocationTimesString;
    }

    public String getAllocatedAmountString() {
        return allocatedAmountString;
    }

    public void setAllocatedAmountString(String allocatedAmountString) {
        this.allocatedAmountString = allocatedAmountString;
    }

    public String getDecrementAllocationTimeString() {
        return decrementAllocationTimeString;
    }

    public void setDecrementAllocationTimeString(String decrementAllocationTimeString) {
        this.decrementAllocationTimeString = decrementAllocationTimeString;
    }

    public String getRemainingAllocationTimeString() {
        return remainingAllocationTimeString;
    }

    public void setRemainingAllocationTimeString(String remainingAllocationTimeString) {
        this.remainingAllocationTimeString = remainingAllocationTimeString;
    }

    public String getDecrementAmountString() {
        return decrementAmountString;
    }

    public void setDecrementAmountString(String decrementAmountString) {
        this.decrementAmountString = decrementAmountString;
    }

    public String getRemainingAmountString() {
        return remainingAmountString;
    }

    public void setRemainingAmountString(String remainingAmountString) {
        this.remainingAmountString = remainingAmountString;
    }

    public String getTongSoKyPBString() {
        return tongSoKyPBString;
    }

    public void setTongSoKyPBString(String tongSoKyPBString) {
        this.tongSoKyPBString = tongSoKyPBString;
    }

    public String getGtPBHangKyString() {
        return gtPBHangKyString;
    }

    public void setGtPBHangKyString(String gtPBHangKyString) {
        this.gtPBHangKyString = gtPBHangKyString;
    }

    public String getSoKyDaPBString() {
        return soKyDaPBString;
    }

    public void setSoKyDaPBString(String soKyDaPBString) {
        this.soKyDaPBString = soKyDaPBString;
    }

    public String getSoKyDaPBConLaiString() {
        return soKyDaPBConLaiString;
    }

    public void setSoKyDaPBConLaiString(String soKyDaPBConLaiString) {
        this.soKyDaPBConLaiString = soKyDaPBConLaiString;
    }

    public String getGtDaPBString() {
        return gtDaPBString;
    }

    public void setGtDaPBString(String gtDaPBString) {
        this.gtDaPBString = gtDaPBString;
    }

    public String getGtConLaiString() {
        return gtConLaiString;
    }

    public void setGtConLaiString(String gtConLaiString) {
        this.gtConLaiString = gtConLaiString;
    }
}
