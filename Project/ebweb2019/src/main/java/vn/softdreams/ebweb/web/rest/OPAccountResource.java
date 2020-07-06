package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.service.OPAccountService;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.UploadInvoiceDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
import vn.softdreams.ebweb.web.rest.errors.FileSizeLimitExceededException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing OPAccount.
 */
@RestController
@RequestMapping("/api/op-account")
public class OPAccountResource {
    private final Logger log = LoggerFactory.getLogger(OPAccountResource.class);

    private static final String ENTITY_NAME = "OPAccountResource";

    private final OPAccountService opAccountService;

    public OPAccountResource(OPAccountService opAccountService) {
        this.opAccountService = opAccountService;
    }

    @PostMapping("")
    @Timed
    public ResponseEntity<List<OPAccountDTO>> createOPAccount(@RequestBody List<OPAccountDTO> opAccount) throws URISyntaxException {
        List<OPAccountDTO> result = opAccountService.saveOPAccount(opAccount);
        return ResponseEntity.created(new URI("/api/op-account/"))
                .body(result);
    }

    @DeleteMapping("")
    @Timed
    public ResponseEntity<UpdateDataDTO> deleteOPAccount(@RequestParam List<UUID> uuids) {
        UpdateDataDTO updateDataDTO = opAccountService.deleteOPAccountByIds(uuids);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateDataDTO);
    }

    @GetMapping("find-all")
    @Timed
    public ResponseEntity<List<OPAccountDTO>> getAllMaterialQuantums(@RequestParam String accountNumber,
                                                                     @RequestParam(required = false) String currencyId) {
        List<OPAccountDTO> opAccounts = opAccountService.findAllByAccountNumber(accountNumber, currencyId);
        return new ResponseEntity<>(opAccounts, HttpStatus.OK);
    }

    @GetMapping("check-have-data")
    @Timed
    public ResponseEntity<Boolean> getCheckHaveData(@RequestParam(required = false) Integer typeId) {
        Boolean checkHaveData = opAccountService.getCheckHaveData(typeId);
        return new ResponseEntity<>(checkHaveData, HttpStatus.OK);
    }

    @PostMapping("check-reference-data")
    @Timed
    public ResponseEntity<UpdateDataDTO> getCheckReferenceData(@RequestBody List<UUID> uuid) {
        UpdateDataDTO checkReferenceData = opAccountService.checkReferenceData(uuid);
        return new ResponseEntity<>(checkReferenceData, HttpStatus.OK);
    }

    @GetMapping(value = "export/pdf/normal")
    @Timed
    public ResponseEntity<byte[]> exportPdfAccountNormal(@RequestParam String accountNumber,
                                            @RequestParam(required = false) String currencyId) {
        byte[] export = opAccountService.exportPdfAccountNormal(accountNumber, currencyId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @GetMapping(value = "export/pdf/accounting-object")
    @Timed
    public ResponseEntity<byte[]> exportPdfAccountingObject(@RequestParam String accountNumber,
                                                         @RequestParam(required = false) String currencyId) {
        byte[] export = opAccountService.exportPdfAccountingObject(accountNumber, currencyId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @PostMapping(value = "temp/download", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<byte[]> downloadFile(@RequestParam Integer type) throws IOException {
        byte[] fileContent = opAccountService.getFileTemp(type);
        return new ResponseEntity<>(fileContent, HttpStatus.OK);

    }

    @PostMapping(value = "upload/normal", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadNormalAccounting(@RequestParam(required = false) MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        try {
            UpdateDataDTO updateDataDTO = opAccountService.uploadNormal(file, null);
            if (updateDataDTO.getMessage().equals("valid")) {
                ObjectMapper jsonMapper = new ObjectMapper();
                return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
            }
            headers.add("isError", updateDataDTO.getError() ? "1" : "0");
            headers.add("message", updateDataDTO.getMessage());
            if (updateDataDTO.getMessage().equals(Constants.ImportExcel.SELECT_SHEET)) {
                headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
                ObjectMapper jsonMapper = new ObjectMapper();
                return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
            }
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.setContentDispositionFormData("error", "error");
            return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
        } catch (FileSizeLimitExceededException | IOException ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
    @PostMapping(value = "upload/normal/{sheetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadNormalAccountingAndSheetName(@RequestParam(required = false) MultipartFile file,
                                                                     @PathVariable("sheetName") String sheetName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            UpdateDataDTO updateDataDTO = opAccountService.uploadNormal(file, sheetName);
            if (updateDataDTO.getMessage().equals("valid")) {
                ObjectMapper jsonMapper = new ObjectMapper();
                return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
            }
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.setContentDispositionFormData("error", "error");
            headers.add("isError", updateDataDTO.getError() ? "1" : "0");
            headers.add("message", updateDataDTO.getMessage());
            return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
        } catch (FileSizeLimitExceededException | IOException ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    @PostMapping(value = "upload/accepted/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boolean> saveOPAccount(@RequestBody String opAccounts, @PathVariable("type") Integer type) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Boolean result =  opAccountService.acceptedOPAccount(Arrays.asList(jsonMapper.readValue(opAccounts, OPAccount[].class)), type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "upload/accounting-object", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadAccountingObject(@RequestParam(required = false) MultipartFile file)
            throws IOException, FileSizeLimitExceededException, URISyntaxException {
        UpdateDataDTO updateDataDTO = opAccountService.uploadAccountingObject(file, null);
        HttpHeaders headers = new HttpHeaders();
        if (updateDataDTO.getMessage().equals("valid")) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.add("isError", updateDataDTO.getError() ? "1" : "0");
        headers.add("message", updateDataDTO.getMessage());
        if (updateDataDTO.getMessage().equals(Constants.ImportExcel.SELECT_SHEET)) {
            headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
    }
    @PostMapping(value = "upload/accounting-object/{sheetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadAccountingObjectAndSheetName(@RequestParam(required = false) MultipartFile file,
                                                                     @PathVariable("sheetName") String sheetName)
            throws IOException, FileSizeLimitExceededException, URISyntaxException {
        UpdateDataDTO updateDataDTO = opAccountService.uploadAccountingObject(file, sheetName);
        HttpHeaders headers = new HttpHeaders();
        if (updateDataDTO.getMessage().equals("valid")) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.add("isError", updateDataDTO.getError() ? "1" : "0");
        headers.add("message", updateDataDTO.getMessage());
        if (updateDataDTO.getMessage().equals(Constants.ImportExcel.SELECT_SHEET)) {
            headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
    }
}
