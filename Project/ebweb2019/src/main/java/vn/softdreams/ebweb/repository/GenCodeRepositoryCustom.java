package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.GenCode;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GenCode entity.
 */
public interface GenCodeRepositoryCustom {
    GenCode findWithTypeID(int typeID, UUID companyID, UUID branchID, int displayOnBook);

    List<GenCode> getAllGenCodeForSystemOption(UUID companyID, String currentBook);
}
