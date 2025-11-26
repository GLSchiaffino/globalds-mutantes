package org.globalds.mutantes.service;

import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatsServiceWithFilterTest {

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private StatsService service;

    public StatsServiceWithFilterTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStatsWithDateRange() {

        when(repository.countByIsMutantAndCreatedAtBetween(eq(true), any(), any()))
                .thenReturn(5L);

        when(repository.countByIsMutantAndCreatedAtBetween(eq(false), any(), any()))
                .thenReturn(10L);

        StatsResponse response = service.getStats("2025-01-01", "2025-01-10");

        assertEquals(5, response.getCount_mutant_dna());
        assertEquals(10, response.getCount_human_dna());
        assertEquals(0.5, response.getRatio());
    }

    @Test
    void testStatsWithoutDates() {

        when(repository.countByIsMutantAndCreatedAtBetween(eq(true), any(), any()))
                .thenReturn(3L);

        when(repository.countByIsMutantAndCreatedAtBetween(eq(false), any(), any()))
                .thenReturn(3L);

        StatsResponse response = service.getStats(null, null);

        assertEquals(3, response.getCount_mutant_dna());
        assertEquals(3, response.getCount_human_dna());
        assertEquals(1.0, response.getRatio());
    }
}
