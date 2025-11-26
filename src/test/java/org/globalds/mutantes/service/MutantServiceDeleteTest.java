package org.globalds.mutantes.service;

import org.globalds.mutantes.entity.DnaRecord;
import org.globalds.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MutantServiceDeleteTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private MutantService service;

    public MutantServiceDeleteTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteExistingHash() {
        DnaRecord record = new DnaRecord();
        record.setDnaHash("abc123");

        when(repository.findByDnaHash("abc123")).thenReturn(Optional.of(record));

        boolean result = service.deleteByHash("abc123");

        assertTrue(result);
        verify(repository, times(1)).delete(record);
    }

    @Test
    void testDeleteNonExistingHash() {
        when(repository.findByDnaHash("nope")).thenReturn(Optional.empty());

        boolean result = service.deleteByHash("nope");

        assertFalse(result);
        verify(repository, never()).delete(any());
    }
}
