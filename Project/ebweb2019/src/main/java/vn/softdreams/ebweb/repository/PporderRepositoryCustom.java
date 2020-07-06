package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSearchDTO;

import java.util.List;
import java.util.UUID;

public interface PporderRepositoryCustom {

    PPOrder findByRowNum(PPOrderSearchDTO searchDTO, Integer rowNum);

    Page<PPOrder> searchAll(Pageable pageable, PPOrderSearchDTO searchDTO);

    Page<PPOrderDTO> findAllPPOrderDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate,
                                       Integer typeId, List<UUID> itemsSelected, String currency, UUID companyID);

    Integer findByRowNumByID(PPOrderSearchDTO searchDTO, UUID id);

    int checkReferences(UUID id);

    void updateReferences(List<UUID> deletedIDs);

    void updateReferencesByPPOrderID(UUID id);

    List<PPOrderDTO> findPPOrderDetailsDTO(UUID id);

    void multiDeletePPOrder(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildPPOrder(String tableChildName, List<UUID> uuidList);

    void updateReferencesByPPOrderListID(List<UUID> uuidList_dmh);

    void updateStatus(String uuids);
}
