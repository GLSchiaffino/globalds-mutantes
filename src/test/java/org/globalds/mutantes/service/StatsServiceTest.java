package org.globalds.mutantes.service;

import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    @Test
    void testNormal() {
        DnaRecordRepository repo = mock(DnaRecordRepository.class);
        StatsService service = new StatsService(repo);

        when(repo.countByIsMutant(true)).thenReturn(40L);
        when(repo.countByIsMutant(false)).thenReturn(100L);

        StatsResponse r = service.getStats();

        assertEquals(40, r.getCount_mutant_dna());
        assertEquals(100, r.getCount_human_dna());
        assertEquals(0.4, r.getRatio());
    }

    @Test
    void testNoHumans() {
        DnaRecordRepository repo = mock(DnaRecordRepository.class);
        StatsService service = new StatsService(repo);

        when(repo.countByIsMutant(true)).thenReturn(10L);
        when(repo.countByIsMutant(false)).thenReturn(0L);

        StatsResponse r = service.getStats();

        assertEquals(10, r.getCount_mutant_dna());
        assertEquals(0, r.getCount_human_dna());
        assertEquals(10.0, r.getRatio());
    }

    @Test
    void testZero() {
        DnaRecordRepository repo = mock(DnaRecordRepository.class);
        StatsService service = new StatsService(repo);

        when(repo.countByIsMutant(true)).thenReturn(0L);
        when(repo.countByIsMutant(false)).thenReturn(0L);

        StatsResponse r = service.getStats();

        assertEquals(0, r.getRatio());
    }
}
