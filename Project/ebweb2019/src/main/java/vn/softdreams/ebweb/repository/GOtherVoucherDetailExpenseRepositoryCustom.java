package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseViewDTO;

import java.util.List;
import java.util.UUID;

public interface GOtherVoucherDetailExpenseRepositoryCustom {

    List<GOtherVoucherDetailExpenseViewDTO> findAllByGOtherVoucherViewID(UUID id);
}
