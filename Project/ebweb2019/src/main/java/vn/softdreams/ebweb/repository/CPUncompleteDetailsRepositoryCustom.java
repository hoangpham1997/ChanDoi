package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPUncompleteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPUncompleteDetailsRepositoryCustom {

    List<CPUncompleteDetails> getByCosetID(String collect, LocalDate toDate, UUID org);

}
