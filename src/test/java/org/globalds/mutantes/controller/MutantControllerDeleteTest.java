package org.globalds.mutantes.controller;

import org.globalds.mutantes.service.MutantService;
import org.globalds.mutantes.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;        // ← agregado
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
class MutantControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    void deleteExistingRecord() throws Exception {
        when(mutantService.deleteByHash("abc123")).thenReturn(true);

        mockMvc.perform(delete("/mutant/abc123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNonExistingRecord() throws Exception {
        when(mutantService.deleteByHash("nope")).thenReturn(false);

        mockMvc.perform(delete("/mutant/nope"))
                .andExpect(status().isNotFound());
    }

    @Test
    void statsWithFilters() throws Exception {
        when(statsService.getStats("2025-01-01", "2025-01-07"))
                .thenReturn(new org.globalds.mutantes.dto.StatsResponse(4, 8, 0.5));

        mockMvc.perform(get("/stats?startDate=2025-01-01&endDate=2025-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(4))
                .andExpect(jsonPath("$.count_human_dna").value(8))
                .andExpect(jsonPath("$.ratio").value(0.5));
    }
}
