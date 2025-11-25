package org.globalds.mutantes.repository;

import org.globalds.mutantes.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    Optional<DnaRecord> findByDnaHash(String dnaHash);
    long countByIsMutant(boolean isMutant);
}
