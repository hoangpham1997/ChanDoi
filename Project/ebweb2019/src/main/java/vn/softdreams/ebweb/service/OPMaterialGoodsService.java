package vn.softdreams.ebweb.service;

import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.OPMaterialGoods;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface OPMaterialGoodsService {
    List<OPMaterialGoodsDTO> findAllByAccountNumber(String accountNumber, UUID repositoryId);

    List<OPMaterialGoodsDTO> saveOPMaterialGoods(List<OPMaterialGoodsDTO> opMaterialGoods);

    byte[] exportPdf(String accountNumber, UUID repositoryId);

    UpdateDataDTO upload(MultipartFile file, String sheetName) throws IOException;

    UpdateDataDTO deleteOPAccountByIds(List<UUID> uuids);

    Boolean getCheckHaveData();

    Boolean acceptedOPMaterialGoods(List<OPMaterialGoods> opAccounts);
}
