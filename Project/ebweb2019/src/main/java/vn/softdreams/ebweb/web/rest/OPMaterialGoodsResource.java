package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.OPMaterialGoods;
import vn.softdreams.ebweb.service.OPMaterialGoodsService;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.errors.FileSizeLimitExceededException;
import vn.softdreams.ebweb.web.rest.util.HeaderUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing OPMaterialGoods.
 */
@RestController
@RequestMapping("/api/op-material-goods")
public class OPMaterialGoodsResource {
    private final Logger log = LoggerFactory.getLogger(MaterialQuantumResource.class);


    private final OPMaterialGoodsService opMaterialGoodsService;

    public OPMaterialGoodsResource(OPMaterialGoodsService opMaterialGoodsService) {
        this.opMaterialGoodsService = opMaterialGoodsService;
    }

    @PostMapping("")
    @Timed
    public ResponseEntity<List<OPMaterialGoodsDTO>> createPporder(@RequestBody List<OPMaterialGoodsDTO> opMaterialGoods) throws URISyntaxException {
        List<OPMaterialGoodsDTO> result = opMaterialGoodsService.saveOPMaterialGoods(opMaterialGoods);
        return ResponseEntity.created(new URI("/api/op-material-goods/"))
                .body(result);
    }
    @DeleteMapping("")
    @Timed
    public ResponseEntity<UpdateDataDTO> deleteOPAccount(@RequestParam List<UUID> uuids) {
        UpdateDataDTO updateDataDTO = opMaterialGoodsService.deleteOPAccountByIds(uuids);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateDataDTO);
    }

    @GetMapping("find-all")
    @Timed
    public ResponseEntity<List<OPMaterialGoodsDTO>> getAllMaterialQuantums(@RequestParam String accountNumber,
                                                                           @RequestParam(required = false) UUID repositoryId) {
        log.debug("REST request to get a page of MaterialQuantums");
        List<OPMaterialGoodsDTO> opAccounts = opMaterialGoodsService.findAllByAccountNumber(accountNumber, repositoryId);
        return new ResponseEntity<>(opAccounts, HttpStatus.OK);
    }

    @GetMapping("check-have-data")
    @Timed
    public ResponseEntity<Boolean> getCheckHaveData() {
        log.debug("REST request to get a page of MaterialQuantums");
        Boolean checkHaveData = opMaterialGoodsService.getCheckHaveData();
        return new ResponseEntity<>(checkHaveData, HttpStatus.OK);
    }

    @GetMapping(value = "export/pdf")
    @Timed
    public ResponseEntity<byte[]> exportPdfAccountNormal(@RequestParam String accountNumber,
                                                         @RequestParam UUID repositoryId) {
        byte[] export = opMaterialGoodsService.exportPdf(accountNumber, repositoryId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadNormalAccounting(@RequestParam(required = false) MultipartFile file)
            throws IOException, FileSizeLimitExceededException, URISyntaxException {
        UpdateDataDTO updateDataDTO = opMaterialGoodsService.upload(file, null);
        HttpHeaders headers = new HttpHeaders();

        if (updateDataDTO.getMessage().equals("valid")) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.add("message", updateDataDTO.getMessage());
        headers.add("isError", updateDataDTO.getError() ? "1" : "0");
        if (updateDataDTO.getMessage().equals(Constants.ImportExcel.SELECT_SHEET)) {
            headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "upload/{sheetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadNormalAccountingAndSheetName(@RequestParam(required = false) MultipartFile file,
                                                                     @PathVariable("sheetName") String sheetName)
        throws IOException, FileSizeLimitExceededException, URISyntaxException {
        UpdateDataDTO updateDataDTO = opMaterialGoodsService.upload(file, sheetName);
        HttpHeaders headers = new HttpHeaders();

        if (updateDataDTO.getMessage().equals("valid")) {
            ObjectMapper jsonMapper = new ObjectMapper();
            return new ResponseEntity<>(jsonMapper.writeValueAsBytes(updateDataDTO.getResult()), headers, HttpStatus.OK);
        }
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("error", "error");
        headers.add("isError", updateDataDTO.getError() ? "1" : "0");
        headers.add("message", updateDataDTO.getMessage());
        return new ResponseEntity<>(updateDataDTO.getExcelFile(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "upload/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boolean> saveOPAccount(@RequestBody String opMaterialGoods) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Boolean result =  opMaterialGoodsService.acceptedOPMaterialGoods(Arrays.asList(jsonMapper.readValue(opMaterialGoods, OPMaterialGoods[].class)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
