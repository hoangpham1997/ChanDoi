package vn.softdreams.ebweb.repository;

import org.hibernate.exception.SQLGrammarException;

import java.util.List;
import java.util.UUID;

public interface BackupDataRepositoryCustom {
    List<String> backupDataString(UUID compayID);

    void restoreData(StringBuilder sqlRestore) throws SQLGrammarException;
}
