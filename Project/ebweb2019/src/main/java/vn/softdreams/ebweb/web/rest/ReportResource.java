package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.service.ReportBusinessService;
import vn.softdreams.ebweb.service.ReportService;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ReportResource {

    private final ReportService reportService;
    private final ReportBusinessService reportBusinessService;

    public ReportResource(
        ReportService reportService,
        ReportBusinessService reportBusinessService
    ) {
        this.reportService = reportService;
        this.reportBusinessService = reportBusinessService;
    }

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author hieugie
     * <p>
     * Hàm tạo biểu mẫu
     * Edit Hautv
     */
    @GetMapping("/report/pdf")
    @Timed
    public ResponseEntity<byte[]> getReportPdf(@RequestParam(required = false) UUID id,
                                               @RequestParam(required = false) int typeID,
                                               @RequestParam(required = false) int typeReport) throws JRException, InvocationTargetException, IllegalAccessException {
        String name = "Name";
        switch (typeID) {
            case Constants.MCReceipt.TYPE_PHIEU_THU:
                name = "PHIEU-THU.pdf";
                break;
            case Constants.MCPayment.TYPE_PHIEU_CHI:
                name = "PHIEU-CHI.pdf";
                break;
            case 120:
            case 126:
            case 130:
            case 140:
                name = "MBTellerPaper.pdf";
                break;
            case 160:
                name = "MBDeposit.pdf";
                break;
            case 300:
                name = "SaQuote.pdf";
                break;
            case 220:
            case 230:
                name = "PPDiscountReturn.pdf";
                break;
            case TypeConstant.DON_DAT_HANG:
                name = "SAOrder.pdf";
                break;
            case TypeConstant.KIEM_KE_QUY:
                name = "MCAudit.pdf";
                break;
            case TypeConstant.BAN_HANG_CHUA_THU_TIEN:
            case TypeConstant.BAN_HANG_THU_TIEN_NGAY_CK:
            case TypeConstant.BAN_HANG_THU_TIEN_NGAY_TM:
                name = "SAInvoice.pdf";
                break;
            case TypeConstant.HANG_BAN_TRA_LAI:
            case TypeConstant.HANG_GIAM_GIA:
                name = "SAReturn.pdf";
                break;
            case TypeConstant
                .MUA_HANG_QUA_KHO:
                name = "PPInvoice.pdf";
                break;
            case TypeConstant.DON_MUA_HANG:
                name = "PPOrder.pdf";
                break;
            case TypeConstant.XUAT_KHO:
                name = "Xuat-kho.pdf";
                break;
            case TypeConstant.XUAT_HOA_DON:
            case TypeConstant.XUAT_HOA_DON_TRA_LAI:
                name = "VATInvoice.pdf";
                break;
            case TypeConstant
                .NHAP_KHO:
                name = "RSInwardOutward.pdf";
            case TypeConstant
                .PP_SERVICE:
                name = "PPService.pdf";
            case TypeConstant.THONG_BAO_PHAT_HANH_HDDT:
                name = "Thong_bao_phat_hanh_hddt.pdf";
                break;
            case TypeConstant.KET_CHUYEN_LAI_LO:
                name = "GOtherVoucher.pdf";
                break;
            case TypeConstant.PHAN_BO_CHI_PHI_TRA_TRUOC:
                name = "Phan_bo_chi_phi_tra_Truoc.pdf";
                break;

            case TypeConstant.GHI_GIAM_CCDC:
                name = "Ghi_giam_CCDC.pdf";
                break;
            case TypeConstant.DIEU_CHINH_CCDC:
                name = "Dieu_Chinh_CCDC.pdf";
                break;
            case TypeConstant.DIEU_CHUYEN_CCDC:
                name = "Dieu_Chuyen_CCDC.pdf";
                break;

            case TypeConstant.CHUYEN_KHO:
                name = "RSTransfer.pdf";
            case TypeConstant.KIEM_KE_CCDC:
                name = "Kiem_ke_CCDC.pdf";
                break;
            case TypeConstant.PHAN_BO_CCDC:
                name = "Phan_bo_CCDC.pdf";
                break;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        try {
            byte[] bytes = reportService.getReport(id, typeID, typeReport);
            headers.setContentDispositionFormData(name, name);
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author Hautv
     * <p>
     * Hàm get Báo cáo
     */
    @PostMapping("/business/report")
    @Timed
    public ResponseEntity<byte[]> getReportBusiness(@RequestBody RequestReport requestReport) throws JRException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        try {
            byte[] bytes = reportBusinessService.getReportBusiness(requestReport);
            headers.setContentDispositionFormData("name", "name");
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/business/instruction")
    @Timed
    public ResponseEntity<byte[]> getInstruction() throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        try {
            byte[] bytes = reportBusinessService.getInstructionPDF();
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author hieugie
     * <p>
     * Hàm tạo báo cáo excel demo.
     */
    @GetMapping("/report/excel")
    @Timed
    public ResponseEntity<byte[]> getReportExcel() {

        byte[] result = reportService.getReportExcel();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("unit.xls", "unit.xls");
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author Chuongnv
     * <p>
     * Hàm get Báo cáo
     */
    @PostMapping("/business/report/excel")
    @Timed
    public ResponseEntity<byte[]> getExcelReportBusiness(@RequestBody RequestReport requestReport) throws JRException, IOException {
        File currentDirectory = new File(new File("").getAbsolutePath());
        File reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/bao-cao-excel/" + requestReport.getFileName());
        byte[] fileContent = reportBusinessService.getExcelReportBusiness(requestReport, currentDirectory.getAbsolutePath() + "/report/bao-cao-excel/" + requestReport.getFileName());
        return new ResponseEntity<>(fileContent, HttpStatus.OK);
    }
}
