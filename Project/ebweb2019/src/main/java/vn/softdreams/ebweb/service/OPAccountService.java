package vn.softdreams.ebweb.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.UploadInvoiceDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface OPAccountService {
    List<OPAccountDTO> findAllByAccountNumber(String accountNumber, String currencyId);

    List<OPAccountDTO> saveOPAccount(List<OPAccountDTO> opAccount);

    UpdateDataDTO deleteOPAccountByIds(List<UUID> uuids);

    byte[] exportPdfAccountNormal(String accountNumber, String currencyId);

    byte[] exportPdfAccountingObject(String accountNumber, String currencyId);

    byte[] getFileTemp(Integer type);

    UpdateDataDTO uploadNormal(MultipartFile file, String sheetName) throws IOException;

    UpdateDataDTO uploadAccountingObject(MultipartFile file, String sheetName) throws IOException;

    Boolean getCheckHaveData(Integer typeId);

    Boolean acceptedOPAccount(List<OPAccount> opAccounts, Integer type);

    UpdateDataDTO checkReferenceData(List<UUID> uuid);
}
