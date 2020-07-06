package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseAllocationViewDTO;

import java.util.List;
import java.util.UUID;

public interface GOtherVoucherDetailExpenseAllocationRepositoryCustom {

    List<GOtherVoucherDetailExpenseAllocationViewDTO> findAllByGOtherVoucherViewID(UUID id);

    List<GOtherVoucherDetailExpenseAllocation> findAllByGOtherVoucherID(UUID id);
}
