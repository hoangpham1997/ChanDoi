package vn.softdreams.ebweb.service.impl;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.repository.GenCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing GenCode.
 */
@Service
@Transactional
public class GenCodeServiceImpl implements GenCodeService {

    private final Logger log = LoggerFactory.getLogger(GenCodeServiceImpl.class);

    private final GenCodeRepository genCodeRepository;
    private UserService userService;
    private OrganizationUnitRepository organizationUnitRepository;

    public GenCodeServiceImpl(GenCodeRepository genCodeRepository, UserService userService,
                              OrganizationUnitRepository organizationUnitRepository) {
        this.genCodeRepository = genCodeRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a genCode.
     *
     * @param genCode the entity to save
     * @return the persisted entity
     */
    @Override
    public GenCode save(GenCode genCode) {
        log.debug("Request to save GenCode : {}", genCode);
        return genCodeRepository.save(genCode);
    }

    /**
     * Get all the genCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GenCode> findAll(Pageable pageable) {
        log.debug("Request to get all GenCodes");
        return genCodeRepository.findAll(pageable);
    }


    /**
     * Get one genCode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GenCode> findOne(UUID id) {
        log.debug("Request to get GenCode : {}", id);
        return genCodeRepository.findById(id);
    }

    /**
     * Delete the genCode by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GenCode : {}", id);
        genCodeRepository.deleteById(id);
    }

    /**
     * Hautv
     *
     * @param typeGroupID
     * @return
     */
    @Override
    public GenCode findWithTypeID(int typeGroupID, int displayOnBook) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return genCodeRepository.findWithTypeID(typeGroupID, currentUserLoginAndOrg.get().getOrg(), UUID.randomUUID(), displayOnBook);
        }
        return null;
    }

    @Override
    public String getCodeVoucher(int typeGroupID, int displayOnBook) {
        GenCode genCode = findWithTypeID(typeGroupID, displayOnBook);
        String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(), '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
        return codeVoucher;
    }

    public List<GenCode> getAllGenCodeForSystemOption() {
        /*Optional<User> user = userService.getUserWithAuthorities();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();*/
        UserDTO userDTO = userService.getAccount();
//        String currentBook = organizationUnitRepository.findCurrentBook(user.get().getId(), currentUserLoginAndOrg.get().getOrg());
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
//        if (currentUserLoginAndOrg.isPresent()) {
        return genCodeRepository.getAllGenCodeForSystemOption(userDTO.getOrganizationUnit().getId(), phienSoLamViec.toString());
//        }
//        throw new BadRequestAlertException("", "", "");
    }
}
