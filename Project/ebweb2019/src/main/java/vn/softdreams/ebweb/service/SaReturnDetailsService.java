package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.SaReturnDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDetailsDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing SaReturnDetails.
 */
public interface SaReturnDetailsService {

    /**
     * Get the "id" saReturnDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaReturnDetailsDTO findOne(UUID id);

    List<SaReturnDetailsRSInwardDTO> findAllDetailsById(List<UUID> id);
}
