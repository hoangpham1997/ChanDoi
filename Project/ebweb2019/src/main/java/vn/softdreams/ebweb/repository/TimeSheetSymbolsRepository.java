package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TimeSheetSymbols;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TimeSheetSymbols entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSheetSymbolsRepository extends JpaRepository<TimeSheetSymbols, UUID> {

}
