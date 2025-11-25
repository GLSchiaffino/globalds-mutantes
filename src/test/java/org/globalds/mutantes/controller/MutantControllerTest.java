package org.globalds.mutantes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.globalds.mutantes.dto.DnaRequest;
import org.globalds.mutantes.dto.StatsResponse;
import org.globalds.mutantes.service.MutantService;
import org.globalds.mutantes.service.StatsService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    private static final String[] VALID_DNA = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
    };

    // ============================
    //  TESTS DE /mutant
    // ============================

    @Test
    void testMutant200() throws Exception {
        when(mutantService.analyzeDna(any())).thenReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new DnaRequest(VALID_DNA))))
                .andExpect(status().isOk());
    }

    @Test
    void testHuman403() throws Exception {
        when(mutantService.analyzeDna(any())).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new DnaRequest(VALID_DNA))))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNullDna400() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\": null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEmptyDnaArray400() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\": []}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidCharacter400() throws Exception {
        String[] invalid = { "ABCD", "ATGC", "AAAA", "CCCC" };
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new DnaRequest(invalid))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNotSquare400() throws Exception {
        String[] invalid = { "ATG", "CAGT", "TTAT" };
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new DnaRequest(invalid))))
                .andExpect(status().isBadRequest());
    }

    // ============================
    //  TEST DE /stats
    // ============================

    @Test
    void testStats200() throws Exception {
        when(statsService.getStats()).thenReturn(new StatsResponse(1, 1, 1.0));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk());
    }
}
