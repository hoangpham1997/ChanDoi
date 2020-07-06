package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.EbAuthority;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.EbAuthorityRepository;
import vn.softdreams.ebweb.repository.EbGroupRepository;
import vn.softdreams.ebweb.repository.UserRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EbAuthorityService;
import vn.softdreams.ebweb.service.EbGroupService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.AuthorityTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.EbGroupSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeTableDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EbAuthorityServiceImpl implements EbAuthorityService {
    //    private final Logger log = LoggerFactory.getLogger(EbGroupServiceImpl.class);
    private final EbAuthorityRepository ebAuthorityRepository;
    private final UserService userService;

    public EbAuthorityServiceImpl(EbAuthorityRepository ebAuthorityRepository, UserService userService) {
        this.ebAuthorityRepository = ebAuthorityRepository;
        this.userService = userService;
    }

    @Override
    public EbAuthority save(EbAuthority ebAuthority) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return ebAuthorityRepository.save(ebAuthority);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EbAuthority> findAll(Pageable pageable) {
        return ebAuthorityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EbAuthority> findAll() {
        return ebAuthorityRepository.findAllEbAuthority();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EbAuthority> findOne(UUID id) {
        return ebAuthorityRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        ebAuthorityRepository.deleteById(id);
    }

    @Override
    public Optional<EbAuthority> findOneById(UUID id) {
        return ebAuthorityRepository.findOneById(id);
    }

    @Override
    public List<AuthorityTreeDTO> findAllAuthorityTree() {
        List<AuthorityTreeDTO> authorityAll = new ArrayList<>();
        List<AuthorityTreeDTO> authorities = new ArrayList<>();
        List<EbAuthority> authors = ebAuthorityRepository.findAllEbAuthorityOrderByCode();
        // check demo pack
        UserDTO user = userService.getAccount();
        boolean isDemoPack = user.getEbPackage().getPackageCode().toLowerCase().contains("demo");
        if (isDemoPack) {
            // remove quyen ket xuat bao cao
            List<EbAuthority> exportAuthorities = authors.stream().filter(item ->
                item.getCode().startsWith("17") && item.getCode().endsWith("07") && item.getCode().length() == 6)
                .collect(Collectors.toList());
            authors.removeAll(exportAuthorities);
        }
        for (EbAuthority auth : authors) {
            AuthorityTreeDTO auObj = new AuthorityTreeDTO();
            auObj.setId(auth.getId());
            auObj.setCode(auth.getCode());
            auObj.setName(auth.getName());
            auObj.setParentCode(auth.getParentCode());
            authorityAll.add(auObj);
        }
        for (AuthorityTreeDTO auth : authorityAll) {
            if (auth.getParentCode() != null && auth.getParentCode().equals("")) {
                authorities.add(auth);
            }
        }
        return getAuthorityTree(authorities, authorityAll);
    }

    private List<AuthorityTreeDTO> getAuthorityTree(List<AuthorityTreeDTO> authorities, List<AuthorityTreeDTO> authorityAll) {
        for (AuthorityTreeDTO authParent : authorities) {
            List<AuthorityTreeDTO> authorityChild = new ArrayList<>();
            for (AuthorityTreeDTO auth : authorityAll) {
                if (auth.getParentCode() != null && auth.getParentCode().equals(authParent.getCode())) {
                    authorityChild.add(auth);
                }
            }
            authParent.setChildren(authorityChild);
            if (authorityChild.size() > 0) {
                getAuthorityTree(authorityChild, authorityAll);
            }
        }
        return authorities;
    }

}
