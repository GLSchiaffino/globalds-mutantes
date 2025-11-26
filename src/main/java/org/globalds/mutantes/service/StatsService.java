package org.globalds.mutantes.service;

import lombok.RequiredArgsConstructor;
import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        // versión sin filtros (ya existente)
        long mutants = repository.countByIsMutant(true);
        long humans = repository.countByIsMutant(false);

        double ratio = humans == 0 ? mutants : (double) mutants / humans;

        return new StatsResponse(mutants, humans, ratio);
    }

    // Nueva versión con filtros
    public StatsResponse getStats(String startDate, String endDate) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.MIN;
        LocalDate end   = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.plusDays(1).atStartOfDay();

        long mutantCount = repository.countByIsMutantAndCreatedAtBetween(true, startDT, endDT);
        long humanCount = repository.countByIsMutantAndCreatedAtBetween(false, startDT, endDT);

        double ratio = (humanCount == 0) ? mutantCount : (double) mutantCount / humanCount;

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
