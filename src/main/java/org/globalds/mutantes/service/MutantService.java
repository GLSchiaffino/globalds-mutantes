package org.globalds.mutantes.service;

import lombok.RequiredArgsConstructor;
import org.globalds.mutantes.entity.DnaRecord;
import org.globalds.mutantes.exception.DnaHashCalculationException;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;

    public boolean analyzeDna(String[] dna) {
        String hash = sha256(dna);

        return repository.findByDnaHash(hash)
                .map(DnaRecord::isMutant)
                .orElseGet(() -> {
                    boolean mutant = mutantDetector.isMutant(dna);

                    DnaRecord record = new DnaRecord();
                    record.setDnaHash(hash);
                    record.setMutant(mutant);
                    repository.save(record);

                    return mutant;
                });
    }

    private String sha256(String[] dna) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            for (String s : dna) md.update(s.getBytes());
            return HexFormat.of().formatHex(md.digest());
        } catch (Exception e) {
            throw new DnaHashCalculationException("Error calculating DNA hash", e);
        }
    }
}
