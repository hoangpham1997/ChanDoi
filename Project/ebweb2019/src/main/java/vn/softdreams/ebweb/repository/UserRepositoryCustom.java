package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbUserGroup;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.EbLogHistoryDTO;
import vn.softdreams.ebweb.service.dto.EbUserGroupDTO;
import vn.softdreams.ebweb.service.dto.EbUserOrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.UserSearchDTO;

import java.util.List;
import java.util.UUID;

public interface UserRepositoryCustom {
    void insertUserEbGroup(List<EbUserGroupDTO> ebUserGroupDTOs);

    void insertEbUserOrganizationUnit(List<EbUserOrganizationUnitDTO> ebUserOrganizationUnitDTOs);

    void insertEbLogHistory(List<EbLogHistoryDTO> ebLogHistoryDTOS);

    Page<UserDTO> findAllByParentIDOrderByLogin(Pageable pageable, User user);

    Page<UserDTO> getListUserSearch(Pageable pageable, UserSearchDTO userSearchDTO);

    Page<User> findAllClientByParentIDOrderByLogin(Pageable pageable, User user);
}
