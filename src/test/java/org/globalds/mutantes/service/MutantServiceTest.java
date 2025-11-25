package org.globalds.mutantes.service;

import org.globalds.mutantes.entity.DnaRecord;
import org.globalds.mutantes.exception.DnaHashCalculationException;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector detector;

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private MutantService service;

    private final String[] DNA = {
            "ATGCGA","CAGTGC","TTATGT",
            "AGAAGG","CCCCTA","TCACTG"
    };

    @Test
    void testMutantSaved() {
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(DNA)).thenReturn(true);

        assertTrue(service.analyzeDna(DNA));
        verify(repository).save(any());
    }

    @Test
    void testHumanSaved() {
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(DNA)).thenReturn(false);

        assertFalse(service.analyzeDna(DNA));
        verify(repository).save(any());
    }

    @Test
    void testCached() {
        DnaRecord r = new DnaRecord();
        r.setMutant(true);

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(r));

        assertTrue(service.analyzeDna(DNA));
        verify(detector, never()).isMutant(any());
    }

    @Test
    void testHashLength() {
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(any())).thenReturn(true);

        service.analyzeDna(DNA);

        verify(repository).save(argThat(rec ->
                rec.getDnaHash() != null &&
                        rec.getDnaHash().length() == 64
        ));
    }

    @Test
    void testHashException() {
        MutantService spyService = Mockito.spy(service);

        doThrow(new DnaHashCalculationException("error"))
                .when(spyService).analyzeDna(DNA);

        assertThrows(DnaHashCalculationException.class,
                () -> spyService.analyzeDna(DNA));
    }
}
