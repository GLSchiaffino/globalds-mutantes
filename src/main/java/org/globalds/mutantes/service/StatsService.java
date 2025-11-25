package org.globalds.mutantes.service;

import lombok.RequiredArgsConstructor;
import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        long mutants = repository.countByIsMutant(true);
        long humans  = repository.countByIsMutant(false);

        double ratio = humans == 0 ? mutants : (double) mutants / humans;

        return new StatsResponse(mutants, humans, ratio);
    }
}
